import { json, useLocation } from 'react-router-dom';
import { API_URL } from '../App';
import { useEffect, useState } from 'react';

function Blog() {

    const blogID = new URLSearchParams(useLocation().search).get("id");

    const [error, setError] = useState();
    const [blogData, setBlogData] = useState([]);

    useEffect(() => {
        setError("Retrieving blog \"" + blogID + "\"...");

        fetch(API_URL + "blogs/" + blogID)
            .then(response => response.json())
            .then(data => {
                setBlogData(data);
            })
            .catch(error => {
                console.error(error);
                setError("There was an error retrieving the blog: " + error.message);
            });
    }, [blogID]);

    return (
      <div>
        <p>{error}</p>
        <BlogPage blogData={blogData} />
      </div>
    );
}

function errorBlog() {
    const data = {
        title: "Blog Title",
        category: "Blog Category",
        tags: ["tag1", "tag2"],
        content: [
            {
                type: "h1",
                data: "Here is a <h1> title </h1>."
            },
            {
                type: "h2",
                data: "Here a <h2> subheader </h2>."
            },
            {
                type: "text",
                data: "Here is some more text."
            }
        ]
    }
    return { blogData: data };
}

function BlogPage(jsonBlogData) {

    if (jsonBlogData === undefined) {
        return <p>Blog data is not present.</p>;
    }

    try {
        return (
            <div className="blogContent">
                <h1>{jsonBlogData.blogData.title}</h1>
                <h2>{jsonBlogData.blogData.category}</h2>
                <p>Tags: {jsonBlogData.blogData.tags.join(", ")}</p>
                {jsonBlogData.blogData.content.map(content => (
                    <ContentSection key={content.id} type={content.type} data={content.data} />
                ))}
            </div>
        );
    } catch (error) {
        console.error(error);
        return <p>Error: {error.message}</p>;
    }
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


    /*
    Blog Data Json Example

    {
        "title": "blogTitleHere"
        "category": "blogCategoryHere",
        "tags": ["listTagsHere", "Guide", "CHEAT!", "Helpful"]
        "content": [
            {
                "type": "h1",
                "data": "Here is a <h1> title </h1>."
            },
            {
                "type": "h2",
                "data": "Here a <h2> subheader </h2>."
            },
            {
                "type": "image",
                "data": "imageURLHere"
                WILL BE IMPLEMENTED LATER
            },
            {
                "type": "text",
                "data": "Here is some more text."
            }
        ]
    }
    */

export default Blog;