import mockjs from 'mockjs';
import { getRule, postRule } from './mock/rule';
import { getActivities, getNotice, getFakeList } from './mock/api';
import { getFakeChartData } from './mock/chart';
import { getProfileBasicData } from './mock/profile';
import { getProfileAdvancedData } from './mock/profile';
import { getNotices } from './mock/notices';
import { FakeDescTags,FakeEdgeTags,FakeRectTags } from './mock/fakeTags'
import { format, delay } from 'roadhog-api-doc';

// 是否禁用代理
const noProxy = process.env.NO_PROXY === 'true';
// const noProxy =true;
// 代码中会兼容本地 service mock 以及部署站点的静态数据

const typeList = ['RECT', 'DESC', 'EDGE'];
const sexType = ['MALE','FEMALE','NA'];
const rawCriterionList=mockjs.mock({
  'list|5-7': [
    {
      "qualified|1":true,
      "criterion_id":()=>mockjs.Random.string(),
      "criterion_name":()=>mockjs.Random.cname(),
      "cover": () => mockjs.Random.image('600x300', '#c4ab3f', '#FFF', 'criterion'),
      "type": () => typeList[mockjs.Random.integer(0, 2)],
      "data_set|":[
        {
          "id":()=>mockjs.Random.string(),
          "url":() => mockjs.Random.image('600x300', '#c4ab3f', '#FFF', 'criterion'),
        },
      ],
      "aim|50-100":1,//张数必须小于等于上传图片总张数
      "requirement":() => mockjs.Random.sentence(), //任务要求（用户该做什么）
      "keywords|5-10":[  //标注的关键词
        ()=>mockjs.Random.name()
      ]
    },
  ],
}).list;


