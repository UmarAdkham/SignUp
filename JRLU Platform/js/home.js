document.addEventListener("DOMContentLoaded", function(event) {

  var goDown = document.getElementById('go-down-link');
  goDown.onclick = scroll;

  function scroll(e) {
      e.preventDefault();
      animateScroll(document.getElementById('service-section').getBoundingClientRect().top);
  }

  function animateScroll(targetHeight) {
      var initialPosition = window.scrollY;
      var SCROLL_DURATION = 40;
      var step_x = Math.PI / SCROLL_DURATION;
      var step_count = 0;
      requestAnimationFrame(step);
      function step() {
          if (step_count < SCROLL_DURATION) {
              requestAnimationFrame(step);
              step_count++;
              // use this formula to achive Bezier curve --> y = y0 + 1/4 * h (1 - cos(sx * x) )^2
              window.scrollTo(0, initialPosition + targetHeight * 0.25 * Math.pow((1 - Math.cos(step_x * step_count)), 2));
          }
      }
  }

  // get the element to animate
  var element = document.getElementsByClassName('services')[0];
  var elementHeight = element.clientHeight;

  // listen for scroll event and call animate function
  document.addEventListener('scroll', animate);

  // check if element is in view
  function inView() {
    // get window height
    var windowHeight = window.innerHeight;
    // get number of pixels that the document is scrolled
    var scrollY = window.scrollY || window.pageYOffset;

    // get current scroll position (distance from the top of the page to the bottom of the current viewport)
    var scrollPosition = scrollY + windowHeight;
    // get element position (distance from the top of the page to the bottom of the element)
    var elementPosition = element.getBoundingClientRect().top + scrollY + elementHeight;

    // is scroll position greater than element position? (is element in view?)

    if (scrollPosition > elementPosition) {
      return true;
    }

    return false;
  }

  var firstCol = document.getElementsByClassName('first-col')[0]
  var secondCol = document.getElementsByClassName('second-col')[0]
  var thirdCol = document.getElementsByClassName('third-col')[0]

  // animate element when it is in view
  function animate() {
    // is element in view?
    if (inView()) {
        // element is in view, add class to elements
        firstCol.classList.remove('hidden');
        firstCol.classList.add('animated', 'fadeInLeft');
        secondCol.classList.remove('hidden');
        secondCol.classList.add('animated', 'fadeInUp');
        thirdCol.classList.remove('hidden');
        thirdCol.classList.add('animated', 'fadeInRight');
    }
  }


});
