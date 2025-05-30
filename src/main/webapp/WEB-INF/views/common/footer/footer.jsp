<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/footer/footer.css">
  <title>Footer</title>
</head>
<body>
  <div class="footer-container">

    <div class="footer-top-row">
      <div class="location">
        <div class="footer-heading">Locate Us</div>
        <div class="footer-subtext">Find Branch and ATM near you</div>
      </div>
      <div class="social">
        <div class="footer-heading">Get Social</div>
        <img src="${pageContext.request.contextPath}/assets/fb.png" alt="fb" class="social-icon">
        <img src="${pageContext.request.contextPath}/assets/insta.webp" alt="instagram" class="social-icon">
        <img src="${pageContext.request.contextPath}/assets/x.webp" alt="X" class="social-icon">
        <img src="${pageContext.request.contextPath}/assets/email.webp" alt="email" class="social-icon">
      </div>
    

    
      <div class="contact">
        <div class="footer-heading">Contact Us</div>
        <div class="contact-container">
        <div class="contact-item">
          <img src="${pageContext.request.contextPath}/assets/tollfree.png" alt="tollfree" class="contact-icon">
          <div class="contact-text">Toll Free - 1800 123 789</div>
        </div>
        <div class="contact-item">
          <img src="${pageContext.request.contextPath}/assets/customercare.png" alt="customer care" class="contact-icon">
          <div class="contact-text">Customer Care - 99988 66777</div>
        </div>
        </div>
      </div>
      </div>
    <div class="footer-bottom-row">
      <div class="rights-links">
        <a href="#">Terms and Conditions</a>
        <a href="#">Code of Commit</a>
        <a href="#">Disclaimer</a>
      </div>

      <div class="copyright">
        <img src="${pageContext.request.contextPath}/assets/copyright.png" alt="copyright"
          class="copyright-icon">
        <div class="copyright-text">Fintrust Bank All Rights Reserved</div>
      </div>
    </div>

  </div>
</body>
</html>
