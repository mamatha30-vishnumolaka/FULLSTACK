const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");
const path = require("path");

const app = express();

app.use(cors());
app.use(express.json());
app.use(express.static(__dirname)); 

const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "charan",  
  database: "task3_db"
});

db.connect((err) => {
  if (err) {
    console.log("Database connection failed:", err);
  } else {
    console.log("Database connected");
  }
});

app.post("/login", (req, res) => {
  const { username, password } = req.body;

  const sql = "SELECT * FROM users WHERE username=? AND password=?";
  
  db.query(sql, [username, password], (err, result) => {
    if (err) {
      return res.json({ success: false, message: "Server error" });
    }

    if (result.length > 0) {
      res.json({ success: true, message: "Login Successful" });
    } else {
      res.json({ success: false, message: "Invalid Credentials" });
    }
  });
});

app.listen(5000, () => {
  console.log("Server running on http://localhost:5000");
});