<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>

<div class="profile-container">
  <h2>Profile Details</h2>
  <div class="profile-card">
    <div class="profile-section">
      <p><strong>Person ID:</strong> ${person.person_id}</p>
      <p><strong>Name:</strong> ${person.name}</p>
      <p><strong>Email:</strong> ${person.email}</p>
      <p><strong>Phone Number:</strong> ${person.phone_number}</p>
      <p><strong>Date of Birth:</strong> ${person.dob}</p>
      <p><strong>Status:</strong> ${person.status}</p>
      <p><strong>Address:</strong> ${person.address}</p>
      <p><strong>Aadhar:</strong> ${person.aadhar}</p>
      <p><strong>PAN:</strong> ${person.pan}</p>
      <p><strong>Created At:</strong> ${person.created_at}</p>
      <p><strong>Modified At:</strong> ${person.modified_at}</p>
      <p><strong>Modified By:</strong> ${person.modifier_name}</p>
    </div>

  <div class="action-buttons">
  <a href="${pageContext.request.contextPath}/bank/reset-password">Reset Password</a>


</div>
<div class="token-buttons">
  <button type="button" id="generateTokenBtn">Get Long-Term Token</button>
  <button type="button" id="invalidateTokenBtn">Invalidate Token</button>
</div>

<div class="token-display" id="tokenDisplayContainer" style="display:none;">
  <button class="copy-btn" onclick="copyToken()">Copy</button>
  <p id="tokenText"><strong>Long-Term Token:</strong> <span id="tokenValue"></span></p>
</div>

<script>
  const contextPath = "${pageContext.request.contextPath}";
  const tokenDisplayContainer = document.getElementById("tokenDisplayContainer");
  const tokenValue = document.getElementById("tokenValue");

  document.getElementById("generateTokenBtn").addEventListener("click", function () {
    fetch(contextPath + "/bank/longterm-token", {
      method: "POST",
      credentials: "include"
    })
    .then(response => response.text())
    .then(token => {
      tokenValue.textContent = token;
      tokenDisplayContainer.style.display = "block";
    })
    .catch(error => {
      console.error("Error generating token:", error);
    });
  });

  document.getElementById("invalidateTokenBtn").addEventListener("click", function () {
    fetch(contextPath + "/bank/longterm-token/revocation", {
      method: "POST",
      credentials: "include"
    })
    .then(response => {
      if (response.ok) {
        tokenValue.textContent = "Token invalidated successfully.";
        tokenDisplayContainer.style.display = "block";
      } else {
        tokenValue.textContent = "Failed to invalidate token.";
        tokenDisplayContainer.style.display = "block";
      }
    })
    .catch(error => {
      console.error("Error invalidating token:", error);
    });
  });

  function copyToken() {
    const tokenText = tokenValue.textContent;
    navigator.clipboard.writeText(tokenText)
      .then(() => {
        alert("Token copied to clipboard!");
      })
      .catch(err => {
        console.error("Copy failed", err);
      });
  }
</script>

  </div>
</div>

</body>
</html>
