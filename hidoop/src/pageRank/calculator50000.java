package pageRank;

import formats.Format;
import formats.FormatReader;
import formats.FormatWriter;
import map.MapReduce;

import java.io.BufferedReader;

public class calculator50000 implements MapReduce {


    @Override
    public void map(FormatReader reader, FormatWriter writer) {
        ((PageFormat) reader).open(Format.OpenMode.R);
        Couple_PR_Liens couple = ((PageFormat) reader).readCouple() ;
        String URLpage = ((PageFormat) reader).read().k;

        //((Format) writer).open(Format.OpenMode.W);
    }

    @Override
    public void map(BufferedReader reader, FormatWriter writer) {

    }

    @Override
    public void reduce(FormatReader reader, FormatWriter writer) {

    }
}
