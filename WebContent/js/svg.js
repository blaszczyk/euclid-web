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

function draw() {
  svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board(construction[step]);
  svg += '</svg>';
  $('#result').html(svg)
};

function board(b) {
	r='';
	if(b) {
		b.points.forEach( p => r+=point(p));
		b.curves.forEach( c => {
			if(c.type==="line") { r+=line(c); }
			if(c.type==="segment") { r+=segment(c); }
			if(c.type==="circle") { r+=circle(c); }
	    });
	}
	return r;
};

function point(p) {
  return '<circle cx="'+scaleX(p.x)+'" cy="'+scaleY(p.y)+'" r="5" stroke="black" stroke-width="1" fill="'+color(p)+'"/>';
};

function line(l) {
  ps=[];
  
  dx = width / scale / 2;
  dy = height / scale / 2;
  
  nx=l.normal.x*1;
  ny=l.normal.y*1;
  o=l.offset*1;
  
  nxdx = nx*dx;
  nydy = ny*dy;
  
  if(Math.abs(o-nydy)<=Math.abs(nxdx))
    ps.push({x:(o-nydy)/nx, y:dy});
  if(Math.abs(o+nydy)<=Math.abs(nxdx))
    ps.push({x:(o+nydy)/nx, y:-dy});
  if(Math.abs(o-nxdx)<Math.abs(nydy))
    ps.push({x:dx, y:(o-nxdx)/ny});
  if(Math.abs(o+nxdx)<Math.abs(nydy))
    ps.push({x:-dx, y:(o+nxdx)/ny});
  if(ps.length != 2) {
	  console.log('out of bounds: ' + JSON.stringify(l));
	  return;
  }
  p1=ps[0];
  p2=ps[1];
  return '<line x1="'+scaleX(p1.x)+'" y1="'+scaleY(p1.y)+'" x2="'+scaleX(p2.x)+'" y2="'+scaleY(p2.y)+'" stroke="'+color(l)+'" stroke-width="2"/>';
};

function segment(s) {
  return '<line x1="'+scaleX(s.from.x)+'" y1="'+scaleY(s.from.y)+'" x2="'+scaleX(s.to.x)+'" y2="'+scaleY(s.to.y)+'" stroke="'+color(s)+'" stroke-width="2"/>';
};

function circle(c) {
  return '<circle cx="'+scaleX(c.center.x)+'" cy="'+scaleY(c.center.y)+'" r="'+c.radius*scale+'" stroke="'+color(c)+'" stroke-width="2" fill="transparent"/>';
};

function color(e) {
	if(e.role === 'initial') return 'green';
	if(e.role === 'required') return 'red';
	return 'lightgray';
}

function scaleX(x) {
  return x * scale + width / 2;
};

function scaleY(y) {
  return - y * scale + height / 2;
};
