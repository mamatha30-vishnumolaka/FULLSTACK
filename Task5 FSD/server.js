const express = require("express");
const mysql = require("mysql2");
const path = require("path");

const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());
app.use(express.static(__dirname));

// Database Connection
const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "root@309",   // 🔴 CHANGE THIS
    database: "payment_system"
});

db.connect((err) => {
    if (err) {
        console.log("Database Connection Failed");
    } else {
        console.log("Database Connected Successfully");
    }
});


// ===============================
// 💰 PAYMENT TRANSACTION API
// ===============================
app.post("/pay", (req, res) => {

    const { userId, merchantId, amount } = req.body;

    db.beginTransaction((err) => {

        if (err) {
            return res.send("Transaction Error");
        }

        // 1️⃣ Deduct from User
        db.query(
            "UPDATE users SET balance = balance - ? WHERE id = ? AND balance >= ?",
            [amount, userId, amount],
            (err, result) => {

                if (err || result.affectedRows === 0) {
                    return db.rollback(() => {
                        res.send("Payment Failed - Insufficient Balance");
                    });
                }

                // 2️⃣ Add to Merchant
                db.query(
                    "UPDATE merchants SET balance = balance + ? WHERE id = ?",
                    [amount, merchantId],
                    (err) => {

                        if (err) {
                            return db.rollback(() => {
                                res.send("Payment Failed");
                            });
                        }

                        // 3️⃣ Commit Transaction
                        db.commit((err) => {
                            if (err) {
                                return db.rollback(() => {
                                    res.send("Commit Failed");
                                });
                            }

                            res.send("Payment Successful ✅");
                        });
                    }
                );
            }
        );
    });
});


// ===============================
// 💳 GET BALANCES API
// ===============================
app.get("/balances", (req, res) => {

    const query = `
        SELECT 
            (SELECT balance FROM users WHERE id = 1) AS userBalance,
            (SELECT balance FROM merchants WHERE id = 1) AS merchantBalance
    `;

    db.query(query, (err, results) => {
        if (err) {
            return res.send("Error fetching balances");
        }

        res.json(results[0]);
    });
});


// ===============================
// 🚀 START SERVER
// ===============================
app.listen(PORT, () => {
    console.log("Server running on http://localhost:3000");
});