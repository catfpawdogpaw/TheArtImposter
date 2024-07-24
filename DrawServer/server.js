const express = require("express");
const http = require("http");
const socketIo = require("socket.io");
const cors = require("cors");
const { initializeDatabase, con } = require("./DBconfig.js");
const setupSocket = require("./socketHandler.js");

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

initializeDatabase().catch(console.error);

app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send("Something broke!");
});

const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`서버가 시작되었습니다. http://localhost:${PORT}`);
});

// app.get("/", (req, res) => {
//     res.send(result);
// });
// app.use("/board", boardRouter);
