import { Component, OnInit } from '@angular/core';
declare var particlesJS: any;
@Component({
  selector: 'app-hero-section',
  templateUrl: './hero-section.component.html',
  styleUrls: ['./hero-section.component.css']
})

export class HeroSectionComponent implements OnInit {
  ngOnInit() {
    this.initializeParticles();  }

  initializeParticles() {
    particlesJS('particles-js', {
      particles: {
        number: { value: 100, density: { enable: true, value_area: 700 } },
        color: { value: '#FFA500' },
        shape: { type: 'circle', stroke: { width: 0, color: '#000000' }, polygon: { nb_sides: 5 } },
        opacity: { value: 0.5, random: false, anim: { enable: false, speed: 0.1, opacity_min: 0.1, sync: false } },
        size: { value: 3, random: true, anim: { enable: false, speed: 10, size_min: 0.1, sync: false } },
        line_linked: { enable: true, distance: 150, color: '#e69c9c', opacity: 0.4, width: 1 },
        move: {
          enable: true,
          speed: 2,
          direction: 'none',
          random: false,
          straight: false,
          out_mode: 'out',
          bounce: false,
          attract: { enable: false, rotateX: 600, rotateY: 1200 }
        }
      },
      interactivity: {
        detect_on: 'canvas',
        events: { onhover: { enable: true, mode: 'grab' }, resize: true },
        modes: {
          grab: { distance: 14, line_linked: { opacity: 1 } },
          bubble: { distance: 40, size: 40, duration: 2, opacity: 8, speed: 3 },
          repulse: { distance: 20, duration: 0.4 },
          push: { particles_nb: 20 },
          remove: { particles_nb: 2 }
        }
      },
      retina_detect: true
    });
  }
}