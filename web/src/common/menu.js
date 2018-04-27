import { isUrl } from '../utils/utils';

const menuData = [
  {
    name: 'dashboard',
    icon: 'dashboard',
    path: 'dashboard/analysis',
    // children: [
    //   {
    //     name: '分析页',
    //     path: 'analysis',
    //   },
    //   {
    //     name: '监控页',
    //     path: 'monitor',
    //   },
    //   {
    //     name: '工作台',
    //     path: 'workplace',
    //     // hideInBreadcrumb: true,
    //     // hideInMenu: true,
    //   },
    // ],
  },
  {
    name: '系统信息',
    icon: 'eye-o',
    authority: 'ADMIN',
    path: 'admin/sys_info',
  },
  {
    name: '任务市场',
    icon: 'appstore-o',
    authority: 'WORKER',
    path: 'worker/task-list',
  },
  {
    name: '全部标准集',
    icon:'table',
    authority: 'WORKER',
    path: 'worker/all-criterion',
  },
  {
    name: '我的任务',
    icon: 'profile',
    authority: 'WORKER',
    path: 'worker/my-task',
  },
  {
    name: '任务列表',
    icon: 'profile',
    authority: 'INITIATOR',
    path: 'initiator/my-task',
  },
  {
    name: '公共标准集',
    icon: 'appstore-o',
    authority: 'INITIATOR',
    path: 'initiator/all-criterion',
  },
  {
    name: '我的标准集',
    icon: 'solution',
    authority: 'INITIATOR',
    path: 'initiator/my-criterion',
  },
  {
    name: '新增任务',
    icon: 'form',
    authority: 'INITIATOR',
    path: 'initiator/new-task',
  },
  {
    name: '创建标准集',
    icon: 'edit',
    authority:'INITIATOR',
    path:'initiator/new-criterion',
  },
  {
    name: '我的信息',
    icon: 'user',
    authority: 'WORKER',
    path: 'worker/worker-profile',
  },
  {
    name: '我的信息',
    icon: 'user',
    authority: 'INITIATOR',
    path: 'initiator/initiator-profile',
  },
  {
    name: '个人设置',
    icon: 'setting',
    authority: 'WORKER',
    path: 'worker/setting',
  },
  {
    name: '个人设置',
    icon: 'setting',
    authority: 'INITIATOR',
    path: 'initiator/setting',
  },
 //
 //  {
 //    name: '表单页',
 //    icon: 'form',
 //    authority: 'ADMIN',
 //    path: 'form',
 //    children: [
 //      {
 //        name: '基础表单',
 //        path: 'basic-form',
 //      },
 //      {
 //        name: '分步表单',
 //        path: 'step-form',
 //      },
 //      {
 //        name: '高级表单',
 //        authority: 'ADMIN',
 //        path: 'advanced-form',
 //      },
 //    ],
 //  },
 //  {
 //    name: '列表页',
 //    icon: 'table',
 //    authority: 'ADMIN',
 //    path: 'list',
 //    children: [
 //      {
 //        name: '查询表格',
 //        path: 'table-list',
 //      },
 //      {
 //        name: '标准列表',
 //        path: 'basic-list',
 //      },
 //      {
 //        name: '卡片列表',
 //        path: 'card-list',
 //      },
 //      {
 //        name: '搜索列表',
 //        path: 'search',
 //        children: [
 //          {
 //            name: '搜索列表（文章）',
 //            path: 'articles',
 //          },
 //          {
 //            name: '搜索列表（项目）',
 //            path: 'projects',
 //          },
 //          {
 //            name: '搜索列表（应用）',
 //            path: 'applications',
 //          },
 //        ],
 //      },
 //    ],
 //  },
 //  {
 //    name: '详情页',
 //    authority: 'ADMIN',
 //    icon: 'profile',
 //    path: 'profile',
 //    children: [
 //      {
 //        name: '基础详情页',
 //        path: 'basic',
 //      },
 //      {
 //        name: '高级详情页',
 //        path: 'advanced',
 //        authority: 'ADMIN',
 //      },
 //    ],
 //  },
 //  {
 //    name: '结果页',
 //    authority: 'ADMIN',
 //    icon: 'check-circle-o',
 //    path: 'result',
 //    children: [
 //      {
 //        name: '成功',
 //        path: 'success',
 //      },
 //      {
 //        name: '失败',
 //        path: 'fail',
 //      },
 //    ],
 //  },
 // {
 //    name: '账户',
 //    icon: 'user',
 //    path: 'user',
 //    authority: 'ADMIN',
 //
 //    children: [
 //      {
 //        name: '登录',
 //        path: 'login',
 //      },
 //      {
 //        name: '注册',
 //        path: 'register',
 //      },
 //      {
 //        name: '注册结果',
 //        path: 'register-result',
 //      },
 //    ],
 //  },
];

function formatter(data, parentPath = '/', parentAuthority) {
  return data.map(item => {
    let { path } = item;
    if (!isUrl(path)) {
      path = parentPath + item.path;
    }
    const result = {
      ...item,
      path,
      authority: item.authority || parentAuthority,
    };
    if (item.children) {
      result.children = formatter(item.children, `${parentPath}${item.path}/`, item.authority);
    }
    return result;
  });
}

export const getMenuData = () => formatter(menuData);
