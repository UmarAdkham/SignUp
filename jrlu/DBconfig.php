<?php
//Configuration of database connection
$host = "localhost"; /* Host name */
$user = "root"; /* User */
$password = ""; /* Password */
$dbname = "new_jrlu"; /* Database name */

$con = mysqli_connect($host, $user, $password,$dbname);
// Check connection
if (!$con) {
 die("Failed to connect to MySQL: " . mysqli_connect_error());
}

?>