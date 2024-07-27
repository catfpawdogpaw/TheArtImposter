const redisModule = require("redis");
require("dotenv").config();

// Redis 클라이언트 생성
const redis = redisModule.createClient({
    socket: {
        host: `${process.env.REDIS_HOST}`,
        port: process.env.REDIS_PORT,
        connectTimeout: process.env.REDIS_TIMEOUT,
    },
    password: process.env.REDIS_PASSWORD,
});

// Redis 클라이언트 오류 처리
redis.on("error", (err) => {
    console.error("Redis error:", err);
});

// 클라이언트가 성공적으로 연결되었을 때
redis.on("connect", () => {
    console.log("Connected to Redis server");
});

// 클라이언트를 내보내기
module.exports = redis;
