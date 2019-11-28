package config;

import java.rmi.Remote;

/* interface permettant de ranger à distance la liste des fragments
selon le Daemons sur lequel ils ont été envoyés */
public interface FragmentListInter extends Remote {

    public void setFragments(String fragments[][]);

    public String[][] getFragments();
}