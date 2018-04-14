import {exploreTaskMarket, getWorkerTaskDetail,receiveWorkerTask} from '../services/apiList';

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
      //TODO: 1  will return
      // const taskDetail = yield call(getWorkerTaskDetail, 1);

      yield put({type: 'setSelectedTaskData', payload: {taskDetail}});
    },
    * receiveTask({payload},{call}){
      //TODO:1  will return
      yield call(receiveWorkerTask, payload);
      // yield call(receiveWorkerTask, 1);
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
        selectedTask: action.payload.taskDetail,
      };
    },
  },
};
