import { isLoggedIn } from "../App";

function CreatePost() {
    if (!isLoggedIn()) {
        window.location.pathname = "/login";
        return null;
    }
    return (
        <div className="app">
            <h1>Create Post</h1>
        </div>
    );
}

export default CreatePost;