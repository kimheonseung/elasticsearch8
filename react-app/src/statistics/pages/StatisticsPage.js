import React, { useEffect, useState } from 'react';
import Layout from 'layout/Layout';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, BarElement, LineElement, Title, ArcElement, Tooltip, Legend } from 'chart.js';
import { Bar, Pie, Line } from 'react-chartjs-2';
import axios from 'axios';
import './StatisticsPage.css'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  Title,
  Tooltip,
  Legend
);

function StatisticsPage() {
  const defaultApiUrl = 'http://localhost:8088/aggregation/sample-log';

  // const lbs = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];

  // const dt = {
  //   labels: lbs,
  //   datasets: [
  //     {
  //       label: 'Dataset 1',
  //       data: [1, 500, 372, 382, 543, 928, 182],
  //       backgroundColor: 'rgba(255, 99, 132, 0.5)',
  //     },
  //     {
  //       label: 'Dataset 2',
  //       data: [923, 190, 3, 21, 83, 213, 666],
  //       backgroundColor: 'rgba(53, 162, 235, 0.5)',
  //     },
  //   ],
  // };

  const options = {
    responsive: false,
    plugins: {
      legend: {
        position: 'top'
      },
      title: {
        display: true,
        text: 'Hello Chart'
      }
    }
  }

  const [barData, setBarData] = useState({
    labels: ['No Data'],
    datasets: [{label: 'No Data', data: ['No Data'], backgroundColor: ['rgba(255, 99, 132, 0.2)']}],
  })

  const [pieData, setPieData] = useState({
    labels: ['No Data'],
    datasets: [{label: 'No Data', data: ['No Data'], backgroundColor: ['rgba(255, 99, 132, 0.2)']}],
  });
  const [lineData, setLineData] = useState({
    labels: ['No Data'],
    datasets: [{label: 'No Data', data: ['No Data'], backgroundColor: ['rgba(255, 99, 132, 0.2)']}],
  });

  const createQueryString = ({fromMillis, aggregation, aggregationField, aggregationTopN, aggregationType}) => {
    return `?fromMillis=${fromMillis}&aggregation=${aggregation}&aggregationField=${aggregationField}&aggregationTopN=${aggregationTopN}&aggregationType=${aggregationType}`;
  }

  const createBarData = (labels, data) => {
    return {
      labels: labels,
      datasets: [
        {
          label: '# of logs',
          data: data,
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
        }
      ],
    };
  }

  const createPieData = (labels, data) => {
    return {
      labels: labels,
      datasets: [
        {
          label: '# of logs',
          data: data,
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)',
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
          ],
          borderWidth: 1
        }
      ]
    };
  }

  const createLineData = (labels, data) => {
    return {
      labels: labels,
      datasets: [
        {
          label: '# of logs',
          data: data,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor:  'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }
      ]
    };
  }

  const draw = () => {
    const field = 'logName,ip,timeMillis';
    const topN = '5,5,10';
    const type = 'term,term,time';
    axios
      .get(defaultApiUrl+createQueryString({
        fromMillis: 1648303178000,
        aggregation: true,
        aggregationField: field,
        aggregationTopN: topN,
        aggregationType: type
      }))
      .then((rs) => {
        if(200 === rs.data.status) {
          Object.keys(rs.data.dataArray[0]).forEach(key => {
            const dataArray = rs.data.dataArray[0][key];
            if(dataArray.length > 0) {
              const labels = dataArray.map((d) => d.key);
              const data = dataArray.map((d) => d.docCount);
              const aggsType = key.split('_')[1];
              const fieldName = key.split('_')[0];
              switch (aggsType) {
                case 'term':
                  if('ip' === fieldName) {
                    setPieData(createPieData(labels, data));
                  } else if('logName' === fieldName) {
                    setBarData(createBarData(labels, data));
                  }
                  break;
                case 'time':
                  setLineData(createLineData(labels, data));
                  break;
                default:
                  break;
              }

            }
          });
        };
      })
  }

  useEffect(() => {
    // drawIpPie();
    // drawTimeLine();
    draw();
  }, [null]);

  return (
    <>
      <Layout>
        <div className="statistics-wrap">
          <div className="statistics-2">
            <Bar width={320} height={320} options={options} data={barData} />
            <Pie width={320} height={320} options={options} data={pieData} />
          </div>
          <div className="statistics-1">
            <Line width={640} height={320} options={options} data={lineData} />;
          </div>
        </div>
      </Layout>
    </>
  );
}

export default StatisticsPage;