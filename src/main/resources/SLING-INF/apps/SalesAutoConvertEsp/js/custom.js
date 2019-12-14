var remoteuser;
var uploadedSubscriberCount = 0;
var slingip = localStorage.getItem('slingip');
var update=false;
var datatype;
var getsubserviceType;
let json_object=[] ;
let editcontactsobj=[] ;

var funnelName1;
var spfor=1;
var spforent=1;
var spforinf=1;
var spforwarm=1;
var spforconnect=1;
//var mailtemp = document.getElementById("mailtemp").value;


var entire_list_tbody = "";
var part_of_list_tbody = "";
/*document.addEventListener("DOMContentLoaded", function() {
	remoteuser = document.getElementById("remoteemail").value;// 'viki@gmail.com';
	});
*/
//var xlslink;
$(document)
		.ready(
				function() {
					// var remoteuser ='<%=request.getRemoteUser()%>';
					// alert(document.getElementById("schtime").innerHTML);
					var currentTime = new Date();

					var currentOffset = currentTime.getTimezoneOffset();

					var ISTOffset = 330; // IST offset UTC +5:30

					var ISTTime = new Date(currentTime.getTime()
							+ (ISTOffset + currentOffset) * 60000);

					// ISTTime now represents the time in IST coordinates

					var hoursIST = ISTTime.getHours();
					var minutesIST = ISTTime.getMinutes();

					//calStorage.gtItem('remoteuser', remoteuser);
					remoteuser=localStorage.getItem('remoteuser');
					console.log("remoteuser : " + remoteuser);
				
					
					
					mailtemplatedropbox();
					getFunnelList();
					
					// init();
					
					//on group change
				/*	$("#grlist").change(function() {
						
						
					}); */
					
				
				});

/*
*/

//display table

//read uploaded xls file
$("#fileUploaderjson").change(function(evt){
	  console.log("in upload leads");
	    var count= 0;
      var selectedFile = evt.target.files[0];
      var reader = new FileReader();
      reader.onload = function(event) {
        var data = event.target.result;
        var workbook = XLSX.read(data, {
            type: 'binary'
        });
        workbook.SheetNames.forEach(function(sheetName) {
          //groupname
          if (count == 0) {
            var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
            
       
  	 
            json_object = JSON.stringify(XL_row_object);
            console.log("json_object= "+json_object);
         //	console.log("in upload"+mainjs);
            //call a servlet passing json object with mail template -> SubFunnelName,funnelName,email,remoteuser,json_object update mongoApi which will call pallavi mail api.
         //  document.getElementById("jsonObject").innerHTML = json_object;
          }
          count++;
          })
      };
    
      reader.onerror = function(event) {
        console.error("File could not be read! Code " + event.target.error.code);
      };

      reader.readAsBinaryString(selectedFile);
      
      //display table
  	document.getElementById("showlistbut").innerHTML='<button type="button" onclick="displaycontacts()">Show List</button>';
  	
  
      
});

function displaycontacts() {
	
	
document.getElementById("diventirelist").innerHTML='<label class="containers">Entire List<input type="radio" name="radio-list" id="list1" value="list1" checked="checked"><span class="checkmark"></span></label>';
document.getElementById("divpartoflist").innerHTML='<label class="containers">Part Of List <input type="radio" name="radio-list" id="list2" value="list2"><span class="checkmark"></span></label>';
    

document.getElementById("divforlisttable").innerHTML='<div class="to-from-list-part-section"><div class="row"><div class="col-md-12 entire-list-section"><table class="table table-bordered" id="list-table" style="height: 170px; overflow-y: scroll; border:0px; display: block;"><tbody id="csv_leads_list" style="display: table; width: 100%;"></tbody></table></div></div></div>';


var list_type = $("input[name='radio-list']:checked").val();

	 if(json_object.length>0){
		 console.log("json_object new = "+json_object+" :: json_object.length = "+json_object.length);
		 var obj = JSON.parse(json_object);
	 }
//if(editcontactsobj.length>0){
//	 var obj = JSON.parse(editcontactsobj);
//	 editcontactsobj=null;
//}
 	
	  for(var i=0;i<obj.length;i++){ 
		  var Email="";
		     var First_Name="";
		     var Last_Name="";
		     var Phone_Number="";
		     var Address="";
		     var Company_Name="";
		     var Company_HeadCount="";
		     var Industry="";
		     var Institute="";
		     var Source="";
	      var lead_info=obj[i];
	      console.log("lead_info= "+lead_info);
	    
 if (lead_info.Email){
	 Email=  lead_info.Email;  	  
	      } 
 if (lead_info.FirstName){
	 First_Name  =lead_info.FirstName ;
 } 
 if (lead_info.LastName){
	 Last_Name  =lead_info.LastName ;
 } 
 if (lead_info.PhoneNumber){
	 Phone_Number  =lead_info.PhoneNumber ;
	  
 } 
 if (lead_info.Country){
	 Address=  lead_info.Country; 
 } 
 if (lead_info.CompanyName){
	 Company_Name=  lead_info.CompanyName;  
 } 
 if (lead_info.CompanyHeadCount){
	 Company_HeadCount=  lead_info.CompanyHeadCount;
 } 
 if (lead_info.Industry){
	 Industry=  lead_info.Industry;
 } 
 if (lead_info.Institute){
	 Institute=  lead_info.Institute;
 } 
 if (lead_info.Source){
	 Source=  lead_info.Source;
 } 

	entire_list_tbody = entire_list_tbody
			+ "<tr  id='csv_entire_list_leadtr"
			+ (i + 1)
			+ "' name='csv_entire_list_leadtr'><td>"
			+ (i + 1) + "</td><td>"
			+ Email + "</td><td>"
			+ First_Name + "</td><td>"
			+ Last_Name + "</td><td>"
			+ Phone_Number + "</td><td>"
			+ Address + "</td><td>"
			+ Company_Name + "</td><td>"
			+ Company_HeadCount
			+ "</td><td>" + Industry
			+ "</td><td>" + Institute
			+ "</td><td>" + Source
			+ "</td></tr>";
		  } 
	 
		  console.log("list_type = "+list_type);
		  
		  if (list_type == 'list1') {
			  console.log("list_type = "+list_type);
				document.getElementById("csv_leads_list").innerHTML = entire_list_tbody;
				// getSubscribersList();
			} 
	
	     
	    	
}



$(document).on("change","input[type=radio]",function(){
	
	  var list_type = $("input[name='radio-list']:checked").val();
	  console.log("change list_type = "+list_type);
	  if (list_type === 'list2') {
			document.getElementById("csv_leads_list").innerHTML = "";
		  console.log("Enter list_type = "+list_type);
		 if(json_object.length>0){
		  var obj = JSON.parse(json_object);
		  for(var i=0;i<obj.length;i++){ 
			  var Email="";
			     var First_Name="";
			     var Last_Name="";
			     var Phone_Number="";
			     var Address="";
			     var Company_Name="";
			     var Company_HeadCount="";
			     var Industry="";
			     var Institute="";
			     var Source="";
		      var lead_info=obj[i];
		  //    console.log("lead_info= "+lead_info);
		    
	 if (lead_info.Email){
		 Email=  lead_info.Email;  	  
		      } 
	 if (lead_info.FirstName){
		 First_Name  =lead_info.FirstName ;
	 } 
	 if (lead_info.LastName){
		 Last_Name  =lead_info.LastName ;
	 } 
	 if (lead_info.PhoneNumber){
		 Phone_Number  =lead_info.PhoneNumber ;
		  
	 } 
	 if (lead_info.Country){
		 Address=  lead_info.Country; 
	 } 
	 if (lead_info.CompanyName){
		 Company_Name=  lead_info.CompanyName;  
	 } 
	 if (lead_info.CompanyHeadCount){
		 Company_HeadCount=  lead_info.CompanyHeadCount;
	 } 
	 if (lead_info.Industry){
		 Industry=  lead_info.Industry;
	 } 
	 if (lead_info.Institute){
		 Institute=  lead_info.Institute;
	 } 
	 if (lead_info.Source){
		 Source=  lead_info.Source;
	 } 
		      
		  
		      
		      part_of_list_tbody = part_of_list_tbody
				+ "<tr  id='csv_part_of_list_leadtr"
				+ (i + 1)
				+ "' name='csv_part_of_list_leadtr'><td>"
				+ (i + 1)
				+ "</td><td><input id='csv_part_of_list_leadchk"
				+ (i + 1)
				+ "' name='csv_part_of_list_sub_chk_boxes' type='checkbox' class='ads_Checkbox'   value='checkemail'/></td><td>"
				+ Email + "</td><td>"
				+ First_Name + "</td><td>"
				+ Last_Name + "</td><td>"
				+ Phone_Number + "</td><td>"
				+ Address + "</td><td>"
				+ Company_Name + "</td><td>"
				+ Company_HeadCount
				+ "</td><td>" +Industry
				+ "</td><td>" + Institute
				+ "</td><td>" + Source
				+ "</td></tr>";

		   //   console.log("part_of_list_tbody = "+part_of_list_tbody);
			  } 
	  }
		  
		  if(editcontactsobj.length>0){
			  
			  
			  for(var i=0;i<editcontactsobj.length;i++){ 
				  var Email="";
				     var First_Name="";
				     var Last_Name="";
				     var Phone_Number="";
				     var Address="";
				     var Company_Name="";
				     var Company_HeadCount="";
				     var Industry="";
				     var Institute="";
				     var Source="";
			      var lead_info=editcontactsobj[i];
			  //    console.log("lead_info= "+lead_info);
			    
		 if (lead_info.Email){
			 Email=  lead_info.Email;  	  
			      } 
		 if (lead_info.FirstName){
			 First_Name  =lead_info.FirstName ;
		 } 
		 if (lead_info.LastName){
			 Last_Name  =lead_info.LastName ;
		 } 
		 if (lead_info.PhoneNumber){
			 Phone_Number  =lead_info.PhoneNumber ;
			  
		 } 
		 if (lead_info.Country){
			 Address=  lead_info.Country; 
		 } 
		 if (lead_info.CompanyName){
			 Company_Name=  lead_info.CompanyName;  
		 } 
		 if (lead_info.CompanyHeadCount){
			 Company_HeadCount=  lead_info.CompanyHeadCount;
		 } 
		 if (lead_info.Industry){
			 Industry=  lead_info.Industry;
		 } 
		 if (lead_info.Institute){
			 Institute=  lead_info.Institute;
		 } 
		 if (lead_info.Source){
			 Source=  lead_info.Source;
		 } 
			      
			  
			      
			      part_of_list_tbody = part_of_list_tbody
					+ "<tr  id='csv_part_of_list_leadtr"
					+ (i + 1)
					+ "' name='csv_part_of_list_leadtr'><td>"
					+ (i + 1)
					+ "</td><td><input id='csv_part_of_list_leadchk"
					+ (i + 1)
					+ "' name='csv_part_of_list_sub_chk_boxes' type='checkbox' class='ads_Checkbox'   value='checkemail'/></td><td>"
					+ Email + "</td><td>"
					+ First_Name + "</td><td>"
					+ Last_Name + "</td><td>"
					+ Phone_Number + "</td><td>"
					+ Address + "</td><td>"
					+ Company_Name + "</td><td>"
					+ Company_HeadCount
					+ "</td><td>" +Industry
					+ "</td><td>" + Institute
					+ "</td><td>" + Source
					+ "</td></tr>";

			   //   console.log("part_of_list_tbody = "+part_of_list_tbody);
				  } 
			  
		  }
		 
		//  console.log("part_of_list_tbody1 = "+part_of_list_tbody);
		  console.log("list_type = "+list_type);
			$("#csv_select_all").show();
			$("#csv_form").show();
			document.getElementById("csv_leads_list").innerHTML = part_of_list_tbody;
		  
		 
		} else  {
			 console.log("list_type = "+list_type);
				document.getElementById("csv_leads_list").innerHTML = entire_list_tbody;
				// getSubscribersList();
		} 
});


$('#save_xlslead').on('click', MOVEXLSLead);
function MOVEXLSLead(event) {
	console.log("in MOVEXLSLead");
	CreatestaticDatasource();
	console.log("funnelName1 = "+localStorage.getItem('funnelName'));
	//console.log("mailtemp = "+mailtemp);
	var text = '{"remoteuser":"' + remoteuser + '", "funnelName":"'
	+ localStorage.getItem('funnelName') + '", "Category":"Explore",  "group":"' + localStorage.getItem('groupname')  + '", "contacts":' + json_object + '}';
	
 console.log("exldatasource = "+text);
	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/saveFunnel.AddContacts',
		async : false,
		data : {
			data : text
		},
		success : function(dataa) {
			
			
			console.log("jsonresp= " + dataa);
			   document.getElementById('errconfn').innerHTML="Contacts updated Successfully";

		}
	});
	
	
	
	var oTable = document.getElementById('csv_leads_list');
    var rowLength = oTable.rows.length;
  //  console.log("in i= "+rowLength);
    var allfields=[];
    var list_type2 = $("input[name='radio-list']:checked").val();
	  console.log("change list_type = "+list_type2);
	 if (list_type2 === 'list2') {
    for (var i = 0; i < rowLength; i++){
 //   $('#csv_leads_list').find('tr').each(function (i, row) {
    	 var checked;
    	var row = oTable.rows.item(i);
    	

    	var rowjs={};
    	//console.log("in i= "+i);
     
       var rowsarr=[];
    	var sfdarr=[];  
      //gets cells of current row  
       var oCells = oTable.rows.item(i).cells;
       //gets amount of cells of current row
       var cellLength = oCells.length;
		   				for(var j = 0; j < cellLength; j++)
		   				{
		   				  var checkBox = document.getElementsByName("csv_part_of_list_sub_chk_boxes");
		   			//	console.log("checkBox = "+checkBox.length+" j ="+j);
//		   				  for(var i=0;i<checkBox.length-1;i++){
		   					
		   					   checked=checkBox[i].checked;
		   				
//		   				  }
		   					//	console.log("jjjjjjjj=  "+j+" :: cellVal= "+cellVal);
		   					 var list_type = $("input[name='radio-list']:checked").val();
		   					 
		   				  if(checked === true) {
		   				
		   					var cellVal ="";
	   						cellVal = oCells.item(j).innerHTML.replace(/<[^>]*>/g, '');
	   						if(j==2){
	   						rowjs["Email"]=cellVal;
	   						}
	   						if(j==3){
		   						rowjs["FirstName"]=cellVal;
		   						}
	   						if(j==4){
		   						rowjs["LastName"]=cellVal;
		   						}
	   						if(j==5){
		   						rowjs["PhoneNumber"]=cellVal;
		   						}
	   						if(j==6){
		   						rowjs["Address"]=cellVal;
		   						}
	   						if(j==7){
		   						rowjs["CompanyName"]=cellVal;
		   						}
	   						if(j==8){
		   						rowjs["CompanyHeadCount"]=cellVal;
		   						}
	   						if(j==9){
		   						rowjs["Industry"]=cellVal;
		   						}
	   						if(j==10){
		   						rowjs["Institute"]=cellVal;
		   						}
	   						if(j==11){
		   						rowjs["Source"]=cellVal;
		   						}
	   					
		   			       
		   			        
		   			          
		   						 
		   						
		   					 }
		   							
		   				}
		   			//  console.log("final rowjs--"+JSON.stringify(rowjs));
		   			 if(checked === true) {
		   				allfields.push(rowjs);
		   			 }
		   			  
        
    }
}
    console.log("final allfields data--"+JSON.stringify(allfields));

}

function ongroupselect(){
	
	
	var group = document.getElementById("grlist").value;
	console.log("on change group = "+group);
	localStorage.setItem('groupname', group);
	//document.getElementById("mailtemplateIframetab").src = 'https://bizlem.io:8083/portal/servlet/service/DoctigerCreation?group='+group;//+localStorage.getItem('funnelName')+'
	//console.log("iframe 3");
	
	//getMailtemplateList();
	
}




// btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block

