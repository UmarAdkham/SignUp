<?php
	$username = $_POST['name'];
	$password = $_POST['psw'];

	//$username = 'umaradkham';
    //$password = 'psw';
	

	$con = mysqli_connect("localhost", "root", "", "new_jrlu");

	if (mysqli_connect_errno($con)){
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}

	//Searching for an Admin with the inserted username and password
	$resultCheckAdmin = mysqli_query($con, "SELECT * from admin where admin_username = '$username' and admin_psw = '$password'");

	$rowCheckAdmin = mysqli_fetch_array($resultCheckAdmin);

	if(empty($rowCheckAdmin)){
		echo "false";
	}
	else{
		echo "true";
	}

	mysqli_close($con);

?>

