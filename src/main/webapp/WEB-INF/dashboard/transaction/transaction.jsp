<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Transaction</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transaction.css">
  
</head>
<body>

<div class="section" id="transaction">
  <h2 class="heading">Transaction</h2>

  <label class="checkbox-label">
    <input type="checkbox" id="other_bank" name="otherBank" value = "true">
    Other Bank Transaction
  </label>

<div class="input-group">
   <p class="dropdown-label">Select Your Account</p>
  <select id="account_no">
    <c:forEach var="accNo" items="${sessionScope.account_no_list}">
      <option value="${accNo['account_no']}">${accNo['account_no']}</option>
    </c:forEach>
  </select>
</div>




  <div class="input-group">
    <input type="text" id="transaction_account_no" name="accountNo" required>
    <label for="accountNo">Account No</label>
  </div>
  <input type="hidden" id="transaction_type" name="transaction_type" value="DEBIT">
  <div class="input-group" id="ifsc_group">
    <input type="text" id="ifsc_code" name="ifsc" required>
    <label for="ifsc">IFSC Code</label>
  </div>

  <div class="input-group gpay-amount">
    <span class="currency-symbol">₹</span>
    <input type="text" id="amount" name="amount" required>
  </div>

  <div class="input-group">
    <textarea id="description" name="description"  placeholder="Description"  rows="4" required></textarea>

  </div>

  <button type="submit" class="submit-btn" onclick="submitTransaction()">Send</button>

</div>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const amountInput = document.getElementById("amount");
    amountInput.addEventListener("input", function () {
      this.value = this.value.replace(/[^\d.]/g, ''); 
    });
  });
  document.addEventListener("DOMContentLoaded", function () {
	    const amountInput = document.getElementById("transaction_account_no");
	    amountInput.addEventListener("input", function () {
	      this.value = this.value.replace(/[^\d.]/g, '');
	    });
	  });

  document.addEventListener("DOMContentLoaded", function () {
	  const checkbox = document.getElementById("other_bank");
	  const ifscGroup = document.getElementById("ifsc_group");

	  checkbox.addEventListener("change", function () {
	    ifscGroup.style.display = this.checked ? "block" : "none";
	  });

	  ifscGroup.style.display = checkbox.checked ? "block" : "none";
	});

  function submitTransaction() {
	    const data = {
	      other_bank: document.getElementById("other_bank").checked,
	      transaction_account_no: document.getElementById("transaction_account_no").value,
	      ifsc_code: document.getElementById("ifsc_code").value,
	      amount: document.getElementById("amount").value,
	      description: document.getElementById("description").value,
	      account_no :document.getElementById("account_no").value,
	      transaction_type : document.getElementById("transaction_type").value
	    };

	    fetch("${pageContext.request.contextPath}/bank/transaction", {
	      method: "POST",
	      headers: {
	        "Content-Type": "application/json"
	      },
	      body: JSON.stringify(data)
	    })
	    .then(response => {
	      if (response.ok) {
	        alert("Transaction successful");
	      
	      } else {
	        alert("Transaction failed");
	      }
	    })
	    .catch(error => {
	      console.error("Error:", error);
	      alert("Something went wrong");
	    });
	  }
</script>

</body>
</html>
