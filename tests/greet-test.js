import http from 'k6/http';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';
import { sleep } from 'k6';

export const options = {
  duration: '5m', // total duration of the test
  vus: 50, // number of Virtual Users
  thresholds: {
    http_req_duration: ['p(95)<500'],  // 95% of requests must complete below 500ms
  },
};

export default function () {
  const res = http.get('http://127.0.0.1:8181/api/trivydemo/greet');
  console.log(`Response body: ${res.body}`);
  console.log(`Status: ${res.status}`);
  console.log(`Headers: ${JSON.stringify(res.headers)}`);
  sleep(1);
}

export function handleSummary(data) {
  return {
    'stdout': textSummary(data, { indent: ' ', enableColors: true }), // Show summary in the console
    'greet-test-summary.json': JSON.stringify(data), // Save JSON summary to a file
  };
}