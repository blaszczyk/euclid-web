$(document).ajaxError( (e,f,g) => {
  updateJobId();
  console.log(e,f,g);
  if(f.responseText)
    alert(f.responseText);
});

var jobId;

function construct() {
  $.ajax({
	type:'POST',
	url:'rest/solve',
    contentType: 'application/json',
	data: problem(),
	success: jId => {
		updateJobId(jId);
		setTimeout(pollSolution,1000);
	}
  });
};

function pollSolution() {
  if(!jobId) return;
  $.ajax({
	type:'GET',
	url:'rest/solve/'+jobId,
    Accept : 'application/json',
	dataType: 'json',
	success: c => {
		if(c) {
			updateJobId();
			draw(c);
			if(c.length<=1)
				alert('no solution');
		}
		else {
			setTimeout(pollSolution,1000);
		}
	}
  });
};

function halt() {
  $.ajax({
	type:'DELETE',
	url:'rest/solve/'+jobId
  });
};

function preview() {
  $.ajax({
	type:'POST',
	url:'rest/problem',
    Accept : 'application/json',
    contentType: 'application/json',
	data: problem(),
	dataType: 'json',
	success: draw
  });
};

function list() {
  $.ajax({
	type:'GET',
	url:'rest/problem',
    contentType: 'application/json',
	dataType: 'json',
	success: list => {
		o='';
		list.forEach( p => o+='<option value="{}">{}</option>'.replace(/{}/g, p));
		$('#list').html(o);
	}
  });
};

function load() {
  name = val('list');
  $.ajax({
	type:'GET',
	url:'rest/problem/'+name,
    Accept: 'application/json',
	dataType: 'json',
	success: p => {
		Object.keys(p).forEach(k => val(k, p[k]));
		val('name', name);
	}
  });
};

function save() {
  name = val('name');
  $.ajax({
	type:'PUT',
	url:'rest/problem/'+name,
    contentType: 'application/json',
	data: problem(),
	success: s => {
		list();
		alert(name+' saved successfully!');
	}
  });
};

function del() {
    name = val('list');
    if(!confirm('delete ' + name + '?'))
    	return;
    $.ajax({
	  	type:'DELETE',
	  	url:'rest/problem/'+name,
	  	success: s => {
	  		list();
	  	}
    });
};

function problem() {
	p = {};
	$('#problem').children()
		.filter((i,c) => c.id)
		.map((i,c) => c.id)
		.each((i,id) => p[id]=val(id));
	return JSON.stringify(p);
};

function updateJobId(jId) {
	jobId=jId;
	$('#controls').children()
	  .filter((i,c)=>c.id)
	  .each((i,c) => {
	    c.disabled = ((c.id==='halt') == !jobId);
	  });
};

function val(id, value) {
	if(value === undefined) {
		return $('#'+id).val();
	}
	$('#'+id).val(value);
}

$(() => {
	list();
	$('button').click(e => window[e.srcElement.id]());
});
