var kpiOn=false;

function toggleKpi() {
  kpiOn = !kpiOn;
  if(kpiOn) {
    showKpi();
  }
  else {
    closeKpi();
  }
};

function showKpi(kpi) {
  if(!kpiOn) {
    return;
  }
  if(kpi) {
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
    showMatrix(kpi, matrixKeys, ++rows);
  }
  
  $('#kpi').css('display','block');
};

function showKeyVals(kpi, keys) {
  trs='';
  keys.forEach(k => {
    tds=wrap('td',k)+wrapf('td',kpi[k]);
    trs+=wrap('tr',tds);
  });
  $('#kpi-key-vals').html(trs);
};

function showMatrix(kpi, keys, rows) {
  trs='';
  ths='';
  keys.forEach(k => {
    ths+=wrap('th',k);
  });
  trs+=wrap('tr',ths);
  for(var i = 0; i < rows; i++) {
    tds='';
    keys.forEach(k => {
      tds+=wrapf('td',kpi[k+'-'+i]);
    });
    trs+=wrap('tr',tds);
  }
  $('#kpi-matrix').html(trs);
};

function wrap(element,content) {
  return '<'+element+'>'+content+'</'+element+'>';
};

function wrapf(element,number) {
  content = number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
  return wrap(element,content);
};

function closeKpi() {
  $('#kpi').css('display','none');
};
