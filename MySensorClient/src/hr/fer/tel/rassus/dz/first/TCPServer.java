/*
 * This code has been developed at the Department of Telecommunications,
 * Faculty of Electrical Engineering and Computing, University of Zagreb.
 */
package hr.fer.tel.rassus.dz.first;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author marinco
 */
public class TCPServer implements ServerInterface {

    private static int PORT = 10007; // server port
    private static final int NUMBER_OF_THREADS = 4;
    private static final int BACKLOG = 10;

    private final AtomicInteger activeConnections;
    private ServerSocket serverSocket;
    private final ExecutorService executor;
    private final AtomicBoolean runningFlag;
    public static final long end = System.currentTimeMillis();


    private String username;

    public TCPServer() {
        activeConnections = new AtomicInteger(0);
        executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        runningFlag = new AtomicBoolean(false);
    }

    // Starts all required server services.
    @Override
    public void startup() {
        // create a server socket, bind it to the specified port on the local host
        // and set the max backlog for client requests
        System.out.println("Insert username");
        Scanner sc=new Scanner(System.in);
        username=sc.next();
        System.out.println("Insert port");
        PORT=Integer.parseInt(sc.next());
        try {
            if (register(username,PORT)) {
                System.out.println("Welcome " + username + "! Your port number is "+ PORT );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s;
        while(true){
            s=sc.next();
            if(s.trim().equals("start")){
                break;
            }
        }

        System.out.println("Searching for your nearest neighbour");
        UserAddress u=null;
        try {
            u=searchNeighbour(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Your nearest neighbour is " + u.getIp() + " "+u.getPort());

        //sc.close();
        Listener l=new Listener();
        l.setUsername(username);
        l.PORT=u.getPort();
        new Thread(l).start();

        try {
            this.serverSocket = new ServerSocket(PORT, BACKLOG);/*SOCKET->BIND->LISTEN*/;

            // set socket timeout to avoid blocking when there are no new incoming connection requests
            serverSocket.setSoTimeout(500);
            runningFlag.set(true);
            System.out.println("Server is ready!");

        } catch (SocketException e1) {
            System.err.println("Exception caught when setting server socket timeout: " + e1);
        } catch (IOException ex) {
            System.err.println("Exception caught when opening or setting the server socket: " + ex);
        }
    }

    //Main loop which accepts all client requests
    @Override
    public void loop() {

        while (runningFlag.get()) {
            try {
                // listen for a connection to be made to server socket from a client
                // accept connection and create a new active socket which communicates with the client
                Socket clientSocket = serverSocket.accept();/*ACCEPT*/

                // execute a new request handler in a new thread
                Runnable worker = new Worker(clientSocket, runningFlag, activeConnections,username);
                executor.execute(worker);
                //increment the number of active connections
                activeConnections.getAndIncrement();
            } catch (SocketTimeoutException ste) {
                // do nothing, check the runningFlag flag
            } catch (IOException e) {
                System.err.println("Exception caught when waiting for a connection: " + e);
            }
        }
    }

    @Override
    public void shutdown() {
        while (activeConnections.get() > 0) {
            System.out.println("WARNING: There are still active connections"); //Need to wait!
            try {
                Thread.sleep(5000);
            } catch (java.lang.InterruptedException e) {
                // Do nothing, check again whether there are still active connections to the server.
            }
        }
        if (activeConnections.get() == 0) {
            System.out.println("Starting server shutdown.");
            try {
                serverSocket.close();
                /*CLOSE the main server socket*/
            } catch (IOException e) {
                System.err.println("Exception caught when closing the server socket: " + e);
            } finally {
                executor.shutdown();
            }

            System.out.println("Server has been shutdown.");
        }
    }

    public boolean getRunningFlag() {
        return runningFlag.get();
    }

    public static boolean register(String username,int port) throws IOException {
        URL obj = new URL("http://localhost:8080/sensors");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        Random random=new Random();
        double longitude=15.87 + (16 - 15.87) * random.nextDouble();
        double latitude=45.75 + (45.85 - 45.75) * random.nextDouble();
        con.setRequestMethod("POST");
        String urlParameters = "username=" + username + "&latitude="+String.valueOf(latitude)+"&longitude="+String.valueOf(longitude)+"&IPaddress=localhost&port="+String.valueOf(port);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        //print result
        return (responseCode == 200);
    }

   static UserAddress searchNeighbour(String username) throws IOException {
       URL obj = new URL("http://localhost:8080/sensors/"+username+"/search");
       HttpURLConnection con = (HttpURLConnection) obj.openConnection();

       // optional default is GET
       con.setRequestMethod("GET");
       int responseCode = con.getResponseCode();

       BufferedReader in = new BufferedReader(
               new InputStreamReader(con.getInputStream()));
       String inputLine;
       StringBuffer response = new StringBuffer();

       while ((inputLine = in.readLine()) != null) {
           response.append(inputLine);
       }
       in.close();
        String r=response.toString();
        String[] parts=r.split("\"|:|}");
        String ip=parts[4];
        int port=Integer.parseInt(parts[8]);
        System.out.println(response.toString());
        UserAddress u=new UserAddress(ip,port);
        return u;
   }



    public static void main(String[] args) {
        ServerInterface server = new TCPServer();
        //start all required services
        server.startup();
        //run the main loop for accepting client requests
        server.loop();
        //initiate shutdown when startup is finished
        server.shutdown();

    }

}
