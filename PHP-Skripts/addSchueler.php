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
$vorname = $postvars["vorname"];
$nachname = $postvars["nachname"];
$Gibon = $postvars["Gibon"];
$Teul = $postvars["Teul"];
$Hanbon = $postvars["Hanbon"];
$Gyeokpa = $postvars["Gyeokpa"];
$Daeryeon = $postvars["Daeryeon"];
$Chayu = $postvars["Chayu"];
$Hosinsul = $postvars["Hosinsul"];
$Pruefungs_Bereit = $postvars["Pruefungs_Bereit"];
$Rang = $postvars["Rang"];
$Anwesenheit = $postvars["Anwensenheit"];
$Anwesenheit_num = $postvars["Anwesenheit_num"];
$IsSchueler = $postvars["Schueler"];
$TaekwonDo = $postvars["TaekwonDo"];
$Kickboxen = $postvars["Kickboxen"];
$Yoga = $postvars["Yoga"];
$Pilates = $postvars["Pilates"];
$password = $postvars["password"];
$last_quiz = $postvars["Last_Quiz"];

$j = array('vorname'=>$vorname, 'nachname'=>$nachname);
echo json_encode($j);
//$query = "INSERT INTO Schueler_Table (vorname, nachname) VALUES('$vorname', '$nachname')";
//mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
$query = "INSERT INTO Guwon_Schueler (vorname, nachname, Gibon,Teul, Hanbon,
                            Gyeokpa,Daeryeon,Chayu,Hosinsul,Pruefungs_Bereit,
                            Rang,Anwesenheit, Anwesenheit_num, IsSchueler, 
                            TaekwonDo,Kickboxen,Yoga,Pilates, password, Last_Quiz)
 VALUES('$vorname', '$nachname', '$Gibon', '$Teul'
, '$Hanbon', '$Gyeokpa', '$Daeryeon', '$Chayu', '$Hosinsul', '$Pruefungs_Bereit', 
        '$Rang', '$Anwesenheit', '$Anwesenheit_num', '$IsSchueler','$TaekwonDo','$Kickboxen',
        '$Yoga','$Pilates', '$pasword', '$last_quiz')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

