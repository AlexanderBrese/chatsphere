import Account from './account.graphqls';
import AccountResolvers from './account';

import User from './user.graphqls';
import UserResolvers from './user';

import Contact from './contact.graphqls';
import ContactResolvers from './contact';

import Participant from './participant.graphqls';
import ParticipantResolvers from './participant';

const merge = require('lodash.merge');

export const typeDefs = [
  Account,
  User,
  Contact,
  Participant,
];

export const resolvers = merge(
  {},
  AccountResolvers, UserResolvers, ContactResolvers, ParticipantResolvers,
);
