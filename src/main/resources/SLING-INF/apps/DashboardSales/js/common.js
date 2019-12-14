var chartData = {};
// common javascript
var slingip = localStorage.getItem('slingip');
var remoteemail=localStorage.getItem('remoteuser');
$(document).ready(function(){
	//bind date pickers
  //bindDatePicker();
	$.ajax({
	    type: "GET",
	    url: '/portal/servlet/service/Salesconverter.DashboardSales?email='+remoteemail,
	    contentType: "application/json; charset=utf-8",
	    crossDomain: true,
	    dataType: "json",
	    success: function (data, status, jqXHR) {
	    chartData = data;
	    if(chartData){
	      if(chartData.funnelList){
	        bindFunnelFilterOption();
	      }
	      bindCharts();
	    }
	    
	    }, error: function (jqXHR, status) {
	        console.log(jqXHR);
	     //   alert('fail' + status.code);
	    }
	});

  $("#funnel-filter").change(bindCharts);
  $('#upg-rate-funnel-filter').change(bindUpgradeRateChart);
  $('#most-act-chart-filter').change(bindActiveLeadChart);
  $('#expected-chart-filter').change(expectedChartChangeListener)
})


function clicked(){
$(document).ready(function(){
	//bind date pickers
  //bindDatePicker();
var fid="selectexcel";
	if(document.getElementById(fid).value !="")
	{
	var reader = new FileReader();

	var fname = document.getElementById(fid).value;
	 console.log("filename-- " + fname);
	var f = document.getElementById(fid).files;
	reader.readAsDataURL(f[0]);

	reader.onloadend = function() {

	var filedata = reader.result;

	var fd = filedata.substr(0, filedata.indexOf(",") + 1);
	var fdata = filedata.replace(fd, "");
	 console.log("filedata-- " + fdata);
	 
		
		$.ajax({
			type : 'POST',
			url : 'https://'+slingip+':8088/ParseEmailId/ReadExcelServ',
			async : false,
			data : fdata,
			contentType : "application/json",
			success : function(dataa) {
				
				console.log("dataaset = "+dataa);
				chartData = dataa;
			    if(chartData){
			      excelCharts(chartData);
			    }

			}
		}).fail(function(err) {

		console.log("error" + err);
		});

		
	}}else{
		console.log("- else" );
	}
	
	
	
//	$.ajax({
//    type: "POST",
//    url: 'http://development.bizlem.io:8087/dashboard/ExcelData',
//    contentType: "application/json; charset=utf-8",
//    crossDomain: true,
//    
//    dataType: "json",
//    success: function (data, status, jqXHR) {
//    chartData = data;
//    if(chartData){
//      excelCharts(chartData);
//    }
//    
//    }, error: function (jqXHR, status) {
//        console.log(jqXHR);
//      //  alert('fail' + status.code);
//    }
//});

})
}



var excelCharts = function(dataval){

//alert(dataval.outcome);
if(dataval.outcome){
      bindExpectedChart(dataval.outcome);
    }

}


var bindDatePicker = function(){
	$('.datePicker').datepicker();
}

var bindCharts = function(){
  clearCharts();
  var funnelData = getSelectedFunnelData();
  if(funnelData){
    $('#raw-lead').text(funnelData.rawLeads);
    $('#hot-lead').text(funnelData.hotLeads);
    $('#conv-rate').text(funnelData.convRate + "%");
    $('#conv-lead').text(funnelData.convLeads);
    
    if(funnelData.campaignFunnel){
      bindFunnelChart(funnelData.campaignFunnel[0], funnelData.campaignFunneldata);
    }
    
    if(funnelData.funnelLeadCategory){
      bindfunnleCatchart(funnelData.funnelLeadCategory[0]);
    }
    
    if(funnelData.upgradationRate){
      bindUpgradationChart(funnelData.upgradationRate);
    }
    
    if(funnelData.mostActiveLeads){
      bindActiveLeadChart(funnelData.mostActiveLeads[0]);
    }
    if(funnelData.upcomingCampaign){
      bindUpcomingCmpaign(funnelData.upcomingCampaign)
    }
    if(funnelData.outcome){
      bindExpectedChart(funnelData.outcome);
    }
    
    if(funnelData.standout){
      bindStandOutchart(funnelData.standout[0]);
    }
    if(funnelData['active user']){
      bindActiveUserchart(funnelData['active user']);
    }
    
    if(funnelData.dripRate){
      bindConversationRateChart(funnelData.dripRate);
    }
    if(funnelData.missedOppurtinities){
      bindMissedOppurtinitiesChart(funnelData.missedOppurtinities);
    }
    if(funnelData.Geolocation){
      bindGeoLocationchart(funnelData.Geolocation)
    }
    //bindCustAcqCostChart();
  }
}

