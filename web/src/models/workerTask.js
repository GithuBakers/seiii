import {
  getWorkerReceivedTaskList,
  getWorkerReceivedTask,
  getWorkerRecommendTask
} from '../services/apiList';

export default {
  namespace: 'workerTask',

  state: {
    finishedTaskList: [],
    unfinishedTaskList: [],
    finishedNumber: 0,
    unfinishedNumber: 0,
    allNumber: 0,
    selectedTask: {},
  },

  effects: {
    *fetchAllList(_, { call, put }) {
      const allTaskList = yield call(getWorkerReceivedTaskList, true);
      const finishedTaskList = yield allTaskList.filter(item=>item.finished);
      const unfinishedTaskList = yield allTaskList.filter(item=>!item.finished);
      yield put({
        type: 'refreshAllLists',
        payload: {
          finishedTaskList,
          unfinishedTaskList,
        },
      });
    },
    *fetchSelectedTask({ payload }, { call, put }) {
      const taskDetail = yield call(getWorkerReceivedTask, payload);
      yield put({ type: 'setSelectedTaskData', payload: { taskDetail } });
    },

    *fetchRecommendTask({payload},{call,put}){
      const taskDetail = yield call(getWorkerRecommendTask, payload);
      yield put({ type: 'setSelectedTaskData', payload: { taskDetail } });
    },
  },

  reducers: {
    refreshAllLists(state, action) {
      return {
        ...state,
        finishedTaskList: action.payload.finishedTaskList,
        unfinishedTaskList: action.payload.unfinishedTaskList,
        finishedNumber: action.payload.finishedTaskList.length,
        unfinishedNumber: action.payload.unfinishedTaskList.length,
        allNumber:
        action.payload.finishedTaskList.length + action.payload.unfinishedTaskList.length,
      };
    },
    setSelectedTaskData(state, action) {
      return {
        ...state,
        selectedTask: action.payload.taskDetail,
      };
    },
  },
};
