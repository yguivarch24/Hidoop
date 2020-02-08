package config;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/* class permettant de ranger à distance la liste des fragments
selon le Daemons sur lequel ils ont été envoyés */
public class FragmentList extends UnicastRemoteObject implements FragmentListInter {

    private HashMap<String, ArrayList<String>> fragments;
   private File fragmentFile;

    public FragmentList() throws RemoteException {
        fragments = new HashMap<>();
        int i = 0;
        for (String s: Project.HOSTS) {
            fragments.put(s + ":" + Project.HOSTSPORT[i],new ArrayList<>());
            i++;
        }
    }

    @Override
    public void addFragment(String host, String fname) throws RemoteException {
        fragments.get(host).add(fname);
    }

    @Override
    public void removeFragment(String host, String fname) throws RemoteException {
        fragments.get(host).remove(fname);
    }

    @Override
    public HashMap<String, ArrayList<String>> getFragments() throws RemoteException {
        return fragments;
    }

    public FragmentList getFragmentObject() throws RemoteException{
        return this ;
    }
    public void saveFragmentFile(String fileName) throws IOException,RemoteException {
        FileOutputStream fos = new FileOutputStream(fragmentFile) ;
        String fileContent="";
        for (Map.Entry mapentry : fragments.entrySet()) {
            fileContent=fileContent+mapentry.getKey()+"=";
            List<String> mapentruList=(ArrayList<String>)mapentry.getValue();
            for(String l:mapentruList){
                fileContent=fileContent+l+"?";
                System.out.println(fileContent);
            }
            System.out.println("sub"+fileContent.substring(fileContent.length(), fileContent.length() ));
            if(fileContent.substring(fileContent.length()-3, fileContent.length() ).contains("?")){
                fileContent = fileContent.substring(0, fileContent.length() - 1);
            }
            fileContent=fileContent+'\n';
        }
        fos.write(fileContent.getBytes());
        fos.close();
    }
    public HashMap<String, ArrayList<String>> setHashMapFragmentFile() throws RemoteException, IOException {
        if(fragmentFile.exists()){
            fragments = new HashMap<>();
            FileReader fis = new FileReader(fragmentFile);
            BufferedReader bufferedReader = new BufferedReader(fis);
            String line="";
            while ((line = bufferedReader.readLine()) != null){

                ArrayList<String> fragmentList = new ArrayList<String>();
                StringTokenizer st1 = new StringTokenizer(line, "=");
                String key=st1.nextToken();
                System.out.println("begin:"+key+":end");
                if(st1.hasMoreTokens()){
                    String value=st1.nextToken();
                    System.out.println(value);
                    StringTokenizer st = new StringTokenizer(value, "?");
                    for ( int i = 0; i<st.countTokens(); i++ )
                    {
                        fragmentList.add(st.nextToken());
                    }
                    if(st.hasMoreTokens()){
                        fragmentList.add(st.nextToken());
                    }
                    System.out.println(fragmentList.toString());
                }
                fragments.put(key,fragmentList);
            }
            fis.close();
        }
        return fragments;
    }

}