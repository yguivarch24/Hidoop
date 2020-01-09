/* une PROPOSITION de squelette, incomplète et adaptable... */

package hdfs;
import formats.Format;
import formats.KV;
import formats.KVFormat;
import formats.LineFormat;

import java.io.IOException;

public class HdfsClient {
    private Machine hdfsS[];


    private static void usage() {
        System.out.println("Usage: java HdfsClient read <file>");
        System.out.println("Usage: java HdfsClient write <line|kv> <file>");
        System.out.println("Usage: java HdfsClient delete <file>");
    }
	
    public static void HdfsDelete(String hdfsFname) {}
	
    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname, 
     int repFactor) {
        hdfsClientWrite thread = new	hdfsClientWrite( localFSSourceFname , fmt , repFactor) ;
        thread.start();
    }

    public static void HdfsRead(String hdfsFname, String localFSDestFname) {
        HdfsClientRead thread = new HdfsClientRead(hdfsFname ,localFSDestFname  ) ;
        thread.start();
    }

    // partie du code executer par le client
    public static void  main2(String[] args ) throws InvalidArgumentException, IOException, connexionPerdueException {
        System.out.println("coucou git");
        //chargement des parmetres de HDFS (fichier config : liste des serveurs )
        byte[] addr = new byte[]{ 127,0,0,1  } ;



        //on doit lire le commande qu'on souhaite réaliser
        switch(args[0]) {



            //envoie d'un fichier :   send +path_fichier +format optionelle
            case "send" :
                if( args.length < 2 ){
                    throw new InvalidArgumentException() ;
                }
                String FilePath  = args[1];
                String option = "";
                if(args.length == 3){
                    option = args[2] ;
                }


                HdfsClientSend thread = new	HdfsClientSend( FilePath , option) ;
                thread.start();




                break ;
            //telecharge le fichier : download + nom_fichier
            case "download" :


                break;

            //liste les fichiers disponibles
            case "list" :
                break ;
        }

    }

	
    public static void main(String[] args) {
        // java HdfsClient <read|write> <line|kv> <file>

        try {
            if (args.length<2) {usage(); return;}

            switch (args[0]) {
              case "read": HdfsRead(args[1],null); break;
              case "delete": HdfsDelete(args[1]); break;
              case "write": 
                Format.Type fmt;
                if (args.length<3) {usage(); return;}
                if (args[1].equals("line")) fmt = Format.Type.LINE;
                else if(args[1].equals("kv")) fmt = Format.Type.KV;
                else {usage(); return;}
                HdfsWrite(fmt,args[2],1);
            }	
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
