<?php

function send_mobile_notification_request($user_mobile_info, $payload_info)
{
    //Default result
    $result = -1;
    //Change depending on where to send notifications
    $pem_preference = "production";
    $user_device_type = $user_mobile_info['user_device_type'];
    $user_device_key = $user_mobile_info['user_mobile_token'];
    if ($user_device_type == "iOS") {
        $apns_url = NULL;
        $apns_cert = NULL;
        //Apple server listening port
        $apns_port = 2195;
        if ($pem_preference == "production") {
            $apns_url = 'gateway.push.apple.com';
            $apns_cert = __DIR__.'/cert-prod.pem';
        }
        //develop .pem
        else {
            $apns_url = 'gateway.sandbox.push.apple.com';
            $apns_cert = __DIR__.'/cert-dev.pem';
        }
        $stream_context = stream_context_create();
        stream_context_set_option($stream_context, 'ssl', 'local_cert', $apns_cert);
        $apns = stream_socket_client('ssl://' . $apns_url . ':' . $apns_port, $error, $error_string, 2, STREAM_CLIENT_CONNECT,                                   $stream_context);
        $apns_message = chr(0) . chr(0) . chr(32) . pack('H*', str_replace(' ', '', $user_device_key)) . chr(0) . chr(strlen($payload_info)) .                               $payload_info;
        if ($apns) {
            $result = fwrite($apns, $apns_message);
        }
        @socket_close($apns);
        @fclose($apns);
    }
    else if ($user_device_type == "Android") {
        // API access key from Google API's Console
        define('API_ACCESS_KEY', "AIzaSyC3dUE5iOBZ7RDNDfpeHI9b0iBDbDLE7Ps");
        // prep the bundle
        $msg = array
        (
            'message' => json_decode($payload_info)->aps->alert,
            'title' => 'This is a title. title',
            'subtitle' => 'This is a subtitle. subtitle',
            'tickerText' => 'Ticker text here...Ticker text here...',
            'vibrate' => 1,
            'sound' => 1,
            'largeIcon' => 'large_icon',
            'smallIcon' => 'small_icon'
        );
        $fields = array
        (
            'registration_ids' => array($user_device_key),
            'data' => $msg
        );
        $headers = array
        (
            'Authorization: key=' . API_ACCESS_KEY,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt( $ch,CURLOPT_URL,'https://android.googleapis.com/gcm/send' );
        curl_setopt( $ch,CURLOPT_POST, true );
        curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
        curl_setopt( $ch,CURLOPT_RETURNTRANSFER, false );
        curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
        curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
        $result = curl_exec($ch);
        curl_close($ch);
    }
    return $result > 0;
}

function create_payload_json($message) {
    //Badge icon to show at users ios app icon after receiving notification
    $badge = "0";
    $sound = 'default';
    $payload = array();
    $payload['aps'] = array('alert' => $message, 'badge' => intval($badge),'sound' => $sound);
    return json_encode($payload);
}