var clearCharts = function(){
  $('#funnel-chart').html("");
  $("#campaign-funnel-data > tbody").html("");

  $('#funnel-lead-cat').html("");
  $('#funnel-lead-cat-table').html('')

  $('#upg-rate-funnel-filter').html('')
  $('#upg-rate').html('')
  $("#upg-rate-table > tbody").html("");

  $('#most-active-lead').html('');
  $('#most-active-lead-table > tbody').html("");

  $('#upcoming-campaign > tbody').html("");

  $('#expected-chart-filter').html('')
  $('#expected-chart').html('');
  $('#expected-table > tbody').html('');

  $('#stand-out-chart').html('');
  $('#stand-out-table > tbody').html('');

  $('#act-usr-chart').html('')
  $('#act-usr-table > tbody').html('')

  $('#conv-rate-chart').html('');
  $('#conv-rate-table > tbody').html('');

  $('#missed-opp').html('')
  $('#missed-opp-table > tbody').html('');

  $('#cust-acq-cost').html('');
  $('#cust-acq-cost-table').html('');

  $('#geo-location-chart').html('');
  $('#geo-location-table > tbody').html('');
}

var getSelectedFunnelData = function(){
  var selectedFunnel = $("#funnel-filter").val();
  if(selectedFunnel){
    var result = chartData.data.filter(obj => {
      return obj.funnelName === selectedFunnel;
    })
    if(result.length){
      return result[0];
    }
  }
}

var bindFunnelFilterOption = function(){
  $.each(chartData.funnelList, function (i, item) {
    $('#funnel-filter').append($('<option>', { 
        value: item,
        text : item 
    }));
  });
  $("#funnel-filter").val($("#funnel-filter option:first").val());
}

var bindFunnelChart = function(chartData, tableData){
	
  var data = [['Explore',chartData.explore],
  ['Entice',chartData.entice],
  ['Inform',chartData.inform],
  ['Warm',chartData.warm],
  ['Connect ',chartData.connect],];
  
	var options = {
		dynamicArea: true,
		bottomPinch: 1
  };
  
  console.log(data)
	var chart = new D3Funnel(".funnel-chart");
  chart.draw(data, options);
  
  $.each(tableData, function (i, item) {
    $("#campaign-funnel-data tbody").append("<tr><td>" + item.source + "</td><td>" + item.leads + "</td>")
  });
}

