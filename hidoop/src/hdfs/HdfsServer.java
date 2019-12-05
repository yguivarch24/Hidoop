package hdfs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HdfsServer implements Runnable {

    Socket socket ;
    InputStream input  ;
    OutputStream output ;
    byte[] message ;


    public HdfsServer(Socket s) throws IOException {
        socket = s ;
        input = s.getInputStream() ;
        output = s.getOutputStream() ;
    }

    public void run() {
        try {

            //on attends le mesage du client
            while (input.available() == 0) {} // on attends
            //on lit la commande /!\ cmd pas entiere ?
            String cmd = new String( input.readAllBytes()) ;
            System.out.println(cmd);

            //on traite ici chaqu'une des commandes

            String[] arg = cmd.split("/@/") ;
            switch(arg[0]){
                case "write" :
                    break ;
                case "send" :
                    break;
                case"delete":
                    break ;
                case "list" :
                    break ;




            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void delete(String hdfsFname,ObjectOutputStream oos){
        /*File fileToDelete = new File(hdfsFname);
        fileToDelete.delete();	
        oos.writeObject("file is deleted");*/
    }
    public static void write(String hdfsFname,String frag,ObjectOutputStream oos){
        /*File fileToAdd = new File(hdfsFname);
		FileWriter fw = new FileWriter(fileToAdd);
		fw.write(frag,0, frag.length());
		oos.writeObject("file is added");
		fw.close();*/
    }
    public static void read(String hdfsFname, ObjectOutputStream oos){
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