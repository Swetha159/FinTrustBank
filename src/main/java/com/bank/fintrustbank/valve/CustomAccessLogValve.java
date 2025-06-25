package com.bank.fintrustbank.valve;

import java.io.CharArrayWriter;
import java.util.UUID;

import org.apache.catalina.valves.AccessLogValve;

public class CustomAccessLogValve  extends AccessLogValve{

	
	    @Override
		public void log(CharArrayWriter message) {
	        String requestId = UUID.randomUUID().toString();


	     
	        super.log(message);
	    }
	}


