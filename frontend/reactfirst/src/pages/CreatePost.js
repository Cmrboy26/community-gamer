import { isLoggedIn } from "../App";
import { useEffect, useState } from "react";
import { API_URL } from "../App";

function CreatePost() {
    
    const [sections, setSections] = useState([]);

    useEffect(() => {
        if (!isLoggedIn()) {
            window.location.pathname = "/login";
        } else {
            setSections([{ type: "h1" }]);
        }
    }, []);

    const handleDeleteSection = (index) => {
        setSections((prevSections) => {
            const updatedSections = [...prevSections];
            updatedSections.splice(index, 1);
            return updatedSections;
        });
    };

    const handleCreatePost = (e) => {
        e.preventDefault();
        const title = document.getElementById("title").value;
        const category = document.getElementById("category").value;
        const tags = document.getElementById("tags").value.split(",");
        const content = sections.map((section) => {
            const type = section.type;
            const data = document.getElementById(sections.indexOf(section) + type).value;
            return { type, data };
        });
        const blog = {
            title: title,
            category: category,
            tags: tags,
            sections: content
        }
        fetch(API_URL + "postblog", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(blog)
        }).then(() => {
            window.location.pathname = "/home";
        });
    };

    return (
        <div className="app">
            <h1>Create Post</h1>
            <p>NOTE: Currently, progress will not be saved. Draft your post elsewhere before posting!</p>
            <br></br>
            <form>
                <label>Title:</label>
                <input id="title" type="text" />
                <br></br>
                <label>Category:</label>
                <input id="category" type="text" />
                <br></br>
                <label>Comma Separated Tags:</label>
                <input id="tags" type="text" />
            </form>
            <br></br>
            {sections.map((section, index) => (
                <ContentSection
                    key={section.type}
                    index={index}
                    type={section.type}
                    onDelete={handleDeleteSection}
                />
            ))}
            <br></br>
            <form onSubmit={(e) => {
                e.preventDefault();
                const type = e.target.querySelector("select").value;
                setSections((prevSections) => {
                    return [...prevSections, { type: type }];
                });
            }}>
                <label>
                    Room type:
                    <select>
                        <option value="h1">Header</option>
                        <option value="h2">Subheader</option>
                        <option value="text">Normal Text</option>
                    </select>
                </label>
                <button type="submit">Add Section</button>
            </form>
            <br></br>
            <form onSubmit={handleCreatePost}>
                <button type="submit">Create Post</button>
            </form>
        </div>
    );
}

function ContentSection({ index, type, onDelete }) {
    const handleDelete = (e) => {
        e.preventDefault();
        onDelete(index);
    };

    return (
        <div>
            <form onSubmit={handleDelete}>
                <label>{type}</label>
                {
                    type === "h1" ?
                        <input id={index+type} type="text" />
                        : type === "h2" ?
                            <input id={index+type} type="text" />
                            : <textarea id={index+type} />
                }
                <button type="delete">Delete</button>
            </form>
        </div>
    );
}

export default CreatePost;