var bindfunnleCatchart = function(chartData){
  var data ={
    labels:[],
    series: []
  }

  for (var key in chartData) {
    if (chartData.hasOwnProperty(key)) {
      data.labels.push(key)
      var barData = chartData[key][0]
      var i = 0;
      for (var barDataKey in barData) {
        if (barData.hasOwnProperty(barDataKey)) {
          var obj = data.series[i];
          if(!obj){
            obj = {"name":barDataKey,"data":[barData[barDataKey]]}
            data.series.push(obj)
          }else{
            obj.data.push(barData[barDataKey])
          }
        }
        i++;
      }
    }
  }

  var options = {
    stackBars: true,
    axisY: {
      labelInterpolationFnc: function(value) {
        return value;
    }
  },
  plugins: [
    Chartist.plugins.tooltip(),
  Chartist.plugins.legend()
  ]
  }

  new Chartist.Bar('#funnel-lead-cat', data,options).on('draw', function(data) {
    if(data.type === 'bar') {
      data.element.attr({
        style: 'stroke-width: 30px'
      });
    }
  });

  var html ="<thead><tr><th>Duration</th>"
  for (var key in chartData) {
    if (chartData.hasOwnProperty(key)) {
      var barData = chartData[key][0]
      for (var barDataKey in barData) {
        if (barData.hasOwnProperty(barDataKey)) {
          html += "<th>" + barDataKey + "</th>"
        }
      }
      html += "</tr>"
    }
    break;
  }
  html += "</tr></thead><tbody>"
  for (var key in chartData) {
    if (chartData.hasOwnProperty(key)) {
      html += "<tr><td>" + key + "</td>"
      var barData = chartData[key][0]
      for (var barDataKey in barData) {
        if (barData.hasOwnProperty(barDataKey)) {
          html += "<td>" + barData[barDataKey] + "</td>"
        }
      }
      html += "</tr>"
    }
  }
  html += "</tbody>"
  $("#funnel-lead-cat-table").append(html)

}

var bindUpgradationChart = function(data){
  bindUpgradeRateFilter(data.subFunnel);
  bindUpgradeRateChart();
}

var bindUpgradeRateFilter = function(data){
  $.each(data, function (i, item) {
    $('#upg-rate-funnel-filter').append($('<option>', { 
        value: item,
        text : item 
    }));
  });
  $("#upg-rate-funnel-filter").val($("#upg-rate-funnel-filter option:first").val());
}

var bindUpgradeRateChart = function(){
  $('#upg-rate').html('')
  $("#upg-rate-table > tbody").html("");
  var upgradedata = getSelectedUpgrdRateData();
  if(upgradedata){
    $('#upg-rate').append('<div style="position: absolute; left: ' + ((upgradedata.rate.substring(0,upgradedata.rate.length-1) * 9) / 10) + '%; top: -5px;"> ' +
    '<p style="margin-bottom: 0;font-weight: bold;font-size: 24px;">' + upgradedata.rate + '</p>' +
    '<div style="width: 0;height: 0;border-left: 20px solid transparent;border-right: 20px solid transparent;border-top: 25px solid #607d8b;"></div></div>');
    var chart = new Chartist.Bar('#upg-rate', {
      labels: ['Upgradation Rate'],
      series: [
      [25],
      [25],
      [50]
      ]
    }, {
      stackBars: true,
      seriesBarDistance: 10,
      reverseData: true,
      horizontalBars: true,
      axisY: {
        offset: 30
      }
    });
      
      chart.on('draw', function(event) {
      // If the draw event is for labels on the x-axis
      if(event.type === 'label' && event.axis.units.pos === 'x') {
        // If foreign object is NOT supported, we need to fallback to text-anchor and event.width / 2 offset.
          event.element.attr({
            x: event.x -10,
            'text-anchor': 'start'
          });
      }
    });

    $.each(upgradedata.tableData, function (i, item) {
      $("#upg-rate-table tbody").append("<tr><td>" + item.source + "</td><td>" + item.rate + "</td></tr>")
    });
  }
}

var getSelectedUpgrdRateData = function(){
    var selectedSubFunnel = $("#upg-rate-funnel-filter").val();
    var funneldata = getSelectedFunnelData();
    if(selectedSubFunnel && funneldata){
      
      var result = funneldata.upgradationRate.data.filter(obj => {
        return obj.subFunnel === selectedSubFunnel;
      })
      if(result.length){
        return result[0];
      }
    }
}


