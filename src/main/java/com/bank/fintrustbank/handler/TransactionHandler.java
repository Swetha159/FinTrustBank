package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.service.TransactionService;
import com.bank.fintrustbank.util.TimeFormatter;
import com.bank.fintrustbank.util.ValidationUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class TransactionHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();

	private final TransactionDAO dao = new TransactionDAO();
	private final TransactionService service = new TransactionService();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		try {
			String path = request.getPathInfo();
			if (path.equals("/transaction")) {
				System.out.println("inside do post");
				handleTransaction(request, response);

			} else if (path.equals("/deposit")) {
				handleDeposit(request, response);

			} else if (path.equals("/withdraw")) {
				handleWithdraw(request, response);

			} else if (path.equals("/creditcount")) {
				handleCreditCount(request, response);
			} else if (path.equals("/debitcount")) {
				handleDebitCount(request, response);
			} else if (path.equals("/history")) {
				handleTransactionHistory(request, response);
		

			}
			if (path.equals("/beneficiary")) {

				handleBeneficiaries(request, response);

			}
		} catch (IOException | ServletException | SQLException | TaskException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		try {
			String path = request.getPathInfo();
			if (path.equals("/transactions")) {

				handleViewTransaction(request, response);

			} else if (path.equals("/deposit")) {
				request.setAttribute("mode", "deposit");
				

				request.setAttribute("page", "deposit-withdraw");
				if (request.getAttribute("role").equals("ADMIN")) {
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				} else if (request.getAttribute("role").equals("SUPERADMIN")) {
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				} else {
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
			} else if (path.equals("/withdraw")) {
				request.setAttribute("mode", "withdraw");
				

				request.setAttribute("page", "deposit-withdraw");
				if (request.getAttribute("role").equals("ADMIN")) {
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				} else if (request.getAttribute("role").equals("SUPERADMIN")) {
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				} else {
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
			} else if (path.equals("/transaction")) {
			

				request.setAttribute("page", "transaction");
				String role = (String) request.getAttribute("role");
				if(role.equals("CUSTOMER"))
				{
					 List<Map<String,Object>> accountNos = new AccountDAO().getAccountNo((String) request.getAttribute("personId"))	;
						
							  Long accountNo = (Long) accountNos.get(0).get("account_no");	
						
								request.setAttribute("accountNo", accountNo);
						  request.setAttribute("account_no_list", accountNos);
				}
				forwardToDashboardByRole(request, response);
			}
		} catch (IOException | SQLException | TaskException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	private void handleCreditCount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		Long accountNo = rootNode.path("account_no").asLong();

		Long creditCount = dao.getTotalCreditThisWeek(accountNo);
		System.out.println("accountNo" + accountNo + "count" + creditCount);
		Map<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("credit", creditCount);
		response.setContentType("application/json");
		mapper.writeValue(response.getWriter(), jsonResponse);

	}

	private void handleDebitCount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		Long accountNo = rootNode.path("account_no").asLong();

		Long debitCount = dao.getTotalDebitThisWeek(accountNo);
		Map<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("debit", debitCount);
		response.setContentType("application/json");
		mapper.writeValue(response.getWriter(), jsonResponse);

	}

	private void handleBeneficiaries(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {
		BufferedReader reader = request.getReader();
		String jsonBody = reader.lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		String accountNo = rootNode.path("account_no").asText();

		List<Map<String, Object>> recentAccounts = dao.getBeneficiaries(accountNo);

		JSONArray jsonArray = new JSONArray();

		for (Map<String, Object> account : recentAccounts) {
			JSONObject jsonObj = new JSONObject();
			for (Map.Entry<String, Object> entry : account.entrySet()) {
				jsonObj.put(entry.getKey(), entry.getValue());
			}
			jsonArray.put(jsonObj);
		}

		System.out.println("result" + jsonArray.toString());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());

	}

	private void handleTransactionHistory(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException, ServletException {
	
		 String sessionPersonId = (String) request.getAttribute("personId");
		 String role= (String) request.getAttribute("role");
		 Long accountNo = null;
		
		 if(role.equals("CUSTOMER"))
		 {
			 List<Map<String,Object>> accountNos = new AccountDAO().getAccountNo(sessionPersonId)	;
				String accountNoParam = request.getParameter("account_no");
				if (accountNoParam != null) {
					accountNo = Long.parseLong(accountNoParam);
				} 
				else{
					  accountNo = (Long) accountNos.get(0).get("account_no");	
				}
			   
				  request.setAttribute("account_no_list", accountNos);
		 }
		 else
			 
		 {
				String accountNoParam = request.getParameter("account_no");
				if (accountNoParam != null) {
					accountNo = Long.parseLong(accountNoParam);
				} 
				
		 }
		
		request.setAttribute("accountNo", accountNo);

		String transactionStatus = request.getParameter("transaction_status") != null
				? request.getParameter("transaction_status")
				: "SUCCESS";
		int offset = Integer.parseInt(request.getParameter("offset") != null ? request.getParameter("offset") : "0");

		boolean hasPrev = offset > 0;

		request.setAttribute("offset", offset);
		request.setAttribute("hasPrev", hasPrev);

		long startDate=0;
		long endDate =0;
		System.out.println("accountNo");
		String startDateParam = request.getParameter("start_date");
		String endDateParam = request.getParameter("end_date");

		System.out.println("hereeee" + startDateParam + endDateParam);

	
		List<String> errorList = new ArrayList<>();

	


		if ((startDateParam == null || startDateParam.isEmpty()) && (endDateParam == null) || endDateParam.isEmpty()) {
			long beforeFiveDaysMillis = ZonedDateTime.now().minusDays(5).withHour(0).withMinute(0).withSecond(0)
					.withNano(0).toInstant().toEpochMilli();

			startDate = beforeFiveDaysMillis;
			long currentMillis = System.currentTimeMillis();
			endDate = currentMillis;
			

		} else {
			 if (!ValidationUtil.isValidDateRange(startDateParam, endDateParam)) {
		            errorList.add("Invalid date range. Please check start and end dates.");
		        } else {
		            try {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

						if (!ValidationUtil.isValidDateRange(startDateParam, endDateParam)) {
						    errorList.add("Invalid date range. Please check start and end dates.");
						}
						Date start = sdf.parse(startDateParam);
						Date end = sdf.parse(endDateParam);
						
						

						startDate = start.getTime();

						Calendar calendar = Calendar.getInstance();
						calendar.setTime(end);
						calendar.add(Calendar.DATE, 1);
						calendar.add(Calendar.MILLISECOND, -1);
						endDate = calendar.getTimeInMillis();

		            } catch (ParseException e) {
		                e.printStackTrace();
		                throw new TaskException("Error parsing date range", e);
		            }
		        }
		    }

		   
			
		
		if (!ValidationUtil.isValidAccountNo(accountNo)) {
		    errorList.add("Account number must be exactly 12 digits.");
		}

		

		if (!errorList.isEmpty()) {
		    request.setAttribute("errorList", errorList);
		    request.setAttribute("page", "history");
		    forwardToDashboardByRole(request, response);
		    
		}

		
		List<Map<String, Object>> transactions = dao.getTransactionHistory(accountNo, offset, startDate, endDate,transactionStatus);
		System.out.println(transactions.toString());
		TimeFormatter.convertMillisToDateTime(transactions, "date_time");

		boolean hasNext = transactions.size() == 11;
		if (transactions.size() > 10) {
			transactions = transactions.subList(0, 10);
		}
		request.setAttribute("hasNext", hasNext);
		request.setAttribute("transaction_list", transactions);
		
		request.setAttribute("page", "history");
		forwardToDashboardByRole(request, response);


	}

	private void forwardToDashboardByRole(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String role = (String) request.getAttribute("role");
		if (role.equals("CUSTOMER")) {
			request.getRequestDispatcher("/bank/customer/dashboard").forward(request, response);
		} else if (role.equals("ADMIN")) {
			request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
		} else if (role.equals("SUPERADMIN")) {
			request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
		} else {
			request.setAttribute("errorMessage", "Access Restricted");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}
	}

	private List<Map<String, Object>> handleViewTransaction(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {

		BufferedReader reader = request.getReader();
		String jsonBody = reader.lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		String accountNo = rootNode.path("account_no").asText();

		return (List<Map<String, Object>>) dao.getTransactions(accountNo);

	}

	
	private boolean handleTransaction(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException, TaskException, SQLException {

	    String sessionPersonId = (String) request.getAttribute("personId");
	    String jsonBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

	    Transaction debit;
	    JsonNode rootNode;
	    try {
	        debit = mapper.readValue(jsonBody, Transaction.class);
	        rootNode = mapper.readTree(jsonBody);
	    } catch (Exception e) {
	        throw new TaskException("Invalid request body", e);
	    }

	    boolean otherBank = rootNode.path("other_bank").asBoolean(false);
	    String ifscCode = rootNode.path("ifsc_code").asText("");
	    String fromAccount = String.valueOf(debit.getAccountNo()).trim();
	    String toAccount = String.valueOf(debit.getTransactionAccountNo()).trim();
	    double amount = debit.getAmount();
	    String description = debit.getDescription() != null ? debit.getDescription().trim() : "";

	    
	    List<String> errorList = new ArrayList<>();

	    if (!fromAccount.matches("\\d{12}")) {
	        errorList.add("Sender account number must be exactly 12 digits.");
	    }

	    if (!toAccount.matches("\\d{12}")) {
	        errorList.add("Receiver account number must be exactly 12 digits.");
	    }

	    if (fromAccount.equals(toAccount)) {
	        errorList.add("Sender and receiver account numbers cannot be the same.");
	    }

	    if (amount <= 0) {
	        errorList.add("Transaction amount must be greater than 0.");
	    }

	    if (description.isEmpty()) {
	        errorList.add("Description cannot be empty.");
	    }

	    if (otherBank && (ifscCode.isEmpty() || !ifscCode.matches("^[A-Z]{4}0[A-Z0-9]{6}$"))) {
	        errorList.add("Invalid or missing IFSC code for other bank transaction.");
	    }

	    if (!errorList.isEmpty()) {
	        request.setAttribute("errorList", errorList);
	        request.setAttribute("page", "transaction");
	        forwardToDashboardByRole(request ,response );
	        return false;
	    }

	    if (otherBank) {
	        debit.setDescription(ifscCode + " " + description);
	    }

	    long transactionTime = System.currentTimeMillis();
	    debit.setDateTime(transactionTime);
	    debit.setTransactionBy(sessionPersonId);
	    debit.setCustomerId(sessionPersonId);

	    return service.processTransaction(debit, otherBank);
	}


	public boolean handleDeposit(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException, TaskException {
		
		String sessionPersonId = (String) request.getAttribute("personId");
		BufferedReader reader = request.getReader();

		Transaction credit = mapper.readValue(reader, Transaction.class);
		long transactionTime = System.currentTimeMillis();
		credit.setDateTime(transactionTime);
		credit.setTransactionBy(sessionPersonId);
		credit.setCustomerId(sessionPersonId);
		return service.processDeposit(credit);

	}

	public boolean handleWithdraw(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException, TaskException {
	
		String sessionPersonId = (String)request.getAttribute("personId");
		BufferedReader reader = request.getReader();

		Transaction debit = mapper.readValue(reader, Transaction.class);
		long transactionTime = System.currentTimeMillis();
		debit.setDateTime(transactionTime);
		debit.setTransactionBy(sessionPersonId);
		debit.setCustomerId(sessionPersonId);
		return service.processWithdraw(debit);

	}
}
