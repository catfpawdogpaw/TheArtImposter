const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors");
const setupSocket = require("./socketHandler.js");
const dataReceiver = require("./spring/dataReceiver");

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

// app.get("/", (req, res) => {
//     res.send(result);
// });
// app.use("/board", boardRouter);