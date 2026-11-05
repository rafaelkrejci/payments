import http from 'k6/http';
import { check } from 'k6';

const baseUrl = (__ENV.BASE_URL || 'http://localhost:8080').replace(/\/+$/, '');
const startRate = Number(__ENV.START_RATE || 5000);
const endRate = Number(__ENV.END_RATE || 20000);
const holdDuration = __ENV.HOLD_DURATION || '1m';
const rampUpDuration = __ENV.RAMP_UP_DURATION || '1m';
const preAllocatedVUs = Number(__ENV.PREALLOCATED_VUS || 1000);
const maxVUs = Number(__ENV.MAX_VUS || 5000);

function validatePositiveInteger(name, value) {
  if (!Number.isInteger(value) || value <= 0) {
    throw new Error(`${name} must be a positive integer`);
  }
}

validatePositiveInteger('START_RATE', startRate);
validatePositiveInteger('END_RATE', endRate);
validatePositiveInteger('PREALLOCATED_VUS', preAllocatedVUs);
validatePositiveInteger('MAX_VUS', maxVUs);

if (preAllocatedVUs > maxVUs) {
  throw new Error('PREALLOCATED_VUS must not exceed MAX_VUS');
}

export const options = {
  scenarios: {
    create_payments: {
      executor: 'ramping-arrival-rate',
      startRate,
      timeUnit: '1s',
      preAllocatedVUs,
      maxVUs,
      stages: [
        { duration: holdDuration, target: startRate },
        { duration: rampUpDuration, target: endRate },
        { duration: holdDuration, target: endRate },
      ],
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    checks: ['rate>0.99'],
    dropped_iterations: ['count==0'],
  },
};

const payload = JSON.stringify({
  pan: 411111111,
  cvv: 123,
  expirationDate: '12/30',
  cardHolderName: 'Carga',
  cardHolderLastName: 'Teste',
  cardHolderEmail: 'carga.teste@example.invalid',
  cardHolderPhone: '11999999999',
  sku: 'LOAD-TEST',
  amount: 1500,
});

export default function () {
  const response = http.post(`${baseUrl}/api/v1/payments/`, payload, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(response, {
    'payment accepted (HTTP 200)': (res) => res.status === 200,
    'response includes transactionId': (res) => {
      if (res.status !== 200) {
        return false;
      }
      try {
        const body = res.json();
        return typeof body.transactionId === 'string' && body.transactionId.length > 0;
      } catch {
        return false;
      }
    },
  });
}
