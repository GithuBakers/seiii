import { fakeRegister } from '../services/api';
import { setAuthority } from '../utils/authority';
import { reloadAuthorized } from '../utils/Authorized';
import { register } from '../services/apiList'
import { notification } from 'antd/lib/index';

export default {
  namespace: 'register',

  state: {
    status: undefined,
  },

  effects: {
    *submit({ payload }, { call, put }) {
      yield console.log(payload);
      const response = yield call(register, payload);
      if(response.status==='error'){
        yield notification.warning({
          message: '用户名已被注册',
          description: '请再尝试使用一个新的用户名进行注册.',
        });
      }
      yield put({
        type: 'registerHandle',
        payload: response,
      });
    },
  },

  reducers: {
    registerHandle(state, { payload }) {
      setAuthority('user');
      reloadAuthorized();
      return {
        ...state,
        status: payload.status,
      };
    },
  },
};
