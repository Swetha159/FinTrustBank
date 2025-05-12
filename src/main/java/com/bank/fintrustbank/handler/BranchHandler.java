package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.util.BranchIdGenerator;
import com.bank.fintrustbank.util.IFSCCodeGenerator;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class BranchHandler implements HttpRequestHandler{
	
	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();
	BranchDAO branchDAO = new BranchDAO();
	 @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
			 String path = request.getPathInfo();
			 try {
			 if(path.equals("/branch"))
			 {
				 handleAddBranch(request, response);

			 }
			 }catch (IOException  | ServletException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }
	 
	 @Override
	    public void doPut(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		 String path = request.getPathInfo();
		 try {
		 if(path.equals("/branch"))
		 {
			 handleUpdateBranch(request,response);
		 }
		 }catch (IOException  | ServletException e) {
				e.printStackTrace();
				throw new TaskException(e.getMessage(), e);
			}
	 }

	private boolean handleUpdateBranch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException {
		

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

		}
		String sessionPersonId = (String) session.getAttribute("personId");
		BufferedReader reader = request.getReader();
		
		Branch branch =mapper.readValue(reader,Branch.class);
		branch.setModifiedBy(sessionPersonId);
		branch.setModifiedAt(System.currentTimeMillis());
		return branchDAO.updateBranch(branch);
		
		
	}

	private boolean handleAddBranch(HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException, TaskException {
	

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

		}
		String sessionPersonId = (String) session.getAttribute("personId");
		
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		String location = rootNode.path("location").asText();
		
		String branchId = BranchIdGenerator.generateBranchId(location);
		String managerId = rootNode.path("manager_id").asText();
	
		Branch branch = new Branch(branchId ,managerId, IFSCCodeGenerator.createCode(branchId, location), location , System.currentTimeMillis(),(Long)null ,sessionPersonId);
		
		return branchDAO.addBranch(branch);
		
		
	}


	
	

}
