package config;

import hdfs.HdfsServeur;
import hdfs.InvalidArgumentException;
import ordo.DaemonImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Project {

    public final static String PATH = "/tmp/";
    public final static String[] HOSTS =  /*{"eowyn","arwen"};*/ {"localhost"/*,"localhost", "localhost", "localhost"*/};
    public final static Integer[] HOSTSPORT = /*{4001, 4001};*/  {4010/*,4011,4012,4013*/};

    public final static int TAILLEPART = 1024;

    public final static String NAMINGNODE = /*"eowyn";*/ "localhost"; //"fluor";
    public final static Integer REGISTRYPORT = 12344;


    // Main d'initilisation pour test sur localhost
    public static void main(String[] args) {

        try { // TODO : faut-il 1 registre par machine ? faut-il tout centraliser sur le namingNode (si oui, changer appel des Daemons dans Job) ?
            LocateRegistry.createRegistry(REGISTRYPORT);
            Naming.bind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", new FragmentList());

            int i = 0;
            for (int port : HOSTSPORT) {
                HdfsServeur hdfsServeur = new HdfsServeur(port);
                new Thread(hdfsServeur).start();
                new Thread(new DaemonImpl(hdfsServeur,"localhost", Integer.toString(port), i)).start();
                i++;
            }

        } catch (InvalidArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Création du serveur échoué");
        }
        catch (RemoteException e) {
            System.out.println(e.getMessage());
            System.out.println("Config échoué");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }

}
