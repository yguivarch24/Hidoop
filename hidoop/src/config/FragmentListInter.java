package config;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/* interface permettant de ranger à distance la liste des fragments
selon le Daemon sur lequel ils ont été envoyés */
public interface FragmentListInter extends Remote {

    void addFragment(String host, String fname) throws RemoteException;

    void removeFragment(String host, String fname) throws RemoteException;

    HashMap<String, ArrayList<String>> getFragments() throws RemoteException;

    FragmentList getFragmentObject() throws RemoteException ;

}