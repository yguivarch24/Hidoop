package hdfs;

import config.FragmentList;
import config.FragmentListInter;
import config.Project;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HdfsClientDelete  extends Thread {
    //TODO connection + commande
    private String nom ;
    private FragmentListInter listeNamingNode ;
    HdfsClientDelete(String nomFichier)  {
        nom = nomFichier ;
        try {
            listeNamingNode = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void run(){
    //TODO
        //on demande au rmi les serveur qui possede un fragment du fichier

        List<String> listeServeur = null;
        try {
            assert listeNamingNode != null;
            listeServeur = conversion(listeNamingNode.getFragments());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println("fin conversion ");
        //System.out.println("debut de la supression ");
        //TODO format de la liste
        //on se connecte au serveurs
        for( int i = 0 ; i<listeServeur.size() ; i++ ){
            String serv = listeServeur.get(i) ;
            String[] infoServ = serv.split(":") ;

            //initialisation des commandes
            String fName = nom + ".part" + i; // le nom du fragment que l'on supprime à cette itération
            String fNameRes = fName + "-res";
            String cmd ="delete/@/" + fName;
            try {

                //initialisation du socket
                Socket s = new Socket(infoServ[0]  ,Integer.parseInt(infoServ[1]) ) ;
                InputStream input = s.getInputStream();
                OutputStream output = s.getOutputStream();
                try {
                    //envoie première commande
                    output.write(cmd.getBytes());
                } catch (IOException e) {
                    System.out.println("erreur envoie cmd");
                }

                try {
                    byte[] buffer = new byte[16];
                    Arrays.fill(buffer, (byte) '\0');
                    int byteread = input.read(buffer);
                    String Sbuffer = Byte2String(buffer,byteread);
                    //TODO : Bien lire les buffer avec une boucle while
                    /*while(byteread > 0) {
                        byteread = input.read(buffer);
                        Sbuffer = Sbuffer + new String(buffer);
                    }*/
                    //System.out.println(Sbuffer);
                    switch (Sbuffer) {
                        case "ok":
                            //System.out.println("les fichiers part et part-res ont bien ete supprimes");
                            try {
                                listeNamingNode.removeFragment(serv, fName);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "ok1":
                            try {
                                listeNamingNode.removeFragment(serv, fName);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            //System.out.println("le fichier part a ete supprime");
                            break;
                        case "ok2":
                            //System.out.println("le fichier part-res a ete supprime");
                            break;
                        default:
                            //System.out.println("les fichiers n'ont pas pu etre supprime");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                System.out.println( "connexion impossible au serveur") ;
            }

        }
        //System.out.println("fin supression");
    }

    private String Byte2String(byte[] b, int taille) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < taille; i++) {
            s.append((char) b[i]);
        }
        return s.toString();
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