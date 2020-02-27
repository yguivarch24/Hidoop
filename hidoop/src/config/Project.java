package config;

import hdfs.HdfsServeur;
import hdfs.InvalidArgumentException;
import ordo.DaemonImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Project {

    public final static String PATH = "/tmp/";
    public static String[] HOSTS /*=  {"piafabec","carapuce","magicarpe", "rondoudou"}*/;
    public static Integer[] HOSTSPORT /*= {4010,4011,4012, 4013}*/;

    public final static int TAILLEPART = 1024*1024*50;

    public static String NAMINGNODE = "salameche";
    public static Integer REGISTRYPORT = 12344;


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
                new Thread(new DaemonImpl(hdfsServeur,"localhost", Integer.toString(port))).start();
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

    public static void setHostsAndPorts(String fileConfigString) throws RemoteException, IOException {
        System.out.println("INput:"+fileConfigString);
            String line="";
            StringTokenizer st = new StringTokenizer(fileConfigString, ",");
            String[] hosts =new String[st.countTokens()];
            Integer[] ports =new Integer[st.countTokens()];
            int i=0;
            while (st.hasMoreTokens()){
                StringTokenizer st1 = new StringTokenizer(st.nextToken(), ":");
                String host=st1.nextToken();
                hosts[i]=host;
                int port=Integer.parseInt(st1.nextToken());
                ports[i]=port;
                i++;
            }
        System.out.println("RESULTAT :");
        System.out.println(Arrays.toString(hosts));
            HOSTS = hosts;
            HOSTSPORT = ports;

    }

    public static void setNamingnode(String namingNodeString) throws RemoteException, IOException {
       StringTokenizer st1 = new StringTokenizer(namingNodeString, ":");
       NAMINGNODE=st1.nextToken();
       REGISTRYPORT=Integer.parseInt(st1.nextToken());
    }

    public static void setHOSTS(String[] HOSTS) {
        Project.HOSTS = HOSTS;
    }

    public static void setHOSTSPORT(Integer[] HOSTSPORT) {
        Project.HOSTSPORT = HOSTSPORT;
    }
}