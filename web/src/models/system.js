import {getSystemInformation} from '../services/apiList';

export default {
  namespace: 'system',

  state: {
    initiator_number:0,
    worker_number:0,
    total_user_number:0,
    unfinished_number:0,
    finished_number:0,
    total_task_number:0,
  },

  effects: {
    * fetchSystemInformation(_, {call, put}) {
      const systemInfo = yield call(getSystemInformation);
      yield console.log('system_info', systemInfo);
      yield put({
        type: 'refreshSystemInformation',
        payload: systemInfo,
      });
    },

  },

  reducers: {
    refreshSystemInformation(state, action) {
      return {
        ...state,
        ...action.payload,
      };
    },
  },
};
