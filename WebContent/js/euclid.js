
function construct() {
  $.ajax({
	type:'POST',
	url:'rest/solve',
    Accept : 'application/json',
    contentType: 'application/json',
	data: JSON.stringify(problem()),
	dataType: 'json',
	success: solution => {
		draw(solution);
	},
	error: e => {
	  console.log(e);
	}
  });
};

function visualize() {
  $.ajax({
	type:'POST',
	url:'rest/problem',
    Accept : 'application/json',
    contentType: 'application/json',
	data: JSON.stringify(problem()),
	dataType: 'json',
	success: solution => {
		draw(solution);
	},
	error: e => {
	  console.log(e);
	}
  });
};

function problem() {
	return{
      initial : $('#initial')[0].value,
      required : $('#required')[0].value,
      variables : $('#variables')[0].value,
      depth : $('#depth')[0].value
    };
};

var width = 800;
var height = 800;
var scale = 100;

function draw(solution) {
  svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board(solution.solution,'lightgray');
  svg += board(solution.required,'red');
  svg += board(solution.initial,'green');
  svg += '</svg>';
  $('#result').html(svg)
};

function board(b, color) {
	r='';
	b.points.forEach( p => r+=point(p, color));
    b.lines.forEach( l => r+=line(l, color));
	b.circles.forEach( c => r+=circle(c, color));
	return r;
}

function point(p, color) {
  return '<circle cx="'+scaleX(p.x)+'" cy="'+scaleY(p.y)+'" r="5" stroke="black" stroke-width="1" fill="'+color+'"/>';
};

function line(l,color) {
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
  if(Math.abs(o-nxdx)<=Math.abs(nydy))
    ps.push({x:dx, y:(o-nxdx)/ny});
  if(Math.abs(o+nxdx)<=Math.abs(nydy))
    ps.push({x:-dx, y:(o+nxdx)/ny});
  
  p1=ps[0];
  p2=ps[1];
  return '<line x1="'+scaleX(p1.x)+'" y1="'+scaleY(p1.y)+'" x2="'+scaleX(p2.x)+'" y2="'+scaleY(p2.y)+'" stroke="'+color+'" stroke-width="2"/>'
};

function circle(c,color) {
  return '<circle cx="'+scaleX(c.center.x)+'" cy="'+scaleY(c.center.y)+'" r="'+c.radius*scale+'" stroke="'+color+'" stroke-width="2" fill="transparent"/>';
};

function scaleX(x) {
  return x * scale + width / 2;
};

function scaleY(y) {
  return - y * scale + height / 2;
};
