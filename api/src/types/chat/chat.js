/* eslint-disable no-console */
const resolvers = {
  Chat: {
    __resolveType(implementor) {
      if (implementor.isPublic !== undefined) {
        return 'GroupChat';
      }

      return 'PrivateChat';
    },
  },
};
export default resolvers;
