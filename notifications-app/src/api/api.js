import axios from "axios";

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
});

export const AUTH_API = axios.create({
  baseURL: import.meta.env.VITE_AUTH_URL || "http://localhost:8000/realms/dev/protocol/openid-connect/token",
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
  },
});

export const setAuthToken = (token) => {
  if (token) {
    API.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete API.defaults.headers.common["Authorization"];
  }
};

// interceptor
export const setupInterceptors = (logout) => {
  API.interceptors.response.use(
    (res) => res,
    (err) => {
      if (err.response && err.response.status === 401) {
        logout();
      }
      return Promise.reject(err);
    }
  );
};

export default API;