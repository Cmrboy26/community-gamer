import { useLocation } from 'react-router-dom';
import { API_URL } from '../App';
import { useEffect, useState } from 'react';

function Search() {

    const query = new URLSearchParams(useLocation().search).get("q");

    const [blogs, setBlogs] = useState([]);

    useEffect(() => {
        fetch(API_URL + "blogs")
            .then(response => response.json())
            .then(data => {
                setBlogs(data);
            })
            .catch(error => {
                console.error(error)
            });
    }, []);

    if (query === null) {
        return <h1>Search for something...</h1>;
    }

    const length = FilterSearchResults({ blogs, query }).length;
    console.log(blogs);

    return (
        <>
            <h1>Search results for "{query}"</h1>
            <SearchResultAmount amount={length} />
            {FilterSearchResults({ blogs, query }).map(blog => (<BlogSection key={blog.id} blog={blog} />))}
        </>
    );
}

function FilterSearchResults({ blogs, query }) {
    return blogs.filter(blog => blog.title.includes(query));
}

function SearchResultAmount({ amount }) {
    const multipleSuffix = amount === 1 ? "" : "s";
    return <p>{amount} result{multipleSuffix} found.</p>;
}

function BlogSection({ blog }) {
    return (
        <div className="blogsection">
            <h2>{blog.title}</h2>
            <button>Read</button>
        </div>
    );
}

export default Search;