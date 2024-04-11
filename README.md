# community-gamer
A simple, full-stack application for publishing custom game blogs.

## Project Outline
The backend half of this application is programmed in Java and utilizes the Spring Boot framework. React was used to develop the frontend of the website. This project began development around March 24th of 2024,
and the minimal viable product was completed around April 10th, 2024. The project was being worked on concurrently with my rigorous high school classes.

## Personal Goals
Throughout developing this project, I wanted to achieve the following goals:
<br>
- Sharpen my CSS, HTML, and JS skills in preparation for a Start College Now class in Web Development
- Learn basic SQL to prepare for Start College Now class
- Intuitively understand how front and backend interface with each other through REST
- Expand my general Java framework knowledge with Spring Boot
- Update myself on modern security practices (human verification with reCAPTCHA, password encryption with bCrypt)
- Understand how cookies function in browsers

## Installation and Usage
1. Download the repository.
2. Create a folder within the project: "/backend/app/.config". inside, create these files with (contents specified in quotes).
- database.csv "sqldatabaseurl:port,username,password,databasename" (See step 4 for details about creating the SQL database)
- jwt_secret.csv "your-generated-jwt-private-key-here"
- recaptcha_secret.csv "your-private-recaptcha-key"
3. Inside "frontend/reactfirst/Config.js", put your public reCAPTCHA key inside of the corresponding value. In addition, configure any other values inside of the file.
4. Create an SQL server with two tables and plug the credentials into database.csv:
- "blogs" with columns (id INT(10) UNSIGNED, userid INT(10) UNSIGNED, date DATETIME, json TEXT)
- "users" with columns (id INT(10) UNSIGNED, username TEXT, email TEXT, password TEXT)

<hr>

(Note: if running on Linux, prepend ``./`` to the beginning of any `gradlew` command)

To run the backend, run these commands:<br>
`cd backend`, `gradlew :App:run`<br>

To run the frontend, run these commands:<br>
`cd frontend/reactfirst`, (for the first run) `npm install`, `npm start`<br>

## Tests

To test the backend, run these commands:<br>
`cd backend`, `gradlew :App:test`<br>

To test the frontend, run these commands:<br>
`cd frontend/reactfirst`, (for the first run) `npm install`, `npm test`
