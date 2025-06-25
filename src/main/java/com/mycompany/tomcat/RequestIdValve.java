package com.mycompany.tomcat;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;

import java.io.IOException;
import javax.servlet.ServletException;

public class RequestIdValve extends AccessLogValve {

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

     
        String clientIP = request.getRemoteAddr();
        String requestId = java.util.UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        System.out.println("Request received from " + clientIP + " with ID " + requestId);

        try {
         
            getNext().invoke(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

           
            // Call the original logging mechanism
            log(request, response, duration);
        }
    }
}
