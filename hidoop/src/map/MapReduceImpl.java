package map;

import formats.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MapReduceImpl implements MapReduce {

    public void map(FormatReader reader, FormatWriter writer) {
        
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        KV ligne;   // ici, une ligne contient plusieurs mots

        //reader.open(Format.OpenMode.R);
        while((ligne = reader.read()) != null) {

            StringTokenizer tokens = new StringTokenizer(ligne.v);
            while (tokens.hasMoreTokens()) {

                String mot = tokens.nextToken();

                if (map.containsKey(mot)) {
                    map.put(mot, map.get(mot).intValue() + 1);
                }

                else {
                    map.put(mot, 1);
                }
            }
        }
        //reader.close();

        //writer.open(Format.OpenMode.W);
        for (String mot : map.keySet()) {
            writer.write(new KV(mot, map.get(mot).toString()));
        }
        //writer.close();
    }

    public void reduce(FormatReader reader, FormatWriter writer) {
        
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        KV ligne;   // ici, une ligne contient un mot et son nombre d'occurence

        //reader.open(Format.OpenMode.R);
        while ((ligne = reader.read()) != null) {

            if (map.containsKey(ligne.v)) {
                map.put(ligne.k, map.get(ligne.k) + Integer.parseInt(ligne.v));
            }

            else {
                map.put(ligne.k, Integer.parseInt(ligne.v));
            }
        }
        //reader.close();

        //writer.open(Format.OpenMode.W);
        for (String mot : map.keySet()) {
            writer.write(new KV(mot, map.get(mot).toString()));
        }
        //writer.close();
    }
}