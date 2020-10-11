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

if(!mysqli_query($link, "SELECT * FROM Guwon_Photo_Update")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Photo_Update");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '":' . $row['id'] . ',';
    $j = $j . '"' . 'photo_id' . '":' . $row['photo_id'] . ',';
    $j = $j . '"' . 'last_update' . '":"' . $row['last_update'] . '"';
    if(++$counter+1 == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);