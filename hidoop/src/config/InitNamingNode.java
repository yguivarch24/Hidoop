package config;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class InitNamingNode {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Project.REGISTRYPORT);
            Naming.bind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", new FragmentList());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");
        }
    }
}
