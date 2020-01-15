package hdfs;

import config.FragmentListInter;
import config.Project;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HdfsClientRead {

    private String nom  ;
    private FileOutputStream fos ;
    private FragmentListInter listeNamingNode ;

    HdfsClientRead(String nomFichier, String PositionLocal){
        nom = nomFichier ;
        File fichier = new File(PositionLocal);
        try {
            fos = new FileOutputStream(fichier);
        } catch (FileNotFoundException e) {
            System.out.println("probleme avec le fichier ");
        }
        //gestion du rmi
        try {
            listeNamingNode = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

    }
    public void read() {


        //on demande au rmi où sont stocké les fragment du fichier
        List<String> listeServeur = null;
        try {
            listeServeur = conversion(listeNamingNode.getFragments());
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        //on les telecharge ensuite dens l'ordre
        for( int i = 0 ; i<listeServeur.size() ; i++ ) {
            String serv = listeServeur.get(i);
            String[] infoServ = serv.split(":");
            //on se connecte au serveur
            Socket s = null;
            try {
                s = new Socket(infoServ[0], Integer.parseInt(infoServ[1]));
            } catch (IOException e) {
                System.out.println("connexion impossible au serveur");
            }
            //on envoie la cmd
            String cmd = "read/@/" + nom + ".part" + i + "-res";

            InputStream input = null;
            OutputStream output;
            try {
                input = s.getInputStream();
                output = s.getOutputStream();
                output.write(cmd.getBytes());
            } catch (IOException e) {
                System.out.println("erreur envoie cmd");
            }
            while (true) {
                try {
                    if (!(input.available() == 0)) {
                        byte[] buffer = input.readNBytes(input.available());
                        // System.out.println(new String(buffer));
                        fos.write(buffer);

                        //TODO maj du rmi
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }


            //on l'ecrit au bonne enroit dans le fichier

            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> conversion(HashMap<String, ArrayList<String>> frags) { // conversion d'une fragmentList en une liste telle que le ième fragment se trouve sur le ième host de la liste
        boolean trouve = true;
        List<String> list = new ArrayList<>();
        int i = 0;
        while (trouve) { // pour chaque fragments, on parcours la liste de fragment de chaque host... pas très optimal...
            trouve = false;
            for (String host : frags.keySet()) {
                if ((!trouve) && (frags.get(host).contains(nom + ".part" + i))) {
                    list.add(host);
                    trouve = true;
                }
            }
            i++;
        }
        return list;
    }
}