<?php

// return the data to the user so that they know something is happening
echo("This is your data<br>");
print_r($_REQUEST);
echo("<br><br>");

// now connect to the database
	$db = pg_connect('host=localhost user=user5 port=5432 dbname=user5db password=user5password');

	// check if the connection worked – the ! means not
	// in other words – 'if not $db' then exit
	if (!$db) {
	  echo "An error occurred connecting to the database.\n";
	  exit;
	}

// extract the values from the form - using $_REQUEST as this works no matter whether the user has used POST or GET
$point_name = $_REQUEST['n'];
$question = $_REQUEST['q'];
$answer = $_REQUEST['answer'];
$truefalse = $_REQUEST['tf'];
$mobile_id = $_REQUEST['mID'];


// write the SQL insert script to insert the data in the database
$query = "insert into qanswers (point_name, question , answer , true_false , mobile_id ) values (";
$query = $query."'".$point_name."','".$question."','".$answer."','".$truefalse."','".$mobile_id."')";

// show the SQL query to the user (this is useful for debugging purposes)
echo($query);
echo("<br><br>");

// run the insert query
// if the result is TRUE it has worked
if (pg_query($db,$query)) {echo("Your data has been saved to the database");}
else {echo("There was an error saving your data");}

?>