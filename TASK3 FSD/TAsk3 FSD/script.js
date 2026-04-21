async function login() {
    let username = document.getElementById("username").value.trim();
    let password = document.getElementById("password").value.trim();

    let userError = document.getElementById("userError");
    let passError = document.getElementById("passError");
    let message = document.getElementById("message");

    userError.innerText = "";
    passError.innerText = "";
    message.innerText = "";

    let valid = true;

    if (username === "") {
        userError.innerText = "Username is required";
        valid = false;
    }

    if (password === "") {
        passError.innerText = "Password is required";
        valid = false;
    }

    if (!valid) return;

    const response = await fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    const data = await response.json();

    if (data.success) {
        message.style.color = "green";
        message.innerText = data.message;
    } else {
        message.style.color = "red";
        message.innerText = data.message;
    }
}