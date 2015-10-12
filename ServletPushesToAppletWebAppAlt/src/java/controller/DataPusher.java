package controller;

import java.io.*;
import java.util.*;

/**
 *
 * @author Tom Ellman
 */
public class DataPusher extends Thread {

    private static final int DELAY = 1000;
    private boolean running;
    private ArrayList<PrintWriter> allPrintWriters;

    public DataPusher(ArrayList<PrintWriter> allPrintWriters) {
        this.allPrintWriters = allPrintWriters;
        running = true;
    }

    public void run() {
        while (running) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException ex) {
            }
            String time = new Date().toString();
            push(time);
        }
    }

    public void push(String time) {
        for (PrintWriter printWriter:allPrintWriters) {
            System.out.print("Pushing: " + time);
            printWriter.println(time);
            System.out.println("...Pushed.");
        }
    }
    
    public void stopRunning() {
        running = false;
    }
}
