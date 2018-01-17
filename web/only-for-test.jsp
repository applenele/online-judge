<!DOCTYPE HTML>
<html>
<head>
    <!-- ... -->
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="plugin/datetimepicker/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="stylesheet" href="css/font-awesome.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
    <script src="plugin/datetimepicker/js/moment-with-locales.min.js"></script>
    <script src="plugin/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>



</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6">
            <input type="text" class="form-control datetimepicker-input" id="datetimepicker5" data-toggle="datetimepicker" data-target="#datetimepicker5"/>
        </div>
        <script type="text/javascript">
            $(function () {
                $('#datetimepicker5').datetimepicker({
                    locale: 'zh-cn',
                    format: "YYYY/MM/DD HH:mm"
                });
            });
        </script>
    </div>
</div>
</body>
<html>