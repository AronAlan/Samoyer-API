export default [
  {
    path: '/User',
    layout: false,
    routes: [
      { path: '/User/login', component: './User/Login' },
      { path: '/User/register', component: './User/Register' },
    ],
  },
  {
    path: '/interface_info/:id',
    name: '查看接口',
    icon: 'smileTwoTone',
    component: './InterfaceInfo',
    hideInMenu: true,
  },
  {
    path: '/welcome',
    name: '🧡  欢迎使用',
    // icon: 'smileTwoTone',
    component: './Welcome',
  },
  {
    path: '/interface',
    name: '🔥  在线接口开放平台',
    // icon: 'HeartTwoTone',
    component: './Interface',
  },
  {
    path: '/my',
    name: '🌈  我的接口',
    // icon: 'HeartTwoTone',
    component: './My',
  },
  {
    path: '/admin',
    // icon: 'CrownTwoTone',
    name: '😃  管理页',
    access: 'canAdmin',
    routes: [
      {
        path: '/admin/InterfaceInfo',
        component: './Admin/InterfaceInfo',
        name: '⭐  接口管理',
      },
      {
        path: '/admin/interface_analysis',
        component: './Admin/InterfaceAnalysis',
        name: '⚡   接口分析',
      },
      {
        path: '/admin/User',
        component: './Admin/User',
        name: '☀️  用户管理',
      },
    ],
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: '404',
    path: '*',
    layout: false,
  },
];
