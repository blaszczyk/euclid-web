$(document).ajaxError( (e,f,g) => {
  updateJobId();
  updatePSButtons();
  console.log(e,f,g);
  if(f.responseText)
    alert(f.responseText);
});

function postReq(path, data, callback) {
  $.ajax({
	type:'POST',
	url:'rest/'+path,
    contentType: 'application/json',
    Accept : 'application/json',
	dataType: 'json',
	data: data,
	success: callback
  });
};

function getReq(path, callback) {
  $.ajax({
	type:'GET',
	url:'rest/'+path,
    Accept : 'application/json',
	dataType: 'json',
	success: callback
  });
};

function deleteReq(path, callback) {
  $.ajax({
	type:'DELETE',
	url:'rest/'+path,
	success: callback
  });
};

function putReq(path, data, callback) {
  $.ajax({
	type:'PUT',
	url:'rest/'+path,
    contentType: 'application/json',
	data: data,
	success: callback
  });
};
