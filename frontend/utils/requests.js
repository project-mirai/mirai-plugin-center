const APP_BASE_API_URL = "http://103.39.216.9:9090/"
// create an axios instance
const service = axios.create({
  baseURL: APP_BASE_API_URL, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 20000 // request timeout
})
