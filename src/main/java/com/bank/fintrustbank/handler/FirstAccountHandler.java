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
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.AccountRequestDAO;
import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.dao.PrivilegedUserDAO;
import com.bank.fintrustbank.factory.AccountFactory;
import com.bank.fintrustbank.factory.PersonFactory;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.service.FirstAccountService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class FirstAccountHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	FirstAccountService service = new FirstAccountService();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getPathInfo();
		try {
			if (path.equals("/account-request")) {
				List<Branch> branches = new BranchDAO().getAllBranches();
				System.out.println(branches.toString());
				request.setAttribute("branches", branches);
				System.out.println("inside get");
				request.getRequestDispatcher("/WEB-INF/views/newaccount.jsp").forward(request, response);
			}else if (path.equals("/admin/requests")) {
		
			List<Map<String,Object>> requests =handleAllRequest(request, response) ; 
			request.setAttribute("requests", requests);
			request.getRequestDispatcher("/bank"+path).forward(request, response) ; 
				
			}
		} catch (ServletException | IOException | SQLException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getPathInfo();
		try {
			System.out.println("inga varen");
			if (path.equals("/account-request")) {

				handleFirstAccountRequest(request, response);

			} else if (path.equals("/account-request/approval")) {
				handleFirstAccountApprove(request, response);
			} else if (path.equals("/admin/firstaccount")) {
				handleAdminCreateFirstAccount(request, response);
			} 

		} catch (IOException | SQLException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}

	}

	private List<Map<String, Object>> handleAllRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, TaskException, SQLException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			return null;
		}

		String sessionPersonId = (String) session.getAttribute("personId");
		/*
		 * String jsonBody = new
		 * BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		 * 
		 * JsonNode rootNode = mapper.readTree(jsonBody);
		 * 
		 * String status = rootNode.path("status").asText();
		 */
		
		String status = request.getParameter("status")!=null ?  request.getParameter("status") : "PENDING" ;
		
		String branchId  = session.getAttribute("branch_id")!=null ? (String) session.getAttribute("branch_id") : new PrivilegedUserDAO().getBranch(sessionPersonId);
		session.setAttribute("branch_id" , branchId);
		
		
		
		return new AccountRequestDAO().getAccountRequests(branchId, status);
	}

	private boolean handleAdminCreateFirstAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException {
		
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			return false;
		}

		String sessionPersonId = (String) session.getAttribute("personId");

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);
		String branchId = rootNode.path("branch_id").asText();
		String accountType = rootNode.path("account_type").asText();
		Person person = PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE", "CUSTOMER");
		Account account = AccountFactory.createAccount(sessionPersonId, branchId, person.getPersonId(), accountType);
		return service.adminCreateFirstAccount(person, account);
	}

	private boolean handleFirstAccountApprove(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			return false;
		}

		String sessionPersonId = (String) session.getAttribute("personId");

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = rootNode.path("branch_id").asText();
		String customerId = rootNode.path("person_id").asText();
		String accountType = rootNode.path("account_type").asText();
		Account account = AccountFactory.createAccount(sessionPersonId, branchId, customerId, accountType);

		return service.approveFirstAccount(account);

	}

	private boolean handleFirstAccountRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {

		BufferedReader reader = request.getReader();

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = rootNode.path("branch_id").asText();
		String accountType = rootNode.path("account_type").asText();

		Person person = PersonFactory.createPerson(null, jsonBody, "PENDING", "CUSTOMER");

		AccountRequest acRequest = new AccountRequest(person.getPersonId(), branchId, accountType, "PENDING",
				System.currentTimeMillis(), (Long) null, person.getPersonId());

		return service.requestFirstAccount(acRequest, person);

	}

}
