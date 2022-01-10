serviceEndpointURL = window.location.protocol + '//' + window.location.host;

async function validateLogin(callback) {
  try {
    await Request.HEAD('/validate');
    callback(true);
  } catch (e) {
    callback(false);
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
