# Location-based-quiz

This is a mobile app developed for Android devices and works as follows: 
once the user launches the app, the user’s movement is tracked so that when the user approaches a given location
at a predefined radius the app prompts the user to answer a multiple-choice question about that location. 

The questions are stored in a table in a database on a server and are accessed by the app when launched.
After answering the question, the app pops up a message to let the user know the correct answer.
The user’s answer and mobile phone ID (IMEI number) are sent as an HTTP Request to the web server and then stored in a database.

The web part of this app is about the process of storing and accessing the questions on a server as well as storing the answer
of the user on the database. These questions are generated on a Google Maps embedded website and works as follows:
the user can fetch the latitude/longitude coordinates of a point by clicking on the map and generate a question about this point.
To generate the question, the user must fill in 6 textboxes (point name, question, and 4 possible answers) and choose which one 
by selecting from a group of radio buttons. By clicking on a “submit” button this information is then stored in relational database 
on the server. The final part of the web application is about uploading the information from the mobile app to the database. 

For the purpose of testing the application 10 points were created. These are located in and around the UCL campus in Bloomsbury. 

The mobile app was built in Android Studio Developer with the use of Java programming language. 
The part of the app which was built in the web was developed using PHP, Javascript and HTML scripts and makes use of the 3-Tier 
Internet Architecture.
