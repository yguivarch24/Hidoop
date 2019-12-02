

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class hdfsService  extends Thread  {
	Socket socket ; 
	InputStream  input  ;
	OutputStream output ; 
	byte[] message ; 
	
	
	public hdfsService(Socket s) throws IOException {
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
}
