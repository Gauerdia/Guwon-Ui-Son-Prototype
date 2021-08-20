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
$date = $postvars["date"];
$punkte = $postvars["punkte"];

$j = array('date'=>$date,'id'=>$id, 'punkte'=>$punkte);


if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
$q_score = $row['Quiz_Score'] + $punkte;
$o_score = $row['Overall_Score'] + $punkte;

$query = "UPDATE Guwon_Schueler Set Last_Quiz = '$date',
          Quiz_Score = '$q_score', Overall_Score = '$o_score' WHERE id = '$id' ";
if (mysqli_query($link, $query)) {
    echo json_encode($j);
} else {
    $k = array('error'=>mysqli_error($link));
    echo json_encode($k);
}

