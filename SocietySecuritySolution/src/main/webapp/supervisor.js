$(document).ready(function(){
	const date = new Date();
	const year = date.getFullYear();
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');
	const dateString = year + "-" + month + "-" + day;
	
	$("#date_N").attr("value", dateString);
	$("#date_N").attr("min", dateString);
	$("#date_N").attr("max", dateString);
	
	$("#date_E").attr("value", dateString);
	$("#date_E").attr("min", dateString);
	$("#date_E").attr("max", dateString);
	
	$("#date_M").attr("value", dateString);
	$("#date_M").attr("min", dateString);
	$("#date_M").attr("max", dateString);
	
	
	$("#visitorNform").submit(function(event){
		let flatNo = test_input($("#flatNo_N").val());
		let nVisitorMobile = test_input($("#mobile_N").val());
		
		if(!/^[1-8]0[1-8]$/.test(flatNo)){
			$("#msg1").text("Invalid Flat Number!");
			event.preventDefault();
		}
		if(!/^[1-9][0-9]{9}$/.test(nVisitorMobile)){
			$("#msg1").text("Mobile number should contain digits only!");
			event.preventDefault();
		}
	});
	
	$("#visitorRform").submit(function(event){
		let securityCode = test_input($("#securityCode_R").val());
		let rVisitorMobile = test_input($("#mobile_R").val());
		
		if(!/^SSS[0-9]{4}$/.test(securityCode)){
			$("#msg2").text("Invalid Security Code!");
			event.preventDefault();
		}
		if(!/^[1-9][0-9]{9}$/.test(rVisitorMobile)){
			$("#msg2").text("Mobile number should contain digits only!");
			event.preventDefault();
		}
	});
	
	$("#visitorRentry").submit(function(event){
		let securityCode = test_input($("#securityCode_E").val());
		let flatNo = test_input($("#flatNo_E").val());
		
		if(!/^SSS[0-9]{4}$/.test(securityCode)){
			$("#msg3").text("Invalid Security Code!");
			event.preventDefault();
		}
		if(!/^[1-8]0[1-8]$/.test(flatNo)){
			$("#msg3").text("Invalid Flat Number!");
			event.preventDefault();
		}
	});
	
	$("#visitorRexit").submit(function(event){
		let securityCode = test_input($("#securityCode_T").val());
		let flatNo = test_input($("#flatNo_T").val());
		
		if(!/^SSS[0-9]{4}$/.test(securityCode)){
			$("#msg4").text("Invalid Security Code!");
			event.preventDefault();
		}
		if(!/^[1-8]0[1-8]$/.test(flatNo)){
			$("#msg4").text("Invalid Flat Number!");
			event.preventDefault();
		}
	});
	
	$("#maintenanceForm").submit(function(event){
		let flatNo = test_input($("#flatNo_M").val());
		
		if(!/^[1-8]0[1-8]$/.test(flatNo)){
			$("#msg5").text("Invalid Flat Number!");
			event.preventDefault();
		}
	});
	
	$("#addStaffForm").submit(function(event){
		let staffMobile = test_input($("#mobile_S").val());
		
		if(!/^[1-9][0-9]{9}$/.test(staffMobile)){
			$("#msg6").text("Mobile number should contain digits only!");
			event.preventDefault();
		}
	});
	
	
	$("#supervisorOptions").change(function(){
		
		let selectedValue = $(this).val();
		
		const hours = date.getHours().toString().padStart(2, '0');
		const minutes = date.getMinutes().toString().padStart(2, '0');
		const timeString = hours + ":" + minutes;
		
		$("#msg1").text("");
		$("#msg2").text("");
		$("#msg3").text("");
		$("#msg4").text("");
		$("#msg5").text("");
		$("#msg6").text("");
		
		if(selectedValue == "requestNormalVisitor"){
			$("#mobile_N").val("");
			$("#name_N").val("");
			$("#flatNo_N").val("");
			
			$("#visitorNform").css("display", "block");
			$("#visitorRform").css("display", "none");
			$("#visitorEntryExit").css("display", "none");
			$("#maintenanceForm").css("display", "none");
			$("#addRemoveStaff").css("display", "none");
		}
		else if(selectedValue == "newRegularVisitor"){
			$("#securityCode_R").val("");
			$("#name_R").val("");
			$("#mobile_R").val("");
			
			$("#visitorNform").css("display", "none");
			$("#visitorRform").css("display", "block");
			$("#visitorEntryExit").css("display", "none");
			$("#maintenanceForm").css("display", "none");
			$("#addRemoveStaff").css("display", "none");
		}
		else if(selectedValue == "checkInOutRegularVisitor"){
			$("#checkIn").attr("value", timeString);
			$("#checkIn").attr("min", timeString);
			
			$("#checkOut").attr("value", timeString);
			$("#checkOut").attr("min", timeString);
			
			$("#securityCode_E").val("");
			$("#flatNo_E").val("");
			
			$("#securityCode_T").val("");
			$("#flatNo_T").val("");
			
			$("#visitorNform").css("display", "none");
			$("#visitorRform").css("display", "none");
			$("#visitorEntryExit").css("display", "flex");
			$("#maintenanceForm").css("display", "none");
			$("#addRemoveStaff").css("display", "none");
		}
		else if(selectedValue == "maintenanceRequest"){
			$("#flatNo_M").val("");
			$("#amount_M").val("");
			
			$("#visitorNform").css("display", "none");
			$("#visitorRform").css("display", "none");
			$("#visitorEntryExit").css("display", "none");
			$("#maintenanceForm").css("display", "block");
			$("#addRemoveStaff").css("display", "none");
		}
		else if(selectedValue == "addRemoveStaffDetails"){
			$("#role_S").val("");
			$("#name_S").val("");
			$("#hours_S").val("");
			$("#mobile_S").val("");
			
			$("#role_F").val("");
			$("#name_F").val("");
		
			$("#visitorNform").css("display", "none");
			$("#visitorRform").css("display", "none");
			$("#visitorEntryExit").css("display", "none");
			$("#maintenanceForm").css("display", "none");
			$("#addRemoveStaff").css("display", "flex");	
		}
		
	});
});

test_input = (value) =>{
	value = value.trim();
	return value;
}