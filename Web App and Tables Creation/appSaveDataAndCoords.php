<!DOCTYPE html>
<html>
<!-- taken from: http://code.google.com/apis/maps/documentation/javascript/examples/map-simple.html -->
  <head>
    <title>Google Maps JavaScript API v3 Example: Map Simple</title>
    <meta name="viewport"
        content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta charset="UTF-8">
	<!--This is the CSS part of the webpage. Following comments are from https://www.w3.org/standards/webdesign/htmlcss -->
	<!--HTML (the Hypertext Markup Language) and CSS (Cascading Style Sheets) are two of the core technologies for building Web pages. -->
	<!--HTML provides the structure of the page, CSS the (visual and aural) layout, for a variety of devices -->
    <style type="text/css">
      html, body, #map_canvas 
	  {
        margin: 0;
        padding: 0;
        height: 100%;
      }
    </style>

	<!--The following script is in Javascript and references the Google Maps Javascript API-->
    <script 
		type="text/javascript"
		src="http://maps.googleapis.com/maps/api/js?sensor=false"> // sensor = false because there is not GPS 
	</script>

    <script 
		type="text/javascript">
		var map;

		// this is the function to initialise the map when the page loads - it is called by the 'addDomListener' call below
		function initialize() 
		{
			// set up startup options for the map: which zoom level, which locaiton should the map be centered on and which basemap type should appear
			var myOptions = {
			  zoom: 15,
			  center: new google.maps.LatLng(51.52397146424084, -0.13406753540039062),
			  mapTypeId: google.maps.MapTypeId.ROADMAP};

			// create the new map
			map = new google.maps.Map(document.getElementById('map_canvas'), myOptions);

			// create a new point to add on the map, using the organisations.png icon
			var image = 'organisations.png';
			var myLatLng = new google.maps.LatLng(51.52397146424084, -0.13406753540039062);
			var beachMarker = new google.maps.Marker({position: myLatLng, map: map, icon: image});

			// set the text contents of the popup
		   var contentString = 'This is my first popup';

			// create a default info window pop-up (supplied by Google)
			var infowindow = new google.maps.InfoWindow({content: contentString});

			// add a listener onto the point so that it responds to a click event
			google.maps.event.addListener(beachMarker, 'click', function() {infowindow.open(map,beachMarker);});

			// add a listener so that when the user clicks on the map the
			// clicked point is added to the text boxes
			// document.getElementById allows you to set values for each text box using its ID
			google.maps.event.addListener(map, 'click', function(point) {
					document.getElementById("latitude").value = point.latLng.lat();
					document.getElementById("longitude").value = point.latLng.lng();});
		} // end of the initialize function

		// call the initialize method to create the map
		// this listener is called when the window is first loaded
		google.maps.event.addDomListener(window, 'load', initialize);
    </script>
	
	
  </head>
  <body>
    <div id="map_canvas" style="width:70%;float:left"></div>  <!--Contains the map-->
    <div id="lat_lng" style="width:30%;float:right">
	<form action="appProcessData.php" method="post">
		Point Name: <input type="text" name="name" id="name" /><br/>
		Question: <input type="text" name="question" id="question" /><br />
		Possible Answer 1: <input type="text" name="answer1" id="answer1" /><br />
		Possible Answer 2: <input type="text" name="answer2" id="answer2" /><br />
		Possible Answer 3: <input type="text" name="answer3" id="answer3" /><br />
		Possible Answer 4: <input type="text" name="answer4" id="answer4" /><br />
		Correct Answer:		
							<input type="radio" name="correct" value="pa1" > Answer 1<br>
							<input type="radio" name="correct" value="pa2" > Answer 2<br>
							<input type="radio" name="correct" value="pa3" > Answer 3<br>
							<input type="radio" name="correct" value="pa4" > Answer 4<br>
							
		Latitude: <input type="text" name="latitude" id="latitude" /><br />
		Longitude: <input type="text" name="longitude" id="longitude" />

		<input type="submit" value="Submit" />
	</form>
    </div><!-- end of the lat-lng div -->
  </body>
</html>
