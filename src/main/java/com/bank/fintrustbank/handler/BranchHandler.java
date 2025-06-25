package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.util.BranchIdGenerator;
import com.bank.fintrustbank.util.IFSCCodeGenerator;
import com.bank.fintrustbank.util.TimeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class BranchHandler implements HttpRequestHandler{
	
	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();
	private final BranchDAO branchDAO = new BranchDAO();
	 @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
			 String path = request.getPathInfo();
			 try {
			 if(path.equals("/branch"))
			 {
				 System.out.println("inside do post");
				if(handleAddBranch(request, response))
				{
					response.setStatus(HttpServletResponse.SC_OK); 
			
				}
				else
				{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			 }
			 if(path.equals("/branchdetails"))
			 {
				 handleBranchDetails(request,response);
			 }
			 }catch (IOException  | ServletException | SQLException e) {
					e.printStackTrace();
					
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }
	 
	 @Override
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
			 String path = request.getPathInfo();
			 try {
			 if(path.equals("/branches"))
			 {
				 handleGetBranches(request, response);

			 }
	
			 if(path.equals("/branch"))
			 {
				 request.getRequestDispatcher("/WEB-INF/admindashboard/create-branch.jsp").forward(request, response);
			 }
			 }catch (SQLException | ServletException | IOException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }
	 
	 private void handleBranchDetails(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, IOException, ServletException {
		
			String sessionPersonId = (String) request.getAttribute("personId");
			String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
			JsonNode rootNode = mapper.readTree(jsonBody);
			String branchId = rootNode.path("branch_id").asText();
         
			List<Map<String,Object>>  details  = branchDAO.getBranchDetails(branchId);
			
			 if(details!=null)
			 {
				 TimeFormatter.convertMillisToDateTime(details, "created_at");
				 TimeFormatter.convertMillisToDateTime(details, "modified_at");
				 Map<String,Object> branch = details.get(0);
				 request.setAttribute("branch", branch);
				 request.getRequestDispatcher("/WEB-INF/admindashboard/edit-branch.jsp").forward(request, response);
			 }
		else
			{
			 request.setAttribute("page" , "branches") ; 
			 request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response);
			}
		
	}

	private void handleGetBranches(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, ServletException, IOException {

		 
		 List<Map<String , Object>>  result = branchDAO.getBranches();
		 if(result!= null)
		 {
			 TimeFormatter.convertMillisToDateTime(result, "created_at");
			 TimeFormatter.convertMillisToDateTime(result, "modified_at");
			 
			 request.setAttribute("branches" , result) ; 
			 request.setAttribute("page" , "branches") ; 
			 
			 request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response);
		 }
		 else
		 {
			 request.setAttribute("page" , "branches") ; 
			 request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response);
			 
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
		 }catch (IOException  | ServletException | SQLException e) {
				e.printStackTrace();
				throw new TaskException(e.getMessage(), e);
			}
	 }

	private boolean handleUpdateBranch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
		

		String sessionPersonId = (String) request.getAttribute("personId");
		BufferedReader reader = request.getReader();
		
		Branch branch =mapper.readValue(reader,Branch.class);
		branch.setModifiedBy(sessionPersonId);
		branch.setModifiedAt(System.currentTimeMillis());
		return branchDAO.updateBranch(branch);
		
		
	}

	private boolean handleAddBranch(HttpServletRequest request, HttpServletResponse response) throws  IOException, ServletException, TaskException, SQLException {
	

		
		String sessionPersonId = (String) request.getAttribute("personId");
		
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		String location = rootNode.path("location").asText();
		
		String branchId = BranchIdGenerator.generateBranchId(location);
		//String managerId = rootNode.path("manager_id").asText();
	
		Branch branch = new Branch(branchId ,null, IFSCCodeGenerator.createCode(branchId, location), location , System.currentTimeMillis(),(Long)null ,sessionPersonId);
		
		return branchDAO.addBranch(branch);
		
		
	}


	
	

}
