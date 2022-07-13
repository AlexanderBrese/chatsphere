export const USERS = [
  {
    id: 0, username: 'admin', phone: '202-555-0138', date: new Date(2010, 1, 10, 8, 12, 12, 12), avatarUrl: '', status: 'mans not hot', displayName: 'admin',
  },
  {
    id: 1, username: 'root', phone: '202-555-0192', date: new Date(1937, 2, 10, 12, 0, 0, 0), avatarUrl: '', status: 'take off your jacket', displayName: 'root',
  },
  {
    id: 2, username: 'user', phone: '', date: new Date(1995, 5, 10, 8, 0, 0, 0), avatarUrl: 'https://randomuser.me/api/portraits/men/93.jpg', status: 'Status', displayName: 'user',
  },
  {
    id: 3, username: 'Sp0ngeB00Bs', phone: '+1-202-555-0134', date: new Date(1997, 10, 10, 9, 0, 0, 0), avatarUrl: 'https://randomuser.me/portraits/men/20.jpg', status: 'no ketchup just sauce', displayName: 'chatsphere',
  },
  {
    id: 4, phone: '(0) 1234 1337', date: new Date(1957, 10, 12, 8, 12, 5, 0), avatarURL: '', status: 'star shaped', displayName: 'Patrick',
  },
  {
    id: 5, phone: '+49 9437 1447', date: new Date(2018, 10, 10, 8, 0, 0, 0), avatar: '', status: 'what\'s the stitch?', displayName: 'Kimmie',
  },
  {
    id: 6, phone: '(0) 1234 1337', date: new Date(1988, 10, 12, 8, 12, 5, 0), avatar: '', status: 'laugh at my name i laugh at your life', displayName: 'PeePeePooPooMan',
  },
  {
    id: 7, phone: '+22 1743 1337', date: new Date(1994, 10, 10, 8, 0, 0, 0), avatar: '', status: '??????', displayName: 'Pipin Paddlepadeskototolus',
  },
  {
    id: 8, phone: '+49 122 234 1337', date: new Date(1935, 10, 12, 8, 12, 5, 0), avatar: '', status: 'Ay ay', displayName: 'Lazlo',
  },
];


export const NOTIFICATION = [
  {
    push: 'INHERIT',
  },
  {
    push: 'MUTE',
  },
  {
    push: 'NOTIFY',
  },
];

export const CHATSTAMP = [
  {
    iconUrl: '', displayName: 'Sp0ngeB00Bs',
  },
  {
    iconUrl: '', displayName: 'BattleSt4r',
  },
  {
    iconUrl: '', displayName: 'GingerAile',
  },
];

export const MESSAGES = [
  {
    __typename: 'PlainMessage',
    id: 0,
    author: USERS[3],
    date: new Date(2018, 8, 9, 0, 0, 0, 0),
    text: 'Hey, Whats UP ?',
    to: {},
  },
  {
    __typename: 'PlainMessage',
    id: 1,
    author: USERS[4],
    date: new Date(2018, 8, 10, 14, 0, 0, 0),
    text: 'Lets go to the GYM ?',
    to: {},
  },
  {
    __typename: 'PlainMessage',
    id: 2,
    author: USERS[5],
    date: new Date(2018, 8, 8, 9, 0, 0, 0),
    text: 'Do you like apples ?',
    to: {},
  },
];

export const CHATS = [
  {
    id: 0,
    participants: [{ username: 'user', hasPrivileges: false },
      { username: 'Sp0ngeB00Bs', hasPrivileges: false }],
    log: [MESSAGES[0]],
    notify: NOTIFICATION[1],
    stamp: CHATSTAMP[0],
  },
  {
    id: 1,
    participants: [{ username: 'user', hasPrivileges: false },
      { username: 'BattleSt4r', hasPrivileges: false }],
    log: [MESSAGES[1]],
    notify: NOTIFICATION[1],
    stamp: CHATSTAMP[0],
  },
  {
    id: 2,
    participants: [{ username: 'user', hasPrivileges: false },
      { username: 'BattleSt4r', hasPrivileges: false },
      { username: 'GingerAile', hasPrivileges: false }],
    log: [MESSAGES[2]],
    notify: NOTIFICATION[2],
    stamp: CHATSTAMP[1],
    isPublic: true,
  },
];

export const ACCOUNTS = [
  {
    user: USERS[0], email: 'admin@chatsphere.de', contacts: [], blocked: [], notify: undefined, chats: [],
  },
  {
    user: USERS[1], email: 'root@chatsphere.de', contacts: [], blocked: [], notify: undefined, chats: [],
  },
  {
    user: USERS[2], email: 'user@chatsphere.de', contacts: [{ user: USERS[3], nickname: 'SpongeNickname' }], blocked: [], notify: NOTIFICATION[2], chats: CHATS,
  },
  {
    user: USERS[3], email: 'spongey@bikinimail.com', contacts: [], blocked: [], notify: undefined, chats: [CHATS[0]],
  },
  {
    user: USERS[4], username: 'BattleSt4r', email: 'patrick@bikinimail.com', contacts: [], blocked: [], notify: undefined, chats: [CHATS[1], CHATS[2]],
  },
  {
    user: USERS[5], username: 'GingerAile', email: 'patrick@bikinimail.com', contacts: [], blocked: [], notify: undefined, chats: [CHATS[2]],
  },
  {
    user: USERS[6], username: 'PeeP@oo', email: 'poooo@ooo.op', contacts: [], blocked: [], notify: undefined, chats: [],
  },
  {
    user: USERS[7], username: 'paddlepadesko', email: 'pipin@linked.in', contacts: [], blocked: [], notify: undefined, chats: [],
  },
  {
    user: USERS[8], username: 'Lazlo00008', email: 'i<3nature@interwebs.com', contacts: [], blocked: [], notify: undefined, chats: [],
  },
];

export const ACCOUNT = ACCOUNTS[2];
