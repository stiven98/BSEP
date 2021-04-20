
const routes = [
  {
    path: '/',
    component: () => import('pages/Login.vue'),
    children: [
      { path: '', component: () => import('pages/Login.vue') }
    ]
  },
  {
    path: '/adminHome',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/AdminHome.vue') }
    ],
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem('role') !== 'admin') next({ name: '/' })
      else next()
    }
  },
  {
    path: '/userHome',
    component: () => import('layouts/UserLayout.vue'),
    children: [
      { path: '', component: () => import('pages/UserHome.vue') }
    ],
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem('role') !== 'user') next({ name: '/' })
      else next()
    }
  },
  {
    path: '/newCertificate',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('pages/AddCertificate.vue') }
    ],
    beforeEnter: (to, from, next) => {
      if (localStorage.getItem('role') !== 'admin') next({ name: '/' })
      else next()
    }
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes