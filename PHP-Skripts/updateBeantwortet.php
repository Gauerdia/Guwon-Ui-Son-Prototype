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
$vorname = $postvars["vorname"];

$j = array('id'=>$id);


if(!mysqli_query($link, "SELECT * FROM Guwon_Kommunikation WHERE id='$id'")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Kommunikation WHERE id='$id'");

$query = "UPDATE Guwon_Kommunikation Set beantwortet_von = '$vorname' WHERE id = '$id'";

if (mysqli_query($link, $query)) {
    echo json_encode($j);
} else {
    $k = array('error'=>mysqli_error($link));
    echo json_encode($k);
}

