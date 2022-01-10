// lib.js
// ------
// Provides global shortcuts and utility function packages
//
// Author: Marco Kaufmann
//

// Shortcuts
// :::::::::

// Shortcut for getting a query param
function param(key) {
  const urlSearchParams = new URLSearchParams(window.location.search);
  const params = Object.fromEntries(urlSearchParams.entries());

  if (!params.hasOwnProperty(key)) throw new Error('param not set');

  return params[key];
}

// Shortcut for querySelector
function query(identifier) {
  return document.querySelector(identifier);
}

// Shortcut for querySelectorAll
function queryAll(identifier) {
  return document.querySelectorAll(identifier);
}

// Shortcut for when document is ready
function ready(callback, validate = true) {
  document.addEventListener(
    'DOMContentLoaded',
    () => {
      if (validate) {
        validateLogin((result) => {
          if (!result) {
            redirect('/login');
          }
        });
      }
      callback();
    },
    { once: true }
  );
}

// Shortcut for redirecting to path
function redirect(path) {
  window.location.replace(path);
}

// Shortcut for getting the value of an element
function val(identifier, value) {
  if (value) {
    query(identifier).value = value;
  } else {
    return query(identifier).value;
  }
}

// UTILITY PACKAGES
// ::::::::::::::::

// Animation

const Animation = {
  // Slide element in
  // Requires transition classes
  slideIn(identifier) {
    setTimeout(() => {
      query(identifier).classList.remove('translate-x-24', 'opacity-0');
    }, 50);
  },

  // Slide element out
  // Requires transition classes
  slideOut(identifier) {
    query(identifier).classList.add('translate-x-24', 'opacity-0');
  },

  // Fade element in
  // Requires transition classes
  fadeIn(identifier, duration = 150) {
    return new Promise((resolve, _) => {
      query(identifier).classList.remove('opacity-0');
      setTimeout(() => {
        resolve();
      }, duration);
    });
  },

  // Fade element out
  // Requires transition classes
  fadeOut(identifier, duration = 150) {
    return new Promise((resolve, _) => {
      query(identifier).classList.add('opacity-0');
      setTimeout(() => {
        resolve();
      }, duration);
    });
  },
};

// Filter

function Filter(options) {
  this.defaults = {};

  this.options = { ...this.defaults, ...options };
  this.filter = {};

  this.parseSort = (value, allowed) => {
    allowed.forEach((entry) => allowed.push(`-${entry}`));

    if (!allowed.includes(value)) {
      return Object.assign({}, this.options.sort.default);
    }

    return {
      key: value.replace('-', ''),
      desc: value[0] === '-',
    };
  };

  this.parseString = (value, allowed) => {
    return allowed.includes(value) ? value : null;
  };

  this.parseNumber = (value) => {
    return isNaN(parseInt(value)) ? null : parseInt(value);
  };

  this.parseBoolean = (value) => {
    switch (value) {
      case 'true':
        return true;
      case 'false':
        return false;
      default:
        return null;
    }
  };

  this.sortQuery = (key) => {
    let sort = Object.assign({}, this.filter.sort);

    if (sort.key === key) {
      sort.desc = !sort.desc;
    } else {
      sort = {
        key: key,
        desc: false,
      };
    }

    return this.query('sort', sort);
  };

  this.toggleQuery = (key) => {
    if (!this.filter.hasOwnProperty(key)) return this.query();

    let value = this.filter[key];

    switch (value) {
      case true:
        value = false;
        break;
      case false:
        value = null;
        break;
      case null:
        value = true;
        break;
    }

    return this.query(key, value);
  };

  this.query = (key, value) => {
    let copy = this.copy();
    // remove unset filters and sort

    if (key !== undefined && value !== undefined) {
      copy[key] = value;
    }

    for (let key in copy) {
      if (copy[key] === null) delete copy[key];
      if (key === 'sort') {
        copy[key] = copy[key].desc ? '-' + copy[key].key : copy[key].key;
      }
    }

    return '?' + new URLSearchParams(copy).toString();
  };

  this.copy = () => {
    return Object.assign({}, this.filter);
  };

  this.get = (key) => {
    if (!this.filter.hasOwnProperty(key)) return undefined;

    return this.filter[key];
  };

  this.set = (key, value) => {
    this.filter[key] = value;
  };

  this.parse = () => {
    const urlSearchParams = new URLSearchParams(window.location.search);
    const params = Object.fromEntries(urlSearchParams.entries());

    for (let key in this.options) {
      switch (this.options[key].type) {
        case 'boolean':
          this.filter[key] = params.hasOwnProperty(key) ? this.parseBoolean(params[key]) : null;
          break;
        case 'number':
          this.filter[key] = params.hasOwnProperty(key) ? this.parseNumber(params[key]) : null;
          break;
        case 'sort':
          this.filter[key] = params.hasOwnProperty(key)
            ? this.parseSort(params[key], this.options[key].allowed)
            : Object.assign({}, this.options.sort.default);
          break;
        default:
          this.filter[key] = null;
          // unreachable!
          break;
      }
    }
  };

  this.parse();
}

// Form

