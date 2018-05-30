<?php

include("DBconfig.php");
$appointmentID = $_POST['appointmentID'];
//$appointmentID = 1420;

$getServiceID =  mysqli_query($con, "SELECT serviceID FROM appointment WHERE appointmentID = '$appointmentID'");

while ($myRow = mysqli_fetch_assoc($getServiceID)) {
	$serviceID = $myRow['serviceID'];
}

$getHardcopies =  mysqli_query($con, "SELECT documentName FROM service_hardcopy, hardcopy 
										WHERE service_hardcopy.hardcopyID = hardcopy.hardcopyID 
										AND serviceID = '$serviceID'");

$hardcopies = "";
$a = 0;
while ($myRow2 = mysqli_fetch_assoc($getHardcopies)) {
	if ($a > 0) {
		$hardcopies .= ", ";
	}
	$hardcopies .= $myRow2['documentName'];
	$a = 1;
}

	echo $hardcopies;


mysqli_close($con);

?>
