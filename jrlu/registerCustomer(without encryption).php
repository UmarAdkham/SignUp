<?php

include 'AES.php';
$username = $_POST['name'];
$firstname = $_POST['firstname'];
$lastname = $_POST['lastname'];
$dob = $_POST['dob'];
$address = $_POST['address'];
$passportNo = $_POST['passportNo'];
$password = $_POST['psw'];

/*
$blockSize = 256;
$aes_passport = new AES($passportNo, $password, $blockSize);
$enc_passport = $aes_passport->encrypt();*/




/*$username = 'jjaasdssdadd';
$password = 'psw';
$firstname = 'firstname';
$lastname = 'lastname';
$dob = 'asd';
$address = 'asd';
$passportNo ='sdklaasd';*/


$con = mysqli_connect("localhost", "root", "", "new_jrlu");

if (mysqli_connect_errno($con)){
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}


//Check whether fields are not empty
if (!empty($username) && !empty($password) && !empty($firstname) && !empty($lastname) && !empty($address) && !empty($passportNo)) {
	
	$sql_insert = "INSERT into customer (customerID, firstName, lastName, password, address, dob, passportNo) values('$username', '$firstname', '$lastname', '$password', '$address', '$dob', '$passportNo')";

	$dupesql_username = "SELECT * FROM customer where customerID = '$username'";
	$duperaw_username = $con->query($dupesql_username);

	//Check for duplicates
	if (mysqli_num_rows($duperaw_username) > 0) {
		echo "duplicate";
	}
	else {
		if ($con->query($sql_insert) === TRUE) {
			echo "true";
		}

		else{
			echo "false";
		}
	  }

 }
 else{
 	echo "empty";
 }


mysqli_close($con);

?>

