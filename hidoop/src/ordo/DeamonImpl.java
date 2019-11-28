package ordo;

import formats.Format;
import hdfs.IHdfsServer;
import map.Mapper;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeamonImpl extends UnicastRemoteObject implements Daemon {
    private File unmappedFile;
    private HdfsServer hdfsServer;

    protected DeamonImpl() throws RemoteException {
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
}
