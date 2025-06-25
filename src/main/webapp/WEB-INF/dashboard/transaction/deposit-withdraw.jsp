<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/transaction.css">
</head>
<body>

	<div class="section" id="withdraw-deposit">
		
<c:choose>
  <c:when test="${requestScope.mode == 'deposit' || requestScope.mode == 'withdraw'}">
    <div class="heading">
      
      <input type="hidden" id="transaction_type" name="transaction_type"
             value="${requestScope.mode == 'deposit' ? 'credit' : 'debit'}" />
      <p style="margin-top: 6px;">
        ${requestScope.mode == 'deposit' ? 'Deposit' : 'Withdraw'}
      </p>
    </div>
  </c:when>
  <c:otherwise>
    <div class="input-group">
      <label for="transaction_type">Transaction Type</label>
      <select id="transaction_type" name="transaction_type" required>
        <option value="">-- Select --</option>
        <option value="credit">Deposit</option>
        <option value="debit">Withdraw</option>
      </select>
    </div>
  </c:otherwise>
</c:choose>
		<c:if test="${role == 'ADMIN' or role == 'SUPERADMIN'}">
			<div class="input-group">
				<input type="text" id="account_no" name="account_no"  placeholder="ACCOUNT NO" value="${requestScope.account_no}" required>
			</div>

			


			<div class="input-group gpay-amount">
				<span class="currency-symbol">₹</span>
				<input type="text" id="amount" name="amount" required>
			</div>

			<div class="input-group">
				<textarea id="description" name="description" placeholder="Description" rows="4" required></textarea>
			</div>

			<button type="submit" class="submit-btn" onclick="submitTransaction()">Submit</button>
		</c:if>
	</div>

	<script>
	document.addEventListener("DOMContentLoaded", function () {
		document.getElementById("amount").addEventListener("input", function () {
			this.value = this.value.replace(/[^\d.]/g, '');
		});
	});

	function submitTransaction() {
		const account_no = document.getElementById("account_no").value.trim();
		const transaction_type = document.getElementById("transaction_type").value;
		const amount = document.getElementById("amount").value.trim();
		const description = document.getElementById("description").value.trim();

		if (!transaction_type || !account_no || !amount) {
			alert("Please fill all required fields.");
			return;
		}

		const data = {
			account_no: account_no,
			transaction_type: transaction_type,
			amount: amount,
			description: description
		};

		let endpoint = "";

		if (transaction_type === "credit") {
			endpoint = "${pageContext.request.contextPath}/bank/deposit";
		} else if (transaction_type === "debit") {
			endpoint = "${pageContext.request.contextPath}/bank/withdraw";
		} else {
			alert("Invalid transaction type.");
			return;
		}

		fetch(endpoint, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(data)
		})
		.then(function(response) {
			if (response.ok) {
				alert("Transaction successful");
				document.getElementById("account_no").value = "";
				document.getElementById("amount").value = "";
				document.getElementById("description").value = "";
			} else {
				alert("Transaction failed");
			}
		})
		.catch(function(error) {
			console.error("Error:", error);
			alert("Something went wrong");
		});
	}

	</script>
</body>
</html>