const proxy = {
  // 支持值为 Object 和 Array
  'GET /api/v2/worker/information/worker': mockjs.mock({
    user_name: 'worker',
    role: 'WORKER',
    avatar: () => mockjs.Random.image('100x100', '#FF6600', 'W'),
    nick_name: () => mockjs.Random.cname(),
    'credit|1-100': 1,
    'rank|1-10000': 1,
    sex: () => sexType[mockjs.Random.integer(0, 2)],
    birthday: '@date',
    dependencies:rawCriterionList,
    "capability":[
      {"item":"完成标准集", "number":90},
      {"item":"错误学习能力", "number":80},
      {"item":"完成任务量", "number":60},
      {"item":"标注准确度", "number":70},//任务
      {"item":"近期情况", "number":60}
    ],
    "recent":getFakeChartData.offlineChartData,
    //饼图，用户近期各种标注的占比
    "tags":[
      {"item":"标准描述","number":20},
      {"item":"标准矩形","number":30},
      {"item":"标准边界","number":40},
      {"item":"任务描述","number":50},
      {"item":"任务矩形","number":90},
      {"item":"任务边界","number":70}
    ]
  }),
  'GET /api/v2/initiator/information/initiator': mockjs.mock({
    user_name: 'initiator',
    role: 'INITIATOR',
    avatar: () => mockjs.Random.image('100x100', '#894FC4', '#FFF', 'I'),
    nick_name: () => mockjs.Random.cname(),
    sex: () => sexType[mockjs.Random.integer(0, 2)],
    birthday: '@date',
    "tasks|5-10":[
      {
        "task_name":'@name',//任务名
        "task_id":() => mockjs.Random.string(),//任务ID
        "completeness|0-100":1,//0-100，完成情况
        "recent":getFakeChartData.offlineChartData,
      },
    ]
  }),
  'POST /api/v2/initiator/information/initiator': true,
  'POST /api/v2/worker/information/worker': true,
  'POST /api/v2/initiator/task/running_task': true,
  'POST /api/v2/user/password/worker': true,
  'POST /api/v2/user/password/initiator': true,

  'GET /api/v2/initiator/task': (req, res) => {
    let list = {
      'list|5-7': [
        {
          task_id: () => mockjs.Random.string(), //达标比例
          task_name: () => mockjs.Random.cname(),
          cover: () => mockjs.Random.image('600x300', 'TASK'),
          type: () => typeList[mockjs.Random.integer(0, 2)],
          completeness: () => mockjs.Random.integer(0, 100), //达标比例
          finished: true, //状态
        },
      ],
    };
    if (req.query.finished === 'true') {
      console.log('list', mockjs.mock(list));
      res.send(JSON.stringify(mockjs.mock(list).list));
    } else {
      let unfinished = mockjs.mock(list);
      unfinished.list.forEach(e => {
        e.finished = false;
        return e;
      });
      res.send(JSON.stringify(unfinished.list));
    }
  },
  'GET /api/v2/initiator/task/1': mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    initiator_name: () => mockjs.Random.cname(),
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => typeList[mockjs.Random.integer(0, 2)],
    'aim|50-2000': 1,
    'limit|100-500': 1,
    'reward|200-300': 1,
    requirement: () => mockjs.Random.sentence(), //任务要求
    'total_reward|2000-30000': 1,
    completeness: () => mockjs.Random.integer(0, 100), //达标比例
    result: () => mockjs.mock('@url'), //结果所在地
    finished: false, //状态
    "has_result":false,
    "keywords|5-10":[ ()=>mockjs.Random.name()],
    "dependencies": rawCriterionList
  }),
  'GET /api/v2/initiator/task/task_result': mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => typeList[mockjs.Random.integer(0, 2)],
    'aim|50-2000': 1,
    'limit|100-500': 1,
    "keywords|5-10":[ ()=>mockjs.Random.name()],
    "dependencies": rawCriterionList,
    "hive|1000":[
      {
        "x|200-1000":1,//单张图片用户标注数目
        "y|200-1000":1//标注聚集程度
      }
    ],
    "sex_age":[
      {
        "name":'Male',
        "under20": 18.9,
        "under30": 28.8,
        "under40":39.3,
        "above": 81.4
      },
      {
        "name":'Female',
        "under20": 18.9,
        "under30": 28.8,
        "under40":39.3,
        "above": 81.4
      },
      {
        "name":'Others',
        "under20": 18.9,
        "under30": 28.8,
        "under40":39.3,
        "above": 81.4
      }
    ]
  }),
  'GET /api/v2/worker/task/1': mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => typeList[mockjs.Random.integer(0, 2)],
    'limit|100-500': 1,
    'reward|200-300': 1,
    requirement: () => mockjs.Random.sentence(), //任务要求
    "keywords|5-10":[ ()=>mockjs.Random.name()],
    dependencies:rawCriterionList
  }),
  'GET /api/v2/worker/task_list': (req, res) => {
    let list = {
      'list|15-17': [
        {
          task_name: () => mockjs.Random.cname(),
          cover: () => mockjs.Random.image('600x300', '#894FC4','#FFF', 'LIST'),
          type: () => typeList[mockjs.Random.integer(0, 2)],
          task_id: () => mockjs.Random.string(), //达标比例
          'reward|100': 1, //状态
        },
      ],
    };
    res.send(JSON.stringify(mockjs.mock(list).list));
  },
  'GET /api/v2/worker/recommend_task': (req, res) => {
    const type=req.query.type;
    const value= mockjs.mock({
      task_id: () => mockjs.Random.string(), //达标比例
      task_name: () => mockjs.Random.cname() + 'task',
      cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
      type: () => type,
      'limit|100-500': 1,
      'reward|200-300': 1,
      requirement: () => mockjs.Random.sentence(), //任务要求
      "actual_number|3-5":1,
      "total_reward|50-100":1,
      "keywords|5-10":[ ()=>mockjs.Random.name()]
    });
    res.sendStatus(404);
    // res.send(JSON.stringify(value));
  },
  'GET /api/v2/worker/task/received_task/1':  mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => "DESC",
    'limit|100-500': 1,
    'reward|200-300': 1,
    requirement: () => mockjs.Random.sentence(), //任务要求
    "actual_number|3-5":1,
    "total_reward|50-100":1,
    "keywords|5-10":[ ()=>mockjs.Random.name()],
    dependencies:rawCriterionList
  }),
  'GET /api/v2/worker/task/received_task/img/1' :(req, res) => {
    let list = {
      'list|5-7': [
        {
          id:()=>mockjs.Random.string,
          raw: () => mockjs.Random.image(),
          name: () => mockjs.Random.string(),
        },
      ],
    };
    res.send(JSON.stringify(mockjs.mock(list).list));
  },
  'POST /api/v2/worker/task/received_task/1/1':true,
  'GET /api/v2/worker/task/received_task': (req, res) => {
    let list = {
      'list|15-17': [
        {
          task_name: () => mockjs.Random.cname(),
          cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'LIST'),
          type: () => typeList[mockjs.Random.integer(0, 2)],
          task_id: () => mockjs.Random.string(), //达标比例
          'actual_number|20-30':1,
          'total_reward|1000-2000':1,
          'finished|1':true
        },
      ],
    };
    res.send(JSON.stringify(mockjs.mock(list).list));
  },
  'POST /api/v2/worker/task/received_task/1':  mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => "EDGE",
    'limit|100-500': 1,
    'reward|200-300': 1,
    requirement: () => mockjs.Random.sentence(), //任务要求
    'total_reward|2000-30000': 1,
    'actual_number|20-30':1,
  }),
  'POST /api/v2/initiator/task/finished_task': mockjs.mock({
    task_id: () => mockjs.Random.string(), //达标比例
    task_name: () => mockjs.Random.cname() + 'task',
    initiator_name: () => mockjs.Random.cname(),
    cover: () => mockjs.Random.image('600x300', '#894FC4', '#FFF', 'TASK1'),
    type: () => typeList[mockjs.Random.integer(0, 2)],
    'aim|50-2000': 1,
    'limit|100-500': 1,
    'reward|200-300': 1,
    requirement: () => mockjs.Random.sentence(), //任务要求
    'total_reward|2000-30000': 1,
    completeness: () => mockjs.Random.integer(0, 100), //达标比例
    result: () => mockjs.mock('@url'), //结果所在地
    "dependencies":rawCriterionList
  }),
  'GET /api/v2/admin/sys_info': mockjs.mock({
    "initiator_number|1-100":1,
    "worker_number|1-100":1,
    "total_user_number|200-300":1,
    "unfinished_number|2000-3000":1,
    "finished_number|2000-3000":1,
    "total_task_number|4000-6000":1
  }),



  'POST /api/v2/initiator/criterion/new_criterion':true,
  'GET /api/v2/initiator/criterion/img' :(req, res) => {
    let list = {
      'list|5-7': [
        {
          id:()=>mockjs.Random.string,
          raw: () => mockjs.Random.image(),
          name: () => mockjs.Random.string(),
        },
      ],
    };
    res.send(JSON.stringify(mockjs.mock(list).list));
  },
  'POST /api/v2/initiator/criterion/img': true,
  'GET /api/v2/initiator/criterion/myself':rawCriterionList,
  'GET /api/v2/initiator/criterion':rawCriterionList,



  'GET /api/v2/worker/criterion':rawCriterionList,
  'GET /api/v2/worker/criterion/img':(req, res) => {
    let list = {
      'list|3-4': [
        {
          id:()=>mockjs.Random.string,
          raw: () => mockjs.Random.image(),
          name: () => mockjs.Random.string(),
        },
      ],
    };
    res.send(JSON.stringify(mockjs.mock(list).list));
  },
  'POST /api/v2/worker/criterion/img':{...FakeRectTags,correct:false},

  'GET /api/currentUser': {
    $desc: '获取当前用户接口',
    $params: {
      pageSize: {
        desc: '分页',
        exp: 2,
      },
    },
    $body: {
      name: 'Serati Ma',
      avatar: 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png',
      userid: '00000001',
      notifyCount: 12,
    },
  },
  // GET POST 可省略
  'GET /api/users': [
    {
      key: '1',
      name: 'John Brown',
      age: 32,
      address: 'New York No. 1 Lake Park',
    },
    {
      key: '2',
      name: 'Jim Green',
      age: 42,
      address: 'London No. 1 Lake Park',
    },
    {
      key: '3',
      name: 'Joe Black',
      age: 32,
      address: 'Sidney No. 1 Lake Park',
    },
  ],
  'GET /api/project/notice': getNotice,
  'GET /api/activities': getActivities,
  'GET /api/rule': getRule,
  'POST /api/rule': {
    $params: {
      pageSize: {
        desc: '分页',
        exp: 2,
      },
    },
    $body: postRule,
  },
  'POST /api/forms': (req, res) => {
    res.send({ message: 'Ok' });
  },
  'GET /api/tags': mockjs.mock({
    'list|100': [{ name: '@city', 'value|1-100': 150, 'type|0-2': 1 }],
  }),
  'GET /api/fake_list': getFakeList,
  'GET /api/fake_chart_data': getFakeChartData,
  'GET /api/profile/basic': getProfileBasicData,
  'GET /api/profile/advanced': getProfileAdvancedData,
  'POST /api/v2/login': (req, res) => {
    const { password, user_name } = req.body;
    if (password === '888888' && user_name === 'admin') {
      res.append('Roles', ['ADMIN']);
      res.send({
        status: 200,
      });
      return;
    }
    if (password === '1' && user_name === 'worker') {
      res.append('Roles', ['WORKER']);
      res.send({
        status: 200,
      });
      return;
    }
    if (password === '1' && user_name === 'initiator') {
      res.append('Roles', ['INITIATOR']);
      res.send({
        status: 200,
      });
      return;
    }
    res.send({
      status: 403,
    });
  },
  'POST /api/register': (req, res) => {
    res.send({ status: 'ok', currentAuthority: 'user' });
  },
  'GET /api/notices': getNotices,
  'GET /api/500': (req, res) => {
    res.status(500).send({
      timestamp: 1513932555104,
      status: 500,
      error: 'error',
      message: 'error',
      path: '/base/category/list',
    });
  },
  'GET /api/404': (req, res) => {
    res.status(404).send({
      timestamp: 1513932643431,
      status: 404,
      error: 'Not Found',
      message: 'No message available',
      path: '/base/category/list/2121212',
    });
  },
  'GET /api/403': (req, res) => {
    res.status(403).send({
      timestamp: 1513932555104,
      status: 403,
      error: 'Unauthorized',
      message: 'Unauthorized',
      path: '/base/category/list',
    });
  },
  'GET /api/401': (req, res) => {
    res.status(401).send({
      timestamp: 1513932555104,
      status: 401,
      error: 'Unauthorized',
      message: 'Unauthorized',
      path: '/base/category/list',
    });
  },
};

// export default (delay(proxy, 1000));
export default {};
// export default {
// };
