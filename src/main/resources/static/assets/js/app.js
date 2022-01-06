// Global request promise-based utility functions

async function GET(endpoint) {
  try {
    const response = await fetch(`${serviceEndpointURL}/api/${endpoint}`, {
      method: 'GET',
      headers: {
        'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
      },
    });

    if (!response.ok) throw new Error(`GET: Request failed endpoint ${endpoint}.`);

    return await response.json();
  } catch (error) {
    throw error;
  }
}

async function POST(endpoint, payload, json = true) {
  const headers = {
    'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
  };

  if (json) headers['Content-Type'] = 'application/json';

  try {
    const response = await fetch(`${serviceEndpointURL}/api/${endpoint}`, {
      method: 'POST',
      headers,
      body: json ? JSON.stringify(payload) : payload,
    });

    if (!response.ok) throw new Error(`POST: Request failed endpoint ${endpoint}.`);

    return await response.json();
  } catch (error) {
    throw error;
  }
}

async function PUT(endpoint, payload) {
  try {
    const response = await fetch(`${serviceEndpointURL}/api/${endpoint}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) throw new Error(`PUT: Request failed endpoint ${endpoint}.`);

    return await response.json();
  } catch (error) {
    throw error;
  }
}

async function DELETE(endpoint) {
  try {
    const response = await fetch(`${serviceEndpointURL}/api/${endpoint}`, {
      method: 'DELETE',
      headers: {
        'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
      },
    });

    if (!response.ok) throw new Error(`DELETE: Request failed endpoint ${endpoint}.`);

    return await response.json();
  } catch (error) {
    throw error;
  }
}

// Requests for:
// BucketItem

async function postBucketItem(bucketItem) {
  return await POST('bucket-items', bucketItem);
}

async function getBucketItem(bucketItemID) {
  return await GET(`bucket-items/${bucketItemID}`);
}

async function getBucketItems() {
  return await GET('bucket-items');
}

async function putBucketItem(bucketItemID, bucketItem) {
  return await PUT(`bucket-items/${bucketItemID}`, bucketItem);
}

async function deleteLabel(bucketItemID) {
  return await DELETE(`bucket-items/${bucketItemID}`);
}

function getBucketItemJSON(id, title, description, bucket, dateToAccomplish, image, labels) {
  let bucketItem = {
    title: title,
    description: description,
    bucket: {
      id: parseInt(bucket),
    },
    image: null,
    labels: null,
    dateToAccomplish: dateToAccomplish,
  };

  if (id !== null) bucketItem.id = id;
  if (image !== null) bucketItem.image = { id: image };
  if (labels !== null)
    bucketItem.labels = labels.map((label) => {
      return { id: label };
    });

  return bucketItem;
}

// Requests for:
// Bucket

async function postBucket(bucket) {
  return await POST('buckets', bucket);
}

async function getBucket(bucketID) {
  return await GET(`buckets/${bucketID}`);
}

async function getBuckets() {
  return await GET('buckets');
}

async function putBucket(bucketID, bucket) {
  return await PUT(`buckets/${bucketID}`, bucket);
}

async function deleteBucket(bucketID) {
  return await DELETE(`buckets/${bucketID}`);
}

function getBucketJSON(id, name, color) {
  if (id === null) {
    return {
      name: name,
      color: color,
    };
  }
  return {
    id: id,
    name: name,
    color: color,
  };
}

// Requests for:
// Label

async function postLabel(label) {
  return await POST('labels', label);
}

async function getLabel(labelID) {
  return await GET(`labels/${labelID}`);
}

async function getLabels() {
  return await GET('labels');
}

async function putLabel(labelID, label) {
  return await PUT(`labels/${labelID}`, label);
}

async function deleteLabel(labelID) {
  return await DELETE(`labels/${labelID}`);
}

function getLabelJSON(id, name) {
  if (id === null) {
    return {
      name: name,
      color: 'black',
    };
  }
  return {
    id: id,
    name: name,
    color: 'black',
  };
}

async function postImage(data) {
  const payload = new FormData();

  for (const name in data) {
    payload.append(name, data[name]);
  }

  return await POST('images', payload, false);
}
