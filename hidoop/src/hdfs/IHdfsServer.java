package hdfs;
import java.io.ObjectOutputStream;

public interface IHdfsServer{
    public static void delete(String hdfsFname,ObjectOutputStream oos);
    public static void write(String hdfsFname,String frag,ObjectOutputStream oos);
    public static void read(String hdfsFname, ObjectOutputStream oos);
}