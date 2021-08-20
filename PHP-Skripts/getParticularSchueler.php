<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}
$table = 'Schueler_Table';

$body = file_get_contents('php://input');
$postvars = json_decode($body, true);
$id = $postvars["id"];

if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
$eventscore = $row['Turnier_Score'] + $row['Lehrgang_Score'];
$j = array('vorname'=>$row['vorname'], 'nachname'=>$row['nachname'], 'id' => $row['id'],
           'Gibon'=>$row['Gibon'],'Teul'=>$row['Teul'],'Hanbon'=>$row['Hanbon'],'Gyeokpa'=>$row['Gyeokpa'],
           'Daeryeon'=>$row['Daeryeon'],'Chayu'=>$row['Chayu'],'Hosinsul'=>$row['Hosinsul'],'Pruefungs_Bereit'=>$row['Pruefungs_Bereit'],
           'Rang'=>$row['Rang'],'Anwesenheit'=>$row['Anwesenheit'], 'Anwesenheit_num'=>$row['Anwesenheit_num'], 'Anwesenheit_temp'=>$row['Anwesenheit_temp'],
    'IsTemp'=>$row['IsTemp'],'TaekwonDo'=>$row['TaekwonDo'],'Kickboxen'=>$row['Kickboxen'],'Yoga'=>$row['Yoga'],
    'Pilates'=>$row['Pilates'], 'last_quiz'=>$row['Last_Quiz'], 'authorized'=>$row['authorized'], 'Overall_Score'=>$row['Overall_Score'],
    'Anwesenheit_Score'=>$row['Anwesenheit_Score'],'Quiz_Score'=>$row['Quiz_Score'],'Event_Score'=>$eventscore);
echo json_encode($j);
mysqli_close($link);