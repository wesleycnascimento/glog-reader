<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Glog Reader</title>
    <link rel="icon" type="image/ico" href="favicon.ico">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style>
        .centered-container {
            width: 80%;
            max-width: 1200px;
            margin: 20px auto;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .scrollable-tree {
            max-height: 350px;
            overflow-y: auto;
            border: 1px solid #dee2e6;
            padding: 10px;
            border-radius: 6px;
            width: 100%;
        }
        .form-control, textarea {
            width: 100%;
        }
        .btn-inline { display: inline-block; margin-right: 5px; }
        .mt-3 { margin-top: 1rem; }
        .mb-3 { margin-bottom: 1rem; }
    </style>
</head>
<body>
<div class="centered-container">

    <!-- Upload Form -->
    <form id="formCampos" enctype="multipart/form-data" style="width: 100%;">
        <div class="mb-3">
            <label for="input-file-glog" class="form-label">📎 Glog File</label>
            <input type="file" class="form-control" id="input-file-glog" name="glog" accept="application/pdf"/>
        </div>
        <div class="mb-3">
            <label for="input-file-book" class="form-label">📎 Book File</label>
            <input type="file" class="form-control" id="input-file-book" name="book" accept="application/pdf"/>
        </div>
        <div class="mb-3 text-center">
            <button id="sendButton" class="btn btn-primary" type="button">Send</button>
        </div>
        <div class="mb-3">
            <textarea class="form-control" id="textArea" rows="6" readonly></textarea>
        </div>
    </form>

    <!-- Book Structure -->
    <div style="width: 100%;">
        <div class="card">
            <div class="card-header">Book Structure</div>
            <div class="card-body scrollable-tree">
                <ul id="folderTree"></ul>
            </div>
        </div>
    </div>

    <!-- Copy and Clear Buttons -->
    <div class="mt-3 text-center" style="width: 100%;">
        <button class="btn btn-primary btn-inline" onclick="copy();">Copy</button>
        <button class="btn btn-primary btn-inline" onclick="clearAll();">Clear</button>
    </div>

    <!-- Toast -->
    <div class="toast-container position-fixed top-0 end-0 p-1">
        <div id="copyToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <strong class="me-auto">Copy</strong>
                <small>Glog Reader</small>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">Glog result successfully copied!</div>
        </div>
    </div>

</div>

<script>
    function buildTree(folder) {
        if (!folder) return '';
        let html = '<li>' + folder.label;
        if (folder.children && folder.children.length > 0) {
            html += '<ul>';
            folder.children.forEach(child => { html += buildTree(child); });
            html += '</ul>';
        }
        html += '</li>';
        return html;
    }

    function copy() {
        const textArea = document.getElementById("textArea");
        navigator.clipboard.writeText(textArea.value);
        const toastEl = document.getElementById("copyToast");
        new bootstrap.Toast(toastEl).show();
    }

    function clearAll() {
        document.getElementById("formCampos").reset();
        $("#textArea").val("");
        $("#folderTree").html("");
        $("#sendButton").prop("disabled", false);
        $("#input-file-glog").prop("disabled", false);
        $("#input-file-book").prop("disabled", false);
    }

    $("#sendButton").click(function() {
        var glogFile = $("#input-file-glog")[0].files[0];
        var bookFile = $("#input-file-book")[0].files[0];

        // Validation
        if (!glogFile && !bookFile) {
            $("#textArea").val("No file(s) imported!");
            return;
        }
        if (!glogFile) {
            $("#textArea").val("No file(s) imported!");
            return;
        }
        if (!bookFile) {
            $("#textArea").val("No file(s) imported!");
            return;
        }
        if (!glogFile.name.toLowerCase().endsWith(".pdf")) {
            $("#textArea").val("The imported glog file must be a .pdf!");
            return;
        }

        // Proceed with AJAX
        var formData = new FormData();
        formData.append("glog", glogFile);
        formData.append("book", bookFile);

        $.ajax({
            url: "${pageContext.request.contextPath}/sendGlogReader",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json",
            success: function(response) {
                $("#textArea").val(response.message);

                if (response.folder) {
                    let treeHtml = buildTree(response.folder);
                    $("#folderTree").html(treeHtml);
                }

                $("#sendButton").prop("disabled", true);
                $("#input-file-glog").prop("disabled", true);
                $("#input-file-book").prop("disabled", true);
            },
            error: function(xhr) {
                alert("Error processing files: " + xhr.responseText);
            }
        });
    });
</script>
</body>
</html>