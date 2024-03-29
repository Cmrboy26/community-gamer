import React from 'react';
import { API_URL, LOGIN_URL } from '../App';

function Home() {
    const handleLogin = async () => {


        let headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');

        //headers.append("Access-Control-Allow-Origin", "*");
        //headers.append("Access-Control-Allow-Credentials", 'true');

        const response = await fetch(LOGIN_URL + "login", {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                username: 'user',
                password: 'password'
            }),
        });

        if (response.ok) {
            // Login successful, you can now access the GET "/api/postblog" endpoint
            console.log('Login successful');
        } else {
            // Login failed
            console.log('Login failed');
        }
    };

    return (
        <>
            <h1>Home Page</h1>
            <p>Welcome to the home page!</p>
            <p>
                <button onClick={handleLogin}>Login</button>
            </p>
        </>
    );
}

// TODO: try using cookies to make your own login system



export default Home;