var kpiOn=false;

function toggleKpi() {
  kpiOn = !kpiOn;
  if(kpiOn) {
    pollKpi();
  }
  else {
    closeKpi();
  }
};

function pollKpi() {
  if(jobId && kpiOn) {
    getReq('solve/'+jobId+'/kpi', kpi => {
      if(kpi) {
        showKpi(kpi);
        setTimeout(pollKpi, 1000);
      }
    });
  }
};

function showKpi(kpi) {
  singleKeys=[];
  matrixKeys=[];
  rows=0;
  
  Object.keys(kpi).forEach(k => {
    if(k.match(/\-\d+$/g)) {
      i = k.lastIndexOf('-');
      key = k.substr(0,i);
      num = k.substr(i+1);
      if(matrixKeys.indexOf(key) < 0) {
        matrixKeys.push(key);
      }
      rows = Math.max(rows, num);
    }
    else {
      singleKeys.push(k);
    }
  });
  
  showKeyVals(kpi, singleKeys);
  showMatrix(kpi, matrixKeys, rows++);
  
  $('#kpi').css('display','block');
};

function showKeyVals(kpi, keys) {
  kvs='';
  keys.forEach(k => {
    kvs+='<tr><td>'+k+'</td><td>'+format(kpi[k])+'</td></tr>';
  });
  $('#kpi-key-vals').html(kvs);
};

function showMatrix(kpi, keys, rows) {
  matrix='<tr>';
  keys.forEach(k => {
    matrix+='<th>'+k+'</th>';
  });
  matrix+='</tr>';
  for(var i = 0; i < rows; i++) {
    matrix+='<tr>';
    keys.forEach(k => {
      matrix+='<td>'+format(kpi[k+'-'+i])+'</td>';
    });
    matrix+='</tr>';
  }
  $('#kpi-matrix').html(matrix);
};

function format(num) {
  return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')
};

function closeKpi() {
  $('#kpi').css('display','none');
};
