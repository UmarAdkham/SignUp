<?php
include 'AES.php';
$username = 'jjaasdssdadd';
$password = 'www';
$firstname = 'firstname';
$lastname = 'lastname';
$dob = 'asd';
$address = 'asd';
$passportNo ='AA0205332';

$blockSize = 256;
$aes_passport = new AES($passportNo, $password, $blockSize);
$enc_passport = $aes_passport->encrypt();
echo $enc_passport;
?>