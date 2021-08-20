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
$username = "" . $postvars["username"];

if(!mysqli_query($link, 'SELECT vorname, nachname, Anwesenheit_temp,TaekwonDo,Kickboxen,Yoga,Pilates
                               FROM Guwon_Schueler')){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, 'SELECT vorname,nachname,id, Anwesenheit_temp, Anwesenheit_Std_temp,
                                            TaekwonDo,Kickboxen,Yoga,Pilates
                                     FROM Guwon_Schueler WHERE IsTemp = 0');
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '"' . ':' . $row['id'] . ',';
    $j = $j . '"' . 'vorname' . '"' . ':' . '"' . $row['vorname'] . '",';
    $j = $j . '"' . 'nachname' . '"' . ':' . '"' . $row['nachname'] . '",';
    $j = $j . '"' . 'Anwesenheit_temp' . '"' . ':' . $row['Anwesenheit_temp'] . ',';
    $j = $j . '"' . 'TaekwonDo' . '"' . ':"' . $row['TaekwonDo'] . '",';
    $j = $j . '"' . 'Kickboxen' . '"' . ':"' . $row['Kickboxen'] . '",';
    $j = $j . '"' . 'Yoga' . '"' . ':"' . $row['Yoga'] . '",';
    $j = $j . '"' . 'Pilates' . '"' . ':"' . $row['Pilates'] . '",';
    $j = $j . '"' . 'username' . '"' . ':' . '"' . $username . '"';
    if(++$counter == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
//echo json_encode($j);
mysqli_close($link);