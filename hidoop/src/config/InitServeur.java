package config;

import hdfs.HdfsServer;
import hdfs.InvalidArgumentException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class InitServeur {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Nombre d'argument invalide il en faut 1");
            System.exit(1);
        }
        try {
            int i = Integer.parseInt(args[0]);
            HdfsServer hdfsServer = new HdfsServer(Project.HOSTSPORT[i]);
            new Thread(hdfsServer).start();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            System.out.println("Il faut un entier comme paramètre");
        }
        catch (InvalidArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Création du serveur échoué");
        }

    }
}