const Form = {
  colorize(watcher, color) {
    let classes = query(watcher.identifier).className;
    classes = classes.replace(/text-[a-z]+-500/, '');
    query(watcher.identifier).className = `${classes} text-${color}-500`;
  },
  isValid() {
    return Object.values(Store.get('isValid')).every((value) => value);
  },
  watch(watchers, submit) {
    Store.set('isValid', {});

    watchers.forEach((watcher) => {
      Form.watchValue(watcher, submit);
    });
  },
  watchValue(watcher, submit) {
    Form.validate(watcher, val(watcher.identifier));
    Form.format(watcher, submit);
    if (watcher.colorize) Form.colorize(watcher, val(watcher.identifier));

    if (watcher.event && Array.isArray(watcher.event)) {
      watcher.event.forEach((event) => {
        query(watcher.identifier).addEventListener(event, (e) => {
          Form.validate(watcher, e.target.value);
          Form.format(watcher, submit);
          if (watcher.colorize) Form.colorize(watcher, e.target.value);
        });
      });
    } else {
      const event = watcher.event || 'keyup';
      query(watcher.identifier).addEventListener(event, (e) => {
        Form.validate(watcher, e.target.value);
        Form.format(watcher, submit);
        if (watcher.colorize) Form.colorize(watcher, e.target.value);
      });
    }
  },
  validate(watcher, value) {
    const { identifier, required, validator } = watcher;

    let isValid = Store.get('isValid');

    isValid[identifier] = !required && value === '' ? true : validator(value);
    Store.set('isValid', isValid);
  },
  format(watcher, submit) {
    const { identifier, type, message } = watcher;

    const isValid = Store.get('isValid')[identifier];
    Form.setAppearance(identifier, type, isValid);
    Form.setErrorMessage(identifier, message);
    Form.setSubmit(submit);
  },
  setAppearance(identifier, type, valid) {
    const rules = Store.get('errorStylingRules');
    rules[type].forEach((rule) => {
      let _identifier = rule.identifier.replace('$', identifier);
      if (rule.toggle) {
        query(_identifier).classList.toggle(rule.toggle, valid);
      } else {
        query(_identifier).className = valid ? rule.default : rule.error;
      }
    });
  },
  setErrorMessage(identifier, message) {
    query(`${identifier}-error`).textContent = message;
  },
  setSubmit(identifier) {
    const valid = Form.isValid();

    query(identifier).disabled = !valid;
    Form.setAppearance(identifier, 'submit', valid);
  },
};

// Formatting

const Format = {
  // Convert camelCase to kebab-case
  camelToKebab(camelCase) {
    return camelCase.replace(/([a-z0-9]|(?=[A-Z]))([A-Z])/g, '$1-$2').toLowerCase();
  },
  // Capitalize first character
  capitalize(value) {
    return value.charAt(0).toUpperCase() + value.slice(1);
  },
  // Additionally replace + with space as decodeURIComponent expects %20 for a space
  decodeURIComponent(value) {
    return decodeURIComponent(value).replaceAll('+', ' ');
  },
  // Transform a given ISO date string to DD. Month YYYY format.
  parseDate(dateString, short = false) {
    const date = new Date(dateString);
    const options = short
      ? { year: 'numeric', month: 'short', day: '2-digit' }
      : { year: 'numeric', month: 'long', day: '2-digit' };
    return date.toLocaleDateString('de-CH', options);
  },
  sort(items, key, desc = false) {
    switch (key) {
      case 'title':
      case 'name':
      case 'color':
      case 'icon':
        items.sort((a, b) => {
          return a[key] < b[key] ? -1 : a[key] > b[key] ? 1 : 0;
        });
        break;
      case 'bucket':
        items.sort((a, b) => {
          a = a.bucket ? a.bucket.name : '';
          b = b.bucket ? b.bucket.name : '';
          return a < b ? -1 : a > b ? 1 : 0;
        });
        break;
      case 'dateToAccomplish':
      case 'dateAccomplishedOn':
      case 'year':
        items.sort((a, b) => {
          // Transform dateString (YYYY-MM-DD) to integer
          let x = Format.dateToInt(a[key], desc);
          let y = Format.dateToInt(b[key], desc);

          return x < y ? -1 : x > y ? 1 : 0;
        });
        break;
    }

    if (desc) items.reverse();
    return items;
  },
  dateToInt(dateString, low) {
    if (dateString === null) return low ? 0 : 99999999;
    return parseInt(dateString.replaceAll('-', ''));
  },
  groupByYear(array, key) {
    let years = {};

    array.forEach((item) => {
      let year = item[key].split('-')[0];
      if (!years.hasOwnProperty(year)) {
        years[year] = { year, items: [] };
      }

      years[year].items.push(item);
    });

    return Object.values(years);
  },
};

// Notification

const Notification = {
  // Close notification with given id
  close(id) {
    Animation.slideOut(`[data-notification][data-id="${id}"]`);
    setTimeout(() => {
      query(`[data-notification][data-id="${id}"]`).remove();
    }, 500);
  },
  // Create new notification
  notify(title, message) {
    const notification = query('#notification');
    const id = Random.hex(8);
    const clone = document.importNode(notification.content, true);

    clone.querySelector('[data-notification]').dataset.id = id;
    clone.querySelector('[data-title]').textContent = title;
    clone.querySelector('[data-message]').textContent = message;
    clone.querySelector('[data-close]').addEventListener('click', () => {
      Notification.close(id);
    });

    query('#notification-panel').appendChild(clone);
    Animation.slideIn(`[data-notification][data-id="${id}"]`);

    setTimeout(() => {
      Notification.close(id);
    }, 10000);
  },
};

const Modal = {
  // Close modal with given id
  async close(id) {
    await Animation.fadeOut(`[data-modal][data-id="${id}"]`, 250);
    await Animation.fadeOut('#modal-background', 100);
    query(`[data-modal][data-id="${id}"]`).remove();
    query('#modal-panel').classList.add('hidden');
  },
  async confirm(title, message, callback) {
    const modal = query('#modal-confirm');
    const id = Random.hex(8);
    const clone = document.importNode(modal.content, true);

    clone.querySelector('[data-modal]').dataset.id = id;
    clone.querySelector('[data-title]').textContent = title;
    clone.querySelector('[data-message]').textContent = message;
    clone.querySelector('[data-delete]').addEventListener('click', async () => {
      await Modal.close(id);
      callback();
    });
    clone.querySelector('[data-cancel]').addEventListener('click', async () => {
      await Modal.close(id);
    });

    query('#modal-panel').classList.remove('hidden');
    query('#modal-container').appendChild(clone);
    await Animation.fadeIn('#modal-background', 100);
    await Animation.fadeIn(`[data-modal][data-id="${id}"]`, 250);
  },
  async prompt(title, message, callback) {
    const modal = query('#modal-prompt');
    const id = Random.hex(8);
    const clone = document.importNode(modal.content, true);

    clone.querySelector('[data-modal]').dataset.id = id;
    clone.querySelector('[data-confirm]').dataset.id = id;

    clone.querySelector('[data-title]').textContent = title;
    clone.querySelector('[data-message]').textContent = message;
    clone.querySelector('[data-confirm]').addEventListener('click', async () => {
      let value = val('#confirm');
      await Modal.close(id);
      callback(value);
    });
    clone.querySelector('[data-cancel]').addEventListener('click', async () => {
      await Modal.close(id);
    });

    const watchers = [
      {
        identifier: '#confirm',
        type: 'text',
        required: true,
        message: 'Must be a valid date.',
        event: 'change',
        validator: (value) => Regex.validate(Regex.preset('date', null, null), value),
      },
    ];

    query('#modal-panel').classList.remove('hidden');
    query('#modal-container').appendChild(clone);

    Form.watch(watchers, `[data-confirm][data-id="${id}"`);

    await Animation.fadeIn('#modal-background', 100);
    await Animation.fadeIn(`[data-modal][data-id="${id}"]`, 250);
  },
};

