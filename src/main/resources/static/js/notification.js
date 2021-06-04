$(document).ready(function () {

    function updateNotification() {
        $.ajax({
            url: "/notification/count",
            dataType: "json",
            async: true,
            type: "GET",
            success: function (req) {
                $('.notification-count').attr('hidden', req === 0).text(req);
            }
        });

        setTimeout(updateNotification, 10000);
    }

    updateNotification();
});
