<?php
/* Note:  This code is adapted from source code found here: http://stackoverflow.com/questions/17775627/creating-a-geojson-in-php-from-mysql-to-use-with-mapbox-javascript-api
   Accessed: 6th May 2016
 */

	// Step 1: Connect to the PostGreSQL database
	// we use localhost as the database and webserver are on the same machine
	$db = pg_connect('host=localhost user=user5 port=5432 dbname=user5db password=user5password');

	// check if the connection worked – the ! means not (if not $db' then exit)
	if (!$db) 
	{
	  echo "An error occurred connecting to the database.\n";
	  exit;
	}

	// Step 2: Get the data for the questions from the qpoints table using SQL - $query
	$query = "select id, point_name, question, possible_answer_1, possible_answer_2, possible_answer_3, possible_answer_4, correct_answer, ST_astext(coords) as coords from qpoints";
	$result = pg_query($db,$query);

	// check if it worked, and if not exit
	if (!$result) 
	{
	  echo "An error occurred running the query.\n";
	  exit;
	}

	# Step 3: Build a GeoJSON variable - this variable is a feature collection array
	$geojson = array('type' => 'FeatureCollection', 'features'  => array());


	// add a simple counter to count the number of points
	// this is used by GoogleMaps on Android to display the correct pop-up on the point
	//set the id to zero to get in the while loop
	$id=0;
	# Loop through rows to build feature arrays
	while ($row = pg_fetch_array($result))  
	{
		// first get all the attributes - these are the 'properties' of each point
		$properties = $row;

		// the coordinates are unnecessary at this point, so remove these from the attributes and handle them separately
		unset($properties['coords']);

		// now handle the coords - these are currently a string so we just need to extract the lat/lng (latitude/longitude values)
		$coords = $row['coords'];

		// first remove the word 'POINT' and replace it with '' (i.e. no text)
		$coords = str_replace('POINT(','',$coords);
		// then remove the right bracket and replace it with ''
		$coords = str_replace(')','',$coords);
		// now extract the $lat and $lng values from the remaining string by creating an array - using the explode function
		$coordvalues = explode(' ',$coords);

		$lat = $coordvalues[0];
		$lng = $coordvalues[1];
		$id = $id +1;

		$feature = array(
			'type' => 'Feature',
			'properties' => $properties,
						'geometry' => array(
						'type' => 'Point',
						'coordinates' => array($lat, $lng)),
						'id'=>$id);
		# Add feature arrays to the geojson feature collection array
		array_push($geojson['features'], $feature);
	}

	// Step 4: Echo the data out -  the type of file is set to a JSON file using the 'header' statement
	header('Content-type: application/json');
	echo json_encode($geojson, JSON_NUMERIC_CHECK);

	// disconnect from the database
	$db = NULL;
?>