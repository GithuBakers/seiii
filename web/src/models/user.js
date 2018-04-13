import { query as queryUsers, queryCurrent } from '../services/user';
import { getAuthority, getUserName } from '../utils/authority';
import { getInitiatorProfile, getWorkerProfile, setWorkerProfile, setInitiatorProfile,updatePassword } from '../services/apiList';
import { notification  } from 'antd';
import { routerRedux } from 'dva/router';

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

    * updateWorkerProfile({ payload }, { call, put, select }) {
      const currentUser = yield select(state => state.user.currentUser);
      const userName = currentUser.user_name;
      const result = yield call(setWorkerProfile, userName, payload);
      if (result) {
        yield put({
          type: 'saveCurrentUser',
          payload: {
            ...currentUser,
            ...payload,
          },
        });
      }
    },
    * updateInitiatorProfile({ payload }, { call,put, select }) {
      const currentUser = yield select(state => state.user.currentUser);
      const userName = currentUser.user_name;
      const result = yield call(setInitiatorProfile, userName, payload);
      if (result) {
        yield put({
          type: 'saveCurrentUser',
          payload: {
            ...currentUser,
            ...payload,
          },
        });
      }
    },

    * updatePassword({payload},{call,select,put}){
      const currentUser = yield select(state => state.user.currentUser);
      const userName = currentUser.user_name;
      const result = yield call(updatePassword, userName, payload);
      if(result){
        yield notification['success']({
          message: '密码修改成功',
          description: '为了保障您的账户安全，需要您重新登陆',
        });
        yield put(routerRedux.push('/user/login'));
      }

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
