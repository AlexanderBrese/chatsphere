/* beautify preserve:start */
import { makeExecutableSchema } from 'graphql-tools';
/* beautify preserve:end */

import Schema from './schema.graphqls';

// Types
import {
  typeDefs as Types,
  resolvers as TypesResolvers,
} from './types';

// Scalars
import {
  typeDefs as Scalars,
  resolvers as ScalarsResolvers,
} from './scalars';

const merge = require('lodash.merge');

const concat = require('lodash.concat');

// Put together a schema
const schema = makeExecutableSchema({
  typeDefs: concat(Schema, Types, Scalars),

  resolvers: merge(
    TypesResolvers,
    ScalarsResolvers,
  ),
});

export default schema;
