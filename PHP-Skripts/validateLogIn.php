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
$name = $postvars["name"];
$password = $postvars["password"];
$success = 0;
$id = 0;
$auth = 0;

if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE vorname='$name'")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE vorname='$name' AND password='$password'");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
if($row != null){
    $success = 1;
    $id = $row['id'];
    $auth = $row['authorized'];
}
$j = array('success'=>$success, 'id'=>$id, 'authorized'=>$auth);
echo json_encode($j);