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

if(!mysqli_query($link, "SELECT * FROM Guwon_Events")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Events WHERE teilnehmer_id='$id'");
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '":' . $row['id'] . ',';
    $j = $j . '"' . 'turnier' . '":' . $row['turnier'] . ',';
    $j = $j . '"' . 'lehrgang' . '":' . $row['lehrgang'] . ',';
    $j = $j . '"' . 'pruefung' . '":' . $row['pruefung'] . ',';
    $j = $j . '"' . 'teilnehmer_id' . '":' . $row['teilnehmer_id'] . ',';
    $j = $j . '"' . 'anlass' . '":"' . $row['anlass'] . '",';
    $j = $j . '"' . 'ort' . '":"' . $row['ort'] . '",';
    $j = $j . '"' . 'tag' . '":' . $row['tag'] . ',';
    $j = $j . '"' . 'monat' . '":"' . $row['monat'] . '",';
    $j = $j . '"' . 'jahr' . '":"' . $row['jahr'] . '",';
    $j = $j . '"' . 'uhrzeit' . '":"' . $row['uhrzeit'] . '"';
    if(++$counter == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);