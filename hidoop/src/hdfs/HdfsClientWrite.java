package hdfs;

import formats.Format;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import config.FragmentList;
import config.Project;


public class HdfsClientWrite extends Thread{
    //TODO à mettre dans Project
    int tailleEnvoie = 1000 ;
    File fichier  ;
    Format.Type format;
    //TODO repfact = nbr de replication

    public HdfsClientWrite ( String localFSSourceFname ,Format.Type fmt ,int repFactor) throws InvalidArgumentException, IOException, ConnexionPerdueException {
        fichier = new File(localFSSourceFname);
        tailleEnvoie = repFactor;
        format = fmt;
    }

    private void writeNoFormat( ) throws InvalidArgumentException, IOException, ConnexionPerdueException, NotBoundException {

        boolean exists = fichier.exists();
        //System.out.println(fichier.getPath() );
        //System.out.println(fichier.exists());
        if(exists){
            System.out.println("le fichier existe");
            //le fichier existe bien
            FileInputStream fis = new FileInputStream(fichier) ;
            int partie = 0 ;

            while(fis.available() > 0 ){   //on pourra paraleliser cette partie
                System.out.println("le flux n'est pas vide ");
                //on envoie le flux Ã  un client
                byte[] buffer  ;



                buffer = fis.readNBytes(tailleEnvoie) ;
                //System.out.println(buffer);
                // TODO GestionConnexion à potentiellement supprimer
                Random rand = new Random();
                int val = rand.nextInt(Project.HOSTS.length);
                InetAddress addServeur = InetAddress.getByName(Project.HOSTS[val]);
                int port = Project.HOSTSPORT[val];
                Socket s = new  Socket(addServeur, port);
                while(!s.isConnected() ){}
                System.out.println("envoie Ã  "+ s.toString());
                InputStream input  = s.getInputStream();
                OutputStream output = s.getOutputStream();
                String cmd ="write/@/"+ fichier.getName() + "/@/"+Integer.toString( partie) +"/@/"+Integer.toString( buffer.length)  ;
                output.write(cmd.getBytes());
                System.out.println("fin envoie commande");

                //TODO la rendre passive on attends la reponse (active) ;
                while(  input.available() == 0   ){

                }
                System.out.println("la reponse est bien recus");

                String Sbuffer = new String(input.readNBytes(  input.available() ))  ;
                System.out.println("ok = "  + Sbuffer);
                if(Sbuffer.equals("ok")){
                    //le serveur est pret on peut envoyer le buffer
                    output.write( buffer);

                    s.close();
                    System.out.println("---fin envoie---");

                    //TODO ajouter au naming node
                    updateFragmentList(fichier, val);
                }

                else throw new ConnexionPerdueException() ;

                partie += 1 ;
            }

        }
        else throw new InvalidArgumentException();

    }

    private void writeKV () throws InvalidArgumentException, IOException, ConnexionPerdueException, NotBoundException {
        boolean exists = fichier.exists();
        if(exists){
            FileReader fis = new FileReader(fichier);
            BufferedReader bufferedReader = new BufferedReader(fis);
            String line = "";
            int fileSize = 0;
            int partie = 1 ;

            Random rand = new Random();
            int val = rand.nextInt(Project.HOSTS.length);
            InetAddress addServeur = InetAddress.getByName(Project.HOSTS[val]);
            int port = Project.HOSTSPORT[val];
            Socket s = new  Socket(addServeur, port);
            while(!s.isConnected() ){}
            String stringToSend="";
            OutputStream output = s.getOutputStream();
            while ((line = bufferedReader.readLine()) != null){
                if (fileSize + line.getBytes().length >  tailleEnvoie){

                    String cmd ="write/@/"+ fichier.getName() + "/@/"+Integer.toString( partie) +"/@/"+Integer.toString( stringToSend.getBytes().length)  ;
                    output.write(cmd.getBytes());
                    output.write(stringToSend.getBytes());
                    output.close();
                    s.close();
                    val = rand.nextInt(Project.HOSTS.length);
                    addServeur = InetAddress.getByName(Project.HOSTS[val]);
                    port = Project.HOSTSPORT[val];
                    s = new  Socket(addServeur, port);
                    output = s.getOutputStream();
                    stringToSend=line;

                    updateFragmentList(fichier, val);
                }else{
                    stringToSend=stringToSend+"/n"+line;
                    fileSize=fileSize+line.getBytes().length;
                }
                if(s.isConnected()){
                    output.write(stringToSend.getBytes());
                    output.close();
                    s.close();
                    updateFragmentList(fichier, val);

                }
            }

        }
        else throw new InvalidArgumentException();
    }



    public void run(){
        if (format == null ) {
            try {
                writeNoFormat() ;
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConnexionPerdueException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                writeKV() ;
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConnexionPerdueException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void  updateFragmentList(File fichier,int val) throws RemoteException, NotBoundException, MalformedURLException {
        FragmentList liste = (FragmentList) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        liste.addFragment(Project.HOSTS[val] + ":" + Project.HOSTSPORT[val], fichier.getName());
        Naming.rebind("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list", liste);
    }


}