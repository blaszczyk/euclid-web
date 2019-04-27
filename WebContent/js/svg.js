var construction;
var step=0;

function prev() {
  if(step>0) {
	  step--;
	  draw();
  }
}

function next() {
  if(step<construction.length-1) {
	  step++;
	  draw();
  }
}

var width = 800;
var height = 800;
var scale = 100;

function draw(newConstruction) {
  if(newConstruction) {
	  construction = newConstruction;
	  step = construction.length-1;
  }
  svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board(construction[step]);
  svg += '</svg>';
  $('#svg').html(svg)
};

function board(b) {
	r='';
	if(b) {
		b.forEach(e => r+=window[e.type](e));
	}
	return r;
};

function point(p) {
  return element('circle',{
    'cx':scaleX(p.x),
    'cy':scaleY(p.y),
    'r':'4',
    'stroke':'black',
    'stroke-width':1,
    'fill':color(p)
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
      'stroke':color(l),
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
        'stroke':color(r),
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
    'stroke':color(s),
    'stroke-width':2
  });
};

function circle(c) {
  return element('circle',{
    'cx':scaleX(c.cx),
    'cy':scaleY(c.cy),
    'r':c.radius*scale,
    'stroke':color(c),
    'stroke-width':2,
    'fill':'transparent'
  }) + element('circle',{
	    'cx':scaleX(c.cx),
	    'cy':scaleY(c.cy),
	    'r':'5',
	    'stroke':'blue',
	    'stroke-width':1,
	    'fill':'transparent'
  });
};

function element(name, attrs) {
	res='<'+name;
	Object.keys(attrs).forEach(k => res +=' '+k+'="'+attrs[k]+'"');
	return res+'/>';
};

function color(e) {
	if(e.role === 'initial') return 'green';
	if(e.role === 'required') return 'red';
	return 'lightgray';
};

function scaleX(x) {
  return x * scale + width / 2;
};

function scaleY(y) {
  return - y * scale + height / 2;
};
