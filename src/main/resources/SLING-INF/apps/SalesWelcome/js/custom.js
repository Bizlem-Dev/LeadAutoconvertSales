$(document).ready(function(){
  $('[data-toggle="tooltip"]').tooltip();
});

$('.document-generation').click(function() {
	$('.standard-templates-section').slideDown();
});

$('.custom-control-input').click(function() {
	$('.fill-the-data-for-document-generation').slideDown();
});

$('.save-btn-document-generation').click(function() {
	$('.mail-btn-section').slideDown();
});

$('.generate-and-mail').click(function() {
	$('.mail-box-text-section').slideDown();
});

$('.copy-plus-btn').click(function() {
	var div = $('.append-text').length;
	if(div < 10){
		var data = $('.copy-input').html();
		$('.paste-input').append(data);
	}else{
		$(this).attr('disabled','disabled');
	}
});

$('.copy-delete-btn').click(function() {
	$('.paste-input').find('.append-text:last-child').remove();
	var div = $('.append-text').length;
	if(div < 10){
		$('.copy-plus-btn').removeAttr('disabled');
	}
});