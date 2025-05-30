<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DASHBOARD</title>
</head>
<body>

<jsp:include page="dashboardheader.jsp" />

<nav id="dashboard-menu" class="dashboard-navbar">
    <ul>
        <li>FINTRUST</li>
        <li><a href="${pageContext.request.contextPath}/bank/customer/dashboard">Employees</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/allaccounts">Branches</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/transaction">Customers</a></li>
              <li><a href="${pageContext.request.contextPath}/bank/transaction">Accounts</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/history">Transfer Money</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/customer/additional-account">Account Statement</a></li>
    </ul>
</nav>

</body>
</html>