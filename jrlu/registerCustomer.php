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
$postcode = $_POST['postcode'];
$gender = $_POST['gender'];
$email = $_POST['email'];

/*
$username = 'jjjoo';
$password = 'psw';
$firstname = 'firstname';
$lastname = 'lastname';
$dob = 'asd';
$address = 'asd';
$passportNo ='sdklaasd';
*/

$blockSize = 256;
$firstname_aes = new AES($firstname, $password, $blockSize);
$lastname_aes = new AES($lastname, $password, $blockSize);
$address_aes = new AES($address, $password, $blockSize);
$passport_aes = new AES($passportNo, $password, $blockSize);

$firstname_enc = $firstname_aes->encrypt();
$lastname_enc = $lastname_aes->encrypt();
$address_enc = $address_aes->encrypt();
$passport_enc = $passport_aes->encrypt();

$hash_psw = password_hash($password, PASSWORD_DEFAULT);

//Check whether fields are not empty
if (!empty($username) && !empty($password) && !empty($firstname) && !empty($lastname) && !empty($address) && !empty($passportNo)) {
	
	$sql_insert = "INSERT into customer values('$username', '$firstname_enc', '$lastname_enc', '$hash_psw', '$address_enc', '$postcode', '$dob', '$email', '$gender', '$passport_enc')";

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

