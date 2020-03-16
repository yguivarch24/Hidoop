// v0.0 PM, le 18/12/17
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class CG {
    /* Construction du Graphe : produit un fichier de paires URL <-> PR;liste URL_liens
     * les liens de la liste étant séparés par des espaces
     */
    Map<String,Set<String>> liensDePages;
    Map<String, Double> PR_liens;
    String site;
    static final double s = 0.85;
    public CG() {
        liensDePages = new HashMap<String,Set<String>>();
        PR_liens = new HashMap<String,Double>();
    }


    public static void map(CG cg){
        //Entrée: paires URL;liste URL_liens
        // Permet de calculer le PR de chaque lien grâce à plusieurs iterations de l'algo jusqu'à convergence

        // INITIALISATION PR
        //On créé une nouvelle Hashmap, qui associe à chaque clé (lien) de cg son PR
        Set<Map.Entry<String,Set<String>>> KV = cg.liensDePages.entrySet();
        Iterator<Map.Entry<String,Set<String>>> iter = KV.iterator();
        while(iter.hasNext()){
            Map.Entry<String,Set<String>> elementi = iter.next();
            String url_i = elementi.getKey();
            //Initialisation PageRank
            cg.PR_liens.put(url_i,1.0 / cg.liensDePages.size());
        }
        // FIN INITIALISATION

        //Itérations jusqu'à convergence
        boolean conv = false;
        int nbiter=0;
        while(!conv){
            cg.PR_liens = uneIter(cg);
            nbiter+=1;
            if (nbiter >= 10){
                conv = true;
            }
        }


    }

    public static Map<String, Double> uneIter(CG cg){
        //TODO: traiter le cas des pages pour lesquelles il n'existe pas de lien sortant

        //Une itération du map
        //Il faut d'abord calculer tous les nouveaux PR, puis seulement mettre à jour leurs valeurs, car si on modifie la valeur du PR du premier lien, cela peut influencer sur la nouvelle valeur obtenue pour les liens faisant référence au premier
        HashMap<String,Double> new_PR = new HashMap<String,Double>();

        //Création de l'itérateur pour parcourir le graphe
        Set<Map.Entry<String,Set<String>>> KV = cg.liensDePages.entrySet();
        Iterator<Map.Entry<String,Set<String>>> iter_i = KV.iterator();
        //Parcours du graphe
        while(iter_i.hasNext()){
            Map.Entry<String,Set<String>> elementi = iter_i.next(); //Element
            String url_i = elementi.getKey(); //URL de l'élément i
            Set<String> liens_i = elementi.getValue();
            //Calcul de PR(pi)
            double sommei = 0.0;

            //Calcul de la somme de PR(pj)/nbL(pj)
            Iterator<String> iter_liens_j = liens_i.iterator();
            while(iter_liens_j.hasNext()){
                String lien_j = iter_liens_j.next();
                double PR_j = cg.PR_liens.get(lien_j);
                int nb_references_lien_j = cg.liensDePages.get(lien_j).size();
                sommei +=  PR_j / nb_references_lien_j;
            }

            double PR_i = s * sommei + (1-s) * (1.0 / cg.liensDePages.size() );
            new_PR.put(url_i,PR_i);
        }

        return new_PR;
    }

    void produire(Map<String, Set<String>> pages) {
        //affiche les paires sur la sortie standard (rediriger si besoin)
        for (Map.Entry<String, Set<String>> entree : liensDePages.entrySet()) {
            System.out.print(entree.getKey()+"<->"+1.0/liensDePages.size()+";");
            for (String url : entree.getValue()) {
                System.out.print(url+" ");
            }
            System.out.println();
        }
    }

    void produire2(CG cg){
        //Création de l'itérateur pour parcourir le graphe
        Set<Map.Entry<String,Set<String>>> KV = cg.liensDePages.entrySet();
        Iterator<Map.Entry<String,Set<String>>> iter_i = KV.iterator();
        //Parcours du graphe
        while(iter_i.hasNext()){
            Map.Entry<String,Set<String>> elementi = iter_i.next(); //Element
            String url_i = elementi.getKey(); //URL de l'élément i
            Double PR_i = cg.PR_liens.get(url_i);
            System.out.println(url_i + "---" + PR_i + ";");
        }
    }

    boolean horsSite(String url) {
        //vrai si l'url n'est pas préfixée par le nom du site
        try {
            return !(new URL(url).getHost().equals(site));
        }
        catch (MalformedURLException mu) {
            System.out.println("URL incorrecte :  "+mu);
            return true;
        }

    }

    public static void main (String args[]) {
        // 1 argument : URL du site à évaluer
        CG coGr = null;
        String page;
        String lien;
        URL u ;
        Document doc;
        Set<String> liens = new HashSet<String>();
        LinkedList<String> aTraiter = new LinkedList<String>();

        if (args.length == 1) {
            coGr = new CG();
            aTraiter.add(args[0]);
            try {
                URL usite = new URL(args[0]);
                coGr.site = usite.getHost();
            }
            catch (MalformedURLException mu) {
                System.out.println("Argument attendu :  URL du site à évaluer "+mu);
                System.exit (1);
            }
        } else {
            System.out.println("Nb d'arguments ≠ 1. "+
                    "Un seul argument est attendu : URL du site à évaluer");
            System.exit (1);
        }

        while (aTraiter.size()>0) {
            liens.clear();
            page = aTraiter.pollFirst();
            //System.out.println("page : "+page);
            try {
                doc = Jsoup.connect(page).get();
                Elements links = doc.select("a[href]");

                for (Element link : links) {
                    try {
                        u = new URL(link.attr("abs:href"));
                        // élimination des références
                        lien = u.getProtocol()+"://"+u.getAuthority()+u.getFile();
                        try {
                            //les liens ajoutés doivent être valides
                            Jsoup.connect(lien).get();
                            //et sur le site
                            if (! coGr.horsSite(lien)) {
                                liens.add(lien); //pas de doublon (set)
                            }
                        }
                        catch (IOException iox) {
                            System.out.println("lien inaccessible "+lien+" "+iox);
                        }
                    }
                    catch (MalformedURLException mu) {
                        System.out.println("lien erroné "+link+" "+mu);
                    }
                }
                liens.remove(page); // suppression auto référence éventuelle

                System.out.println("nb liens : "+liens.size());
                coGr.liensDePages.put(page,new HashSet());
                coGr.liensDePages.get(page).addAll(liens);
                //ajouter les liens à traiter trouvés dans la page courante
                for (String url : liens) {
                    if (!(coGr.liensDePages.containsKey(url) || aTraiter.contains(url))) {
                        aTraiter.add(url);
                    }
                }
            }
            catch (IOException e) { //levée par doc = Jsoup.connect(page).get();
                System.out.println("erreur chargement "+page+" ("+e+")");
            }
        }
        System.out.println("--------- : ");

        map(coGr);

        coGr.produire(coGr.liensDePages);
        coGr.produire2(coGr);
        System.out.println("");
        System.out.println("Nombre de pages = " + coGr.liensDePages.size());
    }
}
