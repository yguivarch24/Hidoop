package ordo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {

    public Semaphore mapFini;

    public CallBackImpl(Semaphore sem) throws RemoteException {
        this.mapFini = sem;
    }

    public void call() throws RemoteException{
        this.mapFini.release();
    }
}
