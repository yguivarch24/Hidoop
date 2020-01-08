package config;

import hdfs.HdfsServer;
import hdfs.InvalidArgumentException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class InitNamingNode {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Project.REGISTRYPORT);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");
        }
    }
}
