package pageRank;
//classe implementant le constructeur de graph

import formats.*;
import map.MapReduce;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BranImpl implements MapReduce {

    @Override
    public void map(FormatReader reader, FormatWriter writer) {
        //on lit la page web
        ((Format) reader).open(Format.OpenMode.R);
        ((Format) writer).open(Format.OpenMode.W);
        System.out.println(  ((Format) reader).getFname() );
        LineFormatKV lfkv = new LineFormatKV (( (Format) reader).getFname() )  ;
        ((Format) reader).close();
        lfkv.open(Format.OpenMode.R);



        String URLpage  = lfkv.read().v ;
        System.out.println(URLpage);

        Set<String> urltrouvees = null;
        try {
            urltrouvees = BranImpl.findLinks(URLpage) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> URLvalide = new ArrayList<>() ;
        for(String e : urltrouvees){
            if( !e.startsWith("http") &&   !e.equals(URLpage)  && !URLvalide.contains(e) ){
                URLvalide.add(e) ;
            }
        }
        for(String e : URLvalide){
            System.out.println(e);
        }

        /*
        Couple_PR_Liens couple = new Couple_PR_Liens() ;
        couple.setLiens(URLvalide);
        couple.setPR(1.0);
        ((PageFormat)writer).writeCouple(URLpage , couple) ;
        */
        for(String e : URLvalide ) {
            writer.write(new KV(URLpage, e));
        }
        ((Format) writer).close() ;
        lfkv.close();


    }

    @Override
    public void map(BufferedReader reader, FormatWriter writer) {

    }

    @Override
    public void reduce(FormatReader reader, FormatWriter writer) {

    }


    private static Set<String> findLinks(String url) throws IOException {
        Set<String> links = new HashSet<>();
        try {


            Document doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            Elements elements = doc.select("a[href]");
            for (Element element : elements) {
                links.add(element.attr("href"));
            }

            return links;
        }
        catch (Exception e){
            try {
                e.printStackTrace();
                System.out.println("!!!page  indisponible -- "+url);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return links ;

        }

    }

    public static void main(String args[]){
        KV KVPage = new KV("http://hidoop.leroipanda.fr.nf/page1.html", "http://hidoop.leroipanda.fr.nf/page1.html ") ;
        KVFormat kvf = new KVFormat("test.fname") ;
        kvf.open(Format.OpenMode.W);
        kvf.write(KVPage);
        kvf.close();
        //PageFormat pf = new PageFormat("test1.fname") ;
        LineFormatKV lfkv  = new LineFormatKV("test1.fname") ;
        BranImpl prog = new BranImpl() ;
        prog.map(kvf , lfkv);



    }
}
