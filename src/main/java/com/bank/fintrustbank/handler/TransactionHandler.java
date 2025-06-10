package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.service.TransactionService;
import com.bank.fintrustbank.util.TimeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class TransactionHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();

	private final TransactionDAO dao = new TransactionDAO();
	TransactionService service = new TransactionService();

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

			}else if (path.equals("/creditcount")) {
				handleCreditCount(request, response);
			} else if (path.equals("/debitcount")) {
				handleDebitCount(request, response);
			}else if (path.equals("/history")) {
				HttpSession session = request.getSession(false);
				if (session == null || session.getAttribute("personId") == null) {
					request.setAttribute("errorMessage", "session expired");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

				}
				List<Map<String, Object>> transactions = 	handleTransactionHistory(request, response);
				System.out.println(transactions.toString());
				TimeFormatter.convertMillisToDateTime(transactions , "date_time");
				
				boolean hasNext = transactions.size() == 11;
				request.setAttribute("hasNext", hasNext);
				request.setAttribute("transaction_list", transactions);
				request.setAttribute("page" , "history");
				System.out.println(session.getAttribute("role"));
				if(session.getAttribute("role").equals("CUSTOMER"))
				{
					request.getRequestDispatcher("/bank/customer/dashboard").forward(request, response);
				}
				else if(session.getAttribute("role").equals("ADMIN"))
				{
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				}
				else if(session.getAttribute("role").equals("SUPERADMIN"))
				{
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				}
				else
				{
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}

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
				HttpSession session = request.getSession(false);
				if (session == null || session.getAttribute("personId") == null) {
					request.setAttribute("errorMessage", "session expired");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

				}
				
				request.setAttribute("page" , "deposit-withdraw");
			if(session.getAttribute("role").equals("ADMIN"))
				{
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				}
				else if(session.getAttribute("role").equals("SUPERADMIN"))
				{
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				}
				else
				{
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
			} else if (path.equals("/withdraw")) {
				request.setAttribute("mode", "withdraw");
				HttpSession session = request.getSession(false);
				if (session == null || session.getAttribute("personId") == null) {
					request.setAttribute("errorMessage", "session expired");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

				}
				
				request.setAttribute("page" , "deposit-withdraw");
			if(session.getAttribute("role").equals("ADMIN"))
				{
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				}
				else if(session.getAttribute("role").equals("SUPERADMIN"))
				{
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				}
				else
				{
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
			}
			else if (path.equals("/transaction")) {
				HttpSession session = request.getSession(false);
				if (session == null || session.getAttribute("personId") == null) {
					request.setAttribute("errorMessage", "session expired");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

				}
				
				request.setAttribute("page" , "transaction");
				if(session.getAttribute("role").equals("CUSTOMER"))
				{
					request.getRequestDispatcher("/bank/customer/dashboard").forward(request, response);
				}
				
				else if(session.getAttribute("role").equals("ADMIN"))
				{
					request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
				}
				else if(session.getAttribute("role").equals("SUPERADMIN"))
				{
					request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
				}
				else
				{
					request.setAttribute("errorMessage", "Access Restricted");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
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
		System.out.println("accountNo"+accountNo+"count"+creditCount);
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


	private void handleBeneficiaries(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
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
		 
      System.out.println("result" +jsonArray.toString());
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());

	}
	
	private List<Map<String, Object>>  handleTransactionHistory(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
		//BufferedReader reader = request.getReader();
		//String jsonBody = reader.lines().collect(Collectors.joining());
		//JsonNode rootNode = mapper.readTree(jsonBody);
	//	JsonNode accountNode = rootNode.path("account_no");
		Long accountNo = null;
		  String  accountNoParam = request.getParameter("account_no");
		  if(accountNoParam!=null )
		  {
			  accountNo = Long.parseLong(accountNoParam);
		  }
		  else
		  {
				 HttpSession session = request.getSession(false);
				   if (session == null || session.getAttribute("personId") == null) {
				       request.setAttribute("errorMessage", "session expired");
				       request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				       return null;
				   }
				   System.out.println("jadsgkhsdfjdfsgskdfsv");
				   accountNo = (Long) session.getAttribute("account_no");
			}
//		if (!accountNode.isMissingNode() && !accountNode.isNull() && accountNode.canConvertToLong()) {
//		    accountNo = accountNode.asLong();
//		
//		} 

	
		  request.setAttribute("accountNo", accountNo);
			/*
			 * String transactionStatus = rootNode.path("transaction_status").asText(); if
			 * (transactionStatus == null || transactionStatus.isEmpty()) {
			 * transactionStatus = "SUCCESS"; }
			 */
		  String transactionStatus  = request.getParameter("transaction_status")!=null ? request.getParameter("transaction_status") : "SUCCESS" ; 
			int offset = Integer.parseInt(request.getParameter("offset") != null ? request.getParameter("offset") : "0");

			
			boolean hasPrev = offset > 0;
			
			request.setAttribute("offset", offset);			
			request.setAttribute("hasPrev", hasPrev);

		//   int offset = rootNode.path("offset").asInt();
		 long startDate ;
		 long endDate ; 
		 System.out.println("accountNo");
		  String startDateParam = request.getParameter("start_date");
		  String endDateParam = request.getParameter("end_date");
		  
		System.out.println("hereeee"+startDateParam +endDateParam );

		  
			/*
			 * JsonNode startDateNode = rootNode.path("start_date"); JsonNode endDateNode =
			 * rootNode.path("end_date");
			 */

		   if ((startDateParam ==null || startDateParam.isEmpty()) && (endDateParam ==null) || endDateParam.isEmpty()) {
			   long beforeFiveDaysMillis = ZonedDateTime.now()
					    .minusDays(5)
					    .withHour(0)
					    .withMinute(0)
					    .withSecond(0)
					    .withNano(0)
					    .toInstant()
					    .toEpochMilli();
			   
			   		startDate = beforeFiveDaysMillis ;
							long currentMillis = System.currentTimeMillis();
							endDate = currentMillis;
							
		   }
		   else
		   {
				  try {
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				        Date start = sdf.parse(startDateParam);
				        Date end = sdf.parse(endDateParam);

				        startDate = start.getTime();

				        Calendar calendar = Calendar.getInstance();
				        calendar.setTime(end);
				        calendar.add(Calendar.DATE, 1); 
				        calendar.add(Calendar.MILLISECOND, -1); 
				        endDate= calendar.getTimeInMillis();

				    } catch (ParseException e) {
				        e.printStackTrace(); 
				        throw new TaskException("Parsing Exception " ,e);
				    }
			  }
		   
		   return (List<Map<String, Object>>) dao.getTransactionHistory(accountNo ,offset ,startDate ,endDate ,  transactionStatus );
		
			
		}

	private List<Map<String, Object>> handleViewTransaction(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {

		BufferedReader reader = request.getReader();
		String jsonBody = reader.lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		String accountNo = rootNode.path("account_no").asText();

		return (List<Map<String, Object>>) dao.getTransactions(accountNo);

	}

	private boolean handleTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException, TaskException, SQLException, com.zoho.training.exceptions.TaskException {

HttpSession session = request.getSession(false);
if (session == null || session.getAttribute("personId") == null) {
    request.setAttribute("errorMessage", "session expired");
    request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
    return false; // Add this to prevent further execution
}

String sessionPersonId = (String) session.getAttribute("personId");

// Read the body ONCE and reuse it
String jsonBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

// Parse JSON from the body
Transaction debit = mapper.readValue(jsonBody, Transaction.class);
JsonNode rootNode = mapper.readTree(jsonBody);

// Fix the otherBank logic
boolean otherBank = rootNode.path("other_bank").asBoolean(false); // default to false

String IFSCCode = rootNode.path("ifsc_code").asText();
long transactionTime = System.currentTimeMillis();
if(otherBank)
{
	debit.setDescription(IFSCCode+" "+debit.getDescription());
}


debit.setDateTime(transactionTime);
debit.setTransactionBy(sessionPersonId);
debit.setCustomerId(sessionPersonId);
return service.processTransaction(debit, otherBank);
}


	public boolean handleDeposit(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException, TaskException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

		}
		String sessionPersonId = (String) session.getAttribute("personId");
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
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}
		String sessionPersonId = (String) session.getAttribute("personId");
		BufferedReader reader = request.getReader();

		Transaction debit = mapper.readValue(reader, Transaction.class);
		long transactionTime = System.currentTimeMillis();
		debit.setDateTime(transactionTime);
		debit.setTransactionBy(sessionPersonId);
		debit.setCustomerId(sessionPersonId);
		return service.processWithdraw(debit);

	}
}
