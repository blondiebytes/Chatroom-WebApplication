/*
 * Copyright 2015 blondiebytes
 */

package chatwebapplet;

/**
 * @author kathrynhodge
 */

public class RecieveThread extends Thread{
    ChatWebApplet applet;

    public RecieveThread(ChatWebApplet applet) {
        this.applet = applet;
    }
    
    public void run() {
        while(true) {
            applet.recieve();
        }
    }
    
    
    

}
