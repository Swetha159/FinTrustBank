<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Forgot Password</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgot-password.css">
  <style>
    #responseMessage {
      margin-top: 15px;
      font-weight: bold;
      color: #155724; s
    }

    #responseMessage.error {
      color: #721c24; 
    }
  </style>
</head>

<body>
  <form onsubmit="event.preventDefault(); sendResetLink();">
    <label>Email:</label>
    <input type="email" id="email" name="email" required />
    <button type="submit">Send Reset Link</button>
     <div id="responseMessage"></div>
  </form>

 

  <script>
    function sendResetLink() {
      const email = document.getElementById("email").value.trim();
      const messageBox = document.getElementById("responseMessage");

      fetch("${pageContext.request.contextPath}/bank/forgot-password", {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ email })
      })
      .then(response => response.json())
      .then(data => {
        if (data.message) {
          messageBox.textContent = data.message;
          messageBox.className = "";
        } else {
          messageBox.textContent = data.error || "Something went wrong";
          messageBox.className = "error";
        }
      })
      .catch(() => {
        messageBox.textContent = "Something went wrong";
        messageBox.className = "error";
      });
    }
  </script>
</body>
</html>