$(".drip-funnel-section").on("click", function() {
	$(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
});

$(".manage-leades-section").on("click", function() {
	$(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
});

$(".set-up-funnel-section").on("click", function() {
	$(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
});

$("select.select-funnel-main").change(function() {
	var selectedVal = $(this).children("option:selected").val();
	if (selectedVal) {
		$('.select-catg').css('display', 'block');
	}
});

$("select.select-catg").change(function() {
	var selectedVal = $(this).children("option:selected").val();
	if (selectedVal) {
		$('.select-campaign').css('display', 'block');
	}
});

$("select.select-campaign").change(function() {
	var selectedVal = $(this).children("option:selected").val();
	if (selectedVal) {
		$('.current-server-box').css('display', 'block');
	}
});

$("select.select-campaign-new").change(function() {
	var selectedVal = $(this).children("option:selected").val();
	if (selectedVal) {
		$('.campaign-name-box').css('display', 'block');
	}
});

$('.add-btn-save').click(function() {
	$('.show-save-btn').css('display', 'block');
});


$(document).ready(
		function() {
			$('body')
					.on(
							'change',
							'.click-select',
							function() {
								var data = $(this).val();
								if (data == 'category') {
									$(this).parents('tr').find('td').find(
											'.open-select').css('display',
											'block');
									$(this).parents('tr').find('td').find(
											'.open-input').css('display',
											'none');
								} else if (data == 'url') {
									$(this).parents('tr').find('td').find(
											'.open-select').css('display',
											'none');
									$(this).parents('tr').find('td').find(
											'.open-input').css('display',
											'block');
								} else {
									$(this).parents('tr').find('td').find(
											'.open-select').css('display',
											'none');
									$(this).parents('tr').find('td').find(
											'.open-input').css('display',
											'none');

								}
							});
		});

$(document).ready(
		function() {
			$('body').on(
					'change',
					'.select-oprater',
					function() {
						var data = $(this).val();
						if (data == 'eqals' || data == 'not-equals'
								|| data == 'is-null' || data == 'is-not-null') {
							$(this).parents('tr').find('td').find(
									'.remove-slect').css('display', 'none');
							$(this).parents('tr').find('td').find(
									'.remove-input').css('display', 'none');
						} else {
							$(this).parents('tr').find('td').find(
									'.remove-slect').css('display', 'block');
							$(this).parents('tr').find('td').find(
									'.remove-input').css('display', 'block');
						}
					});
		});

$(".hide-create-new-list-box").on("click", function() {
	$('.create-new-list-main').slideUp();
});

$(".hide-meege-list-box").on("click", function() {
	$('.import-from-csv-file').slideUp();
});

$(".merge").on("click", function() {
	$('.import-from-csv-file').slideDown();
});

$(".filter-list-1-section-btn").on(
		"click",
		function() {
			$(this).parents('.container-main').find('.filter-section-list-1')
					.css('display', 'block');
			$(this).parents('.container-main').find(
					'.filter-right-list-1-section').removeClass('col-md-12')
					.addClass('col-md-8');
		});

$('input[name=radio]').click(function() {
	if (this.id == "radio1-section") {
		$(".show-import-from-csv-file").slideDown();
	} else {
		$(".show-import-from-csv-file").slideUp();
	}
});

$('input[name=radio]').click(function() {
	if (this.id == "radio3") {
		$(".up-import-from-csv-file").slideDown();
	} else {
		$(".up-import-from-csv-file").slideUp();
	}
});

$('input[name=radio]').click(function() {
	if (this.id == "radio4") {
		$(".show-import-from-csv-file2").slideDown();
	} else {
		$(".show-import-from-csv-file2").slideUp();
	}
});

$('input[name=radio]').click(function() {
	if (this.id == "merge-existing") {
		$(".show-merge-existing-list").slideDown();
	} else {
		$(".show-merge-existing-list").slideUp();
	}
});

// append set up button start
$('body').on(
		'click',
		'.add-more-btn',
		function() {	
			var copy = $(this).parents('.funnel-sub').find('.addMore-copy')
			.html();
			console.log("copy = "+copy);
	$(this).parents('.funnel-sub').find('.add-more-sub-copy').append(
			copy);
//add-more-sub-copy
	
	
	var sp = $(this).parents('.funnel-sub').find(
			'.addMore-sub:last-child .set-up-btn span').html();
	console.log("sp== = "+sp);
	
	
	var sp1 = Number(sp) + 1;
	console.log("sp = "+sp1);

	console.log("spfor = "+spfor);
	if(spfor>1){
		spfor=spfor+1;
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(spfor);
	}else if(spforent>1){
		spforent=spforent+1;
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(spforent);
	}else if(spforinf>1){
		spforinf=spforinf+1;
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(spforinf);
	}else if(spforwarm>1){
		spforwarm=spforwarm+1;
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(spforwarm);
	}else if(spforconnect>1){
		spforconnect=spforconnect+1;
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(spforconnect);
	}
	
	else{
		$(this).parents('.funnel-sub').find(
		'.addMore-sub:last-child .set-up-btn span').html(sp1);
	}
			
		});

$('body').on('click', '.copy-remove-btn', function() {
	$(this).parents('.addMore-sub').remove();
});
// append set up button end

//
$('body').on(
		'click',
		'.open-content-set-up-compaign',
		function() {
			var sp = $(this).parents('.funnel-sub').find(
			'.addMore-sub:last-child .set-up-btn span').html();
	console.log("span = "+sp);
			
			$('.set-up-campaign-form-box').slideDown();
			$(this).parents('.funnel-section').find(
					'.funnel-sub .open-content-set-up-compaign').removeClass(
					'active');
			$(this).addClass('active');
		});

/*
 * $(function () { $(".datepicker").datepicker({ autoclose: true,
 * todayHighlight: true }).datepicker('update', new Date()); });
 */

$('input[name=radio]').click(function() {
	if (this.id == "radio-time") {
		$(".date-time-section").slideDown();
	} else {
		$(".date-time-section").slideUp();
	}
});

$('.open-import-contact-main-section').click(function() {
	$('.import-contact-main-form').slideUp();
	$('.import-contact-main-section').slideDown();
});

// custom select box

$('input[name=radio-list]').click(function() {
	if (this.id == "list1") {
		$(".entire-list-section").slideDown();
	} else {
		$(".entire-list-section").slideUp();
	}
});
$('input[name=radio-list]').click(function() {
	if (this.id == "list2") {
		$(".part-list-section").slideDown();
	} else {
		$(".part-list-section").slideUp();
	}
});

$('input[name=radio-list1]').click(function() {
	if (this.id == "list5") {
		$(".entire-list-section").slideDown();
	} else {
		$(".entire-list-section").slideUp();
	}
});
$('input[name=radio-list1]').click(function() {
	if (this.id == "list6") {
		$(".part-list-section").slideDown();
	} else {
		$(".part-list-section").slideUp();
	}
});

$(document).ready(function() {
	$('.to-from-list-part-section tr').click(function() {
		if (this.style.background == "" || this.style.background == "white") {
			$(this).css('background', '#6E000B').css('color', '#fff');
		} else {
			$(this).css('background', 'white').css('color', '#000');
		}
	});
});

$('.creat-new-campaigns-menu').on('click', function() {
	$("#collapseExample2").slideDown();
	$(".set-up-campaign-form-box").slideDown();
});

$(".creat-new-campaigns-menu").click(function() {
	$('html, body').animate({
		scrollTop : $("#op").offset().top
	}, 2000);
});

$('.create-new-list').on('click', function() {
	$('.create-new-list-main').slideDown();
});

$('body')
		.on(
				'click',
				'.add-new-row',
				function() {
					var addRow = $(
							'.create-new-list-table tbody .append-row-tr')
							.html();
					var noRow = $(
							'.create-new-list-table tbody tr:last-child th:first-child')
							.html();
					var noRow1 = Number(noRow) + 1;
					$('.create-new-list-table tbody').append(
							'<tr><th scope="row">' + noRow1 + '</th>' + addRow
									+ '</tr>');
				});

$('input[name=radio-msg]').click(function() {
	if (this.id == "radio") {
		$(".comment-box-part").slideUp();
	} else {
		$(".comment-box-part").slideDown();
	}
});

$('input[name=radio-msg]').click(function() {
	if (this.id == "radio2") {
		$(".aend-a-webpage-section").slideUp();
	} else {
		$(".aend-a-webpage-section").slideDown();
	}
});

$('input[name=radio-tag]').click(function() {
	if (this.id == "radio-tag") {
		$(".drop-down-part").slideUp();
	} else {
		$(".drop-down-part").slideDown();
	}
});

$('input[name=radio-tag]').click(function() {
	if (this.id == "radio-tag-2") {
		$(".drop-down-part-second").slideUp();
	} else {
		$(".drop-down-part-second").slideDown();
	}
});

$(".drip-funnel-save").on("click", function() {
	$(".drip-funnel-section").trigger("click");
	$(".manage-leades-section").trigger("click");
});

$(".select-existing-upload-save").on("click", function() {
	$(".manage-leades-section").trigger("click");
	$(".set-up-funnel-section").trigger("click");
});

$(".manage-leads-create-new-list-save").on("click", function() {
	$(".manage-leades-section").trigger("click");
	$(".set-up-funnel-section").trigger("click");
});

$(".manage-leads-existing-list-save").on("click", function() {
	$(".manage-leades-section").trigger("click");
	$(".set-up-funnel-section").trigger("click");
});

// $('#op').trigger( "click" );

$('input[name=checkbox-apply-funnel]').click(function() {
	$('.show-apply-to-all-subfunnel-section').slideToggle();
});

$('input[name=checkbox-default-mass]').click(function() {
	$('.show-checkbox-default-mass').slideToggle();
});

$('input[name=table-check-1]').click(function() {
	$('.show-table-check-1').slideToggle();
});

$('input[name=table-check-2]').click(function() {
	$('.show-table-check-2').slideToggle();
});

$('input[name=table-check-3]').click(function() {
	$('.show-table-check-3').slideToggle();
});

$('input[name=table-check-4]').click(function() {
	$('.show-table-check-4').slideToggle();
});

$('.plsu-append-row').click(function() {
	var copy = $('.get-row-append').html();
	$('.append-row-part').append(copy);
});

function toggleIcon(e) {
	$(e.target).prev('.panel-heading').find(".more-less").toggleClass(
			'glyphicon-plus glyphicon-minus');
}
$('.panel-group').on('hidden.bs.collapse', toggleIcon);
$('.panel-group').on('shown.bs.collapse', toggleIcon);

$('body')
		.on(
				'click',
				'.add-row-btn',
				function() {
					var addRow = $('.table-set-up-funnel tbody tr:last-child')
							.html();
					var noRow = $(this).parents('.main-tbl').find(
							'tbody tr:last-child th:first-child').html();
					var noRow1 = Number(noRow) + 1;

					var id1 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".field-ID-select").attr('id');
					var id2 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".type-ID-select").attr('id');
					var id3 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".oprater-ID-select").attr('id');
					var id4 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".opraters-ID-select").attr('id');

					var id5 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".field-name-input").attr('id');
					var id6 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".opt1-input").attr('id');
					var id7 = $('.table-set-up-funnel tbody tr:last-child')
							.find(".opt2-input").attr('id');

					var res = addRow.replace(id1, "fieldID" + noRow1);
					res = res.replace(id2, "typeID" + noRow1);
					res = res.replace(id3, "operatorID" + noRow1);
					res = res.replace(id4, "operatorIDs" + noRow1);

					res = res.replace(id5, "fieldName" + noRow1);
					res = res.replace(id6, "opt1Val" + noRow1);
					res = res.replace(id7, "opt2Val" + noRow1);

					$(this).parents('.main-tbl').find(
							'.table-set-up-funnel tbody').append(
							'<tr class="new">' + res + '</tr>');
					$(this).parents('.main-tbl').find(
							'.table-set-up-funnel tbody tr:last-child th')
							.html(noRow1);
				});

$('.add-new-form').click(function() {
	var getBox = $('.append-div-box').html();
	$('.main-box-section').append(getBox);
});

$('.remove-new-form').click(function() {
	$('.main-box-section').find('li:last-child').remove();
});

$('body').on(
		'click',
		'.remove-row-btn',
		function() {
			var firstTr = $(this).parents('.main-tbl').find(
					'tbody tr:last-child').attr('class');
			if (firstTr == 'new') {
				$(this).parents('.main-tbl').find('tbody tr:last-child')
						.remove();
			}
		});

$('body').on(
		'click',
		'.add-row-btn-second-tbl',
		function() {
			var addRowNew = $('.second-tbl tbody tr:last-child').html();
			$(this).parents('.second-tbl').find('.second-tbl-sub tbody')
					.append('<tr class="new">' + addRowNew + '</tr>');
		});

$('body').on(
		'click',
		'.remove-row-second-tbl',
		function() {
			var firstTr = $(this).parents('.second-tbl').find(
					'.second-tbl-sub tbody tr:last-child').attr('class');
			if (firstTr == 'new') {
				$(this).parents('.second-tbl').find('tbody tr:last-child')
						.remove();
			}
		});






$(document).on(
		'click',
		'.open-content-set-up-compaign',
		function() {
			getMailtemplateList();
			var mutlicat="";
			funnelName1="";
			 funnelName1 = document.getElementById("funnel-name").value;
			 console.log("funnelName1 "+funnelName1);
			 localStorage.setItem('funnelName1', funnelName1);
			// alert($(this).text());
			var sub_funnel_name = $(this).text();
			console.log("mutlicat ="+mutlicat);
			// alert(sub_funnel_name);// EC-Explore EnC-Entice IC-Inform WC-Warm
			// CC-Connect
			if (sub_funnel_name.includes("EC") == true) {
				
				localStorage.setItem('SubFunnelName', "Explore");
				localStorage.setItem('DistanceBtnCampaign', document
						.getElementById("explore").value);
				
				var check="Set Up EC 1";
			
				if(sub_funnel_name.trim() != check.trim()){
					console.log("mutlicat"+mutlicat);
					 mutlicat=sub_funnel_name.replace("Set Up ", "");
					 mutlicat=mutlicat.replace(" ", "_");
					mutlicat="_"+mutlicat;
					console.log("mutlicat = "+mutlicat);
					console.log("funnelName1 = "+funnelName1);
						funnelName1=funnelName1+mutlicat;
						console.log("funnelName1 2 = "+funnelName1);
						localStorage.setItem('funnelName1', funnelName1);
						 mutlicat="";
					
				}
				
				// alert('document.getElementById("explore").value; :
				// '+document.getElementById("explore").value);
			} else if (sub_funnel_name.includes("EnC") == true) {
				localStorage.setItem('SubFunnelName', "Entice");
				localStorage.setItem('DistanceBtnCampaign', document
						.getElementById("entice").value);
				var check="Set Up EnC 1";
				
				if(sub_funnel_name.trim() != check.trim()){
					
					 mutlicat=sub_funnel_name.replace("Set Up ", "");
					mutlicat=mutlicat.replace(" ", "_");
					mutlicat="_"+mutlicat;
					console.log("mutlicat = "+mutlicat);
				
						funnelName1=funnelName1+mutlicat;
						localStorage.setItem('funnelName1', funnelName1);
					
					
				}
				
			} else if (sub_funnel_name.includes("IC") == true) {
				localStorage.setItem('SubFunnelName', "Inform");
				localStorage.setItem('DistanceBtnCampaign', document
						.getElementById("inform").value);
				var check="Set Up IC 1";
				
				if(sub_funnel_name.trim() != check.trim()){
					
					 mutlicat=sub_funnel_name.replace("Set Up ", "");
					mutlicat=mutlicat.replace(" ", "_");
					mutlicat="_"+mutlicat;
					console.log("mutlicat = "+mutlicat);
				
						funnelName1=funnelName1+mutlicat;
						localStorage.setItem('funnelName1', funnelName1);
					
					
				}
				
			} else if (sub_funnel_name.includes("WC") == true) {
				localStorage.setItem('SubFunnelName', "Warm");
				localStorage.setItem('DistanceBtnCampaign', document
						.getElementById("warm").value);
				var check="Set Up WC 1";
				
				if(sub_funnel_name.trim() != check.trim()){
					
					 mutlicat=sub_funnel_name.replace("Set Up ", "");
					mutlicat=mutlicat.replace(" ", "_");
					mutlicat="_"+mutlicat;
					console.log("mutlicat = "+mutlicat);
				
						funnelName1=funnelName1+mutlicat;
						localStorage.setItem('funnelName1', funnelName1);
					
					
				}
			} else if (sub_funnel_name.includes("CC") == true) {
				localStorage.setItem('SubFunnelName', "Connect");
				localStorage.setItem('DistanceBtnCampaign', document
						.getElementById("connect").value);
				
				var check="Set Up CC 1";
				
				if(sub_funnel_name.trim() != check.trim()){
					
					 mutlicat=sub_funnel_name.replace("Set Up ", "");
					mutlicat=mutlicat.replace(" ", "_");
					mutlicat="_"+mutlicat;
					console.log("mutlicat = "+mutlicat);
				
						funnelName1=funnelName1+mutlicat;
						localStorage.setItem('funnelName1', funnelName1);
					
					
				}
			}
			// document.getElementById("campaign-name").campaign-name
			$("#campaign-name").attr("placeholder",
					"Enter the Campaign Name For " + sub_funnel_name);

		});

function addQueryString() {
	// alert("inside addQueryString");
	var campaignName = document.getElementById("campaign-name").value;
	localStorage.setItem('campaignName', campaignName);
	// var fromName = document.getElementById("from-name").value;
	// var fromEmailAddress =
	// document.getElementById("from-email-address").value;
	// alert("localStorage.getItem('listid') "+localStorage.getItem('listid'));
	var queryString = "?campaignName=" + campaignName + "&fromName="
			+ localStorage.getItem('fromName') + "&fromEmailAddress="
			+ localStorage.getItem('fromEmailAddress') + "&funnelName="
			+ localStorage.getItem('funnelName') + "&SubFunnelName="
			+ localStorage.getItem('SubFunnelName') + "&DistanceBtnCampaign="
			+ localStorage.getItem('DistanceBtnCampaign') + "&listid="
			+ localStorage.getItem('listid');
	// alert('queryString : '+queryString);
	// document.getElementById("campaign-name-nxt").setAttribute("href",
	// "set-up-campaign.html"+queryString);
	document.getElementById("campaign-name-nxt").setAttribute("href",
			"/portal/servlet/service/ui.set-up-campaign");
	// window.location.href = "page2.html" + queryString;
	// <%=request.getContextPath()%>/servlet/service/ui.set-up-campaign
}
// Global Variable For Displaying Subscribers List In Table
var entire_list_tbody = "";
var part_of_list_tbody = "";
// Global Variables for funnel setup

var funnelName;
var fromName;
var fromEmailAddress;

$('input[type=radio][name=radio]').change(function() {
	if (this.value == 'csv_file_upload') {
		// alert("csv_file_upload is selected");
	} else if (this.value == 'integrated_ervices') {


	} else if (this.value == 'create_new_list') {
		// alert("create_new_list is selected");
	} else if (this.value == 'create_from_existing_list') {
		// alert("create_new_list is selected");
		getSubscribersList();
	} else if (this.value == 'merge_existing') {
		// alert("merge_existing is selected");
		getMergeSubscribersList();
	} else {
		// alert("You have selected something else");
	}
});

$('input[type=radio][name=radio-list1]')
		.change(
				function() {
					// alert("this.value : "+this.value);
					if (this.value == 'list5') {
						// alert("Entire List is selected");
						var e = document.getElementById("listdropbox");
						var struser = e.options[e.selectedIndex].value;
						if (struser == 'Choose list') {
							alert('Please Select List');
							document.getElementById("leads_list").innerHTML = "";
							// return;
						} else {
							document.getElementById("leads_list").innerHTML = entire_list_tbody;
						}
						// getSubscribersList();
					} else if (this.value == 'list6') {
						// alert("Part Of List is selected part_of_leads_list");
						var e = document.getElementById("listdropbox");
						var struser = e.options[e.selectedIndex].value;
						if (struser == 'Choose list') {
							alert('Please Select List');
							document.getElementById("part_of_leads_list").innerHTML = "";
							$("#list_select_all").hide();
							$("#list_form").hide();
							return;
						} else {
							$("#list_select_all").show();
							$("#list_form").show();
							document.getElementById("part_of_leads_list").innerHTML = part_of_list_tbody;

						}
					} else {
						alert("You have selected something else");
					}
				});



