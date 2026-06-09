import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const API = `${BASE_URL}/api`;

export const options = {
    scenarios: {
        operator_workflow: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '15s', target: 10 },
                { duration: '15s', target: 30 },
                { duration: '60s', target: 50 },
                { duration: '10s', target: 0 },
            ],
            gracefulRampDown: '10s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<3000'],
        http_req_failed: ['rate<0.15'],
        checks: ['rate>0.85'],
    },
};

const jsonHeaders = { headers: { 'Content-Type': 'application/json' } };

export default function operatorScenario() {
    const satelliteName = `LoadSat-VU${__VU}-IT${__ITER}-${Date.now()}`;

    group('1. Получение обзора группировок', () => {
        const response = http.get(`${API}/overview`);
        check(response, {
            'overview: статус 200': (r) => r.status === 200,
        });
    });

    group('2. Добавление спутника в группировку', () => {
        const payload = JSON.stringify({
            param: {
                type: 'COMMUNICATION',
                name: satelliteName,
                batteryLevel: 400 + (__VU % 100),
                bandwidth: 0.75,
            },
            communicationName: 'Орбита-1',
        });

        const response = http.post(`${API}/add-satellites`, payload, jsonHeaders);
        check(response, {
            'add-satellite: статус 200': (r) => r.status === 200,
        });
    });

    group('3. Запуск миссии для группировки', () => {
        const payload = JSON.stringify({
            constellationName: 'Орбита-1',
            missionType: __ITER % 2 === 0 ? 'IMAGING' : 'COMMUNICATION',
        });

        const response = http.post(`${API}/missions`, payload, jsonHeaders);
        check(response, {
            'mission: статус 200': (r) => r.status === 200,
        });
    });

    group('4. Удаление спутника', () => {
        const response = http.del(`${API}/satellites/${encodeURIComponent(satelliteName)}`);
        check(response, {
            'delete-satellite: статус 200': (r) => r.status === 200,
        });
    });

    sleep(0.5 + Math.random() * 0.5);
}

export function handleSummary(data) {
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    return {
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
        [`reports/load-test-report-${timestamp}.html`]: htmlReport(data),
        [`reports/load-test-report-${timestamp}.json`]: JSON.stringify(data, null, 2),
        'reports/load-test-report-latest.html': htmlReport(data),
    };
}
