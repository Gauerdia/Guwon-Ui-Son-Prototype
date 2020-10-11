<?php
//echo 'JDecode Echo: ';
$link = mysqli_connect("mysql04.manitu.net", "u24888", "pWAXVAeaVEXu", "db24888");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}

$body = file_get_contents('php://input');
$postvars = json_decode($body, true);
$Frage = $postvars["Frage"];
$Antwort_A = $postvars["Antwort_A"];
$Antwort_B = $postvars["Antwort_B"];
$Antwort_C = $postvars["Antwort_C"];
$Richtige_Antwort = $postvars["Richtige_Antwort"];
$Mindest_Rang = $postvars["Mindest_Rang"];


$j = array('Frage'=>$Frage, 'Antwort_A'=>$Antwort_A);
echo json_encode($j);
//$query = "INSERT INTO Schueler_Table (vorname, nachname) VALUES('$vorname', '$nachname')";
//mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
$query = "INSERT INTO Guwon_Quiz(Frage, Antwort_A, Antwort_B,Antwort_C, Mindest_Rang,
                            Richtige_Antwort)
 VALUES('$Frage', '$Antwort_A', '$Antwort_B', '$Antwort_C', '$Mindest_Rang', '$Richtige_Antwort')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

