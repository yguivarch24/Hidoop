
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class hdfs implements Runnable {

	static ServerSocket serverConnection;
	InetAddress addr ;
	int port ;

	public hdfs( String host , int port ) throws invalidArgumentException {
	this.port = port ;
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			throw new invalidArgumentException();
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
					

					hdfsService thread =new  hdfsService(socketClient) ; 
					thread.start () ; 
					
					
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
		}
		
		
		
		
	}
	

}
