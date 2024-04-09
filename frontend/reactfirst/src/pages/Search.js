import { useLocation } from 'react-router-dom';
import { API_URL } from '../App';
import { useEffect, useState } from 'react';

function Search() {

    let query = new URLSearchParams(useLocation().search).get("q");
    if (query.length < 1) {
        query = " ";
    }

    const [blogs, setBlogs] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        fetch(API_URL + "blogs", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: query
        })
            .then(response => response.json())
            .then(data => {
                setBlogs(data);
            })
            .catch(error => {
                console.error(error)
                setError("Error: " + error.message);
            });
    }, []);

    if (query === null) {
        return <h1>Search for something...</h1>;
    }
    //const blogList = FilterSearchResults({ blogs, query });
    const blogList = blogs;

    const length = blogList.length;
    console.log(blogs);

    return (
        <>
            <h1>Search results for "{query}"</h1>
            <p>{error}</p>
            <SearchResultAmount amount={length} />
            {blogList.map(blog => (<BlogSection key={blog.id} blog={blog} />))}
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
    const blogid = blog.id;
    console.log(blog);
    var date = new Date(blog.postDate);
    var dateString = date.toDateString();
    return (
        <div className="blogsection">
            <h2>{blog.title}</h2>
            <p>Posted {dateString}</p>
            <button onClick={() => clickBlog(blogid)}>Read</button>
        </div>
    );
}

function clickBlog(id) {
    const search = '/blog?id=' + id;
    window.open(search, "_self");
}

export default Search;