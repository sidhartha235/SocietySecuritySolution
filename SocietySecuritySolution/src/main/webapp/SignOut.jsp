<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign Out | SSS</title>
		<style>
			body{
				background-image: linear-gradient(to right, gray, black, black, gray);
				height: 63vh;
			}
			.paras{
				text-align: center;
				font-size: 30px;
				color: white;
				margin-top: 300px;
			}
			a{
				color: antiquewhite;
				font-size: 35px;
			}
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"> </script>	
	</head>
	
	<body>
		
		<%
			session = request.getSession();
			session.invalidate();
		%>
		
		<div class="paras">
			<p> You have been logged out successfully! </p>
			<p> Click here to <a href="index.jsp">Sign In</a> again. </p>
		</div>
		
	</body>
</html>