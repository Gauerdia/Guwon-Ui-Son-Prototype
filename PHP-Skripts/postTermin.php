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
$tag = $postvars["tag"];
$monat = $postvars["monat"];
$jahr = $postvars["jahr"];
$stunde = $postvars["stunde"];
$minute = $postvars["minute"];
$anlass = $postvars["anlass"];
$ort = $postvars["ort"];

$uhrzeit_komplett = $stunde . ":" . $minute;

$j = array('tag'=>$tag, 'anlass'=>$anlass);
echo json_encode($j);
$query = "INSERT INTO Guwon_Termine (uhrzeit, anlass, ort, tag, monat, jahr)
 VALUES('$uhrzeit_komplett', '$anlass', '$ort', '$tag','$monat','$jahr')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

