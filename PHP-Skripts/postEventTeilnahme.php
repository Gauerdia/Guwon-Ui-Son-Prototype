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

$tag = $postvars["tag"];
$monat = $postvars["monat"];
$jahr = $postvars["jahr"];
$stunde = $postvars["stunde"];
$minuten = $postvars["minuten"];
$anlass = $postvars["anlass"];
$ort = $postvars["ort"];
$uhrzeit = $stunde . ":" . $minuten;

$j = array( 'username'=>$username,'stunden'=>$stunde,'asnlass'=>$anlass);
echo json_encode($j);

if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler");

while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC))
{
    // firstly, we check, if the person has been marked by the user
    $temp_event_teilnahme = $row['event_teilnahme_temp'];

    if($temp_event_teilnahme == 1){
        $temp_id = $row['id'];
        // we reset the temp-variable of all users
        $query = "UPDATE Guwon_Schueler Set event_teilnahme_temp = 0 WHERE id = '$temp_id' ";
        if (mysqli_query($link, $query)) {
            echo "Record updated successfully";
        } else {
            echo "Error updating record: " . mysqli_error($link);
        }
        // we insert an entry for each member who is added to the event
        // in a separated table.
        $query ="INSERT INTO Guwon_Events(teilnehmer_id, anlass, ort, uhrzeit, tag, monat, jahr) 
                 VALUES('$temp_id','$anlass','$ort','$uhrzeit','$tag','$monat','$jahr')";
        if (mysqli_query($link, $query)) {
            echo "Record updated successfully";
        } else {
            echo "Error updating record: " . mysqli_error($link);
        }
    }
}

