package config;

import hdfs.HdfsServeur;
import hdfs.InvalidArgumentException;
import ordo.DaemonImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class Project {

    public final static String PATH = "/tmp/";
    public final static String[] HOSTS =  {"piafabec","carapuce","magicarpe", "rondoudou"};
    public final static Integer[] HOSTSPORT = {4010,4011,4012, 4013};

    public final static int TAILLEPART = 1024*1024*10;

    public final static String NAMINGNODE = "salameche"; //"localhost";
    public final static Integer REGISTRYPORT = 12344;


    // Main d'initilisation pour test sur localhost
    public static void main(String[] args) {

        try {
            LocateRegistry.createRegistry(REGISTRYPORT);
            Naming.bind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", new FragmentList());
            List<Thread> th= new ArrayList<>();
            int i = 0;
            for (int port : HOSTSPORT) {
                HdfsServeur hdfsServeur = new HdfsServeur(port);
                Thread t = new Thread(hdfsServeur);
                t.start();
                th.add(t);
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
        } catch (MalformedURLException | AlreadyBoundException e) {
            e.printStackTrace();
        }

    }

}
