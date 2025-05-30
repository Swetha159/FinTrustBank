<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/myaccounts.css">
</head>
<body>

<div class="section" id="myaccounts">
  <h2 class="heading">My Accounts</h2>
  
  <table id ="accounts">

    <thead  class ="row-head">
      <tr>
        <th>S.No</th>
        <th>Account Number</th>
        <th>Branch</th>
        <th>Account Type</th>
        <th>Set As Primary Account</th>
      </tr>
    </thead>

    <tbody >
      <c:forEach var="account" items="${accounts}" varStatus="status">
        <tr class= "row-data">
          <td>${status.index + 1}</td>
          <td>${account.account_no}</td>
          <td>${account.location}</td>
          <td>${account.account_type}</td>
          <td>
            <form action="${pageContext.request.contextPath}/bank/setPrimary" method="post">
              <input type="hidden" name="account_no" value="${account.account_no}" />
              <button type="submit">Set as Primary</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>



</body>
</html>