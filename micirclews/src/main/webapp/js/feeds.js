var template = $('#hidden-template').html();
var scroll_bottom_factor = 2;
var last_feed_id = 0;
var timeout;

$.ajax({
    type: 'POST',
    url: 'https://mi-circle.appspot.com/_ah/api/feedApi/v1/getFeeds',
    data: {
        'deviceSession': '2700c380-af6e-11e8-96f8-529269fb1459',
        'feedsLimit': 10,
		'lastFeedId': 0
    },
    success: function(result){
		if(result.status){
			if(result.feeds)
				addfeeds(result);
			else
				$('#error-message').css('display', 'block');
		}
		else
			$('#error-message').html(result.message).css('color', '#C70C37').css('display', 'block');

		$('#loading').hide();
    }
})

$(document).ready(function() {
	// Each time the user scrolls
	$(window).scroll(function() {
		clearTimeout(timeout);
		timeout = setTimeout(function() {
			// End of the document reached?
			//$(document).height() - $(window).height() == $(window).scrollTop()
			if ($(document).height() - $(window).height() - $(window).scrollTop() < scroll_bottom_factor) {
				$('#loading').show();
				console.log(last_feed_id);
				$.ajax({
					type: 'POST',
					url: 'https://mi-circle.appspot.com/_ah/api/feedApi/v1/getFeeds',
					data: {
						'deviceSession': '2700c380-af6e-11e8-96f8-529269fb1459',
						'feedsLimit': 5,
						'lastFeedId': last_feed_id
					},
					success: function(result){
						if(result.status && result.feeds){
							addfeeds(result);
						}
						else
							scroll_bottom_factor = 0;	//Falsfy the End of the document condition.
						$('#loading').hide();
					}
				})
			}
		}, 800);

	});
});

function addfeeds(result){
	console.log(result.feeds);
	$.each(result.feeds, function(i, val){
		last_feed_id = val.feedId;

		var item = $(template).clone();
		var feed_image = $(item).find('#feed-image');
		var feed_video = $(item).find('#feed-video');
		var modal = $(item).find('.w3-modal');
		var modal_image = $(item).find('#modal-image');
		if(val.imageUrl){
			feed_image.attr('src', val.imageUrl);
			feed_image.attr('onclick', "$('#modal"+val.feedId+"').css('display','block')");
			modal.attr('id', 'modal'+val.feedId);
			modal.attr('onclick', "this.style.display='none'");
			modal_image.attr('src', val.imageUrl);
			feed_video.remove();
		}
		else if(val.videoUrl){
			feed_image.remove();
			modal.remove();
			feed_video.attr('src', val.videoUrl);
		}
		else if(val.thumbnailUrl){
			feed_image.attr('src', val.thumbnailUrl);
			modal.remove();
			feed_video.remove();

		}
		else{
			feed_image.remove();
			feed_video.remove();
			modal.remove();
		}
		$(item).find('#feed-title').html(val.title);
		$(item).find('#feed-description').html(val.description);
		$(item).find('#creator-name').html(val.creatorUserName);
		$(item).find('#category-name').html(val.category.toUpperCase());
		$(item).find('#feed-timestamp').html(val.createdOn.slice(0,-2)+" UTC");
		$(item).find('#feed-like').html(val.likeCount);
		$(item).find('#feed-dislike').html(val.dislikeCount);
		$(item).find('#feed-report').html(val.reportCount);
		$(item).find('#edit-button').attr('onclick', 'window.open("/feed/edit/'+ val.feedId +'")');
		$(item).find('#delete-button').attr('onclick', 'deleteFeed('+val.feedId+')');
		$('#feed-container').append(item);
	});
}