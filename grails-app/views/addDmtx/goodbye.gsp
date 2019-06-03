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
    <title>Goodbye | PDF-dmtx</title>
</head>

<body>
<div class="row justify-content-center">
    <div class="col-4">
        <h1 class="display-1">Thanks!</h1>
        <g:if test="${editedFileName}">
            <h1>PDF with QR code saved as<br/>${editedFileName}</h1>
        </g:if>
        <g:if test="${decodedStrings}">
            <h1>Results from decoding:</h1>
            <g:each in="${decodedStrings}" var="decodedString"><h1>${decodedString}</h1></g:each>
        </g:if>
        <g:if test="${textDecodedFromImage}">
            <h1>Text decoded from image:<br/>${textDecodedFromImage}</h1>
        </g:if>
        <small class="text-muted"><a href="index">Go again</a></small>
    </div>
</div>
</body>
</html>