var bindActiveLeadChart = function(data){
  var selectedLeadData = getSelectedActiveLeadData();
  //var selectedLeadData = JSON.parse("[{\"lessThen\":2,\"greaterThen\":4,\"lead:100},{\"lessThen\":5,\"greaterThen\":9,\"lead\":130},{\"lessThen\":10,\"greaterThen\":15,\"lead\":185},{\"lessThen\":15,\"greaterThen:\"\",\"lead\":235}]")
  // var chartData = {
  //   labels: ['Piece A', 'Piece B', 'Piece C', 'Piece D', 'Piece E'],
  //     series: [40, 10, 13, 17, 20]
  // }
  var chartData = {
    labels: [],
    series: []
  }
  $.each(selectedLeadData, function (i, item) {
    chartData.labels.push('Between ' + item.lessthan + ',' + item.greaterthan)
    chartData.series.push(item.lead)
  });

	new Chartist.Pie('.most-active-lead', chartData, {
  donut: true,
  donutWidth: 30,
  donutSolid: true,
  startAngle: 270,
  showLabel: false,
  plugins: [
    	Chartist.plugins.tooltip(),
		  Chartist.plugins.legend()
  	 ]
  });
  
  $('#most-active-lead-table > tbody').html("");
  index = 0; 
  while (index < selectedLeadData.length) {
    $('#most-active-lead-table > tbody').append("<tr><td></td><td>" + selectedLeadData[index]['lessthan'] + "</td><td>" + selectedLeadData[index]['greaterthan'] + "</td><td>" + selectedLeadData[index]['lead'] + "</td></tr>")
    index++; 
  }
}

var getSelectedActiveLeadData = function(){
  var selectedLead = $("#most-act-chart-filter").val();
  var funneldata = getSelectedFunnelData();
  if(selectedLead && funneldata){
    for (var key in funneldata.mostActiveLeads) {
      if (funneldata.mostActiveLeads.hasOwnProperty(key)) {
        if(key == selectedLead){
          return funneldata.mostActiveLeads[key]
        }
      }
    }
  }
}

var bindUpcomingCmpaign = function(chartData){  
  index = 0; 
  while (index < chartData.length) { 
    $('#upcoming-campaign > tbody').append("<tr><td>" + chartData[index].date + "</td><td>" + chartData[index].subFunnelName + "</td><td>" + chartData[index].leads + "</td></tr>")
    index++; 
  }
  
}

var bindCustAcqCostChart = function(){
// 	new Chartist.Line('#cust-acq-cost', {
//   labels: ['2012', '2013', '2014', '2015', '2016'],
//   series: [
//     [12, 9, 7, 8, 5],
//     [2, 1, 3.5, 7, 3],
//     [1, 3, 4, 5, 6]
//   ]
// }, {
//   fullWidth: true,
//   chartPadding: {
//     right: 40
//   },
//   plugins: [
//     	Chartist.plugins.tooltip(),
// 		Chartist.plugins.legend({
//             legendNames: ['Facebook', 'Mail', 'Instagram'],
//         })
//   	 ]
// });
}

var bindExpectedChart = function(data){
  bindExpectedChartFilter(data);
  expectedChartChangeListener();
}

var bindExpectedChartFilter = function(data){
  $.each(data, function (i, item) {
    $('#expected-chart-filter').append($('<option>', { 
        value: item.Parameter,
        text : item.Parameter 
    }));
  });
  $("#expected-chart-filter").val($("#expected-chart-filter option:first").val());
}

var getSelectedExpectedData = function(){
  var selectedParameter = $("#expected-chart-filter").val();
  var funneldata = getSelectedFunnelData();
  if(selectedParameter && funneldata){
    
    var result = funneldata.outcome.filter(obj => {
      return obj.Parameter === selectedParameter;
    })
    if(result.length){
      return result[0];
    }
  }
}

