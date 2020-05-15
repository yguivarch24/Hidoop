package hdfs;


import config.Project;
import formats.Format;

import java.io.IOException;
import java.net.Socket;

public class testHdfs {
    public static void main(String[] args ) throws IOException {
        String fichier = "filesample.txt"  ;
        //lancement du naming nodes
        config.InitNamingNode.main(null);
        for(int i = 0 ; i< Project.HOSTS.length ; i++){
            //lancement des servs
            String[] serveur = new String[]{ String.valueOf(i)} ;
            config.InitServeur.main(serveur);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //test envoie
        HdfsClient.HdfsWrite(null ,fichier);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //test read
        //HdfsClient.HdfsRead(fichier , "testReply");


        //test Suppression
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HdfsClient.HdfsDelete(fichier);
    }





}
