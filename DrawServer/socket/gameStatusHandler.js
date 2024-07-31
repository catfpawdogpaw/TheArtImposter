const { defaultGameSet } = require("./roomHandler");
const { roundHandler } = require("./roundHandler");
const { deleteGameRoomStatus } = require("../socket/roomHandler");
const axios = require("axios");

function gameStatusHandler(io, socket, GameRoomStatus) {
    // 이미 게임 진행중이라면 대기
    if (GameRoomStatus.currentTurnIndex != 0) {
        return;
    }

    //사람이 들어올 때 최소인원 체크 후 최초 게임시작
    if (GameRoomStatus.players.length >= GameRoomStatus.gameSetting.minPeople) {
        console.log(`${GameRoomStatus.gameRoomTitle} 방 게임 시작 준비 중...`);
        setTimeout(() => startGame(io, socket, GameRoomStatus), defaultGameSet.GAME_START_DELAY * 1000);
    }
}

async function startGame(io, socket, GameRoomStatus) {
    GameRoomStatus.currentTurnIndex = 1;
    console.log(`${GameRoomStatus.gameRoomTitle} 방 게임 시작`);

    //라운드만큼 반복
    await roundHandler.startRound(io, socket, GameRoomStatus);

    // 모든 라운드 끝내고 게임종료
    endGame(io, socket, GameRoomStatus);
    console.log("게임종료됨");

    //결과 보내기 및 모든 접속해제
    resultStatistics(io, socket, GameRoomStatus);
}

async function endGame(io, socket, GameRoomStatus) {
    // 최종 점수 계산
    const finalScores = GameRoomStatus.players
        .map((player) => ({
            userId: player.userId,
            nickname: player.nickName,
            score: player.curScore,
        }))
        .sort((a, b) => b.score - a.score);

    // 클라이언트에게 최종 결과 전송
    io.to(GameRoomStatus.gameRoomTitle).emit("gameEnd", {
        finalScores: finalScores,
    });
}

async function resultStatistics(io, socket, GameRoomStatus) {
    GameRoomStatus.endTime = new Date();
    // 스프링 서버에 GameRoomStatus 객체 전송
    try {
        await axios.post(`http://${process.env.SPRING_HOST}:${process.env.SPRING_PORT}/Nodejs/result`, GameRoomStatus);
    } catch (error) {
        console.error("스프링 서버에 게임 결과 전송 실패:", error);
    }

    //서버 연결해제
    GameRoomStatus.players.forEach((player) => {
        const playerSocket = io.sockets.sockets.get(player.socketId);
        if (playerSocket) {
            playerSocket.disconnect();
        }
    });

    //방 삭제
    console.log(`${GameRoomStatus.gameRoomTitle} 방 게임 종료 및 정리 완료`);
    deleteGameRoomStatus(GameRoomStatus.gameRoomTitle);
}

module.exports = { gameStatusHandler };
