// dataReceiver.js
const express = require("express");
const router = express.Router();
const {
    PlayerDTO,
    GameRoomStatus,
    RoundResult,
} = require("../model/gameDTO.js");

router.post("/receive-data", (req, res) => {
    const data = req.body;
    // console.log("Received data:", data);

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
    const gameRoom = { id: data.gameRoomId, title: data.gameRoomTitle };
    const settings = {
        turnTimeLimit: data.turnTimeLeft,
        roundTimeLimit: data.roundTimeLimit,
        version: data.gameVersion,
    };
    const subjects = data.subjects;

    const gameRoomStatus = new GameRoomStatus(
        players,
        gameRoom,
        settings,
        subjects
    );

    console.log("Received GameRoomStatus data:", gameRoomStatus.toJSON());

    res.send("Data received");
});

module.exports = router;
