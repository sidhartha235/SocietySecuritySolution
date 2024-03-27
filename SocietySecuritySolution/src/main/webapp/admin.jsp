<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Admin | SSS</title>
		<style>
			body{
				background-image: url("Images/background2.jpg");
				background-size: cover;
			}
			#flatForm, #residentForm{
				display: none;
			}
			h1{
				font-size: 50px;
				text-align: left;
				margin-left: 25px;
			}
			.options{
				text-align: center;
				margin-bottom: 50px;
				margin-top: 50px;
				font-size: 30px;
			}
			.options > select{
				font-size: 25px;
			}
			form{
				text-align: center;
			}
			.formm{
				display: flex;
				flex-direction: row;
				justify-content: center;
				margin-bottom: 30px;
			}
			.left{
				padding: 10px;
			}
			.left *{
				margin: 10px;
				font-size: 25px;
			}
			.right *{
				text-align: left;
				margin: 8px;
				font-size: 22px;
			}
			#submit1, #submit2, #submit3{
				font-size: 25px;
				margin: 10px;
			}
			#submit1{
				margin-bottom: 250px;				
			}
			#submit2{
				margin-bottom: 240px;
			}
			#submit3{
				margin-bottom: 100px;
			}
			#signOutAnchor{
				pointer-events: auto;
			}
			#msg1, #msg2, #msg3{
				font-size: 25px;
				color: white;
			}
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"> </script>
	</head>
	
	<body>
		
		<%@ page import="java.util.List" %>
		<%@ page import="sss.Building" %>
		
		<%
			request.getSession(false);
		%>
		
		<%@ include file="Header.html" %>
		
		<h1> Welcome <%= session.getAttribute("user") %> </h1>
		
		<div class="options">
			<label for="adminOptions"> Privileges: </label>
			<select name="adminOptions" id="adminOptions" onchange="change()">
				<option value="addBuilding"> Add new Building </option>
				<option value="addFlat"> Add new Flat </option>
				<option value="registerResident"> Register new Resident </option>
			</select>
		</div>
		
		<form name="buildingForm" id="buildingForm" method="post" action="addBuilding">
			<div class="formm">	
				<div class="left">
					<label for="buildingName"> Building Name: </label> <br> <br>
					<label for="buildingLocation"> Location: </label> <br>
				</div>
				<div class="right">
					<input type="text" name="buildingName" id="buildingName" placeholder="building name" maxlength="100" required> <br>
					<input type="text" name="buildingLocation" id="buildingLocation" placeholder="address" maxlength="1000" required> <br>
				</div>
			</div>
			<label> Floors: 8 &amp; Flats(per Floor): 8 </label> <br> <br>
			<span id="msg1"> </span> <br>
			<input type="submit" name="submit1" id="submit1" value="Submit">
		</form>
		
		<form name="flatForm" id="flatForm" method="post" action="addFlat">
			<div class="formm">
				<div class="left">
					<label for="buildingName_"> Building: </label> <br> <br>
					<label for="flatNo"> Flat Number: </label> <br> <br>
					<label for="flatType"> Flat Type: </label> <br>
				</div>
				<div class="right">
					<select name="buildingName_" id="buildingName_" required>
						<option selected disabled value=""> -select building- </option>
						<%
							List<Building> buildings = Building.getBuildings();
							for(Building building : buildings){
						%>
								<option value="<%= building.getBuildingID() %>"> <%= building.getBuildingName() + " (" + building.getBuildingID() + ")" %> </option>
						<%
							}
						%>
					</select> <br>
					<input type="number" name="flatNo" id="flatNo" placeholder="nnn" title="Eg. 305 - Floor 3 & Flat 5" required> <br>
					<input type="text" name="flatType" id="flatType" placeholder="eg. 2BHK" maxlength="10" required> <br>
				</div>
			</div>
			<span id="msg2"> </span> <br>
			<input type="submit" name="submit2" id="submit2" value="Submit">
		</form>
		
		<form name="residentForm" id="residentForm" method="post" action="registerResident">
			<div class="formm">
				<div class="left">
					<label for="buildingName__"> Building: </label> <br> <br>
					<label for="residentName"> Resident Name: </label> <br> <br>
					<label for="flatNumber"> Flat Number: </label> <br> <br>
					<label for="residentMobile"> Resident Mobile: </label> <br> <br>
					<label for="residentEmail"> Resident Email: </label> <br> <br>
					<label for="residentPassword"> Resident Password: </label> <br>
				</div>
				<div class="right">
					<select name="buildingName__" id="buildingName__" required>
						<option selected disabled value=""> -select building- </option>
						<%
//							List<Building> buildings = Building.getBuildings();
							for(Building building : buildings){
						%>
								<option value="<%= building.getBuildingID() %>"> <%= building.getBuildingName() + " (" + building.getBuildingID() + ")" %> </option>
						<%
							}
						%>
					</select> <br>
					<input type="text" name="residentName" id="residentName" placeholder="name" maxlength="200" required> <br>
					<input type="number" name="flatNumber" id="flatNumber" placeholder="nnn" title="Eg. 305 - Floor 3 & Flat 5" required> <br>
					<input type="text" name="residentMobile" id="residentMobile" placeholder="contact number" minlength="10" maxlength="10" required> <br>
					<input type="email" name="residentEmail" id="residentEmail" title="Enter your email." placeholder="example@mail" required> <br>
					<input type="password" name="residentPassword" id="residentPassword" title="Enter your password." placeholder="********" minlength="8" required> <br>
				</div>
			</div>
			<span id="msg3"> </span> <br>
			<input type="submit" name="submit3" id="submit3" value="Submit">
		</form>
	
		<%@ include file="Footer.html" %>
	
	</body>
	
	<%
		String building = request.getParameter("building");
		if(building != null){
			int id = Integer.parseInt(building);
	%>
			<script>
				document.getElementById("msg1").innerHTML = "Building added successfully! <br>Supervisor Credentials: supervisor" + <%= id %> + "@sss.com ; supervisor@" + <%= id %>;
			</script>
	<%
		}
	%>
	
	<%
		String flat = (String)session.getAttribute("flat");
		if(flat != null && flat.equals("exists")){
	%>
			<script>
				alert("Flat already exists!");
			</script>
	<%
			session.removeAttribute("flat");
		}
	%>
	
	<%
		String resident = (String)session.getAttribute("resident");
		if(resident != null && resident.equals("exists")){
	%>
			<script>
				alert("Resident with same Flat/Mobile/Email already exists!");
			</script>
	<%
			session.removeAttribute("resident");
		}
	%>
	
	<%
		String flatExists = (String)session.getAttribute("flatExists");
		if(flatExists != null && flatExists.equals("no")){
	%>
			<script>
				alert("Flat does not exist!");
			</script>
	<%
			session.removeAttribute("flatExists");
		}
	%>
	
	<script src="admin.js"> </script>
</html>