package com.example.ebng.flightsimulator.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class BackgroundSender implements Runnable{
    private String ip;
    private int port;
    private Queue<String > queue;
    private volatile boolean shouldStop;

    public BackgroundSender(String ip, int port){
        this.ip = ip;
        this.port = port;
        queue = new LinkedList<String >();
    }

    /**
     * Add a command to queue
     * @param path path of variable
     * @param value the value to update
     */
    public void addCommand(String path, double value) {
        String command = String.format("set %s %f\r\n", path, value);
        queue.add(command);
    }

    /**
     * Run background thread
     */
    @Override
    public void run() {
        // Create connection to server
        Socket clientSocket;
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException ex) {
            // Unable to open connection
            System.out.println("Unable to start connection to server");
            return;
        }

        String command;
        try {
            // Server stream
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            while (!shouldStop){
                if (queue.isEmpty()){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException ex){
                        // Do nothing
                    }
                    continue;
                }
                command = queue.poll();
                // Send data to server
                if (command != null){
                    outToServer.writeBytes(command);
                }
            }

        } catch (IOException ex) {
            // Do nothing
        } finally {
            try{
                clientSocket.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }

    /**
     * Stop the thread
     */
    public void stop(){
        shouldStop = true;
    }
}
