package config;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;

/* interface permettant de ranger à distance la liste des fragments
selon le Daemon sur lequel ils ont été envoyés */
public interface FragmentListInter extends Remote {


    public HashMap<String, ArrayList<String>> getFragments();
    public void setFragments(HashMap<String, ArrayList<String>> fragments);
}