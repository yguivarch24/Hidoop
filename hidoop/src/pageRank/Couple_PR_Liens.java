package application;

import java.util.ArrayList;
import java.util.Set;

public class Couple_PR_Liens {
    public Double PR;
    public ArrayList<String> Liens;
    public Couple_PR_Liens(){
        PR=null;
        Liens=null;
    }
    public Couple_PR_Liens(Double p, ArrayList<String> l){
        PR=p;
        Liens=l;
    }

    public Double getPR() {
        return PR;
    }

    public void setPR(Double PR) {
        this.PR = PR;
    }

    public ArrayList<String> getLiens() {
        return Liens;
    }

    public void setLiens(ArrayList<String> liens) {
        Liens = liens;
    }
}
