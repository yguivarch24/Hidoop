package pageRank;

import formats.Format;
import formats.KV;
import formats.KVFormat;

import java.io.*;
import java.util.ArrayList;


//classe modelisant le couple clef- valeur ( url , <PR ,[URLs]> )

public class PageFormat extends KVFormat {
    final static String CoupleSeparateur = ";" ;
    private transient LineNumberReader lnr;
    protected Format.OpenMode mode1 ;

    public PageFormat(String fname) {
        super(fname);
    }

    public static Couple_PR_Liens readCouple(KV kv){
        Couple_PR_Liens couple = new Couple_PR_Liens();
        String[] contenus = kv.v.split(CoupleSeparateur);
        couple.setPR(  Double.parseDouble(contenus[0] ) );
        ArrayList<String> liens = new ArrayList<String>() ;
        for(int i = 1 ; i< contenus.length ; i++){
            liens.add(contenus[i]) ;
        }
        couple.setLiens(liens);
        return couple ;
    }
    public  void writeCouple(String URl , Couple_PR_Liens couple) {
        String s = "";
        s = s + String.valueOf(couple.PR) + this.CoupleSeparateur;
        for (String l : couple.Liens) {
            s = s + l + this.CoupleSeparateur;
        }
        this.write(new KV(URl, s));
    }

    @Override
    public KV read() {
        KV kv = new KV() ;
        try {
            String line = lnr.readLine() ;
            String[] sep1 = line.split(KV.SEPARATOR) ;
            kv.k = sep1[0] ;
            kv.v = sep1[1];
            return kv ;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void open(Format.OpenMode mode) {
        try {

            this.mode1 = mode ;
            switch (mode) {
                case R:
                    lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(this.getFname())));
                    break;
                case W:
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFname())));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() {
        try {
            switch (this.mode1 ) {
                case R:
                    lnr.close();
                    break;
                case W:
                    bw.close();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
