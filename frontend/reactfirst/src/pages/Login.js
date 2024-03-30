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
      setError(err.message);
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

export default Login;