var expectedChartChangeListener = function(){
  $('#expected-chart').html('')
  var data = getSelectedExpectedData()
  $('#expected-chart').append('<div style="height: 15px;position: absolute;width: ' + ((data.Actual * 7.75) / 10) + '%;background: #656564;left: 40px;top: 97px;" title="Actual"></div>' +
  '<div style="height: 20px;position: absolute;width: 2%;background: #656564;left: ' + ((data.planned * 9.5) / 10) + '%;top: 94px;"></div>');
	var chart = new Chartist.Bar('.expected-chart', {
  labels: ['Actual VS Targeted'],
  series: [
    [25],
	[25],
	[50]
  ]
}, {
	stackBars: true,
  seriesBarDistance: 10,
  reverseData: true,
  horizontalBars: true,
  axisY: {
    offset: 30
  }
});


chart.on('draw', function(event) {
  // If the draw event is for labels on the x-axis
  if(event.type === 'label' && event.axis.units.pos === 'x') {
    // If foreign object is NOT supported, we need to fallback to text-anchor and event.width / 2 offset.
      event.element.attr({
        x: event.x -10,
        'text-anchor': 'start'
      });
  }
});
$('#expected-table > tbody').html('')
$('#expected-table > tbody').append("<tr><td>" + data.Actual + "</td><td>" + data.planned + "</td></tr>")
}


var bindStandOutchart = function(chartData){
  var data ={
    labels:[],
    series: []
  }

  for (var key in chartData) {
    if (chartData.hasOwnProperty(key)) {
      data.labels.push(key)
      var barData = chartData[key]
      var i = 0;
      for (var barDataKey in barData) {
        if (barData.hasOwnProperty(barDataKey)) {
          var obj = data.series[i];
          if(!obj){
            obj = {"name":barDataKey,"data":[barData[barDataKey]]}
            data.series.push(obj)
          }else{
            obj.data.push(barData[barDataKey])
          }
        }
        i++;
      }
    }
  }
	new Chartist.Bar('.stand-out-chart', data, {
    horizontalBars: true,
      axisY: {
        labelInterpolationFnc: function(value) {
          return value;
      }
 	  },
  	plugins: [
    	Chartist.plugins.tooltip(),
		  Chartist.plugins.legend({
            legendNames: ['Trafic', 'Upgrade', 'Session Time'],
        })
  	 ]
	}).on('draw', function(data) {
  		if(data.type === 'bar') {
    		data.element.attr({
      			style: 'stroke-width: 15px'
    		});
			if(data.type === 'slice') {
     			var $slice = $(data.element._node);
     			$slice.tooltipster({
       				content: $slice.parent().attr('ct:series-name') + ' ' + $slice.attr('ct:value')
    		 	});
  			}
  		}else if(data.type === 'label' && data.axis.units.pos === 'x') {
    		// If foreign object is NOT supported, we need to fallback to text-anchor and event.width / 2 offset.
      		data.element.attr({
        		x: data.x -20,
        		'text-anchor': 'start'
      		});
  		}
  });
  
  var html;
  for (var key in chartData) {
    if (chartData.hasOwnProperty(key)) {
      html += "<tr><td>" + key + "</td>"
      var barData = chartData[key]
      for (var barDataKey in barData) {
        if (barData.hasOwnProperty(barDataKey)) {
          html += "<td>" + barData[barDataKey] + "</td>"
        }
      }
      html += "</tr>"
    }
  }
  $('#stand-out-table tbody').append(html)
}

