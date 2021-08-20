<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}

$j = array( 'response'=>'dummy-response');
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
    }
}

