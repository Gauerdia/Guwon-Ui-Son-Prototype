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
$Mindest_Rang = $postvars["Mindest_Rang"];

if(!mysqli_query($link, "SELECT * FROM Guwon_Quiz")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT id, Frage, Antwort_A, Antwort_B, Antwort_C, Richtige_Antwort 
                                     FROM Guwon_Quiz WHERE Mindest_Rang <= '$Mindest_Rang' ");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
$j = '[';
$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    $j = $j . '{';
    $j = $j . '"' . 'id' . '":' . $row['id'] . ',';
    $j = $j . '"' . 'Frage' . '":"' . $row['Frage'] . '",';
    $j = $j . '"' . 'Antwort_A' . '":"' . $row['Antwort_A'] . '",';
    $j = $j . '"' . 'Antwort_B' . '":"' . $row['Antwort_B'] . '",';
    $j = $j . '"' . 'Antwort_C' . '":"' . $row['Antwort_C'] . '",';
    $j = $j . '"' . 'Richtige_Antwort' . '":"' . $row['Richtige_Antwort'] . '"';
    if(++$counter+1 == $numResults){
        $j = $j . '}';
    } else {
        $j = $j . '},';
    }
}
$j = $j . ']';
echo $j;
mysqli_close($link);