function getSubscribersList() {
	// var remoteuser ='viki@gmail.com';
	//
	console.log("remoteuser : " + remoteuser);
	$
			.ajax({
				type : 'POST',
				url : '/portal/servlet/service/Searchlist.listsearch',
				data : {
					remoteuser : remoteuser,
				},
				success : function(dataa) {
					// alert ("Searchlist.listsearch : "+dataa);
					var jsonData = JSON.parse(dataa);
					var optionMap = "";
					for (var i = 0; i < jsonData.JsonArray.length; i++) {
						var array = jsonData.JsonArray[i];
						optionMap = optionMap + "<option value="
								+ array.List_id + ">" + array.List_Name
								+ "</option>";
					}
					document.getElementById("listdropbox").innerHTML = "<option>Choose list</option>"
							+ optionMap;
					// document.getElementById("mergelistdropbox1").innerHTML =
					// "<option>Choose list</option>" + optionMap;
					// document.getElementById("mergelistdropbox2").innerHTML =
					// "<option>Choose list</option>" + optionMap;
				}
			});
}
function getMergeSubscribersList() {
	// var remoteuser ='viki@gmail.com';
	// var remoteuser ='<%=request.getRemoteUser()%>';
	console.log("remoteuser : " + remoteuser);
	$
			.ajax({
				type : 'POST',
				url : '/portal/servlet/service/Searchlist.listsearch',
				data : {
					remoteuser : remoteuser,
				},
				success : function(dataa) {
					// alert ("Searchlist.listsearch : "+dataa);
					var jsonData = JSON.parse(dataa);
					var optionMap = "";
					for (var i = 0; i < jsonData.JsonArray.length; i++) {
						var array = jsonData.JsonArray[i];
						optionMap = optionMap + "<option value="
								+ array.List_id + ">" + array.List_Name
								+ "</option>";
					}
					document.getElementById("mergelistdropbox1").innerHTML = "<option>Choose list</option>"
							+ optionMap;
					document.getElementById("mergelistdropbox2").innerHTML = "<option>Choose list</option>"
							+ optionMap;
				}
			});
}
// save_list_merge
$('#save_list_merge').on('click', saveMergeListLead);
function saveMergeListLead(event) {
	// var list_type = $("input[name='radio-list1']:checked").val();
	var e1 = document.getElementById("mergelistdropbox1");
	var mergelist1 = e1.options[e1.selectedIndex].value;

	var mergelistName = e1.options[e1.selectedIndex].text;
	// alert("mergelistName : "+mergelistName);
	// .text()

	var e2 = document.getElementById("mergelistdropbox2");
	var mergelist2 = e2.options[e2.selectedIndex].value;

	// alert('mergelist1 : '+mergelist1+' mergelist2 : '+mergelist2);

	if (mergelist1 != 'Choose list' && mergelist2 != 'Choose list') {
		if (mergelist1 == mergelist2) {
			alert("You Have Selected Same List !");
			return;
		}

		$.ajax({
			type : 'POST',
			dataType : 'json',
			async : false,
			// contentType: false,
			url : '/portal/servlet/service/Searchlist.getlist_data_new',
			data : {
				selectedlistid : mergelist2
			},
			success : function(data) {

				var lesdArr = [];
				for (var i = 0; i < data.length; i++) {
					var lead_info = data[i];
					var leadObj = {};
					leadObj['EmailAddress'] = lead_info.Email_Name;
					leadObj['FirstName'] = lead_info.Name;
					leadObj['LastName'] = 'NA';
					leadObj['PhoneNumber'] = 'NA';
					leadObj['Address'] = 'NA';
					lesdArr.push(leadObj);
				}
				var finalleadObj = {};
				finalleadObj['ListId'] = mergelist1;
				finalleadObj['ListName'] = mergelistName;
				finalleadObj['ListData'] = lesdArr;
				// alert("data : "+JSON.stringify(finalleadObj));
				saveMergeLeadData(lesdArr, mergelistName, mergelist1);

			}
		});

	} else {
		alert("You have selected something else");
		/*
		 * document.getElementById("leads_list").innerHTML ="";
		 * document.getElementById("part_of_leads_list").innerHTML =""; $(
		 * "#list_select_all" ).hide(); $( "#list_form" ).hide();
		 */

	}
}
function saveMergeLeadData(lesdArr, listname, listid) {
	// alert(JSON.stringify(lesdArr));
	// createlistPlusLead createListAndSubscribers
	// alert('listname: '+listname);
	// alert('lesdArr: '+JSON.stringify(lesdArr));
	// console.log('lesdArr: '+JSON.stringify(lesdArr));
	// localStorage.setItem('lesdArr',JSON.stringify(lesdArr));

	// alert('funnelCreationStatus:
	// '+localStorage.getItem('funnelCreationStatus'));
	if (localStorage.getItem('funnelCreationStatus') == 'Funnel Created') {
		alert('Funnel: ' + localStorage.getItem('funnelName') + " Created");
	} else if (localStorage.getItem('funnelCreationStatus') == 'Funnel Exists') {
		alert('Funnel: ' + localStorage.getItem('funnelName') + " Exists");
	}
	// alert('remoteuser: '+localStorage.getItem('remoteuser'));
	// alert('funnelName: '+localStorage.getItem('funnelName'));
	// alert('fromName: '+localStorage.getItem('fromName'));
	// alert('fromEmailAddress: '+localStorage.getItem('fromEmailAddress'));

	$.ajax({
		// url:
		// '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
		url : '/portal/servlet/service/createListAndSubscribers.mergeList',// createListAndSubscribers
		type : 'POST',
		async : false,
		cache : false,
		// timeout: 30000,
		data : {
			jsonArr : JSON.stringify(lesdArr),
			listname : listname,
			list_id : listid,
			remoteuser : localStorage.getItem('remoteuser'),
			funnelName : localStorage.getItem('funnelName'),
			fromName : localStorage.getItem('fromName'),
			fromEmailAddress : localStorage.getItem('fromEmailAddress'),
		},
		// cache: false,
		// dataType: 'json',
		// processData: false,
		// contentType: false,
		success : function(data) {
			var jsonData = JSON.parse(data);
			// alert("data: listid"+jsonData.listid);
			localStorage.setItem('listid', jsonData.listid);
			// console.log("-------------------------subscribers
			// start-----------------------");
			// console.log("data: "+data);
			// console.log("-------------------------subscribers
			// edn-----------------------");
		},
	// timeout: 30000 // sets timeout to 30 seconds

	});

	// alert('Hello Akhilesh: ');

}

$('#save_list_lead').on('click', saveListLead);
function saveListLead(event) {
	var list_type = $("input[name='radio-list1']:checked").val();
	if (list_type == 'list5') {
		var entire_list_leadtrs = document
				.getElementsByName("entire_list_leadtr");
		var entire_list_name = document.getElementById("entire_list_name").value;
	
		// alert('entire_list_name : '+entire_list_name);
		// alert(entire_list_leadtrs.length);

		var lesdArr = [];
		for (var m = 0; m < entire_list_leadtrs.length; m++) {
			var lead_chk_box = entire_list_leadtrs[m];
			var leadtr = document.getElementById($(lead_chk_box).attr('id')
					.replace("part_of_list_leadchk", "part_of_list_leadtr"));
			// alert(leadtr);
			var leadObj = {};
			for (var j = 0; j < leadtr.cells.length; j++) {
				// alert(leadtr.cells.item(j).innerHTML);
				if (j == 1) {
					leadObj['EmailAddress'] = leadtr.cells.item(1).innerHTML;
				}
				if (j == 2) {
					leadObj['FirstName'] = leadtr.cells.item(2).innerHTML;
				}
				if (j == 3) {
					leadObj['LastName'] = leadtr.cells.item(3).innerHTML;
				}
				if (j == 4) {
					leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML;
				}
				if (j == 5) {
					leadObj['Address'] = leadtr.cells.item(5).innerHTML;
				}
				if (j == 6) {
					// leadObj['Address'] = tabs[j];
				//	console.log();
					leadObj['Email_status'] = leadtr.cells.item(6).innerHTML;
					// if(tabs[j]==undefined){
					// leadObj['Email_status'] = 'NA';
					// }
					// else{
					// leadObj['Email_status'] = tabs[j];
					// }
				}
			}
			lesdArr.push(leadObj);
			console.log("lesdArr=====  == " + JSON.stringify(lesdArr));
			var finalleadObj = {};
			finalleadObj['ListName'] = entire_list_name;
			finalleadObj['ListData'] = lesdArr;

		}
		// alert(JSON.stringify(lesdArr));
		console.log(JSON.stringify(finalleadObj));
		saveLeadData(lesdArr, entire_list_name);

	} else if (list_type == 'list6') {
		var part_of_list_leadtrs = document
				.getElementsByName("part_of_list_leadtr");
		// alert(part_of_list_leadtrs.length);

		var part_of_list_name = document.getElementById("part_of_list_name").value;
		// alert('part_of_list_name : '+part_of_list_name);

		if ($('input[name="part_of_list_sub_chk_boxes"]:checked').length == 0) {
			alert('Please Select At Least One Lead.');
		} else {
			var lead_chk_boxes = $('input[name="part_of_list_sub_chk_boxes"]:checked');
			var lesdArr = [];
			for (var m = 0; m < lead_chk_boxes.length; m++) {
				var lead_chk_box = lead_chk_boxes[m];
				var leadtr = document
						.getElementById($(lead_chk_box).attr('id').replace(
								"part_of_list_leadchk", "part_of_list_leadtr"));
				// alert(leadtr);
				var leadObj = {};
				for (var j = 0; j < leadtr.cells.length; j++) {
					// alert(leadtr.cells.item(j).innerHTML);
					if (j == 2) {
						leadObj['EmailAddress'] = leadtr.cells.item(2).innerHTML;
					}
					if (j == 3) {
						leadObj['FirstName'] = leadtr.cells.item(3).innerHTML;
					}
					if (j == 4) {
						leadObj['LastName'] = leadtr.cells.item(4).innerHTML;
					}
					if (j == 5) {
						leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML;
					}
					if (j == 6) {
						leadObj['Address'] = leadtr.cells.item(6).innerHTML;
					}
					if (j == 7) {
						leadObj['Email_status'] = leadtr.cells.item(7).innerHTML;
					}
				}

				lesdArr.push(leadObj);
				var finalleadObj = {};
				finalleadObj['ListName'] = part_of_list_name;
				finalleadObj['ListData'] = lesdArr;

			}
			console.log("lesdArr 3 == " + JSON.stringify(lesdArr));
			// alert(JSON.stringify(lesdArr));
			console.log(JSON.stringify(finalleadObj));
			saveLeadData(lesdArr, part_of_list_name);
		}
	}

}



$('input[type=radio][name=radio-list]')
		.change(
				function() {
					// alert("this.value : "+this.value);
					if (this.value == 'list1') {
						if (document.getElementById("fileUploader").files.length == 0) {
							document.getElementById("leads_list").innerHTML = "";
							alert("no files selected");
						} else {
							document.getElementById("csv_leads_list").innerHTML = entire_list_tbody;
						}
					} else if (this.value == 'list2') {
						$("#csv_select_all").show();
						$("#csv_form").show();
						document.getElementById("csv_part_of_leads_list").innerHTML = part_of_list_tbody;
				

					} else {
						alert("You have selected something else");
					}
				});

$('#save_csv_lead').on('click', saveLead);
function saveLead(event) {
	var list_type = $("input[name='radio-list']:checked").val();
	if (list_type == 'list1') {
		var entire_list_leadtrs = document
				.getElementsByName("csv_entire_list_leadtr");
		// alert(entire_list_leadtrs.length);

		var csv_entire_list_name = document
				.getElementById("csv_entire_list_name").value;
		if (csv_entire_list_name == "" || csv_entire_list_name == null) {
			console.log('plz enter _list_name');
			
			return false;
		}
		// alert('csv_entire_list_name : '+csv_entire_list_name);

		var lesdArr = [];
		for (var m = 0; m < entire_list_leadtrs.length; m++) {
			var lead_chk_box = entire_list_leadtrs[m];
			var leadtr = document.getElementById($(lead_chk_box).attr('id')
					.replace("csv_part_of_list_leadchk",
							"csv_part_of_list_leadtr"));
			// alert(leadtr);
			var leadObj = {};
			console.log("leadtr.cells.length ="+leadtr.cells.length);
			for (var j = 0; j < leadtr.cells.length; j++) {
				// alert(leadtr.cells.item(j).innerHTML);
				if (j == 1) {
					leadObj['EmailAddress'] = leadtr.cells.item(1).innerHTML;
				}
				if (j == 2) {
					leadObj['FirstName'] = leadtr.cells.item(2).innerHTML;
				}
				if (j == 3) {
					leadObj['LastName'] = leadtr.cells.item(3).innerHTML;
				}
				if (j == 4) {
					leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML;
				}
				if (j == 5) {
					leadObj['Address'] = leadtr.cells.item(5).innerHTML;
				}
				if (j == 6) {
					leadObj['CompanyName'] = leadtr.cells.item(6).innerHTML;
				}
				if (j == 7) {
					leadObj['CompanyHeadCount'] = leadtr.cells.item(7).innerHTML;
				}
				if (j == 8) {
					leadObj['Industry'] = leadtr.cells.item(8).innerHTML;
				}
				if (j == 9) {
					leadObj['Institute'] = leadtr.cells.item(9).innerHTML;
				}
				if (j == 10) {
					leadObj['Source'] = leadtr.cells.item(10).innerHTML;
				}
				if (j == 11) {
					leadObj['Email_Status'] = leadtr.cells.item(11).innerHTML;
					console.log(j+" jj  leadtr.cells.item(10).innerHTML ="+leadtr.cells.item(11).innerHTML);
				}
				console.log(j+" jj  leadtr.cells.item(10).innerHTML ");
				/*
				 * if(j==1){ leadObj['EmailAddress'] =
				 * leadtr.cells.item(1).innerHTML; } if(j==2){
				 * leadObj['FirstName'] = leadtr.cells.item(2).innerHTML; }
				 * if(j==3){ leadObj['LastName'] =
				 * leadtr.cells.item(3).innerHTML; } if(j==4){
				 * leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML; }
				 * if(j==5){ leadObj['Address'] =
				 * leadtr.cells.item(5).innerHTML; }
				 */
			}
			lesdArr.push(leadObj);
			var finalleadObj = {};
			finalleadObj['ListName'] = csv_entire_list_name;
			finalleadObj['ListData'] = lesdArr;

		}
		// alert(JSON.stringify(lesdArr));
	
		console.log("all csv ="+JSON.stringify(finalleadObj));
		saveLeadData(lesdArr, csv_entire_list_name);

	} else if (list_type == 'list2') {
		var part_of_list_leadtrs = document
				.getElementsByName("csv_part_of_list_leadtr");
		// alert(part_of_list_leadtrs.length);
		var csv_part_of_list_name = document
				.getElementById("csv_part_of_list_name").value;
		console.log('csv_part_of_list_name : ' + csv_part_of_list_name);
		// alert('csv_part_of_list_name : '+csv_part_of_list_name);

		if ($('input[name="csv_part_of_list_sub_chk_boxes"]:checked').length == 0) {
			alert('Please Select At Least One Lead.');
		} else {
			var lead_chk_boxes = $('input[name="csv_part_of_list_sub_chk_boxes"]:checked');
			var lesdArr = [];
			for (var m = 0; m < lead_chk_boxes.length; m++) {
				var lead_chk_box = lead_chk_boxes[m];
				var leadtr = document.getElementById($(lead_chk_box).attr('id')
						.replace("csv_part_of_list_leadchk",
								"csv_part_of_list_leadtr"));
				// alert(leadtr);
				var leadObj = {};
				for (var j = 0; j < leadtr.cells.length; j++) {
					// alert(leadtr.cells.item(j).innerHTML);
					if (j == 2) {
						leadObj['EmailAddress'] = leadtr.cells.item(2).innerHTML;
					}
					if (j == 3) {
						leadObj['FirstName'] = leadtr.cells.item(3).innerHTML;
					}
					if (j == 4) {
						leadObj['LastName'] = leadtr.cells.item(4).innerHTML;
					}
					if (j == 5) {
						leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML;
					}
					if (j == 6) {
						leadObj['Address'] = leadtr.cells.item(6).innerHTML;
					}
					if (j == 7) {
						leadObj['CompanyName'] = leadtr.cells.item(7).innerHTML;
					}
					if (j == 8) {
						leadObj['CompanyHeadCount'] = leadtr.cells.item(8).innerHTML;
					}
					if (j == 9) {
						leadObj['Industry'] = leadtr.cells.item(9).innerHTML;
					}
					if (j == 10) {
						leadObj['Institute'] = leadtr.cells.item(10).innerHTML;
					}
					if (j == 11) {
						leadObj['Source'] = leadtr.cells.item(11).innerHTML;
					}
					if (j == 12) {
						leadObj['Email_Status'] = leadtr.cells.item(12).innerHTML;
					}
					/*
					 * if(j==2){ leadObj['EmailAddress'] =
					 * leadtr.cells.item(2).innerHTML; } if(j==3){
					 * leadObj['FirstName'] = leadtr.cells.item(3).innerHTML; }
					 * if(j==4){ leadObj['LastName'] =
					 * leadtr.cells.item(4).innerHTML; } if(j==5){
					 * leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML; }
					 * if(j==6){ leadObj['Address'] =
					 * leadtr.cells.item(6).innerHTML; }
					 */
				}
				lesdArr.push(leadObj);
				var finalleadObj = {};
				finalleadObj['ListName'] = csv_part_of_list_name;
				finalleadObj['ListData'] = lesdArr;

			}
			// alert(JSON.stringify(lesdArr));
			console.log(JSON.stringify(finalleadObj));
			saveLeadData(lesdArr, csv_part_of_list_name);
		}
	}

}

