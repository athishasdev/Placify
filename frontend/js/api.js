// ========== Placify API Client ==========
const API_BASE = 'http://localhost:8083/api';

const api = {
    async request(url, options = {}) {
        let userEmail = null;
        try {
            const user = JSON.parse(localStorage.getItem('placify_user'));
            if (user && user.email) userEmail = user.email;
        } catch (e) {}

        const config = {
            credentials: 'omit', // No longer need cookies for auth
            headers: { 
                'Content-Type': 'application/json',
                ...(userEmail ? { 'X-User-Email': userEmail } : {}),
                ...options.headers 
            },
            ...options,
        };
        // Remove Content-Type for FormData
        if (options.body instanceof FormData) {
            delete config.headers['Content-Type'];
        }
        try {
            const res = await fetch(`${API_BASE}${url}`, config);
            if (res.status === 401 || res.status === 403) {
                const current = window.location.pathname;
                if (!current.includes('login') && !current.includes('register') && !current.includes('index')) {
                    localStorage.removeItem('placify_user');
                    // Use relative path to work on both file:// and http://
                    const pathParts = window.location.pathname.split('/');
                    const inPagesDir = pathParts[pathParts.length - 2] === 'pages';
                    window.location.href = inPagesDir ? 'login.html' : 'pages/login.html';
                }
                throw new Error('Authentication required');
            }
            const data = await res.json();
            return data;
        } catch (err) {
            if (err.message === 'Authentication required') throw err;
            console.error('API Error:', err);
            throw err;
        }
    },

    // Auth
    login(email, password) {
        return this.request('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) });
    },
    register(name, email, password) {
        return this.request('/auth/register', { method: 'POST', body: JSON.stringify({ name, email, password }) });
    },
    logout() {
        return this.request('/auth/logout', { method: 'POST' });
    },
    me() {
        return this.request('/auth/me');
    },

    // Resume
    uploadResume(file) {
        const formData = new FormData();
        formData.append('file', file);
        return this.request('/resume/upload', { method: 'POST', body: formData });
    },
    getResume(id) {
        return this.request(`/resume/${id}`);
    },
    getLatestResume() {
        return this.request('/resume/latest');
    },

    // Roles
    getRoles() {
        return this.request('/roles');
    },
    getRole(id) {
        return this.request(`/roles/${id}`);
    },
    createRole(data) {
        return this.request('/roles', { method: 'POST', body: JSON.stringify(data) });
    },
    updateRole(id, data) {
        return this.request(`/roles/${id}`, { method: 'PUT', body: JSON.stringify(data) });
    },
    deleteRole(id) {
        return this.request(`/roles/${id}`, { method: 'DELETE' });
    },

    // Analysis
    analyze(jobRoleId, resumeId) {
        const body = { jobRoleId };
        if (resumeId) body.resumeId = resumeId;
        return this.request('/analyze', { method: 'POST', body: JSON.stringify(body) });
    },
    getReport(id) {
        return this.request(`/report/${id}`);
    },
    getMyReports() {
        return this.request('/reports');
    },

    // Dashboard
    studentDashboard() {
        return this.request('/dashboard/student');
    },
    adminDashboard() {
        return this.request('/dashboard/admin');
    },
};
