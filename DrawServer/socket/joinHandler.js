const drawingHandler = require("./drawingHandler");
const { gameStatusHandler } = require("./gameStatusHandler");
const { getGameRoomStatus, getRoomStatus } = require("./roomHandler");
const { testPlayerDTO } = require("../model/gameDTO");
const { validateToken, updateRedisRoomStatus } = require("../config/redisConfig");

const chatHandler = require("./chatHandler"); //chatHandler 추가

async function joinHandler(io, socket) {
    socket.on("joinRoom", async (roomTitle, userId, refreshToken) => {
        // redis jwt토큰 있는지 검증후 해당유저정보 가져오기
        console.log(roomTitle + "  " + userId + " ");
        //const player = await validateToken(userId, refreshToken, socket);
        const player = testPlayerDTO();
        if (!player) {
            return;
        }
        player.socketId = socket.id;

        const gameRoomStatus = checkRoomStatus(socket, roomTitle);
        if (!gameRoomStatus) {
            return;
        }

        gameRoomStatus.addPlayer(player);
        io.to(roomTitle).emit("playerJoined", player);
        socket.emit("initDrawing", gameRoomStatus.drawingData);
        socket.join(roomTitle);

        //턴, 게임관리
        gameStatusHandler(io, socket, gameRoomStatus);
        chatHandler(io, socket, gameRoomStatus);
        loadGameRoomInfo(gameRoomStatus);
        updateRedisRoomStatus(gameRoomStatus);
        sendGameRoomStatus(socket, gameRoomStatus);

        socket.on("disconnect", () => {
            console.log(`${player.nickName}님이 게임을 떠났습니다.`);
            if (gameRoomStatus) {
                gameRoomStatus.leavePlayer(player.socketId);
                io.to(roomTitle).emit("playerDisconnected", player);
                updateRedisRoomStatus(gameRoomStatus);
            }
        });

        return gameRoomStatus;
    });
}

function loadGameRoomInfo(gameRoomStatus) {
    console.log(gameRoomStatus.gameRoomTitle + "접속");
    console.log(
        `방상태: 
            방 ID: ${gameRoomStatus.gameRoomId}
            방 제목: ${gameRoomStatus.gameRoomTitle}
            현재 인원: ${gameRoomStatus.playerCount}명
            현재 라운드: ${gameRoomStatus.currentRound}
            현재 턴: ${gameRoomStatus.currentTurnIndex + 1}번째 플레이어
            
            플레이어 목록:
            ${gameRoomStatus.players
                .map((player) => `userId: ${player.userId}, 닉네임: ${player.nickName}, socketId: ${player.socketId}`)
                .join("\n       ")}`
    );
}

function checkRoomStatus(socket, roomTitle) {
    const gameRoomStatus = getGameRoomStatus(roomTitle);
    isJoinable = true;
    if (!gameRoomStatus) {
        socket.emit("error", { message: "해당하는 방이 없습니다." });
        console.log(`${roomTitle}에 해당하는 방이 없습니다`);
        isJoinable = false;
    } else if (gameRoomStatus.players.length >= gameRoomStatus.gameSetting.maxPeople) {
        socket.emit("error", { message: "방이 가득 찼습니다." });
        console.log(`${roomTitle} 방이 가득 찼습니다.`);
        socket.disconnect();
        isJoinable = false;
    }

    if (!isJoinable) {
        socket.disconnect();
        return;
    }
    return gameRoomStatus;
}

function sendGameRoomStatus(socket, gameRoomStatus) {

  socket.on("GameRoomStatus", () => {
    socket.emit("GameRoomStatus", getRoomStatus(gameRoomStatus));
  });
  socket.emit("GameRoomStatus", getRoomStatus(gameRoomStatus));

  function getRoomStatus(gameRoomStatus) {
    const { roundResults, drawingData, ...status } = gameRoomStatus;
    return status;
  }
}

module.exports = joinHandler;
