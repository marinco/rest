/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.fer.tel.rassus.dz.first;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marinco
 */
public class Worker implements Runnable {
    public static int row = 0;

    private String username;
    private final Socket clientSocket;
    private final AtomicBoolean isRunning;
    private final AtomicInteger activeConnections;

    public Worker(Socket clientSocket, AtomicBoolean isRunning, AtomicInteger activeConnections, String username) {
        this.clientSocket = clientSocket;
        this.isRunning = isRunning;
        this.activeConnections = activeConnections;
        this.username=username;
    }

    @Override
    public void run() {
       
        System.out.println("Number of active connections: " + activeConnections);
        try (
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);) {
            String receivedString;
            boolean first = true;
            String username = null;
            while ((receivedString = inFromClient.readLine()) != null/*READ*/) {
                if (first) {
                    username = receivedString;
                    System.out.println("Register: " + username);
                    first = false;
                    continue;
                }
                System.out.println("Server received request from " + username);

                //shutdown the server if requested
                if (receivedString.contains("shutdown")) {
                    outToClient.println("Initiating server shutdown!");//WRITE
                    System.out.println("Server sent: Initiating server shutdown!");
                    isRunning.set(false);
                    activeConnections.getAndDecrement();
                    return;
                }
                String[] measurements = readMeasurements(TCPServer.end);
                // read a few lines of text
                String stringToSend = Arrays.toString(measurements).replaceAll("\\[|\\]|\\s", "");
                // send a String then terminate the line and flush
                outToClient.println(stringToSend);
                System.out.println("Server sent: " + stringToSend);
            }

        } catch (IOException ex) {
            System.err.println("Exception caught when trying to read or send data: " + ex);
        }
    }

    public static String[] readMeasurements(long end) {
        BufferedReader br = null;
        try {
            String path = "/home/marinco/Downloads/SensorClient/src/hr/fer/tel/rassus/dz/first/mjerenja.txt";
            br = new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(path)), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e1) {
        }

        long start = System.currentTimeMillis();
        long broj_aktivnih_sekundi = ((start-end) / 1000);
        int redni_broj = (int) ((broj_aktivnih_sekundi % 100) + 2);
        row = redni_broj;

        for (int i = 1; i < redni_broj; ++i) {
            try {
                br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String strLine = null;
        try {
            strLine = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] data = strLine.split(",", -1);

        for (int i = 0; i < data.length; i++) {
            if (data[i].equals("")) {
                data[i] = "";
            }
        }

        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

}
