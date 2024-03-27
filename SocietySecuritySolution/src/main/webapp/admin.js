$(document).ready(function(){
	$("input").focus(function(){
		$(this).css("color", "black");
		$(this).css("background-color", "yellow");
	});
	$("#buildingName_").focus(function(){
		$(this).css("color", "black");
		$(this).css("background-color", "yellow");
	});
	$("#buildingName__").focus(function(){
		$(this).css("color", "black");
		$(this).css("background-color", "yellow");
	});
	
	$("#flatForm").submit(function(event){
		let flatNoValue = test_input($("#flatNo").val());
		
		if(!/^[1-8]0[1-8]$/.test(flatNoValue)){
			$("#msg2").text("Invalid Flat Number!");
			event.preventDefault();
		}
	});
	
	$("#residentForm").submit(function(event){
		let flatNumValue = test_input($("#flatNumber").val());
		let residentMobile = test_input($("#residentMobile").val());
		
		if(!/^[1-8]0[1-8]$/.test(flatNumValue)){
			$("#msg3").text("Invalid Flat Number!");
			event.preventDefault();
		}
		else if(!/^[1-9][0-9]{9}$/.test(residentMobile)){
			$("#msg3").text("Mobile number should contain digits only!");
			event.preventDefault();
		}
		
	});
});

change = () => {
	document.forms["buildingForm"]["buildingName"].value = "";
	document.forms["buildingForm"]["buildingLocation"].value = "";
	
	document.forms["flatForm"]["flatNo"].value = "";
	document.forms["flatForm"]["buildingName_"].value = "";
	document.forms["flatForm"]["flatType"].value = "";
	
	document.forms["residentForm"]["residentName"].value = "";
	document.forms["residentForm"]["buildingName__"].value = "";
	document.forms["residentForm"]["flatNumber"].value = "";
	document.forms["residentForm"]["residentMobile"].value = "";
	document.forms["residentForm"]["residentEmail"].value = "";
	document.forms["residentForm"]["residentPassword"].value = "";
	
	let dropdownList = document.getElementById("adminOptions");
	let selectedOption = dropdownList.options[dropdownList.selectedIndex].value;
	
	if(selectedOption == "addBuilding"){
		document.getElementById("buildingForm").style.display = "block";
		document.getElementById("flatForm").style.display = "none";
		document.getElementById("residentForm").style.display = "none";
		document.getElementById("msg1").innerHTML = "";
	}
	else if(selectedOption == "addFlat"){
		document.getElementById("buildingForm").style.display = "none";
		document.getElementById("flatForm").style.display = "block";
		document.getElementById("residentForm").style.display = "none";
		document.getElementById("msg2").innerHTML = "";
	}
	else if(selectedOption == "registerResident"){
		document.getElementById("buildingForm").style.display = "none";
		document.getElementById("flatForm").style.display = "none";
		document.getElementById("residentForm").style.display = "block";
		document.getElementById("msg3").innerHTML = "";
	}
}

test_input = (value) => {
	value = value.trim();
	return value;
}