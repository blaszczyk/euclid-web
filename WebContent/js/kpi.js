var showKpi=false;

function kpi() {
	showKpi = !showKpi;
	if(showKpi) {
		pollKpi();
	}
	else {
		closeKpi();
	}
};

function pollKpi() {
  if(jobId && showKpi) {
    $.ajax({
      type:'GET',
      url:'rest/solve/'+jobId+'/kpi',
      Accept : 'application/json',
      dataType: 'json',
      success: kpi => {
        if(kpi) {
          drawKpi(kpi);
          setTimeout(pollKpi, 1000);
        }
      }
    });
  }
};

function drawKpi(kpi) {
  singleKeys=[];
  matrixKeys=[];
  rows=0;
  Object.keys(kpi).forEach(k => {
	  if(k.match(/\-\d+$/g)) {
		  i = k.lastIndexOf('-');
		  key = k.substr(0,i);
		  num = k.substr(i+1);
		  if(matrixKeys.indexOf(key) < 0) {
			  matrixKeys.push(key);
		  }
		  rows = Math.max(rows,num);
	  }
	  else {
		  singleKeys.push(k);
	  }
  })
  rows++;
  kvs='';
  singleKeys.forEach(k => {
    kvs+='<tr><td>'+k+'</td><td>'+format(kpi[k])+'</td></tr>';
  });
  $('#kpi-key-vals').html(kvs);
  
  matrix='<tr>';
  matrixKeys.forEach(k => {
	  matrix+='<th>'+k+'</th>';
  })
  matrix+='</tr>';
  $('#kpi-matrix').html(matrix);
  for(var i = 0; i < rows; i++) {
      matrix='<tr>';
      matrixKeys.forEach(k => {
        matrix+='<td>'+format(kpi[k+'-'+i])+'</td>';
    });
    matrix+='</tr>';
    $('#kpi-matrix').append(matrix);
  }
  
  $('#kpi-board').css('display','block');
};

function format(num) {
  return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')
};

function closeKpi() {
  $('#kpi-board').css('display','none');
};
