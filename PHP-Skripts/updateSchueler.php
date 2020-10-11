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

$heute = date("d.m.y");
$column = $postvars["column"];
$value = "[" . $heute . "]" . $postvars["value"];
$value_wo_date = $postvars["value"];
$id = $postvars["id"];


$j = array('column'=>$column, 'value'=>$value, 'id'=>$id);
echo json_encode($j);

if(!mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'")){
    echo ("Error description: " . mysqli_error($link));
}
$retval = mysqli_query($link, "SELECT * FROM Guwon_Schueler WHERE id='$id'");
$row = mysqli_fetch_array($retval, MYSQLI_ASSOC);

if( $column == "Gibon"){
    if($row['Gibon'] != ""){
        $tempvalue = $row['Gibon'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Gibon = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Teul"){
    if($row['Teul'] != ""){
        $tempvalue = $row['Teul'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Teul = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Hanbon"){
    if($row['Hanbon'] != ""){
        $tempvalue = $row['Hanbon'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Hanbon = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Gyeokpa"){
    if($row['Gyeokpa'] != ""){
        $tempvalue = $row['Gyeokpa'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Gyeokpa = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Daeryeon"){
    if($row['Daeryeon'] != ""){
        $tempvalue = $row['Daeryeon'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Daeryeon = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Chayu"){
    if($row['Chayu'] != ""){
        $tempvalue = $row['Chayu'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Chayu = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if( $column == "Hosinsul"){
    if($row['Hosinsul'] != ""){
        $tempvalue = $row['Hosinsul'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Hosinsul = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if($column == "Anwesenheit"){
    if($row['Anwesenheit'] != ""){
        $tempvalue = $row['Anwesenheit'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $retval = mysqli_query($link, "SELECT Anwesenheit_num FROM Guwon_Schueler WHERE id='$id'");
    $row = mysqli_fetch_array($retval, MYSQLI_ASSOC);
    $New_Anwesenheit_num = $row['Anwesenheit_num'] + 1;
    $query = "UPDATE Guwon_Schueler Set Anwesenheit = '$tempvalue', Anwesenheit_num = '$New_Anwesenheit_num' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    };
}
else if($column == "Pruefungs_Bereit"){
    if($row['Pruefungs_Bereit'] != ""){
        $tempvalue = $row['Pruefungs_Bereit'] . ";\n" . $value;
    }else {
        $tempvalue = $value;
    }
    $query = "UPDATE Guwon_Schueler Set Pruefungs_Bereit = '$tempvalue' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    }
}
else if($column == "Rang"){
    $New_Rang = $row['Rang'] + 1;
    $query = "UPDATE Guwon_Schueler Set Rang = '$New_Rang', Pruefungs_Bereit = '' WHERE id = '$id' ";
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    };
}
else if($column == "Anwesenheit_Temp"){
    if($row['Anwesenheit_temp'] == 0){
        $query = "UPDATE Guwon_Schueler Set Anwesenheit_temp = 1 WHERE id = '$id' ";
    }else{
        $query = "UPDATE Guwon_Schueler Set Anwesenheit_temp = 0 WHERE id = '$id' ";
    }
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    };
}
else if($column == "Event_Teilnahme_Temp"){
    if($row['event_teilnahme_temp'] == 0){
        $query = "UPDATE Guwon_Schueler Set event_teilnahme_temp = 1 WHERE id = '$id' ";
    }else{
        $query = "UPDATE Guwon_Schueler Set event_teilnahme_temp = 0 WHERE id = '$id' ";
    }
    if (mysqli_query($link, $query)) {
        echo "Record updated successfully";
    } else {
        echo "Error updating record: " . mysqli_error($link);
    };
}
mysqli_close($link);


