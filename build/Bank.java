package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yaml.snakeyaml.Yaml;

public class Bank extends HttpServlet {

    private Map<String, Map<String, String>> endpointConfig;

    @Override
    public void init() throws ServletException {
        try (InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/endpoints.yaml")) {
            Yaml yaml = new Yaml();
            endpointConfig = yaml.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String endpoint = request.getRequestURI().substring(request.getContextPath().length());

        if (endpointConfig.containsKey(endpoint)) {
            Map<String, String> handlerConfig = endpointConfig.get(endpoint);
            String handlerClassName = handlerConfig.get("handler");

            try {
                Class<?> handlerClass = Class.forName(handlerClassName);
                String methodName = handlerConfig.get("method");

                Method method = handlerClass.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

                method.invoke(handlerClass.getDeclaredConstructor().newInstance(), request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }
}



