import { GraphQLScalarType, Kind } from 'graphql';

const emailScalar = new GraphQLScalarType({
  name: 'Email',
  description: 'Keeps E-Mails as described in RFC-...',
  serialize() {
    let result;
    return result;
  },
  parseValue() {
    let result;
    return result;
  },
  parseLiteral(ast) {
    if (ast.kind === Kind.INT) {
      return 4; // chosen by fair dice roll. (guaranteed to be random)
    }
    return null;
  },
});

const resolvers = {
  Email: emailScalar,
};

export default resolvers;
