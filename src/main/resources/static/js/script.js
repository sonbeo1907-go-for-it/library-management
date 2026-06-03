document.addEventListener('DOMContentLoaded', function() {
    var alerts = document.querySelectorAll('.alert.auto-hide');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            // Bootstrap 5 alert - dùng bootstrap.Alert
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    var deleteLinks = document.querySelectorAll('.confirm-delete');
    deleteLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            if (!confirm('Bạn có chắc muốn xóa? Hành động này không thể hoàn tác.')) {
                e.preventDefault();
            }
        });
    });
});