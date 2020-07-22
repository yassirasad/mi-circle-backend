
document.getElementById('file').addEventListener('change', function(event) {
  var file = event.target.files[0];
  var fileReader = new FileReader();
  var img = document.getElementById('t-preview');
  var t_div = document.getElementById('t-div');
  if (file.type.match('image')) {
    fileReader.onload = function() {
      img.src = fileReader.result;
    };
    fileReader.readAsDataURL(file);
    t_div.style.display = 'none';
  }
  else if (file.type.match('video')){
    fileReader.onload = function() {
      var blob = new Blob([fileReader.result], {type: file.type});
      var url = URL.createObjectURL(blob);
      var video = document.createElement('video');
      var timeupdate = function() {
        if (snapImage()) {
          video.removeEventListener('timeupdate', timeupdate);
          video.pause();
        }
      };
      video.addEventListener('loadeddata', function() {
		if(this.duration > 7){
			this.currentTime = 7;
		}
        if (snapImage()) {
          video.removeEventListener('timeupdate', timeupdate);
        }
      });
      var snapImage = function() {
        var canvas = document.createElement('canvas');
        canvas.width = (video.videoWidth/video.videoWidth)*200;
        canvas.height = (video.videoHeight/video.videoWidth)*200;
        canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
        var image = canvas.toDataURL();
        var success = image.length > 5000;
        if (success) {
          img.src = image;
          URL.revokeObjectURL(url);
        }
        return success;
      };
      video.addEventListener('timeupdate', timeupdate);
      video.preload = 'metadata';
      video.src = url;
      // Load video in Safari / IE11
      video.muted = true;
      video.playsInline = true;
      video.play();
    };
    fileReader.readAsArrayBuffer(file);
    t_div.style.display = 'block';
  }
  else {
    img.src = '';
    t_div.style.display = 'none';
  }
});