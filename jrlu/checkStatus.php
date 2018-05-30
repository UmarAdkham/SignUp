<?php

include("DBconfig.php");
$appointmentID = $_POST['appointmentID'];
//$appointmentID = 1418;

$getStatus =  mysqli_query($con, "SELECT status FROM appointment WHERE appointmentID = '$appointmentID'");

while ($myRow = mysqli_fetch_assoc($getStatus)) {
	$status = $myRow['status'];
}


if ($status > 0) {
	echo "true";
}
else {
	echo "false";
}

mysqli_close($con);

?>

