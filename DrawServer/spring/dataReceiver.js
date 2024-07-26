// dataReceiver.js
const express = require("express");
const router = express.Router();
const { GameRoomStatus } = require("../model/gameDTO.js");
const { addGameRoomStatus } = require("../socket/roomHandler.js");

router.post("/receive-data", (req, res) => {
    const data = req.body;
    console.log(data);
    const gameRoom = data.gameRoom;
    const settings = data.gameSetting;
    const subjects = data.subjectList;

    const gameRoomStatus = new GameRoomStatus(gameRoom, settings, subjects);

    console.log("게임방 생성:", gameRoomStatus.gameRoomId);
    res.send("Data received");

    addGameRoomStatus(gameRoomStatus);
});

module.exports = router;
