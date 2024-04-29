window.onload = function () {
    let dps = [];
    let chart = new CanvasJS.Chart('container', {
        title: {
            text: 'Realtime Stock Prices'
        },
        data: [
            {
                type: 'line',
                dataPoints: dps
            }
        ]
    });

    let xVal = new Date();
    let yVal = 0;
    let updateInterval = 1000;
    let dataLength = 20;
    
    let updateChart = function (count) {
        count = count || 1;

        const eventSource = new EventSource("http://localhost:8182/api/v1/stocks");
        eventSource.addEventListener('stockQuote', (event) => {
            const symbol = JSON.parse(event.data).symbol;
            const time = JSON.parse(event.data).tradeTime;
            const price = JSON.parse(event.data).price;
            if (symbol === 'AAPL'){
                xVal = new Date(time);
                yVal = price;
                if (!checkIfDPExists(dps, xVal)){
                    dps.push({x:xVal, y: yVal});
                }
            }
        });
        if (dps.length > dataLength) {
            dps.shift();
        }

        chart.render();
    }
    updateChart(dataLength);
    setInterval(function(){updateChart()}, updateInterval);
}

let checkIfDPExists = (dps, currDate) =>{
    if (!dps.length){
        return false;
    }
    for (let i = 0; i < dps.length; i++){
        if ((dps[i].x).toISOString() === currDate.toISOString()){
            return true;
        }
    }
    return false;
}



