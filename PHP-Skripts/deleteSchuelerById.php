<?php
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

if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler")){
    echo ("Error description: " . mysqli_error($link));
}
$j = array('id'=>$id, 'command'=>"delete");
echo json_encode($j);
$query = "DELETE FROM Guwon_Schueler WHERE id='$id'";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);
