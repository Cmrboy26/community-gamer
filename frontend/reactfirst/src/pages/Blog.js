import { json, useLocation } from 'react-router-dom';
import { API_URL } from '../App';
import { useEffect, useState } from 'react';

function Blog() {

    const blogID = new URLSearchParams(useLocation().search).get("id");

    const [error, setError] = useState();
    const [blogData, setBlogData] = useState([]);

    useEffect(() => {
        setError("Retrieving blog \"" + blogID + "\"...");

        fetch(API_URL + "blog/" + blogID)
            .then(response => response.json())
            .then(data => {
                setBlogData(data);
                setError("");
            })
            .catch(error => {
                setError("There was an error retrieving the blog: " + error.message);
                setBlogData(errorBlog(error));
                console.error(error);
            });
    }, [blogID]);

    return (
      <div>
        <p>{error}</p>
        <BlogPage blogData={blogData} />
      </div>
    );
}

function errorBlog(error) {
    console.log(error);
    const data = {
        id: -2,
        title: "THERE WAS AN ERROR!",
        category: "Error Report",
        tags: ["Uh oh!", "That's not good!", "Error!"],
        sections: [
            {
                type: "h1",
                data: "What went wrong?"
            },
            {
                type: "h2",
                data: "Here's the rundown:"
            },
            {
                type: "text",
                data: "The only thing we got for an error was this: \""+error.messag+"\"."
            },
            {
                type: "text",
                data: "This was apperently occured on line "+error.lineNumber+" in file "+error.fileName+"."
            }
        ]
    }
    return data;
}

function BlogPage(jsonBlogData) {

    if (jsonBlogData.blogData === undefined || jsonBlogData.blogData.length === 0) {
        return <p>Blog data is not present or loading.</p>;
    }

    try {
        console.log(jsonBlogData);
        console.log(jsonBlogData.blogData);

        let blogData = jsonBlogData.blogData;

        if (blogData.id === undefined || blogData.id === null || blogData.id === -1) {
            blogData = errorBlog(new Error("Blog data could not be loaded."));
        }

        return (
            <div className="blogContent">
                <h1 className='large'>{blogData.title}</h1>
                <h2 className='large'>{blogData.category}</h2>
                <Tags tags={blogData.tags} />
                <hr></hr>
                {blogData.sections.map(content => (
                    <ContentSection key={content.data+content.type} type={content.type} data={content.data} />
                ))}
            </div>
        );
    } catch (error) {
        console.error(error);
        return <p>There was an error displaying the blog: {error.message}</p>;
    }
}

function Tags({ tags }) {
    if (tags === undefined) {
        return <></>;
    }
    return <p>{tags.join("  -  ")}</p>
}

function ContentSection({ type, data }) {
    if (type === "h1") {
        return <h2>{data}</h2>;
    }
    if (type === "h2") {
        return <h3>{data}</h3>;
    }
    if (type === "text") {
        return <p>{data}</p>;
    }
    return <></>;
}

export default Blog;