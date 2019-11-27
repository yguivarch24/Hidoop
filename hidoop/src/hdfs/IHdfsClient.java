package hdfs;


public interface IHdfsClient{
    public static void HdfsDelete(String hdfsFname);
    public static void HdfsWrite(Format.Type fmt, String localFSSourceFname,int repFactor);
    public static void HdfsRead(String hdfsFname, String localFSDestFname);
}