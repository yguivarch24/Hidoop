package ordo;

import formats.Format;
import hdfs.IHdfsServer;
import map.Mapper;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeamonImpl extends UnicastRemoteObject implements Daemon {
    private File unmappedFile;
    private IHdfsServer hdfsServer;
    private Mapper mapper;

    protected DeamonImpl() throws RemoteException {
    }


    @Override
    public void runMap(Mapper m, Format reader, Format writer, CallBack cb) throws RemoteException {
        throw new RemoteException("TO DO");
    }
}
