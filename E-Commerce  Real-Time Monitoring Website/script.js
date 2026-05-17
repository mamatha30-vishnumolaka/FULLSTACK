const products = [
  { id: 1, name: "Stylish Bag", price: 1200, image: "images/bag.jpg", category: "Accessories" },
  { id: 2, name: "Wooden Bookshelf", price: 3500, image: "images/bookshelf.jpg", category: "Furniture" },
  { id: 3, name: "Smart Bracelet", price: 1800, image: "images/bracelet.jpg", category: "Electronics" },
  { id: 4, name: "Office Chair", price: 4200, image: "images/chair.jpg", category: "Furniture" },
  { id: 5, name: "Wireless Earbuds", price: 2200, image: "images/earbuds.jpg", category: "Electronics" },
  { id: 6, name: "Comfort Sofa", price: 8000, image: "images/sofa.jpg", category: "Furniture" },
  { id: 7, name: "Bluetooth Speaker", price: 1500, image: "images/speaker.jpg", category: "Electronics" },
  { id: 8, name: "Dining Table", price: 6000, image: "images/table.jpg", category: "Furniture" },
  { id: 9, name: "Leather Wallet", price: 700, image: "images/wallet.jpg", category: "Accessories" }
];

const container = document.getElementById("products");

// DISPLAY
function displayProducts(list) {
  container.innerHTML = "";

  list.forEach(p => {
    container.innerHTML += `
      <div class="card">
        <img src="${p.image}">
        <h3>${p.name}</h3>
        <p class="price">₹${p.price}</p>
        <button class="add-btn" onclick='addToCart(${JSON.stringify(p)})'>+</button>
      </div>
    `;
  });
}

displayProducts(products);

// SEARCH
document.querySelector(".search").addEventListener("input", e => {
  const value = e.target.value.toLowerCase();
  displayProducts(products.filter(p => p.name.toLowerCase().includes(value)));
});

// FILTER
document.querySelectorAll(".filters button").forEach(btn => {
  btn.addEventListener("click", () => {
    document.querySelectorAll(".filters button").forEach(b => b.classList.remove("active"));
    btn.classList.add("active");

    const category = btn.innerText;

    if (category === "All") {
      displayProducts(products);
    } else {
      displayProducts(products.filter(p => p.category === category));
    }
  });
});

// CART
function addToCart(product) {
  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  let existing = cart.find(p => p.id === product.id);

  if (existing) {
    existing.qty += 1;
  } else {
    cart.push({ ...product, qty: 1 });
  }

  localStorage.setItem("cart", JSON.stringify(cart));

  alert("Added to cart 🛒");
}
