/*
 * Copyright 2015 blondiebytes
 */

package chatwebapplet;

/**
 * @author kathrynhodge
 */

public class RecieveThread implements Runnable{
    ChatWebApplet applet;

    public RecieveThread(ChatWebApplet applet) {
        this.applet = applet;
    }
    
    public void run() {
        applet.recieve();
    }
    
    
    

}
