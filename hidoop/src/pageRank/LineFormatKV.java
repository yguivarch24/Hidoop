package pageRank;

import formats.Format;
import formats.KV;

import java.io.*;

public class LineFormatKV  implements Format {
    private static final long serialVersionUID = 1L;

    private String fname;
    private KV kv;

    private transient LineNumberReader lnr;
    private transient BufferedWriter bw;
    private transient long index = 0;
    private transient Format.OpenMode mode;

    public LineFormatKV(String fname) {
        this.fname = fname;
    }

    public void open(Format.OpenMode mode) {
        try {
            this.mode = mode;
            this.kv = new KV();
            switch (mode) {
                case R:
                    lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(fname)));
                    break;
                case W:
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname)));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            switch (mode) {
                case R:
                    lnr.close();
                    break;
                case W:
                    bw.close();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public KV read() {
        try {
            String s = lnr.readLine();
            String[] kvtab = s.split(KV.SEPARATOR) ;
            kv.k = kvtab[0];
            kv.v = kvtab[1];
            if (kv.v == null) return null;
            index += kv.v.length() +kv.k.length()+ KV.SEPARATOR.length() ;
            return kv;
        } catch (Exception e ) {

            return null;
        }
    }

    public void write(KV record) {
        try {
            bw.write(record.k, 0, record.k.length());
            bw.write(KV.SEPARATOR, 0,KV.SEPARATOR .length());
            bw.write(record.v, 0, record.v.length());
            bw.newLine();
            index += record.v.length() +record.k.length()+ KV.SEPARATOR.length() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getIndex() {
        return index;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}