var bindActiveUserchart = function(data){
  var tableData = data.tableData;
  $('#act-usr-table > tbody').append('<tr><td>Active User</td><td>' + tableData['active user'] + '</td></tr>')
  $('#act-usr-table > tbody').append('<tr><td>Headroom</td><td>' + tableData['headroom'] + '</td></tr>')
  $('#act-usr-table > tbody').append('<tr><td>Unsubscribe</td><td>' + tableData['unsubscribe'] + '</td></tr>')
  $('#act-usr-table > tbody').append('<tr><td>Spam</td><td>' + tableData['spam'] + '</td></tr>')
  $('#act-usr-table > tbody').append('<tr><td>Funnel end</td><td>' + tableData['funnel end'] + '</td></tr>')

  // var dummy_data = [{
  //   "month": "nov-18",
  //   "data": {
  //     "bar1": {
  //       "item1": 300,
  //       "item2": 400,
  //       "item3": 10
  //     },
  //     "bar2": {
  //       "item1": 150,
  //       "item2": 900,
  //       "item3": 300
  //     }
  //   }
  // },{
  //   "month": "dec-18",
  //   "data": {
  //     "bar1": {
  //       "item1": 300,
  //       "item2": 400,
  //       "item3": 10
  //     },
  //     "bar2": {
  //       "item1": 150,
  //       "item2": 900,
  //       "item3": 300
  //     }
  //   }
  // }]

  var dummy_data = [];

  var tempDat = data.chartData[0];
  for(var key in tempDat){
    if (tempDat.hasOwnProperty(key)) {
      var columnData = tempDat[key];
      dummy_data.push({
        "month":key,
        "data":{
          "bar1":{
            "item1":columnData['active user'],
            "item2":columnData['headroom'],
            "item3":0
          },
          "bar2":{
            "item1":columnData['spam'],
            "item2":columnData['funnel end'],
            "item3":columnData['unsubscribe']
          }
        }
      })
    }
  }

  console.log(dummy_data)
    
    
var margin = {top: 20, right: 20, bottom: 20, left: 20},
    chart = d3.select('.act-usr-chart'),
    width = +chart.style('width').replace('px', '') - margin.left - margin.right,
    height = +chart.style('height').replace('px', '') - margin.top - margin.bottom,
    svg = chart
		.append('svg')
		.attr('width', width + margin.left + margin.right)
		.attr('height', height + margin.top + margin.bottom)
		.append('g')
		.attr('transform', 'translate(' + margin.left + ',' + margin.top + ')'),
    data = dummy_data,
    stripedPattern = (function(){
      for(var i = 0, ii = data.length; i<ii; i++){
        //implement checker: check for real month
        if(data[i].month==='february'){
          return {monthName: 'february'};
        }
      }
    })(),

    //d3 scale stuff
    x0 = d3.scale.ordinal().rangeRoundBands([0, width], 0.1),
    x1 = d3.scale.ordinal(),
    y = d3.scale.linear().range([height, 0]),
    yBegin,

    //fetch the column headers
    itemLookup= data[0],
    years = d3.keys(itemLookup.data),
    items = d3.keys(itemLookup.data[years[0]]),
    columnHeaders = [],
    innerColumns = (function(){
      var result = {};
      for(var i = 0, ii = years.length; i<ii; i++){
        var holder = [];
        for(var j = 0, jj = items.length; j<jj; j++){
          columnHeaders.push(items[j]+'-'+years[i]);
          holder.push(items[j]+'-'+years[i]);
          result[years[i]] = holder;
        }
      }
      return result;
    })(),

    //holder for the data obj rebuild
    dataRebuild = [];

data.forEach(function(d, i){
  var tempData = {},
      curYear;
  tempData.monthName = d.month;
  // if(d.month === stripedPattern.monthName){
  //   chart
  //     .select('svg')
  //     .append('defs');
  // }
  for(var key in d.data){
    if(curYear != key){
      curYear = key;
      tempData['totalValue-'+curYear] = 0;
    }
    var holder = d.data[key];
    for(var item in holder){
      tempData[item+'-'+key] = holder[item];
      tempData['totalValue-'+curYear] += parseInt(holder[item]);
    }
  }
  dataRebuild.splice(i, 0, tempData);
});

//refactor needed
dataRebuild.forEach(function(d) {
  var yColumn = new Array();
  d.columnDetails = columnHeaders.map(function(name) {
    for (var ic in innerColumns) {
      if($.inArray(name, innerColumns[ic]) >= 0){
        if (!yColumn[ic]){
          yColumn[ic] = 0;
        }
        yBegin = yColumn[ic];
        yColumn[ic] += +d[name];
        return {
          name: (function(){
            var n = name.indexOf('-');
            return name.substring(0, n != -1 ? n : name.length);
          })(),
          value: +d[name],
          year: ic,
          yBegin: yBegin,
          yEnd: +d[name] + yBegin
        };
      }
    }
  });

  d.total = d3.max(d.columnDetails, function(d) {
    return d.yEnd;
  });

});

//get month names
x0.domain(dataRebuild.map(function(d) { return d.monthName; }));

//get something
x1.domain(d3.keys(innerColumns)).rangeRoundBands([0, x0.rangeBand()],.01,0);

//set y domain, get totals
y.domain([0, d3.max(dataRebuild, function(d) {
  return d.total;
})]);

var stackedBars = svg.selectAll(".stackedBars")
.data(dataRebuild)
.enter().append('g')
.attr({
  //styling
  'class': function(d){ return 'month-col '+d.monthName; },
  'transform': function(d){ return "translate(" + x0(d.monthName) + ",0)"; }
});

var bars = stackedBars.selectAll("rect")
.data(function(d) { return d.columnDetails; });

bars
  .enter().append("rect")
  .attr({
  //coords
  'x': function(d){ return x1(d.year)+1},
  'y': height,

  //style
  'width': x1.rangeBand(),
  'height': 0,
  'fill': function(d,i){return d3.scale.category20c().range()[i+1];},
  'class': 'item',

  //data entry
  'data-name': function(d){ return d.name; },
  'data-value': function(d){ return d.value; },
  'data-year': function(d){ return d.year; }
});

bars
  .transition()
  .duration(1000)
  .attr({
  //coords
  'y': function(d){ return y(d.yEnd)},

  //style
  'height': function(d) { return y(d.yBegin) - y(d.yEnd) - 1; }
});

//legend items
svg.selectAll(".month-col")
  .data(dataRebuild)
  .append('g')
  .attr({
  'class': 'legend-item',

  'text-anchor': 'middle',
  'transform': function(){
    return 'translate('+svg.select('.item').attr('width')+','+parseInt(height+margin.top)+')';
  }
})
  .append('text')
  .text(function(d){ return d.monthName; });

//totals
svg.selectAll(".month-col")
  .data(dataRebuild)
  .append('g')
  .attr({
  'class': 'test'
});

//events
var tooltip = d3.select('body')
.append('div')
.attr('class','stacked-bar-chart-tooltip stacked-bar-chart-tooltip-hidden');

var mouseEventMove = function () {
  var item = d3.select(this);
  return tooltip
    .style({
    'top': (d3.event.pageY) - 120 + 'px',
    'right': (window.innerWidth - d3.event.pageX) - 100 + 'px'
  })
    .classed('stacked-bar-chart-tooltip-hidden', false)
    .html(item.attr('data-value'));
},
    mouseEventOut = function (){
     return tooltip.classed('stacked-bar-chart-tooltip-hidden', true);
    };

svg.selectAll('.item')
  .on('mousemove', mouseEventMove)
  .on('mouseout', mouseEventOut);



}

