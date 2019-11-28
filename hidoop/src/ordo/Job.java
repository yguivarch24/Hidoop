package ordo;

import map.*;
import formats.*;
import config.*;
import java.util.concurrent.Semaphore;
import java.rmi.*;
import hdfs.*;

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

        /* lancement du serveur ? */
        hdfsServer = new HdfsServer().start();

        CallBackImpl cb[];
        boolean wait[];
        for (int i = 0; i < Project.HOSTS.length; i++) {
            cb[i] = new CallBackImpl(new Semaphore(0));
            wait[i] = false;
        }

        Format reader;
        Format writer;

        String[][] maps;
        try {            
            maps = ((FragmentList) Naming.lookup("//" + Project.NAMINGNODE + "/list")).getFragments();
        } catch (Exception e) {
            throw new RuntimeException("liste des fragments introuvable");
        }

        for (int j = 0; j < maxLength(maps); j++) {
            for (int i = 0; i < Project.HOSTS.length; i++) {

                if (maps[i].length >= j) {

                    try {
                        switch (this.inFormat) {
                            case LINE :
                                reader = new LineFormat(this.inFName + (i+j+1));
                                break;
                            case KV :
                                reader = new KVFormat(this.inFName + (i+j+1));
                                break;
                            default :
                                reader = new LineFormat(this.inFName + (i+j+1));
                        }
                        switch (this.outFormat) {
                            case LINE :
                                writer = new LineFormat(this.inFName + (i+j+1) + "-res");
                                break;
                            case KV :
                                writer = new KVFormat(this.inFName + (i+j+1) + "-res");
                                break;
                            default :
                                writer = new KVFormat(this.inFName + (i+j+1) + "-res");
                                break;
                        }
                        ((DeamonImpl) Naming.lookup("//" + Project.HOSTS[i] + "/Daemon" + i)).runMap(mr, reader, writer, cb[i]);
                        /* runMap bloquant ? */
                        wait[i] = true;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            
            for (int i = 0; i < Project.HOSTS.length; i++) {
                if (wait[i]) {
                    cb[i].mapFini.acquire();
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
        mr.reduce(outReader, writer);
    }

    private static int maxLength(String list[][]){
        int max = 0;
        for (int i = 0; i < list.length; i++) {
            if (max < list[i].length) {
                max = list[i].length;
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