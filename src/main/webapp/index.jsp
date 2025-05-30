<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FINTRUST BANK</title>
  <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <jsp:include page="WEB-INF/views/common/header/header.jsp"/>
     <div class = "section1">
     <h1>
     Your First Step Towards
     </h1>
     <h1>
     FINANCIAL FREEDOM
     </h1>
 
     <h3>
     Open Your Account Today
     </h3>
     <form action="bank/account-request" method="get">
  <button class="new-account-button">Open New Account</button>
</form>
        
     </div>
	  
	  <div class ="section2">
	   <h1>
    Simple NetBanking Steps
     </h1>
       <ul>
    <li>Login</li>
    <li>Enter Transaction Account Number</li>
    <li>Enter Amount</li>
    <li>Send Money</li>
  </ul>
	  </div>
	
<div class="section3">
  <h1>Explore Our Accounts</h1>
  <div class="cards-wrapper">
    <div class="cards">
      <div class="card">
        <img src="img_avatar.png" alt="Avatar" style="width:100%">
        <div class="container">
          <h4><b>Salary Account</b></h4> 
          <p>Special Account for employees to receive monthly salaries</p> 
          <a href="#">Learn more</a>
        </div>
      </div>

      <div class="card">
        <img src="img_avatar.png" alt="Avatar" style="width:100%">
        <div class="container">
          <h4><b>Savings Account</b></h4> 
          <p>Ideal for individuals to save money</p> 
          <a href="#">Learn more</a>
        </div>
      </div>

      <div class="card">
        <img src="img_avatar.png" alt="Avatar" style="width:100%">
        <div class="container">
          <h4><b>Current Account</b></h4> 
          <p>Designed for business and traders. Unlimited Transactions</p> 
          <a href="#">Learn more</a>
        </div>
      </div>
    </div>
  </div>
</div>

	    <jsp:include page="WEB-INF/views/common/footer/footer.jsp"/>
</body>
</html>