function saveLeadData(lesdArr, listname) {
	// alert(JSON.stringify(lesdArr));
	// createlistPlusLead createListAndSubscribers
	// alert('listname: '+listname);
	// alert('lesdArr: '+JSON.stringify(lesdArr));
	// console.log('lesdArr: '+JSON.stringify(lesdArr));
	// localStorage.setItem('lesdArr',JSON.stringify(lesdArr));

	console.log('funnelCreationStatus: '
			+ localStorage.getItem('funnelCreationStatus'));
	if (localStorage.getItem('funnelCreationStatus') == 'Funnel Created') {
		alert('Funnel: ' + localStorage.getItem('funnelName') + " Created");
	} else if (localStorage.getItem('funnelCreationStatus') == 'Funnel Exists') {
		alert('Funnel: ' + localStorage.getItem('funnelName') + " Exists");
	} else {

	}
	// alert('remoteuser: '+localStorage.getItem('remoteuser'));
	// alert('funnelName: '+localStorage.getItem('funnelName'));
	// alert('fromName: '+localStorage.getItem('fromName'));
	// alert('fromEmailAddress: '+localStorage.getItem('fromEmailAddress'));
	console.log("JSON.stringify(lesdArr) :: " + JSON.stringify(lesdArr));
	console.log("uploadedSubscriberCount :: " + uploadedSubscriberCount);
	var gr = localStorage.getItem('groupname');
	if(listname !== null && listname !== ''){
	$
			.ajax({
				// url:
				// '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
				url : '/portal/servlet/service/createListAndSubscribers.createlistPlusLead',// createListAndSubscribers
				type : 'POST',
				async : false,
				cache : false,
				// timeout: 30000,
				data : {
					jsonArr : JSON.stringify(lesdArr),
					listname : listname,
					remoteuser : localStorage.getItem('remoteuser'),
					funnelName : localStorage.getItem('funnelName'),
					fromName : localStorage.getItem('fromName'),
					fromEmailAddress : localStorage.getItem('fromEmailAddress'),
					SubscriberCount : uploadedSubscriberCount,
					group : gr,
				},
				// cache: false,
				// dataType: 'json',
				// processData: false,
				// contentType: false,
				success : function(data) {
					console.log("jsonData1 = " + data);
					var jsonData = JSON.parse(data);
					console.log("jsonData = " + jsonData);
					// alert("data: listid"+jsonData.listid);

					if (jsonData.quantity) {
						console.log("jsonData.quantity = " + jsonData.quantity);
						alert("You can not upload leads more than "
								+ jsonData.quantity);
						var body = '<h3>You can not upload leads more than '
								+ jsonData.quantity + '</h3>';
						$("#MyPopup .modal-body").html(body);
					} else {
						localStorage.setItem('listid', jsonData.listid);
						console.log("jsonData: " + jsonData);
						// alert("Subscribers List with id "+jsonData.listid+"
						// created Sucessfully.");
						var body = '<h4>Subscribers List created Sucessfully</h4>';
						$("#MyPopup .modal-body").html(body);
					}

					$("#MyPopup").modal("show");
					// console.log("-------------------------subscribers
					// start-----------------------");

					// console.log("-------------------------subscribers
					// edn-----------------------");
				},
			// timeout: 30000 // sets timeout to 30 seconds

			});
}else{
	var body = '<h3>Kindly enter List Name</h3>';
	$("#MyPopup .modal-body").html(body);
	$("#MyPopup").modal("show");	
	
}

}

function saveFunnelData(funnelId) {
	// var remoteuser ='<%=request.getRemoteUser()%>';
	// localStorage.setItem('tst_lcl',"Testing Local Storage on server");
	// alert('localStorage : '+localStorage.getItem('tst_lcl'));
	// var remoteuser ='<%=request.getRemoteUser()%>';
	// remoteuser='viki@gmail.com';
	console.log("funnel user =" + remoteuser);
	funnelName = document.getElementById("funnel-name").value;
	// funnelName1 = document.getElementById("funnel-name").value;
  localStorage.setItem('funnelName1', funnelName1);
		
	fromName = document.getElementById("from-name").value;
	fromEmailAddress = document.getElementById("from-email-address").value;
	
	localStorage.setItem('funnelName', funnelName);
	localStorage.setItem('fromName', fromName);
	localStorage.setItem('fromEmailAddress', fromEmailAddress);
	var groupname = "";
	if (document.getElementById("grlist")
			&& document.getElementById("grlist").value) {

		groupname = document.getElementById("grlist").value;
		console.log("groupname local = " + groupname);
	} else {
		console.log("groupname local = " + groupname);
	}
	localStorage.setItem('groupname', groupname);
	console.log("groupname local = " + groupname);
	// alert("remoteuser : "+remoteuser+"\n"+" funnelName : "+funnelName+"\n"+"
	// fromName : "+fromName+"\n"+" fromEmailAddress : "+fromEmailAddress);
	
	if(funnelName !== null && funnelName !== '')
	{
	
	
}else{
	var body = '<h3>Kindly fill the Details</h3>';
	$("#MyPopup .modal-body").html(body);
	$("#MyPopup").modal("show");
	
}

}

function getFunnelList() {
	// var remoteuser ='viki_gmail.com';
	console.log("remoteuser : " + remoteuser);
	$
			.ajax({
				type : 'GET',
				url : '/portal/servlet/service/ViewCmpaigns.SalesFunnelList?email='
						+ remoteuser,
				// http://prod.bizlem.io:8082/portal/servlet/service/createCampaign.getFunnel?userName=viki_gmail.com
				data : {},
				success : function(dataa) {
					console.log("Searchlist.listsearch : " + dataa);
					var funnel_list_json_arr = JSON.parse(dataa);
					var optionMap = "";
					var funnelsmoniter = "";
					for (var i = 0; i < funnel_list_json_arr.length; i++) {
						var funnel_list_json_obj = funnel_list_json_arr[i];
						// alert ("funnel_list_json_obj :
						// "+funnel_list_json_obj);
						optionMap = optionMap + "<option value='"
								+ funnel_list_json_obj + "'>"
								+ funnel_list_json_obj + "</option>";
						
						funnelsmoniter = funnelsmoniter
						+ "<option style='color:black' value='"
						+ funnel_list_json_obj + "'>"
						+ funnel_list_json_obj
						+ "</option>";
					}
					// alert("optionMap : "+optionMap);
					document.getElementById("funnel_list").innerHTML = "<option>Choose Funnel</option>"
							+ optionMap;
					
					
				
					// alert("optionMap : "+optionMap);
					// <option style="color:gray"
					// value="null">select one option</option>
					document.getElementById("MainFunnel").innerHTML = "<option style='color:black'>Choose list</option>"
							+ funnelsmoniter;
					document.getElementById("insidemove").innerHTML = "<option style='color:black'>Choose list</option>"
							+ funnelsmoniter;
					
					// document.getElementById("mergelistdropbox1").innerHTML =
					// "<option>Choose list</option>" + optionMap;
					// document.getElementById("mergelistdropbox2").innerHTML =
					// "<option>Choose list</option>" + optionMap;
				}
			});
}

$(document).ready(function() {
	$("#funnel_list").change(function() {
		// var remoteuser ='viki_gmail.com';
		// alert('hi : ');
		var e = document.getElementById("funnel_list");
		var funnel_name = e.options[e.selectedIndex].value;
		// alert('list_id : '+list_id);
		var list = "";
		if (funnel_name != 'Choose Funnel') {

			getFunnel(funnel_name, remoteuser);
		} else {

		}
	});
});
var allpagesarr;
function getFunnel(funnel_name, remoteuser) {
	// "Rule_Engine" "SFDC_SELECTDATA" "External_Data" "TRANSFORM_DATA"
	console.log("inside getFunnel getFunnel : " + funnel_name);
		
	$
			.ajax({
			
				url : '/portal/servlet/service/ViewCmpaigns.ViewPageDetails?email='
						+ remoteuser + '&funnel=' + funnel_name,// July17F02',//createListAndSubscribers
				type : 'GET',
				async : false,
				cache : false,
				
				data : {
					
				},
				success : function(data) {
					console.log("getFunnel Ajax : " + data);
					
					var funnel_details_json_obj = JSON.parse(data);
					var funnelName = funnel_details_json_obj["funnelName"];

					var ExploreJsonArr = funnel_details_json_obj["Explore"];
					var ExploreJsonObj = funnel_details_json_obj["Connect"];

					var EnticeJsonArr = funnel_details_json_obj["Entice"];
					var EnticeJsonObj = funnel_details_json_obj["Connect"];

					var InformJsonArr = funnel_details_json_obj["Inform"];
					var InformJsonObj = funnel_details_json_obj["Connect"];

					var WarmJsonArr = funnel_details_json_obj["Warm"];
					var WarmJsonObj = funnel_details_json_obj["Connect"];

					var ConnectJsonArr = funnel_details_json_obj["Connect"];

					var CampaignJsonObj = funnel_details_json_obj["Campaign"];
					var CampaignExploreJsonArr = funnel_details_json_obj["AllCategory"];
					console.log("CampaignExploreJsonArr  =  "+CampaignExploreJsonArr);
					var CampaignEnticeJsonArr = [];//CampaignJsonObj["Entice"];
					var CampaignInformJsonArr = []; //CampaignJsonObj["Inform"];
					var CampaignWarmJsonArr = []; //CampaignJsonObj["Warm"];
					var CampaignConnectJsonArr = []; //CampaignJsonObj["Connect"];
					var totalenticelead = 0;
					var totalinflead = 0;
					var totalwarmlead = 0;
					var totalconnlead = 0;

					// entice
					var enticeJsonObj = null;
					var ListActivateDateStr = "";
					
					console.log("CampaignExploreJsonArr : "
							+ CampaignExploreJsonArr);

					var sub_funnel_heading = '<tr style="text-align: center;"><td colspan="15"><strong>Sub Funnel</strong></td></tr>';

					var category_heading = '<tr style="text-align: center;"><td colspan="3"><h3>Unknown</h3></td><td colspan="3"><h3>Cold</h3></td><td colspan="3"><h3>Warm</h3></td><td colspan="3"><h3>Hot</h3></td><td colspan="3"><h3>Connect </h3></td></tr><tr><th><b>Unknown</b></th><th><b>Date</b></th><th><b>Leads</b></th><th><b>Cold</b></th><th><b>Date</b></th><th><b>Leads</b></th><th><b>Warm</b></th><th><b>Date</b></th><th><b>Leads</b></th><th><b>Hot</b></th><th><b>Date</b></th><th><b>Leads</b></th><th><b>Connect</b></th><th><b>Date</b> </th><th><b>Leads</b> </th><tr>';

					var final_tbl_body = '';

					var tbl_body = '';

					var exp_tbl_body = '';
					var ent_tbl_body = '';
					var inf_tbl_body = '';
					var warm_tbl_body = '';
					var connect_tbl_body = '';

					var max_row_length = CampaignExploreJsonArr.length;
					console.log('max_row_length : ' + max_row_length);

					/*
					 * if(i==0){ exp_tbl_body='<tr><td>Explore</td><td>Date</td><td>Leads</td></tr>'; }
					 * ent_tbl_body='<td>Entice</td><td>Date</td><td>Leads</td>';
					 * inf_tbl_body='<td>Inform</td><td>Date</td><td>Leads</td>';
					 * warm_tbl_body='<td>Warm</td><td>Date</td><td>Leads</td>';
					 * connect_tbl_body='<td>Connect</td><td>Date</td><td>Leads</td>';
					 */

					for (var i = 0; i < 1; i++) {
						var CampaignExploreJsonObj = CampaignExploreJsonArr[i];
//						
						console.log("CampaignExploreJsonObj : "
								+ JSON.stringify(CampaignExploreJsonObj));
						// JSON.stringify(arr)
						// <td class="leadM"><a>10000</a></td>
						console.log("Campaign_Id= "
								+ CampaignExploreJsonObj.Campaign_Id
								+ " category= Explore");
						exp_tbl_body = '<td >'
								+ CampaignExploreJsonObj.CampaignName
								+ '</td><td>'
								+ CampaignExploreJsonObj.Campaign_Date
								+ '</td><td class="leadM" camp_id="'
								+ CampaignExploreJsonObj.Campaign_Id
								+ '" category="Explore" leadtotal="'
								+ CampaignExploreJsonObj.Subscribers_Count
								+ '" campname="'
								+ CampaignExploreJsonObj.CampaignName
								+ '" campdate="'
								+ CampaignExploreJsonObj.Campaign_Date
								+ '"><a style = "text-decoration:underline;">'
								+ CampaignExploreJsonObj.Subscribers_Count
								+ '</a></td>';

//						if (CampaignEnticeJsonArr[i] != undefined) {
							var CampaignEnticeJsonObj = CampaignExploreJsonArr[1];
						

							ent_tbl_body = '<td >'
									+ CampaignEnticeJsonObj.CampaignName
									+ '</td><td>' + CampaignEnticeJsonObj.Campaign_Date
									+ '</td><td class="leadM" camp_id="'
									+ CampaignEnticeJsonObj.Campaign_Id
									+ '"category="Entice" leadtotal="'
									+ CampaignEnticeJsonObj.Subscribers_Count + '" campname="'
									+ CampaignEnticeJsonObj.CampaignName
									+ '"  campdate="' + CampaignEnticeJsonObj.Campaign_Date + '"><a style = "text-decoration:underline;">'
									+ CampaignEnticeJsonObj.Subscribers_Count + '</a></td>';
//						} else {
//							ent_tbl_body = '<td></td><td></td><td></td>';
//
//						}
//						if (CampaignInformJsonArr[i] != undefined) {
							var CampaignInformJsonObj =  CampaignExploreJsonArr[2];
							
							inf_tbl_body = '<td >'
									+ CampaignInformJsonObj.CampaignName
									+ '</td><td >' + CampaignInformJsonObj.Campaign_Date
									+ '</td><td class="leadM" camp_id="'
									+ CampaignInformJsonObj.Campaign_Id
									+ '" category="Inform" leadtotal="'
									+ CampaignInformJsonObj.Subscribers_Count + '" campname="'
									+ CampaignInformJsonObj.CampaignName
									+ '" campdate="' + CampaignInformJsonObj.Campaign_Date + '"><a style = "text-decoration:underline;">'
									+ CampaignInformJsonObj.Subscribers_Count + '</a></td>';
//						} else {
//							inf_tbl_body = '<td></td><td></td><td></td>';
//
//						}
//						if (CampaignWarmJsonArr[i] != undefined) {
							var CampaignWarmJsonObj =  CampaignExploreJsonArr[3];
							

							warm_tbl_body = '<td>'
									+ CampaignWarmJsonObj.CampaignName
									+ '</td><td >' + CampaignWarmJsonObj.Campaign_Date
									+ '</td><td class="leadM" camp_id="'
									+ CampaignWarmJsonObj.Campaign_Id
									+ '" category="Warm" leadtotal="'
									+ CampaignWarmJsonObj.Subscribers_Count + '" campname="'
									+ CampaignWarmJsonObj.CampaignName
									+ '" campdate="' + CampaignWarmJsonObj.Campaign_Date + '"><a style = "text-decoration:underline;">'
									+  CampaignWarmJsonObj.Subscribers_Count + '</a></td>';
//						} else {
//							warm_tbl_body = '<td></td><td></td><td></td>';
//
//						}

//						if (CampaignConnectJsonArr[i] != undefined) {
							var CampaignConnectJsonObj =  CampaignExploreJsonArr[3];
							

							connect_tbl_body = '<td >'
									+ CampaignConnectJsonObj.CampaignName
									+ '</td><td >' + CampaignConnectJsonObj.Campaign_Date
									+ '</td><td class="leadM" camp_id="'
									+ CampaignConnectJsonObj.Campaign_Id
									+ '" category="Connect" leadtotal="'
									+ CampaignConnectJsonObj.Subscribers_Count + '" campname="'
									+ CampaignConnectJsonObj.CampaignName
									+ '" campdate="' + CampaignConnectJsonObj.Campaign_Date + '"><a style = "text-decoration:underline;">'
									+ CampaignConnectJsonObj.Subscribers_Count + '</a></td>';
//						} else {
//							connect_tbl_body = '<td></td><td></td><td></td>';
//
//						}

						tbl_body = tbl_body + '<tr>' + exp_tbl_body
								+ ent_tbl_body + inf_tbl_body + warm_tbl_body
								+ connect_tbl_body + '</tr>';

					}

					document.getElementById("sub_funnel_tbl_body").innerHTML = sub_funnel_heading
							+ category_heading + tbl_body;
				
					var campaign_json_obj = null;
					var CreatedBy = null;
					var funnelName = null;
					var SubFunnelName = null;
					var CampaignNodeNameInSling = null;
					var FromName = null;
					var FromEmailAddress = null;
					var CampaignName = null;
					var Subject = null;
					var Body = null;
					var Type = null;
					var Campaign_Id = null;
					var List_Id = null;
					var campaign_status = null;
					var Campaign_Date = null;

					var explore_row_add_more_sub_copy = "";
					var entice_row_add_more_sub_copy = "";
					var inform_row_add_more_sub_copy = "";
					var warm_row_add_more_sub_copy = "";
					var connect_row_add_more_sub_copy = "";

				}
			});

	$('.leadM')
			.on(
					'click',
					function() {

						console.log("calling leadm");
						var $this = $(this);
						var campid = $this.attr("camp_id");
						console.log("campid" + campid);

						var category = $this.attr("category");
						console.log("category" + category + "funnel_name= "
								+ funnel_name);
						var totallead = $this.attr("leadtotal");
						console.log("totallead= " + totallead);
						var campdate = $this.attr("campdate");
						var campname = $this.attr("campname");
						console.log("campdate= " + campdate + "= campname= "
								+ campname);
						
						var oldcat=["Explore","Entice","Inform","Warm","Connect"];
						var newcat=["Unknown","Cold","Warm","Hot","Connect"];
						$('#classModal1').modal('show');
						$
								.ajax({
									url : '/portal/servlet/service/ViewCmpaigns.ClickedData',
									type : "GET",
									data : {
										email : remoteuser,
										category : category,
										funnel : funnel_name,
										campid : campid
									},
									success : function(data) {
										console.log(data);
										var jsonData = JSON.parse(data);
										var trvalue = "";
										var tractiveuser = "";
										document.getElementById("CampName").innerHTML = campname;
										document.getElementById("CampDate").innerHTML = campdate;
										var newcatg;
										for(var i=0;i<oldcat.length;i++){
											var oldcatg=oldcat[i];
											if(oldcatg === category ){
												newcatg=newcat[i];
												console.log("newcatg = "+newcatg);
												break;
											}
											
											
										}
										document.getElementById("cat").innerHTML = newcatg;
										//
										var TotalLeadData = jsonData.TotalLeadData;

										var totalLeads = '';
										var bounced = '';
										var opened = '';
										var clicked = '';
										var upgradationrate = '';
										var totalSessiontime = '';
										var totalVisits = '';
										var recentSessiontime = '';
										var recentVisits = '';
										if (TotalLeadData
												.hasOwnProperty('totalLeads')) {

											totalLeads = totallead;
										}
										if (TotalLeadData
												.hasOwnProperty('bounced')) {

											bounced = TotalLeadData.bounced;
										}
										if (TotalLeadData
												.hasOwnProperty('opened')) {

											opened = TotalLeadData.opened;
										}
										if (TotalLeadData
												.hasOwnProperty('clicked')) {

											clicked = TotalLeadData.clicked;
										}
										if (TotalLeadData
												.hasOwnProperty('upgradationrate')) {

											upgradationrate = TotalLeadData.upgradationrate;
										}
										if (TotalLeadData
												.hasOwnProperty('totalSessiontime')) {

											totalSessiontime = TotalLeadData.totalSessiontime;
										}
										if (TotalLeadData
												.hasOwnProperty('totalVisits')) {

											totalVisits = TotalLeadData.totalVisits;
										}
										if (TotalLeadData
												.hasOwnProperty('recentSessiontime')) {

											recentSessiontime = TotalLeadData.recentSessiontime;
										}
										if (TotalLeadData
												.hasOwnProperty('recentVisits')) {

											recentVisits = TotalLeadData.recentVisits;
										}
										allpagesarr = jsonData.ActiveUsers;
										console.log("all pages= "
												+ JSON.stringify(allpagesarr));
										// var
										// urljsonarr=jsonData.ActiveUsers.PageUrls;
										// console.log(urljsonarr);
										console.log("total-leads-td : "
												+ totalLeads);
										trvalue = trvalue
												+ '<tr><td><a style = "text-decoration:underline;" class="total-leads-td">'
												+ totallead
												+ '</a></td> <td><a class="bounced-td">'
												+ bounced
												+ '</a></td><td><a class="opened-td">'
												+ opened
												+ '</a></td><td><a style = "text-decoration:underline;" class="clicked-td">'
												+ clicked + '</a></td><td>'
												+ totalSessiontime
												+ '</td></td><td>'
												+ totalVisits
												+ '</td></td><td>'
												+ recentSessiontime
												+ '</td><td>' + recentVisits
												+ '</td>';
										document.getElementById("leadstable").innerHTML = trvalue;
										// <td
										// class="width130">'+upgradationrate+'</td></td>
										for (var i = 0; i < jsonData.ActiveUsers.length; i++) {
											var activeuserdata = jsonData.ActiveUsers[i];
											// <a>'+activeuserdata.Email+'</a>
											var srno = i + 1;
											var emailid1 = activeuserdata.Email;
											// <td class="will-depend"
											// style="display: none;"></td><td
											// class="will-depend"
											// style="display: none;"></td> <td
											// class="will-depend"
											// style="display: none;"></td> <td
											// class="will-depend"
											// style="display: none;"></td>
											tractiveuser = tractiveuser
													+ '<tr id="data'
													+ i
													+ '"><td width="20">'
													+ srno
													+ '</td><td style="word-wrap: break-word;" class="clk-op-tb" emailid="'
													+ activeuserdata.Email
													+ '" ><a style = "text-decoration:underline;" onclick="return urlmodelfun(\''
													+ activeuserdata.Email
													+ '\');" >'
													+ activeuserdata.Email
													+ '</a></td><td class="tb-name clk-op-tb" style="display: none;">'
													+ activeuserdata.Name
													+ '</td><td class="tb-source clk-op-tb" style="display: none;">'
													+ activeuserdata.Source
													+ '</td><td class="to-re-plus">'
													+ activeuserdata.totalSessiontime
													+ '<label class="label label-success"></label></td><td class="to-re-plus">'
													+ activeuserdata.totalVisits
													+ '<label class="label label-success"></label></td><td class="recent to-re-plus" style="display: none;">'
													+ activeuserdata.recentSessiontime
													+ '<label class="label label-success"></label></td><td class="recent to-re-plus" style="display: none;">'
													+ activeuserdata.recentVisits
													+ '</td> ';
											// <td style="width:8%;"></td>
											//<label class="label label-success"><i class="fa fa-plus to-rec-plus"></i></label>
										}
										document
												.getElementById("activeusertable").innerHTML = tractiveuser;
										document
												.getElementById("activeusertableclicked").innerHTML = tractiveuser;
										document
												.getElementById("activeusertableopened").innerHTML = tractiveuser;
										document
												.getElementById("activeusertablebounced").innerHTML = tractiveuser;

										clickedfunction();

									}

								});

					});

	// }
	/*
	 * $('#urlclk').on('click', function(){ $('#classModal1').modal('show');
	 * $('#popupTable2').css('display','block'); });
	 */
}

