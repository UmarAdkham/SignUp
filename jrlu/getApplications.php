<?php 
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include "DBconfig.php";

$bankID = $_POST['bankID'];
//$bankID = '1';

$result = $con->query("SELECT * from service WHERE bankID = '$bankID'");

$outp = '{"manageRecord":[';
$aa=0;

while($row = $result->fetch_array()){

	if($aa > 0)
	{
		$outp .= ",";
	}

	
	$outp .= '{"serviceName":"'.$row["serviceName"].'",';
	$outp .= '"description":"'.$row["description"].'",';
	$outp .= '"serviceID":"'.$row["serviceID"].'"}';


	$aa = 1;
}




$con->close();

$outp .= ']}';

echo($outp);

?>