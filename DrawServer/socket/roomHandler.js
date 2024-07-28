/**
/* 'roomid': GameRoomStatus.
 * @type {Object.<string, GameRoomStatus>}
 */
const Rooms = new Map(); // 방 상태를 관리

function getGameRoomStatus(roomId) {
    return Rooms.get(roomId);
}

function addGameRoomStatus(gameRoomStatus) {
    Rooms.set(gameRoomStatus.gameRoomId, gameRoomStatus);
}

function deleteGameRoomStatus(roomId) {
    return Rooms.delete(roomId);
}

function getAllRoomIds() {
    return Array.from(Rooms.keys());
}
module.exports = {
    getGameRoomStatus,
    addGameRoomStatus,
    deleteGameRoomStatus,
    getAllRoomIds,
};
