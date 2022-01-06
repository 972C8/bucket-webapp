const errorStylingRules = {
  text: [
    {
      identifier: '$-error-parent',
      default: 'mt-1',
      error: 'mt-1 relative rounded-md shadow-sm',
    },
    {
      identifier: '$',
      default:
        'rounded-md sm:text-sm w-full shadow-sm focus:ring-blue-500 focus:border-blue-500 block border-slate-300',
      error:
        'rounded-md sm:text-sm w-full block pr-10 border-red-300 text-red-900 placeholder-red-300 focus:outline-none focus:ring-red-500 focus:border-red-500 ',
    },
    {
      identifier: '$-error-icon',
      toggle: 'hidden',
    },
    {
      identifier: '$-error',
      toggle: 'hidden',
    },
  ],
  submit: [
    {
      identifier: '$',
      default:
        'inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500',
      error:
        'inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-slate-600 bg-slate-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-500 cursor-default',
    },
  ],
};

let isValid;

function formIsValid() {
  return Object.values(isValid).every((value) => value);
}

function watchValues(watchers, submit) {
  isValid = {};

  watchers.forEach((watcher) => {
    watchValue(watcher, submit);
  });
}

function watchValue(watcher, submit) {
  validateAndFeedback(watcher, val(watcher.identifier), submit);

  query(watcher.identifier).addEventListener('keyup', (e) => {
    validateAndFeedback(watcher, e.target.value, submit);
  });
}

function validateAndFeedback(watcher, value, submit) {
  const { identifier, type, regexp, message, required } = watcher;

  isValid[identifier] = !required && value === '' ? true : validate(regexp, value);
  setErrorMessage(identifier, message);
  setAppearance(identifier, type, isValid[identifier]);
  setSubmit(submit);
}

function validate(regexp, value) {
  return regexp.test(value);
}

function setErrorMessage(identifier, message) {
  query(`${identifier}-error`).textContent = message;
}

function setAppearance(identifier, type, valid) {
  errorStylingRules[type].forEach((rule) => {
    let _identifier = rule.identifier.replace('$', identifier);
    if (rule.toggle) {
      query(_identifier).classList.toggle(rule.toggle, valid);
    } else {
      query(_identifier).className = valid ? rule.default : rule.error;
    }
  });
}

function setSubmit(identifier) {
  let valid = formIsValid();

  query(identifier).disabled = !valid;
  setAppearance(identifier, 'submit', valid);
}

const regExpPresets = {
  default: '^[a-z0-9\\s]{MIN,MAX}$',
  number: '[0-9]+',
  text: "^[a-z0-9äöüéèà.:,;'!?()=$\\s_-]{MIN,MAX}$",
  email:
    '^(([^<>()[\\]\\.,;:\\s@\\"]+(\\.[^<>()[\\]\\.,;:\\s@\\"]+)*)|(\\".+\\"))@(([^<>()[\\]\\.,;:\\s@\\"]+\\.)+[^<>()[\\]\\.,;:\\s@\\"]{2,})$',
  password: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$',
  labels: '^[\\sa-z0-9-]+$',
  url: 'https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)',
};

function getRegExp(key, min, max, flags = 'i') {
  if (!regExpPresets.hasOwnProperty(key)) key = 'default';

  return new RegExp(regExpPresets[key].replace('MIN', min).replace('MAX', max), flags);
}

function validResult(result) {
  return result.id && result.id > 0;
}

// General utility functions

/**
 * Transform a given ISO datetime string to DD. Januar YYYY format.
 *
 * @param {*} dateString
 * @returns
 */
function parseDate(dateString, short = false) {
  const date = new Date(dateString);
  const options = short
    ? { year: 'numeric', month: 'short', day: '2-digit' }
    : { year: 'numeric', month: 'long', day: '2-digit' };
  return date.toLocaleDateString('de-CH', options);
}

function param(key) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  const params = Object.fromEntries(urlSearchParams.entries());

  if (!params.hasOwnProperty(key)) throw new Error('param not set');

  return params[key];
}

function query(identifier) {
  return document.querySelector(identifier);
}

function queryAll(identifier) {
  return document.querySelectorAll(identifier);
}

function ready(callback) {
  // Use the handy event callback
  document.addEventListener('DOMContentLoaded', callback, { once: true });
}

function redirect(path) {
  window.location.replace(path);
}

function val(identifier, value) {
  if (value) {
    query(identifier).value = value;
  } else {
    return query(identifier).value;
  }
}

// Notification

function notify(title, message) {
  const notification = query('#notification');
  const id = randomHex(8);

  notification.content.querySelector('[data-notification]').dataset.id = id;
  notification.content.querySelector('[data-title]').textContent = title;
  notification.content.querySelector('[data-message]').textContent = message;
  notification.content
    .querySelector('[data-close]')
    .setAttribute('onclick', `closeNotification("${id}")`);

  const clone = document.importNode(notification.content, true);
  query('#notification-panel').appendChild(clone);
  animateIn(`[data-notification][data-id="${id}"]`);

  setTimeout(() => {
    closeNotification(id);
  }, 10000);
}

function closeNotification(id) {
  animateOut(`[data-notification][data-id="${id}"]`);
  setTimeout(() => {
    query(`[data-notification][data-id="${id}"]`).remove();
  }, 500);
}

function randomHex(length = 8) {
  let str = '';
  for (let i = 0; i < length; i++) {
    str += randomHexChar();
  }
  return str;
}

function randomHexChar() {
  let chars = 'abcdef0123456789';
  let index = Math.floor(Math.random() * chars.length);
  return chars[index];
}

function animateIn(identifier) {
  setTimeout(() => {
    query(identifier).classList.remove('translate-x-24', 'opacity-0');
  }, 100);
}

function animateOut(identifier) {
  query(identifier).classList.add('translate-x-24', 'opacity-0');
}
