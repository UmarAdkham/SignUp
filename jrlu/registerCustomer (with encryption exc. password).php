<?php

include 'AES.php';
include("DBconfig.php");
$username = $_POST['name'];
$firstname = $_POST['firstname'];
$lastname = $_POST['lastname'];
$dob = $_POST['dob'];
$address = $_POST['address'];
$passportNo = $_POST['passportNo'];
$password = $_POST['psw'];

/*$username = 'jjaasdssdadd';
$password = 'psw';
$firstname = 'firstname';
$lastname = 'lastname';
$dob = 'asd';
$address = 'asd';
$passportNo ='sdklaasd';*/


$blockSize = 256;
$firstname_aes = new AES($firstname, $password, $blockSize);
$lastname_aes = new AES($lastname, $password, $blockSize);
$dob_aes = new AES($dob, $password, $blockSize);
$address_aes = new AES($address, $password, $blockSize);
$passport_aes = new AES($passportNo, $password, $blockSize);

$firstname_enc = $firstname_aes->encrypt();
$lastname_enc = $lastname_aes->encrypt();
$dob_enc = $dob_aes->encrypt();
$address_enc = $address_aes->encrypt();
$passport_enc = $passport_aes->encrypt();


//Check whether fields are not empty
if (!empty($username) && !empty($password) && !empty($firstname) && !empty($lastname) && !empty($address) && !empty($passportNo)) {
	
	$sql_insert = "INSERT into customer (customerID, firstName, lastName, password, address, dob, passportNo) values('$username', '$firstname_enc', '$lastname_enc', '$password', '$address_enc', '$dob_enc', '$passport_enc')";

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

