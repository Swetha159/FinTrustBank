package com.bank.fintrustbank.util;

import java.util.Map;

import javax.servlet.ServletContext;

public class EndpointConfigManager {

    private static ServletContext servletContext;

    private EndpointConfigManager() {
    	 if (Holder.ENDPOINT_MAP!= null) {
             throw new IllegalStateException("Endpoint Map retrieved");
         }
    }


    public static Map<String, Map<String, String>> getEndpointMap(ServletContext servletContext) {
        return Holder.ENDPOINT_MAP;
    }

    private static class Holder {
        private static final Map<String, Map<String, String>> ENDPOINT_MAP = loadEndpointMap(servletContext);

        private static Map<String, Map<String, String>> loadEndpointMap(ServletContext servletContext)  {
            if (servletContext == null) {
                throw new IllegalStateException("ServletContext not initialized.");
            }
            
            Map<String, Map<String, String>> config =
                (Map<String, Map<String, String>>) servletContext.getAttribute("endpointConfig");
            
            
            return config;
        }
    }
}
