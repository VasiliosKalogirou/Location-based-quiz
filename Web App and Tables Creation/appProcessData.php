<?php

// return the data to the user so that they know something is happening
echo("This is your data<br>");
print_r($_REQUEST);
echo("<br><br>");

// now connect to the database
$db = pg_connect('host=localhost user=user5 port=5432 dbname=user5db password=user5password');
// check if the connection worked: 'if not $db' then exit
	if (!$db) 
	{
	  echo "An error occurred connecting to the database.\n";
	  exit;
	}
// Get the values that the user types in the URL using $_REQUEST as this works no matter whether the user has used POST or GET
$name = $_REQUEST['name'];
$latitude = $_REQUEST['latitude'];
$longitude = $_REQUEST['longitude'];
$question = $_REQUEST['question'];
$answer1 = $_REQUEST['answer1'];
$answer2 = $_REQUEST['answer2'];
$answer3 = $_REQUEST['answer3'];
$answer4 = $_REQUEST['answer4'];
$correct = $_REQUEST['correct'];
//assign the value of the correct answer to the $correct variable according to the radiobutton clicked
if ($correct == 'pa1') {$correct = $answer1;}
elseif ($correct == 'pa2') {$correct = $answer2;}
elseif ($correct == 'pa3') {$correct = $answer3;}
else {$correct = $answer4;}

// format the coordinate string so that it is correctly structures for the SQL statement
$coords ="ST_geomfromtext('POINT(".$latitude." ".$longitude.")')";

$query = "insert into public.qpoints (point_name, question, possible_answer_1, possible_answer_2, possible_answer_3, possible_answer_4, correct_answer, coords) 
values (";$query = $query."'".$name."','".$question."','".$answer1."','".$answer2."','".$answer3."','".$answer4."', '".$correct."', ".$coords.")";

// show the SQL query to the user - useful for debugging purposes
echo($query);
echo("<br><br>");

// run the insert query and check if the result is TRUE then it has worked
if (pg_query($db,$query)) {	echo("Your data has been saved to the database");}
else {echo("There was an error saving your data");}

?>