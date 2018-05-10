<?php
session_start();

?>
<!DOCTYPE html>
<html>
<head>
	<title>Approve</title>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<!-- icon -->
	<link rel="icon" href="icon.ico" type="image/x-icon">

	<!-- Google font: Oswald -->
	<link href="https://fonts.googleapis.com/css?family=Oswald" rel="stylesheet">

	<!-- Built CSS File -->
	<link href="css/formBuilder.css" type="text/css" rel="stylesheet">

	<!-- Bootstrap Core CSS -->
	<link href="css/bootstrap.min.css" rel="stylesheet">

	<!-- Animate.css CSS -->
	<link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css" rel="stylesheet">

	<!-- FontAwesome CSS -->
	<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

	<!-- My own Script -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

</head>

<header style="font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif;">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle = "collapse" data-target="#m-mainNavbar">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">JRLU</a>
			</div>

			<!-- Collect the nav links for toggling -->
			<div class="collapse navbar-collapse" id="m-mainNavbar">
				<p class="navbar-text navbar-right"><?php echo $_SESSION['admin_firstname'] . ' ' . $_SESSION['admin_lastname']; ?></p>

				<a href="logout.php" type="button" id="m-btn-logout" class="btn btn-default navbar-btn navbar-right">Log out</a>
			</div>
			<!-- End .navbar-collapse -->
		</div>
		<!-- End .container -->
	</nav>
</header>


<body>
	<div class="container">
		<div class="row service-builder">
			<div class="col-xs-12 col-sm-3">
				<img src="qr_code_sample.jpg" width="100%">
			</div>
			<div class="col-xs-12 col-sm-7">
				<table class="table table-striped" style="font-size: 15px">
					<tr><th>Application</th><th>Term Loan Facility</th></tr>
					<tr><th>Firstname</th><td>Rupini</td></tr>

					<tr><th>Lastname</th><td>Chandran</td></tr>

					<tr><th>Mykad/Passport No</th><td>AB0307689</td></tr>

					<tr><th>Email</th><td>rupini@gmail.com</td></tr>

					<tr><th>Date of Birth</th><td>18/04/2018</td></tr>

					<tr><th>Address</th><td>Ten Semantan, West Wing 12-04</td></tr>

					<tr><th>Company Reg No</th><td>A123</td></tr>

					<tr><th>Year of Incorporation</th><td>2016</td></tr>
				</table>

				<!-- Submit and Reset buttons for entire form -->
				<form role="form" class="form-horizontal" action="approve.php" method="POST">

					<div class="col-xs-12" style="margin-bottom: 40px;">
						<button type="reset" class="btn btn-reset btn-default ">Cancel</button>
						<button type="submit" class="btn btn-submit btn-success pull-right">Approve	</button>
					</div>
				</form>

				</div>
			</div>
		</div>
	</body>