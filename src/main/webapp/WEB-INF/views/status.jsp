<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String status = (String) request.getAttribute("status");
    String title = "";
    String message = "";

    if ("PENDING".equalsIgnoreCase(status)) {
        title = "Your Request is in Pending State";
        message = "Please wait while we review and approve your application.";
    } else if ("INACTIVE".equalsIgnoreCase(status)) {
        title = "Inactive User";
        message = "Your account is currently inactive. Please contact support for further assistance.";
    } else if ("rejected".equalsIgnoreCase(status)) {
        title = "Request Rejected";
        message = "Your request to open a new account has been rejected. You may try again later.";
    } else {
        title = "Status Unknown";
        message = "We couldn't determine your request status.";
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= title %></title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #fdf5f9;
            font-family: Arial, sans-serif;
            color: #333;
        }

        .container {
            text-align: center;
            padding: 100px 20px;
        }

        .status-box {
            display: inline-block;
            border: 2px solid #97144d;
            border-radius: 10px;
            padding: 30px;
            background-color: #fff0f5;
        }

        .status-box h1 {
            color: #97144d;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .status-box p {
            font-size: 18px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="status-box">
            <h1><%= title %></h1>
            <p><%= message %></p>
        </div>
    </div>
</body>
</html>
