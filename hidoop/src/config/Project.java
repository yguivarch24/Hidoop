package config;

import hdfs.HdfsServer;
import hdfs.InvalidArgumentException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class Project {

    public final static String PATH = "/tmp";
    public final static String[] HOSTS =  {"localhost","localhost", "localhost", "localhost"};//{"localhost","beatles", "bouba", "albator"};
    public final static Integer[] HOSTSPORT = {4000,4001,4002,4003};
    public final static String NAMINGNODE = "localhost"; //"fluor";
    public final static Integer REGISTRYPORT = 12345;

}
