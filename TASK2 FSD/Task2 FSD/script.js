function login() {

    let username = document.getElementById("username").value.trim();
    let password = document.getElementById("password").value.trim();

    let usernameError = document.getElementById("usernameError");
    let passwordError = document.getElementById("passwordError");
    let message = document.getElementById("message");

    // Clear previous messages
    usernameError.innerText = "";
    passwordError.innerText = "";
    message.innerText = "";

    let valid = true;

    // Validation
    if (username === "") {
        usernameError.innerText = "Username is required";
        valid = false;
    }

    if (password === "") {
        passwordError.innerText = "Password is required";
        valid = false;
    }

    if (!valid) return;

    // Send data to server
    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            message.style.color = "green";
            message.innerText = data.message;
        } else {
            message.style.color = "red";
            message.innerText = data.message;
        }
    })
    .catch(error => {
        message.style.color = "red";
        message.innerText = "Server error. Try again.";
        console.error("Error:", error);
    });
}