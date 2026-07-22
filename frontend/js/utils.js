// ========== Placify Utilities ==========

function showToast(message, type = 'info') {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    const icons = { success: '✓', error: '✕', info: 'ℹ' };
    toast.innerHTML = `<span>${icons[type] || 'ℹ'}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
}

function showLoading() {
    let overlay = document.querySelector('.loading-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.className = 'loading-overlay';
        overlay.innerHTML = '<div class="spinner"></div>';
        document.body.appendChild(overlay);
    }
    overlay.style.display = 'flex';
}

function hideLoading() {
    const overlay = document.querySelector('.loading-overlay');
    if (overlay) overlay.style.display = 'none';
}

function getUser() {
    try { return JSON.parse(localStorage.getItem('placify_user')); }
    catch { return null; }
}

function setUser(user) {
    localStorage.setItem('placify_user', JSON.stringify(user));
}

function clearUser() {
    localStorage.removeItem('placify_user');
}

function requireAuth() {
    const user = getUser();
    if (!user) { 
        const inPagesDir = window.location.pathname.split('/').slice(-2)[0] === 'pages';
        window.location.href = inPagesDir ? 'login.html' : 'pages/login.html'; 
        return null; 
    }
    return user;
}

function requireAdmin() {
    const user = requireAuth();
    if (user && user.role !== 'ADMIN') { 
        const inPagesDir = window.location.pathname.split('/').slice(-2)[0] === 'pages';
        window.location.href = inPagesDir ? 'dashboard.html' : 'pages/dashboard.html'; 
        return null; 
    }
    return user;
}

function renderNavbar(user) {
    const nav = document.getElementById('navbar');
    if (!nav) return;

    const inPagesDir = window.location.pathname.split('/').slice(-2)[0] === 'pages';
    const pre = inPagesDir ? '../' : '';
    const pg = inPagesDir ? '' : 'pages/';

    if (!user) {
        nav.innerHTML = `
            <div class="container">
                <a href="${pre}index.html" class="nav-brand">Placify</a>
                <div class="nav-links">
                    <a href="${pg}login.html">Login</a>
                    <a href="${pg}register.html" class="nav-btn-primary">Sign Up</a>
                </div>
            </div>`;
        return;
    }

    const initial = user.name ? user.name.charAt(0).toUpperCase() : '?';
    const isAdmin = user.role === 'ADMIN';

    nav.innerHTML = `
        <div class="container">
            <a href="${pg}dashboard.html" class="nav-brand">Placify</a>
            <div class="nav-links">
                <a href="${pg}dashboard.html">Dashboard</a>
                ${isAdmin ? `<a href="${pg}admin.html">Admin</a>` : ''}
                ${!isAdmin ? `<a href="${pg}upload.html">Upload Resume</a>` : ''}
                ${!isAdmin ? `<a href="${pg}analysis.html">Analyze</a>` : ''}
                <div class="nav-user">
                    <div class="nav-avatar">${initial}</div>
                    <button onclick="handleLogout()" class="btn-sm">Logout</button>
                </div>
            </div>
        </div>`;
}

async function handleLogout() {
    try {
        await api.logout();
    } catch(e) { /* ignore */ }
    clearUser();
    const inPagesDir = window.location.pathname.split('/').slice(-2)[0] === 'pages';
    window.location.href = inPagesDir ? '../index.html' : 'index.html';
}

function createScoreRing(score, size = 140) {
    const radius = (size - 16) / 2;
    const circumference = 2 * Math.PI * radius;
    const offset = circumference - (score / 100) * circumference;
    let color = '#ef4444';
    if (score >= 70) color = '#10b981';
    else if (score >= 40) color = '#f59e0b';

    return `
        <div class="score-ring" style="width:${size}px;height:${size}px;">
            <svg viewBox="0 0 ${size} ${size}">
                <circle class="ring-bg" cx="${size/2}" cy="${size/2}" r="${radius}"/>
                <circle class="ring-fill" cx="${size/2}" cy="${size/2}" r="${radius}"
                    stroke="${color}" stroke-dasharray="${circumference}"
                    stroke-dashoffset="${offset}"/>
            </svg>
            <span class="score-value" style="color:${color}">${score}%</span>
        </div>`;
}

function getScoreBadge(score) {
    if (score >= 70) return '<span class="badge badge-success">Strong Match</span>';
    if (score >= 40) return '<span class="badge badge-warning">Partial Match</span>';
    return '<span class="badge badge-danger">Needs Improvement</span>';
}
