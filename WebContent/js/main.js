var jobId;

function construct() {
  postReq('solve', problem(), jId => {
		updateJobId(jId.jobId);
		setTimeout(pollSolution,1000);
  });
};

function pollSolution() {
  if(jobId) {
    getReq('solve/'+jobId, c => {
      if(c) {
        updateJobId();
        draw(c);
        if(c.length<=1)
          alert('no solution');
        }
      else {
        setTimeout(pollSolution,1000);
      }
    });
  }
};

function halt() {
  deleteReq('solve/'+jobId);
};

function preview() {
  postReq('problem', problem(), draw);
};

function list(callback) {
  getReq('problem', list => {
    o='';
    list.forEach( p => o+='<option value="{}">{}</option>'.replace(/{}/g, p));
    $('#list').html(o);
    if(callback) {
      callback();
    }
  });
};

function load() {
  name = val('list');
  getReq('problem/'+name, p => {
    Object.keys(p).forEach(k => val(k, p[k]));
    val('name', name);
  });
};

function save() {
  name = val('name');
  putReq('problem/'+name, problem(), () => {
    list(() => val('list', name));
    alert(name+' saved successfully!');
  });
};

function del() {
  name = val('list');
  if(confirm('delete ' + name + ' ?')) {
    deleteReq('problem/'+name, list);
  }
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
};

$(() => {
  list();
  $('button').click(e => window[e.srcElement.id]());
});
