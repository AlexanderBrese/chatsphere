/* eslint-disable no-console */
import ApolloClient from 'apollo-client';
import gql from 'graphql-tag';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { SubscriptionClient } from 'subscriptions-transport-ws';
import { WebSocketLink } from 'apollo-link-ws';

const GRAPHQL_ENDPOINT = 'wss://api.chatsphere.de/graphql'; // test locally: 'ws://localhost:8080/graphql';

console.log(GRAPHQL_ENDPOINT);

const client = new SubscriptionClient(GRAPHQL_ENDPOINT, {
  reconnect: true,
}, WebSocket);


const link = new WebSocketLink(client);
const apolloClient = new ApolloClient({
  link,
  cache: new InMemoryCache(),
  connectToDevTools: true,
});

const GET_ALL_LINKS = gql`
  query allLinks {
    allLinks {
      id
      url
      description
    }
  }
`;

describe('GraphQL Query alllinks', () => {
  /* eslint-disable no-undef */
  beforeEach(() => {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 60000;
  });

  it('Query allLinks', (done) => {
    apolloClient.query({
      query: GET_ALL_LINKS,
    }).then(({ data }) => {
      expect(data.allLinks instanceof Array).toBe(true);
      done();
    }).catch(err => {
      console.log(err);
    });
  });
});
