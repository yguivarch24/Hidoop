package hdfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HdfsServeur extends Thread{
    static String path = "";
    private static ServerSocket serverConnection;
    private InetAddress addr ;
    private int port ;


    public HdfsServeur( String host , int port ) throws InvalidArgumentException {
        this.port = port ;
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new InvalidArgumentException();
        }
    }

    public HdfsServeur( int port ) throws InvalidArgumentException {
        this.port = port ;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new InvalidArgumentException();
        }
    }

    public void run (){


        try {
            serverConnection =  new ServerSocket(this.port,50 ) ;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("erreur serveur socket");
        }
        System.out.println(this.addr.toString() +":"+this.port+  ": serveur connection lançé");

        //on regarde si un nouveau client se connecte
        while(true) {

            try {
                //System.out.println("en attente de connection");
                Socket socketClient = serverConnection.accept();
                System.out.println(this.addr.toString() +" "+this.port+ ": connection réalisée ");
                //on accept la connexion on execute la suite dans un nouveau thread :


                HdfsServeurThread thread =new  HdfsServeurThread(socketClient) ;
                thread.start () ;


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("pb de connection");
            }

        }
    }


}