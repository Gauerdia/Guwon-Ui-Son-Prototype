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
$filename = $postvars["filename"];
$heute = date("d-m-Y");


$j = array('filename'=>$filename, 'heute'=>$heute);
echo json_encode($j);

    $query = "UPDATE Guwon_Banner_Update SET last_update = '$heute',
                       version = version + 1 WHERE name = '$filename'";

mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

