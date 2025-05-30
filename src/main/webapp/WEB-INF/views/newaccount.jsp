<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title> New Account Form</title>
    
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/newaccount.css">
</head>

<body>
 <div class="page-wrapper">
    <jsp:include page="common/header/header.jsp" />

    
  <h2>Person Registration Form</h2>
    <form id="personForm">
    <div class="container">
       <div class="navigation-header">
      <div class="tab-switch">
            <button class="tab-button active"  data-tab="personal"  onclick="switchTab('personal')">Personal</button>
            <button class="tab-button" data-tab="bank"   onclick="switchTab('bank')">Bank</button>
        </div>
     
    </div>
        <div id="personal" class="tab-content active">
            <div class="row">
                <div class="input-group">
                    <input type="text" id="name" name="name" required>
                    <label for="name">Name</label>
                </div>
                <div class="input-group">
                    <input type="text" id="email" name="email" required>
                    <label for="email">Email</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="dob" name="dob" required>
                    <label for="dob">Date Of Birth(dd/mm/yyyy)</label>
                </div>
                <div class="input-group">
                    <input type="text" id="phonenumber" name="phonenumber" required>
                    <label for="phonenumber">Phone Number</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <input type="text" id="address" name="address" required>
                    <label for="address">Address</label>
                </div>
            </div>
            
             
            
            
            
            
        </div>

        <div id="bank" class="tab-content">
            <div class="row">
                <div class="input-group">
                    <input type="text" id="aadhar" name="aadhar" required>
                    <label for="aadhar">Aadhar</label>
                </div>
                <div class="input-group">
                    <input type="text" id="pan" name="pan" required>
                    <label for="pan">PAN</label>
                </div>
            </div>

            <div class="row">
                <div class="input-group">
                    <select name="branchId" id="branchId">
                        <option value="">Select Branch</option>
                        <c:forEach var="branch" items="${branches}">
                            <option value="${branch.getBranchId()}">${branch.getLocation()}${branch.getBranchId()}</option>
                                <p>Branch ID: ${branch.branchId}</p>
                        </c:forEach>
                    </select>
                </div>
                <div class="input-group">
                    
                    <select name="accountType">
                    
                        <option value="" selected>Account Type</option>
                        <option value="SAVINGS">Savings Account</option>
                        <option value="CURRENT">Current  Account</option>
                        <option value="SALARY">Salary Account</option>
                    </select>
                </div>
            </div>
 <div class="row">
			<div class="input-group">
          <input type="password" id="password" name="password" required>
          <label for="password">Password</label>
         
        </div>
        	<div class="input-group">
          <input type="password" id="confirmpassword" name="confirmpassword" required>
          <label for="confirmpassword">Confirm Password</label>
         
        </div>
         </div>
            <div class="row">
                <input type="checkbox" id="terms" name="terms" value="terms">
                <label for="terms"> I agree to the Terms and Conditions</label>
            </div>
            
             <div class="nav-buttons">
               <input type="submit" class="Submit" value="Submit">
        </div>
        </div>
    </div>
</form>
    <jsp:include page="common/footer/footer.jsp" />
</div>
    <script>
        function switchTab(tab) {
            document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
            document.getElementById(tab).classList.add('active');
            document.querySelector('.tab-button[data-tab="' + tab + '"]').classList.add('active');


        }
        
        document.getElementById("personForm").addEventListener("submit", function(e) {
            e.preventDefault(); 
            
            document.getElementById("bank").classList.add("active");

            const form = e.target;
            const data = {
               name: form.name.value,
                email: form.email.value,
                dob: form.dob.value,
                phone_number : form.phonenumber.value,
                address : form.address.value , 
                aadhar : form.aadhar.value , 
                pan :form.pan.value , 
                branch_id : form.branchId.value ,
                account_type : form.accountType.value , 
                password : form.confirmpassword.value 
            };

            fetch("<%= request.getContextPath() %>/bank/account-request", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (response.ok) {
                    alert("Person registered successfully!");
                   // form.reset();
                } else {
                    alert("Registration failed.");
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                alert("Error communicating with the server.");
            });
        }); 
        
        
    </script>
</body>

