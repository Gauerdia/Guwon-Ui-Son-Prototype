<?php
$file_url = "Images/profilphoto.jpg";
header('Content-Type: application/force-download');
header('Content-Disposition: attachment; filename="' . basename($file_url) .'"');
readfile($file_url);