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
  console.log(finished);
  return request(`${version}/initiator/task?finished=${finished}`);
}

export async function getInitiatorTaskDetail(taskId) {
  return request(`${version}/initiator/task/${taskId}`);
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

export async function getWorkerTaskDetail(taskName) {
  return request(`${version}/worker/task/${taskName}`);
}

export async function receiveWorkerTask(taskName) {
  return request(`${version}/worker/task/received_task/${taskName}`, {
    method: 'POST',
  });
}

export async function getWorkerTaskImgs(taskName) {
  return request(`${version}/worker/task/received_task/img/${taskName}`);
}

export async function contributeWorkerTask(taskName, imgId, params) {
  //TODO:1
  return request(`${version}/worker/task/received_task/1/1`, {
    method: 'POST',
    body: params,
  });
  // return request(`${version}/worker/task/received_task/${taskName}/${imgId}`, {
  //   method: 'POST',
  //   body: params,
  // });
}

export async function getWorkerReceivedTaskList() {
  return request(`${version}/worker/task/received_task`);
}

export async function getWorkerReceivedTask(taskId) {
  return request(`${version}/worker/task/received_task/${taskId}`);
}

export async function getWorkerRecommendTask(type) {
  console.log("api type",type);
  return request(`${version}/worker/recommend_task?type=${type}`);
}

export async function updatePassword(userName, params) {
  console.log("updatePassword",params);
  return request(`${version}/user/password/${userName}`, {
    method: 'POST',
    body: params,
  });
}
