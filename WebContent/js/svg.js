var width = 800;
var height = 800;
var scale = 100;
var xOff = 0;
var yOff = 0;

var initial;
var required;
var assist;
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

function draw(update) {
  if(update) {
    initial = update.initial;
    required = update.required;
    assist = update.assist;
    construction = update.construction;
    step = construction ? construction.length : 0;
    scale = 100;
    xOff = 0;
    yOff = 0;
  }
  var svg = '<svg width="'+width+'" height="'+height+'">'
  svg += board();
  svg += '</svg>';
  $('#svg').html(svg);
  $('svg').on('wheel', rescale);
};

function rescale(jqe) {
  var e = jqe.originalEvent;
  var dir = e.deltaY;
  if(dir != 0) {
    var factor = dir > 0 ? 1.25 : 0.8;
    var mx = unscaleX(e.offsetX);
    var my = unscaleY(e.offsetY);
    scale *= factor;
    xOff = mx + (xOff - mx) / factor;
    yOff = my + (yOff - my) / factor;
    draw();
    e.preventDefault();
  }
};

function board() {
  var svg='';
  if(!construction) {
    svg+=mapElements(assist,'blue');
    svg+=mapElements(initial,'green');
    svg+=mapElements(required,'red');
  }
  else if(step < 0) {
    svg+=mapElements(initial,'green');
  }
  else if(step < construction.length) {
    for(i=0; i<step; i++) {
      svg+=mapElement(construction[i].curve,'lightgray');
      svg+=mapElements(construction[i].constituents,'lightgray');
    }
    svg+=mapElements(initial,'green');
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

  var p1 = hitAny(px, py, +l.ny, +l.nx);
  var p2 = hitAny(px, py, -l.ny, -l.nx);
  return p1&&p2 ? svgLine(p1.x ,p1.y ,p2.x ,p2.y ,l.color) : '';
};

function ray(r) {
  var ex = scaleX(r.ex);
  var ey = scaleY(r.ey);

  var p = hitAny(ex, ey, +r.dx, -r.dy);
  return p ? svgLine(ex, ey, p.x, p.y, r.color) : '';
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
  return (x-xOff) * scale + width/2;
};

function scaleY(y) {
  return -(y-yOff) * scale + height/2;
};

function unscaleX(x) {
  return (x - width/2) / scale + xOff;
};

function unscaleY(y) {
  return -(y - height/2) / scale + yOff;
};

function hitAny(ex,ey,dx,dy) {
  if(dx<0 && (h = hit(ex,ey,dy/dx,0,height)))
    return {x:0, y:h};
  if(dx>0 && (h = hit(ex,ey,dy/dx,width,height)))
    return {x:width, y:h};
  if(dy<0 && (h = hit(ey,ex,dx/dy,0,width)))
    return {x:h, y:0};
  if(dy>0 && (h = hit(ey,ex,dx/dy,height,width)))
    return {x:h, y:height};
};

function hit(p1,p2,slope,dist,max){
  if(isFinite(slope)) {
    var h = p2+(dist-p1)*slope;
    if(h>=0 && h<=max)
      return h;
  }
};
