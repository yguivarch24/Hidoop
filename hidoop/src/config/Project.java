package config;

import hdfs.HdfsServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Project {

    public final static String PATH = "";
    public final static String[] HOSTS = {"bore", "azote", "antimoine"};
    public final static Integer PORT = 5000;
    public final static String NAMINGNODE = "fluor";


    public static void main(String[] args) {

        try {
            LocateRegistry.createRegistry(PORT);

            for (String host : HOSTS) {
                HdfsServer hdfsServer = new HdfsServer();
            }

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");
        }
    }
}
