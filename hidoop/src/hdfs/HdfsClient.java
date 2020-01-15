/* une PROPOSITION de squelette, incomplète et adaptable... */

package hdfs;
import formats.Format;
import formats.KV;
import formats.KVFormat;
import formats.LineFormat;

import java.io.IOException;

public class HdfsClient {

    public static Thread HdfsDelete(String hdfsFname) {
        HdfsClientDelete thread = new HdfsClientDelete(hdfsFname);
        thread.start();
        return thread;
    }

    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname) {
        new HdfsClientWrite( localFSSourceFname , fmt).write();
    }

    public static void HdfsRead(String hdfsFname, String localFSDestFname) {
        new HdfsClientRead(hdfsFname ,localFSDestFname ).read();
    }

}