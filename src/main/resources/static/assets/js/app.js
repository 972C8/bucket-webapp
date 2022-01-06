function postBucketItem(bucketItem, callbackSuccess, callbackError) {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/bucket-items',
    data: bucketItem,
    success: function (data) {
      callbackSuccess(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
      callbackError(jqXHR.responseJSON.message)
    },
  })
}

function getBucketItem(bucketItemID, callback) {
  $.ajax({
    type: 'GET',
    dataType: 'json',
    url: serviceEndpointURL + '/api/bucket-items/' + bucketItemID,
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function getBucketItems(callback) {
  $.ajax({
    type: 'GET',
    dataType: 'json',
    url: serviceEndpointURL + '/api/bucket-items',
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function putBucketItem(bucketItemID, bucketItem, callbackSuccess, callbackError) {
  $.ajax({
    type: 'PUT',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/bucket-items/' + bucketItemID,
    data: bucketItem,
    success: function (data) {
      callbackSuccess(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
      callbackError(jqXHR.responseJSON.message)
    },
  })
}

function deleteBucketItem(bucketItemID, callback) {
  $.ajax({
    type: 'DELETE',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/bucket-items/' + bucketItemID,
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function getBucketItemJSON(id, title, description, dateToAccomplish, image) {
  if (id === null) {
    return JSON.stringify({
      title: title,
      description: description,
      dateToAccomplish: dateToAccomplish,
      image: image,
    })
  }
  return JSON.stringify({
    id: id,
    title: title,
    description: description,
  })
}

function postBucket(bucket, callbackSuccess, callbackError) {
  $.ajax({
    type: 'POST',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/buckets',
    data: bucket,
    success: function (data) {
      callbackSuccess(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
      callbackError(jqXHR.responseJSON.message)
    },
  })
}

function getBucket(bucketID, callback) {
  $.ajax({
    type: 'GET',
    dataType: 'json',
    url: serviceEndpointURL + '/api/buckets/' + bucketID,
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function getBuckets(callback) {
  $.ajax({
    type: 'GET',
    dataType: 'json',
    url: serviceEndpointURL + '/api/buckets',
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function putBucket(bucketID, bucket, callbackSuccess, callbackError) {
  $.ajax({
    type: 'PUT',
    contentType: 'application/json',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/buckets/' + bucketID,
    data: bucket,
    success: function (data) {
      callbackSuccess(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
      callbackError(jqXHR.responseJSON.message)
    },
  })
}

function deleteBucket(bucketID, callback) {
  $.ajax({
    type: 'DELETE',
    headers: {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    },
    url: serviceEndpointURL + '/api/buckets/' + bucketID,
    success: function (data) {
      callback(data)
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log(jqXHR, textStatus, errorThrown)
    },
  })
}

function getBucketJSON(id, name, color) {
  if (id === null) {
    return JSON.stringify({
      name: name,
      color: color,
    })
  }
  return JSON.stringify({
    id: id,
    name: name,
    color: color,
  })
}
