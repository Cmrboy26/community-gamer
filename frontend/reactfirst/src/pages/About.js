import React from 'react';
import { API_URL, LOGIN_URL } from '../App';

function About() {
    return (
        <>
            <div className="centerText inline"> 
                <img src='logo.png' width={150} height={150} alt='logo'></img>
                <h1>Community<br></br>Gamer</h1>
            </div>
            <h2>Welcome to the Community Gamer!</h2>
            <div className='safeLeft safeRight'>
                <h1>
                    What are we?
                </h1>
                <p className='large centerText'>
                    We are a blog/news hybrid that offers a 
                    platform for individuals to post news,
                    content, and guides to their favorite games.
                </p>
            </div>
            <div className='safeLeft safeRight'>
                <h1>
                    Who made us?
                </h1>
                <p className='large centerText'>
                    This website was created by Colten, an
                    aspiring software developer, who wanted to
                    create a platform for gamers to share their
                    experiences and helpful tips with others (and practice 
                    developing a fullstack application).
                </p>
            </div>
            <h1>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
                Spam
                <br></br>
            </h1>
        </>
    );
}

export default About;