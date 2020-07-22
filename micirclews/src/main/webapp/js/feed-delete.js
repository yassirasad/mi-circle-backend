
function deleteFeed(feed_id) {
	if (confirm("Are you sure to delete this feed?")){
		$.ajax({
			type: 'POST',
			url: 'https://mi-circle.appspot.com/_ah/api/feedApi/v1/deleteFeed',
			data: {
				'deviceSession': '2700c380-af6e-11e8-96f8-529269fb1459',
				'feedId': feed_id
			},
			success: function(result){
				if(result.status)
					alert('Feed deleted successfully');
				else
					alert(result.message);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert('Error in the request');
			}
		})

    }
}