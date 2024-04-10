import { render, screen } from '@testing-library/react';
import App from './App';

// Test if the reCAPTCHA public key is present in the Config.js file
test('reCAPTCHA public key is present', () => {
  const config = require('./Config.js');
  expect(config.config.reCAPTCHA_public_key).toBeTruthy();
});

test('API URL is present', () => {
  const config = require('./Config.js');
  expect(config.config.api_url).toBeTruthy();
});

test('API URL has trailing slash', () => {
  const config = require('./Config.js');
  expect(config.config.api_url.endsWith('/')).toBeTruthy();
});
