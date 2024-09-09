<?php
    $con = mysqli_connect("localhost", "root", "", "db_login");
    if (mysqli_connect_errno()){
        echo "gagal konek ke database".mysqli_connect_errno();
    }
?>