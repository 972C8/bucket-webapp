serviceEndpointURL = window.location.protocol + '//' + window.location.host;

function login(email, password, remember, callback) {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/login',
    data: JSON.stringify({
      email: email,
      password: password,
      remember: remember,
    }),
    success: function () {
      callback(true);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown);
      callback(false);
    },
  });
}

function validateLogin(callback) {
  $.ajax({
    type: 'HEAD',
    url: serviceEndpointURL + '/validate',
    success: function () {
      callback(true);
    },
    error: function () {
      callback(false);
    },
  });
}

function register(name, email, password, callbackSuccess, callbackError) {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/user/register',
    data: JSON.stringify({
      name: name,
      email: email,
      password: password,
    }),
    success: function () {
      callbackSuccess(true);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown);
      callbackError(jqXHR.responseJSON.message);
    },
  });
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
