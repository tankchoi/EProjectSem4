const toggleButton = document.querySelector('.dropdown-toggle');
const menu = document.querySelector('.dropdown-menu');
const menuToggle = document.querySelector('.menu-toggle');
const navSide = document.querySelector('.nav-side');

// --- Điều khiển Side Navigation ---
menuToggle.addEventListener('click', () => {
    // Kiểm tra giá trị hiện tại của thuộc tính 'left'
    // Hoặc tốt hơn là kiểm tra một lớp CSS để biết trạng thái
    if (navSide.style.left === '-15rem') {
        navSide.style.left = '0'; // Hiển thị menu bên
    } else {
        navSide.style.left = '-15rem'; // Ẩn menu bên
    }

    // *** CÁCH TỐT HƠN: Sử dụng class CSS ***
    // navSide.classList.toggle('is-open');
    // Với CSS:
    // .nav-side { left: -15rem; transition: left 0.3s ease; }
    // .nav-side.is-open { left: 0; }
});

// --- Điều khiển Dropdown Menu ---
toggleButton.addEventListener('click', (e) => {
    e.stopPropagation(); // Ngăn chặn sự kiện click lan truyền ra ngoài document
    // để tránh đóng dropdown ngay lập tức bởi document.addEventListener

    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';

    // *** CÁCH TỐT HƠN: Sử dụng class CSS ***
    // menu.classList.toggle('is-visible');
    // Với CSS:
    // .dropdown-menu { display: none; opacity: 0; transition: opacity 0.3s ease; }
    // .dropdown-menu.is-visible { display: block; opacity: 1; }
    // (Lưu ý: display: none/block sẽ tắt transition, có thể dùng visibility/opacity thay thế)
});

// --- Đóng Dropdown khi click ra ngoài ---
document.addEventListener('click', (e) => {
    // Kiểm tra xem click có nằm ngoài vùng .dropdown hay không
    // e.target.closest('.dropdown') sẽ trả về phần tử .dropdown gần nhất nếu click bên trong
    // hoặc null nếu click bên ngoài
    if (!e.target.closest('.dropdown')) {
        menu.style.display = 'none'; // Đóng dropdown
        // Hoặc: menu.classList.remove('is-visible');
    }
});