function urlmodelfun(emailid) {
	console.log("in url model");
	$('#modelurl').modal('show');

	console.log("allpagesarr= " + JSON.stringify(allpagesarr));
	// var $this = $(this);
	// var emailid = $this.attr("emailid");
	console.log("emailid = " + emailid);
	var pageurlarr;
	for (var i = 0; i < allpagesarr.length; i++) {
		var activeuserdata = allpagesarr[i];
		if (activeuserdata.Email == emailid) {
			var sp=" ";
			document.getElementById("emailurl").innerHTML = sp + emailid;
			pageurlarr = activeuserdata.PageUrls;
			console.log("pageurlarr= " + JSON.stringify(pageurlarr));

		}
	}
	var urltblbdy = '';
	console.log("pageurlarr1= " + JSON.stringify(pageurlarr)
			+ pageurlarr.length);
	if (pageurlarr.length > 0) {
		for (var j = 0; j < pageurlarr.length; j++) {
			var emailurldata = pageurlarr[j];

			console.log("emailurldata= " + JSON.stringify(emailurldata));
			urltblbdy = urltblbdy + '<tr><td>' + emailurldata.URL
					+ '</td><td >' + emailurldata.AvgTime + '</td></tr>';
		}
		document.getElementById("urlstable").innerHTML = urltblbdy;
	} else {

	}

}

// $('#schedule_campaign').on('click', scheduleCampaign);


$(function() {

	$("#btnClosePopup").click(function() {
		$("#MyPopup").modal("hide");
	});
});

$(function() {

	$("#btnClosePopup3").click(function() {
		$("#modelurl").modal("hide");
	});
});
// monitoring

$('.lead-tb').click(function() {
	$('.tb-name').toggle();
	$('.tb-source').toggle();
	$('.lead-part-plus').toggleClass('fa-minus fa-plus');
	$('.total-plus').removeClass('fa-minus');
	$('.total-plus').addClass('fa-plus');
	$('.recent').hide();
});

$('.to-re-plus').on("click", function() {
	$('.will-depend').toggle();
	$(this).find('.to-rec-plus').toggleClass('fa-minus fa-plus');
});

$('.total').click(function() {
	$('.recent').toggle();
	$('.tb-name').hide();
	$('.tb-source').hide();
	$('.lead-part-plus').removeClass('fa-minus');
	$('.lead-part-plus').addClass('fa-plus');
	$('.total-plus').toggleClass('fa-minus fa-plus');
});

$('.total-leads-td').click(function() {
	$('.total-leads').css('display', 'block');
	$('.bounced').css('display', 'none');
	$('.opened').css('display', 'none');
	$('.clicked').css('display', 'none');
});

$('.bounced-td').click(function() {
	$('.bounced').css('display', 'block');
	$('.opened').css('display', 'none');
	$('.clicked').css('display', 'none');
	$('.total-leads').css('display', 'none');
});

$('.opened-td').click(function() {
	$('.opened').css('display', 'block');
	$('.bounced').css('display', 'none');
	$('.clicked').css('display', 'none');
	$('.total-leads').css('display', 'none');
});

$('.clicked-td').click(function() {
	$('.clicked').css('display', 'block');
	$('.opened').css('display', 'none');
	$('.bounced').css('display', 'none');
	$('.total-leads').css('display', 'none');
});

// $('.to-re-plus').click(function(){
// $(this).find('.to-rec-plus').removeClass('fa-plus');
// $(this).find('.to-rec-plus').addClass('fa-minus');
// });

// $('.lead-tb').click(function(){
// $('.lead-part-plus').removeClass('fa-plus');
// $('.lead-part-plus').addClass('fa-minus');
// $('.total-plus').removeClass('fa-minus');
// $('.total-plus').addClass('fa-plus');
// $('.to-rec-plus').removeClass('fa-minus');
// $('.to-rec-plus').addClass('fa-plus');
// });

// $('.total').click(function(){
// $('.total-plus').removeClass('fa-plus');
// $('.total-plus').addClass('fa-minus');
// $('.lead-part-plus').removeClass('fa-minus');
// $('.lead-part-plus').addClass('fa-plus');
// $('.to-rec-plus').removeClass('fa-minus');
// $('.to-rec-plus').addClass('fa-plus');
// });

$('.configuration').on('click', function() {
	$('.pull-push-section').css('display', 'block');
});

$('.configuration-post').on('click', function() {
	$('.post-section').css('display', 'block');
});

// $('.lead-tb').on("click",function(){
// $('.tb-name').show();
// $('.tb-source').show();
// $('.recent').hide();
// $('.will-depend').hide();
// });

// $('.total').on("click",function(){
// $('.recent').show();
// $('.tb-name').hide();
// $('.tb-source').hide();
// });

$('.lead-tb').on("click", function() {
	$('.tb-three').hide();
});

$('.clk-op-tb').on("click", function() {

	$('.tb-three').show();
});

// $('.to-re-plus').on("click",function(){
// $('.will-depend').show();
// });

$("#popupTable2").on("click", function() {
	$('#tbl3').css('display', 'block');
});

$('.lead-part-plus').click(function() {
	$('.lead-sub').toggle();
	$('.lead-part-plus').toggleClass('fa-minus fa-plus');
	$('.minus-ve-plus').removeClass('fa-minus');
	$('.minus-ve-plus').addClass('fa-plus');
	$('.minus-ve-tb-sub').hide();
	$('.total-res').hide();
	$('.total-res-tb').hide();
	$('.to-res-plus-tb').hide();
	$('.plus-ve-plus').removeClass('fa-minus');
	$('.plus-ve-plus').addClass('fa-plus');
	$('.lead-th').toggleClass('display-block display-none');
	$('.ve-th').removeClass('display-block');
	$('.ve-th').addClass('display-none');
	$('.total-res').hide();
});

$('.minus-ve-plus').click(function() {
	$('.total-res').hide();
	$('.will-depend').hide();
	$('.minus-ve-tb-sub').toggle();
	$('.minus-ve-plus').toggleClass('fa-minus fa-plus');
	$('.lead-part-plus').removeClass('fa-minus');
	$('.lead-part-plus').addClass('fa-plus');
	$('.lead-sub').hide();
	$('.total-res-tb').hide();
	$('.plus-ve-plus').removeClass('fa-minus');
	$('.plus-ve-plus').addClass('fa-plus');
	$('.lead-th').removeClass('display-block');
	$('.lead-th').addClass('display-none');
	$('.ve-th').toggleClass('display-block display-none');
});

$('.plus-ve-plus').click(function() {
	$('.total-res-tb').toggle();
	$('.total-res').hide();
	$('.will-depend').hide();
	$('.total-plus').removeClass('fa-minus');
	$('.total-plus').addClass('fa-plus');
	$('.to-rec-plus').removeClass('fa-minus');
	$('.to-rec-plus').addClass('fa-plus');
	$('.plus-ve-plus').toggleClass('fa-minus fa-plus');
	$('.minus-ve-plus').removeClass('fa-minus');
	$('.minus-ve-plus').addClass('fa-plus');
	$('.minus-ve-tb-sub').hide();
	$('.lead-part-plus').removeClass('fa-minus');
	$('.lead-part-plus').addClass('fa-plus');
	$('.lead-sub').hide();
	$('.lead-th').removeClass('display-block');
	$('.lead-th').addClass('display-none');
	$('.ve-th').removeClass('display-block');
	$('.total-res').hide();
	$('.ve-th').addClass('display-none');
});

$('.total-plus').click(function() {
	$('.total-res').toggle();
	$('.total-plus').toggleClass('fa-minus fa-plus');
	$('.minus-ve-plus').removeClass('fa-minus');
	$('.minus-ve-plus').addClass('fa-plus');
	$('.minus-ve-tb-sub').hide();
	$('.lead-part-plus').removeClass('fa-minus');
	$('.lead-part-plus').addClass('fa-plus');
	$('.lead-sub').hide();
	$('.lead-part-plus').removeClass('fa-minus');
	$('.lead-part-plus').addClass('fa-plus');
	$('.lead-sub').hide();
});

$('.to-res-plus').click(function() {
	$('.to-res-plus-tb').toggle();
	$(this).find('.to-rec-plus').toggleClass('fa-minus fa-plus');
});

$('.move-btn')
		.click(
				function() {
					var total = $('.move-th').length;
					if (total == 0) {
						$('.delete-th').remove();
						$('.delete-td').remove();
						$('.classTableC')
								.find('thead tr')
								.prepend(
										'<th width="100" style="border:1px 0px 0px 0px solid red !important;" class="move-th"></th>');
						$('.classTableC')
								.find('tbody tr')
								.prepend(
										'<td width="100" class="move-td"><input type="radio" name="radio1" class="move-checkbox-click"><button class="radio1-btn btn btn-warning btn-xs apd-move move-checkbox-btn" style="display:none;" id="dynamicmovebutton" onclick="movefunction(this);">Move</button></td>');
					}
				});

// $('body').on('click', '.move-checkbox-click', function(){
// if($(this).prop("checked") == true){
// }else{
// $(this).parent('td').find('button').css('display','none');
// }
// });

$('body').on('click', 'input[name="radio1"]', function() {
	$(this).parents('tbody').find('button').hide();
	$(this).parent('td').find('button').css('display', 'inline-block');
});

$('body').on('click', 'input[name="radio2"]', function() {
	$(this).parents('tbody').find('button').hide();
	$(this).parent('td').find('button').css('display', 'inline-block');
});

$('.delete-btn')
		.click(
				function() {
					var total = $('.delete-th').length;
					if (total == 0) {
						$('.move-th').remove();
						$('.move-td').remove();
						$('.classTableC').find('thead tr').prepend(
								'<th width="100" class="delete-th"></th>');
						$('.classTableC')
								.find('tbody tr')
								.prepend(
										'<td width="100" class="delete-td"><input type="radio" name="radio2" class="delete-checkbox-click"><button class="btn btn-danger btn-xs apd-delete" style="display:none;">Delete</button></td>');
					}
				});

$('body').on('click', '.delete-checkbox-click', function() {
	if ($(this).prop("checked") == true) {
		$(this).parent('td').find('button').css('display', 'inline-block');
	} else {
		$(this).parent('td').find('button').css('display', 'none');
	}
});

$('body').on('click', '.move-checkbox-btn', function() {
	$('#myModal').modal('show');
});

$('.lead-part-plus').click(function() {
	$('.classTableC').addClass('width-60');
	$('.classTableC').addClass('width-50');
	$('.classTableC').removeClass('width-80');
	$('.classTableC').removeClass('width-100');
	$('.classTableC').removeClass('width-70');
});

$('.total-plus').click(function() {
	$('.classTableC').addClass('width-80');
	$('.classTableC').removeClass('width-60');
	$('.classTableC').removeClass('width-70');
});

$('.to-res-plus').click(function() {
	$('.classTableC').addClass('width-100');
	$('.classTableC').addClass('width-80');
	$('.classTableC').removeClass('width-60');
	$('.classTableC').removeClass('width-70');
});

$('.minus-ve-plus').click(function() {
	$('.classTableC').addClass('width-70');
	$('.classTableC').addClass('width-80');
	$('.classTableC').removeClass('width-60');
});



$(document)
		.ready(
				function() {
					$("#MainFunnel")
							.change(
									function() {
										var selectedVal = $(this).find(
												':selected').val();
										var funnelnamemain = $("#MainFunnel")
												.find(':selected').val();
										var remoteuser = localStorage
												.getItem('remoteuser');
										// alert(funnelnamemain);
										// alert(selectedVal);
										$
												.ajax({
													url : "/portal/servlet/service/Moniteringui.FunnelList",
													type : "POST",
													data : {

														selectedfunnel : selectedVal,
														email : remoteuser

													},
													success : function(data) {
														var funnel_list_json_arr = JSON
																.parse(data);
														console
																.log("funnel_list_json_arr= "
																		+ funnel_list_json_arr);
														var optionMap = "";
														for (var i = 0; i < funnel_list_json_arr.FunnelList.length; i++) {
															var funnel_list_json_obj = funnel_list_json_arr.FunnelList[i];

															optionMap = optionMap
																	+ "<option style='color:black' value='"
																	+ funnel_list_json_obj
																	+ "'>"
																	+ funnel_list_json_obj
																	+ "</option>";
														}
														document
																.getElementById("SubFunnel").innerHTML = "<option style='color:black'>Choose list</option>"
																+ optionMap;
														document
																.getElementById("subinsidemove").innerHTML = "<option>Choose list</option>"
																+ optionMap;

													}

												});
									});
				});

$(document)
		.ready(
				function() {
					$("#SubFunnel")
							.change(
									function() {
										var selectedVal = $(this).find(
												':selected').val();
										var funnelnamemain = $("#MainFunnel")
												.find(':selected').val();
										var username = localStorage
												.getItem('remoteuser');

										document
												.getElementById("subfunnelvalue").value = selectedVal;
										// alert(funnelnamemain);
										// alert(selectedVal);
										$
												.ajax({
													url : "/portal/servlet/service/Moniteringui.SubscriberData",
													type : "POST",
													data : {
														funnelname : funnelnamemain,
														subfunnelname : selectedVal,
														username : username
													},
													success : function(data) {

														console.log(data);
														var jsonData = JSON
																.parse(data);

														var tractiveuser = "";
														// "TotalLeadData":{"totalSessiontime":0,"recentSessiontime":0,"clicked":0}
														console
																.log("jsonData.TotalLeadData.totalSessiontime= "
																		+ jsonData.TotalLeadData.totalSessiontime);
														document
																.getElementById("tsesstim").innerHTML = jsonData.TotalLeadData.totalSessiontime;
														document
																.getElementById("tclk").innerHTML = jsonData.TotalLeadData.clicked;

														for (var i = 0; i < jsonData.ActiveUsers.length; i++) {
															var activeuserdata = jsonData.ActiveUsers[i];
															console
																	.log("activeuserdata = "
																			+ activeuserdata);
															document
																	.getElementById("subid").value = activeuserdata.SubscriberId;
															document
																	.getElementById("subemail").value = activeuserdata.Email;
															var srno = i + 1;
															tractiveuser = tractiveuser
																	+ '<tr id="data'
																	+ i
																	+ '"><td width="35">'
																	+ srno
																	+ '</td><td style="word-wrap: break-word;" class="clk-op-tb">'
																	+ activeuserdata.Email
																	+ '</td><td  class="tb-name lead-sub" style="display:none;">'
																	+ activeuserdata.Name
																	+ '</td><td class="tb-source lead-sub" style="display: none;">'
																	+ activeuserdata.Source
																	+ '</td><td></td><td class="minus-ve-tb-sub" style="display:none;"></td><td  style="display:none;">'
																	+ activeuserdata.SubscriberId
																	+ '</td><td class="minus-ve-tb-sub" style="display:none;"></td><td class="minus-ve-tb-sub" style="display:none;"></td><td></td><td class="to-re-plus total total-res-tb to-res-plus" style="display:none;">'
																	+ activeuserdata.totalSessiontime
																	+ '<label class="label label-success"><i class="fa fa-plus to-rec-plus"></i></label></td><td class="to-re-plus total total-res-tb to-res-plus" style="display:none;">'
																	+ activeuserdata.totalVisits
																	+ '<label class="label label-success"><i class="fa fa-plus to-rec-plus" ></i></label></td><td class="recent to-re-plus recent total-res to-res-plus" style="display: none;">'
																	+ activeuserdata.recentSessiontime
																	+ '<label class="label label-success"><i class="fa fa-plus to-rec-plus"></i></label></td><td class="recent to-re-plus recent total-res to-res-plus" style="display: none;">'
																	+ activeuserdata.recentVisits
																	+ '<label class="label label-success"><i class="fa fa-plus to-rec-plus" ></i></label></td><td class="will-depend to-res-plus-tb" style="display:none;">'
																	+ activeuserdata.recentVisits
																	+ '</td><td class="will-depend to-res-plus-tb" style="display:none;">'
																	+ activeuserdata.recentVisits
																	+ '</td><td class="will-depend to-res-plus-tb" style="display:none;">'
																	+ activeuserdata.recentVisits
																	+ '</td><td class="will-depend to-res-plus-tb" style="display:none;">'
																	+ activeuserdata.recentVisits
																	+ '</td>';

														}

														document
																.getElementById("activeusertable").innerHTML = tractiveuser;
														campaignid();

													}

												});

									});
				});

