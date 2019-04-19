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
  dx = width / scale / 2;
  dy = height / scale / 2;
  nx=l.normal.x*1;
  ny=l.normal.y*1;
  o=l.offset*1;
  nxdx = nx*dx;
  nydy = ny*dy;

  ps=[];
  if(Math.abs(o-nydy)<=Math.abs(nxdx))
    ps.push({x:(o-nydy)/nx, y:dy});
  if(Math.abs(o+nydy)<=Math.abs(nxdx))
    ps.push({x:(o+nydy)/nx, y:-dy});
  if(Math.abs(o-nxdx)<Math.abs(nydy))
    ps.push({x:dx, y:(o-nxdx)/ny});
  if(Math.abs(o+nxdx)<Math.abs(nydy))
    ps.push({x:-dx, y:(o+nxdx)/ny});
  
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
  dx = width / scale / 2;
  dy = height / scale / 2;
  ex = Number(r.end.x);
  ey = Number(r.end.y);
  dirx = Number(r.direction.x);
  diry = Number(r.direction.y);

  p=null;
  if((+dx-ex)*dirx>0 && Math.abs(ey+(+dx-ex)*diry/dirx)<=dy)
	    p={x:+dx, y:ey+(+dx-ex)*diry/dirx};
  if((-dx-ex)*dirx>0 && Math.abs(ey+(-dx-ex)*diry/dirx)<=dy)
	    p={x:-dx, y:ey+(-dx-ex)*diry/dirx};
  if((+dy-ey)*diry>0 && Math.abs(ex+(+dy-ey)*dirx/diry)<=dx)
	    p={x:ex+(+dy-ey)*dirx/diry, y:+dy};
  if((-dy-ey)*diry>0 && Math.abs(ex+(-dy-ey)*dirx/diry)<=dx)
	    p={x:ex+(-dy-ey)*dirx/diry, y:-dy};

  if(p) {
    return element('line',{
        'x1':scaleX(r.end.x),
        'y1':scaleY(r.end.y),
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
    'x1':scaleX(s.from.x),
    'y1':scaleY(s.from.y),
    'x2':scaleX(s.to.x),
    'y2':scaleY(s.to.y),
    'stroke':color(s),
    'stroke-width':2
  });
};

function circle(c) {
  return element('circle',{
    'cx':scaleX(c.center.x),
    'cy':scaleY(c.center.y),
    'r':c.radius*scale,
    'stroke':color(c),
    'stroke-width':2,
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
