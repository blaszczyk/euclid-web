var paramSearchOn=false;
var current;
var varsBackup;

var imin;
var imax;
var ni;

function toggleParamSearch() {
  paramSearchOn = !paramSearchOn;
  $('#paramSearch').css('display', paramSearchOn ? 'block' : 'none');
};

function start() {
  imin = numVal('imin');
  imax = numVal('imax');
  ni = numVal('ni');
  current = 0;
  varsBackup = val('variables').replace(/^i\=[\d\.]+\s+/,'');
  tryNext();
};

function skip() {
  halt();
};

function abort() {
  current = ni;
  halt();
};

function tryNext() {
  val('variables', currentI() + varsBackup);
  updatePSButtons(true);
  preview();
  postJob(c => {
    if(c.construction) {
      draw(c);
      updatePSButtons();
    }
    else {
      current++;
      if(current > ni) {
        updatePSButtons();
      }
      else {
        tryNext();
      }
    }
  });
};

function currentI() {
  var i = imin + (imax - imin) * current / ni;
  i = i.toFixed(9).replace(/\.?0+$/,'');
  return 'i='+i+'\r\n';
};

function updatePSButtons(running) {
  $('#start').html(running ? current : 'start');
  $('#start').attr('disabled',!!running);
  $('#skip').attr('disabled',!running);
  $('#abort').attr('disabled',!running);
};

function numVal(id) {
  return 1*val(id);
};
