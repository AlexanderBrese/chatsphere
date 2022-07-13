import Message from './message.graphqls';
import MessageResolvers from './message';

import PlainMessage from './plain-message.graphqls';
import PlainMessageResolvers from './plain-message';

import NotificationMessage from './notification-message.graphqls';
import NotificationMessageResolvers from './notification-message';

import DocumentMessage from './document-message.graphqls';
import DocumentMessageResolvers from './document-message';

import AudioMessage from './audio-message.graphqls';
import AudioMessageResolvers from './audio-message';

import PictureMessage from './picture-message.graphqls';
import PictureMessageResolvers from './picture-message';

import VideoMessage from './video-message.graphqls';
import VideoMessageResolvers from './video-message';

const merge = require('lodash.merge');

export const typeDefs = [
  Message,
  PlainMessage,
  NotificationMessage,
  DocumentMessage,
  AudioMessage,
  PictureMessage,
  VideoMessage,
];

export const resolvers = merge(
  {},
  MessageResolvers, PlainMessageResolvers, NotificationMessageResolvers,
  DocumentMessageResolvers, AudioMessageResolvers, PictureMessageResolvers,
  VideoMessageResolvers,
);
