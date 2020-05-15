package pageRank;

import formats.Format;
import formats.FormatReader;
import formats.FormatWriter;
import formats.KV;
import map.MapReduce;

import java.io.BufferedReader;

public class calculator50000 implements MapReduce {
    final static String separateur = ";"  ;

    @Override
    public void map(FormatReader reader, FormatWriter writer) {

        ((Format) reader).open(Format.OpenMode.R);
        //((Format) writer).open(Format.OpenMode.W);
        KV k = ((Format) reader).read() ;
        String URLpage  = k.k ;
        Couple_PR_Liens couple = PageFormat.readCouple(k) ;
        ((Format) reader).close();
        LineFormatKV lfkv = new LineFormatKV (( (Format) writer).getFname() )  ;
        lfkv.open(Format.OpenMode.W);

        for(String s : couple.getLiens() ){
            KV k = new KV() ;
            k.k = s ;
            System.out.println(couple.getPR().toString());
            k.v = URLpage +calculator50000.separateur +couple.getPR().toString()+calculator50000.separateur+couple.getLiens().size()  ;
            lfkv.write(k);
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
