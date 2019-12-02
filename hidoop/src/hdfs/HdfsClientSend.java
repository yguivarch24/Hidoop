package hdfs;

import java.io.*;
import java.net.Socket;

public class HdfsClientSend extends Thread{
    int tailleEnvoie = 1000 ;
    File fichier  ;
    String argument ;
    public HdfsClientSend (String FilePath  , String Argument) throws InvalidArgumentException, IOException, connexionPerdueException {
        this.argument = Argument ;
        fichier = new File(FilePath) ;
        boolean exists = fichier.exists();
        if(exists){
            System.out.println("le fichier existe");
            //le fichier existe bien
            FileInputStream fis = new FileInputStream(fichier) ;
            int partie = 0 ;

            while(fis.available() > 0 ){   //on pourra paraleliser cette partie
                System.out.println("le flux n'est pas vide ");
                //on envoie le flux à un client
                byte[] buffer  ;



                buffer = fis.readNBytes(tailleEnvoie) ;
                Socket s = gestionConnexion.connexionServeur() ;
                InputStream input  = s.getInputStream();
                OutputStream output = s.getOutputStream();
                String cmd ="write/@/"+ fichier.getName() + "/@/"+Integer.toString( partie) ;
                output.write(cmd.getBytes());

                //on attends la reponse (active) ;
                while(  input.available() == 0   ){

                }
                String Sbuffer = new String(input.readAllBytes() )  ;
                if(Sbuffer.equals("ok")){
                    //le serveur est pret on peut envoyer le buffer
                    output.write( buffer);

                }
                else throw new connexionPerdueException() ;

                partie += 1 ;
            }

        }
        else throw new InvalidArgumentException();
    }



    public void run(){

    }


}
