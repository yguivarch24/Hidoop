/* une PROPOSITION de squelette, incomplète et adaptable... */

package hdfs;
import formats.Format;
import formats.KV;
import formats.KVFormat;
import formats.LineFormat;

import java.io.IOException;

public class HdfsClient {
    //private Machine hdfsS[];


    private static void usage() {
        System.out.println("Usage: java HdfsClient read <file>");
        System.out.println("Usage: java HdfsClient write <line|kv> <file>");
        System.out.println("Usage: java HdfsClient delete <file>");
    }

    public static void HdfsDelete(String hdfsFname) {
        HdfsClientDelete thread = new HdfsClientDelete(hdfsFname);
        thread.run();
    }

    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname) {
        try {
            HdfsClientWrite write = new HdfsClientWrite( localFSSourceFname , fmt);
            write.write();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConnexionPerdueException e) {
            e.printStackTrace();
        }
    }

    public static void HdfsRead(String hdfsFname, String localFSDestFname) {
        HdfsClientRead thread = new HdfsClientRead(hdfsFname ,localFSDestFname  ) ;
        thread.start();
    }

}