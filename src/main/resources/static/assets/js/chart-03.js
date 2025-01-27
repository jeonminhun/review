let chartTwo, chartThree;

    // 차트 생성 (차트 두 개 초기화)
    document.addEventListener('DOMContentLoaded', () => {
        chart02(); // 첫 번째 차트 초기화
        chart03(); // 두 번째 차트 초기화
    });

    fetch('/chart-data')
    .then(response => response.json())
    .then(data => {
        // 차트 업데이트 함수 호출
        updateChartTwo(data); // 차트 데이터 업데이트
        updateChartThree(data);
    });

    // 차트 02 초기화 함수
    function chart02() {
        const chartTwoOptions = {
            series: [
                { name: "별점", data: [13, 23, 20, 8, 13] },  // 초기 데이터
            ],
            colors: ["#3056D3"],
            chart: {
                type: "bar",
                height: 335,
                stacked: true,
                toolbar: { show: false },
                zoom: { enabled: false },
            },
            responsive: [
                {
                    breakpoint: 1536,
                    options: { plotOptions: { bar: { borderRadius: 0, columnWidth: "25%" } } },
                },
            ],
            plotOptions: {
                bar: { horizontal: false, borderRadius: 0, columnWidth: "25%" },
            },
            dataLabels: { enabled: false },
            xaxis: { categories: ["총 별점", "가성비 별점", "품질 별점", "내구성 별점", "디자인 별점"] },
            legend: {
                position: "top",
                horizontalAlign: "left",
                fontFamily: "Satoshi",
                fontWeight: 500,
                fontSize: "14px",
                markers: { radius: 99 },
            },
            fill: { opacity: 1 },
        };

        const chartSelector = document.querySelectorAll("#chartTwo");
        if (chartSelector.length) {
            chartTwo = new ApexCharts(document.querySelector("#chartTwo"), chartTwoOptions);
            chartTwo.render();
        }
    }

    // 차트 02 데이터 업데이트 함수
        function updateChartTwo(data) {
            if (chartTwo) {
                chartTwo.updateOptions({
                    series: [{
                        name: "별점",
                        data: data.data // 서버에서 받은 데이터
                    }],
                    xaxis: {
                        categories: data.labels // 서버에서 받은 카테고리 (X축)
                    }
                });
            }
        }

    // 차트 03 초기화 함수
    function chart03() {
        const chartThreeOptions = {
            series: [95, 1, 1, 1, 1, 1], // 초기 데이터
            chart: {
                type: "donut",
                width: 380
            },
            colors: ["#3C50E0", "#6577F3", "#8FD0EF", "#0FADCF", "#FF6F61", "#FFB400"],
            labels: ["5점", "4점", "3점", "2점", "1점", "0점"],
            legend: {
                show: false,
                position: "bottom"
            },
            plotOptions: {
                pie: {
                    donut: {
                        size: "65%",
                        background: "transparent"
                    }
                }
            },
            dataLabels: {
                enabled: false
            },
            responsive: [{
                breakpoint: 640,
                options: {
                    chart: {
                        width: 200
                    }
                }
            }]
        };

        const chartSelector = document.querySelectorAll("#chartThree");
        if (chartSelector.length) {
            chartThree = new ApexCharts(document.querySelector("#chartThree"), chartThreeOptions);
            chartThree.render();
        }
    }

    // 차트 03 데이터 업데이트 함수
    function updateChartThree(data) {
        if (chartThree) {
            chartThree.updateOptions({
                series: data.total // 서버에서 받은 데이터
            });
        }
    }

