package hdfs;

import java.io.*;
import java.net.Socket;

public class HdfsServeurThread  extends Thread  {
    Socket socket ;
    InputStream  input  ;
    OutputStream output ;
    byte[] message ;
    String path ;

    public HdfsServeurThread (Socket s) throws IOException {
        socket = s ;
        input = s.getInputStream() ;
        output = s.getOutputStream() ;

        path ="";
        path = Integer.toString(s.getLocalPort()  )  ;
    }


    public void run() {
        try {

            //on attends le mesage du client
            while (input.available() == 0) {
                //System.out.println("serv : j'attends");
            } // on attends
            //on lit la commande /!\ cmd pas entiere ?
            //System.out.println(input.available());
            String cmd = new String(input.readNBytes( input.available() ));
            System.out.println(this.socket.toString() + " cmd : " + cmd) ;

            //on traite ici chaqu'une des commandes

            String[] arg = cmd.split("/@/");

            switch (arg[0]) {
                case "write":
                    write(arg);
                    break;
                case "read":
                    read(arg);
                    break;
                case "delete":
                    delete(arg) ;
                    break;
            }


        } catch (Exception e) {
            System.out.println("erreur detected hdfsService");
        }


    }




    private void write(String[] arg) throws IOException {
        System.out.println(this.socket.toString() + " debut write  ");
        File file = new File(path+arg[1] + ".part"+ Integer.toString( Integer.parseInt( arg[2])));
        FileOutputStream fos = new FileOutputStream(file);
        System.out.println(this.socket.toString() + " creation du fichier ,envoie du ok ");
        output.write("ok".getBytes());
        //on attends la rÃ©ponse
        while (input.available() <  Integer.parseInt( arg[3])) {

        }
        fos.write( input.readNBytes(input.available()) );
        fos.close();
        System.out.println(this.socket.toString() + " transfert fini");
    }

    private void read(String[] arg ) throws IOException {
        System.out.println(this.socket.toString() + " debut read  ");
        File file = new File(path+arg[1] + ".part"+ Integer.toString( Integer.parseInt( arg[2])));
        if(file.exists()){
            output.write("ok".getBytes());
            System.out.println(socket.toString() + " les fichier est existant ");
            //l'envoie
            FileInputStream fis = new FileInputStream(file) ;
            int nbByte = fis.available() ;
            byte[] buffer = new byte[nbByte];
            output.write( buffer , 0, nbByte);
            //on ferme le fichier
            fis.close();


        }
        else
        {
            output.write("no".getBytes());
            System.out.println(socket.toString() + " le fichier est inexistant ");
        }
    }
    private void delete(String[] arg){
        System.out.println(this.socket.toString() + " debut delete  ");
        File file = new File(path+arg[1] + ".part"+ Integer.toString( Integer.parseInt( arg[2])));
        try {
            if (file.exists()) {
                var statut = file.delete();


                if (statut) {
                    output.write("ok".getBytes());
                } else {
                    output.write("no".getBytes());
                }


            } else {
                output.write("no".getBytes());
            }
        }
        catch( Exception e) {
        }


    }

}

