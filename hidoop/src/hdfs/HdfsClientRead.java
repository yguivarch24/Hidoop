package hdfs;

import java.io.*;
import java.net.Socket;

public class HdfsClientRead  extends Thread {

    File fichier  ;
    String nom  ;

    public HdfsClientRead(String nomFichier  , String PositionLocal){
        nom = nomFichier ;
        fichier = new File(PositionLocal) ;
    }
    public void run() {
        //TODO

        //on demande au rmi où sont stocké les fragment du fichier


        //on les telecharge ensuite dens l'ordre
            //pour chaque partie :
            //on se connecte au serveur

            //on envoie la cmd
            String cmd ="read/@/"+ nom ;
            //on telechage la partie

            //on l'ecrit au bonne enroit dans le fichier


    }

}