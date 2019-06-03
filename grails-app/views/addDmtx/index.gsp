<%--
  Created by IntelliJ IDEA.
  User: ttn
  Date: 27/5/19
  Time: 4:45 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Index | PDF-dmtx</title>
</head>

<body>
<div class="row justify-content-center">
    <div class="col-4">
        <g:uploadForm class="upload-form" action="handlePDF">
            <input name="pdfFile" required type="file" class="custom-file-input" accept=".pdf" id="customFile">
            <label id="fileName" class="text-left custom-file-label" for="customFile">Choose PDF</label>
            <g:submitButton name="methodToCall" class="btn btn-lg btn-block btn-success"
                            value="Generate QR"></g:submitButton>
            <g:submitButton name="methodToCall" class="btn btn-lg btn-block btn-primary"
                            value="Read QR"></g:submitButton>
        </g:uploadForm>
        <small class="text-muted">or <a href="uploadImage">upload</a> an image</small>
    </div>
</div>
<script>
    document.querySelector("#customFile").onchange = function () {
        document.querySelector("#fileName").textContent = this.files[0].name;
    }
</script>
</body>
</html>
