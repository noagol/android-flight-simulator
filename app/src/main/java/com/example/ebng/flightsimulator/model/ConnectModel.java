package com.example.ebng.flightsimulator.model;

public class ConnectModel {
    private Thread thread;
    private BackgroundSender runnable;

    /**
     * Connect model
     * @param ip server ip
     * @param port server port
     */
    public ConnectModel(String ip, int port){
        // Run background thread
        runnable = new BackgroundSender(ip, port);
        thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Send updated aileron value
     * @param value the value
     */
    public void updateAileron(double value){
        runnable.addCommand("/controls/flight/aileron", value);
    }

    /**
     * Send updated elevator value
     * @param value the value
     */
    public void updateElevator(double value){
        runnable.addCommand("/controls/flight/elevator", value);
    }

    /**
     * Stop the model
     */
    public void stop(){
        runnable.stop();
        try{
            thread.join();
        } catch (InterruptedException ex) {
            // Do nothing
        }
    }

}
