// dataReceiver.js
const express = require("express");
const router = express.Router();
const { GameRoomStatus } = require("../model/gameDTO.js");
const { Rooms } = require("../socketHandler.js");

router.post("/receive-data", (req, res) => {
    const data = req.body;

    const gameRoom = data.gameRoom;
    const settings = data.gameSetting;
    const subjects = data.subjectList;
    console.log(gameRoom);

    const gameRoomStatus = new GameRoomStatus(gameRoom, settings, subjects);

    console.log("Received GameRoomStatus data:", gameRoomStatus);
    res.send("Data received");

    Rooms[gameRoom.id] = gameRoomStatus;
});

module.exports = router;
