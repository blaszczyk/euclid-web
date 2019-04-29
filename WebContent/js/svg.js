var initial;
var required;
var construction;
var step=0;

function prev() {
  if(step>=0) {
	  step--;
	  draw();
  }
};

function next() {
  if(step<construction.length) {
	  step++;
	  draw();
  }
};

var width = 800;
var height = 800;
var scale = 100;

function draw(update) {
  if(update) {
    initial = update.initial;
    required = update.required;
    construction = update.construction;
    step = construction ? construction.length : 0;
  }
  svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board();
  svg += '</svg>';
  $('#svg').html(svg)
};

function board() {
  svg='';
  if(!construction) {
    svg+=mapElements(initial,'green');
    svg+=mapElements(required,'red');
  }
  else if(step < 0) {
    svg+=mapElements(initial,'lightgray');
  }
  else if(step < construction.length) {
    svg+=mapElements(initial,'lightgray');
    for(i=0; i<step; i++) {
      svg+=mapElement(construction[i].curve,'lightgray');
      svg+=mapElements(construction[i].constituents,'lightgray');
    }
    svg+=mapElement(construction[step].curve,'black');
    svg+=mapElements(construction[step].constituents,'blue');
  }
  else {
    construction.forEach(c => {
      svg+=mapElement(c.curve,'lightgray');
      svg+=mapElements(c.constituents,'lightgray');
    });
    svg+=mapElements(initial,'green');
    svg+=mapElements(required,'red');
  }
  return svg;
};

function mapElements(elements,color) {
  svg='';
  elements.forEach(e => svg+=mapElement(e,color));
  return svg;
};

function mapElement(element,color) {
  element.color=color;
  return window[element.type](element);
};

function point(p) {
  return element('circle',{
    'cx':scaleX(p.x),
    'cy':scaleY(p.y),
    'r':'4',
    'stroke':'black',
    'stroke-width':1,
    'fill':p.color
  });
};

function line(l) {
  xm = width / scale / 2;
  ym = height / scale / 2;
  nx=l.nx*1;
  ny=l.ny*1;
  o=l.offset*1;
  nxxm = nx*xm;
  nyym = ny*ym;

  ps=[];
  if(Math.abs(o-nyym)<=Math.abs(nxxm))
    ps.push({x:(o-nyym)/nx, y:ym});
  if(Math.abs(o+nyym)<=Math.abs(nxxm))
    ps.push({x:(o+nyym)/nx, y:-ym});
  if(Math.abs(o-nxxm)<Math.abs(nyym))
    ps.push({x:xm, y:(o-nxxm)/ny});
  if(Math.abs(o+nxxm)<Math.abs(nyym))
    ps.push({x:-xm, y:(o+nxxm)/ny});
  
  if(ps.length == 2) {
    return element('line',{
      'x1':scaleX(ps[0].x),
      'y1':scaleY(ps[0].y),
      'x2':scaleX(ps[1].x),
      'y2':scaleY(ps[1].y),
      'stroke':l.color,
      'stroke-width':2
    });
  }
  console.log('out of bounds: ' + JSON.stringify(l));
};

function ray(r) {
  xm = width / scale / 2;
  ym = height / scale / 2;
  ex = r.ex*1;
  ey = r.ey*1;
  dx = r.dx*1;
  dy = r.dy*1;

  p=null;
  if((+xm-ex)*dx>0 && Math.abs(ey+(+xm-ex)*dy/dx)<=ym)
	    p={x:+xm, y:ey+(+xm-ex)*dy/dx};
  if((-xm-ex)*dx>0 && Math.abs(ey+(-xm-ex)*dy/dx)<=ym)
	    p={x:-xm, y:ey+(-xm-ex)*dy/dx};
  if((+ym-ey)*dy>0 && Math.abs(ex+(+ym-ey)*dx/dy)<=xm)
	    p={x:ex+(+ym-ey)*dx/dy, y:+ym};
  if((-ym-ey)*dy>0 && Math.abs(ex+(-ym-ey)*dx/dy)<=xm)
	    p={x:ex+(-ym-ey)*dx/dy, y:-ym};

  if(p) {
    return element('line',{
        'x1':scaleX(r.ex),
        'y1':scaleY(r.ey),
        'x2':scaleX(p.x),
        'y2':scaleY(p.y),
        'stroke':r.color,
        'stroke-width':2
      });
  }
  console.log('out of bounds: ' + JSON.stringify(r));
};

function segment(s) {
  return element('line',{
    'x1':scaleX(s.x1),
    'y1':scaleY(s.y1),
    'x2':scaleX(s.x2),
    'y2':scaleY(s.y2),
    'stroke':s.color,
    'stroke-width':2
  });
};

function circle(c) {
  return element('circle',{
    'cx':scaleX(c.cx),
    'cy':scaleY(c.cy),
    'r':c.radius*scale,
    'stroke':c.color,
    'stroke-width':2,
    'fill':'transparent'
  });
};

function element(name, attrs) {
  res='<'+name;
  Object.keys(attrs).forEach(k => res +=' '+k+'="'+attrs[k]+'"');
  return res+'/>';
};

function scaleX(x) {
  return x * scale + width / 2;
};

function scaleY(y) {
  return - y * scale + height / 2;
};
