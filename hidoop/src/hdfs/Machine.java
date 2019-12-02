package hdfs;

public class Machine{
    private String port;
    private String host;

    //constructeur de la classe
    public Machine (String h,String p){
        port=p;
        host=h;
    }
    
    //setters de la classe
    public void setHost(String h){
        host=h;
    }

    public void setPort(String p){
        port=p;
    }

    //getters de la classe 

    public String getHost(){
        return host;
    }

    public String getPort(){
        return port;
    }

}