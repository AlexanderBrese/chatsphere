import {
  typeDefs as Shared,
  resolvers as SharedResolvers,
} from './shared';

import {
  typeDefs as User,
  resolvers as UserResolvers,
} from './user';

import {
  typeDefs as Chat,
  resolvers as ChatResolvers,
} from './chat';

import {
  typeDefs as Message,
  resolvers as MessageResolvers,
} from './message';

const merge = require('lodash.merge');
const concat = require('lodash.concat');

export const typeDefs = concat(Shared, User, Chat, Message);

export const resolvers = merge(
  {},
  UserResolvers,
  SharedResolvers,
  ChatResolvers,
  MessageResolvers,
);
