const API = "http://localhost:5000";

async function loadProducts() {
  const res = await fetch(`${API}/products`);
  const data = await res.json();

  const container = document.getElementById("products");

  data.forEach(product => {
    const div = document.createElement("div");
    div.innerHTML = `
      <h3>${product.name}</h3>
      <p>${product.price}</p>
      <button onclick="addToCart(${product.id})">Add to Cart</button>
    `;
    container.appendChild(div);
  });
}

async function addToCart(id) {
  const res = await fetch(`${API}/products`);
  const products = await res.json();
  const item = products.find(p => p.id === id);

  await fetch(`${API}/cart`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(item)
  });

  alert("Added to cart");
}

loadProducts();