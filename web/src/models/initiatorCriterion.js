import {
  createCriterion,
  getAllCriterionForInitiator,
  getMyCriterion,
} from '../services/apiList';
import { getUserName } from '../utils/authority';

export default {
  namespace: 'initiatorCriterion',

  state: {
    allCriterion:[],
    myCriterion:[],
  },

  effects: {
    *fetchAllCriterion(_, { call, put }) {
      const allCriterion = yield call(getAllCriterionForInitiator, true);
      yield put({
        type: 'refreshList',
        payload: {
          allCriterion,
        },
      });
    },
    *fetchMyCriterion(_, { call, put }) {
      const userId = yield getUserName();
      const myCriterion = yield call(getMyCriterion, userId);
      yield put({ type: 'refreshList', payload: { myCriterion } });
    },

    *createCriterion ({payload},{call}){
      yield call(createCriterion, payload);
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
