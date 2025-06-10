<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login | FinTrust</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>
  <jsp:include page="/WEB-INF/views/alert.jsp" />
  <div class="card">
    <div class="login-container">
      <h2>Login</h2>
      <form  id= "loginform" >
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
        <a href="#">Create New Account</a>
      </div>
    </div>
  </div>
  <script type="text/javascript">
  document.getElementById("loginform").addEventListener("submit", function(e) {
      e.preventDefault(); 
      

      const form = e.target;
      const data = {
          email: form.email.value,
          password : form.password.value 
      };

      fetch("<%= request.getContextPath() %>/login", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify(data)
      })
      .then(response => {
    if (response.type === 'opaqueredirect') {
        window.location.reload();
    } else if (response.status === 302 || response.status === 301) {
        const redirectUrl = response.headers.get("Location");
        if (redirectUrl) {
            window.location.href = redirectUrl;
        } else {
            window.location.reload();
        }
    } else {
        return response.json();
    }
})
.then(result => {
    if (result) {
        if (result.success) {
            window.location.href = result.redirectUrl;
        } else {
            alert(result.message);
        }
    }
})
      .catch(error => {
          console.error("Fetch error:", error);
          alert("Error communicating with the server.");
      });
  }); 
  
  
  
  </script>
</body>
</html>
