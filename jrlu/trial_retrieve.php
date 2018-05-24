<?php
include 'AES.php';
$password = 'www';
$passportNo ='BRomimOQTEGKHYm2CEE9ug==';

$blockSize = 256;
$aes = new AES($passportNo, $password, $blockSize);
$dec=$aes->decrypt();

echo $dec;
?>