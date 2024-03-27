<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Resident | SSS</title>
		<style>
			body{
				background-image: url("Images/background3.jpg");
				background-size: cover;
			}
			h1{
				font-size: 50px;
				text-align: left;
				margin-left: 25px;
			}
			h2{
				font-size: 35px;
				text-align: left;
				margin-left: 50px;
			}
			#foot{
				margin-bottom: 250px;
			}
			.visitors, .dues{
				display: flex;
				flex-direction: row;
				margin-left: 50px;
			}
			.visitors > form, .dues > form{
				margin: 10px;
				padding: 20px;
				padding-top: 0px;
				border: 3px solid black;
				background-color: yellow;
			}
			.visitor *, .due *{
				font-size: 25px;
			}
			.none{
				font-size: 25px;
				color: white;
			}
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"> </script>
	</head>
	
	<body>
	
		<%@ page import="java.util.List" %>
		<%@ page import="sss.NormalVisitor" %>
		<%@ page import="sss.Maintenance" %>
	
		<%
			session = request.getSession(false);
			int residentID = (int)session.getAttribute("id");
		%>
		
		<%@ include file="Header.html" %>
		
		<h1> Welcome <%= session.getAttribute("user") %> </h1>
		
		<h2> Visitor Requests </h2>
		<div class="visitors">
		<%
			List<NormalVisitor> visitors = NormalVisitor.getNormalVisitors(residentID);
			for(NormalVisitor visitor : visitors){
		%>
				<form name="visitor" id="visitor" method="post" action="approveDenyVisitor">
					<div class="visitor">
						<p> <%= visitor.getVisitorName() %> </p>
						<p> <%= visitor.getVisitorMobile() %> </p>
						<p> <%= visitor.getRequestDate() %> </p>
						<input type="hidden" name="visitorName" value="<%= visitor.getVisitorName() %>">
						<input type="hidden" name="visitorMobile" value="<%= visitor.getVisitorMobile() %>">
						<input type="hidden" name="requestDate" value="<%= visitor.getRequestDate() %>">
						<input type="submit" name="approve" id="approve" value="Approve">
						<input type="submit" name="deny" id="deny" value="Deny">
					</div>
				</form>
		<%
			}
			if(visitors.size() == 0){
		%>
				<p class="none"> No visitors currently! </p>
		<%
			}
		%>
		</div>
		
		<h2> Maintenance Dues </h2>
		<div class="dues">
		<%
			List<Maintenance> dues = Maintenance.getDues(residentID);
			for(Maintenance due : dues){
		%>
				<form name="due" id="due" method="post" action="payDues">
					<div class="due">
						<p> <%= due.getIssueDate() %> </p>
						<p> <%= due.getAmount() %> </p>
						<input type="hidden" name="resID" value="<%= residentID %>">
						<input type="hidden" name="issueDate" value="<%= due.getIssueDate() %>">
						<input type="hidden" name="amount" value="<%= due.getAmount() %>">
						<select name="modeOfPay" id="modeOfPay" required>
							<option selected disabled value=""> -mode of payment- </option>
							<option value="cash"> Cash  </option>
							<option value="upi"> UPI </option>
							<option value="netbanking"> Net banking </option>
							<option value="card"> Debit/Credit Card </option>
							<option value="cheque"> Cheque </option>
						</select>
						<input type="submit" name="pay" id="pay" value="Pay">
					</div>
				</form>
		<%
			}
			if(dues.size() == 0){
		%>
				<p class="none"> No dues currently! </p>
		<%
			}
		%>
		</div>
		
		<p id="foot"> </p>
		
		<%@ include file="Footer.html" %>
	
	</body>
	
	<script src=""> </script>
</html>