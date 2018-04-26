import { exploreTaskMarket, getWorkerRecommendTask, getWorkerTaskDetail, receiveWorkerTask } from '../services/apiList';

export default {
  namespace: 'taskMarket',

  state: {
    taskList: [],
    selectedTask: {},
  },

  effects: {
    * fetchTaskList(_, {call, put}) {
      const taskList = yield call(exploreTaskMarket, true);
      yield console.log('taskList',taskList);
      yield put({
        type: 'refreshTaskList',
        payload:taskList,
      });
    },
    * fetchSelectedTask({payload}, {call, put}) {
      const taskDetail = yield call(getWorkerTaskDetail, payload);
      yield put({type: 'setSelectedTaskData', payload: {taskDetail}});
    },
    * receiveTask({payload},{call}){
      yield call(receiveWorkerTask, payload);
    },
    *fetchRecommendTask({payload},{call,put}){
      const taskDetail = yield call(getWorkerRecommendTask, payload);
      yield put({ type: 'setSelectedTaskData', payload: { taskDetail } });
    },

  },

  reducers: {
    refreshTaskList(state, action) {
      return {
        ...state,
        taskList: action.payload,
      };
    },
    setSelectedTaskData(state, action) {
      return {
        ...state,
        selectedTask: action.payload.taskDetail||{},
      };
    },
  },
};
