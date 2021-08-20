<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}

if(!mysqli_query($link, "SELECT * FROM Guwon_Kommunikation")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Kommunikation");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '"' . ':' . $row['id'] . ',';
    $j = $j . '"' . 'vorname_sender' . '"' . ':' . '"' . $row['vorname_sender'] . '",';
    $j = $j . '"' . 'nachname_empfaenger' . '"' . ':' . '"' . $row['nachname_empfaenger'] . '",';
    $j = $j . '"' . 'vorname_empfaenger' . '"' . ':' . '"' . $row['vorname_empfaenger'] . '",';
    $j = $j . '"' . 'nachname_sender' . '"' . ':' . '"' . $row['nachname_sender'] . '",';
    $j = $j . '"' . 'nachricht' . '"' . ':' . '"' . $row['nachricht'] . '",';
    $j = $j . '"' . 'betreff' . '"' . ':' . '"' . $row['betreff'] . '",';
    $j = $j . '"' . 'datum' . '"' . ':' . '"' . $row['datum'] . '",';
    $j = $j . '"' . 'id_sender' . '"' . ':' . $row['id_sender'] . ',';
    $j = $j . '"' . 'id_empfaenger' . '"' . ':'. $row['id_empfaenger'] . ',';
    $j = $j . '"' . 'gelesen' . '"' . ':' . $row['gelesen'] . ',';
    $j = $j . '"' . 'beantwortet_von' . '"' . ':"' . $row['beantwortet_von'] . '"';
    if(++$counter+1 == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);