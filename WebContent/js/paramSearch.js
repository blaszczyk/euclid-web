var paramSearchOn=false;
var current;
var initBackup;

function toggleParamSearch() {
  paramSearchOn = !paramSearchOn;
  $('#paramSearch').css('display', paramSearchOn ? 'block' : 'none');
};

function start() {
  current=0;
  initBackup = val('initial');
  updatePSButtons(true);
  tryNext();
};

function skip() {
  halt();
};

function abort() {
  current = numVal('ni');
  halt();
};

function tryNext() {
  val('initial', initBackup + currentPoint());
  $('#start').html(current);
  preview();
  postJob(c => {
    if(c.construction) {
      draw(c.construction);
      updatePSButtons();
    }
    else {
      current++;
      if(current > numVal('ni')) {
        val('initial', initBackup);
        updatePSButtons();
      }
      else {
        tryNext();
      }
    }
  });
};

function currentPoint() {
  i = numVal('imin') + current * (numVal('imax')-numVal('imin')) / numVal('ni');
  i = i.toFixed(9).replace(/\.?0+$/,'');
  x = val('xi').replace(/(?<!\w)i(?!\w)/g, i);
  y = val('yi').replace(/(?<!\w)i(?!\w)/g, i);
  return ' :p('+x+','+y+')';
};

function updatePSButtons(enable) {
  $('#start').html('start');
  $('#start').attr('disabled',!!enable);
  $('#skip').attr('disabled',!enable);
  $('#abort').attr('disabled',!enable);
};

function numVal(id) {
  return 1*val(id);
};
