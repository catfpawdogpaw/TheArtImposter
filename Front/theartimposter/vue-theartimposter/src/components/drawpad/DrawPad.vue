<template>
    <div>
        <canvas
            ref="canvas"
            @mousedown="startDrawing"
            @mousemove="draw"
            @mouseup="stopUpDrawing"
            @mouseleave="stopLeaveDrawing"
        ></canvas>
        <!-- <div>
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
        <div><button @click="saveCanvasAsImage()">저장하기</button></div> -->
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
import drawpad from './drawpad.js';
import { EventBus } from '@/utils/eventBus';

export default {
    name: 'DrawingComponent',
    inject: ['socket'],
    data() {
        return {
            myTurn: false,
            socket: null,
            isDrawing: false,
            context: null,
            canvas: null,
            lineWidth: 2,
            prevPoint: null,
            room: 'TestRoom',
            color: 'black',
            colors: ['black', 'red', 'blue', 'green', 'yellow', 'orange', 'purple'],
        };
    },
    created() {
        EventBus.$on('turnPlayerChanged', this.checkCanDraw); // 이벤트 수신 및 핸들러 등록
        EventBus.$on('settingGamePlayers', () => {
            console.log('settingGamePlayers - 이벤트 버스 도착 - 드로우');
            this.selectColor(this.$store.getters.getMyInfo.color);
        });
        this.socket = this.$socket;
    },
    beforeDestroy() {
        EventBus.$off('settingGamePlayers');
        EventBus.$off('turnPlayerChanged', this.checkCanDraw); // 이벤트 수신 및 핸들러 등록
    },
    mounted() {
        this.setupCanvas();
        this.setupListeners();
    },
    methods: {
        setupCanvas() {
            this.canvas = this.$refs.canvas;
            this.context = drawpad.setupCanvas(this.canvas);
        },
        setupListeners() {
            if (this.socket) {
                this.setupDrawingListeners(
                    this.socket,
                    this.drawFromServer,
                    this.initDrawingFromServer,
                    this.clearCanvasLocal,
                );
            } else {
                console.error('Socket is not available');
            }
        },
        startDrawing(e) {
            this.isDrawing = true;
            this.prevPoint = drawpad.startDrawing(e, this.context, this.canvas);
        },
        stopUpDrawing() {
            this.isDrawing = false;
            this.prevPoint = null;

            if (this.myTurn == true) {
                this.myTurn = false;
                EventBus.$emit('turnEnd'); // turnPlayer 변경 시 이벤트 발행
            }
        },
        stopLeaveDrawing() {
            this.isDrawing = false;
            this.prevPoint = null;
        },
        draw(e) {
            if (!this.isDrawing || !this.myTurn) return;

            const newPoint = drawpad.draw(e, this.context, this.canvas, this.prevPoint, this.color, this.lineWidth);
            if (this.socket) {
                this.emitDraw(this.socket, {
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
                this.emitClearCanvas(this.socket);
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
        checkCanDraw() {
            console.log('현재 턴' + this.$store.getters.getTurnPlayer.turn);
            console.log('내 턴' + this.$store.getters.getMyInfo.turn);
            if (this.$store.getters.getMyInfo.turn == this.$store.getters.getTurnPlayer.turn) {
                console.log('그린다');
                this.myTurn = true;
            } else {
                console.log('못 그린다');
                this.myTurn = false;
            }
        },
        setupDrawingListeners(socket, drawCallback, initDrawingCallback, clearCanvasCallback) {
            socket.on('draw', drawCallback);
            socket.on('initDrawing', initDrawingCallback);
            socket.on('clearCanvas', clearCanvasCallback);
        },

        emitDraw(socket, drawingData) {
            socket.emit('draw', drawingData);
        },

        emitClearCanvas(socket) {
            socket.emit('clearCanvas');
        },
    },
};
</script>

<style scoped>
canvas {
    border: 2px solid rgb(13, 18, 71);
    background-color: hsla(227, 54%, 93%, 0.849);
}
</style>
