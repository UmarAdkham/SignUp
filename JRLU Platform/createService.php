<?php
session_start();

//Variables for service table
$name = $_POST['service_name'];
$desc = $_POST['description'];
$bankID = $_SESSION['bankID'];
$common_fields = $_POST['common_fields'];
 


//Variables for data_type table
$numOfFields = $_POST['noFields'];
for($i = 0; $i < $numOfFields; $i++) {
	//Getting values as an array. Length of array is not static
	$fieldName[$i] = $_POST["field_name{$i}"];
	$fieldType[$i] = $_POST["field_type{$i}"];
}

$conn = new mysqli("localhost", "root","", "new_jrlu");

if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}



$sql = "INSERT INTO service (serviceName, bankID, description) VALUES ('$name', '$bankID', '$desc')";

//Getting auto inserted ID of the service
if ($conn->query($sql) === TRUE) 
{
	$last_service_id = $conn->insert_id;


//Inserting values into data_type table from checkboxes
	$numOfCheckBoxes = count($common_fields);
	for ($i=0; $i < $numOfCheckBoxes; $i++) { 
		// Specifying data type
		if ($common_fields[$i] === 'Date of Birth') 
		{
			${"sql$i"} = "INSERT INTO data_type (fieldName, fieldType) VALUES ('$common_fields[$i]', 'Date Picker')";
		}
		else
		{
			${"sql$i"} = "INSERT INTO data_type (fieldName, fieldType) VALUES ('$common_fields[$i]', 'Text Area')";
		}
		// End of Specifying data type
		
		if ($conn->query(${"sql$i"}) === TRUE)
		{
			${"last_datatype_id$i"} = $conn->insert_id;
		}
		${"sqlBridge$i"} = "INSERT INTO service_data_type VALUES ('$last_service_id', '${"last_datatype_id$i"}')";
		$conn -> query(${"sqlBridge$i"});
	}


//Inserting values into data_type table from POSTed array
	for ($i=0; $i < $numOfFields; $i++) { 
		${"sql$i"} = "INSERT INTO data_type (fieldName, fieldType) VALUES ('$fieldName[$i]', '$fieldType[$i]')";
		if ($conn->query(${"sql$i"}) === TRUE)
		{
			${"last_datatype_id$i"} = $conn->insert_id;
		}
		${"sqlBridge$i"} = "INSERT INTO service_data_type VALUES ('$last_service_id', '${"last_datatype_id$i"}')";
		$conn -> query(${"sqlBridge$i"});
	}


	$message = "New Service created successfully";
	echo "<script type='text/javascript'>alert('$message');
	window.location.href = 'home.php';
	</script>";
	

} else {
	echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();

?>
