package hdfs;

import config.FragmentListInter;
import formats.Format;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;

import config.Project;


public class HdfsClientWrite {

    private File fichier  ;
    private Format.Type format;
    //TODO repfact = nbr de replication

    HdfsClientWrite(String localFSSourceFname, Format.Type fmt) {
        fichier = new File(localFSSourceFname);
        format = fmt;
    }

    private void writeNoFormat( ) throws InvalidArgumentException, IOException, ConnexionPerdueException, NotBoundException {

        boolean exists = fichier.exists();
        if(exists){
            System.out.println("le fichier existe");
            //le fichier existe bien
            FileInputStream fis = new FileInputStream(fichier) ;
            int partie = 0 ;
            int i = 0;
            while(fis.available() > 0 ){   //on pourra paraleliser cette partie
                System.out.println("le flux n'est pas vide ");
                //on envoie le flux Ã  un client
                int taille = Math.min(Project.TAILLEPART, fis.available()) ;
                byte[] buffer = new byte[taille];
                fis.read(buffer,0, taille) ;

                Random rand = new Random();
                int val = rand.nextInt(Project.HOSTS.length);
                InetAddress addServeur = InetAddress.getByName(Project.HOSTS[val]);

                int port = Project.HOSTSPORT[val];
                System.out.println(addServeur.toString()+":" + port);
                Socket s = new  Socket(addServeur, port);
                System.out.println("envoie à  "+ s.toString());
                InputStream input  = s.getInputStream();
                OutputStream output = s.getOutputStream();
                String cmd ="write/@/"+ fichier.getName() + "/@/"+partie +"/@/"+buffer.length;
                output.write(cmd.getBytes());
                System.out.println("fin envoie commande");

                //TODO la rendre passive on attends la reponse (active) ;
                System.out.println("attente de la reponse dans du serveur");
                byte[] bufferRep = new byte[100];
                int nbByte = input.read(bufferRep);

                System.out.println("la reponse est bien recus");
                String Sbuffer = new String(Arrays.copyOfRange( bufferRep ,0 ,nbByte ));
                System.out.println("ok = "  + Sbuffer);
                if(Sbuffer.equals("ok")){
                    //le serveur est pret on peut envoyer le buffer
                    output.write( buffer);

                    s.close();
                    System.out.println("---fin envoie---");

                    //TODO ajouter au naming node
                    updateFragmentList(fichier, val, i);
                    i++;
                }

                else throw new ConnexionPerdueException() ;

                partie += 1 ;
            }

        }
        else throw new InvalidArgumentException();

    }

    private void writeKV () throws InvalidArgumentException, IOException, NotBoundException {
        boolean exists = fichier.exists();
        if(exists){
            FileReader fis = new FileReader(fichier);
            BufferedReader bufferedReader = new BufferedReader(fis);
            String line;
            int fileSize = 0;
            int partie = 0 ;
            StringBuilder stringToSend= new StringBuilder();

            while ((line = bufferedReader.readLine()) != null){
                if(line.getBytes().length > Project.TAILLEPART){
                    if(fileSize > 0) {
                        sendServeur(stringToSend.toString(),partie);
                        fileSize = 0;
                        stringToSend = new StringBuilder();
                        partie++;
                    }
                    sendServeur(line,partie);
                    partie++;
                } else if (fileSize + line.getBytes().length > Project.TAILLEPART){
                    sendServeur(stringToSend.toString(),partie);

                    stringToSend = new StringBuilder(line).append('\n');
                    fileSize=line.getBytes().length;

                    partie++;
                }else{
                    stringToSend.append(line).append('\n');
                    fileSize=fileSize+line.getBytes().length;
                }
            }
            if(fileSize>0){
                sendServeur(stringToSend.toString(),partie);
            }


        }
        else throw new InvalidArgumentException();
    }

    private void sendServeur(String stringToSend, int partie) throws IOException, NotBoundException {
        System.out.println("envoie du fichier au serveur");
        Random rand = new Random();
        int val = rand.nextInt(Project.HOSTS.length);
        InetAddress addServeur = InetAddress.getByName(Project.HOSTS[val]);
        int port = Project.HOSTSPORT[val];
        Socket s = new  Socket(addServeur, port);
        OutputStream output = s.getOutputStream();
        InputStream input = s.getInputStream();
        String cmd = "write/@/"+ fichier.getName() + "/@/"+ partie +"/@/"+ stringToSend.getBytes().length;
        output.write(cmd.getBytes());

        System.out.println("attente de la reponse dans du serveur");
        byte[] bufferRep = new byte[100];
        int nbByte = input.read(bufferRep);

        System.out.println("la reponse est bien recus");
        String Sbuffer = new String(Arrays.copyOfRange( bufferRep ,0 ,nbByte ));
        System.out.println("ok = "  + Sbuffer);
        if(Sbuffer.equals("ok")){
            //le serveur est pret on peut envoyer le buffer
            output.write(stringToSend.getBytes());


            System.out.println("---fin envoie---");
            updateFragmentList(fichier, val, partie);
        }
        input.close();
        output.close();
        s.close();

    }



    public void write(){
        if (format == null ) {
            try {
                writeNoFormat() ;
            } catch (InvalidArgumentException | IOException | ConnexionPerdueException | NotBoundException e) {
                e.printStackTrace();
            }
        }
        else if(format == Format.Type.KV || format == Format.Type.LINE){
            try {
                writeKV() ;
            } catch (InvalidArgumentException | IOException | NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void  updateFragmentList(File fichier,int val, int i) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println(Project.HOSTS[val] + ":" + Project.HOSTSPORT[val] + fichier.getName() + ".part" + i);
        FragmentListInter liste = (FragmentListInter) Naming.lookup("//" + Project.NAMINGNODE + ":" + Project.REGISTRYPORT + "/list");
        liste.addFragment(Project.HOSTS[val] + ":" + Project.HOSTSPORT[val], fichier.getName() + ".part" + i);

    }


}