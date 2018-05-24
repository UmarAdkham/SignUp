<?php
/*$username = $_POST['name'];
$firstname = $_POST['firstname'];
$lastname = $_POST['lastname'];
$password = $_POST['psw'];*/

$username = 'umaradkham';
$password = 'psw';
$firstname ='firstname';
$lastname = 'lastname';

$con = mysqli_connect("localhost", "root", "", "new_jrlu");

if (mysqli_connect_errno($con)){
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

/*//checking for duplicate username
$resultRegAdmin = $con->query("SELECT admin_username from admin where admin_username = '$username'");

foreach ($resultRegAdmin as $key => $rst) {
	$admin_username = $rst["admin_username"];
}


if($admin_username != NULL){
	echo "false";
}else{
	//if no insert details*/
	$result = mysqli_query($con, "INSERT into admin values('$username', '$password', '$firstname', '$lastname')");
	echo "true";


mysqli_close($con);

?>

