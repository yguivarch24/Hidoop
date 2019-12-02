import java.io.IOException;

public class hdfsClient {
	
	
	
	// partie du code executer par le client
	
	public static void  main(String[] args ) throws invalidArgumentException, IOException, connexionPerdueException {
		System.out.println("coucou git");
		//chargement des parmetres de HDFS (fichier config : liste des serveurs )
		byte[] addr = new byte[]{ 127,0,0,1  } ;

		gestionConnexion.listeAddress.add(addr) ;

		//on doit lire le commande qu'on souhaite réaliser
		switch(args[0]) {


		
		//envoie d'un fichier :   send +path_fichier +format optionelle
		case "send" :
			if( args.length < 2 ){
				throw new invalidArgumentException() ;
			}
			String FilePath  = args[1];
			String option = "";
			if(args.length == 3){
				option = args[2] ;
			}


			hdfsClientSend thread = new	hdfsClientSend( FilePath , option) ;
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

}


