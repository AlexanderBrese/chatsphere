import Date from './date.graphqls';
import DateResolvers from './date';

import Email from './email.graphqls';
import EmailResolvers from './email';

const merge = require('lodash.merge');

export const typeDefs = [
  Date,
  Email,
];

export const resolvers = merge(
  EmailResolvers,
  DateResolvers,
);
