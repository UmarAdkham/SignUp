<?php

include("DBconfig.php");
include 'AES.php';


$json = $_POST['fieldNames'];
$serviceID = $_POST['serviceID'];
$customerUsername = $_POST['customerID'];
$encryption_key = $_POST['key']; 
$blockSize = 256;

/*
$json = '[{"Fieldname":"Firstname","Value":"Umar"},{"Fieldname":"Lastname","Value":"Adkhamov"},{"Fieldname":"Educational Level","Value":"Secondary"}]';
$serviceID = '1';
$customerUsername = "me";
$encryption_key = "kjasnka"; 
*/

$sql_appointment = "INSERT into appointment(serviceID, customerID) values('$serviceID', '$customerUsername')"; 
$result = "";
if ($con->query($sql_appointment) === TRUE) 
{
	$appointmentID = $con->insert_id;
	$decode = json_decode($json,true);
	
	foreach( $decode as $key => $value ) {
		$fieldname = $decode[$key]['Fieldname'];
	//Encrypt value
		$value = $decode[$key]['Value'];
		$value_aes = new AES($value, $encryption_key, $blockSize);
		$value_enc = $value_aes->encrypt();

		$sql_values = "INSERT into filleddata values('$appointmentID', '$fieldname', '$value_enc')"; 
		if ($con->query($sql_values) === TRUE) {
			$result .= "t";
		}

		else{
			$result .= "f";
		}
	}
}
if ($result === "" || strpos($result, "f")) {
	echo $result;
	echo "fail";
}
else{
	echo $appointmentID;
}

?>