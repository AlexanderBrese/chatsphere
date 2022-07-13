import { createServer } from 'http';

import app from './server';

const server = createServer(app);
let currentApp = app;

server.listen(3000, () => {
  /* eslint-disable no-console */
  console.log('GraphQL-server listening on port 3000.');
  console.log('GraphQL Endpoint http://localhost:3000/graphql');
  console.log('GraphiQl Query Editor http://localhost:3000/graphiql');
  console.log('GraphQL Playground http://localhost:3000/live');
  console.log('Voyager Query http://localhost:3000/voyager/query');
  console.log('Voyager Mutation http://localhost:3000/voyager/mutation');
  console.log('Voyager Subscription http://localhost:3000/voyager/subscription');
  /* eslint-disable no-console */
});

if (module.hot) {
  module.hot.accept(['./server', './schema'], () => {
    server.removeListener('request', currentApp);
    server.on('request', app);
    currentApp = app;
  });
}
