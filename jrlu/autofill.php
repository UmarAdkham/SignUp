<?php
include("DBconfig.php");
include 'AES.php';
$username = $_POST['name'];
$password = $_POST['psw'];

//$username = 'anon';
//$password = 'www';

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
$email_aes = new AES($email, $password, $blockSize);
$dob_aes = new AES($dob, $password, $blockSize);


//Decrypt 
$firstname_dec=$firstname_aes->decrypt();
$lastname_dec=$lastname_aes->decrypt();
$address_dec=$address_aes->decrypt();
$passport_dec=$passport_aes->decrypt();
$email_dec=$email_aes->decrypt();
$dob_dec=$dob_aes->decrypt();


//Generate JSON
$outp = '{"autofill":[';
$outp .= '{"firstname":"'.$firstname_dec.'",';
$outp .= '"lastname":"'.$lastname_dec.'",';
$outp .= '"address":"'.$address_dec.'",';
$outp .= '"postcode":"'.$postcode.'",';
$outp .= '"dob":"'.$dob_dec.'",';
$outp .= '"email":"'.$email_dec.'",';
$outp .= '"gender":"'.$gender.'",';
$outp .= '"passport":"'.$passport_dec.'"}';
$outp .= ']}';
echo $outp;


mysqli_close($con);

?>

