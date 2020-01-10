package hdfs;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HdfsClientDelete  extends Thread {
    //TODO connection + commande
    String nom ;
    public HdfsClientDelete( String nomFichier){
        nom = nomFichier ;

    }

    public void run(){
    //TODO
        //on demande au rmi les serveur qui possede un fragment du fichier
        FragmentList listeNamingNode  = (FragmentList) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        List<String> listeServeur = conversion(listeNamingNode);
        //TODO format de la liste
        //on se connecte au serveurs
        for( String serv : listeServeur ){
            String[] infoServ = serv.split(":") ;
            Socket s = null;
            try {
                s = new Socket(infoServ[0]  ,Integer.parseInt(infoServ[1]) ) ;
            } catch (IOException e) {
                System.out.println( "connexion impossible au serveur") ;
            }
            //on envoie la cmd
            String fName = nom + ".part" + listeServeur.indexOf(serv); // le nom du fragment que l'on supprime à cette itération
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
                Naming.rebind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", ((FragmentList) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISRTYPORT + "/list")).removeFragment(serv, fName));
            }
            else{
                
                System.out.println("le fichier n'as pas pus etre supprimer") ; 
            }

        }

        private List<String> conversion(FragmentList frags) {
            Boolean trouve = true;
            List<String> list = new ArrayList<String>();
            int i = 0;
            while (trouve) {
                trouve = false;
                for (String host : frags.keySet()) {
                    if (!trouve) && (frags.get(host).containsValue(nomFichier + ".part" + i)) {
                        list.add(host);
                        trouve = true;
                    }
                }
                i++;
            }
            return list;
        }

    }

}