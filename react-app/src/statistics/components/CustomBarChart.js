import React, { useEffect, useRef, useState } from 'react';

function CustomBarChart({id}) {
  const chartRef = useRef(null);
  const defaultApiUrl = 'http://localhost:8088/';
  
  const [el, setEl] = useState();
  const [data, setData] = useState({
    categories: ['Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
    series: [
      {
        name: 'Budget',
        data: [5000, 3000, 5000, 7000, 6000, 4000, 1000],
      },
      {
        name: 'Income',
        data: [8000, 4000, 7000, 2000, 6000, 3000, 5000],
      },
    ],
  });
  const options = {
    chart: { width: 700, height: 400 },
  };
  let chart;

  useEffect(() => {
    if(!el) {
      let chartEl = document.getElementById(id);
      setEl(chartEl);
      chart = new BarChart({chartEl, data, options})
    }

  }, [data]);

  return (
    <>
      <div className="custom-bar-chart" id={id}></div>
    </>
  );
}

export default CustomBarChart;