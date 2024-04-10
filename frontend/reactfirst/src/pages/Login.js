import { useState } from "react";
import { API_URL, logout, logoutToHome } from "../App";
import { LOGIN_URL } from "../App";
//import { signIn } from "react-auth-kit";
//import { signIn } from "react-auth-kit/hooks/useSignIn";
import useSignIn from 'react-auth-kit/hooks/useSignIn';
import { isLoggedIn } from "../App";

function registerAccount() {
  window.location.pathname = "/register";
}

function Login() {
  const [error, setError] = useState("");
  const signIn = useSignIn();

  const onSubmit = async (event, email, password) => {
    event.preventDefault();
    console.log("Logging in...");
    setError("");

    if (!email || !password) {
      setError("Email and password must not be empty.");
      return;
    }

    const emailReg = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/;

    if (!emailReg.test(email)) {
      setError("Invalid email format.");
      return;
    }

    try {
      const response = await fetch(API_URL + "login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email,
          password: password,
        }),
      });

      const status = response.status;
      const json = await response.json();

      if (status !== 200) {
        throw new Error(json.message);
      }
      
      signIn({
        auth: {
          token: json.token,
          type: 'Bearer'
        },
        userState: {
          email: email
        }
      });
      window.location.pathname = "/home";
    } catch (err) {
      setError("Error: " + err.message);
      console.error(err);
    }
  }

  if (isLoggedIn()) {
    return (
      <div className="App">
        <h1>You are already logged in!</h1>
        <br></br>
        <button onClick={logoutToHome}>
          Logout
        </button>
      </div>
    );
  }

  return (
    <div className="App">
      <h1>Login</h1>
      <form onSubmit={
        (event) => {
          onSubmit(event, event.target.email.value, event.target.password.value);
        }
      }>
        <label>Email: </label>
        <input type="email" name="email" />
        <br></br>
        <label>Password: </label>
        <input type="password" name="password" />
        <br></br>
        <br></br>
        <button type="submit" id="login">Login</button>
      </form>
      <form onSubmit= {
        (event) => {
          event.preventDefault();
          registerAccount();
        }
      }>
        <button type="submit" id="register">Register</button>
      </form>
      <p>{error}</p>
    </div>
  );
}

function login() {
  
}

export default Login;