validate = () => {
	
	document.getElementById("error").innerHTML = "";
	
//	let email = test_input(document.forms["loginForm"]["username"].value);
	let password = test_input(document.forms["loginForm"]["password"].value); 
	
	if(password.length < 8){
		document.getElementById("error").innerHTML = "Password is at least 8 characters!";
		return false;
	}
	
	return true;
	
}

test_input = (value) => {
	value = value.trim();
	return value;
}