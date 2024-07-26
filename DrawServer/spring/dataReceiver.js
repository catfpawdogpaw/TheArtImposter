const express = require('express');
const router = express.Router();

module.exports = (io) => {
  router.post('/receive-data', (req, res) => {
    const data = req.body;

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

    // 프론트엔드로 전송
    io.emit('receiveData', processedData);

    res.send('Data received and processed');
  });

  return router;
};
