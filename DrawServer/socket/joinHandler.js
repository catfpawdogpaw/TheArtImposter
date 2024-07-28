const drawingHandler = require("./drawingHandler");
const { getGameRoomStatus } = require("./roomHandler");
const { testPlayerDTO } = require("../model/gameDTO");
const { validateToken } = require("../config/redisConfig");

async function joinHandler(io, socket) {
    socket.on("joinRoom", async (roomId, accessToken) => {
        // redis jwt토큰 있는지 검증후 해당유저정보 가져오기
        // redis에서 유효한 방인지 검증
        // const player = await validateToken(accessToken, socket);

        const player = testPlayerDTO();
        if (!player) {
            return;
        }
        player.socketId = socket.id;

        // 생성된 GameRoomStatus의 gameRoomId에 socket.join(gameRoomId)
        // + player의 socketid 설정
        const gameRoomStatus = getGameRoomStatus(Number(roomId));
        console.log(roomId + "  " + accessToken);
        if (!gameRoomStatus) {
            socket.emit("error", { message: "해당하는 방이 없습니다." });
            console.log(`${roomId}에 해당하는 방이 없습니다`);
            socket.disconnect();
            return;
        }

        gameRoomStatus.addPlayer(player);
        socket.join(Number(roomId));

        drawingHandler(io, socket, gameRoomStatus);

        loadGameRoomInfo(gameRoomStatus);

        socket.on("disconnect", () => {
            console.log(`${player.nickName}님이 게임을 떠났습니다.`);

            if (gameRoomStatus) {
                gameRoomStatus.leavePlayer(player.socketId);

                io.to(Number(roomId)).emit("playerDisconnected", player);
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
                .map(
                    (player) =>
                        `userId: ${player.userId}, 닉네임: ${player.nickName}, socketId: ${player.socketId}`
                )
                .join("\n       ")}`
    );
}

module.exports = joinHandler;
