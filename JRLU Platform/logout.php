<?php
  session_start();
  unset($_SESSION["admin_username"]);
  unset($_SESSION["admin_firstname"]);
  unset($_SESSION["admin_lastname"]);
  unset($_SESSION["bankID"]);
  header("Location: index.php");
?>
