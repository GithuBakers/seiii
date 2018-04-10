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
  console.log('init profile', userName);
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

export async function getInitiatorTaskDetail(taskName) {
  return request(`${version}/initiator/task/${taskName}`);
}

export async function finishInitiatorTask(taskName) {
  return request(`${version}/initiator/task/finished_task`, {
    method: 'POST',
    body: {
      task_name: taskName,
    },
  });
}

// WORKER
export async function getWorkerProfile(userName) {
  return request(`${version}/worker/information/${userName}`);
}

export async function setWorkerProfile(userName, params) {
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
  return request(`${version}/worker/task/received_task/${taskName}/${imgId}`, {
    method: 'POST',
    body: params,
  });
}

export async function getWorkerReceivedTaskList() {
  return request(`${version}/worker/task/received_task`);
}

export async function getWorkerReceivedTask(taskName) {
  return request(`${version}/worker/task/received_task/${taskName}`);
}
