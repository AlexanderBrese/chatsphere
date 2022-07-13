import capitalize from './capitalize';
import abbreviate from './abbreviate';
import uppercase from './uppercase';

export const CAPITALIZE = {
  name: 'capitalize',
  handler: capitalize,
};

export const ABBREVIATE = {
  name: 'abbreviate',
  handler: abbreviate,
};

export const UPPERCASE = {
  name: 'uppercase',
  handler: uppercase,
};
