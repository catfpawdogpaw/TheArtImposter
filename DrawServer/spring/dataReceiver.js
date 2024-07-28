// dataReceiver.js
const express = require("express");
const router = express.Router();
const { PlayerDTO, GameRoomStatus } = require("../model/gameDTO.js");
const { Rooms } = require("../socketHandler.js");

router.post("/receive-data", (req, res) => {
    const data = req.body;

    const players = data.playerList.map(
        (player) =>
            new PlayerDTO(
                player.playerId,
                player.nickName,
                player.profileImage,
                player.vicCnt,
                player.gameCnt,
                player.userId
            )
    );
    const gameRoom = data.gameRoom;
    const settings = data.gameSetting;
    const subjects = data.subjectList;

    const gameRoomStatus = new GameRoomStatus(
        players,
        gameRoom,
        settings,
        subjects
    );
    console.log("Received GameRoomStatus data:", gameRoomStatus);
    res.send("Data received");

    Rooms[gameRoom.id] = gameRoomStatus;
});

module.exports = router;
