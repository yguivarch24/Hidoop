package hdfs;

import config.Project;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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

        //path ="";
        path = Project.PATH+Integer.toString(s.getLocalPort()  ) + "/"  ;
        File file = new File(path);
        file.mkdir();
    }


    public void run() {
        //System.out.println(socket.toString() + " j'attends un message");
        try {

            //on attends le mesage du client
            byte[] buffer = new byte[100]; 
            int nbByte  = input.read(buffer);

            //on lit la commande /!\ cmd pas entiere ?
            //System.out.println(input.available());
            String cmd = new String(Arrays.copyOfRange( buffer ,0 ,nbByte ));
            System.out.println(this.socket.toString() + " cmd : " + cmd) ;

            //on traite ici chaqu'une des commandes

            String[] arg = cmd.split("/@/");
            //System.out.println(cmd);
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
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();

        } catch (Exception e) {
            System.out.println("erreur detected hdfsService "+e.toString());
        }


    }




    private void write(String[] arg) throws IOException {
        //System.out.println(this.socket.toString() + " debut write  ");
        File file = new File(path+arg[1] + ".part"+ Integer.toString( Integer.parseInt( arg[2])));
        FileOutputStream fos = new FileOutputStream(file);
        System.out.println(this.socket.toString() + " creation du fichier ,envoie du ok ");
        output.write("ok".getBytes());
        //on attends la rÃ©ponse
        int taille = Integer.parseInt(arg[3]);
        byte[] buffer = new byte[taille];
        int nbByte;
        int count = 0;
        do {
            nbByte = input.read(buffer, count, taille - count);
            count += nbByte;
        } while (nbByte > 0);
        fos.write(buffer);
        fos.close();
        //System.out.println(this.socket.toString() + " transfert fini");
    }

    private void read(String[] arg ) throws IOException {
        //System.out.println(this.socket.toString() + " debut read  ");
        File file = new File(path+arg[1] );
        if(file.exists()){
            //output.write("ok".getBytes());
            //System.out.println(socket.toString() + " le fichier existe ");
            //l'envoie
            FileInputStream fis = new FileInputStream(file) ;
            int nbByte = fis.available() ;
            byte[] buffer = new byte[nbByte];
            fis.read(buffer , 0 , nbByte) ;
            //System.out.println(new String(buffer));
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
        //System.out.println(this.socket.toString() + " debut delete  ");
        File file = new File(path+arg[1]);
        File file2 = new File(path+arg[1]+"-res");
        try {
            if (file.exists()) {
                var status = file.delete();
                var status2 = file2.delete();

                if (status && status2) {
                    output.write("ok".getBytes());
                } else if (status) {
                    output.write("ok1".getBytes());
                } else if (status2) {
                    output.write("ok2".getBytes());
                } else {
                    output.write("ko".getBytes());
                }


            } else {
                output.write("ko".getBytes());
            }
        }
        catch( Exception e) {
        }


    }

}

