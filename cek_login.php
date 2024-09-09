<?php
    include 'koneksi.php';

    $result = array();

    $username = $_POST['username'];
    $password = $_POST['password'];

    $query = mysqli_query($con, "SELECT * FROM `tb_login` WHERE username='$username' AND password=md5($password)");
    while ($row = mysqli_fetch_assoc($query)){
        $result[] = $row;
    }
    
    echo json_encode(array('result' => $result));
?>