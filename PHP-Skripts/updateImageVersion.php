<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}

$body = file_get_contents('php://input');
$postvars = json_decode($body, true);
$id = $postvars["id"];
$heute = date("d-m-Y");


$j = array('id'=>$id, 'heute'=>$heute);
echo json_encode($j);

$query = "UPDATE Guwon_Schueler SET last_image_version_update = '$heute',
                       image_version = image_version + 1 WHERE id = '$id'";

mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

