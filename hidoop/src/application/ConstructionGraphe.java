package application;

import formats.FormatReader;
import formats.FormatWriter;
import map.MapReduce;

import java.util.HashMap;
import java.util.Map;

public class ConstructionGraphe implements MapReduce {


    /**
     * void map: pour chaque page considérée (lien présent sur la page d'origine), extrait les liens figurants dans la page considérée
     * @param reader lecteur qui va récupérer toutes les pages à considérer
     * @param writer écrit l'ensemble des clés valeurs déstinées au traitement de PageRank, de la forme < URL, (PR,liste_liens) >
     */
    public void map(FormatReader reader, FormatWriter writer) {
        Map<String,Integer> hm = new HashMap<>();
    }

    public void reduce(FormatReader reader, FormatWriter writer) {

    }
}
