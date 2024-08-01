export default {
    setupCanvas(canvas) {
        const context = canvas.getContext('2d');
        canvas.width = 800;
        canvas.height = 580;
        context.strokeStyle = 'black';
        context.lineWidth = 2;
        context.lineCap = 'round';
        context.lineJoin = 'round';
        return context;
    },

    startDrawing(e, context, canvas) {
        const rect = canvas.getBoundingClientRect();
        return {
            x: e.clientX - rect.left,
            y: e.clientY - rect.top,
        };
    },

    draw(e, context, canvas, prevPoint, color, lineWidth) {
        const rect = canvas.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        context.strokeStyle = color;
        context.lineWidth = lineWidth;
        context.beginPath();
        context.moveTo(prevPoint.x, prevPoint.y);
        context.lineTo(x, y);
        context.stroke();

        return { x, y };
    },

    drawFromServer(context, data) {
        console.log('그림 데이터 : ' + data);
        context.save();
        context.strokeStyle = data.color;
        context.lineWidth = data.lineWidth;
        context.beginPath();
        context.moveTo(data.prevX, data.prevY);
        context.lineTo(data.x, data.y);
        context.stroke();
        context.restore();
    },

    clearCanvas(context, canvas) {
        context.clearRect(0, 0, canvas.width, canvas.height);
    },

    saveCanvasAsImage(canvas) {
        const dataURL = canvas.toDataURL('image/png');
        const link = document.createElement('a');
        link.href = dataURL;
        link.download = 'drawing.png';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    },
};
