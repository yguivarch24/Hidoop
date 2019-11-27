package hdfs;

public interface IHdfsServer{
    public static void delete(String hdfsFname);
    public static void write(Format.Type fmt, String localFSSourceFname, int repFactor);
    public static void read(String hdfsFname, String localFSDestFname);
}