function campaignid() {

	var subf = document.getElementById("subfunnelvalue").value;
	// alert(subf);
	var username = localStorage.getItem('remoteuser');
	
	var funnelnamemain = $("#MainFunnel").find(':selected').val();
	console.log(funnelnamemain)

	$.ajax({
		url : "/portal/servlet/service/Moniteringui.CampaignList",
		type : "POST",
		data : {

			Subfunnelname : subf,
			username : username,
			funnelnamemain : funnelnamemain

		},
		success : function(data) {
			console.log(data);
			var jsonData = JSON.parse(data);
			document.getElementById("campid").value = jsonData.Campaign_Id;
			// document.getElementById("listid").value=jsonData.List_Id;

		}

	});
}



function logout(uid){
	  $.ajax({
	  type: 'GET',
	  url: 'http://'+slingip+':8078/OpenFireClient/Logout.jsp',
	  data: {
	  username: uid
	  }, 
	  complete: function(){
	  window.location="http://"+slingip+":8082/portal/logout.jsp";
	  }
	  });
	  }

function mailtemplatedropbox() {
/**setInterval(function()
		{ 
	
		console.log(" setInterval ");
		getMailtemplateList();
		}, 30000); **/
	getMailtemplateList();
}

function getMailtemplateList(){
	console.log("getMailtemplateList");
	var username = localStorage.getItem('remoteuser');
	
	var select = $('#mailtemp');
	$.ajax({
		url: '/portal/servlet/service/GetMailDTATemplateListSKU1?email='+username+'&group='+localStorage.getItem('groupname'), 
		type: "GET", 
		async:false,
		data:{
		},
		success: function(data) {
		//console.log("data :: "+data);
		var json = JSON.parse(data);
										//	console.log("response json : " + json);
										//	console.log("groups : " + json.MailTemplates);

											if (json.MailTemplates.length == 0) {
												json.MailTemplates.push("NA");
											}
											var htmlOptions = [];
											if (json.MailTemplates.length > 0) {
												for (i = 0; i < json.MailTemplates.length; i = i + 1) {
													console.log("groups : " + json.MailTemplates[i].templatename);
													if(i == 0){
													let	html = '<option value="Select Mail Template">Select Mail Template</option>';
											              htmlOptions[htmlOptions.length] = html;
													}
													let	html = '<option value="' + json.MailTemplates[i].templatename + '">' + json.MailTemplates[i].templatename + '</option>';
											         htmlOptions[htmlOptions.length] = html;
												
													
													
												}
												select.empty().append( htmlOptions.join('') );
												
												//json.MailTemplates.push("Select Mail Template");
												/*var newDiv = document
														.getElementById("mailtemplatelist");
												var selectHTML = "";
												selectHTML = '<select class="custom-btn form-control"  id="mailtemp">';
												for (i = 0; i < json.MailTemplates.length; i = i + 1) {
													if(i == 0){
														selectHTML += "<option value='Select Mail Template'>Select Mail Template</option>";
														
													}
													selectHTML += "<option value='"
															+ json.MailTemplates[i] + "'>"
															+ json.MailTemplates[i]
															+ "</option>";
													
												}*/
						                     
												
											}
											//newDiv.innerHTML = selectHTML;
		}

		});
	
}

function GetDataSourceInfo(){
	
	console.log("getMailtemplateList next");
	
	
	console.log("funnelName1 = "+funnelName1);
	var exldatasource;

	$.ajax({
		url: '/portal/servlet/service/LeadAutoConvertMailTailApi?group='+localStorage.getItem('groupname')+'&email='+remoteuser+'&mailtemplatename=' + localStorage.getItem('SelectedTemplateName')+'&lgtype=null' , 
		type: "GET", 
		async:false,
		data:{
		},
		success: function(data) {
	//	console.log("data :: "+data);
		exldatasource = JSON.parse(data);
		
		//console.log("data :: "+json);
		/* document.getElementById("campaign-name-nxt").setAttribute("href",
		"/portal/servlet/service/Salesconverter.datasource");
		document.getElementById("campaign-name-nxt").setAttribute("target", "_blank"); */
		
		}

		});
	
	var camp_name=document.getElementById('campaign-name').value;
	console.log("funnelName1 = "+funnelName1);
	//console.log("mailtemp = "+mailtemp);
	var text = '{"remoteuser":"' + remoteuser + '", "funnelName":"'
	+ funnelName1 + '", "fromName":"' + localStorage.getItem('fromName') 
	+ '", "fromEmailAddress":"' + localStorage.getItem('fromEmailAddress') 
	+ '",  "group":"' + localStorage.getItem('groupname')  + '",  "category":"' + localStorage.getItem('SubFunnelName')  + '",  "mailtemplate":"' + localStorage.getItem('SelectedTemplateName')  + '","campaignName":"' + camp_name + '",  "DistanceBtnCampaign":"' + localStorage.getItem('DistanceBtnCampaign')  + '"}';
	
	 funnelwithcategory(text);
	 exldatasource["remoteuser"]=remoteuser;
	 exldatasource["group"]=localStorage.getItem('groupname');
	console.log("exldatasource = "+JSON.stringify(exldatasource));
	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/saveFunnel.savemailtempXLS',
		async : false,
		data : {
			data : JSON.stringify(exldatasource)
		},
		success : function(dataa) {
			 
			console.log("jsonresp= " + dataa);
			 let xlslink = dataa;
//			datasourcefile
			console.log("xlslink= " + xlslink);
			localStorage.setItem('xlslink',xlslink);
		
	      //  window.location.href="http://bizlem.io:8082/portal/servlet/service/Salesconverter.datasource";
			var a = document.getElementById('datasourcefile'); // or grab it by tagname etc
			//a.href = xlslink;
			//http://bizlem.io:8082/portal/servlet/service/Salesconverter.LACwebservice

		}
	});
	/*document.getElementById("campaign-name-nxt").setAttribute("href",
	"/portal/servlet/service/Salesconverter.datasource");
	*/
//	document.getElementById("datasourcefile").setAttribute("href",
//			xlslink);
	
	
}



$(document).ready(function(){
	$("#mailtemp").change(function () {
        let end = this.value;
        var mailtemp = document.getElementById("mailtemp").value;
        console.log("mailtemp = "+mailtemp);
        console.log("You have selected the template - " + end);
        localStorage.setItem('SelectedTemplateName', end);
    });
    $(".custom-control-input").click(function(){
      var radioValue = $("input[name='type_radio']:checked").val();
        if(radioValue == 0){
          $('.check-radio-op-one').css('display','block');
        }
        else{
          $('.check-radio-op-one').css('display','none');
        }
    });
});


function funnelwithcategory(jsdata){
	
	console.log("jsdata = "+JSON.stringify(jsdata));
	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/saveFunnel.funnel',
		async : false,
		data : {
			data : jsdata
		},
		success : function(dataa) {
			// subFunnelJsonObj = JSON.parse(dataa);
			 
			console.log("jsonresp= " + dataa);
//			var jsonresp = JSON.parse(dataa);
//			console.log("jsonresp= " + jsonresp);
			/*if (dataa == "False") {
				alert("Funnel not creted. User does not exist ");
			} else {
				localStorage.setItem('funnelCreationStatus', dataa);
				// alert("Funnel is creted sucessfully ");
				console.log("Funnel is creted sucessfully : " + dataa)

				var body = '<h3>Funnel is created successfully</h3>';

				$("#MyPopup .modal-body").html(body);
				$("#MyPopup").modal("show");
			}*/
			/*
			 * for(var i=0;i<subFunnelJsonArray.length;i++){
			 * funnel_thead=funnel_thead+"<th><a href='<%=request.getContextPath()%>/servlet/service/CampaignReportApi.campaignView?subfunnel_name="+subFunnelJsonArray[i]+"&funnel_name="+funnelId+"'
			 * target='_blank'>"+subFunnelJsonArray[i]+"</th>"; }
			 */

		}
	});
	
	
	
}

/*$('.download-csv').click(function getcalclink() {
	console.log("link dwl= " + xlslink);
	var a = document.getElementById('datasourcefile'); // or grab it by tagname etc
	a.href = xlslink;

});
$('.downloadcalexcel').click(function getcalclink() {
	console.log("calculation link dwl= " + xlslink);

	var a = document.getElementById('downldcalclink'); // or grab it by tagname etc
	a.href = xlslink;

});
*/

