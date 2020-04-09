package application;

import formats.FormatReader;
import formats.FormatWriter;
import map.MapReduce;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ConstructionGraphe implements MapReduce {


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

            }
        } catch(IOException e){}

    }

    public void reduce(FormatReader reader, FormatWriter writer) {

    }
}
