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

$heute = date("d-m-Y");
$username = "[" . $heute . "]" . $postvars["username"];
$username_wo_date = $postvars["username"];
$stunden = $postvars["username"] . ":" . $postvars["stunden"] . " Stunden";
$punkte = $postvars["punkte"];

$j = array( 'username'=>$username,'stunden'=>$stunden,'punkte'=>$punkte);
echo json_encode($j);

if(!mysqli_query($link, "SELECT Anwesenheit FROM Guwon_Schueler WHERE Anwesenheit_temp='Ja' ")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler");

while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC))
{
    // firstly, we check, if the person has been marked by the user
    $tempanwesen = $row['Anwesenheit_temp'];
    if($tempanwesen == 1){
        //tempvalue takes the existing value of attendances, puts a break in
        //and augments it with the incoming data from the user.
        $tempvalue = $row['Anwesenheit'] . ";\n" . $username;
        //temppunkte extends the existing score.
        $temppunkte = $row['Overall_Score'] + $punkte;
        $temp_anwesenheit_punkte = $row['Anwesenheit_Score'] + $punkte;
        $tempid =$row['id'];
        // Anwesenheit_num is an iterator to see fastly, how often someone's been
        // at the training.
        $prior_anwesenheit_num = $row['Anwesenheit_num'];
        $query = "UPDATE Guwon_Schueler Set Anwesenheit = '$tempvalue', Anwesenheit_temp = 0,
                  Anwesenheit_num = $prior_anwesenheit_num + 1, 
                  Anwesenheit_Score = '$temp_anwesenheit_punkte', Overall_Score = '$temppunkte' WHERE id = '$tempid' ";
        if (mysqli_query($link, $query)) {
            echo "Record updated successfully";
        } else {
            echo "Error updating record: " . mysqli_error($link);
        }
        // then, we post into the Kalender-Table, so that the person can see his own
        // attendances.
        $vorname = $row['vorname'];
        $nachname = $row['nachname'];

        $query ="INSERT INTO Guwon_Kalender(vorname, nachname, custom_date, description) 
                 VALUES('$vorname','$nachname','$heute','$stunden')";
        if (mysqli_query($link, $query)) {
            echo "Record updated successfully";
        } else {
            echo "Error updating record: " . mysqli_error($link);
        }
    }
}

