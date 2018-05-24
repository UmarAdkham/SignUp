<?php
include("DBconfig.php");
$username = $_POST['name'];
$password = $_POST['psw'];

/*$username = 'rupini';
$password = '123';
*/
//Searching for an Customer with the inserted username and password
$resultCheckCustomer = mysqli_query($con, "SELECT * from customer where customerID = '$username'");

	while($rowCheckCustomer = $resultCheckCustomer->fetch_array()){
	$hashed_password = $rowCheckCustomer['password'];
	}
	if ($hashed_password === $password) {
		echo "true";	
	}
	else {
		echo "false";
	}

mysqli_close($con);

?>

