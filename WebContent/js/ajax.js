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
	success: s => {
		if(s) {
			solution=s;
			step=s.construction.curves.length;
			if(!step) alert('no solution');
			jobId=null;
			enableButtons();
			draw();
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
	success: s => {
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
	success: s => {
		solution=s;
		draw();
	},
	error: e => {
	  console.log(e);
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

function enableButtons() {
	  $('#preview').attr('enabled', !jobId);
	  $('#construct').attr('enabled', !jobId);
	  $('#halt').attr('enabled', !!jobId);
	  $('#prev').attr('enabled', solution && step > 0);
	  $('#next').attr('enabled', solution && step < solution.construction.curves.length - 1);
};

$(list);
