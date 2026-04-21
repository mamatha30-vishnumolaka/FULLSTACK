const mysql = require("mysql2");

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root@309",
    database: "System_login"
});

db.connect((err) => {
    if (err) {
        console.log("Database connection failed");
        console.log(err);   // ✅ This will show real error
    } else {
        console.log("Connected to System_login database");
    }
});

module.exports = db;