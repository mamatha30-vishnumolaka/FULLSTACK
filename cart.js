console.log("cart.js loaded ✅");

// ==============================
// LOAD CART
// ==============================
let cart = JSON.parse(localStorage.getItem("cart")) || [];

const container = document.getElementById("cart-items");
const subtotalEl = document.getElementById("subtotal");
const taxEl = document.getElementById("tax");
const totalEl = document.getElementById("total");

// ==============================
// DISPLAY CART
// ==============================
function displayCart() {

  console.log("Cart:", cart);

  if (!container) return;

  container.innerHTML = "";

  if (cart.length === 0) {
    container.innerHTML = "<h2>Your cart is empty 🛒</h2>";
    return;
  }

  let subtotal = 0;

  cart.forEach((item, index) => {

    let qty = item.qty || 1;
    subtotal += item.price * qty;

    container.innerHTML += `
      <div class="card">
        <img src="${item.image}" width="120">

        <div>
          <h3>${item.name}</h3>
          <p>₹${item.price}</p>

          <button onclick="decrease(${index})">-</button>
          ${qty}
          <button onclick="increase(${index})">+</button>

          <br><br>

          <button class="remove-btn" onclick="removeItem(${index})">
            Remove
          </button>
        </div>
      </div>
    `;
  });

  let tax = subtotal * 0.1;
  let total = subtotal + tax;

  subtotalEl.innerText = "₹" + subtotal;
  taxEl.innerText = "₹" + tax.toFixed(0);
  totalEl.innerText = "₹" + total.toFixed(0);
}

// ==============================
// FUNCTIONS
// ==============================
function increase(i) {
  cart[i].qty++;
  save();
}

function decrease(i) {
  if (cart[i].qty > 1) {
    cart[i].qty--;
  }
  save();
}

function removeItem(i) {
  cart.splice(i, 1);
  save();
}

function save() {
  localStorage.setItem("cart", JSON.stringify(cart));
  displayCart();
}

// ==============================
// CHECKOUT BUTTON
// ==============================
const checkoutBtn = document.querySelector(".checkout");

if (checkoutBtn) {
  checkoutBtn.addEventListener("click", () => {

    console.log("Checkout clicked 🔥");

    let user = localStorage.getItem("user");
    let address = document.getElementById("address").value;
    let payment = document.querySelector('input[name="payment"]:checked');

    if (!user) {
      alert("Please login first!");
      window.location.href = "login.html";
      return;
    }

    if (!address) {
      alert("Enter address!");
      return;
    }

    if (!payment) {
      alert("Select payment method!");
      return;
    }

    const orderId = "ORD" + Math.floor(Math.random() * 100000);

    localStorage.setItem("orderId", orderId);
    localStorage.removeItem("cart");

    alert("Order placed successfully 🎉");

    window.location.href = "success.html";
  });
}

// ==============================
// INITIAL LOAD
// ==============================
displayCart();