/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatwebapplet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JTextArea;

/**
 *
 * @author kathrynhodge
 */
public class Connection extends Thread {
    
    private final int PORT = 12333;
    private Scanner networkInput;
    private AppletReceiver appletReceiver;
    private JTextArea serverMessagesArea;
    private boolean running = true;
    private boolean connected = false;
    private ServerSocket serverSocket;
    private Socket socket;
    
    public String nameOfUser;
    public String uniqueID;
    public static int count = 0;
    public static ArrayList<String> messages = new ArrayList<>();
    public static HashMap<String, String> idNameMap = new HashMap<>();
    
    public Connection(AppletReceiver appletReceiver, JTextArea serverMessagesArea) {
        this.appletReceiver = appletReceiver;
        this.serverMessagesArea = serverMessagesArea;
    }
    
    public void connect(String name) {
        // Set name of user and unique ID
            nameOfUser = name;
            uniqueID = ""+ count++;
            
            // Add this key, value pair to the hash map
            idNameMap.put(uniqueID, nameOfUser);
            
            // Create 'entered' string
            String userHasEnteredString = nameOfUser + "has entered the chat room";
            // Add it to our messages
            messages.add(userHasEnteredString);
           // Send client ID string to the client; !!! TO DO
    }

    public void connectToServer() {
        try {
            
            // Setting up Server
            int port = makeServerSocket();
            if (port==-1) {
                serverMessagesArea.setText("Error: No free port.");
                return;
            }
            String portString = "" + port;
            sendHttpRequest("cmd","Connect","port",portString);
            socket = serverSocket.accept();
            networkInput = new Scanner(socket.getInputStream());
            serverSocket.close();
            setConnected(true);
            serverMessagesArea.append("connected = true\n");
        } catch (IOException ex) {
            serverMessagesArea.setText("Error in connectToServer\n");
            serverMessagesArea.append(ex.toString());
        }
    }

    private int makeServerSocket() {
        for (int port=1024; port<= 65535; port++) {
            try {
                serverSocket = new ServerSocket(port);
                return port;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }
    
    public String send(String id, String message) {
        String name = idNameMap.get(id);
        if (name != null) {
            messages.add(name+": "+message);
            return "Received";
        } else {
            return "Failure to Receive";
        }
    }
    
    public String disconnect(String id) {
       String name = idNameMap.get(id);
        if (name != null) {
            messages.add(name+" has left the chat room");
            return "Disconnect request confirmed";
        } else {
            return "Disconnect request failed";
        }
    }
    
    public void disconnectToServer() {
        setConnected(false);
        sendHttpRequest("cmd","Disconnect");
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
        socket = null;
    }

    public void sendHttpRequest(String... requestData) {
        try {
            URL sourceURL = appletReceiver.getDocumentBase();
            String host = sourceURL.getHost();
            String webAppName = "/ServletPushesToAppletWebAppAlt";
            String servletName = "/Controller";
            String address = "http://" + host + ":8080" + webAppName + servletName;
            QueryString query = buildQuery(requestData);
            URL url = new URL(address + "?" + query);
            URLConnection urlConnection = url.openConnection();
            String confirmation = getResponse(urlConnection);
            serverMessagesArea.append(confirmation);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private QueryString buildQuery(String[] requestData) {
        QueryString query = new QueryString();
        for (int i = 0; i < requestData.length; i = i+2) {
            String name = requestData[i];
            String value = requestData[i+1];
            query.add(name, value);
        }
        return query;
    }

    private String getResponse(URLConnection urlConnection) {
        InputStream in = null;
        try {
            in = urlConnection.getInputStream();
            InputStreamReader r = new InputStreamReader(in);
            int c;
            String answer = "";
            while ((c = r.read()) != -1) {
                answer += (char) c;
            }
            return answer;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "Response stream error.";
    }


    public void run() {
        do {
            if (isConnected()) {
                try {
                    String receipt = networkInput.nextLine();
                    serverMessagesArea.append(receipt + "\n");
                } catch (NoSuchElementException ex) {
                    ex.printStackTrace();
                }
            }
        } while (isRunning());
    }

    public synchronized boolean isConnected() {
        return this.connected;
    }

    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
    }

    public  synchronized boolean isRunning() {
        return this.running;
    }

    public synchronized void stopRunning() {
        this.running = false;
    }
}
