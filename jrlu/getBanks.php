<?php 
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include "DBconfig.php";

$result = $con->query("SELECT * from bank");

$outp = '{"manageRecord":[';
$aa=0;

while($row = $result->fetch_array()){

	if($aa > 0)
	{
		$outp .= ",";
	}


	$outp .= '{"bankID":"'.$row["bankID"].'",';
	$outp .= '"bankName":"'.$row["bankName"].'"}';

	$aa = 1;
}


$con->close();

$outp .= ']}';

echo($outp);

?>