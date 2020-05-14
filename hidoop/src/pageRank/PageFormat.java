package pageRank;

import formats.KV;
import formats.KVFormat;

import java.util.ArrayList;


//classe modelisant le couple clef- valeur ( url , <PR ,[URLs]> )

public class PageFormat extends KVFormat {
    final String CoupleSeparateur = ";" ;
    public PageFormat(String fname) {
        super(fname);
    }

    public Couple_PR_Liens readCouple(){
        Couple_PR_Liens couple = new Couple_PR_Liens();
        KV kv = this.read() ;
        String[] contenus = kv.v.split(CoupleSeparateur);
        couple.setPR(  Double.parseDouble(contenus[0] ) );
        ArrayList<String> liens = new ArrayList<String>() ;
        for(int i = 1 ; i< contenus.length ; i++){
            liens.add(contenus[i]) ;
        }
        couple.setLiens(liens);
        return couple ;
    }
    public  void writeCouple(String URl , Couple_PR_Liens couple){
        String s = "";
        s = s + String.valueOf(couple.PR) + this.CoupleSeparateur;
        for(String l : couple.Liens){
            s = s + l + this.CoupleSeparateur ;
        }
        this.write(new KV(  URl , s ));
    }


}
