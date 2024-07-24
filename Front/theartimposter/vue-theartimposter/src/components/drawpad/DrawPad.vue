<template>
    <div>
        <canvas
            ref="canvas"
            @mousedown="startDrawing"
            @mousemove="draw"
            @mouseup="stopDrawing"
            @mouseleave="stopDrawing"
        ></canvas>
        <button @click="clearCanvas">Clear Canvas</button>
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
        };
    },
    mounted() {
        this.setupCanvas();
        this.connectToServer();
    },
    methods: {
        setupCanvas() {
            this.canvas = this.$refs.canvas;
            this.canvas.width = 800;
            this.canvas.height = 600;
            this.context = this.canvas.getContext("2d");
            this.context.strokeStyle = "#000000";
            this.context.lineWidth = 2; // 선 굵기 설정
            this.context.lineCap = "round"; // 선 끝 모양을 둥글게 설정
            this.context.lineJoin = "round";
        },
        connectToServer() {
            this.socket = io("http://localhost:3000");
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

            this.context.lineTo(x, y);
            this.context.stroke();
            this.socket.emit("draw", {
                prevX: this.prevPoint.x,
                prevY: this.prevPoint.y,
                x: x,
                y: y,
            });

            this.prevPoint = { x, y };
        },
        drawFromServer(data) {
            this.context.beginPath();
            this.context.moveTo(data.prevX, data.prevY);
            this.context.lineTo(data.x, data.y);
            this.context.stroke();
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
    },
};
</script>

<style scoped>
canvas {
    border: 1px solid black;
}
</style>
