# community-gamer
A simple, full-stack website for publishing custom game blogs written with Spring Boot in the backend and React in the frontend.

## Goals 
In this project, I wanted to achieve the following goals:
<br>
<br>[X] Sharpen my CSS, HTML, and JS skills in preparation for a Start College Now class in Web Development
<br>[X] Learn basic SQL to prepare for Start College Now class
<br>[X] Intuitively understand how front and backend interface with each other through REST
<br>[X] Expand my Java framework knowledge with Spring Boot
<br>[X] Update myself on modern security practices (human verification with reCAPTCHA, password encryption with bCrypt)
<br>[X] Understand how cookies function in browsers

## Installation and Usage
1. Download the repository.
2. Create a folder within the project: "/backend/app/.config". inside, create these files with (contents specified in quotes).
- database.csv "sqldatabaseurl:port,username,password,databasename" (SEE 4 for details about creating the SQL database)
- jwt_secret.csv "your-generated-jwt-private-key-here"
- recaptcha_secret.csv "your-private-recaptcha-key"
3. Inside "frontend/reactfirst/Config.js", put your public reCAPTCHA key inside of the corresponding value. In addition, configure any other values inside of the file.
4. Create an SQL server with two tables and plug the credentials into database.csv:
- "user" with columns (id INT(10) UNSIGNED, userid INT(10) UNSIGNED, date DATETIME, json TEXT)
- "blog" with columns (id INT(10) UNSIGNED, username TEXT, email TEXT, password TEXT)

<hr>

To run the backend, run these commands:<br>
`cd backend`, `./gradlew :App:run`<br>
To test the backend, run these commands:<br>
`cd backend`, `./gradlew :App:test`<br>

To run the frontend, run these commands:<br>
`cd frontend/reactfirst`, (for the first run) `npm install`, `npm start`<br>

## Tests
Tests will be included sometime soon.
