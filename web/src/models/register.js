import { fakeRegister } from '../services/api';
import { setAuthority } from '../utils/authority';
import { reloadAuthorized } from '../utils/Authorized';
import { register } from '../services/apiList'

export default {
  namespace: 'register',

  state: {
    status: undefined,
  },

  effects: {
    *submit({ payload }, { call, put }) {
      yield console.log(payload);
      const response = yield call(register, payload);
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