var bindConversationRateChart = function(data){
	new Chartist.Pie('.conv-rate-chart', {
  series: [20, 20, 20, 20, 20]
}, {
  donut: true,
  donutWidth: 30,
  donutSolid: true,
  startAngle: 270,
  total: 200,
  showLabel: true,
  plugins: [
    ctDonutMarks({
      marks: [data.rate],
      lineAttributes: {
        stroke: 'red',
      }
    })
  ]
});

$('#conv-rate-table > tbody').append("<tr><td>" + data.rawLead + "</td><td>" + data.conv + "</td><td>" + data.convRate + "</td></tr>");
}

var bindMissedOppurtinitiesChart = function(chartData){
		var data = {
		
    labels: [],
    series: [],
	name:[]
  };
	for (var i = 0; i < chartData.length; i++) {
    
    console.log(chartData[i].name);
	console.log(chartData[i].reason);
	console.log(chartData[i].opp);


   data.labels.push(chartData[i].reason);
      data.series.push(chartData[i].opp);
	  data.name.push(chartData[i].name);
}
	


var options = {
  showLabel: false,
    plugins: [
        Chartist.plugins.legend()
    ]
};

new Chartist.Pie('.missed-opp', data, options);

i = 0; 
while (i < data.labels.length) { 
  $('#missed-opp-table > tbody').append("<tr><td>" + data.labels[i] + "</td><td>" + data.series[i] + "</td><td>"+data.name[i]+"</td></tr>")
  i++; 
}
	
}

