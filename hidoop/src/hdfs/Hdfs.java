package  hdfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Hdfs implements Runnable {

	static ServerSocket serverConnection;
	InetAddress addr ;
	int port ;

	public Hdfs(String host , int port ) throws InvalidArgumentException {
	this.port = port ;
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			throw new InvalidArgumentException();
		}
	}
	
	public void run (){


		try {
			serverConnection =  new ServerSocket(this.port,50 ,addr ) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("serveur connection lancé");
		
		//on regarde si un nouveau client se connecte
		while(true) {
			Socket socketClient  ; 
				try {
					socketClient = serverConnection.accept() ;
					System.out.println("connection réalisé ");
					//on accept la connexion on execute la suite dans un nouveau thread : 
					

					HdfsService thread =new  HdfsService(socketClient) ;
					thread.start () ; 
					
					
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
		}
		
		
		
		
	}
	

}
