package config;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;

/* interface permettant de ranger à distance la liste des fragments
selon le Daemon sur lequel ils ont été envoyés */
public interface FragmentListInter extends Remote {

    void addFragment(String host, String fname);

    void removeFragment(String host, String fname);

    HashMap<String, ArrayList<String>> getFragments();

}