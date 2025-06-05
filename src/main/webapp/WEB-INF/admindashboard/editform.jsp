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
                <input type="text" name="name" value="<%= name %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Email:</label>
                <input type="email" name="email" value="<%= email %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Phone Number:</label>
                <input type="text" name="phone_number" value="<%= phone %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Date of Birth:</label>
                <input type="text" name="dob" value="<%= formattedDob %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>Address:</label>
                <input type="text" name="address" value="<%= address %>" class="editable" readonly />
            </div>
            <div class="column">
                <label>Aadhar:</label>
                <input type="text" name="aadhar" value="<%= aadhar %>" class="editable" readonly />
            </div>
        </div>

        <div class="row">
            <div class="column">
                <label>PAN:</label>
                <input type="text" name="pan" value="<%= pan %>" class="editable" readonly />
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

    for (const [key, value] of formData.entries()) {
      if (key !== 'created_at' && key !== 'modified_at') {
        jsonData[key] = value;
      }
    }
    const role = '${sessionScope.role}';
    console.log("Role from session:", role);
    fetch('${pageContext.request.contextPath}/bank/person', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(jsonData)
    })
    .then(response => response.text())
    .then(msg => {
      let redirectUrl = '';

      if (role === 'SUPERADMIN') {
        redirectUrl = '${pageContext.request.contextPath}/bank/admin?alertMessage=';
      } else {
        redirectUrl = '${pageContext.request.contextPath}/bank/customers?alertMessage=';
      }

      window.location.href = redirectUrl + encodeURIComponent(msg);
    })
    .catch(err => {
      console.error("Error saving changes:", err);
      const errorUrl = role === 'SUPERADMIN'
        ? '${pageContext.request.contextPath}/bank/admin?alertMessage='
        : '${pageContext.request.contextPath}/bank/customers?alertMessage=';

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
