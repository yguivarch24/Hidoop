package ordo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CallBackImpl extends UnicastRemoteObject implements CallBack {

    protected CallBackImpl(String dest) throws RemoteException {
        this.destination = dest;
    }

    public void call() {

    }
}
