<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty alertMessage}">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/alert.css" />
  
  <div id="alertModal">
    <div>
      <p><strong>Alert:</strong> ${alertMessage}</p>
      <button id="alertCloseBtn">Close</button>
    </div>
  </div>

  <script>
    document.getElementById('alertCloseBtn').onclick = function() {
      document.getElementById('alertModal').style.display = 'none';
    };
    window.onclick = function(event) {
      if (event.target == document.getElementById('alertModal')) {
        document.getElementById('alertModal').style.display = 'none';
      }
    };
  </script>
</c:if>
