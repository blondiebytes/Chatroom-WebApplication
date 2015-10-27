package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kathrynhodge
 */
public class Controller extends HttpServlet {
    
    public PrintWriter out;
    public static int count = 0;
    public static ArrayList<String> messages = new ArrayList<String>();
    // ID -> name
    public static HashMap<String, String> idNameMap = new HashMap<String, String>();
    // ID -> indexOfMessagesAlreadySeen
    public static HashMap<String, Integer> idMessageIndexMap = new HashMap<String, Integer>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        out = response.getWriter();
        // Do we also need to have a series of printwriters to send back messages?
        
        // 1. Find type of request -> send?, connect? -->
        if ("CONNECT".equals(request.getParameter("cmd"))) {
            // Get value of name parameter
            String name = request.getParameter("name");
            connect(name);
        } else if ("SEND".equals(request.getParameter("cmd"))) {
           // Get ID param
            String id = request.getParameter("id");
            // Get message param
            String message = request.getParameter("message");
            send(id, message);
        } else if ("RECIEVE".equals(request.getParameter("cmd"))) {
            // GET ID param
            String id = request.getParameter("id");
            recieve(id);
        } else if ("DISCONNECT".equals(request.getParameter("cmd"))) {
            // GET ID param
            String id = request.getParameter("id");
            disconnect(id);
        } else {
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
   public void connect(String name) {
        // Set unique ID
            String uniqueID = ""+ count++;
            
            // Add this key, value pair to the idName hash map
            idNameMap.put(uniqueID, name);
            
            // Add key, value pair to the idMessageIndex hash map
            idMessageIndexMap.put(uniqueID, 0);
            
            // Create 'entered' string
            String userHasEnteredString = name + " has entered the chat room";
            // Add it to our messages
            messages.add(userHasEnteredString);
            
           // Send client ID string to the client
            out.print(uniqueID);
    }
    
  public void send(String id, String message) {
        String name = idNameMap.get(id);
        if (name != null) {
            messages.add(name+": "+message);
            // Send Sent to the client if it worked;
            out.print("Sent");
        } else {
            // Send Failure to Send to the client if didn't worked;
            out.print("Failure to Send");
        }
    }
  
  public void recieve(String id) {
      // Get index:
      Integer index = idMessageIndexMap.get(id);
      // Create a string with the messages from index to 
      // the length of the array list. 
      String unseenMessages = "";
      for (int i = index; i < messages.size(); i++) {
          unseenMessages = messages.get(i) + "\n";
      }
      // Set the index up to messages.size();
      idMessageIndexMap.replace(id, index, messages.size());
      // Return the formatted string 
      out.print(unseenMessages);
  }
    
 public void disconnect(String id) {
       String name = idNameMap.get(id);
        if (name != null) {
            messages.add(name+" has left the chat room");
            idNameMap.remove(id);
            // Send these strings back to the client
            out.print("Disconnect request confirmed");
        } else {
            out.print("Disconnect request failed");
        }
        
    }

}
