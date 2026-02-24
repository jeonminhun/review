let chartTwo, chartThree;

    // 차트 생성 (차트 두 개 초기화)
    document.addEventListener('DOMContentLoaded', () => {
        chart02(); // 첫 번째 차트 초기화
        chart03(); // 두 번째 차트 초기화
    });

    fetch('/chart-data/'+ product_id)
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
            xaxis: { categories: ["총 별점", "가성비 별점", "내구성 별점", "품질 별점", "디자인 별점"] },
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
           series: [95, 1, 1, 1, 1],
           chart: {
               type: "donut",
               width: "100%", // 부모 컨테이너에 맞게 조정
               height: 350
           },
           colors: ["#3C50E0", "#6577F3", "#8FD0EF", "#0FADCF", "#FF6F61"],
           labels: ["5점", "4점", "3점", "2점", "1점"],


           // 1. 범례 활성화: 각 색상이 어떤 점수인지 표시
           legend: {
               show: true,
               position: "bottom",
               fontFamily: "SBCSNeoBD", // 프로젝트 폰트에 맞춰 변경
               fontWeight: 500,
               labels: {
                   colors: ["#64748B"],
                   useSeriesColors: false
               },
               markers: {
                   radius: 12
               }
           },

           // 2. 툴팁 설정 (마우스 오버 가시성 개선 핵심)
                   tooltip: {
                       enabled: true,
                       theme: 'light',
                       style: {
                           fontSize: '14px',
                           fontFamily: 'SBCSNeoBD',
                       },
                       // 커스텀 HTML 툴팁 사용 (가장 예쁘게 나옵니다)
                       custom: function({series, seriesIndex, dataPointIndex, w}) {
                           const stars = ["★★★★★", "★★★★☆", "★★★☆☆", "★★☆☆☆", "★☆☆☆☆"];
                           const color = w.config.colors[seriesIndex];
                           const label = w.config.labels[seriesIndex];
                           const value = series[seriesIndex];

                           return `
                               <div style="background: #fff; border: 1px solid ${color}; padding: 10px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                   <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 4px;">
                                       <span style="background-color: ${color}; width: 10px; height: 10px; border-radius: 50%; display: inline-block;"></span>
                                       <span style="font-weight: 600; color: #1C2434;">${stars[seriesIndex]} (${label})</span>
                                   </div>
                                   <div style="padding-left: 18px; font-size: 16px; color: ${color}; font-weight: 800;">
                                       ${value}%
                                   </div>
                               </div>
                           `;
                       }
                   },

                   plotOptions: {
                       pie: {
                           donut: {
                               size: "70%", // 중앙 공간 확보
                               labels: {
                                   // 2. 도넛 중앙에 라벨 표시
                                   show: true,
                                   total: {
                                       show: true,
                                       showAlways: true,
                                       label: "총 별점", // 중앙 상단 텍스트
                                       fontSize: "16px",
                                       fontWeight: 600,
                                       color: "#64748B",
                                       formatter: function (w) {
                                           // 가장 높은 첫 번째 데이터(5점)의 비율 표시
                                           return w.globals.series[0]+w.globals.series[1]+w.globals.series[2]+w.globals.series[3]+w.globals.series[4] + "개";
                                       }
                                   }
                               }
                           }
                       }
                   },


           // 3. 차트 위에 백분율 표시 (선택 사항)
           dataLabels: {
               enabled: true, // 작게라도 백분율을 보여주면 가시성이 좋아집니다
               formatter: function (val) {
                   return val.toFixed(0) + "%";
               },
               dropShadow: {
                   enabled: false
               }
           },

           responsive: [{
               breakpoint: 640,
               options: {
                   chart: {
                       width: 250
                   },
                   legend: {
                       position: "bottom"
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

