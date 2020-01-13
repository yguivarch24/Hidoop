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

    public static void main(String args[]) {

        String inName = "";
        String outName = "";
        Format.Type fileType = null;
        int nbFragments = Project.HOSTS.length;

        int nbArgs = args.length;
        switch (nbArgs) {
            case 4 :
                try {
                    fileType = toFormat(args[3]);
                } catch (FormatInconnuException e) {
                    System.out.println(e.getMessage());
                    System.exit(2);
                }
            case 3 :
                nbFragments = Integer.parseInt(args[2]);
            case 2 :
                outName = args[1];
            case 1 :
                inName = args[0];
                break;
            default :
                System.out.println("arguments :  NomFichierIn  NomFichierOut  NombreFragments  TypeFichier");
                System.out.println("    NomFichierIn : le nom du fichier d'entrée (obligatoire)");
                System.out.println("    NomFichierOut : le nom du fichier de sortie (optionnel)");
                System.out.println("    NombreFragment : le nombre de fragments à former à partir du fichier d'entrée (optionnel, nécessite un nom de fichier de sortie)");
                System.out.println("    TypeFichier : le type du fichier d'entrée (optionnel, nécessite un nom de fichier de sortie et un nombre de fragment)");
                System.exit(1);
        }

        if (outName.equals("")) {outName = inName + "-KVres";}
        
        /* Fragmentation du fichier */
        HdfsClient.HdfsWrite(fileType, inName);
        System.out.println("Write fini");

        /* Lancement du traitement */
        try {
            new Job(inName, inName + "-KVres", fileType).startJob(new MapReduceImpl());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("Traitement fini. Fichier disponible sous le nom de " + outName + " dans le repertoire courant");
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