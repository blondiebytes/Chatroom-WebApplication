<%-- 
    Document   : index
    Created on : Oct 19, 2015, 7:15:13 PM
    Author     : kathrynhodge
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="Controller">
        <%-- some how we need to figure out how to set 
        up the buttons so that when you press SEND or 
        log on initially, the correct value is put in for the
        command request --%>
        
        Name: <input type="text" name="name" 
                                  value="${param.name}">
        <%-- For when we need ID--%>
        <input type="text" name="id" 
                                  value="${param.id}">
        <%-- For when we need Message --%>
        <input type="text" name="message" 
                                  value="${param.message}">
        <%-- For which button we press--%>
        <input type="submit" name="cmd" 
                             value="RECIEVE/CONNECT/SEND/DISCONNECT">  
        
    </form>
    </body>
</html>
