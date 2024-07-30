const { updateRedisRoomStatus } = require("../config/redisConfig");
const { defaultGameSet } = require("./roomHandler");

const roundHandler = {
    async startRound(io, GameRoomStatus) {
        while (
            GameRoomStatus.currentRound <= GameRoomStatus.gameSetting.round
        ) {
            //색, 역할, 순서 정하기
            randomRole(GameRoomStatus);
            // 각 플레이어에게 정보 전송
            sendRoletoPlayers(io, GameRoomStatus);

            console.log(`라운드 ${GameRoomStatus.currentRound} 시작`);

            // 턴 시작
            await startTurns(io, GameRoomStatus);

            // 투표 단계
            await voteStep(io, GameRoomStatus);

            // 라운드 결과 발표
            // await announceRoundResult(io, GameRoomStatus);

            // 다음 라운드 준비
            GameRoomStatus.currentRound++;
            updateRedisRoomStatus(GameRoomStatus);
        }
    },
};

async function startTurns(io, GameRoomStatus) {
    return new Promise((resolve) => {
        const players = GameRoomStatus.players;
        const turnOrder = players.sort((a, b) => a.turn - b.turn);
        let currentPlayerIndex = 0;

        async function nextTurn() {
            // 모든 사람의 턴이 끝났는지 확인
            if (currentPlayerIndex >= players.length) {
                resolve();
                return;
            }

            // 현재 순서 플레이어
            const currentPlayer = turnOrder[currentPlayerIndex];
            GameRoomStatus.currentTurnIndex = currentPlayer.turn;

            // 턴 타이머 시작
            io.to(GameRoomStatus.gameRoomTitle).emit("turnStart", {
                userId: currentPlayer.userId,
                nickname: currentPlayer.nickName,
                timeLimit: GameRoomStatus.gameSetting.turnTimeLimit,
            });

            let turnTimer = setTimeout(
                () => endTurn(),
                GameRoomStatus.gameSetting.turnTimeLimit * 1000
            );

            function handleDrawEnd() {
                clearTimeout(turnTimer);
                endTurn();
            }

            function endTurn() {
                io.to(GameRoomStatus.gameRoomTitle).emit("turnEnd", {
                    userId: currentPlayer.userId,
                    nickname: currentPlayer.nickName,
                });

                // 턴 종료 후 리스너 제거
                const playerSocket = io.sockets.sockets.get(
                    currentPlayer.socketId
                );
                if (playerSocket) {
                    playerSocket.removeListener("drawEnd", handleDrawEnd);
                }

                currentPlayerIndex++;
                nextTurn();
            }

            // 현재 플레이어에게 종료 이벤트 리스너 추가
            const playerSocket = io.sockets.sockets.get(currentPlayer.socketId);
            if (playerSocket) {
                playerSocket.on("drawEnd", handleDrawEnd);
            }
        }

        // 첫 턴 시작
        nextTurn();
    });
}

async function voteStep(io, GameRoomStatus) {
    console.log("투표 단계 시작");
}

async function announceRoundResult(io, GameRoomStatus) {
    // 라운드 결과 발표
    // 결과 GameRoomStatus.roundResults에 저장
    console.log(`라운드 ${GameRoomStatus.currentRound} 결과 발표`);
}

function endGame(io, GameRoomStatus) {
    console.log("게임 종료");
    io.to(GameRoomStatus.gameRoomId).emit("gameEnd", {
        message: "게임이 종료되었습니다.",
    });
    // 게임 종료 후 처리 로직 (예: 최종 점수 계산, 결과 저장 등)
}

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

module.exports = { roundHandler };
