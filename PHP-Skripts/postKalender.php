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
$vorname = $postvars["vorname"];
$nachname = $postvars["nachname"];
$date = $postvars["date"];
$desc = $postvars["desc"];


$j = array('vorname'=>$vorname, 'nachname'=>$nachname,'date'=>$date,'desc'=>$desc);
echo json_encode($j);

$query = "INSERT INTO Guwon_Kalender (vorname, nachname, date, description)
 VALUES('$vorname', '$nachname', '$date', '$description')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

