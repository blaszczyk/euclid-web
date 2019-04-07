var jobId;

function construct() {
  $.ajax({
	type:'POST',
	url:'rest/solve',
    contentType: 'application/json',
	data: JSON.stringify(problem()),
	success: jId => {
		jobId = jId;
		enableButtons();
		setTimeout(pollSolution,1000);
	},
	error: e => {
	  console.log(e);
	  alert(e.responseText);
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
			construction=c;
			step=c.length-1;
			jobId=null;
			enableButtons();
			draw();
			if(step<=1)
				alert('no solution');
		}
		else if(jobId != null){
			setTimeout(pollSolution,1000);
		}
	},
	error: e => {
	  console.log(e);
	}
  });
};

function halt() {
  if(!jobId) return;
  $.ajax({
	type:'DELETE',
	url:'rest/solve/'+jobId,
	success: () => {
		jobId=null;
		enableButtons()
	},
	error: e => {
	  console.log(e);
	}
  });
};

function preview() {
  $.ajax({
	type:'POST',
	url:'rest/problem',
    Accept : 'application/json',
    contentType: 'application/json',
	data: JSON.stringify(problem()),
	dataType: 'json',
	success: c => {
		construction=c;
		step=1;
		draw();
	},
	error: e => {
	  console.log(e);
	  alert(e.responseText);
	}
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
	},
	error: e => {
	  console.log(e);
	}
  });
};

function load() {
  name = $('#list').attr('value');
  $.ajax({
	type:'GET',
	url:'rest/problem/'+name,
    Accept: 'application/json',
	dataType: 'json',
	success: p => {
		$('#variables').attr('value', p.variables);
		$('#initial').attr('value', p.initial);
		$('#required').attr('value', p.required);
		$('#depth').attr('value', p.depth);
		$('#name').attr('value', name);
	},
	error: e => {
	  console.log(e);
	}
  });
};

function save() {
  name = $('#name').attr('value');
  $.ajax({
	type:'PUT',
	url:'rest/problem/'+name,
    contentType: 'application/json',
	data: JSON.stringify(problem()),
	success: s => {
		list();
		alert(name+' saved successfully!');
	},
	error: e => {
	  console.log(e);
	}
  });
};

function problem() {
	return{
      initial : $('#initial').attr('value'),
      required : $('#required').attr('value'),
      variables : $('#variables').attr('value'),
      depth : $('#depth').attr('value')
    };
};

$('body').delegate('button','click', function() {
})


function enableButtons() {
	function e(id,enable) {
		$('#'+id).prop('disabled',!enable);
	};
	e('preview', !jobId);
	e('construct', !jobId);
	e('halt', jobId);
};

$(list);
