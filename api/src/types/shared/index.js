import Notification from './notification.graphqls';
import NotificationResolvers from './notification';

const merge = require('lodash.merge');

export const typeDefs = [
  Notification,
];

export const resolvers = merge(
  {},
  NotificationResolvers,
);
