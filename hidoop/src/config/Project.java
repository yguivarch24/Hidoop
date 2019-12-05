package config;

import hdfs.HdfsServer;
import hdfs.InvalidArgumentException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class Project {

    public final static String PATH = "";
    public final static String[] HOSTS =  {"localhost","calimero", "bouba", "albator"};//{"bore", "azote", "antimoine"};
    public final static Integer PORT = 5000;
    public final static String NAMINGNODE = "fluor";

    public static void main(String[] args) {
        ArrayList<HdfsServer> serverList = new ArrayList<>();
        try {
            LocateRegistry.createRegistry(PORT);

            for (String host : HOSTS) {
                HdfsServer hdfsServer = new HdfsServer(host,PORT);
                serverList.add(hdfsServer);
            }

        } catch (RemoteException | InvalidArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");
        }
    }
}
