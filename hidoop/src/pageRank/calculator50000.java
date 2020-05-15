package pageRank;

import formats.*;
import map.MapReduce;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

public class calculator50000 implements MapReduce {
    final static String separateur = ";"  ;
    final static double S = 0.85 ;

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
        //fichier en entrée de type url lien<-> urlPage , pr , nbLien
        //on extrait les information
        HashMap<String , ArrayList<String>> pages  = new HashMap<String , ArrayList<String>>() ;
        HashMap<String , Double> pagePR = new HashMap<String , Double>() ;
        HashMap<String , Integer> pageNbLiens =new HashMap<String , Integer>() ;
        LineFormatKV lfkv = new LineFormatKV (( (Format) reader).getFname() ) ;
        PageFormat pf = new PageFormat(   ((Format) writer ).getFname()     ) ;
        pf.open(Format.OpenMode.W);
        //((Format) writer).open(Format.OpenMode.W);
        lfkv.open(Format.OpenMode.R);
        KV k = lfkv.read() ;
        while(k != null ){
            String lien = k.k  ;
            String[] stab = k.v.split(calculator50000.separateur) ;
            String page = stab[0];
            double pr =  Double.parseDouble(stab[1 ]);
            int nbLiens = Integer.parseInt(stab[2]) ;
            if(!pages.containsKey(page) ){
                pages.put(page , new ArrayList<>()) ;
                pagePR.put(page , pr) ;
                pageNbLiens.put(page, nbLiens ) ;
            }
            else{
                pages.get(page).add(lien) ;
            }


            k = lfkv.read() ;
        }
        //calcule du Page Rank !!!!!
        for(String e :pages.keySet()){
            double pr = 0 ;
            for(String l : pages.get(e )){
                pr += pagePR.get(l)  / pageNbLiens.get(l) ;
            }
            pr = calculator50000.S * pr + (1 - calculator50000.S) * 1/ pages.size() ;
            Couple_PR_Liens couple = new Couple_PR_Liens() ;
            couple.setPR(pr);
            couple.setLiens(pages.get(e ));
            pf.writeCouple(e , couple );

        }
        pf.close();
        lfkv.close();

    }
}
