package hdfs;

import java.io.*;
import java.net.Socket;

public class HdfsClientDelete  extends Thread {
    //TODO connection + commande
    String nom ;
    public HdfsClientDelete( String nomFichier){
        nom = nomFichier ;

    }

    public void run(){
    //TODO
        //on demande au rmi les serveur qui possede un fragment du fichier

        //on se connecte au serve

        //on envoie la cmd
        String cmd ="delete/@/"+ nom ;


        //on met à jours le rmi en foncyion de la réponse

    }

}