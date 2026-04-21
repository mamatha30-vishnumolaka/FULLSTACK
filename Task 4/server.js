const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "charan",
  database: "task1_db"
});

db.connect(err => {
  if (err) console.log(err);
  else console.log("Database connected");
});


app.get("/orders", (req, res) => {
  const query = `
    SELECT c.name, p.product_name, o.quantity,
           p.price, (o.quantity * p.price) AS total_amount,
           o.order_date
    FROM Orders o
    JOIN Customers c ON o.customer_id = c.customer_id
    JOIN Products p ON o.product_id = p.product_id
    ORDER BY o.order_date DESC
  `;

  db.query(query, (err, result) => {
    if (err) res.status(500).send(err);
    else res.json(result);
  });
});

app.listen(5000, () => console.log("Server running on port 5000"));