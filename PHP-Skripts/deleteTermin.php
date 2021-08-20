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

if(!mysqli_query($link, "SELECT * FROM Guwon_Termine")){
    echo ("Error description: " . mysqli_error($link));
}
$j = array('id'=>$id, 'command'=>"delete");
echo json_encode($j);
$query = "DELETE FROM Guwon_Termine WHERE id='$id'";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);
