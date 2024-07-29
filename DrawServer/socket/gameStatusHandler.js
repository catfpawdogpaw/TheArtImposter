const { defaultGameSet } = require("./roomHandler");
const { updateRedisRoomStatus } = require("../config/redisConfig");

function gameStatusHandler(io, socket, GameRoomStatus) {
    // 이미 게임 진행중이라면 대기
    if (GameRoomStatus.currentTurnIndex != 0) {
        return;
    }

    //사람이 들어올 때 최소인원 체크 후 최초 게임시작
    if (GameRoomStatus.players.length >= GameRoomStatus.gameSetting.minPeople) {
        console.log(`${GameRoomStatus.gameRoomTitle} 방 게임 시작 준비 중...`);
        setTimeout(
            () => startGame(io, socket, GameRoomStatus),
            defaultGameSet.GAME_START_DELAY
        );
    }
}

async function startGame(io, socket, GameRoomStatus) {
    GameRoomStatus.currentTurnIndex = 1;
    console.log(`${GameRoomStatus.gameRoomTitle} 방 게임 시작`);

    //색, 역할, 순서 정하기
    randomRole(GameRoomStatus);

    // 각 플레이어에게 정보 전송
    sendRoletoPlayers(io, GameRoomStatus);

    // 턴 시작
    // startTurns(io, roomId, GameRoomStatus);
}

module.exports = { gameStatusHandler };

function randomRole(GameRoomStatus) {
    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }
    // 역할 배정, 순서, 색깔 섞기
    const fakeArtistIndex = Math.floor(
        Math.random() * GameRoomStatus.players.length
    );
    const roles = GameRoomStatus.players.map((_, index) =>
        index === fakeArtistIndex ? "fake_artist" : "artist"
    );
    const turn = shuffle([...Array(GameRoomStatus.players.length).keys()]);
    const colors = shuffle(defaultGameSet.COLORS).slice(
        0,
        GameRoomStatus.players.length
    );
    // 역할 배정
    GameRoomStatus.players.forEach((player, index) => {
        player.color = colors[index];
        player.turn = turn[index];
        player.gameRole = roles[index];
    });
}

function sendRoletoPlayers(io, GameRoomStatus) {
    //주제 설정
    const subject = GameRoomStatus.getSubject();
    const category = subject.category;
    const keyword = subject.subject;
    // 각 플레이어에게 정보 전송
    GameRoomStatus.players.forEach((player, playerIndex) => {
        const myInfo = {
            role: player.gameRole,
            turn: player.turn,
            color: player.color,
            category: category,
            keyword: player.gameRole === "fake_artist" ? "???" : keyword,
        };

        const otherPlayersInfo = GameRoomStatus.players
            .map((otherPlayer, index) => {
                if (index === playerIndex) return null;
                return {
                    userId: otherPlayer.userId,
                    nickname: otherPlayer.nickName,
                    turn: otherPlayer.turn,
                    color: otherPlayer.color,
                };
            })
            .filter((info) => info !== null); // null 제거

        io.to(player.socketId).emit("gameInfo", {
            myInfo: myInfo,
            otherPlayers: otherPlayersInfo,
        });
    });

    updateRedisRoomStatus(GameRoomStatus);
}
