<?php
	header("Content-Type:text/xml");
	$connection = mysqli_connect('localhost','root','','new_jrlu') or die(mysqli_error());
	
	$result = mysqli_query($connection, "SELECT * from branch") or die(mysqli_error());
	
	$_xml = '<?xml version="1.0"?>';
	$_xml .="<branches>";
	
	while($row=mysqli_fetch_assoc($result)) {
		$_xml .="<branch>";
		$_xml .="<branchName>".$row['branchName']."</branchName>";
		$_xml .="</branch>";	
	}
	$_xml.="</branches>";
	$xmlObject = new SimpleXMLElement($_xml);
	
	print $xmlObject->asXML();
	//print $xmlObject->asXML("SSS.xml");
?>