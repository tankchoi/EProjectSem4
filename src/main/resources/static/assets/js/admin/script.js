const toggleButton = document.querySelector('.dropdown-toggle');
const menu = document.querySelector('.dropdown-menu');
const menuToggle = document.querySelector('.menu-toggle');
const navSide = document.querySelector('.nav-aside');

// --- Điều khiển Side Navigation ---
if (menuToggle && navSide) {
    menuToggle.addEventListener('click', () => {
        navSide.classList.toggle('is-collapsed');
    });
}

// --- Điều khiển Dropdown Menu ---
if (toggleButton && menu) {
    toggleButton.addEventListener('click', (e) => {
        e.stopPropagation();
        menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
    });

    // --- Đóng Dropdown khi click ra ngoài ---
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.dropdown')) {
            menu.style.display = 'none';
        }
    });
}

// --- Xử lý Search ---
const searchInput = document.querySelector('.search-contain input');
if (searchInput) {
    searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            const searchTerm = this.value.trim();
            if (searchTerm) {
                // Có thể thêm logic tìm kiếm ở đây
                console.log('Tìm kiếm:', searchTerm);
                // Ví dụ: window.location.href = '/admin/search?q=' + encodeURIComponent(searchTerm);
            }
        }
    });
}

// --- Xử lý active menu ---
function setActiveMenuItem() {
    const currentPath = window.location.pathname;
    const menuItems = document.querySelectorAll('.menu-item a');

    menuItems.forEach(item => {
        const href = item.getAttribute('href');
        if (href && currentPath === href) {
            item.closest('.menu-item').classList.add('active');
        }
    });
}

// --- Xác nhận đăng xuất ---
const logoutLinks = document.querySelectorAll('a[href*="/logout"]');
logoutLinks.forEach(link => {
    link.addEventListener('click', function (e) {
        if (!confirm('Bạn có chắc chắn muốn đăng xuất?')) {
            e.preventDefault();
        }
    });
});

// --- Khởi tạo khi trang load ---
document.addEventListener('DOMContentLoaded', function () {
    setActiveMenuItem();
});