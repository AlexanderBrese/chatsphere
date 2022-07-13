import Registration from '@/modules/registration/';
import Shell from '@/modules/app/Shell/';
import Login from '@/modules/login/';
import Auth from '@/modules/app/Auth';
import ChatListPage from '@/modules/chatlist/';
import ChatPage from '@/modules/chat';
import Search from '@/modules/search';
import Profile from '@/modules/profile/';
import ProfileSettings from '@/modules/profilesettings/';
import ContactsPage from '@/modules/contactlist';
import Settings from '@/modules/settings';
import localforage from 'localforage';

const ROUTES = [
  {
    path: '/',
    redirect: '/oauth2/login',
  },
  {
    path: '/oauth2',
    name: 'Auth',
    component: Auth,
    beforeEnter: (to, from, next) => {
      localforage.getItem('token').then((value) => {
        if (value) {
          next('/chat');
        } else {
          next();
        }
      });
    },

    children: [
      {
        path: 'login',
        name: 'Login',
        component: Login,
      },
      {
        path: 'register',
        name: 'Registration',
        component: Registration,
      },
    ],
  },
  {
    path: '/shell',
    name: 'Shell',
    component: Shell,
    beforeEnter: (to, from, next) => {
      localforage.getItem('token').then((value) => {
        if (value) {
          next();
        } else {
          next('/');
        }
      });
    },
    children: [
      {
        path: '/chat',
        name: 'Chat',
        component: ChatListPage,
        icon: 'sms',
      },
      {
        path: '/chat/:id',
        component: ChatPage,
      },
      {
        path: '/contacts',
        name: 'Contacts',
        component: ContactsPage,
        icon: 'account_box',
      },
      {
        path: '/settings',
        name: 'Settings',
        component: Settings,
        icon: 'settings',
      },
      {
        path: '/profile/:username',
        name: 'Profile',
        component: Profile,
      },
      {
        path: '/profilesettings',
        name: 'ProfileSettings',
        component: ProfileSettings,
      },
      {
        path: '/search',
        name: 'Search',
        component: Search,
        icon: 'search',
      },
    ],
  },
  {
    path: '*',
    redirect: '/oauth2/login',
  },
];

export const APP_ROUTES = ROUTES[2].children;

export default ROUTES;
