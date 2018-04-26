import fetch from 'dva/fetch';
import { notification } from 'antd';
import { routerRedux } from 'dva/router';
import store from '../index';
import {getToken} from './authority'


const codeMessage = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '用户得到授权，但是访问是被禁止的。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。',
};
function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  const errortext =  response.error||codeMessage[response.status];
  notification.error({
    message: `请求错误 ${response.status}: ${response.url}`,
    description: errortext,
  });
  const error = new Error(errortext);
  error.name = response.status;
  error.response = response;
  throw error;
}

/**
 * Requests a URL, returning a promise.
 *
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export default async function request(url, options) {
  const defaultOptions = {
    credentials: 'include',
    method: 'GET',
  };
  const newOptions = { ...defaultOptions, ...options };
  if (newOptions.method === 'POST' || newOptions.method === 'PUT') {
    if (!(newOptions.body instanceof FormData)) {
      newOptions.headers = {
        Accept: 'application/json',
        'Content-Type': 'application/json; charset=utf-8',
        ...newOptions.headers,
      };
      newOptions.body = JSON.stringify(newOptions.body);
    } else {
      // newOptions.body is FormData
      newOptions.headers = {
        Accept: 'application/json',
        'Content-Type': 'multipart/form-data',
        ...newOptions.headers,
      };
    }
  }

  newOptions.headers={
    Authorization:getToken(),
    ...newOptions.headers,
  };

  let response = await fetch(url, newOptions);
  console.log('response', response);

  try {
    // await checkStatus(response);
    // await console.log('whole response', response);
    // if (newOptions.method === 'DELETE' || response.status === 204) {
    //   return response.text();
    // }
    let newStatus = 200;
    if (url.includes('/login')
      ||url.includes('/user/new_user')
      ||url.includes('/worker/recommend_task')
    ) {
      if (response.status === 403) {
        newStatus = 'error';
      } else {
        newStatus = 'ok';
      }
    }else{
      await checkStatus(response);
      await console.log('whole response', response);
      if (newOptions.method === 'DELETE' || response.status === 204) {
        return response.text();
      }
    }

    let data=undefined;
    if(!url.includes('/login')){
      data = await response.json();
    }
    console.log('data', data);

    if (data!=undefined && (typeof data !== "object"||Array.isArray(data))) {
      return data;
    }

    let ret = {
      token: undefined,
      currentAuthority: undefined,
      ...data,
      status: newStatus,
    };

    if (response.headers.get('Authorization')) {
      ret.token = await response.headers.get('Authorization');
    }
    if (response.headers.get('Roles')) {
      ret.currentAuthority = await response.headers.get('Roles');
    }

    await console.log('ret', ret);

    return ret;
  } catch (e) {
    const { dispatch } = store;
    const status = e.name;
    console.log('status', status);
    console.log('e',e);

    if (status === 401) {
      dispatch({
        type: 'login/logout',
      });
      return;
    }
    if (status === 403) {
      dispatch(routerRedux.push('/exception/403'));
      return;
    }
    if (status <= 504 && status >= 500) {
      dispatch(routerRedux.push('/exception/500'));
      return;
    }
    if (status >= 404 && status < 422) {
      dispatch(routerRedux.push('/exception/404'));
      return;
    }
  }

  return;
}
