const express = require("express");
const cors = require("cors");
const path = require("path");
const db = require("./db");   // ✅ only once

const app = express();

app.use(cors());
app.use(express.json());

// ✅ Serve frontend files
app.use(express.static(path.join(__dirname, "..")));

// ✅ Default route
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "..", "index.html"));
});

// =====================================
// 📦 GET PRODUCTS FROM DB
// =====================================
app.get("/api/products", (req, res) => {
  db.query("SELECT * FROM products", (err, result) => {
    if (err) {
      console.log(err);
      return res.json(err);
    }
    res.json(result);
  });
});

// =====================================
// 🛒 ADD TO CART (DB)
// =====================================
app.post("/api/cart", (req, res) => {

  const { product_id, qty } = req.body;

  db.query(
    "INSERT INTO cart (product_id, qty) VALUES (?, ?)",
    [product_id, qty],
    (err, result) => {

      if (err) {
        console.log("DB Error:", err);
        return res.json(err);
      }

      res.json({ message: "Cart saved in MySQL ✅" });
    }
  );

});

// =====================================
// 🛒 GET CART FROM DB
// =====================================
app.get("/api/cart", (req, res) => {
  db.query("SELECT * FROM cart", (err, result) => {
    if (err) return res.json(err);
    res.json(result);
  });
});

// =====================================
// 🧾 SAVE ORDER
// =====================================
app.post("/api/order", (req, res) => {

  const { order_id, address, payment } = req.body;

  db.query(
    "INSERT INTO orders (order_id, address, payment) VALUES (?, ?, ?)",
    [order_id, address, payment],
    (err, result) => {

      if (err) {
        console.log(err);
        return res.json(err);
      }

      res.json({ message: "Order saved ✅" });
    }
  );

});

// =====================================
app.listen(3000, () => {
  console.log("Server running at http://localhost:3000");
});