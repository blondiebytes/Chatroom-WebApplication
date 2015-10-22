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
    
    public static int count = 0;
    public static ArrayList<String> messages = new ArrayList<String>();
    // ID -> name
    public static HashMap<String, String> idNameMap = new HashMap<String, String>();
    
    // map<ID, numberofSeenMessage>

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
        PrintWriter out = response.getWriter();
        try {
            // 1. Find type of request -> send?, connect?
            // request parameter called cmd inside of the request that says which we want
            // when we click buttoms before -> those set these parameters
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controller</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
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
            
            // Add this key, value pair to the hash map
            idNameMap.put(uniqueID, name);
            
            // Create 'entered' string
            String userHasEnteredString = name + "has entered the chat room";
            // Add it to our messages
            messages.add(userHasEnteredString);
           // Send client ID string to the client; !!! TO DO
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

}
