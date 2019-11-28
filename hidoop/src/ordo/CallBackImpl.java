package ordo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {

    public Semaphore mapFini;

    protected CallBackImpl(Semaphore sem) throws RemoteException {
        this.mapFini = sem;
    }

    public void call() {
        this.mapFini.release();
    }
}
