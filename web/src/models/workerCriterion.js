import {
  getAllCriterionForWorker,
} from '../services/apiList';
import { getUserName } from '../utils/authority';

export default {
  namespace: 'workerCriterion',

  state: {
    allCriterion:[],
  },

  effects: {
    *fetchAllCriterion(_, { call, put }) {
      const allCriterion = yield call(getAllCriterionForWorker, true);
      yield console.log("allCriterion_worker",allCriterion);
      yield put({
        type: 'refreshList',
        payload: {
          allCriterion,
        },
      });
    },
  },

  reducers: {
    refreshList(state, action) {
      return {
        ...state,
        ...action.payload,
      };
    },
  },
};
