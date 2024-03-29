import logo from './logo.svg';
import { Routes, Route, Link } from 'react-router-dom';
import './App.css';

import Home from './pages/Home';
import Search from './pages/Search';

const API_URL = window.location.hostname+"/api/";
//const API_URL = "https://jubilant-spork-7wq5rv4jjjr2xv4q-8080.app.github.dev/api/";
//const API_URL = "http://localhost:8080/api/";
//const API_URL = "https://jubilant-spork-7wq5rv4jjjr2xv4q-8080.app.github.dev/api/";

const Main = () => {
  return (
    <Routes>
      <Route exact path="/home" element={<Home />} />
      <Route exact path="/search" element={<Search />} />
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
          <Link to="/home">Home</Link>
          <Link to="/search">Search</Link>
        </div>
        <div className="right">
          <SearchBar />
        </div>
      </div>
    </>
  );
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

export default App;
export { API_URL }
