const { defaultGameSet } = require("./roomHandler");
const { roundHandler } = require("./roundHandler");

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
    // endGame(io, GameRoomStatus);
    console.log("게임종료됨");

    //결과 보내기 및 모든 접속해제
}

module.exports = { gameStatusHandler };
