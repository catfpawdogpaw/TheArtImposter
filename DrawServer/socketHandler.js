const joinHandler = require("./socket/joinHandler");

function setupSocket(io) {
    io.on("connection", (socket) => {
        console.log(`소켓아이디: ${socket.id} 접속`);

        joinHandler(io, socket);

        socket.on("disconnect", () => {
            console.log(`소켓아이디: ${socket.id} 접속해제`);
        });
    });
}

//게임이 끝났을 때 스프링에 전달구현

module.exports = { setupSocket };
