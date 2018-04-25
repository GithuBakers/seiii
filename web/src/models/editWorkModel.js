import {getWorkerTaskImgs,getInitiatorCriterionImgs,getWorkerCriterionImgs} from "../services/apiList";
import {WORKER_NORMAL,WORKER_CRITERION,INITIATOR_CRITERION} from '../data/markRequestType'

export default {

  namespace: 'editWorkModel',

  state: {
    finishFetch:false,
    isOpen: false,
    image:[],
    markRequestType:undefined,
  },


  reducers: {
    setState(state, action) {
      if(state.isOpen===false){
        state.finishFetch=false;
      }
      if(action.payload&&action.payload.isOpen===false){

      }
      return {...state, ...action.payload};
    },

    setImage(state, action) {
      return {...state, ...action.payload}
    },

    setFinishFetch(state, action) {
      return {...state, ...action.payload}
    },

    setMarkRequestType(state, action) {
      return {...state, ...action.payload}
    },
  },

  effects: {
    * fetchImageDetail({payload}, {call, put}) {
      const requestType=payload.markRequestType||WORKER_NORMAL;

      yield console.log("start fetch");
      yield put({type: 'setOpenState', payload: {isOpen: true}});
      yield put({type: 'setMarkRequestType', payload: {markRequestType: requestType}});

      let results;
      switch (requestType){
        case WORKER_NORMAL:
          results = yield call(getWorkerTaskImgs,payload.id);
          break;
        case WORKER_CRITERION:
          results = yield call(getWorkerCriterionImgs,payload.id);
          break;
        case INITIATOR_CRITERION:
          results = yield call(getInitiatorCriterionImgs,payload.id);
          break;
        default:
          results = null;
          break;
      }
      yield put({type: `setImage`, payload: {image: results}});
      yield put({type: `setFinishFetch`, payload: {finishFetch: true}});
    },
    *setOpenState({payload},{put,select}){
      yield put({type: 'setState', payload});
      const requestType = yield select(state => state.editWorkModel.markRequestType);
      if(payload.isOpen===false){
        yield put({type:'workerTask/fetchAllList'});
      }

      switch (requestType){
        case WORKER_NORMAL:
          yield put({type:'workerTask/fetchAllList'});
          break;
        case WORKER_CRITERION:
          yield put({type:'workerTask/fetchAllList'});
          break;
        case INITIATOR_CRITERION:
          yield put({type:'workerTask/fetchAllList'});
          break;
        default:
          break;
      }
    },
  },
};
