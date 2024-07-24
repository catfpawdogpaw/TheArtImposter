<template>
    <div>
        <div>
            <label for="room">Room:</label>
            <input v-model="room" id="room" placeholder="Enter room name" />
            <button @click="joinRoom">Join Room</button>
        </div>
        <canvas
            ref="canvas"
            @mousedown="startDrawing"
            @mousemove="draw"
            @mouseup="stopDrawing"
            @mouseleave="stopDrawing"
        ></canvas>
        <div>
            <button @click="clearCanvas">지우기</button>
            <button
                v-for="c in colors"
                :key="c"
                @click="selectColor(c)"
                :style="{ backgroundColor: c }"
            >
                {{ c }}
            </button>
        </div>
        <div><button @click="saveCanvasAsImage()">저장하기</button></div>
        <div>
            <label for="lineWidth">Line Width:</label>
            <button @click="setLineWidth(1)">1px</button>
            <button @click="setLineWidth(2)">2px</button>
            <button @click="setLineWidth(4)">4px</button>
            <button @click="setLineWidth(8)">8px</button>
            <button @click="setLineWidth(16)">16px</button>
        </div>
    </div>
</template>

<script>
import io from "socket.io-client";

export default {
    data() {
        return {
            socket: null,
            isDrawing: false,
            context: null,
            canvas: null,
            lineWidth: null,
            prevPoint: null,
            room: "",
            color: "black",
            colors: [
                "black",
                "red",
                "blue",
                "green",
                "yellow",
                "orange",
                "purple",
            ],
        };
    },
    mounted() {
        this.setupCanvas();
    },
    methods: {
        setupCanvas() {
            this.canvas = this.$refs.canvas;
            this.canvas.width = 800;
            this.canvas.height = 600;
            this.context = this.canvas.getContext("2d");
            this.context.strokeStyle = this.color;
            this.context.lineWidth = 2; // 선 굵기 설정
            this.context.lineCap = "round"; // 선 끝 모양을 둥글게 설정
            this.context.lineJoin = "round";
        },
        connectToServer() {
            this.socket = io("http://localhost:3000");
            console.log("서버에 접속하였습니다" + this.room);
            this.socket.on("draw", (data) => {
                this.drawFromServer(data);
            });
            this.socket.on("initDrawing", (data) => {
                console.log("기존그림 데이터:", data);
                this.initDrawingFromServer(data);
            });
            this.socket.on("clearCanvas", () => {
                this.clearCanvasLocal();
            });
        },
        joinRoom() {
            this.connectToServer();
            if (this.socket) {
                this.socket.emit("joinRoom", this.room);
            }
        },
        startDrawing(e) {
            this.isDrawing = true;
            const rect = this.canvas.getBoundingClientRect();
            this.prevPoint = {
                x: e.clientX - rect.left,
                y: e.clientY - rect.top,
            };
            this.context.beginPath();
            this.context.moveTo(this.prevPoint.x, this.prevPoint.y);
        },
        stopDrawing() {
            this.isDrawing = false;
            this.prevPoint = null;
        },
        draw(e) {
            if (!this.isDrawing) return;

            const rect = this.canvas.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            this.context.strokeStyle = this.color;
            this.context.lineTo(x, y);
            this.context.stroke();
            if (this.socket) {
                this.socket.emit("draw", {
                    prevX: this.prevPoint.x,
                    prevY: this.prevPoint.y,
                    x: x,
                    y: y,
                    color: this.color,
                    lineWidth: this.lineWidth,
                });
            }

            this.prevPoint = { x, y };
        },
        drawFromServer(data) {
            this.context.save();
            this.context.strokeStyle = data.color;
            this.context.lineWidth = data.lineWidth;
            this.context.beginPath();
            this.context.moveTo(data.prevX, data.prevY);
            this.context.lineTo(data.x, data.y);
            this.context.stroke();
            this.context.restore();
        },
        initDrawingFromServer(data) {
            data.forEach(this.drawFromServer);
        },
        clearCanvas() {
            this.socket.emit("clearCanvas");
            this.clearCanvasLocal();
        },
        clearCanvasLocal() {
            this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        },
        selectColor(color) {
            this.color = color;
        },
        setLineWidth(width) {
            this.lineWidth = width;
        },
        saveCanvasAsImage() {
            const dataURL = this.canvas.toDataURL("image/png");

            // 이미지 다운로드 링크 생성
            const link = document.createElement("a");
            link.href = dataURL;
            link.download = "drawing.png"; // 다운로드할 파일의 이름
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        },
    },
};
</script>

<style scoped>
canvas {
    border: 1px solid black;
    background-color: ivory;
}
</style>
