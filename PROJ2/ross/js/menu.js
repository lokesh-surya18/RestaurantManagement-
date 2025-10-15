// menu.js - handles menu display and cart logic
const menuData = [
  {
    id: 1,
    name: 'Classic Margherita Pizza',
    desc: 'Fresh mozzarella, basil, and tomato sauce on a crispy crust.',
    price: 399,
    rating: 4.8,
    img: 'assets/images/pizza.png'
  },
  {
    id: 2,
    name: 'Paneer Tikka Sizzler',
    desc: 'Grilled paneer cubes with veggies and spicy marinade.',
    price: 349,
    rating: 4.6,
    img: 'assets/images/paneer.png'
  },
  {
    id: 3,
    name: 'Chocolate Lava Cake',
    desc: 'Warm chocolate cake with gooey molten center.',
    price: 199,
    rating: 4.9,
    img: 'assets/images/lava-cake.png'
  }
];
 // global menu data
let cart = [];



function addToCart(id) {
  console.log('Add to cart clicked, id:', id);
  const item = menuData.find(i => i.id === id); // use global menuData correctly
  if (item) {
    cart.push(item);
    console.log('Item added:', item);
    updateCartCount();
  } else {
    console.log('Item not found for id:', id);
  }
}

function updateCartCount() {
  document.getElementById('cart-count').textContent = cart.length;
}

document.addEventListener('DOMContentLoaded', function() {
  if (document.getElementById('menu-items')) {
    loadMenu(); // load menu data once on page load
    document.getElementById('cart-icon').addEventListener('click', showCartSummary);
  }
});

function showCartSummary() {
  const summaryDiv = document.getElementById('cart-summary');
  summaryDiv.classList.add('active');
  let html = '<h3>Order Summary</h3><ul>';
  let total = 0;
  cart.forEach(item => {
    html += `<li>${item.name} <span>₹${item.price}</span></li>`;
    total += item.price;
  });
  html += '</ul>';
  html += `<div class="total">Total: ₹${total}</div>`;
  html += '<button class="confirm-btn" onclick="confirmOrder()">Confirm Order</button>';
  summaryDiv.innerHTML = html;
}

function confirmOrder() {
  const summaryDiv = document.getElementById('cart-summary');
  summaryDiv.innerHTML = '<h3>Thank you! Your order is confirmed.</h3>';
  cart = [];
  updateCartCount();
  setTimeout(() => summaryDiv.classList.remove('active'), 2000);
}

async function deleteMenuItem(id) {
  const response = await fetch(`http://localhost:8080/api/menu/${id}`, {
    method: "DELETE"
  });
  if (response.ok) {
    alert('Item deleted');
    loadMenu(); // Refresh menu list after deletion
  } else {
    alert('Failed to delete item');
  }
}


