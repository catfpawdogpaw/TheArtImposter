// src/plugins/axios.js
import axios from "axios";

const instance = axios.create({
    baseURL: 'http://localhost:8080/api', // 서버의 기본 URL을 설정합니다.
    headers: {
        'Content-Type': 'application/json'
    }
});

instance.interceptors.request.use(config => {
    const token = localStorage.getItem('access_token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default instance;
