package ordo;

import map.*;
import formats.*;
import config.*;
import java.util.concurrent.Semaphore;
import java.net.MalformedURLException;
import java.rmi.*;
import hdfs.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Job implements JobInterfaceX {

    private Format.Type inFormat;
    private Format.Type outFormat;
    private String inFName;
    private String outFName;
    private int nbReduces;
    private int nbMaps;
    private SortComparator sortComp;

    public Job() {
        this.inFName = "";
        this.outFName = "";
        this.inFormat = Format.Type.LINE;
        this.outFormat = Format.Type.KV;
    }

    public Job(String infname, String outfname, Format.Type inForm) {
        this.inFName = infname;
        this.outFName = outfname;
        this.inFormat = inForm;
        this.outFormat = Format.Type.KV;
    }

    public void startJob(MapReduce mr) {

        if (this.outFName.equals("")) { // si aucun nom pour le fichier de sortie n'à été donné, on met un nom par défaut
            this.outFName = this.inFName + "-KVres";
        }

        CallBackImpl[] cb = new CallBackImpl[Project.HOSTS.length]; // 1 CallBack par hosts
        boolean[] wait = new boolean[Project.HOSTS.length]; // indique si le ième host doit être attendu ou non (càd s'il doit renvoyer un CallBack)
        for (int i = 0; i < Project.HOSTS.length; i++) { // initialisation des CallBack
            try {
                cb[i] = new CallBackImpl(new Semaphore(0)); // CallBack = Semaphore initialisé à 0 pour que startJob se bloque si CallBack non reçu
                wait[i] = false;
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }

        Format reader; // pour lire les fragments
        Format writer; // pour écrire le résultat du traitement des fragments

        HashMap<String, ArrayList<String>> maps; // pour stocker la liste des fragments sur chaque host
        try {            
            maps = ((FragmentList) Naming.lookup("//" + Project.NAMINGNODE + "/list")).getFragments();
            // on récupère les noms des fragments pour chaque hosts sur le registry du NamingNode
        } catch (Exception e) {
            throw new RuntimeException("liste des fragments introuvable");
        }

        for (int j = 0; j < maxLength(maps); j++) { // une boucle par fragment (1 fragment traité sur chaque host)
            for (int i = 0; i < Project.HOSTS.length; i++) { // une boucle par host

                if (maps.get(Project.HOSTS[i]).size() >= j) { // si il reste un fragment non traité sur l'host

                    try {
                        switch (this.inFormat) { // initialisation du reader à partir du nom récupérer depuis NamingNode
                            case LINE :
                                reader = new LineFormat(maps.get(Project.HOSTS[i]).get(j));
                                break;
                            case KV :
                                reader = new KVFormat(maps.get(Project.HOSTS[i]).get(j));
                                break;
                            default :
                                reader = new LineFormat(maps.get(Project.HOSTS[i]).get(j));
                        }
                        
                        switch (this.outFormat) { // initialisation du writer à partir du nom récupérer depuis NamingNode suivi de -res pour le différencier d'un fragment non traité
                            case LINE :
                                writer = new LineFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                            case KV :
                                writer = new KVFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                            default :
                                writer = new KVFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                        }

                        // création de copies constantes des variables pour le lancement du runMap dans un thread (pour éviter blocage)
                        final int num = i;
                        final MapReduce mapRed = mr;
                        final Format read = reader;
                        final Format write = writer;
                        final CallBackImpl[] caba = cb;
                        new Thread(() -> {
                                try {
                                    ((DeamonImpl) Naming.lookup("//" + Project.HOSTS[num] + ":" + Project.HOSTSPORT[num] + "/Daemon")).runMap(mapRed, read, write, caba[num]); // on récupère le ième Daemon et on lance le map
                                } catch (NotBoundException | MalformedURLException | RemoteException e) {
                                    throw new RuntimeException(e.getMessage());
                                }
                            }).start(); // lancement du thread

                        wait[i] = true; // un runMap à été lancé, il faudra attendre son CallBack

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            
            for (int i = 0; i < Project.HOSTS.length; i++) { // on attend le CallBack de chaque host ayant sur lesquels un runMap à été lancé
                if (wait[i]) { // si le CallBack de cet host est attendu
                    try {
                        cb[i].mapFini.acquire(); // si le CallBack a déja été reçu, on passe, sinon on l'attend
                        wait[i] = false; // aucuns runMap ne tourne désormais sur cet host, on ne cherchera pas à l'attendre tant qu'aucun autre runMap n'aura été lancé dessus
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        /* appel du hdfsread ? */
        HdfsClient.HdfsRead(this.inFName, this.inFName + "-res"); // en supposant que l'appel static soit possible

        HdfsClient.Delete(this.inFName); // suppréssion des fragments désormais inutiles sur les serveurs

        switch (this.outFormat) { // initialisation du reader pour le fichier résultant des traitement et du writer pour le fichier de sortie de Hidoop
            case LINE :
                reader = new LineFormat(this.inFName + "-res");
                writer = new LineFormat(this.outFName);
                break;
            case KV :
                reader = new KVFormat(this.inFName + "-res");
                writer = new KVFormat(this.outFName);
                break;
            default :
                reader = new KVFormat(this.inFName + "-res");
                writer = new KVFormat(this.outFName);
                break;
        }

        mr.reduce(reader, writer); // Traitement du fichier résultant des traitements des fragments

        // TODO : suppression du fichier obtenu par hdfsread (pré-reduce)
    }

    private static int maxLength(HashMap<String, ArrayList<String>> map){
        int max = 0;
        for (int i = 0; i < Project.HOSTS.length; i++) {
            if (max < map.get(Project.HOSTS[i]).size()) {
                max = map.get(Project.HOSTS[i]).size();
            }
        }
        return max;
    }


    public void setInputFormat(Format.Type ft) {
        this.inFormat = ft;
    }

    public Format.Type getInputFormat() {
        return this.inFormat;
    }

    public void setInputFname(String fname) {
        this.inFName = fname;
    }

    public String getInputFname() {
        return this.inFName;
    }

    public void setOutputFname(String fname) {
        this.outFName = fname;
    }

    public String getOutputFname() {
        return this.outFName;
    }

    public void setOutputFormat(Format.Type ft) {
        this.outFormat = ft;
    }

    public Format.Type getOutputFormat() {
        return this.outFormat;
    }

    public void setNumberOfReduces(int tasks) {
        this.nbReduces = tasks;
    }

    public int getNumberOfReduces() {
        return this.nbReduces;
    }

    public void setNumberOfMaps(int tasks) {
        this.nbMaps = tasks;
    }

    public int getNumberOfMaps() {
        return this.nbMaps;
    }

    public void setSortComparator(SortComparator sc) {
        this.sortComp = sc;
    }

    public SortComparator getSortComparator() {
        return this.sortComp;
    }
}