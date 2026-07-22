// ========== Placify Environment & API Configuration ==========
(function () {
    const hostname = window.location.hostname;
    const isLocalhost = hostname === 'localhost' || hostname === '127.0.0.1' || hostname === '';

    // Environment-driven API Base URL (defaults to http://localhost:8080/api for local dev, /api for prod)
    const defaultLocalApi = 'http://localhost:8080/api';
    const defaultProdApi = '/api';

    window.CONFIG = {
        API_BASE_URL: window.ENV_API_BASE_URL || (isLocalhost ? defaultLocalApi : defaultProdApi),
        DEMO_ACCOUNTS: {
            ADMIN_EMAIL: 'admin@placify.com',
            ADMIN_PASS: 'admin123',
            STUDENT_EMAIL: 'student@placify.com',
            STUDENT_PASS: 'student123'
        }
    };
})();
