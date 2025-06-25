<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login | FinTrust</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>
<c:if test="${not empty alertMessage}">
  <jsp:include page="/WEB-INF/views/alert.jsp" />
</c:if>

  <div class="card">
    <div class="login-container">
      <h2>Login</h2>
      <form id="loginform" method="post" action="${pageContext.request.contextPath}/login">
      
        <div class="input-group">
          <input type="text" id="email" name="email" required>
          <label for="email">Email</label>
        </div>
        <div class="input-group">
          <input type="password" id="password" name="password" required>
          <label for="password">Password</label>
        <a href="${pageContext.request.contextPath}/bank/forgot-password">Forgot Password?</a>

        </div>
                  
        <button type="submit" class="login-button">Login</button>
      </form>
      
      <div class="login-links">
        <p>Don't have an account?</p>
        <a href="${pageContext.request.contextPath}/account-request">Create New Account</a>
      </div>
    </div>
  </div>
<%-- <script type="text/javascript">
  document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("loginform").addEventListener("submit", function(e) {
      e.preventDefault();

      const form = e.target;
      const data = {
        email: form.email.value,
        password: form.password.value
      };

      fetch("<%= request.getContextPath() %>/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      })
      .then(response => {
        if (!response.ok) {
          throw new Error("Login failed");
        }
        return response.json();
      })
      .then(result => {
        if (result.success) {
          window.location.href = result.redirectUrl;
        } else {
          alert(result.message || "Login failed");
        }
      })
      .catch(error => {
        console.error("Fetch error:", error);
        alert("Incorrect email or password.");
      });
    });
  });
</script> --%>

</body>
</html>
