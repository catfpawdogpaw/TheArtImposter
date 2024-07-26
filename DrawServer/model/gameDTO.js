class GameRoomStatus {
    constructor(players, gameRoom, settings, subjects) {
        this.roundResults = [];
        //playerDTO
        this.players = players;

        this.gameRoomId = gameRoom.id;
        this.gameRoomTitle = gameRoom.title;

        this.playerCount = players.length;
        this.currentRound = 1;
        this.currentTurnIndex = 0;

        this.subject = subjects.map(
            (subj) => new Subject(subj.subjectId, subj.category, subj.subject)
        );

        // maxPeople, round설정 필요
        this.gameSetting = new GameSetting(
            settings.version,
            settings.turnTimeLimit,
            settings.roundTimeLimit,
            settings.minPeople,
            8,
            3
        );

        this.startTime = new Date();
    }

    setRoundResult() {
        this.roundResults.push(new RoundResult(this.currentRound));
    }

    getRoundResult(roundIndex) {
        return this.roundResults[roundIndex - 1];
    }

    addPlayer(player) {
        this.players.push(player);
        this.playerCount++;
    }
    leavePlayer(playerId) {
        const index = this.players.findIndex(
            (player) => player.playerId === playerId
        );
        if (index === -1) {
            console.log(`${playerId}에 해당하는 유저를 찾을 수 없습니다.`);
            return;
        }
        const removedPlayer = this.players.splice(index, 1)[0];
        console.log(`${removedPlayer.nickName}님이 게임을 떠났습니다.`);
    }

    toJSON() {
        return JSON.stringify({
            players: this.players,
            gameRoomId: this.gameRoomId,
            gameRoomTitle: this.gameRoomTitle,
            subjects: this.subjects,
            playerCount: this.playerCount,
            currentRound: this.currentRound,
            currentTurnIndex: this.currentTurnIndex,
            turnOrder: this.turnOrder,
            turnTimeLeft: this.turnTimeLeft,
            roundTimeLimit: this.roundTimeLimit,
            gameVersion: this.gameVersion,
            roundResults: this.roundResults,
            startTime: this.startTime.toISOString(),
        });
    }
}

class RoundResult {
    constructor(roundNumber, players, subject, winRole, image, startTime) {
        this.this.roundNumber = roundNumber;
        this.players = players;
        this.subject = subject;
        this.winRole = winRole;
        this.image = image;
        this.startTime = startTime;
        this.endTime = new Date();
    }

    addTurn(playerId, subject, answer) {
        this.turns.push({ playerId, subject, answer });
    }

    setVotes(votes) {
        this.votes = votes;
    }

    setWinner(winnerId) {
        this.winner = winnerId;
    }
}

class GameSetting {
    constructor(
        version,
        turnTimeLimit,
        roundTimeLimit,
        minPeople,
        maxPeople,
        round
    ) {
        this.version = version;
        this.turnTimeLimit = turnTimeLimit;
        this.roundTimeLimit = roundTimeLimit;
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.round = round;
    }
}

class Subject {
    constructor(subjectId, category, subject) {
        this.subjectId = subjectId;
        this.category = category;
        this.subject = subject;
    }
}

class PlayerDTO {
    constructor(playerId, nickName, profileImage, vicCnt, gameCnt, userId) {
        this.playerId = playerId;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.vicCnt = vicCnt;
        this.gameCnt = gameCnt;
        this.userId = userId;
    }
    curScore = 0;
    color = null;
    turn = null;
    gameRole = null;
}

module.exports = {
    PlayerDTO,
    GameRoomStatus,
    RoundResult,
    GameSetting,
    Subject,
};
