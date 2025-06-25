package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.AccountRequestDAO;
import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.factory.AccountFactory;
import com.bank.fintrustbank.factory.PersonFactory;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.service.FirstAccountService;
import com.bank.fintrustbank.util.TimeFormatter;
import com.bank.fintrustbank.util.ValidationUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class FirstAccountHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	private final FirstAccountService service = new FirstAccountService();
	
	private final  BranchDAO branchDAO = new BranchDAO();
	
	private final  AccountRequestDAO requestDAO = new AccountRequestDAO();
	
	

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		// String path = request.getPathInfo();
		// String path = request.getServletPath();
		String path = request.getServletPath();
		if (path.startsWith("/bank")) {
			path = request.getPathInfo();
		}
		try {
			if (path.equals("/account-request")) {
				List<Branch> branches = branchDAO.getAllBranches();
				System.out.println(branches.toString());

				request.setAttribute("branches", branches);
				System.out.println("inside get");
				request.getRequestDispatcher("/WEB-INF/views/newaccount.jsp").forward(request, response);
			} else if (path.equals("/admin/requests")) {

				handleAllRequest(request, response);
				

			}
		} catch (ServletException | IOException | SQLException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		// String path = request.getPathInfo();
		String path = request.getServletPath();
		if (path.startsWith("/bank")) {
			path = request.getPathInfo();
		}
		try {
			if (path.equals("/account-request")) {

				if (handleFirstAccountRequest(request, response)) {
					request.setAttribute("alertMessage", "Registration Succesful");
					request.getRequestDispatcher("/").forward(request, response);
				} else {
					System.out.println("inside else");
					request.getRequestDispatcher("/WEB-INF/views/newaccount.jsp").forward(request, response);
				}

			} else if (path.equals("/account-request/approval")) {
				if (handleFirstAccountApprove(request, response)) {
					response.setContentType("text/plain");
					response.getWriter().write("Account Request Accepted");

				} else {
					response.setContentType("text/plain");
					response.getWriter().write("Account Request Accept Unsuccessful");

				}
			} else if (path.equals("/account-request/rejection")) {
				if (handleFirstAccountReject(request, response)) {
					response.setContentType("text/plain");
					response.getWriter().write("Account Request Rejected");
				} else {
					response.setContentType("text/plain");
					response.getWriter().write("Account Request Reject Unsuccessful");
				}
			} else if (path.equals("/admin/firstaccount")) {
				handleAdminCreateFirstAccount(request, response);
			}

		} catch (IOException | SQLException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}

	}

	private boolean handleFirstAccountReject(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException, SQLException {

		String sessionPersonId = (String) request.getAttribute("personId");

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);

		String customerId = rootNode.path("person_id").asText();

		return service.rejectFirstAccount(customerId, sessionPersonId);

	}

	private void handleAllRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, TaskException, SQLException {

		String status = request.getParameter("status") != null ? request.getParameter("status") : "PENDING";

		String branchId = (String) request.getAttribute("branchId");

		System.out.println();

		List<Map<String, Object>> requests =requestDAO.getAccountRequests(branchId, status);
		if (requests != null) {
			TimeFormatter.convertMillisToDateTime(requests, "created_at");

			request.setAttribute("requests", requests);
		}
		request.setAttribute("page", "requests");

		if (request.getAttribute("role").equals("ADMIN")) {
			request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
		} else if (request.getAttribute("role").equals("SUPERADMIN")) {
			request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
		} else {
			request.setAttribute("errorMessage", "Access Restricted");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}

	}

	private boolean handleAdminCreateFirstAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException {

		String sessionPersonId = (String) request.getAttribute("personId");

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);
		String branchId = rootNode.path("branch_id").asText();
		String accountType = rootNode.path("account_type").asText();
		Person person = PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE", "CUSTOMER");
		Account account = AccountFactory.createAccount(sessionPersonId, branchId, person.getPersonId(), accountType);
		return service.adminCreateFirstAccount(person, account);
	}

	private boolean handleFirstAccountApprove(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException, SQLException {

		String sessionPersonId = (String) request.getAttribute("personId");

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());

		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = (String) request.getAttribute("branchId");
		String customerId = rootNode.path("person_id").asText();
		String accountType = rootNode.path("account_type").asText();
		Account account = AccountFactory.createAccount(sessionPersonId, branchId, customerId, accountType);

		return service.approveFirstAccount(account);

	}

private boolean handleFirstAccountRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException, TaskException, SQLException {

    String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
    JsonNode rootNode = mapper.readTree(jsonBody);

    String name = rootNode.path("name").asText().trim();
    String email = rootNode.path("email").asText().trim();
    String dob = rootNode.path("dob").asText().trim();
    String phone = rootNode.path("phone_number").asText().trim();
    String address = rootNode.path("address").asText().trim();
    String aadhar = rootNode.path("aadhar").asText().trim();
    String pan = rootNode.path("pan").asText().trim();
    String branchId = rootNode.path("branch_id").asText().trim();
    String accountType = rootNode.path("account_type").asText().trim();
    String password = rootNode.path("password").asText().trim();

    
    if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(email) || !ValidationUtil.isNotEmpty(dob)
            || !ValidationUtil.isNotEmpty(phone) || !ValidationUtil.isNotEmpty(address)
            || !ValidationUtil.isNotEmpty(aadhar) || !ValidationUtil.isNotEmpty(pan)
            || !ValidationUtil.isNotEmpty(branchId) || !ValidationUtil.isNotEmpty(accountType)
            || !ValidationUtil.isNotEmpty(password)) {
        return sendJsonError(response, "All fields are required.");
    }

    if (!ValidationUtil.isValidEmail(email)) {
        return sendJsonError(response, "Invalid email format.");
    }

    if (!ValidationUtil.isValidPhone(phone)) {
        return sendJsonError(response, "Phone number must be 10 digits.");
    }

    if (!ValidationUtil.isValidAadhar(aadhar)) {
        return sendJsonError(response, "Aadhar number must be 12 digits.");
    }

    if (!ValidationUtil.isValidPan(pan)) {
        return sendJsonError(response, "Invalid PAN format.");
    }

    if (!ValidationUtil.isValidPassword(password)) {
        return sendJsonError(response, "Password must be at least 6 characters.");
    }

    // Proceed with account creation
    Person person = PersonFactory.createPerson(null, jsonBody, "PENDING", "CUSTOMER");

    AccountRequest acRequest = new AccountRequest(
            person.getPersonId(), branchId, accountType, "PENDING",
            System.currentTimeMillis(), null, person.getPersonId());

    return service.requestFirstAccount(acRequest, person);
}
private boolean sendJsonError(HttpServletResponse response, String message) throws IOException {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", true);
    errorResponse.put("message", message);

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); 
    mapper.writeValue(response.getWriter(), errorResponse);

    return false;
}

}
