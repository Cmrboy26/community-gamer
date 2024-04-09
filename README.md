# community-gamer
A simple, full-stack website for publishing custom game blogs written with Spring Boot in the backend and React in the frontend.

## Goals 
In this project, I wanted to achieve the following goals
[x] Sharpen my CSS, HTML, and JS skills in preparation for a Start College Now class in Web Development
[x] Learn basic SQL to prepare for Start College Now class
[x] Intuitively understand how front and backend interface with each other through REST
[x] Expand my Java framework knowledge with Spring Boot
[x] Update myself on modern security practices (human verification with reCAPTCHA, password encryption with bCrypt)
[x] Understand how cookies function in browsers

## Installation and Usage
1. Download the repository.
2. Create a folder inside the repository called ".config", and inside, create these files with (contents specified in quotes).
- database.csv "sqldatabaseurl:port,username,password,databasename" (SEE 4 for details about creating the SQL database)
- jwt_secret.csv "your-generated-jwt-private-key-here"
- recaptcha_secret.csv "your-private-recaptcha-key"
3. Inside of frontend/reactfirst/public, create a file called "recaptcha_public.txt" and put your public reCAPTCHA key inside of it.
4. Create an SQL server with two tables and plug the credentials into database.csv:
- "user" with columns (id INT(10) UNSIGNED, userid INT(10) UNSIGNED, date DATETIME, json TEXT)
- "blog" with columns (id INT(10) UNSIGNED, username TEXT, email TEXT, password TEXT)
5. In /frontend/reactfirst, run `npm install` and `npm start` to run.
6. In /backend, run the gradle project.
7. Connect to the website at "localhost:3000"

## Tests
Tests will be included in the future.
