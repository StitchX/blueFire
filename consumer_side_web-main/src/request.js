import axios from "axios";

const instance = axios.create({
  baseURL: "/consumer",
  timeout: 8000,
});

instance.interceptors.request.use(
  (config) => {
    return config;
  },
  (err) => {
  }
);

instance.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
  }
);

export default instance;
