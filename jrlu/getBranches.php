<?php
header("Content-Type:text/xml");
include ("DBconfig.php");
$bankID = $_POST['bankID'];
//$bankID = "3";

$result = mysqli_query($con, "SELECT * FROM branch WHERE bankID = '$bankID'") or die(mysqli_error());

$_xml = '<?xml version="1.0"?>';
$_xml .="<branches>";

while($row=mysqli_fetch_assoc($result)) {
	$_xml .="<branch>";
	$_xml .="<branchID>".$row['branchID']."</branchID>";
	$_xml .="<branchName>".$row['branchName']."</branchName>";
	$_xml .="</branch>";	
}
$_xml.="</branches>";
$xmlObject = new SimpleXMLElement($_xml);

print $xmlObject->asXML();
	//print $xmlObject->asXML("SSS.xml");
?>