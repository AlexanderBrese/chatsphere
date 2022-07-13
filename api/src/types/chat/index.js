import Chat from './chat.graphqls';
import ChatResolvers from './chat';

import PrivateChat from './private-chat.graphqls';
import PrivateChatResolvers from './private-chat';

import GroupChat from './group-chat.graphqls';
import GroupChatResolvers from './group-chat';

import ChatStamp from './chat-stamp.graphqls';
import ChatStampResolvers from './chat-stamp';

const merge = require('lodash.merge');

export const typeDefs = [
  Chat,
  GroupChat,
  PrivateChat,
  ChatStamp,
];

export const resolvers = merge(
  {},
  ChatResolvers, PrivateChatResolvers, GroupChatResolvers, ChatStampResolvers,
);
