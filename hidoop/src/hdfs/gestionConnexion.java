package hdfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class gestionConnexion {
    final static int port = 4577 ;
    public static List<byte[] > listeAddress = new ArrayList<byte[] >() ;




    //fonction qui envoie les donnée à un serveur random
    public static Socket connexionServeur () throws IOException {
        Random rand = new Random();
        byte[] addServeur  = listeAddress.get(rand.nextInt(listeAddress.size())) ;
        Socket s = new  Socket(  InetAddress.getByAddress(addServeur)    , port ) ;
        return s ;
    }

}

