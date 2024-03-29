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
                console.log(data);
                setBlogs(data);
            })
            /*.catch(error => {
                console.error(error)
                //throw new Error("Error fetching blogs: "+window.location.hostname);
                throw error;
            })*/;
    }, []);

    if (query === null) {
        return <h1>Search for something...</h1>;
    }

    return (
        <>
            <h1>Search results for "{query}"</h1>
            {blogs/*.filter(blog => blog.title.includes(query))*/
                .map(blog => (<BlogSection key={blog.id} blog={blog} />
            ))}
        </>
    );
}

function BlogSection({ blog }) {
    return (
        <div>
            <h2>{blog.title}</h2>
            <p>{blog.body}</p>
        </div>
    );
}

export default Search;