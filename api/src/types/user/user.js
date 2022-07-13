/* eslint-disable no-console */
import { ACCOUNT, ACCOUNTS } from '../../mocks';

export const resolvers = {
  Query: {
    user(obj, { username }) {
      console.log('\n Username: \n\n\n', username);
      const usr = ACCOUNTS.filter(acc => acc.user.username === username)[0].user;
      console.log('\n User Object: \n\n\n', usr);
      return usr;
    },
  },

  Mutation: {
    updatePhone(obj, { updatePhoneInput }) {
      console.log('\n Phone Input \n\n\n', updatePhoneInput);
      console.log('\n User before phone change \n\n\n', ACCOUNT.user);
      ACCOUNT.user.phone = updatePhoneInput.phone;
      console.log('\n User after phone change \n\n\n', ACCOUNT.user);
      return ACCOUNT.user;
    },

    updateDisplayName(obj, { updateDisplayNameInput }) {
      console.log('\n displayName Input \n\n\n', updateDisplayNameInput);
      console.log('\n User before displayName change \n\n\n', ACCOUNT.user);
      ACCOUNT.user.displayName = updateDisplayNameInput.displayName;
      console.log('\n User after displayName change \n\n\n', ACCOUNT.user);
      return ACCOUNT.user;
    },

    updateStatus(obj, { updateStatusInput }) {
      console.log('\n Status Input \n\n\n', updateStatusInput);
      console.log('\n User before status change \n\n\n', ACCOUNT.user);
      ACCOUNT.user.status = updateStatusInput.status;
      console.log('\n User after status change \n\n\n', ACCOUNT.user);
      return ACCOUNT.user;
    },

    reportUser(obj, { reportUserInput }) {
      console.log('\n Report Input \n\n\n', reportUserInput);
      return true;
    },
  },
};
export default resolvers;
