export default value => {
  if (!value) return '';
  const newValue = value.toString();
  return newValue.toUpperCase();
};
