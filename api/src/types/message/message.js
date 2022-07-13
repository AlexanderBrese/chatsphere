const resolvers = {
  Message: {
    __resolveType(implementor) {
      if (implementor.audio) {
        return 'AudioMessage';
      }

      if (implementor.document) {
        return 'DocumentMessage';
      }

      if (implementor.notify) {
        return 'NotificationMessage';
      }

      if (implementor.picture) {
        return 'PictureMessage';
      }

      if (implementor.VideoMessage) {
        return 'VideoMessage';
      }

      if (implementor.text) {
        return 'PlainMessage';
      }

      return null;
    },
  },
};
export default resolvers;
