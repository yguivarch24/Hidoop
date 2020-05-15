package pageRank;

import formats.*;
import map.MapReduce;

import java.io.BufferedReader;

public class calculator50000 implements MapReduce {
    final static String separateur = ";"  ;

    @Override
    public void map(FormatReader reader, FormatWriter writer) {

         // ((Format) reader).open(Format.OpenMode.R);
        //((Format) writer).open(Format.OpenMode.W);
        System.out.println(((Format) reader).getFname());
        KVFormat kvf = new KVFormat( ((Format) reader).getFname()) ;
        kvf.open(Format.OpenMode.R);
        KV k = kvf.read() ;
        String URLpage  = k.k ;
        Couple_PR_Liens couple = PageFormat.readCouple(k) ;
       // ((Format) reader).close();
        LineFormatKV lfkv = new LineFormatKV (( (Format) writer).getFname() )  ;
        lfkv.open(Format.OpenMode.W);

        for(String s : couple.getLiens() ){
            KV k1 = new KV() ;
            k1.k = s ;
            System.out.println(couple.getPR().toString());
            k1.v = URLpage +calculator50000.separateur +couple.getPR().toString()+calculator50000.separateur+couple.getLiens().size()  ;
            lfkv.write(k1);
        }
        lfkv.close();

    }

    @Override
    public void map(BufferedReader reader, FormatWriter writer) {

    }

    @Override
    public void reduce(FormatReader reader, FormatWriter writer) {

    }
}
