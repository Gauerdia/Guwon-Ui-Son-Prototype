<?php
//echo 'JDecode Echo: ';
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
$user = $postvars["user"];
$value = "[" . $date . "]" . $postvars["user"];
$vorname = $postvars["vorname"];
$nachname = $postvars["nachname"];
$desc = $postvars["desc"];
$value2 = $user . ":" . $desc;

$j = array('id' => $id, 'date' => $date);
echo json_encode($j);

$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);

if ($row['Anwesenheit'] != "") {
    $tempvalue = $row['Anwesenheit'] . ";\n" . $value;
} else {
    $tempvalue = $value;
}
$query = "UPDATE Guwon_Schueler Set Anwesenheit = '$tempvalue' WHERE id = '$id' ";
if (mysqli_query($link, $query)) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . mysqli_error($link);
}
$query2 ="INSERT INTO Guwon_Kalender(vorname, nachname, date, description) VALUES('$vorname','$nachname','$date','$value2')";
if (mysqli_query($link, $query2)) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . mysqli_error($link);
}
