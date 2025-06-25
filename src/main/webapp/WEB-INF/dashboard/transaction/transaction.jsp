<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Transaction</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/transaction.css">

</head>
<body>
<c:if test="${not empty errorList}">
  <div style="color:red;">
    <ul>
      <c:forEach var="err" items="${errorList}">
        <li>${err}</li>
      </c:forEach>
    </ul>
  </div>
</c:if>
	<div class="section" id="transaction">
		<h2 class="heading">Transaction</h2>

		<label class="checkbox-label"> <input type="checkbox"
			id="other_bank" name="otherBank" value="true"> Other Bank
			Transaction
		</label>
		
		<c:if test="${role == 'CUSTOMER'}">
  <label class="checkbox-label">
    <input type="checkbox" id="self_transfer" name="selfTransfer" value="true"> Self Transfer
  </label>
</c:if>
		<div class="input-group">
			<c:choose>
				<c:when
					test="${role == 'ADMIN' or role == 'SUPERADMIN'}">
					<input type="text" id="account_no" name="accountNo" required>
					<label for="accountNo">Account No</label>
				</c:when>
				<c:otherwise>
					<p class="dropdown-label">Account Number</p>
					<select id="account_no" name="account_no" required>
						<c:forEach var="accNo" items="${account_no_list}">
							<option value="${accNo['account_no']}">${accNo['account_no']}</option>
						</c:forEach>
					</select>
				</c:otherwise>
			</c:choose>
		</div>
	<div class="input-group" id="transaction_account_group">
  <c:choose>
    <c:when test="${role == 'CUSTOMER'}">
      <div id="selfTransactionWrapper">
        <input type="text" id="transaction_account_no" list="recentAccounts" required> 
        <label for="transaction_account_no">Transaction Account No</label>
        <datalist id="recentAccounts"></datalist>
      </div>

      <div id="selfAccountSelectWrapper" style="display: none;">
        <select id="self_account_no" required>
          <c:forEach var="accNo" items="${account_no_list}">
            <option value="${accNo['account_no']}">${accNo['account_no']}</option>
          </c:forEach>
        </select>
     
      </div>
    </c:when>

    <c:otherwise>
      <input type="text" id="transaction_account_no" list="recentAccounts" required> 
      <label for="transaction_account_no">Transaction Account No</label>
      <datalist id="recentAccounts"></datalist>
    </c:otherwise>
  </c:choose>
