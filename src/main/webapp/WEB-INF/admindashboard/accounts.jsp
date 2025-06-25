<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Account List</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account-requests.css">
</head>
<body>

<c:if test="${not empty param.alertMessage}">
  <c:set var="alertMessage" value="${param.alertMessage}" scope="request" />
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>

<h2>Account List</h2>

<table>
  <thead>
    <tr>
      <th>Customer ID</th>
      <th>Customer Name</th>
      <th>Account No</th>
      <th>Account Type</th>
      <th>Balance</th>
      <th>Status</th>
      <th>Created At</th>
      <th>Modified At</th>
      <th>Modifier Name</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="account" items="${accounts}">
      <tr>
        <td>${account.customer_id}</td>
        <td>${account.name}</td>
        <td>${account.account_no}</td>
        <td>${account.account_type}</td>
        <td>${account.balance}</td>
   <td>
 <select name="status_${account.account_no}" id="status_${account.account_no}"
        onfocus="storeOldStatus(this)"
        onchange="handleStatusChange(this, '${account.account_no}')">

        <c:forEach var="s" items="${['ACTIVE','INACTIVE']}">
          <option value="${s}" <c:if test="${account.account_status == s}">selected</c:if>>${s}</option>
        </c:forEach>
      </select>
    </td>
       
        <td>${account.created_at}</td>
        <td>${account.modified_at}</td>
        <td>${account.modifier_name}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<script type="text/javascript">


let oldStatusMap = {};

function storeOldStatus(selectElement) {
    oldStatusMap[selectElement.id] = selectElement.value;
}

async function handleStatusChange(selectElement, accountNo) {
    const newStatus = selectElement.value;
    const oldStatus = oldStatusMap[selectElement.id];

    if (newStatus === oldStatus) return;

    const message = [
  	  "Do you want to change the status from",
  	  oldStatus,
  	  "to",
  	  newStatus + "?"
  	].join(" ");
  	const confirmChange = confirm(message);

  if (!confirmChange) {
      selectElement.value = oldStatus;
      return;
  }

    try {
        const response = await fetch('${pageContext.request.contextPath}/bank/account/status', {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                account_no: accountNo,
                status: newStatus
            })
        });

        if (response.ok) {
            alert("Status updated successfully.");
            oldStatusMap[selectElement.id] = newStatus;
        } else {
            alert("Failed to update status.");
            selectElement.value = oldStatus;
        }
    } catch (error) {
        alert("Error: " + error);
        selectElement.value = oldStatus;
    }
}</script>
</body>
</html>
