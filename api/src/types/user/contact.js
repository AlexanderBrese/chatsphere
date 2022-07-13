import { ACCOUNT, ACCOUNTS } from '../../mocks';

const resolvers = {
  Mutation: {
    removeContact(obj, { removeContactInput }) {
      console.log('\n Remove Contact Input', removeContactInput);
      console.log('\n contacts before remove \n\n\n', ACCOUNT.contacts);
      const deleteContact = ACCOUNT.contacts
        .filter(contact => contact.user.id === removeContactInput.contactUsername)[0];

      ACCOUNT.contacts = ACCOUNT.contacts
        .filter(contact => contact.user.username !== removeContactInput.contactUsername);
      console.log('\n contacts after remove \n\n\n', ACCOUNT.contacts);

      return deleteContact;
    },

    updateNickname(obj, { updateNicknameInput }) {
      console.log('\n Update Nickname Input', updateNicknameInput);
      console.log('\n contacts before update name \n\n\n', ACCOUNT.contacts);
      ACCOUNT.contacts[ACCOUNT.contacts
        .findIndex(contact => contact.user.username === updateNicknameInput.contactUsername)]
        .nickname = updateNicknameInput.nickname;
      console.log('\n contacts after update name \n\n\n', ACCOUNT.contacts);
      return ACCOUNT
        .contacts[ACCOUNT.contacts
          .findIndex(contact =>
            contact.user.username === updateNicknameInput.contactUsername)];
    },

    blockContact(obj, { blockContactInput }) {
      console.log('\n Block Contact Input \n\n\n', blockContactInput);
      const searchAcc = ACCOUNTS
        .filter(acc => acc.user.username === blockContactInput.contactUsername)[0];
      console.log(searchAcc.user.username);
      const con = ACCOUNT.contacts
        .filter(contact => contact.user.username === searchAcc.user.username)[0];
      console.log('\n block Contact', con);
      console.log('\n blocked before block \n\n\n', ACCOUNT.blocked);
      ACCOUNT.blocked.push(con);
      console.log('\n blocked after block \n\n\n', ACCOUNT.blocked);
      return true;
    },

    unblockContact(obj, { unblockContactInput }) {
      console.log('\n Unlock Contact Input \n\n\n', unblockContactInput);
      console.log('\n blocked before block \n\n\n', ACCOUNT.blocked);
      const searchAcc = ACCOUNTS
        .filter(acc => acc.user.username === unblockContactInput.contactUsername)[0];
      console.log(searchAcc.user.username);
      ACCOUNT.blocked = ACCOUNT.blocked
        .filter(contact => contact.user.username !== searchAcc.user.username);
      console.log('\n blocked after block \n\n\n', ACCOUNT.blocked);
      return true;
    },
  },
};

export default resolvers;
