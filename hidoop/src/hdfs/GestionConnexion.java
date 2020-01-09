package hdfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO faire dans cette classe la gestion du RMI
public class  GestionConnexion {

    public static List<InetAddress > listeAddress = new ArrayList<InetAddress>();
    public static List<Integer> listePort = new ArrayList<Integer>() ;




    //fonction qui envoie les donnÃ©e Ã  un serveur random
    public static Socket connexionServeur () throws IOException {
        Random rand = new Random();
        int val = rand.nextInt(listeAddress.size());
        InetAddress addServeur  = listeAddress.get(val) ;
        int port = listePort.get(val) ;
        // System.out.println(addServeur.toString() +" "+port);
        return new  Socket(addServeur    , port );
    }

    public static int  nbServeur(){
        return listeAddress.size() ;
    }

    public static Socket connexionServeur (int i ) throws IOException {

        int val =i;
        InetAddress addServeur  = listeAddress.get(val) ;
        int port = listePort.get(val) ;
        // System.out.println(addServeur.toString() +" "+port);
        return new  Socket(addServeur    , port );
    }



}

