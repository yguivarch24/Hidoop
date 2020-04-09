package application;

import java.util.Set;

public class Couple_PR_Liens {
    public Double PR;
    public Set<String> Liens;
    public Couple_PR_Liens(){
        PR=null;
        Liens=null;
    }
    public Couple_PR_Liens(Double p, Set<String> l){
        PR=p;
        Liens=l;
    }

    public Double getPR() {
        return PR;
    }

    public void setPR(Double PR) {
        this.PR = PR;
    }

    public Set<String> getLiens() {
        return Liens;
    }

    public void setLiens(Set<String> liens) {
        Liens = liens;
    }
}
