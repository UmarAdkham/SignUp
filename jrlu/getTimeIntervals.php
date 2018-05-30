<?php
header("Content-Type:text/xml");
include ("DBconfig.php");
$branchID = $_POST['branchID'];
$date = $_POST['date'];
//$date = "2018-05-27";
//$branchID = "10";

//check whether this branch has appointments
$sql = mysqli_query($con, "SELECT branchID FROM appointment WHERE branchID = '$branchID' AND appointment_date = '$date'") or die(mysqli_error());

$row_cnt_appointment = mysqli_num_rows($sql);
if ($row_cnt_appointment > 0) {

	$getNumberOfStaff =  mysqli_query($con, "SELECT username FROM staff WHERE branchID = '$branchID' and type = 'Bank Teller'") or die(mysqli_error());
	$row_cnt_staff = mysqli_num_rows($getNumberOfStaff);
	if ($row_cnt_staff <= $row_cnt_appointment) {
		$result = mysqli_query($con, "SELECT * FROM time_intervals 
			WHERE interval_id NOT IN(SELECT interval_id 
			FROM appointment 
			WHERE branchID = '$branchID' 
			and 
			appointment_date = '$date') ") or die(mysqli_error());
	}
	else{
	$result = mysqli_query($con, "SELECT * FROM time_intervals") or die(mysqli_error());
	}
}
else{
	$result = mysqli_query($con, "SELECT * FROM time_intervals") or die(mysqli_error());
}

$_xml = '<?xml version="1.0"?>';
$_xml .="<times>";

while ($row = mysqli_fetch_assoc($result)) {
	$_xml .="<time>";
	$_xml .="<interval_id>".$row['interval_id']."</interval_id>";
	$_xml .="<time_interval>".$row['time_interval']."</time_interval>";
	$_xml .="</time>";
}

$_xml.="</times>";
$xmlObject = new SimpleXMLElement($_xml);

print $xmlObject->asXML();

?>