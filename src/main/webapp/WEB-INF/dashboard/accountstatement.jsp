<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Account Statement</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/accountstatement.css">
  <style>
    #date-fields {
      display: none;
      gap: 20px;
      flex-wrap: wrap;
    }
    .date-range-summary {
      margin-top: 10px;
      font-weight: bold;
      color: #2e5c8a;
    }
  </style>
  <script>
    function toggleDateFields() {
      const option = document.getElementById("view_option").value;
      const dateFields = document.getElementById("date-fields");
      const startDate = document.getElementById("start_date");
      const endDate = document.getElementById("end_date");

      if (option === "last_5_days") {
        dateFields.style.display = "none";
        startDate.removeAttribute("name");
        endDate.removeAttribute("name");
      } else {
        dateFields.style.display = "flex";
        startDate.setAttribute("name", "start_date");
        endDate.setAttribute("name", "end_date");

        if (!startDate.value || !endDate.value) {
          const today = new Date().toISOString().split("T")[0];
          startDate.value = today;
          endDate.value = today;
        }
      }
    }

    window.onload = () => {
      toggleDateFields();

      document.getElementById("view_option").addEventListener("change", toggleDateFields);

      document.getElementById("statementform").addEventListener("submit", function (e) {
        const accountNoInput = document.getElementById("account_no");
        const viewOption = document.getElementById("view_option").value;
        const startDate = document.getElementById("start_date").value;
        const endDate = document.getElementById("end_date").value;

        if (!accountNoInput || accountNoInput.value.trim() === "") {
          alert("Please enter or select an account number.");
          e.preventDefault();
          return;
        }

        const accountNo = accountNoInput.value.trim();

        if (!/^\d+$/.test(accountNo)) {
          alert("Account number must contain digits only.");
          e.preventDefault();
          return;
        }

         if (accountNo.length !== 12) {
          alert("Account number must be exactly 12 digits.");
          e.preventDefault();
          return;
        } 

        if (viewOption === "date_range") {
          if (!startDate || !endDate) {
            alert("Please provide both start and end dates.");
            e.preventDefault();
            return;
          }

          const start = new Date(startDate);
          const end = new Date(endDate);

          if (end < start) {
            alert("End date must be after or equal to start date.");
            e.preventDefault();
            return;
          }
        }
      });
    };
  </script>
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

<div class="section" id="statement">
  <h2 class="heading">Account Statement</h2>

  <form method="post" action="${pageContext.request.contextPath}/bank/history" id="statementform">
    <input type="hidden" name="offset" value="0" id="offsetField" />

    <div class="input-group">
      <p class="dropdown-label">Select Your Account</p>
      <c:choose>
        <c:when test="${role == 'ADMIN' or role == 'SUPERADMIN'}">
          <input type="text" name="account_no" id="account_no" value="${accountNo}" />
        </c:when>
        <c:otherwise>
          <select name="account_no" id="account_no">
            <c:forEach var="accNo" items="${account_no_list}">
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
      <label for="view_option">View Option</label>
      <select id="view_option" name="view_option">
        <option value="last_5_days" <c:if test="${param.view_option == 'last_5_days'}">selected</c:if>>Last 5 Days</option>
        <option value="date_range" <c:if test="${param.view_option == 'date_range'}">selected</c:if>>Specify Date Range</option>
      </select>
    </div>

    <div id="date-fields">
      <div class="input-group">
        <label for="start_date">Start Date</label>
        <input type="date" id="start_date" value="${param.start_date}" />
      </div>
      <div class="input-group">
        <label for="end_date">End Date</label>
        <input type="date" id="end_date" value="${param.end_date}" />
      </div>
    </div>

    <button class="btn" type="submit">View Statement</button>
  </form>

  <c:if test="${param.view_option == 'date_range'}">
    <div class="date-range-summary">
      Showing results from <strong>${param.start_date}</strong> to <strong>${param.end_date}</strong>
    </div>
  </c:if>

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

  <c:if test="${hasPrev}">
    <form method="post" action="${pageContext.request.contextPath}/bank/history" style="display: inline;">
      <input type="hidden" name="account_no" value="${accountNo}" />
      <input type="hidden" name="offset" value="${offset - 10}" />
      <input type="hidden" name="view_option" value="${param.view_option}" />
      <c:if test="${param.view_option == 'date_range'}">
        <input type="hidden" name="start_date" value="${param.start_date}" />
        <input type="hidden" name="end_date" value="${param.end_date}" />
      </c:if>
      <button class="btn" type="submit">Previous</button>
    </form>
  </c:if>

  <c:if test="${hasNext}">
    <form method="post" action="${pageContext.request.contextPath}/bank/history" style="display: inline;">
      <input type="hidden" name="account_no" value="${accountNo}" />
      <input type="hidden" name="offset" value="${offset + 10}" />
      <input type="hidden" name="view_option" value="${param.view_option}" />
      <c:if test="${param.view_option == 'date_range'}">
        <input type="hidden" name="start_date" value="${param.start_date}" />
        <input type="hidden" name="end_date" value="${param.end_date}" />
      </c:if>
      <button class="btn" type="submit">Next</button>
    </form>
  </c:if>

</div>
</body>
</html>
