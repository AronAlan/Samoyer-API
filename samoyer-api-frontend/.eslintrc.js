module.exports = {
  extends: [require.resolve('@umijs/lint/dist/config/eslint')],
  globals: {
    page: true,
    REACT_APP_ENV: true,
  },
  rules: {
    '@typescript-eslint/no-unused-vars': 'off', // 禁用此规则
    // 其他规则...
  },
};
