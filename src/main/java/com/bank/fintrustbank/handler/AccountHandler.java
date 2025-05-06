package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.service.NewAccountService;
import com.bank.fintrustbank.util.AccountNumberGenerator;
import com.bank.fintrustbank.util.Password;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class AccountHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();

	private static final PersonDAO personDAO = new PersonDAO();
	private static final AccountDAO accountDAO = new AccountDAO();
	
	public static void handleSearchAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	public static void handleEditAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getPathInfo();
		if (path.equals("/new-account")) {
			try {
				handleNewAccount(request, response);
			} catch (IOException | TaskException | SQLException e) {

				e.printStackTrace();
			}

		}
	}
	

	public static boolean handleNewAccount(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, TaskException, SQLException {

	    HttpSession session = request.getSession(false); 
	    if (session == null || session.getAttribute("personId") == null) {
	        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired or unauthorized.");
	        return false;
	    }

	    String sessionPersonId = (String) session.getAttribute("personId");

	  
	    String jsonBody = new BufferedReader(request.getReader())
	            .lines()
	            .collect(Collectors.joining());

	    JsonNode rootNode = mapper.readTree(jsonBody);

	    boolean alreadyUser = rootNode.path("already_user").asBoolean();
	    String branchId = rootNode.path("branch_id").asText();
	    String customerId = rootNode.path("person_id").asText();
	    String accountType = rootNode.path("account_type").asText();
	       
	    if (!alreadyUser) {
	    	
	    	NewAccountService service = new NewAccountService();
	    	boolean result = service.createNewCustomerWIthAccount(createCustomer(sessionPersonId, jsonBody),createAccount(sessionPersonId, alreadyUser, branchId, customerId, accountType));

	    	return result;
	    	
	    	
	    }
	    else
	    {
	    	Account account = createAccount(sessionPersonId, alreadyUser, branchId, customerId, accountType);
	    	return accountDAO.addAccount(account);
	    	
	    }
	}
	private static Account createAccount(String sessionPersonId, boolean alreadyUser,
			String branchId, String customerId, String accountType) throws TaskException, IOException {
		Account account = new Account();
	
	    account.setPrimaryAccount(!alreadyUser);
	    account.setBranchId(branchId);
	    account.setAccountType(accountType);
	    account.setCustomerId(customerId);

	    account.setAccountNo(AccountNumberGenerator.generateAccountNumber(branchId));
	    account.setModifiedBy(sessionPersonId);
	    account.setCreatedAt(System.currentTimeMillis());
	    account.setStatus("ACTIVE");
	    account.setBalance(0.00);
	    
	    return account;
	        
	    
	}

	private static Person createCustomer(String sessionPersonId, String jsonBody)
			throws JsonProcessingException, JsonMappingException, TaskException, IOException {
		
		Person person = mapper.readValue(jsonBody, Person.class);


		String rawPassword = person.getDob().toString() + person.getAadhar().toString();
		person.setPassword(Password.createHash(rawPassword));
		
		person.setModifiedBy(sessionPersonId);
		person.setRole("CUSTOMER");
		person.setCreatedAt(System.currentTimeMillis());
		person.setModifiedAt(null);
		person.setStatus("ACTIVE");
		
		return  person;
	}
		
	
}