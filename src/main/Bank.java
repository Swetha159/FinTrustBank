package com.bank.fintrustbank;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Bank extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Bank() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String endpoint = request.getPathInfo();
		switch(endpoint)
		{
		case "/home":
			break;
		case "/allbranch":
			break;
			
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String endpoint = request.getPathInfo();
		switch(endpoint)
		{

		case "/login":
		    LoginHandler.handleLogin(request,response);
			break;
		case "/newaccount":
			NewAccountHandler.handleNewAccount(request,response);
			break;
		case "/dashboard":
			DashboardHandler.handleDashboard(request,response);
			break;
		case "/lasttransactions":
			DashboardHandler.handleLastTransactions(request,response);
			break;
		case "/overallbalance":
			DashboardHandler.handleOverallBalance(request,response);
			break;
		case "totalcredits":
			DashboardHandler.handleTotalCredits(request,response);
			break;
		case "/totaldebits" :
			DashboardHandler.handleTotalCredits(request,response);
			break;
		case "/myprofile" :
			CustomerHandler.handleMyProfile(request,response);
			break;
		case "/branch" :
			BranchHandler.handleBranch(request,response);
			break;
		case "/nominee" :
			NomineeHandler.handleNominee(request,response);
			break;
		case "/accounts" :
			AccountsHandler.handleAccounts(request,response);
			break;
		case "/addnominee" :
			NomineeHandler.handleAddNominee(request,response);
			break;
		case "/statement" :
			TransactionHandler.handleStatement(request,response);
			break;
		case "/addadmin" :
			AdminHandler.handleAddNominee(request,response);
			break;
		case "/addbranch" :
			BranchHandler.handleBranch(request,response);
			break;
		case "/addaccount" :
			NewAccountHandler.handleNewAccount(request,response);
			break;
		case "/transaction" :
			TransactionHandler.handleTransaction(request,response);
			break;
		case "/setcredentials" :
			CredentialsHandler.handleSetCredentials(request,response);
			break;
		case "/resetcredentials" :
			CredentialsHandler.handleSetCredentials(request,response);
			break;
		case "/request" :
			RequestHandler.handleRequest(request,response);
			break;
		case "/customers" :
			CustomerHandler.handleCustomers(request,response);
			break;
		case "/admins" :
			AdminHandler.handleAdmins(request,response);
			break;
		case "/deleterequest" :
			RequestHandler.handleDeleteRequest(request,response);
			break;
		case "/branchaccount" :
			BranchHandler.handleBranchAccount(request,response);
			break;
		case "/searchaccount" :
			AccountHandler.handleSearchAccount(request,response);
			break;
		case "/editcustomer" :
			CustomerHandler.handleEditCustomer(request,response);
			break;
		case "/editbranch" :
			BranchHandler.handleEditBranch(request,response);
			break;
		case "/editadmin" :
			AdminHandler.handleEditAdmin(request,response);
			break;
		case "/editaccount" :
		AccountHandler.handleEditAccount(request,response);
			break;
			
		}
		
		
		doGet(request, response);
	}
}



