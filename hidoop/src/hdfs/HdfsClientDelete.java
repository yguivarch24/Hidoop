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
        List<String> listeServeur  = new ArrayList<>();
        //TODO demande au rmi
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
            String cmd ="delete/@/"+ nom+".part"+listeServeur.indexOf(serv) ;
            
            InputStream input = null;
            OutputStream output = null ;
            try {
                input  = s.getInputStream();
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
                //TODO maj RMI
            }
            else{
                
                System.out.println("le fichier n'as pas pus etre supprimer") ; 
            }

        }



        

    }

}