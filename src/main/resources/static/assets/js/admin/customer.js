// Customer DataTable initialization - Fixed version
$(document).ready(function () {
    // Destroy existing DataTable if it exists
    if ($.fn.DataTable.isDataTable('#customerTable')) {
        $('#customerTable').DataTable().destroy();
    }

    // Initialize DataTable only if table has data
    const tableElement = $('#customerTable');
    const hasEmptyState = tableElement.find('tbody tr td[colspan]').length > 0;

    if (tableElement.length && !hasEmptyState) {
        tableElement.DataTable({
            paging: false,
            searching: false,
            ordering: true,
            info: false,
            destroy: true, // Allow reinitialization
            language: {
                search: "Tìm kiếm:",
                lengthMenu: "Hiển thị _MENU_ dòng",
                info: "Hiển thị _START_ đến _END_ của _TOTAL_ dòng",
                infoEmpty: "Hiển thị 0 đến 0 của 0 dòng",
                infoFiltered: "(lọc từ _MAX_ dòng)",
                emptyTable: "Không có dữ liệu",
                zeroRecords: "Không tìm thấy dữ liệu phù hợp",
                paginate: {
                    first: "Đầu",
                    last: "Cuối",
                    next: "Tiếp",
                    previous: "Trước"
                }
            }
        });
    }

    // Auto-hide alerts
    setTimeout(function () {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => {
                if (alert.parentNode) {
                    alert.parentNode.removeChild(alert);
                }
            }, 500);
        });
    }, 5000);
});