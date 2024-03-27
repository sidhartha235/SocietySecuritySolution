<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Supervisor | SSS</title>
		<style>
			body{
				background-image: url("Images/background2.jpg");
				background-size: cover;
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
			input[type="submit"]{
				font-size: 25px;
				margin: 10px;
			}
			#visitorRentry, #addStaffForm{
				margin-right: 50px;
			}
			#visitorRexit, #removeStaffForm{
				margin-left: 50px;
			}
			#visitorRform, #visitorEntryExit, #maintenanceForm, #addRemoveStaff{
				display: none;
			}
			#submit1{
				margin-bottom: 210px;
			}
			#submit2{
				margin-bottom: 260px;
			}
			#submit3, #submit4{
				margin-bottom: 180px;
			}
			#submit5{
				margin-bottom: 260px;
			}
			#submit6, #submit7{
				margin-bottom: 180px;
			}
			#msg1, #msg2, #msg3, #msg4, #msg5, #msg6{
				font-size: 25px;
				color: white;
			}
		</style>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"> </script>
	</head>
	
	<body>
		
		<%
			request.getSession(false);
			int buildingID = (int)session.getAttribute("building");
		%>
		
		<%@ include file="Header.html" %>
		
		<h1> Welcome <%= session.getAttribute("user") %> </h1>
		
		<div class="options">
			<label for="supervisorOptions"> Privileges: </label>
			<select name="supervisorOptions" id="supervisorOptions">
				<option value="requestNormalVisitor"> Normal Visitor Entry </option>
				<option value="newRegularVisitor"> New Regular Visitor </option>
				<option value="checkInOutRegularVisitor"> Regular Visitor Entry / Exit </option>
				<option value="maintenanceRequest"> Issue Maintenance Bill </option>
				<option value="addRemoveStaffDetails"> Staff Details </option>
			</select>
		</div>
		
		
		<form name="visitorNform" id="visitorNform" method="post" action="requestNormalVisitor">
			<div class="formm">
				<div class="left">
					<label for="date_N"> Date: </label> <br> <br>
					<label for="mobile_N"> Visitor Mobile: </label> <br> <br>
					<label for="name_N"> Visitor Name: </label> <br> <br>
					<label for="flatNo_N"> Flat Number: </label> <br>
				</div>
				<div class="right">
					<input type="date" name="date_N" id="date_N"> <br>
					<input type="text" name="mobile_N" id="mobile_N" placeholder="contact" minlength="10" maxlength="10" required> <br>
					<input type="text" name="name_N" id="name_N" placeholder="name" maxlength="200" required> <br>
					<input type="number" name="flatNo_N" id="flatNo_N" placeholder="nnn" title="Eg. 305 - Floor 3 & Flat 5" required> <br>
				</div>
			</div>
			<span id="msg1"> </span> <br>
			<input type="submit" name="submit1" id="submit1" value="Send Request">
		</form>
		
		
		<form name="visitorRform" id="visitorRform" method="post" action="newRegularVisitor">
			<div class="formm">
				<div class="left">
					<label for="securityCode_R"> Security Code: </label> <br> <br>
					<label for="name_R"> Name: </label> <br> <br>
					<label for="mobile_R"> Mobile: </label> <br>
				</div>
				<div class="right">
					<input type="text" name="securityCode_R" id="securityCode_R" placeholder="SSSxxxx" minlength="7" maxlength="7" required> <br>
					<input type="text" name="name_R" id="name_R" placeholder="name" maxlength="200" required> <br>
					<input type="text" name="mobile_R" id="mobile_R" placeholder="contact" minlength="10" maxlength="10" required> <br>
				</div>
			</div>
			<span id="msg2"> </span> <br>
			<input type="submit" name="submit2" id="submit2" value="Submit">
		</form>
		
		
		<div class="formm" id="visitorEntryExit">
			<form name="visitorRentry" id="visitorRentry" method="post" action="checkInOutRegularVisitor">
				<div class="formm">
					<div class="left">
						<label for="date_E"> Date: </label> <br> <br>
						<label for="securityCode_E"> Security Code: </label> <br> <br>
						<label for="flatNo_E"> Flat Number: </label> <br> <br>
						<label for="checkIn"> Check-In Time: </label> <br>
					</div>
					<div class="right">
						<input type="date" name="date_E" id="date_E" required> <br>
						<input type="text" name="securityCode_E" id="securityCode_E" placeholder="SSSxxxx" minlength="7" maxlength="7" required> <br>
						<input type="number" name="flatNo_E" id="flatNo_E" placeholder="nnn" required> <br>
						<input type="time" name="checkIn" id="checkIn" required> <br>
					</div>
				</div>
				<span id="msg3"> </span> <br>
				<input type="submit" name="submit3" id="submit3" value="Check In">
			</form>
			<form name="visitorRexit" id="visitorRexit" method="post" action="checkInOutRegularVisitor">
				<div class="formm">
					<div class="left">
						<label for="securityCode_T"> Security Code: </label> <br> <br>
						<label for="flatNo_T"> Flat Number: </label> <br> <br>
						<label for="checkOut"> Check-Out Time: </label> <br>
					</div>
					<div class="right">
						<input type="text" name="securityCode_T" id="securityCode_T" placeholder="SSSxxxx" minlength="7" maxlength="7" required> <br>
						<input type="number" name="flatNo_T" id="flatNo_T" placeholder="nnn" required> <br>
						<input type="time" name="checkOut" id="checkOut" required> <br>
					</div>
				</div>
				<span id="msg4"> </span> <br>
				<input type="submit" name="submit4" id="submit4" value="Check Out">
			</form>
		</div>
		
		
		<form name="maintenanceForm" id="maintenanceForm" method="post" action="maintenanceRequest">
			<div class="formm">
				<div class="left">
					<label for="date_M"> Date: </label> <br> <br>
					<label for="flatNo_M"> Flat Number: </label> <br> <br>
					<label for="amount_M"> Amount: </label> <br>
				</div>
				<div class="right">
					<input type="date" name="date_M" id="date_M" required> <br>
					<input type="number" name="flatNo_M" id="flatNo_M" placeholder="nnn" required> <br>
					<input type="number" name="amount_M" id="amount_M" placeholder="Eg. 5000" required> <br>
				</div>
			</div>
			<span id="msg5"> </span> <br>
			<input type="submit" name="submit5" id="submit5" value="Submit">
		</form>
		
		
		<div class="formm" id="addRemoveStaff">
			<form name="addStaffForm" id="addStaffForm" method="post" action="addRemoveStaffDetails">
				<div class="formm">
					<input type="hidden" name="buildingID" value="<%= buildingID %>">
					<div class="left">
						<label for="role_S"> Role: </label> <br> <br>
						<label for="name_S"> Staff Name: </label> <br> <br>
						<label for="hours_S"> Working Hours: </label> <br> <br>
						<label for="mobile_S"> Emergency Contact: </label> <br>
					</div>
					<div class="right">
						<input type="text" name="role_S" id="role_S" placeholder="type of staff" title="security, cleaning, gardener, temple, clubhouse, ..." maxlength="100" required> <br>
						<input type="text" name="name_S" id="name_S" placeholder="name" maxlength="100" required> <br>
						<input type="text" name="hours_S" id="hours_S" placeholder="Eg. 1000 - 2000 / 10:00-20:00" minlength="11" maxlength="11" required> <br>
						<input type="text" name="mobile_S" id="mobile_S" placeholder="contact" minlength="10" maxlength="10" required> <br>
					</div>
				</div>
				<span id="msg6"> </span> <br>
				<input type="submit" name="submit6" id="submit6" value="Add">
			</form>
			<form name="removeStaffForm" id="removeRtaffForm" method="post" action="addRemoveStaffDetails">
				<div class="formm">
					<input type="hidden" name="buildingID" value="<%= buildingID %>">
					<div class="left">
						<label for="role_F"> Role: </label>  <br> <br>
						<label for="name_F"> Staff Name: </label> <br>
					</div>
					<div class="right">
						<input type="text" name="role_F" id="role_F" placeholder="type of staff" title="security, cleaning, gardener, temple, clubhouse, ..." maxlength="100" required> <br>
						<input type="text" name="name_F" id="name_F" placeholder="name" maxlength="100" required> <br>
					</div>
				</div>
				<input type="submit" name="submit7" id="submit7" value="Remove">
			</form>
		</div>
		
		<%@ include file="Footer.html" %>
	
	</body>
	
	<%
		String residentExists = (String)session.getAttribute("residentExists");
		if(residentExists != null && residentExists.equals("no")){
	%>
			<script>
				alert("Resident does not exist!");
			</script>
	<%
			session.removeAttribute("residentExists");
		}
	%>
	
	<%
		String visitor = (String)session.getAttribute("visitor");
		if(visitor != null && visitor.equals("exists")){
	%>
			<script>
				alert("Visitor already requested for the day!");
			</script>
	<%
			session.removeAttribute("visitor");
		}
	%>
	
	<%
		String regularVisitor = (String)session.getAttribute("regularVisitor");
		if(regularVisitor != null && regularVisitor.equals("exists")){
	%>
			<script>
				alert("Visitor already exists with same SecurityCode/Mobile!");
			</script>
	<%
			session.removeAttribute("regularVisitor");
		}
	%>
	
	<%
		String regularVisitorExists = (String)session.getAttribute("regularVisitorExists");
		if(regularVisitorExists != null && regularVisitorExists.equals("no")){
	%>
			<script>
				alert("No regular visitor with given security code!");
			</script>
	<%
			session.removeAttribute("regularVisitorExists");
		}
	%>
	
	<%
		String regularVisitorExited = (String)session.getAttribute("regularVisitorExited");
		if(regularVisitorExited != null && regularVisitorExited.equals("yes")){
	%>
			<script>
				alert("Regular visitor already checked out!");
			</script>
	<%
			session.removeAttribute("regularVisitorExited");
		}
	%>
	
	<%
		String regularVisitorEntered = (String)session.getAttribute("regularVisitorEntered");
		if(regularVisitorEntered != null && regularVisitorEntered.equals("yes")){
	%>
			<script>
				alert("Regular visitor already checked in!");
			</script>
	<%
			session.removeAttribute("regularVisitorEntered");
		}
	%>
	
	<script src="supervisor.js"> </script>
</html>