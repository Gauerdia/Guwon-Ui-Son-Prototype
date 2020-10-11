<?php
//echo 'JDecode Echo: ';
$link = mysqli_connect("mysql04.manitu.net", "u24888", "pWAXVAeaVEXu", "db24888");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}
if(!mysqli_query($link, 'SELECT content, name FROM Guwon_Aktuelles_Content')){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, 'SELECT * FROM Guwon_Aktuelles_Content');
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '"' . ':' . $row['id'] . ',';
    $j = $j . '"' . 'content' . '"' . ':"' . $row['content'] . '",';
    $j = $j . '"' . 'name' . '"' . ':"' . $row['name'] . '",';
    $j = $j . '"' . 'last_update' . '"' . ':"' . $row['last_update'] . '",';
    $j = $j . '"' . 'excerpt' . '"' . ':"' . $row['excerpt'] . '"';
    if(++$counter == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);