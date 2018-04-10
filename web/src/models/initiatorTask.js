import {
  getInitiatorTask,
  getInitiatorTaskDetail,
  finishInitiatorTask,
  createTask,
} from '../services/apiList';

export default {
  namespace: 'initiatorTask',

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
      const finishedTaskList = yield call(getInitiatorTask, true);
      const unfinishedTaskList = yield call(getInitiatorTask, false);
      yield put({
        type: 'refreshAllLists',
        payload: {
          finishedTaskList,
          unfinishedTaskList,
        },
      });
    },
    *fetchSelectedTask({ payload }, { call, put }) {
      const taskDetail = yield call(getInitiatorTaskDetail, payload);
      yield put({ type: 'setSelectedTaskData', payload: { taskDetail } });
    },
    *deleteTask({ payload }, { call }) {
      yield call(finishInitiatorTask, payload);
    },
    *uploadNewTask({ payload }, { call }) {
      yield call(createTask, payload);
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
