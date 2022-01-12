serviceEndpointURL = window.location.protocol + '//' + window.location.host;

const Request = {
  async HEAD(endpoint) {
    try {
      const response = await fetch(`${serviceEndpointURL}${endpoint}`, {
        method: 'HEAD',
      });

      if (!response.ok) {
        console.error(response);
        throw new Error(`HEAD: Request failed endpoint ${endpoint}.`);
      }

      return response;
    } catch (error) {
      throw error;
    }
  },

  async GET(endpoint) {
    try {
      const response = await fetch(`${serviceEndpointURL}${endpoint}`, {
        method: 'GET',
        headers: {
          'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
        },
      });

      if (!response.ok) {
        console.error(response);
        throw new Error(`GET: Request failed endpoint ${endpoint}.`);
      }

      return await response.json();
    } catch (error) {
      throw error;
    }
  },

  async POST(endpoint, payload, json = true, jsonFeedback = true) {
    const headers = {
      'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
    };

    if (json) headers['Content-Type'] = 'application/json';

    try {
      const response = await fetch(`${serviceEndpointURL}${endpoint}`, {
        method: 'POST',
        headers,
        body: json ? JSON.stringify(payload) : payload,
      });

      if (!response.ok) {
        console.error(response);
        throw new Error(`POST: Request failed endpoint ${endpoint}.`);
      }

      return jsonFeedback ? await response.json() : response;
    } catch (error) {
      throw error;
    }
  },

  async PUT(endpoint, payload, jsonFeedback = true) {
    try {
      const response = await fetch(`${serviceEndpointURL}${endpoint}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        console.error(response);
        throw new Error(`PUT: Request failed endpoint ${endpoint}.`);
      }

      return jsonFeedback ? await response.json() : response;
    } catch (error) {
      throw error;
    }
  },

  async DELETE(endpoint) {
    try {
      const response = await fetch(`${serviceEndpointURL}${endpoint}`, {
        method: 'DELETE',
        headers: {
          'X-XSRF-TOKEN': getCookie('XSRF-TOKEN'),
        },
      });

      if (!response.ok) {
        console.error(response);
        throw new Error(`DELETE: Request failed endpoint ${endpoint}.`);
      }

      return response;
    } catch (error) {
      throw error;
    }
  },
};

const API = {
  async register(payload) {
    return await Request.POST('/user/register', payload, true, false);
  },
  async login(payload) {
    return await Request.POST('/login', payload, true, false);
  },

  async getProfile() {
    return await Request.GET('/profile');
  },
  async putProfile(profile) {
    return await Request.PUT('/profile', profile, false);
  },
  async postAvatarPicture(data) {
    const payload = new FormData();

    for (const name in data) {
      payload.append(name, data[name]);
    }

    return await Request.POST('/api/avatars/profile-picture', payload, false, false);
  },

  async postBucketItem(bucketItem) {
    return await Request.POST('/api/bucket-items', bucketItem);
  },
  async getBucketItem(bucketItemID) {
    return await Request.GET(`/api/bucket-items/${bucketItemID}`);
  },
  async getBucketItems(params = '') {
    return await Request.GET(`/api/bucket-items${params}`);
  },
  async putBucketItem(bucketItemID, bucketItem) {
    return await Request.PUT(`/api/bucket-items/${bucketItemID}`, bucketItem);
  },
  async deleteBucketItem(bucketItemID) {
    return await Request.DELETE(`/api/bucket-items/${bucketItemID}`);
  },

  async postBucket(bucket) {
    return await Request.POST('/api/buckets', bucket);
  },
  async getBucket(bucketID) {
    return await Request.GET(`/api/buckets/${bucketID}`);
  },
  async getBuckets() {
    return await Request.GET('/api/buckets');
  },
  async putBucket(bucketID, bucket) {
    return await Request.PUT(`/api/buckets/${bucketID}`, bucket);
  },
  async deleteBucket(bucketID) {
    return await Request.DELETE(`/api/buckets/${bucketID}`);
  },

  async postLabel(label) {
    return await Request.POST('/api/labels', label);
  },
  async getLabel(labelID) {
    return await Request.GET(`/api/labels/${labelID}`);
  },
  async getLabels() {
    return await Request.GET('/api/labels');
  },
  async putLabel(labelID, label) {
    return await Request.PUT(`/api/labels/${labelID}`, label);
  },
  async deleteLabel(labelID) {
    return await Request.DELETE(`/api/labels/${labelID}`);
  },

  async postBucketImage(data) {
    const payload = new FormData();

    for (const name in data) {
      payload.append(name, data[name]);
    }

    return await Request.POST('/api/bucket-items/images', payload, false);
  },

  async postLocation(address) {
    const payload = new FormData();

    payload.append('address', address);

    return await Request.POST('/api/locations', payload, false);
  },
  async getLocation(locationID) {
    return await Request.GET(`/api/locations/${locationID}`);
  },
  async getLocations() {
    return await Request.GET('/api/locations');
  },
};

function getBucketItemPayload(
  id,
  title,
  description,
  bucket,
  dateToAccomplish,
  dateAccomplishedOn,
  completed,
  image,
  location,
  labels
) {
  let bucketItem = {
    title: title,
    description: description,
    bucket: null,
    dateToAccomplish: dateToAccomplish,
    dateAccomplishedOn: dateAccomplishedOn,
    completed: completed,
    image: null,
    location: location,
    labels: null,
  };

  if (id !== null) bucketItem.id = id;
  if (bucket !== '0') bucketItem.bucket = { id: parseInt(bucket) };
  if (image !== null) bucketItem.image = { id: image };
  if (labels !== null)
    bucketItem.labels = labels.map((label) => {
      return { id: label };
    });

  return bucketItem;
}

function getBucketPayload(id, name, color, icon) {
  let bucket = {
    name,
    color,
  };

  if (id !== null) bucket.id = id;
  if (icon !== null) bucket.icon = icon;

  return bucket;
}

function getLabelPayload(id, name) {
  if (id === null) {
    return {
      name: name,
    };
  }
  return {
    id: id,
    name: name,
  };
}
