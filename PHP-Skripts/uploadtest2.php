<?php
if(isset($_FILES["uploaded_file"]["name"])){
    $name = $_FILES["uploaded_file"]["name"];
    $tmp_name = $_FILES["uploaded_file"]["tmp_name"];
    $error = $_FILES["uploaded_file"]["error"];

    if(!empty($name)){
        $location = './Images/';
        if(!is_dir($location))
            mkdir($location);
        if(move_uploaded_file($tmp_name,$location.$name))
            echo json_encode('uploaded');
        else
            echo json_encode('upload failed');
    }
    else
        echo json_encode('Please select file!');
}
?>