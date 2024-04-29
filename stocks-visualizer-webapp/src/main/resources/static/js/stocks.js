window.onload = function () {
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
    let dps = [];
    let chart = new CanvasJS.Chart('container', {
        animationEnabled: true,
        theme: 'light2',
        exportEnabled: true,
        zoomEnabled: true,
        title: {
            text: 'Realtime Stock Prices'
        },
        axisY: {
            prefix: '$',
            title: 'Price'
        },
        toolTip: {
            content: "Date: {x}<br /><strong>Price:</strong><br />Open: {y[0]}, Close: {y[3]}<br />High: {y[1]}, Low: {y[2]}"
        },
        data: [
            {
                type: 'candlestick',
                dataPoints: dps
            }
        ]
    });

    let xVal = new Date();
    let yVal = 0;
    let updateInterval = 1000;
    let dataLength = 100;
    
    let updateChart = function (count) {
        count = count || 1;

        const eventSource = new EventSource("http://localhost:8182/api/v1/stocks");
        eventSource.addEventListener('stockQuote', (event) => {
            const symbol = JSON.parse(event.data).symbol;
            const time = JSON.parse(event.data).tradeTime;
            const price = JSON.parse(event.data).price;
            const close = JSON.parse(event.data).previousClosePrice;
            const high = JSON.parse(event.data).dayHighPrice;
            const low = JSON.parse(event.data).dayLowPrice;
            if (symbol === params.symbol){
                xVal = new Date(time);
                yVal = price;
                if (!checkIfDPExists(dps, xVal)){
                    dps.push({
                        x:xVal,
                        y: [
                            price,
                            close,
                            high,
                            low
                        ]
                    });
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



