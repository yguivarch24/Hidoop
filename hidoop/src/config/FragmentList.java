package config;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/* class permettant de ranger à distance la liste des fragments
selon le Daemons sur lequel ils ont été envoyés */
public class FragmentList extends UnicastRemoteObject implements FragmentListInter {

    private HashMap<String, ArrayList<String>> fragments;

    public FragmentList() throws RemoteException {
        fragments = new HashMap<>();
        int i = 0;
        for (String s: Project.HOSTS) {
            fragments.put(s + ":" + Project.HOSTSPORT[i],new ArrayList<>());
            i++;
        }
    }

    @Override
    public void addFragment(String host, String fname) throws RemoteException {
        fragments.get(host).add(fname);
    }

    @Override
    public void removeFragment(String host, String fname) throws RemoteException {
        fragments.get(host).remove(fname);
    }

    @Override
    public HashMap<String, ArrayList<String>> getFragments() throws RemoteException {
        return fragments;
    }


}