import express from 'express';
import bodyParser from 'body-parser';
import {
  graphqlExpress,
  graphiqlExpress,
} from 'graphql-server-express';
import { express as voyagerExpress } from 'graphql-voyager/middleware';
import expressPlayground from 'graphql-playground-middleware-express';
import cors from 'cors';

import schema from './schema';

const app = express();

if (process.env.NODE_ENV === 'development') {
  app.use('*', cors({ origin: 'http://localhost:3020' }));
}

app.use(
  '/graphiql',
  graphiqlExpress({
    endpointURL: '/graphql',
  }),
);

app.use(
  '/live',
  expressPlayground({
    endpointUrl: '/graphql',
  }),
);

app.use(
  '/voyager/query',
  voyagerExpress({
    endpointUrl: '/graphql',
    displayOptions: {
      rootType: 'Query',
    },
  }),
);

app.use(
  '/voyager/mutation',
  voyagerExpress({
    endpointUrl: '/graphql',
    displayOptions: {
      rootType: 'Mutation',
    },
  }),
);

app.use(
  '/voyager/subscription',
  voyagerExpress({
    endpointUrl: '/graphql',
    displayOptions: {
      rootType: 'Subscription',
    },
  }),
);

app.use('/graphql', bodyParser.json(), graphqlExpress({
  schema,
}));

export default app;
