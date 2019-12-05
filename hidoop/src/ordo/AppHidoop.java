package ordo;

import hdfs.HdfsClient;
import formats.Format;
import config.Project ;
import map.MapReduceImpl;

public class AppHidoop {

    public static void main(String args[]) {

        String inName = "";
        String outName = "";
        Format.Type fileType = Format.Type.LINE;
        int nbFragments = Project.HOSTS.length;

        int nbArgs = args.length;
        switch (nbArgs) {
            case 4 :
                fileType = toFormat(args[3]);
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
                System.out.println("    TypeFichier : ")
                System.exit(1);
        }
        
        /* Fragmentation du fichier */
        HdfsClient.HdfsWrite(fileType, inName, nbFragments);

        /* Lancement du traitement */
        new Job(inName, inName + "-KVres").startJob(new MapReduceImpl());

        System.out.println("Traitement terminé. Fichier disponible sous le nom de " + )
    }

    private static Format.Type toFormat(String str) {
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