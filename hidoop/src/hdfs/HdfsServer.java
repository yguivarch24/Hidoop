package hdfs;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HdfsServer implements Runnable {

    private ServerSocket serverConnection;
    private int port ;

    public HdfsServer(int port) throws InvalidArgumentException {
        try {
            this.port = port ;
            serverConnection = new ServerSocket(port);
        } catch (UnknownHostException e) {
            throw new InvalidArgumentException();
        } catch (IOException e) {
            System.out.println("Impossible de créer le serveur");
            e.printStackTrace();
        }
    }

    public void run (){

        //on regarde si un nouveau client se connecte
        while(true) {
            Socket socketClient  ;
            try {
                socketClient = serverConnection.accept() ;
                System.out.println("connection réalisé ");
                //on accept la connexion on execute la suite dans un nouveau thread :
                new Thread(() -> {
                    traitment(socketClient);
                }).start();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private void traitment(Socket socket) {

        InputStream input  ;
        OutputStream output ;
        byte[] message ;

        try {
            input = socket.getInputStream() ;
            output = socket.getOutputStream() ;
            //on attends le mesage du client
            while (input.available() == 0) {} // on attends
            //on lit la commande /!\ cmd pas entiere ?
            String cmd = new String( input.readAllBytes()) ;
            System.out.println(cmd);

            //on traite ici chaqu'une des commandes

            String[] arg = cmd.split("/@/") ;
            switch(arg[0]){
                case "write" : write("","", output);
                    break ;
                case "send" :
                    break;
                case"delete": delete("",output);
                    break ;
                case "list" :
                    break ;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void delete(String hdfsFname, OutputStream os){
        /*File fileToDelete = new File(hdfsFname);
        fileToDelete.delete();	
        oos.writeObject("file is deleted");*/
    }

    private static void write(String hdfsFname,String frag,OutputStream oos){
        /*File fileToAdd = new File(hdfsFname);
		FileWriter fw = new FileWriter(fileToAdd);
		fw.write(frag,0, frag.length());
		oos.writeObject("file is added");
		fw.close();*/
    }

    private static void read(String hdfsFname, OutputStream oos){
    	/*File fileToSend = new File(hdfsFname);
    	BufferedReader br = new BufferedReader(new FileReader (fileToSend));
    	String stringToSend = "";				
		while (br.readLine() != null ) {
			stringToSend = stringToSend + br.readLine() + "\n";
		}
		oos.writeObject(stringToSend);*/
    }


    /*public static void main(String[] args){
        int port=Integer.parseInt(args[0]);
        ServerSocket sSocket = new ServerSocket(port);
        while(true){
            Socket cSocket=sSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(cSocket.getInputStream());
			String msg = (String) ois.readObject();
			String [] req = msg.split(" ");
            String cmdClient = req [0];
            ObjectOutputStream oos = new ObjectOutputStream(cSocket.getOutputStream());
            if (cmdClient=="CMD_DELETE"){
                delete(req[1],oos);
            }else if(cmdClient=="CMD_WRITE"){
                write(req[1],req[2],oos);
            }else if(cmdClient=="CMD_READ"){
                read(req[1],oos);
            }
        }
    }*/
}