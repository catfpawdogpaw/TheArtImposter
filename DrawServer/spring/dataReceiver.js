const express = require('express');
const router = express.Router();
const { GameRoomStatus } = require("../model/gameDTO.js");
const {
    getGameRoomStatus,
    addGameRoomStatus,
} = require("../socket/roomHandler.js");
const { updateRedisRoomStatus } = require("../config/redisConfig.js");


module.exports = (io) => {
  router.post('/receive-data', (req, res) => {
    const data = req.body;

    console.log(data);
    const gameRoom = data.gameRoom;
    const settings = data.gameSetting;
    const subjects = data.subjectList;

    const gameRoomStatus = new GameRoomStatus(gameRoom, settings, subjects);

    console.log("게임방 생성:", gameRoomStatus.gameRoomTitle);
    res.send("Data received");

    addGameRoomStatus(gameRoomStatus);
    console.log(
        "생성된 방정보" + getGameRoomStatus(gameRoomStatus.gameRoomTitle)
    );
    updateRedisRoomStatus(gameRoomStatus);
});

  return router;
};