// Random

const Random = {
  // Generate random hex string of given length
  hex(length = 8) {
    let str = '';
    for (let i = 0; i < length; i++) {
      str += Random.hexChar();
    }
    return str;
  },
  // Generate random hex char
  hexChar() {
    const chars = 'abcdef0123456789';
    const index = Math.floor(Math.random() * chars.length);
    return chars[index];
  },
};

// Regex

const Regex = {
  presets: {
    default: '^[a-z0-9\\s]{MIN,MAX}$',
    date: '^[0-9]{4}-[0-9]{2}-[0-9]{2}$',
    number: '[0-9]+',
    text: "^[a-z0-9äöüéèà.:,;'!?()=$\\s_-]{MIN,MAX}$",
    email:
      '^(([^<>()[\\]\\.,;:\\s@\\"]+(\\.[^<>()[\\]\\.,;:\\s@\\"]+)*)|(\\".+\\"))@(([^<>()[\\]\\.,;:\\s@\\"]+\\.)+[^<>()[\\]\\.,;:\\s@\\"]{2,})$',
    password: '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$',
    labels: '^[\\sa-z0-9-]+$',
    url: 'https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)',
  },
  preset(preset, min, max, flags = 'i') {
    if (!Regex.presets.hasOwnProperty(preset)) preset = 'default';

    return new RegExp(Regex.presets[preset].replace('MIN', min).replace('MAX', max), flags);
  },
  validate(regex, value) {
    return regex.test(value);
  },
};

// Store

