import { createAction, handleActions } from "redux-actions";
import produce from 'immer';

const initState = {
    key: 'none',
    condition: {},
    addFlag: false,
    conditionArray: []
}

const CHANGE = 'changer/CHANGE';
const UPDATE = 'changer/UPDATE';
const ADD_FLAG   = 'changer/ADD_FLAG';
const ADD_CONDITION = 'changer/ADD_CONDITION';

export const changeSearchSelect = createAction(CHANGE, key => key);
export const updateCondition = createAction(UPDATE, condition => condition);
export const updateAddFlag = createAction(ADD_FLAG, addFlag => addFlag);
export const updateConditionArray = createAction(ADD_CONDITION, condition => condition)

const searchSelectChanger = handleActions({
    [CHANGE]: (state, {payload: key}) => 
        produce(state, draft => {
            draft.key = key;
        }),
    [UPDATE]: (state, {payload: condition}) =>
        produce(state, draft => {
            draft.condition = condition
        }),
    [ADD_FLAG]: (state, {payload: addFlag}) =>
        produce(state, draft => {
            draft.addFlag = addFlag
        }),    
    [ADD_CONDITION]: (state, {payload: condition}) =>
        produce(state, draft => {
            draft.conditionArray.push(condition);
        }),    
}, initState);

export default searchSelectChanger;