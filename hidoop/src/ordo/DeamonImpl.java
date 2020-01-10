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

public class DeamonImpl extends UnicastRemoteObject implements Daemon, Runnable {
    private HdfsServeur hdfsServer;
    private String host;
    private String port;

    public DeamonImpl(HdfsServeur hdfsServer, String machine, String por) throws RemoteException {
        this.hdfsServer = hdfsServer;
        this.host = machine;
        this.port = por;
    }

    @Override
    public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {

        m.map(reader, writer); // traitement d'un fragment (celui lié au reader)

        cb.call(); // appel du CallBack pour relancer la classe Job
    }

    @Override
    public void run() {
        try {
            Naming.rebind("//" + this.host + ":" + this.port + "/Daemon", this);
        } catch(RemoteException re) {
            throw new RuntimeException(re.getMessage());
        } catch(MalformedURLException mue) {
            throw new RuntimeException("URL malformé");
        }
    }
}