const Store = {
  data: {
    colors: [
      'red',
      'orange',
      'amber',
      'yellow',
      'lime',
      'green',
      'emerald',
      'teal',
      'cyan',
      'sky',
      'blue',
      'indigo',
      'violet',
      'purple',
      'fuchsia',
      'pink',
      'rose',
      'gray',
    ],
    errorStylingRules: {
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
    },
    icons: [
      '2fa',
      '3d-cube-sphere',
      'a-b',
      'access-point-off',
      'access-point',
      'accessible',
      'activity',
      'ad-2',
      'ad',
      'adjustments-alt',
      'adjustments-horizontal',
      'adjustments',
      'aerial-lift',
      'affiliate',
      'alarm',
      'alert-circle',
      'alert-octagon',
      'alert-triangle',
      'alien',
      'align-center',
      'align-justified',
      'align-left',
      'align-right',
      'ambulance',
      'anchor',
      'angle',
      'antenna-bars-1',
      'antenna-bars-2',
      'antenna-bars-3',
      'antenna-bars-4',
      'antenna-bars-5',
      'aperture',
      'apple',
      'apps',
      'archive',
      'armchair',
      'arrow-autofit-content',
      'arrow-autofit-down',
      'arrow-autofit-height',
      'arrow-autofit-left',
      'arrow-autofit-right',
      'arrow-autofit-up',
      'arrow-autofit-width',
      'arrow-back-up',
      'arrow-back',
      'arrow-bar-down',
      'arrow-bar-left',
      'arrow-bar-right',
      'arrow-bar-to-down',
      'arrow-bar-to-left',
      'arrow-bar-to-right',
      'arrow-bar-to-up',
      'arrow-bar-up',
      'arrow-big-down',
      'arrow-big-left',
      'arrow-big-right',
      'arrow-big-top',
      'arrow-bottom-bar',
      'arrow-bottom-circle',
      'arrow-bottom-square',
      'arrow-bottom-tail',
      'arrow-down-circle',
      'arrow-down-left-circle',
      'arrow-down-left',
      'arrow-down-right-circle',
      'arrow-down-right',
      'arrow-down',
      'arrow-forward-up',
      'arrow-forward',
      'arrow-left-bar',
      'arrow-left-circle',
      'arrow-left-square',
      'arrow-left-tail',
      'arrow-left',
      'arrow-loop-left',
      'arrow-loop-right',
      'arrow-narrow-down',
      'arrow-narrow-left',
      'arrow-narrow-right',
      'arrow-narrow-up',
      'arrow-ramp-left',
      'arrow-ramp-right',
      'arrow-right-bar',
      'arrow-right-circle',
      'arrow-right-square',
      'arrow-right-tail',
      'arrow-right',
      'arrow-top-bar',
      'arrow-top-circle',
      'arrow-top-square',
      'arrow-top-tail',
      'arrow-up-circle',
      'arrow-up-left-circle',
      'arrow-up-left',
      'arrow-up-right-circle',
      'arrow-up-right',
      'arrow-up',
      'arrow-wave-left-down',
      'arrow-wave-left-up',
      'arrow-wave-right-down',
      'arrow-wave-right-up',
      'arrows-diagonal-2',
      'arrows-diagonal-minimize-2',
      'arrows-diagonal-minimize',
      'arrows-diagonal',
      'arrows-double-ne-sw',
      'arrows-double-nw-se',
      'arrows-double-se-nw',
      'arrows-double-sw-ne',
      'arrows-down-up',
      'arrows-down',
      'arrows-horizontal',
      'arrows-join-2',
      'arrows-join',
      'arrows-left-down',
      'arrows-left-right',
      'arrows-left',
      'arrows-maximize',
      'arrows-minimize',
      'arrows-right-down',
      'arrows-right-left',
      'arrows-right',
      'arrows-sort',
      'arrows-split-2',
      'arrows-split',
      'arrows-up-down',
      'arrows-up-left',
      'arrows-up-right',
      'arrows-up',
      'arrows-vertical',
      'artboard',
      'aspect-ratio',
      'at',
      'atom-2',
      'atom',
      'award',
      'axe',
      'axis-x',
      'axis-y',
      'backhoe',
      'backpack',
      'backspace',
      'badge',
      'badges',
      'ball-american-football',
      'ball-baseball',
      'ball-basketball',
      'ball-bowling',
      'ball-football-off',
      'ball-football',
      'ball-tennis',
      'ball-volleyball',
      'ballon',
      'ban',
      'bandage',
      'barcode',
      'basket',
      'bath',
      'battery-1',
      'battery-2',
      'battery-3',
      'battery-4',
      'battery-automotive',
      'battery-charging-2',
      'battery-charging',
      'battery-eco',
      'battery-off',
      'battery',
      'beach',
      'bed',
      'beer',
      'bell-minus',
      'bell-off',
      'bell-plus',
      'bell-ringing-2',
      'bell-ringing',
      'bell-x',
      'bell',
      'bible',
      'bike',
      'binary',
      'biohazard',
      'blockquote',
      'bluetooth-connected',
      'bluetooth-off',
      'bluetooth',
      'blur',
      'bold',
      'bolt-off',
      'bolt',
      'bone',
      'book-2',
      'book',
      'bookmark-off',
      'bookmark',
      'bookmarks',
      'border-all',
      'border-bottom',
      'border-horizontal',
      'border-inner',
      'border-left',
      'border-none',
      'border-outer',
      'border-radius',
      'border-right',
      'border-style-2',
      'border-style',
      'border-top',
      'border-vertical',
      'bottle',
      'box-margin',
      'box-model-2',
      'box-model',
      'box-multiple-0',
      'box-multiple-1',
      'box-multiple-2',
      'box-multiple-3',
      'box-multiple-4',
      'box-multiple-5',
      'box-multiple-6',
      'box-multiple-7',
      'box-multiple-8',
      'box-multiple-9',
      'box-multiple',
      'box-padding',
      'box',
      'braces',
      'brackets',
      'brand-airbnb',
      'brand-airtable',
      'brand-android',
      'brand-angular',
      'brand-apple-arcade',
      'brand-apple',
      'brand-appstore',
      'brand-asana',
      'brand-behance',
      'brand-bing',
      'brand-bitbucket',
      'brand-booking',
      'brand-bootstrap',
      'brand-chrome',
      'brand-codepen',
      'brand-codesandbox',
      'brand-css3',
      'brand-cucumber',
      'brand-debian',
      'brand-deviantart',
      'brand-discord',
      'brand-disqus',
      'brand-docker',
      'brand-doctrine',
      'brand-dribbble',
      'brand-edge',
      'brand-facebook',
      'brand-figma',
      'brand-firebase',
      'brand-firefox',
      'brand-flickr',
      'brand-foursquare',
      'brand-framer',
      'brand-git',
      'brand-github',
      'brand-gitlab',
      'brand-gmail',
      'brand-google-analytics',
      'brand-google-drive',
      'brand-google-play',
      'brand-google',
      'brand-gravatar',
      'brand-hipchat',
      'brand-html5',
      'brand-instagram',
      'brand-javascript',
      'brand-kickstarter',
      'brand-kotlin',
      'brand-linkedin',
      'brand-loom',
      'brand-mastercard',
      'brand-medium',
      'brand-messenger',
      'brand-meta',
      'brand-netbeans',
      'brand-netflix',
      'brand-notion',
      'brand-nytimes',
      'brand-open-source',
      'brand-opera',
      'brand-pagekit',
      'brand-patreon',
      'brand-paypal',
      'brand-php',
      'brand-pinterest',
      'brand-pocket',
      'brand-producthunt',
      'brand-python',
      'brand-react-native',
      'brand-reddit',
      'brand-safari',
      'brand-sass',
      'brand-sentry',
      'brand-shazam',
      'brand-sketch',
      'brand-skype',
      'brand-slack',
      'brand-snapchat',
      'brand-soundcloud',
      'brand-spotify',
      'brand-stackoverflow',
      'brand-steam',
      'brand-stripe',
      'brand-sublime-text',
      'brand-tabler',
      'brand-tailwind',
      'brand-telegram',
      'brand-tidal',
      'brand-tiktok',
      'brand-tinder',
      'brand-tumblr',
      'brand-twitch',
      'brand-twitter',
      'brand-uber',
      'brand-ubuntu',
      'brand-unsplash',
      'brand-vercel',
      'brand-vimeo',
      'brand-visual-studio',
      'brand-vk',
      'brand-whatsapp',
      'brand-windows',
      'brand-yahoo',
      'brand-ycombinator',
      'brand-youtube',
      'bread',
      'briefcase',
      'brightness-2',
      'brightness-down',
      'brightness-half',
      'brightness-up',
      'brightness',
      'browser',
      'brush',
      'bucket',
      'bug',
      'building-arch',
      'building-bank',
      'building-bridge-2',
      'building-bridge',
      'building-carousel',
      'building-castle',
      'building-church',
      'building-community',
      'building-cottage',
      'building-factory',
      'building-fortress',
      'building-hospital',
      'building-lighthouse',
      'building-monument',
      'building-pavilon',
      'building-skyscraper',
      'building-store',
      'building-warehouse',
      'building',
      'bulb-off',
      'bulb',
      'bulldozer',
      'bus',
      'businessplan',
      'calculator',
      'calendar-event',
      'calendar-minus',
      'calendar-off',
      'calendar-plus',
      'calendar-stats',
      'calendar-time',
      'calendar',
      'camera-minus',
      'camera-off',
      'camera-plus',
      'camera-rotate',
      'camera-selfie',
      'camera',
      'candle',
      'candy',
      'capture',
      'car-crane',
      'car-crash',
      'car',
      'caravan',
      'cardboards',
      'caret-down',
      'caret-left',
      'caret-right',
      'caret-up',
      'cash-banknote-off',
      'cash-banknote',
      'cash',
      'cast',
      'ce',
      'certificate',
      'charging-pile',
      'chart-arcs-3',
      'chart-arcs',
      'chart-area-line',
      'chart-area',
      'chart-arrows-vertical',
      'chart-arrows',
      'chart-bar',
      'chart-bubble',
      'chart-candle',
      'chart-circles',
      'chart-donut-2',
      'chart-donut-3',
      'chart-donut-4',
      'chart-donut',
      'chart-dots',
      'chart-infographic',
      'chart-line',
      'chart-pie-2',
      'chart-pie-3',
      'chart-pie-4',
      'chart-pie',
      'chart-radar',
      'check',
      'checkbox',
      'checks',
      'checkup-list',
      'cheese',
      'chevron-down-left',
      'chevron-down-right',
      'chevron-down',
      'chevron-left',
      'chevron-right',
      'chevron-up-left',
      'chevron-up-right',
      'chevron-up',
      'chevrons-down-left',
      'chevrons-down-right',
      'chevrons-down',
      'chevrons-left',
      'chevrons-right',
      'chevrons-up-left',
      'chevrons-up-right',
      'chevrons-up',
      'christmas-tree',
      'circle-0',
      'circle-1',
      'circle-2',
      'circle-3',
      'circle-4',
      'circle-5',
      'circle-6',
      'circle-7',
      'circle-8',
      'circle-9',
      'circle-check',
      'circle-dashed',
      'circle-dot',
      'circle-dotted',
      'circle-half-vertical',
      'circle-half',
      'circle-minus',
      'circle-off',
      'circle-plus',
      'circle-square',
      'circle-x',
      'circle',
      'circles',
      'clear-all',
      'clear-formatting',
      'click',
      'clipboard-check',
      'clipboard-list',
      'clipboard-plus',
      'clipboard-x',
      'clipboard',
      'clock',
      'cloud-download',
      'cloud-fog',
      'cloud-off',
      'cloud-rain',
      'cloud-snow',
      'cloud-storm',
      'cloud-upload',
      'cloud',
      'code-minus',
      'code-plus',
      'code',
      'coffee',
      'coin',
      'color-picker',
      'color-swatch',
      'column-insert-left',
      'column-insert-right',
      'columns',
      'comet',
      'command',
      'compass',
      'components',
      'confetti',
      'container',
      'contrast-2',
      'contrast',
      'cookie',
      'copy',
      'copyleft',
      'copyright',
      'corner-down-left-double',
      'corner-down-left',
      'corner-down-right-double',
      'corner-down-right',
      'corner-left-down-double',
      'corner-left-down',
      'corner-left-up-double',
      'corner-left-up',
      'corner-right-down-double',
      'corner-right-down',
      'corner-right-up-double',
      'corner-right-up',
      'corner-up-left-double',
      'corner-up-left',
      'corner-up-right-double',
      'corner-up-right',
      'cpu',
      'crane',
      'creative-commons',
      'credit-card-off',
      'credit-card',
      'crop',
      'cross',
      'crosshair',
      'crown-off',
      'crown',
      'crutches',
      'cup',
      'curling',
      'curly-loop',
      'currency-bahraini',
      'currency-bath',
      'currency-bitcoin',
      'currency-cent',
      'currency-dinar',
      'currency-dirham',
      'currency-dogecoin',
      'currency-dollar-australian',
      'currency-dollar-canadian',
      'currency-dollar-singapore',
      'currency-dollar',
      'currency-ethereum',
      'currency-euro',
      'currency-forint',
      'currency-frank',
      'currency-krone-czech',
      'currency-krone-danish',
      'currency-krone-swedish',
      'currency-leu',
      'currency-lira',
      'currency-litecoin',
      'currency-naira',
      'currency-pound',
      'currency-real',
      'currency-renminbi',
      'currency-ripple',
      'currency-riyal',
      'currency-rubel',
      'currency-rupee',
      'currency-shekel',
      'currency-taka',
      'currency-tugrik',
      'currency-won',
      'currency-yen',
      'currency-zloty',
      'currency',
      'current-location',
      'cursor-text',
      'cut',
      'dashboard',
      'database-export',
      'database-import',
      'database-off',
      'database',
      'details',
      'device-analytics',
      'device-audio-tape',
      'device-cctv',
      'device-computer-camera-off',
      'device-computer-camera',
      'device-desktop-analytics',
      'device-desktop-off',
      'device-desktop',
      'device-floppy',
      'device-gamepad',
      'device-laptop',
      'device-mobile-message',
      'device-mobile-rotated',
      'device-mobile-vibration',
      'device-mobile',
      'device-speaker',
      'device-tablet',
      'device-tv',
      'device-watch-stats-2',
      'device-watch-stats',
      'device-watch',
      'devices-2',
      'devices-pc',
      'devices',
      'diamond',
      'dice',
      'dimensions',
      'direction-horizontal',
      'direction',
      'directions',
      'disabled-2',
      'disabled',
      'disc',
      'discount-2',
      'discount',
      'divide',
      'dna-2',
      'dna',
      'dog-bowl',
      'door-enter',
      'door-exit',
      'door',
      'dots-circle-horizontal',
      'dots-diagonal-2',
      'dots-diagonal',
      'dots-vertical',
      'dots',
      'download',
      'drag-drop-2',
      'drag-drop',
      'drone-off',
      'drone',
      'droplet-filled-2',
      'droplet-filled',
      'droplet-half-2',
      'droplet-half',
      'droplet-off',
      'droplet',
      'ear-off',
      'ear',
      'edit-circle',
      'edit',
      'egg',
      'emergency-bed',
      'emphasis',
      'engine',
      'equal-not',
      'equal',
      'eraser',
      'exchange',
      'exclamation-mark',
      'exposure',
      'external-link',
      'eye-check',
      'eye-off',
      'eye-table',
      'eye',
      'eyeglass-2',
      'eyeglass',
      'face-id-error',
      'face-id',
      'face-mask',
      'fall',
      'feather',
      'fence',
      'file-alert',
      'file-analytics',
      'file-certificate',
      'file-check',
      'file-code-2',
      'file-code',
      'file-diff',
      'file-digit',
      'file-dislike',
      'file-download',
      'file-export',
      'file-horizontal',
      'file-import',
      'file-info',
      'file-invoice',
      'file-like',
      'file-minus',
      'file-music',
      'file-off',
      'file-phone',
      'file-plus',
      'file-report',
      'file-search',
      'file-shredder',
      'file-symlink',
      'file-text',
      'file-upload',
      'file-x',
      'file-zip',
      'file',
      'files-off',
      'files',
      'filter-off',
      'filter',
      'fingerprint',
      'firetruck',
      'first-aid-kit',
      'fish',
      'flag-2',
      'flag-3',
      'flag',
      'flame',
      'flare',
      'flask-2',
      'flask',
      'flip-horizontal',
      'flip-vertical',
      'float-center',
      'float-left',
      'float-none',
      'float-right',
      'focus-2',
      'focus',
      'fold-down',
      'fold-up',
      'fold',
      'folder-minus',
      'folder-off',
      'folder-plus',
      'folder-x',
      'folder',
      'folders',
      'forbid-2',
      'forbid',
      'forklift',
      'forms',
      'frame',
      'free-rights',
      'friends',
      'gas-station',
      'gauge',
      'gavel',
      'geometry',
      'ghost',
      'gift',
      'git-branch',
      'git-commit',
      'git-compare',
      'git-fork',
      'git-merge',
      'git-pull-request-closed',
      'git-pull-request-draft',
      'git-pull-request',
      'glass-full',
      'glass-off',
      'glass',
      'globe',
      'golf',
      'gps',
      'grain',
      'grid-dots',
      'grid-pattern',
      'grill',
      'grip-horizontal',
      'grip-vertical',
      'growth',
      'h-1',
      'h-2',
      'h-3',
      'h-4',
      'h-5',
      'h-6',
      'hammer',
      'hand-click',
      'hand-finger',
      'hand-little-finger',
      'hand-middle-finger',
      'hand-move',
      'hand-off',
      'hand-ring-finger',
      'hand-rock',
      'hand-stop',
      'hand-three-fingers',
      'hand-two-fingers',
      'hanger',
      'hash',
      'haze',
      'heading',
      'headphones-off',
      'headphones',
      'headset',
      'heart-broken',
      'heart-rate-monitor',
      'heart',
      'heartbeat',
      'helicopter-landing',
      'helicopter',
      'helmet',
      'help',
      'hexagon-off',
      'hexagon',
      'hierarchy-2',
      'hierarchy',
      'highlight',
      'history',
      'home-2',
      'home',
      'hotel-service',
      'hourglass',
      'ice-cream-2',
      'ice-cream',
      'ice-skating',
      'id',
      'inbox',
      'indent-decrease',
      'indent-increase',
      'infinity',
      'info-circle',
      'info-square',
      'italic',
      'jump-rope',
      'karate',
      'kering',
      'key',
      'keyboard-hide',
      'keyboard-off',
      'keyboard-show',
      'keyboard',
      'lamp',
      'language-hiragana',
      'language-katakana',
      'language',
      'lasso',
      'layers-difference',
      'layers-intersect',
      'layers-linked',
      'layers-subtract',
      'layers-union',
      'layout-2',
      'layout-align-bottom',
      'layout-align-center',
      'layout-align-left',
      'layout-align-middle',
      'layout-align-right',
      'layout-align-top',
      'layout-board-split',
      'layout-board',
      'layout-bottombar',
      'layout-cards',
      'layout-columns',
      'layout-distribute-horizontal',
      'layout-distribute-vertical',
      'layout-grid-add',
      'layout-grid',
      'layout-kanban',
      'layout-list',
      'layout-navbar',
      'layout-rows',
      'layout-sidebar-right',
      'layout-sidebar',
      'layout',
      'leaf',
      'lego',
      'lemon-2',
      'lemon',
      'letter-a',
      'letter-b',
      'letter-c',
      'letter-case-lower',
      'letter-case-toggle',
      'letter-case-upper',
      'letter-case',
      'letter-d',
      'letter-e',
      'letter-f',
      'letter-g',
      'letter-h',
      'letter-i',
      'letter-j',
      'letter-k',
      'letter-l',
      'letter-m',
      'letter-n',
      'letter-o',
      'letter-p',
      'letter-q',
      'letter-r',
      'letter-s',
      'letter-spacing',
      'letter-t',
      'letter-u',
      'letter-v',
      'letter-w',
      'letter-x',
      'letter-y',
      'letter-z',
      'letters-case',
      'license',
      'lifebuoy',
      'line-dashed',
      'line-dotted',
      'line-height',
      'line',
      'link',
      'list-check',
      'list-details',
      'list-numbers',
      'list-search',
      'list',
      'live-photo',
      'live-view',
      'loader-quarter',
      'loader',
      'location',
      'lock-access',
      'lock-off',
      'lock-open',
      'lock-square',
      'lock',
      'login',
      'logout',
      'lollipop',
      'luggage',
      'lungs',
      'macro',
      'magnet',
      'mail-forward',
      'mail-opened',
      'mail',
      'mailbox',
      'man',
      'manual-gearbox',
      'map-2',
      'map-pin-off',
      'map-pin',
      'map-pins',
      'map-search',
      'map',
      'markdown',
      'marquee-2',
      'marquee',
      'mars',
      'mask-off',
      'mask',
      'massage',
      'math-function',
      'math-symbols',
      'math',
      'maximize',
      'meat',
      'medal-2',
      'medal',
      'medical-cross',
      'medicine-syrup',
      'menu-2',
      'menu',
      'message-2',
      'message-circle-2',
      'message-circle-off',
      'message-circle',
      'message-dots',
      'message-language',
      'message-off',
      'message-plus',
      'message-report',
      'message',
      'messages-off',
      'messages',
      'microphone-2',
      'microphone-off',
      'microphone',
      'microscope',
      'miliraty-award',
      'military-rank',
      'milk',
      'minimize',
      'minus-vertical',
      'minus',
      'mist',
      'mood-boy',
      'mood-confuzed',
      'mood-crazy-happy',
      'mood-cry',
      'mood-empty',
      'mood-happy',
      'mood-kid',
      'mood-nervous',
      'mood-neutral',
      'mood-sad',
      'mood-smile',
      'mood-suprised',
      'mood-tongue',
      'moon-2',
      'moon-stars',
      'moon',
      'moped',
      'motorbike',
      'mountain',
      'mouse',
      'movie',
      'mug',
      'multiplier-0-5x',
      'multiplier-1-5x',
      'multiplier-1x',
      'multiplier-2x',
      'mushroom',
      'music',
      'new-section',
      'news',
      'nfc',
      'no-copyright',
      'no-creative-commons',
      'no-derivatives',
      'note',
      'notebook',
      'notes',
      'notification',
      'number-0',
      'number-1',
      'number-2',
      'number-3',
      'number-4',
      'number-5',
      'number-6',
      'number-7',
      'number-8',
      'number-9',
      'nurse',
      'octagon-off',
      'octagon',
      'old',
      'olympics',
      'omega',
      'outlet',
      'overline',
      'package',
      'pacman',
      'page-break',
      'paint',
      'palette',
      'panorama-horizontal',
      'panorama-vertical',
      'paperclip',
      'parachute',
      'parentheses',
      'parking',
      'peace',
      'pencil',
      'pennant',
      'pepper',
      'percentage',
      'perspective',
      'phone-call',
      'phone-calling',
      'phone-check',
      'phone-incoming',
      'phone-off',
      'phone-outgoing',
      'phone-pause',
      'phone-plus',
      'phone-x',
      'phone',
      'photo-off',
      'photo',
      'physotherapist',
      'picture-in-picture-off',
      'picture-in-picture-on',
      'picture-in-picture',
      'pig',
      'pill',
      'pills',
      'pin',
      'pinned-off',
      'pinned',
      'pizza',
      'plane-arrival',
      'plane-departure',
      'plane-inflight',
      'plane',
      'planet',
      'plant-2',
      'plant',
      'play-card',
      'player-eject',
      'player-pause',
      'player-play',
      'player-record',
      'player-skip-back',
      'player-skip-forward',
      'player-stop',
      'player-track-next',
      'player-track-prev',
      'playlist',
      'plug',
      'plus',
      'point',
      'pokeball',
      'polaroid',
      'polygon',
      'pool',
      'power',
      'pray',
      'premium-rights',
      'prescription',
      'presentation-analytics',
      'presentation',
      'printer',
      'prison',
      'prompt',
      'propeller',
      'puzzle-2',
      'puzzle',
      'pyramid',
      'qrcode',
      'question-mark',
      'quote',
      'radio',
      'radioactive',
      'radius-bottom-left',
      'radius-bottom-right',
      'radius-top-left',
      'radius-top-right',
      'rainbow',
      'receipt-2',
      'receipt-off',
      'receipt-refund',
      'receipt-tax',
      'receipt',
      'recharging',
      'record-mail',
      'rectangle-vertical',
      'rectangle',
      'recycle',
      'refresh-alert',
      'refresh-dot',
      'refresh',
      'registered',
      'relation-many-to-many',
      'relation-one-to-many',
      'relation-one-to-one',
      'repeat-once',
      'repeat',
      'replace',
      'report-analytics',
      'report-medical',
      'report-money',
      'report-search',
      'report',
      'resize',
      'ripple',
      'road-sign',
      'rocket',
      'roller-skating',
      'rotate-2',
      'rotate-360',
      'rotate-clockwise-2',
      'rotate-clockwise',
      'rotate-rectangle',
      'rotate',
      'route',
      'router',
      'row-insert-bottom',
      'row-insert-top',
      'rss',
      'ruler-2',
      'ruler',
      'run',
      'sailboat',
      'salt',
      'satellite',
      'sausage',
      'scale-outline',
      'scale',
      'scan',
      'school',
      'scissors',
      'scooter-electric',
      'scooter',
      'screen-share-off',
      'screen-share',
      'scuba-mask',
      'search',
      'section',
      'seeding',
      'select',
      'selector',
      'send',
      'separator-horizontal',
      'separator-vertical',
      'separator',
      'server',
      'servicemark',
      'settings-automation',
      'settings',
      'shadow-off',
      'shadow',
      'shape-2',
      'shape-3',
      'shape',
      'share',
      'shield-check',
      'shield-checkered',
      'shield-chevron',
      'shield-lock',
      'shield-off',
      'shield-x',
      'shield',
      'ship',
      'shirt',
      'shoe',
      'shopping-cart-discount',
      'shopping-cart-off',
      'shopping-cart-plus',
      'shopping-cart-x',
      'shopping-cart',
      'shredder',
      'signature',
      'sitemap',
      'skateboard',
      'sleigh',
      'slice',
      'slideshow',
      'smart-home',
      'smoking-no',
      'smoking',
      'snowflake',
      'soccer-field',
      'social',
      'sock',
      'sofa',
      'sort-ascending-2',
      'sort-ascending-letters',
      'sort-ascending-numbers',
      'sort-ascending',
      'sort-descending-2',
      'sort-descending-letters',
      'sort-descending-numbers',
      'sort-descending',
      'soup',
      'space',
      'spacing-horizontal',
      'spacing-vertical',
      'speakerphone',
      'speedboat',
      'sport-billard',
      'square-0',
      'square-1',
      'square-2',
      'square-3',
      'square-4',
      'square-5',
      'square-6',
      'square-7',
      'square-8',
      'square-9',
      'square-check',
      'square-dot',
      'square-forbid-2',
      'square-forbid',
      'square-minus',
      'square-off',
      'square-plus',
      'square-root-2',
      'square-root',
      'square-rotated-off',
      'square-rotated',
      'square-toggle-horizontal',
      'square-toggle',
      'square-x',
      'square',
      'squares-diagonal',
      'squares-filled',
      'stack-2',
      'stack-3',
      'stack',
      'stairs-down',
      'stairs-up',
      'stairs',
      'star-half',
      'star-off',
      'star',
      'stars',
      'steering-wheel',
      'step-into',
      'step-out',
      'stethoscope',
      'sticker',
      'strikethrough',
      'submarine',
      'subscript',
      'subtask',
      'sum',
      'sun-off',
      'sun',
      'sunrise',
      'sunset',
      'superscript',
      'swimming',
      'switch-2',
      'switch-3',
      'switch-horizontal',
      'switch-vertical',
      'switch',
      'table-export',
      'table-import',
      'table-off',
      'table',
      'tag-off',
      'tag',
      'tags-off',
      'tags',
      'tallymark-1',
      'tallymark-2',
      'tallymark-3',
      'tallymark-4',
      'tallymarks',
      'tank',
      'target',
      'temperature-celsius',
      'temperature-fahrenheit',
      'temperature-minus',
      'temperature-plus',
      'temperature',
      'template',
      'tent',
      'terminal-2',
      'terminal',
      'test-pipe',
      'text-direction-ltr',
      'text-direction-rtl',
      'text-resize',
      'text-wrap-disabled',
      'text-wrap',
      'thermometer',
      'thumb-down',
      'thumb-up',
      'ticket',
      'tilt-shift',
      'tir',
      'toggle-left',
      'toggle-right',
      'toilet-paper',
      'tool',
      'tools-kitchen-2',
      'tools-kitchen',
      'tools',
      'tornado',
      'tournament',
      'track',
      'tractor',
      'trademark',
      'traffic-cone',
      'traffic-lights',
      'train',
      'transfer-in',
      'transfer-out',
      'trash-off',
      'trash-x',
      'trash',
      'tree',
      'trees',
      'trending-down-2',
      'trending-down-3',
      'trending-down',
      'trending-up-2',
      'trending-up-3',
      'trending-up',
      'triangle-off',
      'triangle-square-circle',
      'triangle',
      'trident',
      'trophy',
      'truck-delivery',
      'truck-off',
      'truck-return',
      'truck',
      'typography',
      'umbrella',
      'underline',
      'unlink',
      'upload',
      'urgent',
      'user-check',
      'user-circle',
      'user-exclamation',
      'user-minus',
      'user-off',
      'user-plus',
      'user-search',
      'user-x',
      'user',
      'users',
      'vaccine-bottle',
      'vaccine',
      'variable',
      'vector-beizer-2',
      'vector-bezier',
      'vector-triangle',
      'vector',
      'venus',
      'versions',
      'video-minus',
      'video-off',
      'video-plus',
      'video',
      'view-360',
      'viewfinder',
      'viewport-narrow',
      'viewport-wide',
      'virus-off',
      'virus-search',
      'virus',
      'vocabulary',
      'volume-2',
      'volume-3',
      'volume',
      'walk',
      'wall',
      'wallet',
      'wallpaper',
      'wand',
      'wave-saw-tool',
      'wave-sine',
      'wave-square',
      'wifi-0',
      'wifi-1',
      'wifi-2',
      'wifi-off',
      'wifi',
      'wind',
      'windmill',
      'window',
      'wiper-wash',
      'wiper',
      'woman',
      'world-download',
      'world-latitude',
      'world-longitude',
      'world-upload',
      'world',
      'wrecking-ball',
      'writing-sign',
      'writing',
      'x',
      'yin-yang',
      'zodiac-aquarius',
      'zodiac-aries',
      'zodiac-cancer',
      'zodiac-capricorn',
      'zodiac-gemini',
      'zodiac-leo',
      'zodiac-libra',
      'zodiac-pisces',
      'zodiac-sagittarius',
      'zodiac-scorpio',
      'zodiac-taurus',
      'zodiac-virgo',
      'zoom-cancel',
      'zoom-check',
      'zoom-in',
      'zoom-money',
      'zoom-out',
      'zoom-question',
    ],
    isValid: {},
  },
  set(key, val) {
    Store.data[key] = val;
  },
  get(key) {
    return Store.data[key] || null;
  },
};

