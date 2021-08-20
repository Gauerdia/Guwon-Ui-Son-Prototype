<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}
$table = 'Schueler_Table';

if(!mysqli_query($link, 'SELECT vorname, nachname FROM Guwon_Schueler')){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, 'SELECT id, vorname, nachname,Overall_Score FROM Guwon_Schueler
                                      WHERE IsTemp = 0');
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '"' . ':' . $row['id'] . ',';
    $j = $j . '"' . 'vorname' . '"' . ':' . '"' . $row['vorname'] . '",';
    $j = $j . '"' . 'nachname' . '"' . ':' . '"' . $row['nachname'] . '",';
    $j = $j . '"' . 'Overall_Score' . '"' . ':' . '"' . $row['Overall_Score'] . '"';
    if(++$counter == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);