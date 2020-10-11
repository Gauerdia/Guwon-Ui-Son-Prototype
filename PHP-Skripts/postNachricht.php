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
$vorname_sender = $postvars["vorname_sender"];
$nachname_sender = $postvars["nachname_sender"];
$betreff = $postvars["betreff"];
$nachricht = $postvars["nachricht"];
$datum = $postvars["datum"];
$id_empfaenger = $postvars["id_empfaenger"];
$id_sender = $postvars["id_sender"];


$j = array('vorname'=>$vorname, 'text'=>$text);
echo json_encode($j);
$query = "INSERT INTO Guwon_Kommunikation (vorname_sender, nachname_sender,betreff,nachricht,
                                 datum,id_sender,id_empfaenger,vorname_empfaenger,nachname_empfaenger,
                                 beantwortet_von)
 VALUES('$vorname_sender', '$nachname_sender', '$betreff', '$nachricht', '$datum','$id_sender',
        '$id_empfaenger','temp', 'temp', 'niemand')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);
mysqli_close($link);

