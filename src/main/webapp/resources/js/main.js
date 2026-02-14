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
    smtLeft();
}

function drawArea(ctx, cx, cy, scale, r) {
    const unit = scale / 2;
    ctx.fillStyle = 'rgba(51, 153, 255, 0.2)';
    
    // круг
    ctx.beginPath();
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

    // Риски на осях
    const tickLength = 6;
    for (let x = -5; x <= 5; x++) {
        if (x === 0) continue;
        const px = centerX + x * (scale / 2);
        if (px >= 0 && px <= width) {
            ctx.beginPath();
            ctx.moveTo(px, centerY - tickLength/2);
            ctx.lineTo(px, centerY + tickLength/2);
            ctx.stroke();
        }
    }
    for (let y = -5; y <= 5; y++) {
        if (y === 0) continue;
        const py = centerY - y * (scale / 2);
        if (py >= 0 && py <= height) {
            ctx.beginPath();
            ctx.moveTo(centerX - tickLength/2, py);
            ctx.lineTo(centerX + tickLength/2, py);
            ctx.stroke();
        }
    }
    ctx.strokeText("0", centerX + sixCanv, centerY - sixCanv);
}

function drawPoint(x, y, hit) {
    const color = hit ? "green" : "red";
    const unit = scale / 2;
    
    ctx.beginPath();
    ctx.arc(centerX + x * unit, centerY - y * unit, 3.5, 0, Math.PI * 2);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.strokeStyle = "black";
    ctx.lineWidth = 1;
    ctx.stroke();
}

function smtLeft() { //TODO переименовать метод!!!!!!
    const table = document.getElementById('resultTable');
    if (!table) console.log("таблица с резами не найдена");
    const rows = table.getElementsByTagName('tr');
    if (rows.length < 2) console.log("что-то не так с длинной таблицы с резами");
    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        if (cells.length >= 4) {    
            const x = parseFloat(cells[0].textContent.trim());
            const y = parseFloat(cells[1].textContent.trim().replace(',', '.'));
            const r = parseFloat(cells[2].textContent.trim());
            const hit = cells[3].textContent.trim() === "Да";
            if (!isNaN(x) && !isNaN(y)) {
                const label = document.querySelector('[id$=":r"] .ui-selectonemenu-label');
                const nowR = label ? parseFloat(label.textContent) : 1;
                if (nowR !== r){
                    drawPoint(x * nowR / r, y * nowR / r, hit);
                } else drawPoint(x, y, hit);
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
    
    const xInput = document.querySelector('[id$=":xHidden"]');
    const yInput = document.querySelector('[id$=":y"]');
    
    if (xInput) {
        xInput.value = xClick.toString();
    }
    if (yInput) {
        yInput.value = yClick.toString();
    }
    
    const submitBtn = document.querySelector('[id$=":submitBtn"]');
    if (submitBtn) {
        submitBtn.click();
    }
    let text = "";
    let flag = false;
    console.log(parseFloat(xInput.value))
    console.log(parseFloat(yInput.value))
    if (parseFloat(xInput.value) > 5 || parseFloat(xInput.value) < -5) {
        text += "|x| < 5\n";
        flag = true;
        console.log(xInput.value);
    };

    if (parseFloat(yInput.value) > 5 || parseFloat(yInput.value) < -5) {
        text += "|y| < 5"
        flag = true;
        console.log(yInput.value);
    };
    console.log(111111);
    console.log(text);
    if (flag){showClientError(text)};
}

function showClientError(text) {
    const time = 3000;
    var errorDiv = document.getElementById("error"); 
    errorDiv.textContent = text;
    errorDiv.hidden = false;
    if (errorDiv.timer) {
        clearTimeout(errorDiv.timer);
    }
    errorDiv.timer = setTimeout(() => {
        errorDiv.hidden = true;
        errorDiv.timer = null;
    }, time);
}  

function validateForm() {
    const xInput = document.querySelector('[id$=":xHidden"]');
    const yInput = document.querySelector('[id$=":y"]');
    
    let text = "";
    let flag = false;
    
    // Валидация X
    const xVal = parseFloat(xInput?.value);
    if (xInput && (isNaN(xVal) || xVal > 5 || xVal < -5)) {
        text += "X должен быть от -5 до 5\n";
        flag = true;
    }
    
    // Валидация Y
    const yVal = parseFloat(yInput?.value);
    if (yInput && (isNaN(yVal) || yVal > 5 || yVal < -5)) {
        text += "Y должен быть от -5 до 5";
        flag = true;
    }
    
    if (flag) {
        showClientError(text);
        return false; // Отменяет отправку формы
    }
    
    return true; // Разрешает отправку
}

function handleErrors(xhr, status, args){
    if (args.validationFailed) {
        showClientError(args.errorMessage);
    } else if (!args.sucsess){
        showClientError("Непредвиденная ошибка на серваке")
    }
}


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

document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById('plotCanvas');
    drawArea(ctx, centerX, centerY, scale, 1);
    drawAxes(ctx, width, height, centerX, centerY, scale);
    canvas.addEventListener('click', handleCanvasClick);
    setTimeout(smtLeft, 100);
});