</div>


		<input type="hidden" id="transaction_type" name="transaction_type"
			value="DEBIT">
		<div class="input-group" id="ifsc_group">
			<input type="text" id="ifsc_code" name="ifsc" required> <label
				for="ifsc">IFSC Code</label>
		</div>

		<div class="input-group gpay-amount">
			<span class="currency-symbol">₹</span> <input type="text" id="amount"
				name="amount" required>
		</div>

		<div class="input-group">
			<textarea id="description" name="description"
				placeholder="Description" rows="4" required></textarea>

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


  document.addEventListener("DOMContentLoaded", function () {
	  const accountNoInput = document.getElementById("account_no");
	  const dataList = document.getElementById("recentAccounts");

	  accountNoInput.addEventListener("change", function () {
	    const account_no = this.value;

	    fetch(`${pageContext.request.contextPath}/bank/beneficiary`, {
	      method: "POST",
	      headers: {
	        "Content-Type": "application/json"
	      },
	      body: JSON.stringify({ account_no })
	    })
	      .then(response => {
  console.log("Fetch response status:", response.status);
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  return response.json();
})
	      .then(data => {
	    	  console.log("Received:", data);
	        while (dataList.firstChild) {
	          dataList.removeChild(dataList.firstChild);
	        }

	        data.forEach(account => {
	        	  const option = document.createElement("option");  
	        	  option.value = account.transaction_account_no;
		            option.textContent = account.name.trim()+"-"+account.transaction_account_no;      
		            console.log("Adding option:", option.value, "Text:", option.textContent);
		            dataList.appendChild(option);
	        	  console.log("option:", option);
	        	  dataList.appendChild(option);
	        	});
	      })
	      .catch(error => {
	        console.error("Error fetching recent accounts:", error);
	      });
	  });
	});
  const selfTransferCheckbox = document.getElementById("self_transfer");
  const selfWrapper = document.getElementById("selfTransactionWrapper");
  const selectWrapper = document.getElementById("selfAccountSelectWrapper");

  if (selfTransferCheckbox) {
    selfTransferCheckbox.addEventListener("change", function () {
      if (this.checked) {
        selfWrapper.style.display = "none";
        selectWrapper.style.display = "block";
      } else {
        selfWrapper.style.display = "block";
        selectWrapper.style.display = "none";
      }
    });
  }
  
  document.addEventListener("DOMContentLoaded", function () {
	  const selfCheckbox = document.getElementById("self_transfer");
	  const otherBankCheckbox = document.getElementById("other_bank");
	  const ifscGroup = document.getElementById("ifsc_group");
	  const selfWrapper = document.getElementById("selfTransactionWrapper");
	  const selectWrapper = document.getElementById("selfAccountSelectWrapper");

	  
	  if (selfCheckbox) selfCheckbox.checked = false;
	  if (otherBankCheckbox) otherBankCheckbox.checked = false;
	  if (ifscGroup) ifscGroup.style.display = "none";
	  if (selfWrapper) selfWrapper.style.display = "block";
	  if (selectWrapper) selectWrapper.style.display = "none";

	  if (selfCheckbox) { 
	    selfCheckbox.addEventListener("change", function () {
	      if (this.checked) {
	        selfWrapper.style.display = "none";
	        selectWrapper.style.display = "block";

	        if (otherBankCheckbox) {
	          otherBankCheckbox.checked = false;
	          otherBankCheckbox.parentElement.style.display = "none";
	        }
	        if (ifscGroup) ifscGroup.style.display = "none";
	      } else {
	    	  
	        selfWrapper.style.display = "block";
	        selectWrapper.style.display = "none";
	        if (otherBankCheckbox) otherBankCheckbox.parentElement.style.display = "block";
	        if (otherBankCheckbox?.checked) ifscGroup.style.display = "block";
	        
	      }
	    });
	  }

	  if (otherBankCheckbox) {
	    otherBankCheckbox.addEventListener("change", function () {
	      ifscGroup.style.display = this.checked ? "block" : "none";

	      if (selfCheckbox) {
	        selfCheckbox.checked = false;
	        selfCheckbox.parentElement.style.display = this.checked ? "none" : "block";
	  
	        selfWrapper.style.display = "block";
	        selectWrapper.style.display = "none";
	      }
	    });
	  }
	});
  
  function isValidAccountNumber(accountNo) {
    return /^\d{12}$/.test(accountNo); 
  }

  function submitTransaction() {
    const isSelfTransfer = document.getElementById("self_transfer")?.checked;
    const account_no = document.getElementById("account_no").value.trim();
    const transaction_account_no = isSelfTransfer
      ? document.getElementById("self_account_no").value.trim()
      : document.getElementById("transaction_account_no").value.trim();
    const ifsc = document.getElementById("ifsc_code").value.trim();
    const amount = document.getElementById("amount").value.trim();
    const description = document.getElementById("description").value.trim();

    if (!isValidAccountNumber(account_no)) {
      alert("Sender account number must be exactly 12 digits.");
      return;
    }

    if (!isValidAccountNumber(transaction_account_no)) {
      alert("Receiver account number must be exactly 12 digits.");
      return;
    }

    if (account_no === transaction_account_no) {
      alert("Sender and receiver account numbers cannot be the same.");
      return;
    }
    if (amount === "" || isNaN(amount) || parseFloat(amount) <= 0) {
      alert("Please enter a valid amount.");
      return;
    }

    if (description === "") {
      alert("Please enter a description.");
      return;
    }
 
    const data = {
      other_bank: document.getElementById("other_bank").checked,
      transaction_account_no: transaction_account_no,
      ifsc_code: ifsc,
      amount: amount,
      description: description,
      account_no: account_no,
      transaction_type: document.getElementById("transaction_type").value
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
