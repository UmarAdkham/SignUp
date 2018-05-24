<?php

include("DBconfig.php");

$json = $_POST['fieldNames'];
$serviceID = $_POST['serviceID'];
$customerUsername = $_POST['customerID']; 
/*
$json = '[{"Fieldname":"Firstname","Value":"Umar"},{"Fieldname":"Lastname","Value":"Adkhamov"},{"Fieldname":"Educational Level","Value":"Secondary"}]';
$serviceID = '1';
$customerUsername = "me";*/

$decode = json_decode($json,true);
$result = "";
foreach( $decode as $key => $value ) {
	$fieldname = $decode[$key]['Fieldname'];
	$value = $decode[$key]['Value'];
	$sql_insert = "INSERT into filleddata values('$serviceID', '$customerUsername', '$fieldname', '$value')"; 
	if ($con->query($sql_insert) === TRUE) {
		$result .= "t";
	}

	else{
		$result .= "f";
	}
}
echo $result;

?>