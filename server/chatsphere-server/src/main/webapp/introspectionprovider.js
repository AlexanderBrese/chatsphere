      // Defines a GraphQL introspection fetcher using the fetch API. You're not required to
      // use fetch, and could instead implement introspectionProvider however you like,
      // as long as it returns a Promise
      // Voyager passes introspectionQuery as an argument for this function
      function introspectionProvider(introspectionQuery) {
        // This example expects a GraphQL server at the path /graphql.
        // Change this to point wherever you host your GraphQL server.
        return fetch('/graphql', {
          method: 'post',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({query: introspectionQuery}),
          credentials: 'include',
        }).then(function (response) {
          return response.text();
        }).then(function (responseBody) {
          try {
            return JSON.parse(responseBody);
          } catch (error) {
            return responseBody;
          }
        });
      }
