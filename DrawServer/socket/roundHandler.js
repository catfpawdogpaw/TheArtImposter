const { updateRedisRoomStatus } = require("../config/redisConfig");
const { addDrawListeners, removeDrawListeners } = require("./drawingHandler");
const { defaultGameSet } = require("./roomHandler");
const { createCanvas } = require("canvas");
const fs = require("fs");

const roundHandler = {
    async startRound(io, socket, GameRoomStatus) {
        while (GameRoomStatus.currentRound <= GameRoomStatus.gameSetting.round) {
            //그림판 초기화
            io.to(GameRoomStatus.gameRoomTitle).emit("clearCanvas");
            //색, 역할, 순서 정하기
            randomRole(GameRoomStatus);
            // 각 플레이어에게 정보 전송
            sendRoletoPlayers(io, GameRoomStatus);
            GameRoomStatus.roundStartTime = new Date();

            roundStartlog(GameRoomStatus);

            // 턴 시작
            await startTurns(io, socket, GameRoomStatus);

            await stepInterval();
            // 투표 단계
            const voteResult = await voteStep(io, socket, GameRoomStatus);

            // 라운드 결과 발표
            const winner = await announceRoundResult(io, GameRoomStatus, voteResult);

            // 라운드 마무리
            await finalizeRound(io, GameRoomStatus, winner);

            // 다음 라운드 준비
            GameRoomStatus.currentRound++;
            updateRedisRoomStatus(GameRoomStatus);
        }
    },
};

async function startTurns(io, socket, GameRoomStatus) {
    return new Promise((resolve) => {
        const players = GameRoomStatus.players.filter((player) => player.gameRole);
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
            console.log(`${currentPlayer.nickName}님의 차례 시작. (턴 ${currentPlayerIndex + 1} / ${players.length} )`);

            let turnTimer = setTimeout(
                () => endTurn(),
                GameRoomStatus.gameSetting.turnTimeLimit * 1000
                // 100
            );

            const playerSocket = io.sockets.sockets.get(currentPlayer.socketId);

            // 리스너 추가
            if (playerSocket) {
                addDrawListeners(io, playerSocket, GameRoomStatus);
                playerSocket.on("drawEnd", handleDrawEnd);
            }

            function handleDrawEnd() {
                clearTimeout(turnTimer);
                endTurn();
            }

            function endTurn() {
                io.to(GameRoomStatus.gameRoomTitle).emit("turnEnd", {
                    userId: currentPlayer.userId,
                    nickname: currentPlayer.nickName,
                });
                if (playerSocket) {
                    removeDrawListeners(playerSocket);
                    playerSocket.removeListener("drawEnd", handleDrawEnd);
                }

                currentPlayerIndex++;
                nextTurn();
            }
        }

        // 첫 턴 시작
        nextTurn();
    });
}

async function voteStep(io, socket, GameRoomStatus) {
    return new Promise((resolve) => {
        const votes = {};
        let votedCount = 0;
        // 투표 시간 제한
        let timeoutOccurred = false;
        const voteTimeout = setTimeout(() => {
            timeoutOccurred = true;
            checkVotesComplete();
        }, defaultGameSet.VOTE_TIME * 1000);

        console.log("투표 단계 시작");
        io.to(GameRoomStatus.gameRoomTitle).emit("voteStart", {
            timeLimit: defaultGameSet.VOTE_TIME,
        });

        // 각 플레이어의 소켓에 투표 이벤트 리스너 추가
        GameRoomStatus.players.forEach((player) => {
            const playerSocket = io.sockets.sockets.get(player.socketId);
            if (playerSocket) {
                playerSocket.on("vote", (votedForId) => handleVote(votedForId));
            }
        });

        function handleVote(votedForId) {
            player = GameRoomStatus.players.find((player) => player.socketId === socket.id);
            if (player.userId !== votedForId && !votes[player.userId]) {
                votes[player.userId] = votedForId;
                votedCount++;
                checkVotesComplete();
            }
        }

        function checkVotesComplete() {
            // 투표 했는지 or 시간이 지났는지 확인 후 투표종합
            if (votedCount === GameRoomStatus.players.filter((player) => player.gameRole).length || timeoutOccurred) {
                clearTimeout(voteTimeout);
                removeVoteListeners();
                handleAbstentions();
                countVotes();
            }
        }

        function removeVoteListeners() {
            GameRoomStatus.players.forEach((player) => {
                const playerSocket = io.sockets.sockets.get(player.socketId);
                if (playerSocket) {
                    playerSocket.removeAllListeners("vote");
                }
            });
        }
        //기권처리
        function handleAbstentions() {
            GameRoomStatus.players
                .filter((player) => player.gameRole)
                .forEach((player) => {
                    if (!votes[player.userId]) {
                        votes[player.userId] = "abstain";
                    }
                });
        }

        function countVotes() {
            io.to(GameRoomStatus.gameRoomTitle).emit("voteEnd", {});
            const voteCounts = {};
            let abstentionCount = 0;

            Object.values(votes).forEach((votedId) => {
                if (votedId === "abstain") {
                    abstentionCount++;
                } else {
                    voteCounts[votedId] = (voteCounts[votedId] || 0) + 1;
                }
            });

            const sortedCandidates = Object.entries(voteCounts).sort((a, b) => b[1] - a[1]);
            resolve({ sortedCandidates, abstentionCount });
        }
    });
}

