<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Forgot Password</title></head>
<body>
  <h2>Forgot Password</h2>
  <form action="${pageContext.request.contextPath}/bank/forgot-password" method="post">
    <label>Email:</label>
    <input type="email" name="email" required />
    <button type="submit">Send Reset Link</button>
  </form>
</body>
</html>
