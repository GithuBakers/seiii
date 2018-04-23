import request from '../utils/request';

const version = '/api/v2';

export async function login(params) {
  return request(`${version}/login`, {
    method: 'POST',
    body: params,
  });
}

export async function register(params) {
  return request(`${version}/user/new_user`, {
    method: 'POST',
    body: params,
  });
}

// INITIATOR
export async function getInitiatorProfile(userName) {
  return request(`${version}/initiator/information/${userName}`);
}

export async function setInitiatorProfile(userName, params) {
  return request(`${version}/initiator/information/${userName}`, {
    method: 'POST',
    body: params,
  });
}

export async function createTask(params) {
  return request(`${version}/initiator/task/running_task`, {
    method: 'POST',
    body: params,
  });
}

export async function getInitiatorTask(finished) {
  return request(`${version}/initiator/task?finished=${finished}`);
}

export async function getInitiatorTaskDetail(taskId) {
  //TODO: 1 FOR MOCK
  return request(`${version}/initiator/task/1`);

  // return request(`${version}/initiator/task/${taskId}`);
}

export async function finishInitiatorTask(taskId) {
  return request(`${version}/initiator/task/finished_task`, {
    method: 'POST',
    body: {
      task_name: taskId,
    },
  });
}

// WORKER
export async function getWorkerProfile(userName) {
  return request(`${version}/worker/information/${userName}`);
}

export async function setWorkerProfile(userName, params) {
  console.log("setProfile",params);
  return request(`${version}/worker/information/${userName}`, {
    method: 'POST',
    body: params,
  });
}

export async function exploreTaskMarket() {
  return request(`${version}/worker/task_list`);
}

export async function getWorkerTaskDetail(taskId) {
  //TODO: 1 FOR MOCK
  return request(`${version}/worker/task/1`);

  // return request(`${version}/worker/task/${taskId}`);
}

export async function receiveWorkerTask(taskId) {
  //TODO: 1 FOR MOCK
  return request(`${version}/worker/task/received_task/1`, {
    method: 'POST',
  });
  // return request(`${version}/worker/task/received_task/${taskId}`, {
  //   method: 'POST',
  // });
}

export async function getWorkerTaskImgs(taskId) {
  //TODO: 1 FOR MOCK
  return request(`${version}/worker/task/received_task/img/1`);

  // return request(`${version}/worker/task/received_task/img/${taskId}`);
}

export async function contributeWorkerTask(taskId, imgId, params) {
  //TODO: 1 FOR MOCK
  return request(`${version}/worker/task/received_task/1/1`, {
    method: 'POST',
    body: params,
  });
  // return request(`${version}/worker/task/received_task/${taskId}/${imgId}`, {
  //   method: 'POST',
  //   body: params,
  // });
}

export async function getWorkerReceivedTaskList() {
  return request(`${version}/worker/task/received_task`);
}

export async function getWorkerReceivedTask(taskId) {
  //TODO: 1 FOR MOCK
  return request(`${version}/worker/task/received_task/1`);
  // return request(`${version}/worker/task/received_task/${taskId}`);
}

export async function getWorkerRecommendTask(type) {
  return request(`${version}/worker/recommend_task?type=${type}`);
}

export async function getSystemInformation() {
  return request(`${version}/admin/sys_info`);
}


export async function updatePassword(userName, params) {
  console.log("updatePassword",params);
  return request(`${version}/user/password/${userName}`, {
    method: 'POST',
    body: params,
  });
}
