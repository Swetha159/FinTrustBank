<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Account Statement</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/accountstatement.css">
</head>
<body>
<div class="section" id="statement">
  <h2 class="heading">Account Statement</h2>
<form method="post" action="${pageContext.request.contextPath}/bank/history" id="statementform">
  <input type="hidden" name="offset" value="0" id="offsetField" />

  <div class="input-group">
    <p class="dropdown-label">Select Your Account</p>
    <c:choose>
      <c:when test="${sessionScope.role == 'ADMIN' or sessionScope.role == 'SUPERADMIN'}">
        <input type="text" name="account_no" id = "account_no" value="${accountNo}" />
      </c:when>
      <c:otherwise>
        <select name="account_no">
          <c:forEach var="accNo" items="${sessionScope.account_no_list}">
            <option value="${accNo['account_no']}" 
              <c:if test="${accountNo == accNo['account_no']}">selected</c:if>>
              ${accNo['account_no']}
            </option>
          </c:forEach>
        </select>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="input-group">
    <label for="startDate">Start Date</label>
    <input type="date" id="start_date" name="start_date" value="${param.start_date}" />
  </div>

  <div class="input-group">
    <label for="endDate">End Date</label>
    <input type="date" id="end_date" name="end_date" value="${param.end_date}" />
  </div>

  <button class="btn" type="submit">View Statement</button>
</form>

  <div class="statement-table-container">
    <table class="statement-table" border="1" cellpadding="5" cellspacing="0">
      <thead>
        <tr>
          <th>S.No</th>
          <th>Transaction ID</th>
          <th>Account No</th>
          <th>Amount</th>
          <th>Type</th>
          <th>Date & Time</th>
          <th>Description</th>
          <th>Balance</th>
        </tr>
      </thead>
      <tbody id="statement-body">
        <c:forEach var="row" items="${transaction_list}" varStatus="status">
          <tr>
            <td>${offset + status.index + 1}</td>
            <td>${row.transaction_id}</td>
            <td>${row.transaction_account_no}</td>
            <td>${row.amount}</td>
            <td>${row.transaction_type}</td>
            <td>${row.date_time}</td>
            <td>${row.description}</td>
            <td>${row.available_balance}</td>
          </tr>
        </c:forEach>
        <c:if test="${empty transaction_list}">
          <tr><td colspan="8" style="text-align:center;">No Transactions Found</td></tr>
        </c:if>
      </tbody>
    </table>
  </div>
<form method="post" action="${pageContext.request.contextPath}/bank/history" style="display: inline;">
  <input type="hidden" name="account_no" value="${accountNo}" />
  <input type="hidden" name="offset" value="${offset - 10}" />
  <input type="hidden" name="start_date" value="${param.start_date}" />
  <input type="hidden" name="end_date" value="${param.end_date}" />
  <button class="btn" type="submit" ${!hasPrev ? "disabled" : ""}>Previous</button>
</form>


<form method="post" action="${pageContext.request.contextPath}/bank/history" style="display: inline;">
  <input type="hidden" name="account_no" value="${accountNo}" />
  <input type="hidden" name="offset" value="${offset + 10}" />
  <input type="hidden" name="start_date" value="${param.start_date}" />
  <input type="hidden" name="end_date" value="${param.end_date}" />
  <button class="btn" type="submit" ${!hasNext ? "disabled" : ""}>Next</button>
</form>


</div>
</body>
</html>
