/**
 * Timeline Animations
 * -------------------
 * All items on the timeline are invisible by default (opacity-0).
 * Once an element is inside the viewport (half of the height of the element is enough),
 * classes are replaced on the element, which triggers a CSS animation.
 *
 * The animation simply makes the element visible (opacity-100) and adds a subtle upwards movement (translation).
 */

/**
 * animates elements in the viewport on page load
 */
ready(function () {
  // Add a short delay, because the images are loaded later, which causes element to be in the viewport
  // that would be not once the image is loaded.
  setTimeout(animateElementsInViewport, 250);
});

/**
 * animates elements in the viewport on scrolling
 */
document.addEventListener('scroll', animateElementsInViewport);

/**
 * loop through all elements with a data-replace attribute
 * animate an element if it's in the viewport (make it visible)
 * after the animation, the data-replace attribute is removed from the element
 * to eliminate future checks
 */
function animateElementsInViewport() {
  const replacers = queryAll('[data-replace]');

  replacers.forEach((replacer) => {
    if (isInViewport(replacer)) {
      animateElement(replacer);
    }
  });
}

/**
 * replaces the classes specified in the data-replace attribute
 * this triggers the animation of the element
 * removes the data-replace attribute as the animation is only once
 * and to eliminate future checks
 *
 * @param {DOMElement} element
 */
function animateElement(element) {
  let replaceClasses = JSON.parse(element.dataset.replace.replace(/'/g, '"'));
  Object.keys(replaceClasses).forEach(function (key) {
    element.classList.remove(key);
    element.classList.add(replaceClasses[key]);
  });
  delete element.dataset.replace;
}

/**
 * detect whether an element is in the viewport (visible) or not
 * the element does not have to be fully in the viewport,
 * it is enough to only have half of its height inside the viewport
 *
 * @param {DOMElement} element
 * @returns boolean
 */
function isInViewport(element) {
  const rect = element.getBoundingClientRect();
  return (
    rect.top >= 0 &&
    rect.left >= 0 &&
    rect.bottom <=
      (window.innerHeight || document.documentElement.clientHeight) + element.clientHeight / 2 &&
    rect.right <= (window.innerWidth || document.documentElement.clientWidth)
  );
}
