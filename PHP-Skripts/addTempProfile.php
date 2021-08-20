<?php
$link = mysqli_connect("****", "****", "****", "****");
if (!$link) {
    echo 'Verbindung schlug fehl';
}
if (mysqli_connect_errno()) {
    echo 'Connection Error';
}

$retval = mysqli_query($link, 'SELECT * FROM Guwon_Schueler');
$temp_name;
$temp_id;

$numResults = mysqli_num_rows($retval);
$counter = 0;
while($row = mysqli_fetch_array($retval, MYSQLI_ASSOC)){
    if(++$counter == $numResults) {
        $temp_name = "temp" . ($row["id"]+1);
        $temp_id = $row["id"]+1;
    }
}



$query = "INSERT INTO Guwon_Schueler (vorname, nachname, Gibon,Teul, Hanbon,
                            Gyeokpa,Daeryeon,Chayu,Hosinsul,Pruefungs_Bereit,
                            Rang,Anwesenheit, Anwesenheit_num, IsTemp, 
                            TaekwonDo,Kickboxen,Yoga,Pilates, password, Last_Quiz)
 VALUES('$temp_name', 'temp', 'temp', 'temp', 'temp', 'temp', 'temp', 'temp', 'temp', 'temp', 
        0, 0, 0, 1,0,0,0,0, 'temp', ' 24-02-2019')";
mysqli_query($link,$query) or trigger_error(mysqli_error($link)." in ".$query);

$temp_id = mysqli_insert_id( $link );
$j = array('id'=>$temp_id);
echo json_encode($j);

mysqli_close($link);

