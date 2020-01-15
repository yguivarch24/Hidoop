package ordo;

import hdfs.HdfsClient;
import formats.Format;
import config.Project ;
import map.MapReduceImpl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AppHidoop {

    public static void main(String[] args) {

        String inName = "";
        String outName = "";
        Format.Type fileType = null;

        int nbArgs = args.length;
        switch (nbArgs) {
            case 3 :
                try {
                    fileType = toFormat(args[2]);
                } catch (FormatInconnuException e) {
                    System.out.println(e.getMessage());
                    System.exit(2);
                }
            case 2 :
                outName = args[1];
            case 1 :
                inName = args[0];
                break;
            default :
                System.out.println("arguments :  NomFichierIn  NomFichierOut  TypeFichier");
                System.out.println("    NomFichierIn : le nom du fichier d'entrée (obligatoire)");
                System.out.println("    NomFichierOut : le nom du fichier de sortie (optionnel)");
                System.out.println("    TypeFichier : le type du fichier d'entrée (optionnel, nécessite un nom de fichier de sortie et un nombre de fragment)");
                System.exit(1);
        }

        if (outName.equals("")) {outName = inName + "-KVres";}
        
        /* Fragmentation du fichier */
        HdfsClient.HdfsWrite(fileType, inName);

        /* Lancement du traitement */
        try {
            new Job(inName, outName, fileType).startJob(new MapReduceImpl());
        } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Traitement fini. Fichier disponible sous le nom de " + outName + " dans le repertoire courant");
        System.exit(0);
    }

    private static Format.Type toFormat(String str) throws FormatInconnuException {
        switch (str) {
            case "Line" :
                return Format.Type.LINE;
            case "KV" :
                return Format.Type.KV;
            default :
                throw new FormatInconnuException("Format de fichier inconnu");
                
        }
    }
}