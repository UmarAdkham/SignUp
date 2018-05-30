<?php

include("DBconfig.php");

$appointmentID = $_POST['appointmentID'];
$branchID = $_POST['branchID'];
$appointment_date = $_POST['appointment_date'];
$interval_id = $_POST['interval_id'];


/*
$appointmentID = 1413;
$branchID = 1;
$appointment_date = '2018-5-27';
$interval_id = '1';
*/

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
	$result = $con->query("SELECT * from appointment WHERE appointmentID = '$appointmentID'");

	$outp = '{"manageRecord":[';
	$aa=0;
	while($myRow = $result->fetch_array()){
		$branchID = $myRow['branchID'];
		$appointmentID = $myRow['appointmentID'];
		$serviceID = $myRow['serviceID'];
		$staffID = $myRow['staffID'];
		$appointment_date = $myRow['appointment_date'];
		$interval_id = $myRow['interval_id'];

		$getBankName = $con->query("SELECT bank.bankID, bankName FROM bank, branch WHERE bank.bankID = branch.bankID and branchID = '$branchID'");
		while($myRow2 = $getBankName->fetch_array()){
			$bankName = $myRow2['bankName'];
			$bankID = $myRow2['bankID'];
		}
		$getServiceName = $con->query("SELECT serviceName FROM service WHERE serviceID = '$serviceID'");
		while($myRow3 = $getServiceName->fetch_array()){
			$serviceName = $myRow3['serviceName'];
		}
		$getTime = $con->query("SELECT time_interval FROM time_intervals WHERE interval_id = '$interval_id'");
		while($myRow4 = $getTime->fetch_array()){
			$time_interval = $myRow4['time_interval'];
		}
		$getStaffName = $con->query("SELECT fullname FROM staff WHERE username = '$staffID'");
		while($myRow5 = $getStaffName->fetch_array()){
			$staffName = $myRow5['fullname'];
		}
		$getBranchName = $con->query("SELECT branchName FROM branch WHERE branchID = '$branchID'");
		while($myRow6 = $getBranchName->fetch_array()){
			$branchName = $myRow6['branchName'];
		}

		if($aa > 0)
		{
			$outp .= ",";
		}

		$outp .= '{"appointmentID":"'.$appointmentID.'",';
		$outp .= '"bankName":"'.$bankName.'",';
		$outp .= '"branchName":"'.$branchName.'",';
		$outp .= '"serviceName":"'.$serviceName.'",';
		$outp .= '"staffName":"'.$staffName.'",';
		$outp .= '"appointment_date":"'.$appointment_date.'",';
		$outp .= '"time_interval":"'.$time_interval.'"}';

		$aa = 1;
	}


    $outp .= ']}';

	echo($outp);
}
else{
	echo "false";
}

mysqli_close($con);

?>

