const testimonialsCarouselElement = document.querySelector('#testimonialsCarousel');

const testimonialsCarousel = new bootstrap.Carousel(testimonialsCarouselElement, {
    interval: 5000,
    ride: 'carousel',
    touch: true,
    cycle: true,
    wrap: true
});

const newsCarouselElement = document.querySelector('#newsCarousel');

const newsCarousel = new bootstrap.Carousel(newsCarouselElement, {
    interval: 15000,
    ride: 'carousel',
    touch: true,
    cycle: true,
    wrap: true
});