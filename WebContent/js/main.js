var jobId;

function postJob(onFinished) {
  postReq('solve', problem(), jId => {
    updateJobId(jId.jobId);
    pollJob(onFinished);
  });
};

function pollJob(onFinished) {
  if(jobId) {
    getReq('solve/'+jobId, c => {
      if(c.finished) {
        updateJobId();
        onFinished(c);
      }
      else {
        setTimeout(() => pollJob(onFinished), 1000);
      }
      updateKpi(c.kpi);
    });
  }
};

function construct() {
  postJob(c => {
    if(c.construction) {
      draw(c);
    }
    else {
      alert('no solution');
    }
  });
};

function halt() {
  deleteReq('solve/'+jobId);
};

function preview() {
  postReq('problem', problem(), draw);
};

function list(filter, callback) {
  var search = filter ? '?search='+encodeURIComponent(filter) : '';
  getReq('problem'+search, list => {
    var opts='';
    list.forEach( p => opts+='<option value="{}">{}</option>'.replace(/{}/g, p));
    $('#list').html(opts);
    if(callback) {
      callback();
    }
  });
};

function filter() {
  var search = val('name');
  list(search);
};

function load() {
  var name = val('list');
  getReq('problem/'+name, p => {
    Object.keys(p).forEach(k => val(k, p[k]));
    val('name', name);
  });
};

function save() {
  var name = val('name');
  putReq('problem/'+name, problem(), () => {
    list('',() => val('list', name));
    alert(name+' saved successfully!');
  });
};

function del() {
  var name = val('list');
  if(confirm('delete ' + name + ' ?')) {
    deleteReq('problem/'+name, list);
  }
};

function problem() {
  var p = {};
  $('#problem').children().children()
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
