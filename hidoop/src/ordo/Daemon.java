package ordo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import map.Mapper;
import formats.Format;

public interface Daemon extends Remote {
	public void runMap (Mapper m, List<Format> reader, List<Format> writer, CallBack cb) throws RemoteException;
}
