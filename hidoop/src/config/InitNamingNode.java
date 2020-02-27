package config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;


public class InitNamingNode {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Nombre d'argument invalide il en faut 2");
            System.exit(1);
        }
        try {

            Project.setNamingnode(args[1]);
            Project.setHostsAndPorts(args[0]);
            System.out.println(Arrays.toString(Project.HOSTS));
            LocateRegistry.createRegistry(Project.REGISTRYPORT);
            Naming.bind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", new FragmentList());
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");

        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}