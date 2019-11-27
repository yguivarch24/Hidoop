package ordo;

import map.MapReduce;
import formats.*;

import java.net.MalformedURLException;
import java.rmi.*;

public class Job implements JobInterfaceX {

    private Format.Type inFormat;
    private Format.Type outFormat;
    private String inFName;
    private String outFName;
    private int nbReduces;
    private int nbMaps;
    private SortComparator sortComp;
    private String hosts[];


    public Job(String hosts[]) {
        this.hosts = hosts;
    }


    public void startJob(MapReduce mr) {
        for (int i = 0; i < this.nbMaps; i++) {
            try {
                ((DeamonImpl) Naming.lookup("//" + hosts[i] + "/Daemon" + i)).runMap(mr, new KVFormat(this.inFName), new LineFormat(this.outFName), new CallBack(/* TODO */));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        
        /* Attente CallBack */

        mr.reduce(new LineFormat(this.outFName), new KVFormat(this.inFName));
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

    public static void main(String args[]) {
        
    }
}