/*$('.download-csv').click(function(){
	
	$('.upload-csv').removeAttr("disabled");
});*/







    $('body').on('click', '.edit-btn-get-data', function() {
        var data = $('.table-section > tbody').find('tr').has('input[name="radio-get-data"]:checked').find('td:nth-child(5)').html();
        var data1 = $('.table-section > tbody').find('tr').has('input[name="radio-get-data"]:checked').find('td:nth-child(6)').html();
		  var data2 = $('.table-section > tbody').find('tr').has('input[name="radio-get-data"]:checked').find('td:nth-child(3)').html();
       var data3 = $('.table-section > tbody').find('tr').has('input[name="radio-get-data"]:checked').find('td:nth-child(4)').html();
		  console.log("Enter"+data);
		  console.log("Enter"+data1);
		  console.log("Enter"+data2);
		  console.log("Enter"+data3);
		  remoteemail= remoteemail.replace(/@/g, "_");
		  var customers = [
		        { Name: "-- Field Type --"},
              { Name: "String"},
              { Name: "Integer"},
              { Name: "Double"},
              { Name: "Boolean"}
          ];
		  update = true;
		  $.ajax({
								type : 'GET',
								url : 'https://'+slingip+':8088/apirest/wsapi/getwebserviceDet?user='+remoteemail+'&servicename='+data3+'&webserviceid='+data2
										,
										async: false,
							
								timeout: 30000,
								data : {},
								success : function(response) {
								var jsonString = JSON.stringify(response);
								//obj = JSON.parse(JSON.stringify(response));
							//var jsonString = "'"+JSON.stringify(response)+"'"; 
							jsonString= jsonString.replace(/\"{/g, "{");
							jsonString= jsonString.replace(/\}"/g, "}");
							jsonString= jsonString.replace(/\\"/g, "\"");
						    console.log("response= "+ jsonString);
						    
						    var obj = JSON.parse(jsonString);
							//alert("Response returned :: "+JSON.stringify(response));
							console.log("aa== "+JSON.stringify(obj.outputdata));
							   var jsobj=obj.outputdata;
							   var string = JSON.stringify(obj.outputdata.input);
						       var objects = JSON.parse(string);
						       var stringout = JSON.stringify(obj.outputdata.output);
							   var outstr = JSON.parse(stringout);
							  if(data == 'getdata' && data1 == 'schedule'){
								  console.log("Enter inside schedule"+jsobj.url+jsobj.username);
								  
								  document.getElementById('url').value=jsobj.url;
								  document.getElementById('username').value=jsobj.username;
								  console.log("document.getElementById.value= "+document.getElementById('url').value);
								   document.getElementById('password').value=jsobj.password;
								  document.getElementById('token').value=jsobj.token;
								  document.getElementById("nameofservice").value=jsobj.servicename;
							   var firstData = true;
							   for (var key in objects) {
						       if (objects.hasOwnProperty(key)) {
							   if(objects[key].totalinputentered !== undefined){
							   //console.log(objects[key].totalinputentered);
							   } else{
							   if(firstData){
									   firstData = false;
									 if("yes" === objects[key].primarykey){
						              $( "#firstcheckbox" ).prop( "checked", true );
						            }
									 
									  $("#input-field-one").val(objects[key].fieldname);
									  $("#input-field-second").val(objects[key].fieldlength);
						              $(customers).each(function () {
						              if (this.Name == objects[key].fieldtype) {     
						                 $("#input-field-select > [value=" + this.Name + "]").attr("selected", "true");
						                }
										
						            });
									   }else{
									  
						    var note_window = document.createElement("div");
						    note_window.setAttribute("class", "col-sm-10 col-sm-offset-2 addMore-sub");
						    note_window.style.visibility = 'visible';
						    var title = document.createElement("div");
						    title.setAttribute("class", "row");
						   var subDiv = document.createElement("div");
						   subDiv.setAttribute("class", "col-sm-1");
						   var checkbox = document.createElement('input');
						   checkbox.type = "checkbox";
						   checkbox.name = "prkey5";
						   checkbox.value = "value";
						   if("yes" === objects[key].primarykey){
						   checkbox.setAttribute("checked", "checked");
						   }
						   var label = document.createElement('i'); 
						   label.setAttribute("class", "fa fa-key");
						   subDiv.appendChild(checkbox);
						   subDiv.appendChild( document.createTextNode( '\u00A0' ) );
						   subDiv.appendChild(label);
						   var subDiv1 = document.createElement("div");
						   subDiv1.setAttribute("class", "col-sm-2");
						   var inputF = document.createElement("input");
						   inputF.setAttribute("class", "form-control");
						   inputF.setAttribute("id", "input-field-one");
						   inputF.setAttribute("type", "text");
						   inputF.setAttribute("name", "FieldName5");
						   inputF.setAttribute("placeholder", "Field Name");
						   inputF.setAttribute('value', objects[key].fieldname); 
						   subDiv1.appendChild(inputF);
						   var subDiv2 = document.createElement("div");
						   subDiv2.setAttribute("class", "col-sm-2");
						   var dropdown = document.createElement('select');
						   dropdown.setAttribute("class", "form-control sf-object bg-gray");
						   dropdown.setAttribute("id", "input-field-select");
						   dropdown.setAttribute("name", "ip_fieldtype5");
						   
						   var options_str = "";
						   $(customers).each(function () {
						   
						   if (this.Name == objects[key].fieldtype) {
						    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
						   }else{
						     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
							}
						   });
						   dropdown.innerHTML = options_str;
						   subDiv2.appendChild(dropdown);
						   var subDiv3 = document.createElement("div");
						   subDiv3.setAttribute("class", "col-sm-2");
						   var inputF1 = document.createElement("input");
						   inputF1.setAttribute("class", "form-control");
						   inputF1.setAttribute("id", "input-field-second");
						   inputF1.setAttribute("type", "text");
						   inputF1.setAttribute("name", "ipfieldlength5");
						   inputF1.setAttribute("placeholder", "Field Length");
						   inputF1.setAttribute('value', objects[key].fieldlength); 
						   subDiv3.appendChild(inputF1);
						   var subDiv4 = document.createElement("div");
						   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
						   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
						   title.appendChild(subDiv);
						   title.appendChild(subDiv1);
						   title.appendChild(subDiv2);
						   title.appendChild(subDiv3);
						   title.appendChild(subDiv4);
						   note_window.appendChild(title);
						   var navbar = document.getElementById("div1");
						   navbar.appendChild(note_window);
						      }
						    }
							}
							}
							   
							   var firstoutData = true;
							   for (var key in outstr) {
						       if (outstr.hasOwnProperty(key)) {
							   if(outstr[key].totaloutputentered !== undefined){
							  // console.log(outstr[key].totaloutputentered);
							   } else{
							   if(firstoutData){
									   firstoutData = false;
									  $("#output-field-one").val(outstr[key].fieldname);
									  $("#output-field-second").val(outstr[key].fieldlength);
						              $(customers).each(function () {
						              if (this.Name == outstr[key].fieldtype) {     
						                 $("#output-field-select > [value=" + this.Name + "]").attr("selected", "true");
						                }
										
						            });
									   }else{
									  
							var emptydivout = document.createElement("div");
							emptydivout.setAttribute("class", "col-sm-2");
						    var note_window = document.createElement("div");
						    note_window.setAttribute("class", "col-sm-10 addMore-sub opfld");
							note_window.setAttribute("id","op");
							
						    note_window.style.visibility = 'visible';
						    var title = document.createElement("div");
						    title.setAttribute("class", "row");
						   var subDiv = document.createElement("div");
						   subDiv.setAttribute("class", "col-sm-1");
						
						  // subDiv.appendChild( document.createTextNode( '\u00A0' ) );
						   
						   var subDiv1 = document.createElement("div");
						   subDiv1.setAttribute("class", "col-sm-2");
						   var inputF = document.createElement("input");
						   inputF.setAttribute("class", "form-control");
						   inputF.setAttribute("id", "output-field-one");
						   inputF.setAttribute("type", "text");
						   inputF.setAttribute("name", "FieldName6");
						   inputF.setAttribute("placeholder", "Field Name");
						   inputF.setAttribute('value', outstr[key].fieldname); 
						   subDiv1.appendChild(inputF);
						   var subDiv2 = document.createElement("div");
						   subDiv2.setAttribute("class", "col-sm-2");
						   var dropdown = document.createElement('select');
						   dropdown.setAttribute("class", "form-control sf-object bg-gray");
						   dropdown.setAttribute("id", "out-field-select");
						   dropdown.setAttribute("name", "ip_fieldtype6");
						   
						   var options_str = "";
						   $(customers).each(function () {
						   
						   if (this.Name == outstr[key].fieldtype) {
						    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
						   }else{
						     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
							}
						   });
						   dropdown.innerHTML = options_str;
						   subDiv2.appendChild(dropdown);
						   
						   var subDiv3 = document.createElement("div");
						   subDiv3.setAttribute("class", "col-sm-2");
						   var inputF1 = document.createElement("input");
						   inputF1.setAttribute("class", "form-control");
						   inputF1.setAttribute("id", "output-field-second");
						   inputF1.setAttribute("type", "text");
						   inputF1.setAttribute("name", "ipfieldlength6");
						   inputF1.setAttribute("placeholder", "Field Length");
						   inputF1.setAttribute('value', outstr[key].fieldlength); 
						   subDiv3.appendChild(inputF1);
						   var subDiv4 = document.createElement("div");
						   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
						   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
						   title.appendChild(subDiv);
						   title.appendChild(subDiv1);
						   title.appendChild(subDiv2);
						   title.appendChild(subDiv3);
						   title.appendChild(subDiv4);
						   note_window.appendChild(title);
						   
						   var navbar = document.getElementById("div2");
						   navbar.appendChild(emptydivout);
						   navbar.appendChild(note_window);
						      }
						    }
							}
							}	  
								  
								  
								  
						            $('#addGetData').trigger('click');
						            $('.get-data-schedule').trigger('click');
									
									console.log("Getdata schedule");
						          }else if(data == 'getdata' && data1 == 'endpoint'){
						        	  
						        	document.getElementById("entertoken").value=jsobj.token;
									document.getElementById("enterurl").value=jsobj.url;
									document.getElementById("nameofservice").value=jsobj.servicename;
								var firstData = true;
							   for (var key in objects) { 
						       if (objects.hasOwnProperty(key)) {
							   if(objects[key].totalinputentered !== undefined){
							   console.log(objects[key].totalinputentered);
							   } else{
							   if(firstData){
									   firstData = false;
									 if("yes" === objects[key].primarykey){
						              $( "#firstedcheckbox" ).prop( "checked", true );
						            }
									 
									  $("#input-field-one-ed").val(objects[key].fieldname);
									  $("#input-field-second-ed").val(objects[key].fieldlength);
						              $(customers).each(function () {
						              if (this.Name == objects[key].fieldtype) {     
						                 $("#input-field-select-ed > [value=" + this.Name + "]").attr("selected", "true");
						                }
										
						            });
									   }else{
						    var note_window = document.createElement("div");
						    note_window.setAttribute("class", "col-sm-10 col-sm-offset-2 addMore-sub");
						    note_window.style.visibility = 'visible';
						    var title = document.createElement("div");
						    title.setAttribute("class", "row");
						   var subDiv = document.createElement("div");
						   subDiv.setAttribute("class", "col-sm-1");
						   var checkbox = document.createElement('input');
						   checkbox.type = "checkbox";
						   checkbox.name = "prkey1";
						   checkbox.value = "value";
						   if("yes" === objects[key].primarykey){
						   checkbox.setAttribute("checked", "checked");
						   }
						   var label = document.createElement('i'); 
						   label.setAttribute("class", "fa fa-key");
						   subDiv.appendChild(checkbox);
						   subDiv.appendChild( document.createTextNode( '\u00A0' ) );
						   subDiv.appendChild(label);
						   //subDiv.setAttribute("class","sourceText fa fa-key"); 
						   //$(subDiv.sourceText).append('<i class="fa fa fa-key"></i>');
						   var subDiv1 = document.createElement("div");
						   subDiv1.setAttribute("class", "col-sm-2");
						   var inputF = document.createElement("input");
						   inputF.setAttribute("class", "form-control");
						   inputF.setAttribute("id", "input-field-one-ed");
						   inputF.setAttribute("type", "text");
						   inputF.setAttribute("name", "FieldName1");
						   inputF.setAttribute("placeholder", "Field Name");
						   inputF.setAttribute('value', objects[key].fieldname); 
						   subDiv1.appendChild(inputF);
						   var subDiv2 = document.createElement("div");
						   subDiv2.setAttribute("class", "col-sm-2");
						   var dropdown = document.createElement('select');
						   dropdown.setAttribute("class", "form-control sf-object bg-gray");
						   dropdown.setAttribute("id", "input-field-select-ed");
						   dropdown.setAttribute("name", "ip_fieldtype1");
						   var options_str = "";
						   $(customers).each(function () {
						   
						   if (this.Name == objects[key].fieldtype) {
						    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
						   }else{
						     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
							}
						   });
						   dropdown.innerHTML = options_str;
						   subDiv2.appendChild(dropdown);
						   var subDiv3 = document.createElement("div");
						   subDiv3.setAttribute("class", "col-sm-2");
						   var inputF1 = document.createElement("input");
						   inputF1.setAttribute("class", "form-control");
						   inputF1.setAttribute("id", "input-field-second-ed");
						   inputF1.setAttribute("type", "text");
						   inputF1.setAttribute("name", "ipfieldlength1");
						   inputF1.setAttribute("placeholder", "Field Length");
						   inputF1.setAttribute('value', objects[key].fieldlength); 
						   subDiv3.appendChild(inputF1);
						   var subDiv4 = document.createElement("div");
						   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
						   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
						   title.appendChild(subDiv);
						   title.appendChild(subDiv1);
						   title.appendChild(subDiv2);
						   title.appendChild(subDiv3);
						   title.appendChild(subDiv4);
						   note_window.appendChild(title);
						   var navbar = document.getElementById("div3");
						   navbar.appendChild(note_window);
						      }
						    }
							
							}
							
							}
									
									
						        	  
						            $('#addGetData').trigger('click');
						            $('.get-data-end-point-btn').trigger('click');
						          }else if(data == 'senddata' && data1 == 'configure'){
						        	
						        	  document.getElementById('enterurl3').value=jsobj.url;
						        	  document.getElementById("nameofservice3").value=jsobj.servicename;
						        	  var firstData = true;
									   for (var key in objects) {
								       if (objects.hasOwnProperty(key)) {
									   if(objects[key].totalinputentered !== undefined){
									   //console.log(objects[key].totalinputentered);
									   } else{
									   if(firstData){
											   firstData = false;
											 if("yes" === objects[key].primarykey){
								              $( "#firstcfgcheckbox" ).prop( "checked", true );
								            }
											 
											  $("#input-field-one-cfg").val(objects[key].fieldname);
											  $("#input-field-second-cfg").val(objects[key].fieldlength);
								              $(customers).each(function () {
								              if (this.Name == objects[key].fieldtype) {     
								                 $("#input-field-select-cfg > [value=" + this.Name + "]").attr("selected", "true");
								                }
												
								            });
											   }else{
											  
								    var note_window = document.createElement("div");
								    note_window.setAttribute("class", "col-sm-12 addMore-sub");
								    note_window.style.visibility = 'visible';
								    var title = document.createElement("div");
								    title.setAttribute("class", "row");
								   var subDiv = document.createElement("div");
								   subDiv.setAttribute("class", "col-sm-1");
								   var checkbox = document.createElement('input');
								   checkbox.type = "checkbox";
								   checkbox.name = "prkey3";
								   checkbox.value = "value";
								   if("yes" === objects[key].primarykey){
								   checkbox.setAttribute("checked", "checked");
								   }
								   var label = document.createElement('i'); 
								   label.setAttribute("class", "fa fa-key");
								   subDiv.appendChild(checkbox);
								   subDiv.appendChild( document.createTextNode( '\u00A0' ) );
								   subDiv.appendChild(label);
								   var subDiv1 = document.createElement("div");
								   subDiv1.setAttribute("class", "col-sm-2");
								   var inputF = document.createElement("input");
								   inputF.setAttribute("class", "form-control");
								   inputF.setAttribute("id", "input-field-one-cfg");
								   inputF.setAttribute("type", "text");
								   inputF.setAttribute("name", "FieldName3");
								   inputF.setAttribute("placeholder", "Field Name");
								   inputF.setAttribute('value', objects[key].fieldname); 
								   subDiv1.appendChild(inputF);
								   var subDiv2 = document.createElement("div");
								   subDiv2.setAttribute("class", "col-sm-2");
								   var dropdown = document.createElement('select');
								   dropdown.setAttribute("class", "form-control sf-object bg-gray");
								   dropdown.setAttribute("id", "input-field-select-cfg");
								   dropdown.setAttribute("name", "ip_fieldtype3");
								   
								   var options_str = "";
								   $(customers).each(function () {
								   
								   if (this.Name == objects[key].fieldtype) {
								    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
								   }else{
								     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
									}
								   });
								   dropdown.innerHTML = options_str;
								   subDiv2.appendChild(dropdown);
								   var subDiv3 = document.createElement("div");
								   subDiv3.setAttribute("class", "col-sm-2");
								   var inputF1 = document.createElement("input");
								   inputF1.setAttribute("class", "form-control");
								   inputF1.setAttribute("id", "input-field-second-cfg");
								   inputF1.setAttribute("type", "text");
								   inputF1.setAttribute("name", "ipfieldlength3");
								   inputF1.setAttribute("placeholder", "Field Length");
								   inputF1.setAttribute('value', objects[key].fieldlength); 
								   subDiv3.appendChild(inputF1);
								   var subDiv4 = document.createElement("div");
								   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
								   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
								   title.appendChild(subDiv);
								   title.appendChild(subDiv1);
								   title.appendChild(subDiv2);
								   title.appendChild(subDiv3);
								   title.appendChild(subDiv4);
								   note_window.appendChild(title);
								   var navbar = document.getElementById("div4");
								   navbar.appendChild(note_window);
								      }
								    }
									}
									}
		                           	   var firstoutData = true;
									   for (var key in outstr) {
								       if (outstr.hasOwnProperty(key)) {
									   if(outstr[key].totaloutputentered !== undefined){
									  // console.log(outstr[key].totaloutputentered);
									   } else{
									   if(firstoutData){
											   firstoutData = false;
											  $("#output-field-one-cfg").val(outstr[key].fieldname);
											  $("#output-field-second-cfg").val(outstr[key].fieldlength);
								              $(customers).each(function () {
								              if (this.Name == outstr[key].fieldtype) {     
								                 $("#output-field-select-cfg > [value=" + this.Name + "]").attr("selected", "true");
								                }
												
								            });
											   }else{
											  
									
								    var note_window = document.createElement("div");
								    note_window.setAttribute("class", "col-sm-12 addMore-sub");
									note_window.setAttribute("id","op");
									
								    note_window.style.visibility = 'visible';
								    var title = document.createElement("div");
								    title.setAttribute("class", "row");
								   var subDiv = document.createElement("div");
								   subDiv.setAttribute("class", "col-sm-1");
								
								  // subDiv.appendChild( document.createTextNode( '\u00A0' ) );
								   
								   var subDiv1 = document.createElement("div");
								   subDiv1.setAttribute("class", "col-sm-2");
								   var inputF = document.createElement("input");
								   inputF.setAttribute("class", "form-control");
								   inputF.setAttribute("id", "output-field-one-cfg");
								   inputF.setAttribute("type", "text");
								   inputF.setAttribute("name", "FieldName4");
								   inputF.setAttribute("placeholder", "Field Name");
								   inputF.setAttribute('value', outstr[key].fieldname); 
								   subDiv1.appendChild(inputF);
								   var subDiv2 = document.createElement("div");
								   subDiv2.setAttribute("class", "col-sm-2");
								   var dropdown = document.createElement('select');
								   dropdown.setAttribute("class", "form-control sf-object bg-gray");
								   dropdown.setAttribute("id", "out-field-select-cfg");
								   dropdown.setAttribute("name", "ip_fieldtype4");
								   
								   var options_str = "";
								   $(customers).each(function () {
								   
								   if (this.Name == outstr[key].fieldtype) {
								    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
								   }else{
								     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
									}
								   });
								   dropdown.innerHTML = options_str;
								   subDiv2.appendChild(dropdown);
								   
								   var subDiv3 = document.createElement("div");
								   subDiv3.setAttribute("class", "col-sm-2");
								   var inputF1 = document.createElement("input");
								   inputF1.setAttribute("class", "form-control");
								   inputF1.setAttribute("id", "output-field-second-cfg");
								   inputF1.setAttribute("type", "text");
								   inputF1.setAttribute("name", "ipfieldlength4");
								   inputF1.setAttribute("placeholder", "Field Length");
								   inputF1.setAttribute('value', outstr[key].fieldlength); 
								   subDiv3.appendChild(inputF1);
								   var subDiv4 = document.createElement("div");
								   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
								   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
								   title.appendChild(subDiv);
								   title.appendChild(subDiv1);
								   title.appendChild(subDiv2);
								   title.appendChild(subDiv3);
								   title.appendChild(subDiv4);
								   note_window.appendChild(title);
								   
								   var navbar = document.getElementById("div5");
								  
								   navbar.appendChild(note_window);
								      }
								    }
									}
									}
						        	
									      $('#addSendData').trigger('click');
						            $('.send-data-configure-btn').trigger('click');
						          }else if(data == 'senddata' && data1 == 'endpoint'){
						        	  
						        	   document.getElementById('entertoken2').value=jsobj.token;
									   document.getElementById('enterurl2').value=jsobj.url;
									   document.getElementById("nameofservice3").value=jsobj.servicename;
										var firstData = true;
										   for (var key in objects) {
									       if (objects.hasOwnProperty(key)) {
										   if(objects[key].totalinputentered !== undefined){
										   console.log(objects[key].totalinputentered);
										   } else{
										   if(firstData){
												   firstData = false;
												 if("yes" === objects[key].primarykey){
									              $( "#firstedcheckbox-sd" ).prop( "checked", true );
									            }
												 
												  $("#input-field-one-sd").val(objects[key].fieldname);
												  $("#input-field-second-sd").val(objects[key].fieldlength);
									              $(customers).each(function () {
									              if (this.Name == objects[key].fieldtype) {     
									                 $("#input-field-select-sd > [value=" + this.Name + "]").attr("selected", "true");
									                }
													
									            });
												   }else{
									    var note_window = document.createElement("div");
									    note_window.setAttribute("class", "col-sm-12 addMore-sub");
									    note_window.style.visibility = 'visible';
									    var title = document.createElement("div");
									    title.setAttribute("class", "row");
									   var subDiv = document.createElement("div");
									   subDiv.setAttribute("class", "col-sm-1");
									   var checkbox = document.createElement('input');
									   checkbox.type = "checkbox";
									   checkbox.name = "prkey2";
									   checkbox.value = "value";
									   if("yes" === objects[key].primarykey){
									   checkbox.setAttribute("checked", "checked");
									   }
									   var label = document.createElement('i'); 
									   label.setAttribute("class", "fa fa-key");
									   subDiv.appendChild(checkbox);
									   subDiv.appendChild( document.createTextNode( '\u00A0' ) );
									   subDiv.appendChild(label);
									   //subDiv.setAttribute("class","sourceText fa fa-key"); 
									   //$(subDiv.sourceText).append('<i class="fa fa fa-key"></i>');
									   var subDiv1 = document.createElement("div");
									   subDiv1.setAttribute("class", "col-sm-2");
									   var inputF = document.createElement("input");
									   inputF.setAttribute("class", "form-control");
									   inputF.setAttribute("id", "input-field-one-sd");
									   inputF.setAttribute("type", "text");
									   inputF.setAttribute("name", "FieldName2");
									   inputF.setAttribute("placeholder", "Field Name");
									   inputF.setAttribute('value', objects[key].fieldname); 
									   subDiv1.appendChild(inputF);
									   var subDiv2 = document.createElement("div");
									   subDiv2.setAttribute("class", "col-sm-2");
									   var dropdown = document.createElement('select');
									   dropdown.setAttribute("class", "form-control sf-object bg-gray");
									   dropdown.setAttribute("id", "input-field-select-sd");
									   dropdown.setAttribute("name", "ip_fieldtype2");
									   var options_str = "";
									   $(customers).each(function () {
									   
									   if (this.Name == objects[key].fieldtype) {
									    options_str += '<option value="' + this.Name + '" selected="selected">' + this.Name + '</option>';
									   }else{
									     options_str += '<option value="' + this.Name + '">' + this.Name + '</option>';
										}
									   });
									   dropdown.innerHTML = options_str;
									   subDiv2.appendChild(dropdown);
									   var subDiv3 = document.createElement("div");
									   subDiv3.setAttribute("class", "col-sm-2");
									   var inputF1 = document.createElement("input");
									   inputF1.setAttribute("class", "form-control");
									   inputF1.setAttribute("id", "input-field-second-sd");
									   inputF1.setAttribute("type", "text");
									   inputF1.setAttribute("name", "ipfieldlength2");
									   inputF1.setAttribute("placeholder", "Field Length");
									   inputF1.setAttribute('value', objects[key].fieldlength); 
									   subDiv3.appendChild(inputF1);
									   var subDiv4 = document.createElement("div");
									   subDiv4.setAttribute("class", "col-sm-1 text-right p-0");
									   subDiv4.innerHTML='<a class="btn btn-danger delete-btn sm-btn-custom"><i class="fa fa-trash" style="color:#fff !important;"></i></a>';
									   title.appendChild(subDiv);
									   title.appendChild(subDiv1);
									   title.appendChild(subDiv2);
									   title.appendChild(subDiv3);
									   title.appendChild(subDiv4);
									   note_window.appendChild(title);
									   var navbar = document.getElementById("div6");
									   navbar.appendChild(note_window);
									      }
									    }
										
										}
										
										}
									   
									   
						            $('#addSendData').trigger('click');
						            $('.send-data-end-point-btn').trigger('click');
						          }
							
				
								}
							});   
    }); 
	
   // getsubserviceType=$( "#myTab2 a.active").html();

    function getfunnelEditList(){
    	console.log("funnellist");
    	var username = localStorage.getItem('remoteuser');
//    	funnellabel
    	document.getElementById("funnellabel").innerHTML="Select Funnel To Edit";
    	document.getElementById("funnellist").innerHTML='<select class="custom-btn form-control"  id="funnelone"  onchange="onfunnelselect()"></select>';
    	
//    	 <select class="custom-btn form-control"  id="funnelone"></select>
    	var select = $('#funnelone');
    	$.ajax({
    		url: '/portal/servlet/service/saveFunnel.funnellist?email='+username, 
    		type: "GET", 
    		async:false,
    		data:{
    		},
    		success: function(data) {
    		//console.log("data :: "+data);
    		var json = JSON.parse(data);
    											console.log("response json : " + json);
    										//	console.log("groups : " + json.MailTemplates);

    											if (json.length == 0) {
    												json.push("NA");
    											}
    											var htmlOptions = [];
    											if (json.length > 0) {
    												for (i = 0; i < json.length; i = i + 1) {
    												//	console.log("groups : " + json[i]);
    													if(i == 0){
    													let	html = '<option value="Select Funnel">Select Funnel </option>';
    											              htmlOptions[htmlOptions.length] = html;
    													}
    													let	html = '<option value="' + json[i]+ '">' + json[i] + '</option>';
    											         htmlOptions[htmlOptions.length] = html;
    												
    													
    													
    												}
    												select.empty().append( htmlOptions.join('') );
    												
    										
    						                     
    												
    											}
    										
    		}

    		});
    	
    }
    

    function editFunnelData() {
    	console.log("funnel list ");
    	getfunnelEditList();
    	
     	var username = localStorage.getItem('remoteuser');
    	var funnel=document.getElementById("funnelone").value;
    	
    	
    	
    	
    
    }

    function onfunnelselect(){
    	
    	var username = localStorage.getItem('remoteuser');
    	var funnel=document.getElementById("funnelone").value;
    	$.ajax({
    		url: '/portal/servlet/service/saveFunnel.Editfunnel?email='+username+'&funnel='+funnel, 
    		type: "GET", 
    		async:false,
    		data:{
    		},
    		success: function(data) {
    		//console.log("data :: "+data);
    		var json = JSON.parse(data);
    		console.log("response json : " + json);
    		
    		document.getElementById("funnel-name").value=funnel;
    		document.getElementById("from-name").value=json.FromName;
       	    document.getElementById("from-email-address").value=json.Fromemail;
       	 
       							
    										
    		}

    		});
    	
    	
    	//edit all data
    	$.ajax({
    		url: '/portal/servlet/service/saveFunnel.EditFunnelAlldata?email='+username+'&funnel='+funnel, 
    		type: "GET", 
    		async:false,
    		data:{
    		},
    		success: function(data) {
    		console.log("data :: "+data);
    		var campjson_obj = JSON.parse(data);

    		console.log("response json : " + campjson_obj);
    		if(campjson_obj.Contacts){
    		editcontactsobj=campjson_obj["Contacts"];
    		console.log("response editcontactsobj : " + editcontactsobj);
    		Editdisplaycontacts();	
    		}
    		//editcontactsobj=[{"Email":"mohit.raj@bizlem.io","FirstName":"mohit","LastName":"mohit","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":100,"Industry":"Software","Source":"Friend"},{"Email":"tejal.jabade10@gmail.com","FirstName":"tejal","LastName":"vivek","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"tejal.bizlem@gmail.com","FirstName":"tejal","LastName":"jabade","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"mohitraj.ranu@gmail.com","FirstName":"mohit","LastName":"mohit","PhoneNumber":"+971 (4) 3914901","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"vivek@bizlem.io","FirstName":"vivek","LastName":"vivek","PhoneNumber":"+971 (4) 3914902","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"}];
    	
    		
     	//   var funnel_details_json_obj = JSON.parse(data);
    		if(campjson_obj.Explore){
		   var ExploreJsonArr=campjson_obj["Explore"];
    		
//		   var ExploreJsonArr=[{"Category":"Explore","CreatedBy":"salesautoconvertuser1@gmail.com","funnelName":"edittest4","group":"G1","rawleads":5,"Contacts":[{"Email":"mohit.raj@bizlem.io","FirstName":"mohit","LastName":"mohit","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":100,"Industry":"Software","Source":"Friend"},{"Email":"tejal.jabade10@gmail.com","FirstName":"tejal","LastName":"vivek","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"tejal.bizlem@gmail.com","FirstName":"tejal","LastName":"jabade","PhoneNumber":"+971 (4) 3914900","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"mohitraj.ranu@gmail.com","FirstName":"mohit","LastName":"mohit","PhoneNumber":"+971 (4) 3914901","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"},{"Email":"vivek@bizlem.io","FirstName":"vivek","LastName":"vivek","PhoneNumber":"+971 (4) 3914902","Country":"UAE ","CompanyName":"3i infotech","CompanyHeadCount":12,"Industry":"Software","Source":"Friend"}],"FromName":"ug","campaignName":"Test camp1","FromEmailAddress":"ui","Campaign_id":"SalesTestLead","Created_date":"04-12-19 16:47:08","week":"1 Week","scheduleday":[],"scheduleTime":"","updateflag":"1","leadMailIdCount":"","subFunnelCampCount":"0","Parentfunnel":"edittest4","expleadweek":"week2","Datasource":[{"SessionCount":0,"Recent_SessionCount":0,"AvgSesionDuration":0}]},{"Category":"Explore","CreatedBy":"salesautoconvertuser1@gmail.com","funnelName":"edittest4_EC_2","FromName":"ug","campaignName":"Test camp2","FromEmailAddress":"ui","Campaign_id":"SalesconvertURLTest","Created_date":"04-12-19 16:47:45","week":"1 Week","group":"G1","scheduleday":[],"scheduleTime":"","updateflag":"1","leadMailIdCount":"","subFunnelCampCount":"0","Parentfunnel":"edittest4","ScheduleGapDate":"07-12-19 ","multipleflag":"5","Datasource":[{"SessionCount":0,"Recent_SessionCount":0,"AvgSesionDuration":0}]}];
		   console.log("ExploreJsonAr1r= "+ExploreJsonArr);
		  // var ExploreJsonArr=funnel_details_json_obj["Explore"];
		   var subcpy="";
		  for (var i = 1; i < ExploreJsonArr.length; i++) {
			   console.log("copy i= "+i);
			    spfor=spfor+1;
		   subcpy=subcpy+'<div class="col-md-12 addMore-sub"><div class="row"><div class="col-md-9 set-up-btn-section pr-0"><button type="button" class="btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block">Set Up EC <span>'+spfor+'</span></button></div><div class="col-md-3 pl-0"><button type="button" class="btn btn-danger apend-btn copy-remove-btn plus-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button></div></div></div>';
		  // console.log("copy = "+subcpy);
		  }
		   document.getElementById("explore_row_add_more_sub_copy").innerHTML  =subcpy;   

    		}
		   
    		//entice
    		
    		if(campjson_obj.Entice){
    			   var EnticeJsonArr=campjson_obj["Entice"];
    	   	       console.log("EnticeJsonAr1r= "+EnticeJsonArr);
    		       var subcpy="";
    			  for (var i = 1; i < EnticeJsonArr.length; i++) {
    				   console.log("copy i= "+i);
    				    spforent=spforent+1;
    			   subcpy=subcpy+'<div class="col-md-12 addMore-sub"><div class="row"><div class="col-md-9 set-up-btn-section pr-0"><button type="button" class="btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block">Set Up EnC <span>'+spforent+'</span></button></div><div class="col-md-3 pl-0"><button type="button" class="btn btn-danger apend-btn copy-remove-btn plus-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button></div></div></div>';
    			
    			  }
    			   document.getElementById("entice_row_add_more_sub_copy").innerHTML  =subcpy;   

    	    		}
    		
    		///var spforinf=1;  var spforwarm=1; var spforconnect=1;
    		// { "Explore", "Warm", "Inform", "Entice", "Connect" };
    		
    		if(campjson_obj.Inform){
 			   var InformJsonArr=campjson_obj["Inform"];
 	   	       console.log("Inform= "+InformJsonArr);
 		       var subcpy="";
 			  for (var i = 1; i < InformJsonArr.length; i++) {
 				   console.log("copy i= "+i);
 				  spforinf=spforinf+1;
 			   subcpy=subcpy+'<div class="col-md-12 addMore-sub"><div class="row"><div class="col-md-9 set-up-btn-section pr-0"><button type="button" class="btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block">Set Up IC <span>'+spforinf+'</span></button></div><div class="col-md-3 pl-0"><button type="button" class="btn btn-danger apend-btn copy-remove-btn plus-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button></div></div></div>';
 			
 			  }
 			   document.getElementById("inform_row_add_more_sub_copy").innerHTML  =subcpy;   

 	    		}
    		if(campjson_obj.Warm){
  			   var WarmJsonArr=campjson_obj["Warm"];
  	   	       console.log("Warm= "+WarmJsonArr);
  		       var subcpy="";
  			  for (var i = 1; i < WarmJsonArr.length; i++) {
  				   console.log("copy i= "+i);
  				 spforwarm=spforwarm+1;
  			   subcpy=subcpy+'<div class="col-md-12 addMore-sub"><div class="row"><div class="col-md-9 set-up-btn-section pr-0"><button type="button" class="btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block">Set Up WC <span>'+spforwarm+'</span></button></div><div class="col-md-3 pl-0"><button type="button" class="btn btn-danger apend-btn copy-remove-btn plus-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button></div></div></div>';
  			
  			  }
  			   document.getElementById("warm_row_add_more_sub_copy").innerHTML  =subcpy;   

  	    		}
    		
    		if(campjson_obj.Connect){
   			   var ConnectJsonArr=campjson_obj["Connect"];
   	   	       console.log("Connct= "+ConnectJsonArr);
   		       var subcpy="";
   			  for (var i = 1; i < ConnectJsonArr.length; i++) {
   				   console.log("copy i= "+i);
   				spforconnect=spforconnect+1;
   			   subcpy=subcpy+'<div class="col-md-12 addMore-sub"><div class="row"><div class="col-md-9 set-up-btn-section pr-0"><button type="button" class="btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block">Set Up CC <span>'+spforconnect+'</span></button></div><div class="col-md-3 pl-0"><button type="button" class="btn btn-danger apend-btn copy-remove-btn plus-btn"><i class="fa fa-trash-o" aria-hidden="true"></i></button></div></div></div>';
   			
   			  }
   			   document.getElementById("connect_row_add_more_sub_copy").innerHTML  =subcpy;   

   	    		}
    		
    		
    										
    		}

    		});	
    	
    	
    	
    	
    
    	
    }
    

    function CreatestaticDatasource() {
    	
    	console.log("remoteuser : " + remoteuser);
    	/* {"email":"nilesh@gmail.com","group":"G1","lgtype":"null","SFEmail":"nilesh@gmail.com","datasourcename":"sivadatasource","datasource":"form","saveType":"new","primarykey":"sivafield1","selectedfields":["sivafield1","readdyfield2"]} */
    	
    	var datasrcname="";
    	if (document.getElementById("datasrcname")
				&& document.getElementById("datasrcname").value) {
    	datasrcname=document.getElementById("datasrcname").value;
    	}
    	if(datasrcname ===""){
    		datasrcname=localStorage.getItem('funnelName')+"DS";
    	}
    	
    	var mainjs={};
    	mainjs["email"]=remoteuser;
    	mainjs["group"]=localStorage.getItem('groupname');
    	mainjs["lgtype"]="null";
    	mainjs["SFEmail"]=remoteuser;
    	mainjs["datasourcename"]=datasrcname;
    	mainjs["datasource"]="form";
    	mainjs["saveType"]="new";
    	mainjs["primarykey"]="Email";
    	var dataFields=[];
    	dataFields.push("Email");
    	dataFields.push("FirstName");
    	dataFields.push("LastName");
    	dataFields.push("PhoneNumber");
    	dataFields.push("Country");
    	dataFields.push("CompanyName");
    	dataFields.push("CompanyHeadCount");
    	dataFields.push("Industry");
    	dataFields.push("Institute");
    	dataFields.push("Source");
    	dataFields.push("SessionCount");
    	dataFields.push("Recent_SessionCount");
    	dataFields.push("AvgSesionDuration");
    	dataFields.push("Url_Open");
    	dataFields.push("Url_Click");
    	dataFields.push("URL");
    	dataFields.push("dateHourMinute");
    	
    	//var urlClickedJsonArray = [{"field":"CreatedBy","type":"String"},{"field":"FunnelName","type":"String"},{"field":"SubFunnelName","type":"String"},{"field":"Category","type":"String"},{"field":"SubscriberEmail","type":"String"},{"field":"SourceMedium","type":"String"},{"field":"Source","type":"String"},{"field":"ListId","type":"Integer"},{"field":"CampaignId","type":"Integer"},{"field":"SubscriberId","type":"Integer"},{"field":"GAUser","type":"String"},{"field":"SessionCount","type":"Integer"},{"field":"MostRecent_SessionCount","type":"Integer"},{"field":"Recent_SessionCount","type":"Integer"},{"field":"AvgSesionDuration","type":"Integer"},{"field":"MostRecent_AvgSesionDuration","type":"Integer"},{"field":"Recent_AvgSesionDuration","type":"Integer"},{"field":"URL","type":"Url"},{"field":"Recent_Own_Visits","type":"Integer"},{"field":"Only_Own_Visits","type":"Integer"},{"field":"Url_Open","type":"Integer"},{"field":"Url_Click","type":"Integer"}];
    	
    	mainjs["selectedfields"]=dataFields;
    	console.log("mainjs = "+JSON.stringify(mainjs));
    	
    	$
    			.ajax({
    				type : 'POST',
    				url : '/portal/servlet/service/savedatasourceSKUONE.Datasource',
    				dataType:'json',
    				contentType:'application/json;',
    				data : JSON.stringify(mainjs),
    				
    				success : function(dataa) {
    					console.log(dataa);
//    					var jsonData = JSON.parse(dataa);
    				//	console.log(jsonData);
    				}
    			});
    }


    
    function Editdisplaycontacts() {
    	
    	
    	document.getElementById("diventirelist").innerHTML='<label class="containers">Entire List<input type="radio" name="radio-list" id="list1" value="list1" checked="checked"><span class="checkmark"></span></label>';
    	document.getElementById("divpartoflist").innerHTML='<label class="containers">Part Of List <input type="radio" name="radio-list" id="list2" value="list2"><span class="checkmark"></span></label>';
    	    

    	document.getElementById("divforlisttable").innerHTML='<div class="to-from-list-part-section"><div class="row"><div class="col-md-12 entire-list-section"><table class="table table-bordered" id="list-table" style="height: 170px; overflow-y: scroll; border:0px; display: block;"><tbody id="csv_leads_list" style="display: table; width: 100%;"></tbody></table></div></div></div>';


    	var list_type = $("input[name='radio-list']:checked").val();
    		/*if(json_object !=null){
    		 if(json_object.length>0){
    			 console.log("json_object new = "+json_object+" :: json_object.length = "+json_object.length);
    			 var obj = JSON.parse(json_object);
    		 }}*/
    	//if(editcontactsobj.length>0){
//    		 var obj = JSON.parse(editcontactsobj);
//    		 editcontactsobj=null;
    	//}
    	 	
    		  for(var i=0;i<editcontactsobj.length;i++){ 
    			  var Email="";
    			     var First_Name="";
    			     var Last_Name="";
    			     var Phone_Number="";
    			     var Address="";
    			     var Company_Name="";
    			     var Company_HeadCount="";
    			     var Industry="";
    			     var Institute="";
    			     var Source="";
    		      var lead_info=editcontactsobj[i];
    		      console.log("lead_info= "+lead_info);
    		    
    	 if (lead_info.Email){
    		 Email=  lead_info.Email;  	  
    		      } 
    	 if (lead_info.FirstName){
    		 First_Name  =lead_info.FirstName ;
    	 } 
    	 if (lead_info.LastName){
    		 Last_Name  =lead_info.LastName ;
    	 } 
    	 if (lead_info.PhoneNumber){
    		 Phone_Number  =lead_info.PhoneNumber ;
    		  
    	 } 
    	 if (lead_info.Country){
    		 Address=  lead_info.Country; 
    	 } 
    	 if (lead_info.CompanyName){
    		 Company_Name=  lead_info.CompanyName;  
    	 } 
    	 if (lead_info.CompanyHeadCount){
    		 Company_HeadCount=  lead_info.CompanyHeadCount;
    	 } 
    	 if (lead_info.Industry){
    		 Industry=  lead_info.Industry;
    	 } 
    	 if (lead_info.Institute){
    		 Institute=  lead_info.Institute;
    	 } 
    	 if (lead_info.Source){
    		 Source=  lead_info.Source;
    	 } 

    		entire_list_tbody = entire_list_tbody
    				+ "<tr  id='csv_entire_list_leadtr"
    				+ (i + 1)
    				+ "' name='csv_entire_list_leadtr'><td>"
    				+ (i + 1) + "</td><td>"
    				+ Email + "</td><td>"
    				+ First_Name + "</td><td>"
    				+ Last_Name + "</td><td>"
    				+ Phone_Number + "</td><td>"
    				+ Address + "</td><td>"
    				+ Company_Name + "</td><td>"
    				+ Company_HeadCount
    				+ "</td><td>" + Industry
    				+ "</td><td>" + Institute
    				+ "</td><td>" + Source
    				+ "</td></tr>";
    			  } 
    		 
    			  console.log("list_type = "+list_type);
    			  
    			  if (list_type == 'list1') {
    				  console.log("list_type = "+list_type);
    					document.getElementById("csv_leads_list").innerHTML = entire_list_tbody;
    					// getSubscribersList();
    				} 
    		
    		     
    		    	
    	}
    
    //move email
    
    $(document).ready(function() {
    	$("#movebutton").click(function() {

    		//alert(subf);
    		var username = localStorage.getItem('remoteuser');
    		var subinsidemove = $("#subinsidemove").find(':selected').val();
    		console.log(subinsidemove);
    		var insidemove = $("#insidemove").find(':selected').val();
    		console.log(insidemove);
    		var campid = document.getElementById("campid").value;
    		var subid = document.getElementById("subid").value;
    		var subemail = document.getElementById("subemail").value;
    		console.log(subemail);
    		var movejs = {};
    		movejs["FunnelName"] = insidemove;
    		movejs["CampaignId"] = campid;
    		movejs["CreatedBy"] = username;
    		movejs["SubscriberId"] = subid;
    		movejs["Output"] = subinsidemove;
    		movejs["SubscriberEmail"] = subemail;
    		console.log("movejs= " + JSON.stringify(movejs));

    		$.ajax({
    			url : "/portal/servlet/service/Moniteringui.MoveSubfunnel",
    			// dataType: 'json',
    			// contentType: 'application/json',
    			type : "POST",
    			data : {

    				username : username,
    				campid : campid,
    				subid : subid,
    				insidemainfunnel : insidemove,
    				subinsidemove : subinsidemove,
    				subemail : subemail

    			},
    			success : function(data) {

    				console.log(data);
    				$("#myModal").modal("hide");

    				var body = '<h4>Lead Moved Successfully</h4>';

    				$("#movepop .modal-body").html(body);
    				$("#movepop").modal("show");
    				console.log("move popup");
    				//alert("Moved Successfully");
    			}

    		});

    	});
    });

    $(function() {

    	$("#btnClosemovePopup").click(function() {
    		$("#movepop").modal("hide");
    		location.reload();
    	});
    });
    
    