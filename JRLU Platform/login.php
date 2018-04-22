<?php
session_start();

$username = $_POST['login-username'];
$password = $_POST['login-password'];


$conn = new mysqli("localhost", "root","", "new_jrlu");


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if (!empty($username) && !empty($password)) {

  $sql_admin = "SELECT admin_username, admin_psw from admin WHERE admin_username='$username' and admin_psw='$password'";
  $result_admin = $conn->query($sql_admin);

  
  while ($rs_admin = $result_admin->fetch_array()) {
     $_SESSION['session_username_admin'] = $rs_admin['admin_username'];
     $_SESSION['session_password_admin'] = $rs_admin['admin_psw'];
 }


if ($_SESSION['session_username_admin'] == $username && $_SESSION['session_password_admin']==$password) {
    $js_info = $conn->query("SELECT * FROM admin WHERE admin_username = '$username'");
    while ($row = $js_info->fetch_array()){
        $_SESSION['admin_firstname'] = $row['admin_fs_name'];
        $_SESSION['admin_lastname'] = $row['admin_ls_name'];
        $_SESSION['bankID'] = $row['bankID'];
    }
    header("location: home.php");
}

else{
 $message = "Wrong username or password";
 echo "<script type='text/javascript'>alert('$message'); 
  window.location.href = 'index.php';</script>";
}

} 

$conn->close();

?>