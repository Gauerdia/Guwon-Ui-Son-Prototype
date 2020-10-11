<?php
//echo 'JDecode Echo: ';
$link = mysqli_connect("mysql04.manitu.net", "u24888", "pWAXVAeaVEXu", "db24888");
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

$query = "UPDATE Guwon_Schueler SET last_log_in = '$heute' WHERE id = '$id'";

mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

