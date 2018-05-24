<?php
include("DBconfig.php");
$serviceID = $_POST['serviceID'];
//$serviceID = 20;

//Searching for an Customer with the inserted username and password
$result = mysqli_query($con, "SELECT fieldName, fieldType FROM service_data_type, data_type WHERE service_data_type.dataTypeID = data_type.dataTypeID
	and service_data_type.serviceID = '$serviceID'");

$row_cnt = $result->num_rows;

	$outp = '{"fields":[';
	$a = 0;
	while($row = $result->fetch_array()){
		$fieldName = $row['fieldName'];
		$fieldType = $row['fieldType'];

		if ($a == 1) {
			$outp .= ',';
		}

		$outp .= '{"fieldName":"'.$fieldName.'",';
		$outp .= '"fieldType":"'.$fieldType.'"}';

		$a = 1;
	}
	$outp .= ']}';

echo $outp;



mysqli_close($con);

?>

