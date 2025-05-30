<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="cards-bar" id="cards-bar">
    <div class="card" id="balance">
        Your Account Balance 
        <button class="balance-btn" id="balance-btn">View Balance</button>
        <p id="balance-value" style="display:none;"></p>
    </div>
    <div class="card" id="credit">
        Total Credits Week
        <p><span id="credit-value">Loading...</span></p>
    </div>
    <div class="card" id="debit">
        Total Debits Week
        <p><span id="debit-value">Loading...</span></p>
    </div>
</div>
<script>
document.addEventListener("DOMContentLoaded", function () {
    fetch("${pageContext.request.contextPath}/bank/accountno")
        .then(res => res.json())
        .then(data => {
            const accountNo = data.account_no;
            return Promise.all([
                fetch("${pageContext.request.contextPath}/bank/creditcount", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ account_no: accountNo })
                }),
                fetch("${pageContext.request.contextPath}/bank/debitcount", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ account_no: accountNo })
                })
            ]);
        })
        .then(async ([creditRes, debitRes]) => {
            const creditData = await creditRes.json();
            const debitData = await debitRes.json();
            document.getElementById("credit-value").textContent = creditData.credit;
            document.getElementById("debit-value").textContent = debitData.debit;
        })
        .catch(error => {
            console.error("Error:", error);
            document.getElementById("credit-value").textContent = "Error loading";
            document.getElementById("debit-value").textContent = "Error loading";
        });

    document.getElementById("balance-btn").addEventListener("click", function () {
        fetch("${pageContext.request.contextPath}/bank/balance")
            .then(res => res.json())
            .then(data => {
                document.getElementById("balance-btn").style.display = "none";
                document.getElementById("balance-value").style.display = "block";
                document.getElementById("balance-value").textContent = "₹ " + data.balance;
            })
            .catch(error => {
                console.error("Error:", error);
                document.getElementById("balance-value").textContent = "Error fetching balance";
            });
    });
});
</script>

</body>
</html>