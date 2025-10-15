// main.js - shared scripts for navigation, animations, etc.
document.addEventListener('DOMContentLoaded', function() {
  // Fade in hero section
  const hero = document.querySelector('.hero-section');
  if (hero) {
    hero.classList.add('fade-in');
  }

  // Smooth scroll for nav links
  document.querySelectorAll('.navbar a').forEach(link => {
    link.addEventListener('click', function(e) {
      if (this.hash && document.querySelector(this.hash)) {
        e.preventDefault();
        document.querySelector(this.hash).scrollIntoView({ behavior: 'smooth' });
      }
    });
  });
});