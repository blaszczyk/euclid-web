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
  var svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board();
  svg += '</svg>';
  $('#svg').html(svg)
};

function board() {
  var svg='';
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
  var svg='';
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
    'stroke-width':0,
    'fill':p.color
  });
};

function line(l) {
  var px = scaleX(l.nx * l.offset);
  var py = scaleY(l.ny * l.offset);
  var dx = l.ny * 1;
  var dy = l.nx * 1;

  var ps=[];
  if(h = hit(px,py,dy/dx,0,height))
    ps.push({x:0, y:h});
  if(h = hit(px,py,dy/dx,width,height))
    ps.push({x:width, y:h});
  if(h = hit(py,px,dx/dy,0,width,true))
    ps.push({x:h, y:0});
  if(h = hit(py,px,dx/dy,height,width,true))
    ps.push({x:h, y:height});

  if(ps.length == 2) {
    return svgLine(ps[0].x ,ps[0].y ,ps[1].x ,ps[1].y ,l.color);
  }
  console.log('out of bounds: ' + JSON.stringify(l));
};

function ray(r) {
  var ex = scaleX(r.ex);
  var ey = scaleY(r.ey);
  var dx = + r.dx;
  var dy = - r.dy;

  var p=null;
  if(dx < 0 && (h = hit(ex,ey,dy/dx,0,height)))
    p={x:0, y:h};
  if(dx > 0 && (h = hit(ex,ey,dy/dx,width,height)))
    p={x:width, y:h};
  if(dy < 0 && (h = hit(ey,ex,dx/dy,0,width)))
    p={x:h, y:0};
  if(dy > 0 && (h = hit(ey,ex,dx/dy,height,width)))
    p={x:h, y:height};
  if(p) {
    return svgLine(ex, ey, p.x, p.y, r.color);
  }
  console.log('out of bounds: ' + JSON.stringify(r));
};

function segment(s) {
  return svgLine(scaleX(s.x1), scaleY(s.y1), scaleX(s.x2), scaleY(s.y2), s.color);
};

function circle(c) {
  return element('circle',{
    'cx':scaleX(c.cx),
    'cy':scaleY(c.cy),
    'r':c.radius*scale,
    'stroke':c.color,
    'stroke-width':1,
    'fill':'transparent'
  });
};

function svgLine(x1,y1,x2,y2,color) {
  return element('line',{
    'x1':x1,
    'y1':y1,
    'x2':x2,
    'y2':y2,
    'stroke':color,
    'stroke-width':1
  });
};

function element(name, attrs) {
  var res='<'+name;
  Object.keys(attrs).forEach(k => res +=' '+k+'="'+attrs[k]+'"');
  return res+'/>';
};

function scaleX(x) {
  return x * scale + width / 2;
};

function scaleY(y) {
  return - y * scale + height / 2;
};

function hit(p1,p2,slope,dist,max,noEdges){
  if(isFinite(slope)) {
    var h = p2+(dist-p1)*slope;
    if(h>=0 && h<=max && !(noEdges && (h==0||h==max)))
      return h;
  }
};
