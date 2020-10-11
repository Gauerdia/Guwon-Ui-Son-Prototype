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

$value = $postvars["value"];
$target = $postvars["target"];
$excerpt = $postvars["excerpt"];


$j = array('target'=>$target, 'value'=>$value);
echo json_encode($j);

if(!mysqli_query($link, "SELECT * FROM Guwon_Aktuelles_Content")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Aktuelles_Content ");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);

$query;
switch($target){
    case 0:
        $query = "UPDATE Guwon_Aktuelles_Content Set content = '$value', excerpt = '$excerpt' WHERE name = 'Banner' ";
        break;
    case 1:
        $query = "UPDATE Guwon_Aktuelles_Content Set content = '$value', excerpt = '$excerpt' WHERE name = 'SubBanner1' ";
        break;
    case 2:
        $query = "UPDATE Guwon_Aktuelles_Content Set content = '$value', excerpt = '$excerpt' WHERE name = 'SubBanner2' ";
        break;
    default:
        break;
}
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
mysqli_close($link);

