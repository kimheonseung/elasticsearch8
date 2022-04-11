import React, { useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import Grid from '@toast-ui/react-grid';
import TuiGrid from 'tui-grid';
import 'tui-grid/dist/tui-grid.css';
import axios from 'axios';
import Pagination from 'paging/Pagination';


function SearchGrid({defaultApiUrl}) {

  const searchSelectChanger = useSelector(state => state.searchSelectChanger);
  
  const gridRef = useRef();

  const [pagingInfo, setPagingInfo] = useState({
    prev: false,
    next: false,
    page: 1,
    start: 1,
    end: 1,
    totalPage: 1,
    pageList: [1],
  });

  TuiGrid.applyTheme('default', {
    row: {
      odd: {
        background: '#363636',
        text: '#aaa',
      },
      even: {
          background: '#2f2f2f',
          text: '#aaa',
      },
      hover: {
          background: 'darkslateblue',
      }
    },
    cell: {
      header: {
        background: '#2C2C2C',
        text: '#ccc',
        showHorizontalBorder: true,
        showVerticalBorder: true,
        border: '#404040'
      },
      normal: {
          text: '#ccc',
          showHorizontalBorder: true,
          showVerticalBorder: true,
          border: '#404040'
      },
    }
  }); 

  const gridOption = {
    width: 'auto',
    rowHeight: 30,
    columnOptions: {
      resizable: true
    },
    scrollX: false,
    scrollY: true,
    bodyHeight: 'auto',
  }

  const columns = [
    {
      header: '시간',
      name: 'timeMillis',
      align: 'center',
    },
    {
      header: 'IP',
      name: 'ip',
      align: 'center',
    },
    {
      header: '장비명',
      name: 'equipName',
      align: 'center'
    },
    {
      header: '경로',
      name: 'logPath',
      align: 'center'
    },
    {
      header: '이름',
      name: 'logName',
      align: 'center'
    },
    {
      header: '로그',
      name: 'log',
      align: 'center'
    }
  ];

  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);

  useEffect(() => {

    if(searchSelectChanger.conditionArray.length === 0) {
      return;
    }

    let queryString = `?fromMillis=1648303178000&page=${page}&`;
    for(let i = 0 ; i < searchSelectChanger.conditionArray.length ; ++i) {
      if(i > 0) {
        queryString += '&';
      }
      const c = searchSelectChanger.conditionArray[i];
      queryString += `${c.field}=`;
      let operatorString = '';
      for(let j = 0 ; j < c.values.length ; ++j) {
        if(j > 0) {
          queryString += ','
          operatorString += ','
        }
        queryString += c.values[j].value;
        operatorString += 'eq';
      }
      queryString += `&${c.field}Operator=${operatorString}`;
    }

    axios
      .get(defaultApiUrl+'search/sample-log'+queryString)
      .then((result) => {
        console.log(result);
        if(result.data.status === 200) {
          const dataArray = result.data.dataArray;
          setData(dataArray);
          setPagingInfo(result.data.paging);
        }
      })
    
  }, [searchSelectChanger.conditionArray, page]);

  return (
    <>
      <div className="search-grid-area">
        <Grid
          ref={gridRef}
          width={gridOption.width} 
          rowHeight={gridOption.rowHeight} 
          bodyHeight={gridOption.bodyHeight}
          columnOptions={gridOption.columnOptions} 
          rowHeaders={gridOption.rowHeaders}
          scrollX={gridOption.scrollX} 
          scrollY={gridOption.scrollY}
          columns={columns}
          data={data}
        />
        <div className="gridPaging" >
        {
          <Pagination 
            prev={pagingInfo?.prev}
            next={pagingInfo?.next}
            page={pagingInfo?.page}
            start={pagingInfo?.start}
            end={pagingInfo?.end}
            totalPage={pagingInfo?.totalPage}
            pageList={pagingInfo?.pageList}
            handleClick={(page) => setPage(page)} />
        }
        </div>
      </div>
     
    </>
  );
}

export default SearchGrid;