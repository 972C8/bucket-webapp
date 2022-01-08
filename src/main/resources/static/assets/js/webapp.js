serviceEndpointURL = window.location.protocol + '//' + window.location.host;

async function login(email, password, remember, callback) {
  const result = await Request.POST('/login', {
    email: email,
    password: password,
    remember: remember,
  });
  callback(result);
}

async function validateLogin(callback) {
  try {
    await Request.HEAD('/validate');
    callback(true);
  } catch (e) {
    callback(false);
  }
}

async function register(name, email, password, callbackSuccess, callbackError) {
  try {
    await Request.POST('/user/register', {
      name: name,
      email: email,
      password: password,
    });

    callbackSuccess(true);
  } catch (e) {
    callbackError(e);
  }
}

function getURLParameter(name) {
  return (
    decodeURIComponent(
      (new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [
        ,
        '',
      ])[1].replace(/\+/g, '%20')
    ) || null
  );
}

function getCookie(name) {
  const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
  if (match) return match[2];
}
