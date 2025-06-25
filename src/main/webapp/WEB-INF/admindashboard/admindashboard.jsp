<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DASHBOARD</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admindashboard.css">
</head>
<body>

<jsp:include page="../dashboard/dashboardheader.jsp" />

<nav id="dashboard-menu" class="dashboard-navbar">
    <ul>
        <li>FINTRUST</li>
        <li><a href="${pageContext.request.contextPath}/bank/admin/dashboard">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/admin/requests">Account Requests</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/customers">Customers</a></li>
          <li><a href="${pageContext.request.contextPath}/bank/branch-accounts">Accounts</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/transaction">Transfer Money</a></li>
          <li><a href="${pageContext.request.contextPath}/bank/withdraw"> Withdraw </a></li>
            <li><a href="${pageContext.request.contextPath}/bank/deposit"> Deposit </a></li>
   <li>
  <form id="historyForm" method="post" action="${pageContext.request.contextPath}/bank/history" style="display: inline;">

    <a href="#" onclick="document.getElementById('historyForm').submit(); return false;">Account Statement</a>
  </form>
</li>

        <li><a href="${pageContext.request.contextPath}/bank/admin/additional-account">Additional Account Creation</a></li>
        <li><a href="${pageContext.request.contextPath}/bank/account-request">New Account Creation</a></li>
    </ul>
</nav>


<c:set var="pageAttr" value="${requestScope.page != null ? requestScope.page : 'home'}" />
<c:choose>
<c:when test="${pageAttr == 'home'}">
        <jsp:include page="home.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'requests'}">
        <jsp:include page="account-requests.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'customers'}">
        <jsp:include page="customers.jsp" />
    </c:when>
     <c:when test="${pageAttr == 'accounts'}">
        <jsp:include page="accounts.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'additional-account'}">
        <jsp:include page="../dashboard/additional-account.jsp" />
    </c:when>
    <c:when test="${pageAttr == 'transaction'}">
        <jsp:include page="../dashboard/transaction/transaction.jsp" />
    </c:when>
     <c:when test="${pageAttr == 'history'}">
        <jsp:include page="../dashboard/accountstatement.jsp" />
    </c:when>
      <c:when test="${pageAttr == 'deposit-withdraw'}">
        <jsp:include page="../dashboard/transaction/deposit-withdraw.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="notfound.jsp" />
    </c:otherwise>
</c:choose>


</body>
</html>