/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rassus.dz.first;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

/**
 *
 * @author marinco
 */
public class Client implements Runnable{

    private String username;
    private int PORT; // server port
    final static String SERVER_NAME = "localhost"; // server name


    @Override
    public void run() {

        System.out.println("hr.fer.tel.rassus.dz.first.Client.run()");

        // create a client socket and connect it to the name server on the specified port number
        try (Socket clientSocket = new Socket(SERVER_NAME, PORT);/*SOCKET->CONNECT*/) {

            // get the socket's output stream and open a PrintWriter on it
            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(
                    clientSocket.getOutputStream()), true);

            // get the socket's input stream and open a BufferedReader on it
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String inputString;
            System.out.println("Sending requests to: " + PORT);
            boolean first = true;
            while ((inputString = in.readLine()) != null) {
                // send a String then terminate the line and flush
                if (first) {
                    outToServer.println(username);
                    first = false;
                    continue;
                }
                outToServer.println(inputString);//WRITE
                // read a line of text received from server
                String rcvString = inFromServer.readLine();//READ
                System.out.println(username + " received: " + rcvString);
                String myMeasurement = Arrays.toString(Worker.readMeasurements(TCPServer.end)).replaceAll("\\[|\\]|\\s", "");

                System.out.println("My measurement is "+ myMeasurement);
                calculateAndStore(rcvString,myMeasurement);
                if(inputString.equals("stop")){
                   // clientSocket.close();
                    break;
                }
                if (inputString.equals("shutdown")) {
                    break;
                }
                System.out.println("Press ENTER for next request");

            }
            clientSocket.close(); //CLOSE client socket

        } catch (IOException ex) {
            System.err.println("Exception caught when opening the socket or trying to read data: " + ex);
            System.exit(1);
        }//CLOSE
    }

    private void calculateAndStore(String first, String second) {
        String read[] = first.split(",", -1);
        String recv[] = second.split(",", -1);
        String[] measured = new String[6];
        for (int i = 0; i < 6; ++i) {
            if (read[i].equals("") && recv[i].equals("")) {
                measured[i] = "";
            } else {
                if (read[i].equals("")) {
                    read[i] = "0";
                }
                if (recv[i].equals("")) {
                    recv[i] = "0";
                }
                measured[i] = ""
                        + (Float.parseFloat(read[i]) + Float
                        .parseFloat(recv[i])) / 2;
            }
        }

        String s=String.join(",",measured);
        System.out.println("New measurement is: "+ s);
        try {
            storeMeasurement(username,"temperature",measured[0]);
            storeMeasurement(username,"pressure",measured[1]);
            storeMeasurement(username,"humidity",measured[2]);
            storeMeasurement(username,"co2",measured[3]);
            storeMeasurement(username,"no2",measured[4]);
            storeMeasurement(username,"so2",measured[5]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean storeMeasurement(String username, String parameter, String value)throws IOException {
        if(value.equals("")){
            return false;
        }
        URL obj = new URL("http://localhost:8080/sensors/"+username+"/store");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String urlParameters = "username=" + username + "&parameter="+parameter+"&value="+value;

        // Send post request

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        return (responseCode == 200);
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
