<?php

include("DBconfig.php");

$appointmentID = $_POST['appointmentID'];
$branchID = $_POST['branchID'];
$appointment_date = $_POST['appointment_date'];
$interval_id = $_POST['interval_id'];


/*
$appointmentID = 163;
$branchID = 1;
$appointment_date = '2018-5-27';
$interval_id = '1';*/


$getStaffName =  mysqli_query($con, "SELECT username FROM staff
										WHERE branchID = '$branchID'
										and type = 'Bank Teller'
										and username NOT IN (SELECT staffID FROM appointment
															WHERE appointment_date = '$appointment_date'
															and interval_id = '$interval_id' 
															and branchID = '$branchID')
										LIMIT 1") or die(mysqli_error());

while ($myRow = mysqli_fetch_assoc($getStaffName)) {
	$staffUsername = $myRow['username'];
}


$sql_update = "UPDATE appointment SET branchID = '$branchID', staffID = '$staffUsername',appointment_date = '$appointment_date', interval_id = '$interval_id' WHERE appointmentID = '$appointmentID'";

if ($con->query($sql_update) === TRUE) {
	echo "true";
}
else{
	echo "false";
}

mysqli_close($con);

?>

