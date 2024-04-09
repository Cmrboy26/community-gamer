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
    const data = {
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
        return (
            <div className="blogContent">
                <h1 className='large'>{jsonBlogData.blogData.title}</h1>
                <h2 className='large'>{jsonBlogData.blogData.category}</h2>
                <Tags tags={jsonBlogData.blogData.tags} />
                <hr></hr>
                {jsonBlogData.blogData.sections?.map(content => (
                    <ContentSection key={content.id} type={content.type} data={content.data} />
                ))}
            </div>
        );
    } catch (error) {
        console.error(error);
        return <p>Error: {error.message}</p>;
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
    /*if (type === "image") {
        return <img src={data} alt="Blog Image" />;
    }*/
    if (type === "text") {
        return <p>{data}</p>;
    }
    return <></>;
}

export default Blog;