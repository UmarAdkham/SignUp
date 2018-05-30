<?php 
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include "DBconfig.php";

$customerID = $_POST['username'];
//$customerID = 'aaa';

$result = $con->query("SELECT * from appointment WHERE customerID = '$customerID' AND status = 0");

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


	$con->close();

	$outp .= ']}';

	echo($outp);

	?>