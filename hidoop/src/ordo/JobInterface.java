package ordo;

import map.MapReduce;
import formats.Format;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface JobInterface {
// Méthodes requises pour la classe Job  
	public void setInputFormat(Format.Type ft);
    public void setInputFname(String fname);

    public void startJob (MapReduce mr) throws RemoteException, NotBoundException, MalformedURLException;
}