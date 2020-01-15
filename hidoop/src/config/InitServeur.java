package config;

import hdfs.HdfsServeur;
import hdfs.InvalidArgumentException;
import ordo.DaemonImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class InitServeur {

    public static void main(String[] args) throws RemoteException {

        LocateRegistry.createRegistry(Project.REGISTRYPORT);

        if (args.length != 1) {
            System.out.println("Nombre d'argument invalide il en faut 1");
            System.exit(1);
        }
        try {
            int i = Integer.parseInt(args[0]);
            HdfsServeur hdfsServeur = new HdfsServeur(Project.HOSTSPORT[i]);
            new Thread(hdfsServeur).start();
            new Thread(new DaemonImpl(hdfsServeur,Project.HOSTS[i], Integer.toString(Project.HOSTSPORT[i]), i)).start();
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
