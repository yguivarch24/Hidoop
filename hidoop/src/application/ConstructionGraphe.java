package application;

import formats.FormatReader;
import formats.FormatWriter;
import map.MapReduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ConstructionGraphe implements MapReduce {
    String site; //Site sur lequel l'appli a été lancée //TODO: gérer initialisation du site

    @Override
    public void map(FormatReader reader, FormatWriter writer) { //Méthode vide pour coller à l'interface
    }

    /**
     * void map: pour chaque page considérée (lien présent sur la page d'origine), extrait les liens figurants dans la page considérée
     * @param reader lecteur qui va récupérer toutes les pages à considérer
     * @param writer écrit l'ensemble des clés valeurs déstinées au traitement de PageRank, de la forme < URL, (PR,liste_liens) >
     */
    public void map(BufferedReader reader, FormatWriter writer) {
        Map<String,Couple_PR_Liens> hm = new HashMap<>();
        String lienCourant;
        Double nbLiens = 0.0; // On compte le nombre de liens totaux pour calculer le PageRank original sur chaque page, égal à 1/nbLiens
        try {
            while ((lienCourant = reader.readLine()) != null) {
                nbLiens += 1.0; //On compte le nb de liens pour calculer le PR
                //On créé un nouveau Couple pour stocker le PR et les liens référencés par le lien courant
                Couple_PR_Liens Couple = new Couple_PR_Liens();
                //On stocke dans le couple les references
                Couple.setLiens(extractionLiens(lienCourant));
                // On stocke dans la hashmap le KV du lien courant et couple associé, il manque le PageRank du lien courant qu'on ajoutera après avoir traité tous les liens (pour pouvoir calculer le PR à partir du nb total de liens)
                hm.put(lienCourant,Couple);
            }
        } catch(IOException e){

        }
        // On a stocké dans la Hashmap tous les liens de la page originelle et les liens référencés par chacun, il reste à ajouter le PageRank original sur chaque page
        Double PR = 1.0 / nbLiens;
        for(Map.Entry<String,Couple_PR_Liens> mapentry: hm.entrySet()){
            Couple_PR_Liens CoupleCourant = mapentry.getValue();
            CoupleCourant.setPR(PR);
        }
        //La Hashmap est bien remplie
        //TODO: Gérer écriture
    }

    public Set<String> extractionLiens(String page) {
        Set<String> liens = new HashSet<String>();
        Document doc;
        String lien;
        URL u ;
        try {
            doc = Jsoup.connect(page).get();
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                try {
                    u = new URL(link.attr("abs:href"));
                    // élimination des références
                    lien = u.getProtocol() + "://" + u.getAuthority() + u.getFile();
                    try {
                        //les liens ajoutés doivent être valides
                        Jsoup.connect(lien).get();
                        //et sur le site
                        if (!horsSite(lien)) {
                            liens.add(lien); //pas de doublon (set)
                        }
                    } catch (IOException iox) {
                        System.out.println("lien inaccessible " + lien + " " + iox);
                    }
                } catch (MalformedURLException mu) {
                    System.out.println("lien erroné " + link + " " + mu);
                }
            }
            liens.remove(page); // suppression auto référence éventuelle
        } catch (IOException e) { //levée par doc = Jsoup.connect(page).get();
            System.out.println("erreur chargement "+page+" ("+e+")");
        }
        return liens;
    }

    public void reduce(FormatReader reader, FormatWriter writer) {

    }

    public boolean horsSite(String url) {
        //vrai si l'url n'est pas préfixée par le nom du site
        try {
            return !(new URL(url).getHost().equals(site));
        }
        catch (MalformedURLException mu) {
            System.out.println("URL incorrecte :  "+mu);
            return true;
        }
    }
}
