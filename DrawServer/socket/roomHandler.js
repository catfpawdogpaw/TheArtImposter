/**
/* 'roomid': GameRoomStatus.
 * @type {Object.<string, GameRoomStatus>}
 */
const Rooms = new Map(); // 방 상태를 관리

function getGameRoomStatus(roomId) {
    return Rooms.get(roomId);
}

function addGameRoomStatus(gameRoomStatus) {
    Rooms.set(gameRoomStatus.gameRoomTitle, gameRoomStatus);
}

function deleteGameRoomStatus(roomId) {
    return Rooms.delete(roomId);
}

function getAllRoomIds() {
    return Array.from(Rooms.keys());
}

const defaultGameSet = {
    REDIS_EXPIRE_TIME: 1800, //30분
    GAME_START_DELAY: 3,
    VOTE_TIME: 40,
    COLORS: ["red", "blue", "green", "yellow", "orange", "purple"],
};
module.exports = {
    getGameRoomStatus,
    addGameRoomStatus,
    deleteGameRoomStatus,
    getAllRoomIds,
    defaultGameSet,
};
