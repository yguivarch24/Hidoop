package config;

import java.rmi.server.UnicastRemoteObject;

/* class permettant de ranger à distance la liste des fragments
selon le Daemons sur lequel ils ont été envoyés */
public class FragmentList extends UnicastRemoteObject implements FragmentListInter {

    private String[][] fragments;

    public void setFragments(String fragments[][]) {
        this.fragments = fragments;
    }

    public String[][] getFragments() {
        return this.fragments;
    }
}