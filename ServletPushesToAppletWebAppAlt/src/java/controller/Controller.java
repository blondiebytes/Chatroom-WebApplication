/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {


    public static DataPusher dataPusher = null;
    public static ArrayList<Socket> allSockets;
    private static ArrayList<PrintWriter> allPrintWriters;

    public void init() throws ServletException {
        allSockets = new ArrayList<Socket>();
        allPrintWriters = new ArrayList<PrintWriter>();
        System.out.println("Launching data pusher thread.");
        dataPusher = new DataPusher(allPrintWriters);
        dataPusher.start();
        System.out.println("DataPusher started.");
    }

    public void destroy() {
        try {
            System.out.println("Stopping data pusher.");
            dataPusher.stopRunning();
            for (Socket socket:Controller.allSockets) {
                if (!socket.isClosed()) {
                    System.out.print("Closing a socket....");
                    socket.close();
                    System.out.println("closed.");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String answer = null;
            String command = request.getParameter("cmd");
            if (command.equals("Connect")) {
                answer = makeConnection(request);
            } else if (command.equals("Disconnect")) {
                answer = breakConnection(request);
            } else answer = "Invalid command.";
            out.println(answer);
            System.out.println(answer);
        } finally {
            out.close();
        }
    }

    private String makeConnection(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("socket")!=null) {
            return "Error: Already connected.";
        } else {
            String host = request.getRemoteHost();
            int port = Integer.parseInt(request.getParameter("port"));
            System.out.println("Connecting on port: " + port);
            Socket socket = null;
            try {
                InetAddress address = InetAddress.getByName(host);
                socket = new Socket(address, port);
                session.setAttribute("socket", socket);
                allSockets.add(socket);
                OutputStream output = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(output,true);
                allPrintWriters.add(printWriter);
            } catch (IOException ex) {
                ex.printStackTrace();
                return "Connect request failed.";
            }
            return "Connect request confirmed.";
        }
    }

    private String breakConnection(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("socket")==null) {
            return "Error: Not connected.";
        } else {
            Socket socket = (Socket) session.getAttribute("socket");
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            int index = allSockets.indexOf(socket);
            allPrintWriters.remove(index);
            allSockets.remove(index);
            session.setAttribute("socket", null);
            return "Disconnect request confirmed.";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
