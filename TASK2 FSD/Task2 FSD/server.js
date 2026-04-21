const express = require("express");
const path = require("path");
const db = require("./db");   // database connection

const app = express();
const PORT = 3000;

// Middleware
app.use(express.static(__dirname));
app.use(express.urlencoded({ extended: true }));
app.use(express.json());

// Home route
app.get("/", (req, res) => {
    res.sendFile(path.join(__dirname, "index.html"));
});

// Login route
app.post("/login", (req, res) => {
    const { username, password } = req.body;

    const sql = "SELECT * FROM Users WHERE username=? AND password=?";

    db.query(sql, [username, password], (err, result) => {
        if (err) {
            console.log(err);
            res.send("Database Error ❌");
        } else if (result.length > 0) {
            res.send("Login Successful ✅");
        } else {
            res.send("Invalid Username or Password ❌");
        }
    });
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});