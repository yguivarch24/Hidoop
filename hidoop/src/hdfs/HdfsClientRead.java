package hdfs;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HdfsClientRead  extends Thread {

    File fichier  ;
    String nom  ;
    FileOutputStream fos ;

    public HdfsClientRead(String nomFichier  , String PositionLocal){
        nom = nomFichier ;
        fichier = new File(PositionLocal) ;
        try {
            fos = new FileOutputStream(fichier);
        } catch (FileNotFoundException e) {
            System.out.println("probleme avec le fichier ");
        }
    }
    public void run() {


        //on demande au rmi où sont stocké les fragment du fichier
        List<String> listeServeur = new ArrayList<>();
        //TODO RMI recupere les serveur de l'objet 

        //on les telecharge ensuite dens l'ordre
        for (String serv : listeServeur) {
            String[] infoServ = serv.split(":");
            //on se connecte au serveur
            Socket s = null;
            try {
                s = new Socket(infoServ[0], Integer.parseInt(infoServ[1]));
            } catch (IOException e) {
                System.out.println("connexion impossible au serveur");
            }
            //on envoie la cmd
            String cmd ="read/@/"+ nom+".part"+listeServeur.indexOf(serv) ;

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
                    if (!(input.available() == 0)) {
                        fos.write(input.readNBytes(input.available()));
                        //TODO maj du rmi
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }


            //on l'ecrit au bonne enroit dans le fichier


        }
    }
}