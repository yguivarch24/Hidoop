package pageRank;

import config.FragmentListInter;
import config.Project;
import formats.Format;
import formats.KV;
import hdfs.HdfsClient;
import map.MapReduceImpl;
import ordo.FormatInconnuException;
import ordo.Job;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class AppPageRank {

    public static void main(String[] args) throws IOException, NotBoundException {

        String inName = "";
        String outName = "";
        Format.Type fileType = Format.Type.LINE;

        FragmentListInter liste = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        Project.setHOSTS(liste.getHostArray());
        System.out.println(""+Arrays.toString(Project.HOSTS));
        Project.setHOSTSPORT(liste.getPortArray());

        int nbArgs = args.length;
        switch (nbArgs) {
            case 4 :
                try {
                    fileType = toFormat(args[3]);
                } catch (FormatInconnuException e) {
                    System.out.println(e.getMessage());
                    System.exit(2);
                }
            case 3 :
                outName = args[2];
            case 2 :
                inName = args[1];
                Project.setNamingnode(args[0]);
                break;
            default :
                System.out.println("arguments :  NomNamingNode NomFichierIn  NomFichierOut  TypeFichier");
                System.out.println("    NomNamingNode : le nom de la machine host du naming node suivie de son port (exemple -> salameche:12344)");
                System.out.println("    NomFichierIn : le nom du fichier d'entrée (obligatoire)");
                System.out.println("    NomFichierOut : le nom du fichier de sortie (optionnel)");
                System.out.println("    TypeFichier : le type du fichier d'entrée (optionnel, nécessite un nom de fichier de sortie et un nombre de fragment)");
                System.exit(1);
        }
        if (outName.equals("")) {outName = inName + "-KVres";}

        ArrayList<String > pageVisitees = new ArrayList<String>() ;
        ArrayList<String> pile = new ArrayList<>() ;
        pile.add(inName) ;
        do {
            LineFormatKV prochainesPages = new LineFormatKV("prochaine.page");
            prochainesPages.open(Format.OpenMode.W);
            for(String e : pile){
                prochainesPages.write(new KV(e,e));
            }

            prochainesPages.close();
            /* envoie des pages*/
            HdfsClient.HdfsWrite(fileType, inName);

            /* Lancement du traitement */
            try {
                long startTime = System.currentTimeMillis();
                new Job1(inName, outName, fileType).startJob(new BranImpl());
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Temps du Job : " + totalTime);
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
            }


         //on extrait les pages que l'on vien de visité
           LineFormatKV sortie = new LineFormatKV(outName);
            ((LineFormatKV) sortie).open(Format.OpenMode.R);
            KV k ;
            //on regarde les pages nouvelle qui vienne d'etre visité et on met à jours les array
         do{
             k = sortie.read() ;
             if(! pile.contains(k.v )  && !pageVisitees.contains(k.v )){
                 pile.add(k.v) ;
             }
             if(!pageVisitees.contains(k.k ) ){
                 pageVisitees.add(k.k);
             }

         }while(  k != null  );
         ((LineFormatKV) sortie).close();

        }while( ! pile.isEmpty()) ;


        System.out.println("---Construction Graph Fini---");

        System.out.println("---PageRank Fini---");
        System.exit(0);
    }

    private static Format.Type toFormat(String str) throws FormatInconnuException {
        switch (str) {
            case "Line" :
                return Format.Type.LINE;
            case "KV" :
                return Format.Type.KV;
            default :
                throw new FormatInconnuException("Format de fichier inconnu");
                
        }
    }
}