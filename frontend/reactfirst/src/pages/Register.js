import React, { useState } from 'react';
import ReCAPTCHA from "react-google-recaptcha";
import { API_URL } from "../App";

const recaptchaRef = React.createRef();

function Register() {

    const [error, setError] = useState("");

    const [capVal, setCapVal] = useState(null);

    const onSubmit = async (event, username, email, password, confirm) => {
        event.preventDefault();

        if (capVal === null) {
            setError("Please complete the captcha!");
            return;
        }

        // Check if any fields are empty
        if (username === "" || email === "" || password === "" || confirm === "") {
            setError("Please fill in all fields!");
            return;
        }

        // Check if username is greater than 3 characters
        if (username.length < 3) {
            setError("Username must be at least 3 characters!");
            return;
        }

        // See if the email is valid
        if (!(email.includes("@") && email.includes("."))) {
            setError("Invalid email!");
            return;
        }

        // Check if the passwords match
        if (password !== confirm) {
            setError("Passwords do not match!");
            return;
        }

        // See if password is strong enough
        if (password.length < 8) {
            setError("Password must be at least 8 characters!");
            return;
        }

        setError("Registering...");
        // Send a post request to the backend to register the user
        const response = await fetch(API_URL + "register", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password,
                captcha: capVal
            })
        });

        const data = await response.json();

        if (response.status !== 200) {
            setError(data.message);
            console.log(data.message);
            window.grecaptcha.reset();
        } else {
            setError("Registered successfully!");
            window.location.pathname = "/login";
        }
    };
    const clearError = () => {
        setError("");
    };
    const captcha = getReCAPTCHAKey();
    return (
        <div className="App">
            <h1>Register</h1>
            <form onSubmit={
                (event) => {
                    event.preventDefault();
                    const username = event.target.username.value;
                    const email = event.target.email.value;
                    const password = event.target.password.value;
                    const password2 = event.target.password2.value;
                    onSubmit(event, username, email, password, password2);
                }
            } autocomplete="off">
                <label>Username: </label>
                <input type="text" name="username" />
                <br></br>
                <label>Email: </label>
                <input type="email" name="email" />
                <br></br>
                <label>Password: </label>
                <input type="password" onChange={clearError} name="password" />
                <br></br>
                <label>Confirm Password: </label>
                <input type="password" onChange={clearError} name="password2" />
                <br></br>
                <ReCAPTCHA 
                    sitekey={captcha}
                    onChange={val => setCapVal(val)}
                    style={{ display: "inline-block" }}
                />
                <br></br>
                <br></br>
                <button type="submit">Register</button>
                <p>{error}</p>
            </form>
        </div>
    );
}

function getReCAPTCHAKey() {
    // TODO: get this to work
    return "6LeGkrMpAAAAAOqRrlrVQlIRZEbwQnxsoJqjG1iq";
    //return fetch(process.env.PUBLIC_URL+"/recaptcha_public.txt").then(response => response.text()).then(data => {return data;});
}

function registerAccount(email, password) {

}

export default Register