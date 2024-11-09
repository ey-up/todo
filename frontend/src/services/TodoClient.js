import axios from "axios";
import { getToken } from "./StorageService";
import { HTTP_STATUS } from "../constant/HTTP_STATUS";


export const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
});

api.interceptors.request.use(
  (config) => {
    if(config.url.includes("todo")){
      config.headers["Authorization"] = `Bearer ${getToken()}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === HTTP_STATUS.FORBIDDEN) {
      localStorage.removeItem("token")
      window.location.reload();
    }
    return Promise.reject(error);
  }
)