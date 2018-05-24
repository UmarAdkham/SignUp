<?php
include("DBconfig.php");
include 'AES.php';
$username = $_POST['name'];
$password = $_POST['psw'];

//$username = 'akmal';
//$password = 'akmal';

//Searching for an Customer with the inserted username and password
$resultCheckCustomer = mysqli_query($con, "SELECT * from customer where customerID = '$username'");

while($rowCheckCustomer = $resultCheckCustomer->fetch_array()){
	$firstname = $rowCheckCustomer['firstName'];
	$lastname = $rowCheckCustomer['lastName'];
	$address = $rowCheckCustomer['address'];
	$dob = $rowCheckCustomer['dob'];
	$passport = $rowCheckCustomer['passportNo'];
	$hashed_password = $rowCheckCustomer['password'];
	$gender = $rowCheckCustomer['gender'];
	$email = $rowCheckCustomer['email'];
	$postcode = $rowCheckCustomer['postcode'];
}

$blockSize = 256;
$firstname_aes = new AES($firstname, $password, $blockSize);
$lastname_aes = new AES($lastname, $password, $blockSize);
$address_aes = new AES($address, $password, $blockSize);
$passport_aes = new AES($passport, $password, $blockSize);


$firstname_dec=$firstname_aes->decrypt();
$lastname_dec=$lastname_aes->decrypt();
$address_dec=$address_aes->decrypt();
$passport_dec=$passport_aes->decrypt();


/*echo $firstname . " " . $firstname_dec;
echo "<br>";
echo $lastname . " " . $lastname_dec;
echo "<br>";
echo $address . " " . $address_dec;
echo "<br>";
echo $dob . " " . $dob_dec;
echo "<br>";
echo $passport . " " . $passport_dec;
echo "<br>";
echo $hashed_password;
echo "<br>";*/


$outp = '{"autofill":[';
$outp .= '{"firstname":"'.$firstname_dec.'",';
$outp .= '"lastname":"'.$lastname_dec.'",';
$outp .= '"address":"'.$address_dec.'",';
$outp .= '"postcode":"'.$postcode.'",';
$outp .= '"dob":"'.$dob.'",';
$outp .= '"email":"'.$email.'",';
$outp .= '"gender":"'.$gender.'",';
$outp .= '"passport":"'.$passport_dec.'"}';
$outp .= ']}';
echo $outp;



mysqli_close($con);

?>

