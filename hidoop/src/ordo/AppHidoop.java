package ordo;

import config.FragmentListInter;
import hdfs.HdfsClient;
import formats.Format;
import config.Project ;
import map.MapReduceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class AppHidoop {

    public static void main(String[] args) throws IOException, NotBoundException {

        String inName = "";
        String outName = "";
        Format.Type fileType = Format.Type.LINE;

        FragmentListInter liste = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        Project.setHOSTS(liste.getHostArray());
        System.out.println(""+Arrays.toString(Project.HOSTS));
        Project.setHOSTSPORT(liste.getPortArray());

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
                outName = args[2];
            case 2 :
                inName = args[1];
                Project.setNamingnode(args[0]);
                break;
            default :
                System.out.println("arguments :  NomNamingNode NomFichierIn  NomFichierOut  TypeFichier");
                System.out.println("    NomNamingNode : le nom de la machine host du naming node suivie de son port (exemple -> salameche:12344)");
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
            long startTime = System.currentTimeMillis();
            new Job(inName, outName, fileType).startJob(new MapReduceImpl());
            long endTime   = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Temps du Job : " + totalTime);
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