<?php

$new_psw = password_hash("umar", PASSWORD_DEFAULT);
echo $new_psw;

// See the password_hash() example to see where this came from.
$hash = '$2y$10$fIippBK.RZtEEsXnjCiey.Zm6LIqN/kx45/WXTySt49gbYZxY1Gfm';

if (password_verify('Umar Adkhamov', $hash)) {
    echo 'Password is valid!';
} else {
    echo 'Invalid password.';
}
?>