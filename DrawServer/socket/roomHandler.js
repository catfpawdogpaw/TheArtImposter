/**
/* 'roomid': GameRoomStatus.
 * @type {Object.<string, GameRoomStatus>}
 */
const Rooms = {}; // 방 상태를 관리

function getGameRoomStatus(roomId) {
    return Rooms[roomId];
}

function addGameRoomStatus(gameRoomStatus) {
    Rooms[gameRoomStatus.gameRoomId] = gameRoomStatus;
}

function deleteGameRoomStatus(roomId) {
    delete Rooms[roomId];
}

module.exports = { getGameRoomStatus, addGameRoomStatus, deleteGameRoomStatus };
