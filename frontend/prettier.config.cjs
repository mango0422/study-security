// prettier.config.js
/** @type {import("prettier").Options} */
module.exports = {
  semi: false,
  singleQuote: true,
  tabWidth: 2,
  trailingComma: 'es5',
  arrowParens: 'avoid',
  printWidth: 100,
  plugins: ['prettier-plugin-tailwindcss'],
}
