package ordo;

import formats.Format;
import config.Project;
import hdfs.HdfsServeur;
import map.Mapper;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.util.List;

public class DaemonImpl extends UnicastRemoteObject implements Daemon, Runnable {
    private HdfsServeur hdfsServer;
    private String host;
    private String port;

    public DaemonImpl(HdfsServeur hdfsServer, String machine, String por) throws RemoteException {
        this.hdfsServer = hdfsServer;
        this.host = machine;
        this.port = por;
    }

    @Override
    public void runMap(Mapper m, List<Format> reader, List<Format> writer, CallBack cb) throws RemoteException {
        new Thread(() -> {
            for(Format r:reader){
                System.out.println("le reader passé est "+r.getFname());
            }
            for(int i=0;i<reader.size();i++) {
                System.out.println("Je me suis lancé avec le fichier " + reader.get(i).getFname());
                reader.get(i).setFname(Project.PATH + port + "/" + reader.get(i).getFname());
                writer.get(i).setFname(Project.PATH + port + "/" + writer.get(i).getFname());
                m.map(reader.get(i), writer.get(i)); // traitement d'un fragment (celui lié au reader)
            }
            try { // problème : l'exception ne peut pas être propagée à cause du fonctionnement du Thread...
                cb.call(); // appel du CallBack pour relancer la classe Job
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            Naming.rebind("//" + this.host + ":" + Project.REGISTRYPORT + "/Daemon", this);

            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));
        } catch(RemoteException re) {
            System.out.println(this.port);
            throw new RuntimeException(re.getMessage());
        } catch(MalformedURLException mue) {
            throw new RuntimeException("URL malformé");
        }
    }
}