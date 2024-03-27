<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Profile | SSS</title>
		<style>
			body{
				background-image: url("Images/background3.jpg");
				background-size: cover;
			}
			h1{
				font-size: 50px;
				text-align: left;
				margin-left: 25px;
				margin-right: 0px;
			}
			h2{
				font-size: 35px;
				text-align: left;
				margin-left: 50px;
			}
			table{
				margin-left: 50px;
			}
			table, tr, th, td{
				font-size: 25px;
				color: black;
				background-color: antiquewhite;
				border: 1px solid black;
			}
			table{
				max-width: 90vw;
			}
			th, td{
				padding: 3px;
				text-align: center;
			}
			#foot{
				margin-bottom: 250px;
			}
			#back{
				font-size: 25px;
				text-align: right;
				background-color: antiquewhite;
				padding: 10px;
				border-radius: 10px; 
				text-decoration: none;
				margin-left: 65vw;
			}
			#displayBuildings, #displayFlats, #displayResidents, #displayNormalVisitors, #displayRegularVisitors, #displayMaintenance, #displayStaff{
				background-color: antiquewhite;
				padding: 20px;
				border-radius: 10px;
				max-width: 25vw;
				text-align: center; 
			}
			#buildings, #flats, #residents, #normalVisitors, #regularVisitors, #maintenance, #staff{
				display: none;
			}
		</style>
	</head>
	
	<body>

		<%@ include file="Header.html" %>
		
		<%@ page import="java.util.List" %>
		<%@ page import="sss.AdminAccess" %>
		<%@ page import="sss.SupervisorAccess" %>
		<%@ page import="sss.ResidentHistory" %>
	
		<%
			session = request.getSession(false);
			String user = (String)session.getAttribute("user");
			int residentID = 0;
			int buildingID = 0;
			
			if(user.equals("admin")){
				
		%>
				<h1> Hello <%= user %> <a id="back" href="admin.jsp"> &#x1F448; Go Back </a> </h1>
				
				<h2 id="displayBuildings"> Buildings </h2>
				
				<%
					List<AdminAccess> buildings = AdminAccess.getBuildings();
				%>
					<table id="buildings">
					
						<tr>
							<th> Building ID </th>
							<th> Building Name </th>
							<th> Location </th>
							<th> Supervisor Email </th>
						</tr>
				<%
					for(AdminAccess building : buildings){
				%>
						<tr>
							<td> <%= building.getBuildingID() %> </td>
							<td> <%= building.getBuildingName() %> </td>
							<td> <%= building.getLocation() %> </td>
							<td> <%= building.getSuperEmail() %> </td>
						</tr>
				<%
					}
					if(buildings.size() == 0){
				%>
						<tr>
							<td colspan="4"> No buildings created! </td>
						</tr>
				<%
					}
				%>
					
					</table>
		<%	
			}
			else if(user.contains("supervisor")){
			
				buildingID = (int) session.getAttribute("building");
		%>
				<h1> Hello <%= user %> <a id="back" href="supervisor.jsp"> &#x1F448; Go Back </a> </h1>
				
				<h2 id="displayFlats"> Flats </h2>
				
				<%
					List<SupervisorAccess> flats = SupervisorAccess.getFlats(buildingID);
				%>
					<table id="flats">
					
						<tr>
							<th> Flat Number </th>
							<th> Flat Floor </th>
							<th> Flat Type </th>
						</tr>
				<%
					for(SupervisorAccess flat : flats){
				%>
						<tr>
							<td> <%= flat.getFlatNumber() %> </td>
							<td> <%= flat.getFloorNumber() %> </td>
							<td> <%= flat.getFlatType() %> </td>
						</tr>
				<%
					}
					if(flats.size() == 0){
				%>
						<tr>
							<td colspan="3"> No flats created! Please contact Admin. </td>
						</tr>
				<%
					}
				%>
					
					</table>
				
				<h2 id="displayResidents"> Residents </h2>
				
				<%
					List<SupervisorAccess> residents = SupervisorAccess.getResidents(buildingID);
				%>
					<table id="residents">
					
						<tr>
							<th> Resident ID </th>
							<th> Resident Name </th>
							<th> Flat Number </th>
							<th> Mobile </th>
							<th> Email </th>
						</tr>
				<%
					for(SupervisorAccess resident : residents){
				%>
						<tr>
							<td> <%= resident.getResidentID() %> </td>
							<td> <%= resident.getResidentName() %> </td>
							<td> <%= resident.getResidentFlat() %> </td>
							<td> <%= resident.getResidentMobile() %> </td>
							<td> <%= resident.getResidentEmail() %> </td>
						</tr>
				<%
					}
					if(residents.size() == 0){
				%>
						<tr>
							<td colspan="5"> No residents registered yet! </td>
						</tr>
				<%
					}
				%>
					
					</table>
				
				<h2 id="displayNormalVisitors"> Normal Visitors </h2>
				
				<%
					List<SupervisorAccess> normalVisitors = SupervisorAccess.getNormalVisitors(buildingID);
				%>
					<table id="normalVisitors">
					
						<tr>
							<th> Name </th>
							<th> Mobile </th>
							<th> Approval </th>
							<th> Flat Number </th>
							<th> Date </th>
						</tr>
				<%
					for(SupervisorAccess normalVisitor : normalVisitors){
				%>
						<tr>
							<td> <%= normalVisitor.getNVisitorName() %> </td>
							<td> <%= normalVisitor.getNVisitorMobile() %> </td>
							<td> <%= normalVisitor.getNVisitorStatus() %> </td>
							<td> <%= normalVisitor.getNVisitorFlat() %> </td>
							<td> <%= normalVisitor.getNVisitorDate() %> </td>
						</tr>
				<%
					}
					if(normalVisitors.size() == 0){
				%>
						<tr>
							<td colspan="5"> No normal visitors visited your building yet! </td>
						</tr>
				<%
					}
				%>
					
					</table>
		<%		
			}
			else{
				
				residentID = (int)session.getAttribute("id");
		%>
				
				<h1> Hello <%= user %> <a id="back" href="resident.jsp"> &#x1F448; Go Back </a> </h1>
				
				<h2 id="displayRegularVisitors"> Visitors History </h2>
				
				<%
					List<ResidentHistory> visitedVisitors = ResidentHistory.getVisitorsHistory(residentID);
				%>
					<table id="regularVisitors">
						<tr>
							<th> Name </th>
							<th> Mobile </th>
							<th> Visit Date </th>
							<th> Check In </th>
							<th> Check Out </th>
						</tr>
				<%
					for(ResidentHistory visitedVisitor : visitedVisitors){
				%>
						<tr>
							<td> <%= visitedVisitor.getVisitorName() %> </td>
							<td> <%= visitedVisitor.getVisitorMobile() %> </td>
							<td> <%= visitedVisitor.getVisitDate() %> </td>
							<td> <%= visitedVisitor.getCheckIn() %> </td>
							<td> <%= visitedVisitor.getCheckOut() %> </td>
						</tr>
				<%
					}
					if(visitedVisitors.size() == 0){
				%>
						<tr>
							<td colspan="5"> No visitors visited yet! </td>
						</tr>
				<%
					}
				%>
					
					</table>
				
				<h2 id="displayMaintenance"> Maintenance History </h2>
				
				<%
					List<ResidentHistory> paidMaintenances = ResidentHistory.getMaintenanceHistory(residentID);
				%>
					<table id="maintenance">
						<tr>
							<th> Issue Date </th>
							<th> Amount </th>
							<th> Mode of Payment </th>
						</tr>
				<%
					for(ResidentHistory paidMaintenance : paidMaintenances){
				%>
						<tr>
							<td> <%= paidMaintenance.getIssueDate() %> </td>
							<td> <%= paidMaintenance.getAmount() %> </td>
							<td> <%= paidMaintenance.getModeOfPay() %> </td>
						</tr>
				<%
					}
					if(paidMaintenances.size() == 0){
				%>
						<tr>
							<td colspan="3"> No maintenance paid yet! </td>
						</tr>
				<%
					}
				%>
				
					</table>
				
				<h2 id="displayStaff"> Staff Details </h2>
				
				<%
					List<ResidentHistory> staffDetails = ResidentHistory.getStaffDetails(residentID);
				%>
					<table id="staff">
						<tr>
							<th> Role </th>
							<th> Name </th>
							<th> Working Hours </th>
							<th> Emergency Contact </th>
						</tr>
				<%
					for(ResidentHistory staff : staffDetails){
				%>
						<tr>
							<td> <%= staff.getStaffRole() %> </td>
							<td> <%= staff.getStaffName() %> </td>
							<td> <%= staff.getWorkingHours() %> </td>
							<td> <%= staff.getStaffContact() %> </td>
						</tr>
				<%
					}
					if(staffDetails.size() == 0){
				%>
						<tr>
							<td colspan="4"> No staff assigned yet! Please contact your Supervisor. </td>
						</tr>
				<%
					}
				%>
				
					</table>
		<%	
			}
		%>

		<p id="foot"> </p>

		<%@ include file="Footer.html" %>
		
	</body>
	
	<script src="Profile.js"> </script>
</html>