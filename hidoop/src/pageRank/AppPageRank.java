package pageRank;

import config.FragmentListInter;
import config.Project;
import formats.Format;
import formats.KV;
import formats.LineFormat;
import hdfs.HdfsClient;
import hdfs.HdfsClientWrite;
import map.MapReduceImpl;
import ordo.FormatInconnuException;
import ordo.Job;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppPageRank {

    public static void main(String[] args) throws IOException, NotBoundException {

        String inName = "";
        String outName = "";
        Format.Type fileType = Format.Type.LINE;

        FragmentListInter liste = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        Project.setHOSTS(liste.getHostArray());
        System.out.println(""+Arrays.toString(Project.HOSTS));
        Project.setHOSTSPORT(liste.getPortArray());
        System.out.println(""+Arrays.toString(Project.HOSTSPORT));
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

                //System.out.println("ok");
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

        ArrayList<String > pageVisitees = new ArrayList<String>() ;
        ArrayList<String> pile = new ArrayList<>() ;
        pile.add(inName) ;
        HashMap<String, List<String>> liensPages = new  HashMap<String, List<String>>() ;
        String URL = inName ;
        inName ="prochaine.page" ;
        outName = inName + "-res";
        do {
            LineFormatKV prochainesPages = new LineFormatKV("prochaine.page");
            prochainesPages.open(Format.OpenMode.W);
            for(String e : pile){
                prochainesPages.write(new KV(e,e));
                System.out.println("map de :"+e);
            }
            pile = new ArrayList<>();

            prochainesPages.close();
            /* envoie des pages*/
            HdfsClient.HdfsWriteKV(fileType, inName);


            /* Lancement du traitement */
            try {
                long startTime = System.currentTimeMillis();
                new Job1(inName, outName, fileType).startJob(new BranImpl());
                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Temps du Job : " + totalTime);
            } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("je m'arrete ici ...");
            }


         //on extrait les pages que l'on vien de visité
           LineFormatKV sortie = new LineFormatKV(Project.PATH + outName);
            //System.out.println( outName);
            ((LineFormatKV) sortie).open(Format.OpenMode.R);
            KV k ;
            //on regarde les pages nouvelle qui vienne d'etre visité et on met à jours les array

         do{
             //System.out.println("je passe ici");
             k = sortie.read() ;
             if (!(k==null)){
                 if(!pageVisitees.contains(k.k)){
                     pageVisitees.add(k.k) ;
                     liensPages.put(k.k , new ArrayList<String>()  );
                 }
                 if( !pageVisitees.contains(URL+k.v) && !pile.contains(URL+k.v)    ){
                     pile.add(URL+k.v);
                 }
                 if(liensPages.containsKey(k.k) && !liensPages.get(k.k).contains(URL + k.v)   ){
                     liensPages.get(k.k).add(URL+ k.v ) ;
                 }




             }




             /*if(!(k==null)) {
                 System.out.println(k.k);
                 pageVisitees.add( k.k );  }

             if(!(k ==null) && pageVisitees.contains(URL +k.v ) ){
                 System.out.println(URL +k.v +" deja vu" );
             }
             else if(!(k ==null) && pile.contains(URL +k.v )&&!pageVisitees.contains(URL + k.v ) ){
                 System.out.println("ajout de :"+URL +k.v +" à vistite et enleve de la pile " );
                 pageVisitees.add(URL + k.v);
                // System.out.println(pil);
                 pile.remove(URL + k.v) ;
             }
             else if(!(k ==null) && !pile.contains(URL + k.v )  && !pageVisitees.contains(URL + k.v )){
                 System.out.println("ajout de :"+URL +k.v +" à pile " );
                 pile.add(URL + k.v) ;
             }
            */

         }while(  k != null  );
          //  System.out.println("sortie de la boucle ");
            //pile.remove(URL);
         ((LineFormatKV) sortie).close();

        }while( ! pile.isEmpty()) ;


        System.out.println("---map construction graph fini---");
        /*
        for(String e : pageVisitees) {
            System.out.println(e);
            for(String liens : liensPages.get(e) ){
                System.out.println("--"+liens);
            }
        }
        */

        PageFormat pf = new PageFormat(Project.PATH+     "reducer.pr") ;
        pf.open(Format.OpenMode.W);
        for(String e : pageVisitees) {
            ArrayList<String> a = new ArrayList<String>(liensPages.get(e) );
            pf.writeCouple(e,new Couple_PR_Liens(0.0 ,a )  );
        }
        pf.close();


        PageFormat valeur = new PageFormat(Project.PATH+ "valeur.pr") ;
        new BranImpl().reduce( pf,valeur);
        System.out.println("---reducer construction graph fini--- ");
        LineFormatKV prochainesPages = new LineFormatKV("valeur.pr");
        HdfsClient.HdfsWriteKV(fileType, "valeur.pr");

        /* Lancement du traitement */
        try {
            long startTime = System.currentTimeMillis();
            new Job1(inName, outName, fileType).startJob(new calculator50000() );
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Temps du Job : " + totalTime);
        } catch (RemoteException | NotBoundException | MalformedURLException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("je m'arrete ici ...");
        }


        System.out.println("---PageRank Fini---");
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