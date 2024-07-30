const express = require('express');
const router = express.Router();
<<<<<<< HEAD
=======
const { GameRoomStatus } = require("../model/gameDTO.js");
const {
    getGameRoomStatus,
    addGameRoomStatus,
} = require("../socket/roomHandler.js");
const { updateRedisRoomStatus } = require("../config/redisConfig.js");
>>>>>>> fc8ead5b748d10470f748dee1bfa49abaa6b668e

module.exports = (io) => {
  router.post('/receive-data', (req, res) => {
    const data = req.body;
<<<<<<< HEAD

    // 데이터 수신 확인
    console.log("Received data:", data);

    // 필요한 데이터 가공
    const processedData = {
      player: {
        playerId: data.player.playerId,
        nickName: data.player.nickName,
        profileImage: data.player.profileImage,
        vicCnt: data.player.vicCnt,
        gameCnt: data.player.gameCnt,
      },
      otherPlayerList: data.otherPlayerList.map(player => ({
        playerId: player.playerId,
        nickName: player.nickName,
        profileImage: player.profileImage
      })),
      gameRoom: data.gameRoom,
      gameSetting: data.gameSetting,
      subjectList: data.subjectList,
    };

    console.log("Processed data:", processedData);
=======
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
>>>>>>> fc8ead5b748d10470f748dee1bfa49abaa6b668e

    // 프론트엔드로 전송
    io.emit('receiveData', processedData);

    res.send('Data received and processed');
  });

  return router;
};
