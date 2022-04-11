import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { updateConditionArray } from '../modules/searchSelectChangeModule';

function SearchCondition() {

  const dispatch = useDispatch();
  const searchSelectChanger = useSelector(state => state.searchSelectChanger);
  
  useEffect(() => {
    const condition = searchSelectChanger.condition;
    if(Object.keys(condition).length === 0) {
      return;
    }
    dispatch(updateConditionArray(condition));
    
  }, [searchSelectChanger.addFlag]);

  return (
    <>
      <div className="search-condition-area">
        <div className="search-conditions">
          {
            searchSelectChanger.conditionArray.map(c => (
              <div key={c.field} className="condition-box">
                <span className="t-text condition-name">{c.field}</span>  
                <span className="t-text condition-values">
                  {c.values.map(v => (
                    `${v.label}, `
                  ))}
                </span>
              </div>
            ))
          }
        </div>
        
      </div>
     
    </>
  );
}

export default SearchCondition;