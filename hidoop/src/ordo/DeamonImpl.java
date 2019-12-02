package ordo;

import formats.Format;
import config.Project;
import hdfs.HdfsServer;
import hdfs.IHdfsServer;
import map.Mapper;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.AccessException;
import java.rmi.Naming;

public class DeamonImpl extends UnicastRemoteObject implements Daemon, Runnable {
    private HdfsServer hdfsServer;
    private String host;

    public DeamonImpl(HdfsServer hdfsServer, String machine) throws RemoteException {
        this.hdfsServer = hdfsServer;
        this.host = machine;
    }

    @Override
    public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {

        m.map(reader, writer); // traitement d'un fragment (celui lié au reader)

        cb.call(); // appel du CallBack pour relancer la classe Job
    }

    @Override
    public void run() {
        try {
            Naming.rebind("//" + this.host + ":" + Project.PORT.toString() + "/Daemon", this);
        } catch (MalformedURLException mue) {
            throw new RuntimeException(mue.getMessage());
        } catch (RemoteException re) {
            throw new RuntimeException(re.getMessage());
        }
    }
}