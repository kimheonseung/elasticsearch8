import React, { useCallback, useEffect, useState, useRef } from 'react';
import Select from 'react-select';
import { useDispatch, useSelector } from 'react-redux';
import { changeSearchSelect, updateCondition, updateAddFlag } from '../modules/searchSelectChangeModule';
import axios from 'axios';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

function SearchSelect({defaultApiUrl}) {

  const selectRef = useRef();
  const dispatch = useDispatch();
  const searchSelectChanger = useSelector(state => state.searchSelectChanger);
  
  const selectArray = [
    // {key: 'timeMillis', name: '시간'},
    // {key: 'originalLog', name: '원본'},
    {key: 'ip', name: 'IP'},
    {key: 'equipName', name: '장비명'},
    // {key: 'logPath', name: '로그 경로'},
    // {key: 'logName', name: '로그 이름'},
    // {key: 'log', name: '로그'},
  ];
  const [options, setOptions] = useState([]);

  const onSelectChange = useCallback((e) => {
    e.preventDefault();
    selectRef.current.clearValue();
    const option = e.target.value;
    dispatch(changeSearchSelect(option));
    const groupByFieldUrl = defaultApiUrl+'/groupby/field?fromMillis=1648303178000&aggregationField='+option;
    axios
    	.get(groupByFieldUrl)
    	.then((result) => {
    		const status = result.data.status;
    		if(status === 200) {
			    let options = [];
      		const dataArray = result.data.dataArray;
			    dataArray.forEach((d) => {
				    options.push({"value": d, "label": d});
			    })
          setOptions(options);
    		} else {

    		}
    })
  }, [dispatch]);

  const handleSelectClose = () => {
    console.log();
  }

  const handleSelectChange = (selectedOptions) => {
    dispatch(updateCondition({
      field: searchSelectChanger.key,
      values: selectedOptions
    }))
  }

  const handleSearchAdd = () => {
    dispatch(updateAddFlag(!searchSelectChanger.addFlag));
  }

  useEffect(() => {
  });

  return (
    <>
      <div className="search-select-area">
        <div className="search-selectors">
          <select id="search-key" onChange={onSelectChange}>
            <option id="none" value="none">선택</option>
            {
              selectArray.map(select => (
                <option key={select.key} id={select.key} value={select.key}>{select.name}</option>
              ))
            }
          </select>

          <Select 
            ref={selectRef}
            menuPlacement={'top'}
            closeMenuOnSelect={false}
            onMenuClose={handleSelectClose}
            isMulti
            className="basic-multi-select"
            onChange={handleSelectChange}
            options={options}
          />
        </div>
        <div className="search-buttons">
          <button className="btn btn-secondary t-btn-toggle" onClick={handleSearchAdd}>
            <FontAwesomeIcon id="search-add-icon" size="2x" icon={faPlus}></FontAwesomeIcon>
          </button>
        </div>
      </div>
    </>
  );
}

export default SearchSelect;