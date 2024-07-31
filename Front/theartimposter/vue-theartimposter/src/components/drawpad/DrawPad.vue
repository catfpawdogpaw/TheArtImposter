<template>
    <div>
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
import socketHandler from "@/components/chatting/socketHandler";

export default {
  name: 'DrawingComponent',
  inject: ['socket'],
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
    this.setupListeners();
  },
  methods: {
      setupCanvas() {
        this.canvas = this.$refs.canvas;
        this.context = drawpad.setupCanvas(this.canvas);
      },
      setupListeners() {
        if (this.socket) {
          socketHandler.setupDrawingListeners(
              this.socket(),
              this.drawFromServer,
              this.initDrawingFromServer,
              this.clearCanvasLocal
          );
        } else {
          console.error('Socket is not available');
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
            socketHandler.emitDraw(this.socket, {
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
            socketHandler.emitClearCanvas(this.socket);
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
