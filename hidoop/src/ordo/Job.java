package ordo;

import map.*;
import formats.*;
import config.*;
import java.util.concurrent.Semaphore;
import java.rmi.*;
import hdfs.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Job implements JobInterfaceX {

    private Format.Type inFormat = Format.Type.LINE;
    private Format.Type outFormat = Format.Type.KV;
    private String inFName = "";
    private String outFName = "";
    private int nbReduces;
    private int nbMaps;
    private SortComparator sortComp;

    public Job(String infname, String outfname) {
        this.inFName = infname;
        this.outFName = outfname;
    }

    public void startJob(MapReduce mr) {

        CallBackImpl[] cb = new CallBackImpl[Project.HOSTS.length];
        boolean[] wait = new boolean[Project.HOSTS.length];
        for (int i = 0; i < Project.HOSTS.length; i++) {
            try {
                cb[i] = new CallBackImpl(new Semaphore(0));
                wait[i] = false;
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }

        Format reader;
        Format writer;

        HashMap<String, ArrayList<String>> maps;
        try {            
            maps = ((FragmentList) Naming.lookup("//" + Project.NAMINGNODE + "/list")).getFragments();
        } catch (Exception e) {
            throw new RuntimeException("liste des fragments introuvable");
        }

        for (int j = 0; j < maxLength(maps); j++) {
            for (int i = 0; i < Project.HOSTS.length; i++) {

                if (maps.get(Project.HOSTS[i]).size() >= j) {

                    try {
                        switch (this.inFormat) {
                            case LINE :
                                reader = new LineFormat(maps.get(Project.HOSTS[i]).get(j));
                                break;
                            case KV :
                                reader = new KVFormat(maps.get(Project.HOSTS[i]).get(j));
                                break;
                            default :
                                reader = new LineFormat(maps.get(Project.HOSTS[i]).get(j));
                        }
                        switch (this.outFormat) {
                            case LINE :
                                writer = new LineFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                            case KV :
                                writer = new KVFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                            default :
                                writer = new KVFormat(maps.get(Project.HOSTS[i]).get(j) + "-res");
                                break;
                        }
                        ((DeamonImpl) Naming.lookup("//" + Project.HOSTS[i] + ":" + Project.PORT.toString() + "/Daemon" + (i+1))).runMap(mr, reader, writer, cb[i]);
                        /* runMap bloquant ? */
                        wait[i] = true;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            
            for (int i = 0; i < Project.HOSTS.length; i++) {
                if (wait[i]) {
                    try {
                        cb[i].mapFini.acquire();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        /* appel du hdfsread ? */
        /* TODO */

        switch (this.outFormat) {
            case LINE :
                reader = new LineFormat(this.inFName + "-res");
                writer = new LineFormat(this.outFName);
                break;
            case KV :
                reader = new KVFormat(this.inFName + "-res");
                writer = new KVFormat(this.outFName);
                break;
            default :
                reader = new KVFormat(this.inFName + "-res");
                writer = new KVFormat(this.outFName);
                break;
        }
        mr.reduce(reader, writer);
    }

    private static int maxLength(HashMap<String, ArrayList<String>> map){
        int max = 0;
        for (int i = 0; i < Project.HOSTS.length; i++) {
            if (max < map.get(Project.HOSTS[i]).size()) {
                max = map.get(Project.HOSTS[i]).size();
            }
        }
        return max;
    }


    public void setInputFormat(Format.Type ft) {
        this.inFormat = ft;
    }

    public Format.Type getInputFormat() {
        return this.inFormat;
    }

    public void setInputFname(String fname) {
        this.inFName = fname;
    }

    public String getInputFname() {
        return this.inFName;
    }

    public void setOutputFname(String fname) {
        this.outFName = fname;
    }

    public String getOutputFname() {
        return this.outFName;
    }

    public void setOutputFormat(Format.Type ft) {
        this.outFormat = ft;
    }

    public Format.Type getOutputFormat() {
        return this.outFormat;
    }

    public void setNumberOfReduces(int tasks) {
        this.nbReduces = tasks;
    }

    public int getNumberOfReduces() {
        return this.nbReduces;
    }

    public void setNumberOfMaps(int tasks) {
        this.nbMaps = tasks;
    }

    public int getNumberOfMaps() {
        return this.nbMaps;
    }

    public void setSortComparator(SortComparator sc) {
        this.sortComp = sc;
    }

    public SortComparator getSortComparator() {
        return this.sortComp;
    }
}