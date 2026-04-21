const mysql = require("mysql2");

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root@309",
    database: "payment_system"
});

db.connect((err) => {
    if (err) {
        console.log("DB Connection Failed");
    } else {
        console.log("Database Connected");
    }
});

module.exports = db;