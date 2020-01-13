package hdfs;

import config.FragmentList;
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

public class HdfsClientDelete  extends Thread {
    //TODO connection + commande
    String nom ;
    FragmentListInter listeNamingNode ;
    public HdfsClientDelete( String nomFichier)  {
        nom = nomFichier ;
        try {
            listeNamingNode = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
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
        System.out.println("fin conversion ");
        System.out.println("debut de la supression ");
        //TODO format de la liste
        //on se connecte au serveurs
        for( int i = 0 ; i<listeServeur.size() ; i++ ){
            String serv = listeServeur.get(i) ;
            String[] infoServ = serv.split(":") ;
            Socket s = null;
            try {
                s = new Socket(infoServ[0]  ,Integer.parseInt(infoServ[1]) ) ;
            } catch (IOException e) {
                System.out.println( "connexion impossible au serveur") ;
            }
            //on envoie la cmd
            String fName = nom + ".part" + i; // le nom du fragment que l'on supprime à cette itération
            String cmd ="delete/@/" + fName;



            InputStream input = null;
            OutputStream output = null ;
            try {
                input = s.getInputStream();
                output = s.getOutputStream();
                output.write(cmd.getBytes());
            } catch (IOException e) {
                System.out.println("erreur envoie cmd");
            }
            while(true){
                try {
                    if (!(input.available() == 0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            String Sbuffer = null;
            try {
                 Sbuffer = new String(input.readNBytes(  input.available() ))  ;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if( Sbuffer.equals( "ok")){
                
                System.out.println("le fichier est bien supprimer");


                try {
                    listeNamingNode.removeFragment(serv, fName);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
            else{
                
                System.out.println("le fichier n'as pas pus etre supprimer") ; 
            }

        }
        System.out.println("fin supression");
    }

    private List<String> conversion(HashMap<String, ArrayList<String>> frags) throws RemoteException { // conversion d'une fragmentList en une liste telle que le ième fragment se trouve sur le ième host de la liste
        boolean trouve = true;
        List<String> list = new ArrayList<String>();
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