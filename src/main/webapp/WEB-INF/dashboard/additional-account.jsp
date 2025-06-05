<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/additional-account.css">
</head>
<body>
<div class="section" id="openadditionalaccount">
  <h2>Open Additional Account</h2>
  <form onsubmit="createAccount(); return false;">
    <div class="row">
    <c:if test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'SUPERADMIN'}">
  <div class="input-group">
    <input type="text" name="personId" id="personId" placeholder="Enter Person ID" required />
  </div>
</c:if>

      <div class="input-group">
        <select name="branchId" id="branchId" required>
          <option value="">Select Branch</option>
          <c:forEach var="branch" items="${branches}">
            <option value="${branch.branchId}">${branch.location} (${branch.branchId})</option>
          </c:forEach>
        </select>
      </div>
      <div class="input-group">
        <select name="accountType" id="accountType" required>
          <option value="">Account Type</option>
          <option value="SAVINGS">Savings Account</option>
          <option value="CURRENT">Current Account</option>
          <option value="SALARY">Salary Account</option>
        </select>
      </div>
      <button type="submit" id="create-account-btn">Create Account</button>
    </div>
  </form>
</div>

<script>
function createAccount() {
	 const personIdField = document.getElementById("personId");
  const data = {
    branch_id: document.getElementById("branchId").value,

    account_type: document.getElementById("accountType").value
  };

  if (personIdField) {
	    const personId = personIdField.value;
	    data.person_id = personId;
	  }
  fetch("${pageContext.request.contextPath}/bank/customer/additional-account", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  })
  .then(response => {
    if (response.ok) {
      alert("Account created successfully");
    } else {
      alert("Account creation failed");
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
