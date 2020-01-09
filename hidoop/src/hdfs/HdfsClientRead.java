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
        // on demande Ã  chaque serveur la liste des fichier posseder
        //TODO
        //on regarde quelle sont les parties qui nous interresse  (les inserrer dans une hashmap  nÂ° part -> serveur )
        //TODO
        //on les telecharge ensuite dens l'ordre
            //on telechage la partie
            //TODO
            //on l'ecrit au bonne enroit dans le fichier
            //TODO

    }

}
