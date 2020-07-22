<!DOCTYPE html>
<html>
<head>
    <title>Feeds</title>
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js" ></script>
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/js/bootstrap-select.min.js"></script>
</head>
<body>

<%@include file="header.jsp" %>

<div class='marg'>
	<div class="container" id="feed-container">
		<h3 id="error-message">No Feed to show</h3>
	</div>
	<div id="loading">
		<img src="ajax-loader.gif" alt="Loading…" />
	</div>
</div>

<script id="hidden-template" type="text/x-custom-template">

	<div class="card" id="feed-card">
	  <div class="card-body d-flex justify-content-around">
		<div>
			<img src="" alt="Feed Image" id="feed-image">
		</div>
		<div id="" class="w3-modal">
			<span class="w3-closebtn w3-hover-red w3-container w3-padding-hor-16 w3-display-topright">&times;</span>
			<div class="w3-modal-content w3-animate-zoom">
				<img src="" id="modal-image">
			</div>
		</div>
		<div>
			<video id="feed-video" controls>
				<source src="movie.mp4" type="video/*">
				Your browser does not support the video tag.
			</video>
		</div>
		<div class="flex-grow-1">
			<h4 class="card-title" id="feed-title"></h4>
			<div class="d-flex">
				<h5 class="card-subtitle mb-2 flex-fill" id="creator-name"></h5>
				<h5 class="card-subtitle mb-2 flex-fill-inline badge-pill badge-info" id="category-name">CATEGORY</h5>
			</div>
			<p class="card-text" id="feed-description"></p>
		</div>
	  </div>
	  <div class="card-footer d-flex align-items-center">
		<div class="text-muted flex-fill card-footer-items" id="feed-timestamp"></div>
		<div class="flex-fill card-footer-items">
			<!--
			<span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>&nbsp;
			<span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span>&nbsp;
			<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
			-->
			<img src="ic_thumb_up.png"><span id="feed-like"></span>&nbsp;
			<img src="ic_thumb_down.png"><span id="feed-dislike"></span>&nbsp;
			<img src="ic_report.png"><span id="feed-report"></span>
		</div>
		<div class="flex-fill card-footer-items">
			<button type="button" class="btn btn-outline-primary btn-sm" id="edit-button" aria-label="Edit">
				<span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit
			</button>
			<button type="button" class="btn btn-outline-danger btn-sm" id="delete-button" aria-label="Delete">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Delete
			</button>
		</div>
	  </div>
	</div>

</script>

<script src="js/feeds.js"></script>
<script src="js/feed-delete.js"></script>

<%@include file="footer.jsp" %>

</body>
</html>
