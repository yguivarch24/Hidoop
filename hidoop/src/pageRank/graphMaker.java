package pageRank;

import formats.*;
import map.MapReduce;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class graphMaker implements MapReduce  {
    HashMap<String , List<String>> pageswebs ;


    //la page web est dans un formatKV ULR
    @Override
    public void map(FormatReader reader, FormatWriter writer) {
        //on lit la page web
        ((Format) reader).open(Format.OpenMode.R);
        ((Format) writer).open(Format.OpenMode.W);
        String URLpage  = reader.read().k ;
        ((Format) reader).close();
        //List<KV> listeclefurl = new ArrayList<>();

        List<String> pageParcourrus = new ArrayList<String>() ;
        List<String> pile = new ArrayList<String>() ;
        pile.add(URLpage) ;

        //parcours toutes les pages du site et création des clef
        do{
            String urlCourrant = pile.get(0) ;
            System.out.println("page visitée: " + urlCourrant );
            if( ( !urlCourrant.startsWith("http") &&   !urlCourrant.equals(URLpage) )|| pageParcourrus.size() ==0  ) {



                if (pageParcourrus.contains(urlCourrant)) {
                    //la page est deja visitéon fait rien
                    pile.remove(urlCourrant);
                }
                else {
                    writer.write((new KV(urlCourrant, urlCourrant)));
                    pageParcourrus.add(urlCourrant);
                    pile.remove(urlCourrant);
                    Set<String> urltrouvees = null;
                    try {
                        if (urlCourrant.equals(URLpage)) {
                            urltrouvees = findLinks(urlCourrant);
                        } else {
                            urltrouvees = findLinks(URLpage + urlCourrant);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert urltrouvees != null;
                    pile.addAll(urltrouvees);

                }



            }
            else   {
                pile.remove(urlCourrant);
            }
        }while (pile.size() > 0 ) ;

        //on crréer le set de clef
        /*
        for(String s : pageParcourrus ){
            System.out.println(s);
        }
        */


        ((Format) writer).close();
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
        KV KVPage = new KV("http://hidoop.leroipanda.fr.nf", "http://hidoop.leroipanda.fr.nf ") ;
        KVFormat kvf = new KVFormat("test.fname") ;
        kvf.open(Format.OpenMode.W);
        kvf.write(KVPage);
        kvf.close();
        LineFormat lf = new LineFormat("test1.fname") ;
        graphMaker prog = new graphMaker() ;
        prog.map(kvf , lf);



    }
}
