class GameRoomStatus {
    constructor(gameRoom, settings, subjects) {
        this.roundResults = [];
        //playerDTO
        this.players = null;

        this.gameRoomId = gameRoom.gameRoomId;
        this.gameRoomTitle = gameRoom.title;

        this.playerCount = 0;
        this.currentRound = 1;
        this.currentTurnIndex = 0;
        this.drawingData = [];

        this.subjects = subjects.map(
            (subj) => new Subject(subj.subjectId, subj.category, subj.subject)
        );

        this.gameSetting = settings;
        this.startTime = new Date();
        this.roundStartTime = new Date();
    }

    addRoundResult(winRole, image) {
        const activePlayers = this.players.filter(
            (player) => player.gameRole !== null
        );
        const currentSubject = this.subjects[this.currentRound - 1];

        this.roundResults.push(
            new RoundResult(
                this.currentRound,
                activePlayers,
                currentSubject,
                winRole,
                image,
                this.roundStartTime
            )
        );
        this.drawingData = [];
        this.roundStartTime = new Date();
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
}

class RoundResult {
    constructor(roundNumber, players, subject, winRole, image, startTime) {
        this.players = players;
        this.subject = subject;
        this.roundNumber = roundNumber;
        this.winRole = winRole;
        this.image = image;
        this.startTime = startTime;
        this.endTime = new Date();
    }
}

class GameSetting {
    constructor(
        version,
        turnTimeLimit,
        roundTimeLimit,
        minPeople,
        maxPeople,
        round = null
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
    constructor(userId, nickName, profileImage, vicCnt, gameCnt) {
        this.userId = userId;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.vicCnt = vicCnt;
        this.gameCnt = gameCnt;
    }
    curScore = 0;
    color = null;
    turn = null;
    gameRole = null;
    socketId = null;
}

module.exports = {
    PlayerDTO,
    GameRoomStatus,
    RoundResult,
    GameSetting,
    Subject,
};
