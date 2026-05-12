/**
 * QuanAn App — main.js
 * Shared utilities, API helpers, cart state, toast notifications
 * Base URL: http://localhost:8080
 */

const API = 'http://localhost:8080/api';

// ============================================================
// API HELPERS
// ============================================================
async function apiFetch(path, options = {}) {
  const defaults = { headers: { 'Content-Type': 'application/json' } };
  const cfg = { ...defaults, ...options, headers: { ...defaults.headers, ...options.headers } };
  try {
    const res = await fetch(API + path, cfg);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    if (res.status === 204) return null;
    return await res.json();
  } catch (e) {
    console.error('API error:', path, e);
    throw e;
  }
}

const api = {
  get:    (path)         => apiFetch(path),
  post:   (path, data)   => apiFetch(path, { method: 'POST',   body: JSON.stringify(data) }),
  put:    (path, data)   => apiFetch(path, { method: 'PUT',    body: JSON.stringify(data) }),
  delete: (path)         => apiFetch(path, { method: 'DELETE' }),
};

// ============================================================
// CART STATE (localStorage)
// ============================================================
const CART_KEY = 'quanan_cart';

const Cart = {
  getAll() {
    try { return JSON.parse(localStorage.getItem(CART_KEY)) || []; }
    catch { return []; }
  },
  save(items) {
    localStorage.setItem(CART_KEY, JSON.stringify(items));
    Cart.updateUI();
  },
  add(item) {
    const items = Cart.getAll();
    const idx = items.findIndex(i => i.monID === item.monID);
    if (idx >= 0) { items[idx].qty += 1; }
    else { items.push({ ...item, qty: 1 }); }
    Cart.save(items);
    toast(`Đã thêm "${item.tenMon}" vào giỏ`, 'success');
  },
  remove(monID) {
    const items = Cart.getAll().filter(i => i.monID !== monID);
    Cart.save(items);
  },
  setQty(monID, qty) {
    const items = Cart.getAll();
    const idx = items.findIndex(i => i.monID === monID);
    if (idx < 0) return;
    if (qty <= 0) { items.splice(idx, 1); }
    else { items[idx].qty = qty; }
    Cart.save(items);
  },
  clear() { Cart.save([]); },
  total() { return Cart.getAll().reduce((s, i) => s + i.gia * i.qty, 0); },
  count() { return Cart.getAll().reduce((s, i) => s + i.qty, 0); },
  updateUI() {
    const c = Cart.count();
    document.querySelectorAll('.cart-count').forEach(el => el.textContent = c);
    document.querySelectorAll('.cart-total-display').forEach(el => el.textContent = fmt(Cart.total()));
    if (typeof renderCartItems === 'function') renderCartItems();
  },
};

// ============================================================
// TOAST NOTIFICATIONS
// ============================================================
function toast(msg, type = 'info', duration = 2800) {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const el = document.createElement('div');
  const icon = type === 'success' ? '✅' : type === 'error' ? '❌' : type === 'warning' ? '⚠️' : 'ℹ️';
  el.className = `toast ${type}`;
  el.innerHTML = `<span>${icon}</span><span>${msg}</span>`;
  container.appendChild(el);
  setTimeout(() => { el.style.opacity = '0'; el.style.transition = 'opacity .4s'; setTimeout(() => el.remove(), 400); }, duration);
}

// ============================================================
// FORMATTING
// ============================================================
function fmt(n) {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(n);
}

function fmtDate(str) {
  if (!str) return '—';
  return new Date(str).toLocaleString('vi-VN', { dateStyle: 'short', timeStyle: 'short' });
}

// ============================================================
// MODAL HELPERS
// ============================================================
function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

document.addEventListener('click', e => {
  if (e.target.matches('.modal-overlay')) { e.target.classList.remove('open'); }
  if (e.target.matches('[data-close-modal]')) { closeModal(e.target.dataset.closeModal); }
});

// ============================================================
// CART PANEL TOGGLE
// ============================================================
document.addEventListener('click', e => {
  const btn = e.target.closest('.cart-btn');
  if (btn) {
    const panel = document.getElementById('cartPanel');
    if (panel) panel.classList.toggle('open');
  }
  const close = e.target.closest('[data-close-cart]');
  if (close) { document.getElementById('cartPanel')?.classList.remove('open'); }
});

// ============================================================
// TABS
// ============================================================
document.addEventListener('click', e => {
  const tab = e.target.closest('.tab-btn');
  if (!tab) return;
  const parent = tab.closest('[data-tabs]') || tab.closest('.tabs')?.parentElement;
  if (!parent) return;
  parent.querySelectorAll('.tab-btn').forEach(t => t.classList.remove('active'));
  parent.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));
  tab.classList.add('active');
  const target = document.getElementById(tab.dataset.tab);
  if (target) target.classList.add('active');
});

// ============================================================
// SEARCH FILTER HELPER
// ============================================================
function filterTable(inputEl, tableEl) {
  const q = inputEl.value.toLowerCase();
  tableEl.querySelectorAll('tbody tr').forEach(row => {
    row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
  });
}

// ============================================================
// STATUS LABEL
// ============================================================
const STATUS_LABELS = {
  PENDING:     'Chờ xác nhận',
  CONFIRMED:   'Đã xác nhận',
  PREPARING:   'Đang chuẩn bị',
  READY:       'Sẵn sàng',
  DELIVERED:   'Đã giao',
  CANCELLED:   'Đã hủy',
  AVAILABLE:   'Có sẵn',
  UNAVAILABLE: 'Hết hàng',
};

function statusBadge(s) {
  return `<span class="status status-${s}">${STATUS_LABELS[s] || s}</span>`;
}

// ============================================================
// FOOD EMOJI MAP
// ============================================================
const FOOD_EMOJI = {
  'phở':      '🍜', 'bún':  '🍜', 'cơm':   '🍚', 'bánh': '🥐',
  'gà':       '🍗', 'bò':   '🥩', 'heo':   '🥩', 'tôm':  '🦐',
  'cá':       '🐟', 'rau':  '🥗', 'canh':  '🍲', 'lẩu':  '🫕',
  'trà':      '🍵', 'cà phê':'☕', 'nước': '🥤', 'sinh tố':'🧃',
  'bia':      '🍺', 'pepsi':'🥤',
};

function getFoodEmoji(name = '') {
  const n = name.toLowerCase();
  for (const [k, v] of Object.entries(FOOD_EMOJI)) { if (n.includes(k)) return v; }
  return '🍽️';
}

// initialise cart count on page load
document.addEventListener('DOMContentLoaded', () => Cart.updateUI());