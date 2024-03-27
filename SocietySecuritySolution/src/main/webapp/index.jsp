<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login | SSS</title>
		<style>
			body{
				background-image: url("Images/background1.jpg");
				background-size: 1680px 953px;
			}
			body *{
				text-align: center;
			}
			h1{
				font-size: 50px;
				color: darkblue;
				/*text-shadow: 5px 5px 5px white, -5px -5px 5px white;*/
				background-color: ghostwhite;
				text-decoration: underline red wavy 3px;
				margin-left: 500px;
				margin-right: 500px;
				border-radius: 8px;
			}
			.formm{
				display: flex;
				flex-direction: row;
				justify-content: center;
				margin-bottom: 30px;
			}
			.loginLabels{
				padding: 10px;
			}
			.loginLabels *{
				font-size: 25px;
				margin: 10px;
			}
			.loginCred *{
				font-size: 22px;
				margin: 8px;
				text-align: left;
			}
			#submit{
				font-size: 25px;
				margin: 10px
			}
			form{
				background-color: ghostwhite;
				margin-left: 400px;
				margin-right: 900px;
				margin-top: 150px;
				border: 5px solid black;
				border-radius: 8px;
				outline: 5px solid ghostwhite;
				padding: 20px;
			}
			#error{
				color: red;
			}
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"> </script>
	</head>
	
	<script src="index.js"> </script>
	
	<body>
		<%-- <%@ include file="Header.html" %> --%>
	
		<h1> Welcome to SecureLiving Solutions </h1>
		
		<form name="loginForm" id="loginForm" method="post" onsubmit="return validate()" action="verifyUser">
			<div class="formm">
				<div class="loginLabels">
					<label for="username"> Username: </label> <br> <br>
					<label for="password"> Password: </label>
				</div>
				<div class="loginCred">
					<input type="email" name="username" id="username" title="Enter your email." placeholder="example@mail" required> <br>
					<input type="password" name="password" id="password" title="Enter your password." placeholder="********" required> <br>
					<span id="error"> </span>
				</div>
			</div>
			<input type="submit" name="submit" id="submit" value="Log In">
		</form>
		
		<%-- <%@ include file="Footer.html" %> --%>
	</body>
	
	<%
		String message = request.getParameter("msg");
		if(message != null && message.equals("invalid")){
			out.println("<script>");
			out.println("document.getElementById('error').innerHTML = 'User does not exist with these credentials!'");
			out.println("</script>");
		}
		
		String Session = request.getParameter("session");
		if(Session != null && Session.equals("expired")){
	%>
			<script>
				document.getElementById('error').innerHTML = "Session Expired! Please Log In again.";
			</script>
	<%
		}
	%>
	
</html>