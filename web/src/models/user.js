import { query as queryUsers, queryCurrent } from '../services/user';
import { getAuthority, getUserName } from '../utils/authority';
import { getInitiatorProfile, getWorkerProfile } from '../services/apiList';

export default {
  namespace: 'user',

  state: {
    list: [],
    currentUser: {},
  },

  effects: {
    *fetch(_, { call, put }) {
      const response = yield call(queryUsers);
      yield put({
        type: 'save',
        payload: response,
      });
    },
    *fetchCurrent(_, { call, put }) {
      const authority = yield getAuthority();
      const userName = yield getUserName();
      yield console.log('userName', userName);
      yield console.log('authority', authority);
      let response = {};
      if (authority.includes('WORKER')) {
        response = yield call(getWorkerProfile, userName);
      } else if (authority.includes('INITIATOR')) {
        response = yield call(getInitiatorProfile, userName);
      } else {
        response = yield call(queryCurrent);
      }
      yield console.log('user', response);
      yield put({
        type: 'saveCurrentUser',
        payload: response,
      });
    },
  },

  reducers: {
    save(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
    saveCurrentUser(state, action) {
      return {
        ...state,
        currentUser: action.payload,
      };
    },
    changeNotifyCount(state, action) {
      return {
        ...state,
        currentUser: {
          ...state.currentUser,
          notifyCount: action.payload,
        },
      };
    },
  },
};
