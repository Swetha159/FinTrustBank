<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Map, java.text.SimpleDateFormat, java.util.Date" %>
<%
    Map<String, Object> customer = (Map<String, Object>) request.getAttribute("person");

    String personId = (String) customer.get("person_id");
    String modifierId = (String) customer.get("modified_by");
    String modifierName = (String) customer.get("modifier_name");

    String name = (String) customer.get("name");
    String email = (String) customer.get("email");
    String phone = (String) customer.get("phone_number");
    Long aadhar = (Long) customer.get("aadhar");
    String pan = (String) customer.get("pan");
    String address = (String) customer.get("address");
    String status = (String) customer.get("status");
    String createdAt = (String) customer.get("created_at");
    String modifiedAt = (String) customer.get("modified_at");

    String formattedDob = "";
    java.sql.Date dobDate = (java.sql.Date) customer.get("dob");
    if (dobDate != null) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        formattedDob = sdf.format(dobDate);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Customer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer-edit.css" />
</head>
<body>

<div class="container">
    <h2>Edit Customer Details</h2>
    <input type="hidden" name="target_role" id="target_role" value="${requestScope.target_role}" />

    <form id="updateForm">
        <div class="row">
            <div class="column">
                <label>Person ID:</label>
                <input type="text" name="person_id" value="<%= personId %>" readonly />
            </div>
            <div class="column">
                <label>Status:</label>
                <input type="text" name="status" value="<%= status %>" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Name:</label>
                <input type="text" id="name" name="name" value="<%= name %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Email:</label>
                <input type="email" id="email" name="email" value="<%= email %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Phone Number:</label>
                <input type="text" id="phone_number" name="phone_number" value="<%= phone %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Date of Birth:</label>
                <input type="text" id="dob" name="dob" value="<%= formattedDob %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Address:</label>
                <input type="text" id="address" name="address" value="<%= address %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Aadhar:</label>
                <input type="text" id="aadhar" name="aadhar" value="<%= aadhar %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>PAN:</label>
                <input type="text" id="pan" name="pan" value="<%= pan %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Created At:</label>
                <input type="text" name="created_at" value="<%= createdAt %>" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Modified At:</label>
                <input type="text" name="modified_at" value="<%= modifiedAt %>" readonly />
            </div>
            <div class="column">
                <label>Modified By:</label>
                <%
                    if (modifierId.equals(personId)) {
                %>
                    <input type="text" value="Self" readonly />
                <%
                    } else {
                %>
                    <a class="modifier-link" onclick="fetchModifierDetails('<%= modifierId %>')"><%= modifierName %></a>
                <%
                    }
                %>
            </div>
        </div>

        <div class="button-group">
            <button type="button" id="editButton" onclick="enableEditing()">Update</button>
            <button type="button" id="saveButton" style="display: none;" onclick="saveChanges()">Save Changes</button>
        </div>
    </form>
</div>

<script>
function enableEditing() {
  document.querySelectorAll(".editable").forEach(input => input.removeAttribute("readonly"));
  document.getElementById("editButton").style.display = "none";
  document.getElementById("saveButton").style.display = "inline-block";
}

function saveChanges() {
  const form = document.getElementById("updateForm");
  const formData = new FormData(form);
  const jsonData = {};

  const name = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim();
  const phone = document.getElementById("phone_number").value.trim();
  const dob = document.getElementById("dob").value.trim();
  const address = document.getElementById("address").value.trim();
  const aadhar = document.getElementById("aadhar").value.trim();
  const pan = document.getElementById("pan").value.trim();

  const emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
  const phonePattern = /^\d{10}$/;
  const aadharPattern = /^\d{12}$/;
  const panPattern = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;

  if (!name) {
    alert("Name cannot be empty.");
    return;
  }
  if (!emailPattern.test(email)) {
    alert("Invalid email format.");
    return;
  }
  if (!phonePattern.test(phone)) {
    alert("Phone number must be 10 digits.");
    return;
  }
  if (!dob) {
    alert("Date of birth cannot be empty.");
    return;
  }
  if (!address) {
    alert("Address cannot be empty.");
    return;
  }
  if (!aadharPattern.test(aadhar)) {
    alert("Aadhar must be 12 digits.");
    return;
  }
  if (!panPattern.test(pan)) {
    alert("PAN must be in format ABCDE1234F.");
    return;
  }

  for (const [key, value] of formData.entries()) {
    if (key !== 'created_at' && key !== 'modified_at') {
      jsonData[key] = value;
    }
  }

  const role = '${sessionScope.role}';
 
 
  const targetRole = document.getElementById("target_role").value;
  console.log("Role from session:", role);
  
  let redirectUrl = '';

  if (targetRole === 'CUSTOMER') {
    redirectUrl = '${pageContext.request.contextPath}/bank/customers?alertMessage=';
  } else if (targetRole === 'ADMIN') {
    redirectUrl = '${pageContext.request.contextPath}/bank/admins?alertMessage=';
  } else if (targetRole === 'SUPERADMIN') {
    redirectUrl = '${pageContext.request.contextPath}/bank/superadmins?alertMessage=';
  } else {
	  if(role === 'CUSTOMER')
		{
			  redirectUrl = '${pageContext.request.contextPath}/bank/customer/dashboard?alertMessage='; 
		}
		
		else if(role === 'ADMIN')
		{
			  redirectUrl = '${pageContext.request.contextPath}/bank/admin/dashboard?alertMessage='; 
		}
		else if(role === 'SUPERADMIN')
		{
			  redirectUrl = '${pageContext.request.contextPath}/bank/superadmin/dashboard?alertMessage='; 
		}
		else
		{
			 redirectUrl = '${pageContext.request.contextPath}'; 
		}
 
  }

  
  
  fetch('${pageContext.request.contextPath}/bank/person', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(jsonData)
  })
  .then(response => response.text())
  .then(msg => {
    
    window.location.href = redirectUrl + encodeURIComponent(msg);
  })
  .catch(err => {
    console.error("Error saving changes:", err);

    window.location.href = errorUrl + encodeURIComponent("Error saving changes.");
  });
}

function fetchModifierDetails(modifierId) {
  fetch('${pageContext.request.contextPath}/bank/customer/details', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ person_id: modifierId })
  })
  .then(response => response.text())
  .then(html => {
    document.open();
    document.write(html);
    document.close();
  })
  .catch(err => {
    console.error("Failed to fetch modifier details", err);
  });
}
</script>

</body>
</html>
