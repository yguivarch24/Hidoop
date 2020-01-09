package hdfs;

import java.io.*;
import java.net.Socket;

public class HdfsClientSend extends Thread{
    int tailleEnvoie = 1000 ;
    File fichier  ;
    Format.Type format;
    public HdfsClientSend ( String localFSSourceFname ,Format.Type fmt ,int repFactor) throws InvalidArgumentException, IOException, connexionPerdueException {
        fichier = new File(FilePath) ;
        tailleEnvoie = repFactor ;
        format = fmt ;
    }

    private void writeNoFormat( ) throws InvalidArgumentException, IOException, connexionPerdueException {

        boolean exists = fichier.exists();
        //System.out.println(fichier.getPath() );
        //System.out.println(fichier.exists());
        if(exists){
            System.out.println("le fichier existe");
            //le fichier existe bien
            FileInputStream fis = new FileInputStream(fichier) ;
            int partie = 0 ;

            while(fis.available() > 0 ){   //on pourra paraleliser cette partie
                System.out.println("le flux n'est pas vide ");
                //on envoie le flux Ã  un client
                byte[] buffer  ;



                buffer = fis.readNBytes(tailleEnvoie) ;
                //System.out.println(buffer);

                Socket s = GestionConnexion.connexionServeur() ;
                while(!s.isConnected() ){}
                System.out.println("envoie Ã  "+ s.toString());
                InputStream input  = s.getInputStream();
                OutputStream output = s.getOutputStream();
                String cmd ="write/@/"+ fichier.getName() + "/@/"+Integer.toString( partie) +"/@/"+Integer.toString( buffer.length)  ;
                output.write(cmd.getBytes());
                System.out.println("fin envoie commande");

                //TODO la rendre passive on attends la reponse (active) ;
                while(  input.available() == 0   ){

                }
                System.out.println("la reponse est bien recus");

                String Sbuffer = new String(input.readNBytes(  input.available() ))  ;
                System.out.println("ok = "  + Sbuffer);
                if(Sbuffer.equals("ok")){
                    //le serveur est pret on peut envoyer le buffer
                    output.write( buffer);

                    s.close();
                    System.out.println("---fin envoie---");
                    //TODO ajouter au naming node
                }

                else throw new connexionPerdueException() ;

                partie += 1 ;
            }

        }
        else throw new invalidArgumentException();

    }

    private void writeKV () {}

    public void run(){
        if (format == null ) {
            writeNoFormat() ;
        }
        esle(){

        }
    }


}