function ctDonutMarks(options) {
  return function ctDonutMarksPlugin(chart) {
    var defaultOptions = {
      marks: [],
      offset: 10,
      lineAttributes: {
        stroke: 'black',
        'stroke-width': '3px'
      }
    };

    options = Chartist.extend({}, defaultOptions, options);

    if(chart instanceof Chartist.Pie && chart.options.donut) {

      chart.on('created', function(context) {
        if (context.options.donut) {
          var radius = 
            Math.min(context.chartRect.width() / 2, 
                     context.chartRect.height() / 2) - 
              context.options.donutWidth / 2;
    
          var center = {
            x: context.chartRect.x1 + context.chartRect.width() / 2,
            y: context.chartRect.y2 + context.chartRect.height() / 2
          };
          
          var data = Chartist.getDataArray(chart.data);
    
          var totalDataSum = context.options.total || data.reduce(function(total, value) {
            return total + value;
          }, 0);
          
          options.marks.forEach(function(mark) {
            var angle = context.options.startAngle + mark / totalDataSum * 360;
            var position = Chartist.polarToCartesian(center.x, center.y, radius, angle);
            var offset = context.options.donutWidth / 2 + options.offset;
            var p1 = Chartist.polarToCartesian(position.x, position.y, offset, angle);
            var p2 = Chartist.polarToCartesian(position.x - 40, position.y + 30, offset, angle - 180);
            context.svg.append(new Chartist.Svg('line', Chartist.extend({
              x1: p1.x,
              y1: p1.y,
              x2: p2.x,
              y2: p2.y,
              stroke: 'black'
            }, options.lineAttributes)));
          });
        }
      });
    }
  };
}

var bindGeoLocationchart = function(data){
  google.charts.load('current', {
    'packages':['geochart'],
    'mapsApiKey': 'AIzaSyCZ3-tkqSLRq5BKE6jF1JVGVFIwWU0B1so'
  });
  google.charts.setOnLoadCallback(drawRegionsMap);

  function drawRegionsMap() {
    // var chartdata = google.visualization.arrayToDataTable([
    //   ['Country',   'Latitude'],
    //   ['Algeria', 36], 
    //   ['Angola', -8], 
    //   ['Benin', 6], 
    //   ['Botswana', -24],
    //   ['Burkina Faso', 12], 
    //   ['Burundi', -3]
    // ]);

    var tempData = []
    for (var i=0; i < data.length; i++) {
      var row = data[i];
      
      if(i > 0){
        var string = row[0] + ', Hotleads:' + row[1] + ', Converted:' + row[2]
        tempData.push([ row[0], { v: row[2], f: string }])
        $('#geo-location-table tbody').append('<tr><td>' + row[0] + '</td><td>' + row[1] + '</td><td>' + row[2] + '</td></tr>')
      }else{
        tempData.push([ row[0], row[2]])
      }
    }
    console.log(tempData)

    var chartdata = google.visualization.arrayToDataTable(tempData);
    console.log(chartdata)

    var options = {
      region: 'world', // Africa
      colorAxis: {colors: ['#d17905', '#453d3f', '#d70206', '#f05b4f']},
      backgroundColor: '#ffffff',
      datalessRegionColor: '#f4c63d',
      defaultColor: '#d17905',
      allowHtml: true,
      showLegend: true
    };

    var chart = new google.visualization.GeoChart(document.getElementById('geo-location-chart'));
    chart.draw(chartdata, options);
  };

  
  
}
