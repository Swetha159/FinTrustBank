package com.bank.fintrustbank;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SBank")
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
			handleLogin(request,response);
			break;
		case "/newaccount":
			handleNewAccount(request,response);
			break;
		case "/dashboard":
			handleDashboard(request,response);
			break;
		case "/lasttransactions":
			handleLastTransactions(request,response);
			break;
		case "/overallbalance":
			handleOverallBalance(request,response);
			break;
		case "totalcredits":
			handleTotalCredits(request,response);
			break;
		case "/totaldebits" :
			handleTotalCredits(request,response);
			break;
		case "/myprofile" :
			handleMyProfile(request,response);
			break;
		case "/branch" :
			handleBranch(request,response);
			break;
		case "/nominee" :
			handleNominee(request,response);
			break;
		case "/accounts" :
			handleAccounts(request,response);
			break;
		case "/addnominee" :
			handleNominee(request,response);
			break;
		case "/statement" :
			handleStatement(request,response);
			break;
		case "/addadmin" :
			handleLogin(request,response);
			break;
		case "/addbranch" :
			handleBranch(request,response);
			break;
		case "/addaccount" :
			handleAddAccount(request,response);
			break;
		case "/transaction" :
			handleTransaction(request,response);
			break;
		case "/setcredentials" :
			handleSetCredentials(request,response);
			break;
		case "/resetcredentials" :
			handleResetCredentials(request,response);
			break;
		case "/request" :
			handleRequest(request,response);
			break;
		case "/customers" :
			handleCustomers(request,response);
			break;
		case "/admins" :
			handleAdmins(request,response);
			break;
		case "/deleterequest" :
			handleDeleteRequest(request,response);
			break;
		case "/branchaccount" :
			handleBranchAccount(request,response);
			break;
		case "/searchaccount" :
			handleSearchAccount(request,response);
			break;
		case "/editcustomer" :
			handleEditCustomer(request,response);
			break;
		case "/editbranch" :
			handleEditBranch(request,response);
			break;
		case "/editadmin" :
			handleEditAdmin(request,response);
			break;
		case "/editaccount" :
			handleEditAccount(request,response);
			break;
			
		}
		
		
		doGet(request, response);
	}


	private void handleEditAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleEditAdmin(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleEditBranch(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleEditCustomer(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleSearchAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleBranchAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleDeleteRequest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleAdmins(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleCustomers(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleAddAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleResetCredentials(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleSetCredentials(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleTransaction(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleStatement(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleAccounts(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleNominee(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleBranch(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleMyProfile(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleTotalCredits(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleOverallBalance(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleLastTransactions(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleDashboard(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleNewAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}


	private void handleLogin(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

}
