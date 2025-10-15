// book.js - handles table booking logic
/*
const branches = [
  {
    id: 1,
    name: 'Downtown Branch',
    img: 'assets/images/branch1.png',
    slots: ['12:00 PM', '1:30 PM', '3:00 PM', '7:00 PM', '9:00 PM']
  },
  {
    id: 2,
    name: 'Lakeside Branch',
    img: 'assets/images/branch2.png',
    slots: ['12:30 PM', '2:00 PM', '4:00 PM', '8:00 PM']
  }
];*/
let branches = [];
let selectedBranch = null;
let selectedSlot = null;

async function fetchBranches() {
  try {
    const response = await fetch('http://localhost:8080/api/branches');
    branches = await response.json();
    renderBranches();
  } catch (error) {
    console.error("Failed to fetch branches:", error);
  }
}

function renderBranches() {
  const branchDiv = document.getElementById('branches');
  branchDiv.innerHTML = '';
  branches.forEach(branch => {
    const card = document.createElement('div');
    card.className = 'branch-card';
    card.innerHTML = `<img src="${branch.imageUrl}" alt="${branch.name}"><div>${branch.name}</div>`;
    card.onclick = () => selectBranch(branch.id);
    if (selectedBranch && selectedBranch.id === branch.id) card.classList.add('selected');
    branchDiv.appendChild(card);
  });
}

function selectBranch(id) {
  selectedBranch = branches.find(b => b.id === id);
  selectedSlot = null;
  renderBranches();
  renderSlots();
  document.getElementById('booking-form').innerHTML = '';
  document.getElementById('booking-confirmation').classList.remove('active');
}

// Replace with real available slots if backend provides them (otherwise use static for example)
function renderSlots() {
  const slotDiv = document.getElementById('slots');
  slotDiv.innerHTML = '';
  if (!selectedBranch) return;
  // You may need another API endpoint to fetch available slots per branch in a real setup
  const staticSlots = ["12:00 PM", "1:30 PM", "3:00 PM", "7:00 PM", "9:00 PM"];
  staticSlots.forEach(slot => {
    const btn = document.createElement('button');
    btn.className = 'slot-btn';
    btn.textContent = slot;
    // Assume enabled, or disable if required based on backend data
    if (selectedSlot === slot) btn.classList.add('selected');
    btn.onclick = () => selectSlot(slot);
    slotDiv.appendChild(btn);
  });
}

function selectSlot(slot) {
  selectedSlot = slot;
  renderSlots();
  renderBookingForm();
}

function renderBookingForm() {
  const formDiv = document.getElementById('booking-form');
  formDiv.classList.add('active');
  formDiv.innerHTML = `
    <input type="text" id="cust-name" placeholder="Your Name" required>
    <input type="tel" id="cust-phone" placeholder="Phone Number" required>
    <button onclick="bookTable()">Book Now</button>
  `;
}

// Replace this to POST booking details to backend!
async function bookTable() {
  const name = document.getElementById('cust-name').value;
  const phone = document.getElementById('cust-phone').value;
  if (!name || !phone) return alert('Please enter your name and phone number.');

  const payload = {
    branchId: selectedBranch.id,
    timeslot: selectedSlot,
    name: name,
    phone: phone
  };

  const response = await fetch('http://localhost:8080/api/book', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(payload)
  });
  const result = await response.json();

  document.getElementById('booking-form').classList.remove('active');
  const confDiv = document.getElementById('booking-confirmation');
  confDiv.textContent = result.message;
  confDiv.classList.add('active');
}

document.addEventListener('DOMContentLoaded', function() {
  if (document.getElementById('branches')) {
    fetchBranches();
  }
});
