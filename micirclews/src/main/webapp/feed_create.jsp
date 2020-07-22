<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

<!DOCTYPE html>
<html>
<head>
    <title>Create Feed</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js" ></script>
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/js/bootstrap-select.min.js"></script>
    <link rel="stylesheet" href="/css/multiple-select.css">
    <script src="/js/multiple-select.js"></script>
    <script src="/js/select-options.js"></script>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <script src="/js/form_validation.js"></script>
</head>
<body>

<%@include file="header.jsp" %>

<div class='marg'>
    <div class="container">
    <%! BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
    <form class="col-lg-6 col-lg-offset-3 needs-validation" action="<%= blobstoreService.createUploadUrl("/feed/create") %>" method="post" enctype="multipart/form-data" novalidate>

        <h1 align="center">Create Feed</h1>
        <br/>

        <div class="form-row align-items-center">
            <label class="col-auto" for="country">Country</label>
            &nbsp;
            <select class="form-control input col" id="country" name="country" multiple="multiple" searchable required></select>
            <div class="invalid-feedback">Please select the country</div>
        </div>
        <br/>

        <div class="form-row align-items-center">
            <label class="col-auto" for="category">Category</label>
            &nbsp;
            <select class="form-control input col" id="category" name="category" required></select>
            <div class="invalid-feedback">Please select the category</div>
        </div>
        <br/>

        <div class="form-row">
            <label for="title">Title</label>
            <input type="text" class="form-control input" id="title" name="title" maxlength="100" />
            <div class="invalid-feedback">Please provide the title</div>
        </div>
        <br/>

        <div class="form-row">
            <label for="description">Description</label>
            <textarea class="form-control input" id="description" name="description" rows="5" maxlength="999" ></textarea>
            <div class="invalid-feedback">Please provide the description</div>
        </div>
        <br/>

        <div class="form-row align-items-center">
            <label class="col-auto" for="file">Image / Video</label>
            &nbsp;
            <input type="file" class="form-control input col" id="file" name="file" accept="image/*, video/*" />
        </div>
        <br/>

        <div class="form-row align-items-center">
            <label class="col-auto" for="t-preview">Thumbnail preview</label>
            &nbsp;
            <img id="t-preview" name="t-preview">
        </div>
        <br/>

        <div id="t-div">
            <p><em>&nbsp;&nbsp;Attach above preview image or any custom image for video thumbnail</em></p>
            <div class="form-row align-items-center">
                <label class="col-auto" for="t-file">Video thumbnail</label>
                &nbsp;
                <input type="file" class="form-control input col" id="t-file" name="t-file" accept="image/*" />
            </div>
            <br/>
        </div>

        <c:if test="${not empty sessionScope.error}">
            <li style="color:red"><c:out value="${sessionScope.error}"/></li><br/>
            <script>
                document.getElementById('title').value = "${sessionScope.title}";
                document.getElementById('description').value = "${sessionScope.description}";
            </script>
        </c:if>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong><c:out value="${sessionScope.success}"/></strong>
            </div>
        </c:if>

        <div align="center"><input type="submit" class="btn btn-primary" value="CREATE"/></div>
    </form>
    <!-- <div class="col-lg-6">
        sajfhkjsdhfkjsdhfkjhsdjkfh
    </div> -->
</div>
</div>

<%@include file="footer.jsp" %>
<script src="/js/thumbnail.js"></script>

</body>
</html>
