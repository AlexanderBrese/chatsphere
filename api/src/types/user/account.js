/* eslint-disable no-console */
import { ACCOUNTS, ACCOUNT, USERS } from '../../mocks';

const resolvers = {
  Query: {
    account() {
      console.log('ACCOUNT\n\n\n', ACCOUNT);
      return ACCOUNT;
    },
    usernameTaken(obj, { username }) {
      const free = ACCOUNTS.every(account => account.username !== username);
      console.log(!free);
      return !free;
    },
    login(obj, { loginInput }) {
      console.log(loginInput);
      const exists = ACCOUNTS.filter(acc => acc.username === loginInput.username).length > 0;
      console.log(exists);

      console.log('\n ACCOUNT before Login \n\n\n', ACCOUNT);
      const changeAccount = ACCOUNTS.filter(acc => acc.username === loginInput.username)[0];
      ACCOUNT.user = changeAccount.user;
      ACCOUNT.username = changeAccount.username;
      ACCOUNT.email = changeAccount.email;
      ACCOUNT.contacts = changeAccount.contacts;
      ACCOUNT.blocked = changeAccount.blocked;
      ACCOUNT.notify = changeAccount.notify;
      ACCOUNT.chats = changeAccount.chats;
      console.log('\n ACCOUNT after Login \n\n\n', ACCOUNT);

      return exists;
    },
  },
  Mutation: {
    createAccount(obj, { createAccountInput }) {
      const newUser = {
        id: USERS.length,
        phone: '',
        date: Date.now(),
        avatar: '',
        status: '',
        displayName: createAccountInput.username,
      };

      const newAccount = {
        user: newUser,
        username: createAccountInput.username,
        email: createAccountInput.email,
        contacts: [],
        blocked: [],
        notify: undefined,
        chats: [],
      };
      console.log(newUser);
      console.log(newAccount);

      USERS.push(newUser);
      ACCOUNTS.push(newAccount);

      return null;
    },

    updateNotificationSettings(obj, { updateNotificationInput }) {
      console.log(updateNotificationInput);
      console.log(ACCOUNT.notify);
      ACCOUNT.notify.push = updateNotificationInput.notify.push;
      console.log(ACCOUNT.notify);
    },

    updateEmail(obj, { updateEmailInput }) {
      console.log('\n Email Input \n\n\n', updateEmailInput);
      console.log('\n Account before email change \n\n\n', ACCOUNT);
      ACCOUNT.email = updateEmailInput.email;
      console.log('\n Account after email change \n\n\n', ACCOUNT);
      return ACCOUNT;
    },

    updatePassword(obj, { updatePasswordInput }) {
      console.log('\n Password Input \n\n\n', updatePasswordInput);
      return ACCOUNT;
    },
  },
};
export default resolvers;
