<template>
    <div>
<!--        <div>-->
<!--            <label for="room">Room:</label>-->
<!--            <input v-model="room" id="room" placeholder="Enter room name" />-->
<!--            <button @click="joinRoom">Join Room</button>-->
<!--        </div>-->
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
import drawpad from "./drawpad.js";
import drawsocket from "./drawsocket.js";

export default {
    data() {
        return {
            socket: null,
            isDrawing: false,
            context: null,
            canvas: null,
            lineWidth: 2,
            prevPoint: null,
            room: "TestRoom",
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
    this.$root.$on('joinRoom', this.joinRoom);
  },
    methods: {
        setupCanvas() {
            this.canvas = this.$refs.canvas;
            this.context = drawpad.setupCanvas(this.canvas);
        },
        connectToServer() {
            this.socket = drawsocket.connectToServer("http://localhost:3000");
            drawsocket.setupDrawingListeners(
                this.socket,
                this.drawFromServer,
                this.initDrawingFromServer,
                this.clearCanvasLocal
            );
        },
        joinRoom() {
            this.connectToServer();
            if (this.socket) {
                drawsocket.joinRoom(this.socket, this.room, null);
            }
        },
        startDrawing(e) {
            this.isDrawing = true;
            this.prevPoint = drawpad.startDrawing(e, this.context, this.canvas);
        },
        stopDrawing() {
            this.isDrawing = false;
            this.prevPoint = null;
        },
        draw(e) {
            if (!this.isDrawing) return;
            const newPoint = drawpad.draw(
                e,
                this.context,
                this.canvas,
                this.prevPoint,
                this.color,
                this.lineWidth
            );
            if (this.socket) {
                drawsocket.emitDraw(this.socket, {
                    prevX: this.prevPoint.x,
                    prevY: this.prevPoint.y,
                    x: newPoint.x,
                    y: newPoint.y,
                    color: this.color,
                    lineWidth: this.lineWidth,
                });
            }
            this.prevPoint = newPoint;
        },
        drawFromServer(data) {
            drawpad.drawFromServer(this.context, data);
        },
        initDrawingFromServer(data) {
            data.forEach(this.drawFromServer);
        },
        clearCanvas() {
            if (this.socket) {
                drawsocket.emitClearCanvas(this.socket);
            }
            this.clearCanvasLocal();
        },
        clearCanvasLocal() {
            drawpad.clearCanvas(this.context, this.canvas);
        },
        selectColor(color) {
            this.color = color;
        },
        setLineWidth(width) {
            this.lineWidth = width;
        },
        saveCanvasAsImage() {
            drawpad.saveCanvasAsImage(this.canvas);
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
