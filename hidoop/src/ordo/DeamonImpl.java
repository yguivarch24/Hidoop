package ordo;

import formats.Format;
import hdfs.HdfsServer;
import hdfs.IHdfsServer;
import map.Mapper;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeamonImpl extends UnicastRemoteObject implements Daemon, Runnable {
    private HdfsServer hdfsServer;

    public DeamonImpl(HdfsServer hdfsServer) throws RemoteException {
        this.hdfsServer = hdfsServer;
    }

    @Override
    public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {

        m.map(reader, writer);

        cb.call();
        throw new RemoteException("TO DO");
    }

    public static void main(String args[]) {
        /* ~ hdfsServer.start() ~ */

        /* attente des runMap */
    }

    @Override
    public void run() {

    }
}
