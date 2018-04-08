import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPDesktopServer implements Runnable{

 
    public static final String SERVERIP = "127.0.0.1";

    public static final int SERVERPORT = 1818;

 
    public void run() {

         try {

             System.out.println("S: Connecting...");

             ServerSocket serverSocket = new ServerSocket(SERVERPORT);

             while (true) {

                  Socket client = serverSocket.accept();

                  System.out.println("S: Receiving...");
 
                  try {

                      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                      String str = in.readLine();

                      System.out.println("S: Received: '" + str + "'");

                    } catch(Exception e) {

                        System.out.println("S: Error");

                        e.printStackTrace();

                    } finally {

                        client.close();

                        System.out.println("S: Done.");

                    }

             }

          } catch (Exception e) {

             System.out.println("S: Error");

             e.printStackTrace();
         }
    }
 
   public static void main (String a[]) {

        Thread desktopServerThread = new Thread(new TCPDesktopServer());

        desktopServerThread.start();
    }
}
