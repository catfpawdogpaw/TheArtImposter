const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors");
const { setupSocket } = require("./socketHandler.js");
const dataReceiver = require("./spring/dataReceiver.js");
const { redis, findRedisDataByKey } = require("./config/redisConfig.js");

const app = express();
const server = http.createServer(app);
const io = socketIo(server, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"],
    },
});
setupSocket(io);

app.use(cors());
app.use(express.json());
redis.connect();

app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send("Something broke!");
});

// 데이터 수신 라우터 사용
app.use("/", dataReceiver);

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`서버가 시작되었습니다. http://localhost:${PORT}`);
});

app.post("/set-data", (req, res) => {
    const { key, value } = req.body;
    try {
        const reply = redis.set(key, value);
        res.json({ message: "Data saved successfully", reply });
    } catch (err) {
        console.error("Redis error:", err);
        res.status(500).json({ error: err.message });
    }
});

app.get("/get-data/:key", async (req, res) => {
    const key = req.params.key;
    try {
        const data = await findRedisDataByKey(key);
        if (data) {
            res.json({ data });
        } else {
            res.status(404).json({ message: "Data not found" });
        }
    } catch (err) {
        res.status(500).json({ error: "Internal server error" });
    }
});
