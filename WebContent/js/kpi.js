var kpiOn=false;
var showZeroLines=false;
var kpi;

function updateKpi(newKpi) {
  kpi=newKpi;
  if(kpiOn) {
    showKpi();
  }
};

function toggleKpi() {
  kpiOn = !kpiOn;
  if(kpiOn && kpi) {
    showKpi();
  }
  else {
    closeKpi();
  }
};

function toggleZeroLines() {
  showZeroLines = !showZeroLines;
  showKpi();
};

function showKpi() {
  var singleKeys=[];
  var matrixKeys=[];
  var maxrow=-1;

  Object.keys(kpi).forEach(k => {
    if(k.match(/\-\d+$/g)) {
      i = k.lastIndexOf('-');
      key = k.substr(0,i);
      if(matrixKeys.indexOf(key) < 0) {
        matrixKeys.push(key);
      }
      row = k.substr(i+1);
      maxrow = Math.max(maxrow, row);
    }
    else {
      singleKeys.push(k);
    }
  });

  showKeyVals(singleKeys);
  showMatrix(matrixKeys, maxrow);
  $('#kpi').css('display','block');
};

function showKeyVals(keys) {
  var trs='';
  keys.forEach(k => {
    var tds=wrap('td',k)+wrapf('td',kpi[k]);
    trs+=wrap('tr',tds);
  });
  $('#kpi-key-vals').html(trs);
};

function showMatrix(keys, maxrow) {
  var trs='';
  var ths='';
  keys.forEach(k => {
    ths+=wrap('th',k);
  });
  trs+=wrap('tr',ths);
  for(var i = 0; i <= maxrow; i++) {
    var tds='';
    keys.forEach(k => {
      tds+=wrapf('td',kpi[k+'-'+i]);
    });
    if(showZeroLines || tds.match(/[1-9]/g)) {
      trs+=wrap('tr',tds);
    }
  }
  $('#kpi-matrix').html(trs);
};

function wrap(element,content) {
  return '<'+element+'>'+content+'</'+element+'>';
};

function wrapf(element,number) {
  var content = number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
  return wrap(element,content);
};

function closeKpi() {
  $('#kpi').css('display','none');
};
