import { Routes, Route, Link } from 'react-router-dom';
import './App.css';

import Home from './pages/Home';
import Search from './pages/Search';
import Login from './pages/Login';
import Register from './pages/Register';
import About from './pages/About';
import Blog from './pages/Blog';
import CreatePost from './pages/CreatePost';
import { RequireAuth } from "react-auth-kit";
import Cookies from 'js-cookie';
import { config } from './Config';

//const LOGIN_URL = "http://localhost:8080/";
const API_URL = config.api_url + "api/";

const Main = () => {
  return (
    <Routes>
      <Route exact path="/home" element={<Home />} />
      <Route exact path="/search" element={<Search />} />
      <Route exact path="/login" element={<Login />} />
      <Route exact path="/logout" element={<Logout />} />
      <Route exact path="/register" element={<Register />} />
      <Route exact path="/post" element={<CreatePost />} />
      <Route exact path="/about" element={<About />} />
      <Route exact path="/blog" element={<Blog />} />
      <Route path="" element={<Home />} />
    </Routes>
  );
}

function App() {
  return (
    <div className="App">
      <Navbar />
      <Main />
    </div>
  );
}

function Navbar() {
  return (
    <>
      <div className="navbar">
        <div className="left">
          <img src='logo.png' width={40} height={40} alt='logo'></img>
          <Link to="/home">Home</Link>
          <LoginLink />
          <Link to="/about">About</Link>
        </div>
        <div className="right">
          <PostLink />
          <SearchBar />
        </div>
      </div>
    </>
  );
}

function PostLink() {
  if (isLoggedIn()) {
    return (
      <Link to="/post">Post</Link>
    );
  }
}

function LoginLink() {
  if (isLoggedIn()) {
    return (
      <Link to="/logout">Logout</Link>
    );
  }
  return (
    <Link to="/login">Login</Link>
  );
}

function logoutToHome() {
  if (isLoggedIn()) {
    logout();
    window.location.pathname = "/home";
  }
}

function Logout() {
  logout();
  return <h2>Logging out...</h2>;
}

function logout() {
  fetch(API_URL + "logout", {
    method: "DELETE",
    credentials: "include",
  }).then(() => {
    Cookies.remove("_auth");
    Cookies.remove("_auth_type");
    Cookies.remove("_auth_state");
    window.location.pathname = "/home";
  });
}

function logInCheck() {
  // TODO: Ping the server to check if the user is logged in (aka token is stored in the database)
}

function handleSearch(event) {
  // Go to the search page ("/search")
  event.preventDefault();
  const search = "/search" + "?q=" + document.getElementById("search").value;
  window.open(search, "_self");
}

function SearchBar() {
  return (
    <form onSubmit={handleSearch}>
      <input type="text" id="search" placeholder="Search..." />
    </form>
  );
}

function isLoggedIn() {
  return Cookies.get("_auth") !== undefined;
}

/*function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn Reactttt!
        </a>
      </header>
    </div>
  );
}*/

export { isLoggedIn, logout, logoutToHome };
export default App;
export { API_URL }

