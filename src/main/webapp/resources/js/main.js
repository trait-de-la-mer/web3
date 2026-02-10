const scale = 80,
        sixCanv = 6,
        twoCanv = 2,
        zeroCanv = 0,
        canvases = document.getElementsByTagName('canvas'),
        canvas = canvases[zeroCanv],
        ctx = canvas.getContext('2d'),
        { height, width } = canvas,
        centerX = width / twoCanv,
        centerY = height / twoCanv;

function redrawCanvas() {
    ctx.clearRect(0, 0, width, height);
    const rSelect = document.querySelector('[id$=":r"] .ui-selectonemenu-label');
    let currentR = 1;
    
    if (rSelect && rSelect.textContent) {
        const parsedR = parseFloat(rSelect.textContent);
        if (!isNaN(parsedR)) {
            currentR = parsedR;
        }
    }
    
    drawArea(ctx, centerX, centerY, scale, currentR);
    drawAxes(ctx, width, height, centerX, centerY, scale);
}

function drawArea(ctx, cx, cy, scale, r) {
    const unit = scale / 2;
    ctx.fillStyle = 'rgba(51, 153, 255, 0.2)';
    
    // круг
    ctx.moveTo(cx, cy);
    ctx.arc(cx, cy, (unit) * r, Math.PI/2, Math.PI, false);
    ctx.fill();
    
    // прямоугольник
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.lineTo(cx + unit * r, cy);
    ctx.lineTo(cx + unit * r, cy + (unit / 2) * r);
    ctx.lineTo(cx, cy + (unit / 2) * r);
    ctx.closePath();
    ctx.fill();
    
    // треугольник  
    ctx.beginPath();
    ctx.moveTo(cx, cy);
    ctx.lineTo(cx, cy - (unit / 2) * r);
    ctx.lineTo(cx + (unit / 2) * r, cy);
    ctx.closePath();
    ctx.fill();
}
function drawAxes(ctx, width, height, centerX, centerY, scale) {
    ctx.strokeStyle = "white";
    ctx.lineWidth = 1;
    
    // Оси
    ctx.beginPath();
    ctx.moveTo(0, centerY);
    ctx.lineTo(width, centerY);
    ctx.moveTo(centerX, 0);
    ctx.lineTo(centerX, height);
    ctx.stroke();
    
    // Подписи
    const sixCanv = 6;
    ctx.font = "12px monospace";
    for (let x = -5; x <= 5; x++) {
        if (x === 0) continue;
        const px = centerX + x * (scale / 2);
        if (px >= 0 && px <= width) {
            ctx.strokeText(x.toString(), px - 4, centerY + 15);
        }
    }
    for (let y = -5; y <= 5; y++) {
        if (y === 0) continue;
        const py = centerY - y * (scale / 2);
        if (py >= 0 && py <= height) {
            ctx.strokeText(y.toString(), centerX + 6, py + 4);
        }
    }
    ctx.strokeText("0", centerX + sixCanv, centerY - sixCanv);
}

document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById('plotCanvas');
    if (canvas) {
        canvas.addEventListener('click', handleCanvasClick);
    }
});

    function drawPoint(x, y, hit) {
    const color = hit ? "green" : "red";
    const unit = scale / 2;
    
    ctx.beginPath();
    ctx.arc(centerX + x * unit, centerY - y * unit, 5, 0, Math.PI * 2);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = "black";
    ctx.lineWidth = 1;
    ctx.stroke();
}
   function smtLeft() {
    const table = document.getElementById('resultTable');
    if (!table) return;
    
    const rows = table.getElementsByTagName('tr');
    if (rows.length < 2) return;
    
    redrawCanvas();
    
    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        if (cells.length >= 4) {
            const x = parseFloat(cells[0].textContent.trim());
            const y = parseFloat(cells[1].textContent.trim().replace(',', '.'));
            const hit = cells[3].textContent.trim() === "Да";
            
            if (!isNaN(x) && !isNaN(y)) {
                drawPoint(x, y, hit);
            }
        }
    }
}


function handleCanvasClick(e) {  
    const label = document.querySelector('[id$=":r"] .ui-selectonemenu-label');
    const r = label ? parseFloat(label.textContent) : 1;
    
    const rect = canvas.getBoundingClientRect();
    const clickX = e.clientX - rect.left;
    const clickY = e.clientY - rect.top;
    
    const unit = scale / 2;
    const xClick = (clickX - centerX) / unit;
    const yClick = (centerY - clickY) / unit;
    console.log(xClick, yClick, r);
    
    const xInput = document.querySelector('[id$=":xHidden"]');
    const yInput = document.querySelector('[id$=":y"]');
    
    if (xInput) {
        xInput.value = xClick.toString().replace('.', ',');
    }
    if (yInput) {
        yInput.value = yClick.toString().replace('.', ',');
    }
    
    const submitBtn = document.querySelector('[id$=":submitBtn"]');
    if (submitBtn) {
        submitBtn.click();
    }
}