// Template

const Template = {
  // Renders template with given rules
  render(identifier, rules, parent) {
    const template = query(identifier);
    const clone = document.importNode(template.content, true);

    rules.forEach((rule) => {
      if (rule.textContent) {
        clone.querySelector(`[data-${rule.key}]`).textContent = rule.textContent;
      }

      if (rule.html) {
        clone.querySelector(`[data-${rule.key}]`).innerHTML = rule.html;
      }

      if (rule.classes) {
        clone.querySelector(`[data-${rule.key}]`).classList.add(...rule.classes);
      }

      if (rule.src) {
        clone.querySelector(`[data-${rule.key}]`).src = rule.src;
      }

      if (rule.href) {
        clone.querySelector(`[data-${rule.key}]`).href = rule.href;
      }

      if (rule.attr) {
        for (let key in rule.attr) {
          clone.querySelector(`[data-${rule.key}]`).setAttribute(key, rule.attr[key]);
        }
      }

      if (rule.listener) {
        clone.querySelector(`[data-${rule.key}]`).addEventListener('click', () => {
          rule.listener();
        });
      }

      if (rule.remove) {
        if (clone.querySelector(`[data-${rule.key}]`).textContent === '') {
          clone.querySelector(`[data-${rule.key}]`).remove();
        }
      }
    });

    query(parent).appendChild(clone);
  },
};
