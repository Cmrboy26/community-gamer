import React, { useState } from 'react';

function Register() {

    const [error, setError] = useState("");
    const onSubmit = async (event, username, email, password, confirm) => {
        event.preventDefault();

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
    };
    const clearError = () => {
        setError("");
    }


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
                <input ype="text" name="username" />
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
                <br></br>
                <button type="submit">Register</button>
                <p>{error}</p>
            </form>
        </div>
    );
}

function registerAccount(email, password) {

}

export default Register