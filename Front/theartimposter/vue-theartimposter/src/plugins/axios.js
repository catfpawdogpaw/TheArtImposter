// src/plugins/axios.js
import axios from 'axios';
import store from '@/store/store';

const instance = axios.create({
    baseURL: 'http://192.168.230.30:8080/api', // 서버의 기본 URL을 설정합니다.
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // 모든 요청에 쿠키를 포함하도록 설정
});

// axios 인터셉터 ( 서버의 모든 요청 헤더에 access 토큰을 포함시킵니다. )
instance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('access_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

let isRefreshing = false;
let subscribers = [];

function onAccessTokenFetched(accessToken) {
    subscribers.forEach((callback) => callback(accessToken));
    subscribers = [];
}

function addSubscriber(callback) {
    subscribers.push(callback);
}

// 응답 인터셉터 추가
instance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        const { config, response } = error;
        const originalRequest = config;

        if (response && (response.status === 401 || response.status === 403) && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise((resolve) => {
                    addSubscriber((accessToken) => {
                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        resolve(instance(originalRequest));
                    });
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            return new Promise((resolve, reject) => {
                store
                    .dispatch('refreshToken')
                    .then((accessToken) => {
                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        resolve(instance(originalRequest));
                        onAccessTokenFetched(accessToken);
                    })
                    .catch((error) => {
                        reject(error);
                        store.dispatch('logout');
                    })
                    .finally(() => {
                        isRefreshing = false;
                    });
            });
        }

        return Promise.reject(error);
    },
);

export default instance;
