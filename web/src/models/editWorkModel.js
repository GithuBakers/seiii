import {getWorkerTaskImgs} from "../services/apiList";

export default {

  namespace: 'editWorkModel',

  state: {
    finishFetch:false,
    isOpen: false,
    image:[],
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
  },

  effects: {
    * fetchImageDetail({payload}, {call, put}) {
      yield console.log("start fetch");
      yield put({type: 'setOpenState', payload: {isOpen: true}});
      const results = yield call(getWorkerTaskImgs,payload);
      //TODO:1  will return
      // const results = yield call(getWorkerTaskImgs,1);
      yield console.log(results);
      yield put({type: `setImage`, payload: {image: results}});
      yield put({type: `setFinishFetch`, payload: {finishFetch: true}});
    },
    *setOpenState({payload},{put}){
      yield put({type: 'setState', payload});
      if(payload.isOpen===false){
        yield put({type:'workerTask/fetchAllList'});
      }
    },
  },
};
