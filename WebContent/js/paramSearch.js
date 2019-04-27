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
  updatePSButtons(true);
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

function updatePSButtons(running) {
  $('#start').html(running ? current : 'start');
  $('#start').attr('disabled',!!running);
  $('#skip').attr('disabled',!running);
  $('#abort').attr('disabled',!running);
};

function numVal(id) {
  return 1*val(id);
};
