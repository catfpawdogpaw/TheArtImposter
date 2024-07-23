const express = require("express");
const boardRouter = require("./route/board.js");
const { initializeDatabase } = require("./config.js");

const app = express();
initializeDatabase().catch(console.error);

const server = app.listen(process.env.PORT || 3000, listen);

function listen() {
    let host = server.address().address;
    let port = server.address().port;
    console.log("서버가 시작되었습니다. http://" + host + ":" + port);
}

app.get("/", (req, res) => {
    res.send("hello main");
});

app.use("/board", boardRouter);