async function announceRoundResult(io, GameRoomStatus, voteResult) {
    return new Promise((resolve) => {
        const { sortedCandidates, abstentionCount } = voteResult;

        const voteCountMap = Object.fromEntries(sortedCandidates);

        //전체 투표결과
        const voteResults = GameRoomStatus.players.map((player) => {
            const votes = voteCountMap[player.userId] || 0;
            return {
                username: player.nickName,
                votes: votes,
                role: player.gameRole,
            };
        });
        voteResults.sort((a, b) => b.votes - a.votes);

        // console.log("Vote results: " + JSON.stringify(voteResults, null, 2));

        io.to(GameRoomStatus.gameRoomTitle).emit("voteResult", {
            voteResults: voteResults,
            abstentionCount: abstentionCount,
        });

        const topVotes = voteResults[0]?.votes || 0;
        const mostVotedPlayers = voteResults.filter((player) => player.votes === topVotes);

        if (mostVotedPlayers.length > 1 || topVotes === 0) {
            console.log("동점자 발생 또는 모두 기권. 가짜 예술가의 승리입니다.");
            endRound("fake_artist");
        } else {
            const mostVotedPlayer = mostVotedPlayers[0];
            if (mostVotedPlayer.role === "fake_artist") {
                // 가짜 예술가의 주제 맞추기 단계
                // startFakeArtistGuess(mostVotedPlayer);
                endRound("artist");
            } else {
                // 가짜 예술가의 승리
                endRound("fake_artist");
            }
        }

        function endRound(winner) {
            io.to(GameRoomStatus.gameRoomTitle).emit("roundEnd", {
                winner: winner,
            });
            console.log("라운드 종료 승리 역할:" + winner);
            resolve(winner);
        }
    });
}

async function finalizeRound(io, GameRoomStatus, winner) {
    //점수 반영
    if (winner === "artist") {
        GameRoomStatus.players.forEach((player) => {
            if (player.gameRole === "artist") {
                player.curScore += 2;
            }
        });
    } else if (winner === "fake_artist") {
        const fakeArtist = GameRoomStatus.players.find((player) => player.gameRole === "fake_artist");
        if (fakeArtist) {
            fakeArtist.curScore += 3;
        }
    }

    // 라운드 결과 저장
    const roundImage = await createRoundImage(GameRoomStatus.drawingData);
    GameRoomStatus.addRoundResult(winner, roundImage);

    console.log(`Round ${GameRoomStatus.currentRound} finalized. Winner: ${winner}`);
    console.log("Updated scores:", GameRoomStatus.players.map((p) => `${p.nickName}: ${p.curScore}`).join(", "));
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
    const fakeArtistIndex = Math.floor(Math.random() * GameRoomStatus.players.length);
    const roles = GameRoomStatus.players.map((_, index) => (index === fakeArtistIndex ? "fake_artist" : "artist"));
    const turn = shuffle([...Array(GameRoomStatus.players.length).keys()]);
    const colors = shuffle(defaultGameSet.COLORS).slice(0, GameRoomStatus.players.length);
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
async function stepInterval() {
    return new Promise((resolve) => {
        setTimeout(resolve, defaultGameSet.STEP_INTERVAL * 1000);
    });
}

async function createRoundImage(drawingData) {
    width = defaultGameSet.CANVAS_WIDTH;
    height = defaultGameSet.CANVAS_HEIGHT;

    const canvas = createCanvas(width, height);
    const ctx = canvas.getContext("2d");
    ctx.fillStyle = defaultGameSet.CANVAS_FILLSTYLE;
    ctx.fillRect(0, 0, width, height);
    ctx.lineCap = "round";
    ctx.lineJoin = "round";

    // 그림 그리기
    drawingData.forEach((data) => {
        ctx.strokeStyle = data.color;
        ctx.lineWidth = data.lineWidth;
        ctx.beginPath();
        ctx.moveTo(data.prevX, data.prevY);
        ctx.lineTo(data.x, data.y);
        ctx.stroke();
    });

    return canvas.toDataURL("image/png");
}

function roundStartlog(GameRoomStatus) {
    const round = GameRoomStatus.currentRound;
    const subject = GameRoomStatus.getSubject();
    const category = subject.category;
    const word = subject.subject;
    console.log(`라운드 ${round} 시작. 단어:${word} 주제:${category}`);
}

module.exports = { roundHandler };
