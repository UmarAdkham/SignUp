<?php
header("Content-Type:text/xml");

include ("DBconfig.php");
$branchID = $_POST['branchID'];
$date = $_POST['date'];
/*$date = "2018-05-25";
$branchID = "10";*/
$day = date('l', strtotime($date));


//check whether this branch has appointments
$sql = mysqli_query($con, "SELECT branchID FROM appointment WHERE branchID = '$branchID' AND appointment_date = '$date'") or die(mysqli_error());

$row_cnt_branch = mysqli_num_rows($sql);

if ($row_cnt_branch > 0) {

	//check how many tellers does the bank have for today
	$check_teller = mysqli_query($con, "SELECT * from staff, teller_time_slots, time_slots 
		WHERE staff.username = teller_time_slots.tellerID
		AND teller_time_slots.timeID = time_slots.timeID
		AND day = '$day'
		AND branchID = '$branchID' and type = 'Bank Teller'") or die(mysqli_error());
	
	$row_cnt_teller = mysqli_num_rows($check_teller);
	//echo "number of staff " . $row_cnt_teller;

	//if branch has more tellers than number of appointments made
	if ($row_cnt_teller > $row_cnt_branch) {
		$result = mysqli_query($con, "SELECT time_interval FROM time_intervals") or die(mysqli_error());
	}
	else{
		$get_interval_id = mysqli_query($con, "SELECT interval_id FROM appointment WHERE branchID='$branchID'") or die(mysqli_error());
		while($myRow = $get_interval_id->fetch_array()){
			$interval_id[] = $myRow['interval_id'];
		}
		$result = mysqli_query($con, "SELECT time_interval FROM time_intervals WHERE time_intervals.interval_id NOT IN('$interval_id[0]', $interval_id[1])") or die(mysqli_error());

	}
}
//if branch does not have any appointments
else{
	$result = mysqli_query($con, "SELECT time_interval FROM time_intervals") or die(mysqli_error());
}

$_xml = '<?xml version="1.0"?>';
$_xml .="<times>";

while($row=mysqli_fetch_assoc($result)) {
	$_xml .="<time>";
	$_xml .="<time_interval>".$row['time_interval']."</time_interval>";
	$_xml .="</time>";	
}
$_xml.="</times>";
$xmlObject = new SimpleXMLElement($_xml);

print $xmlObject->asXML();
	//print $xmlObject->asXML("SSS.xml");*/
?>

