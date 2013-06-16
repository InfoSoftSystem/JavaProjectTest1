<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.promefrut.simefrut.struts.administration.forms.UserForm"%>
<%@ page import="java.util.ResourceBundle,org.apache.struts.Globals,java.util.Locale"%>
<%@ page import="org.promefrut.simefrut.utils.LookUpResourceSchema"%>
<%@ page import="java.util.ArrayList, java.util.List, java.util.Map, java.util.Iterator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String contextPath = request.getContextPath();
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ext-all-gray-4.1.0-b3.css" rel="stylesheet" type="text/css">
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/ItemSelector.css" rel="stylesheet" type="text/css" />
				
		<!--<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-debug-w-comments 4.1.0-b3.js"></script>
		<script type="text/javascript" lang="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-debug-w-comments 4.0.7.js"></script>-->
		
		<!--  -->
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-4.1.0-b3.js"></script>
		
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/form/MultiSelect.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/form/ItemSelector.js"></script>
		
		
		
		<script>
	
			Ext.onReady(function(){
				var ctrMapping = 'ctrdescspa';
				
				Ext.define('modelCtrSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrSk', type: 'string', mapping: 'ctrsk'},
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
				
				var storeCountries = Ext.create('Ext.data.Store', {
			        id: 'storeCountries',
			        //pageSize: 50,
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'queryHarvestAreaAction.do?accion=loadCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }//,
			            // sends single sort as multi parameter
			            //simpleSortMode: true
			        },
			        autoLoad: false
			    });
				
				Ext.widget('itemselector', {
					width: 300,
		            height: 300,
					name: 'countries',
		            id: 'countries',
		            renderTo:'itemselector',
		            hideLabel: true,
		            blankText: '<bean:message key="queryHarvestArea.countries.selected.required"/>',
		            buttons:["add", "remove"],
		            store: storeCountries,
		            displayField: 'ctrDesc',
		            valueField: 'ctrId',
		            allowBlank: false/*,
		            listeners:{
		            	render:function(){
							this.store.on('load', function(store) {
					            this.bindStore(store);
					        }, this);
							
							//alert('done');
		            	}
		            }*/
                });
				
				
				var tmpStore = storeCountries;//Ext.getStore('storeCountries');
        		
        		tmpStore.proxy.extraParams={
	                initialDate: 2001,
	                finalDate: 2011
	            };

				//var m = wait();
				new Ext.util.DelayedTask().delay(1000,function(){
					tmpStore.load({
						scope:this,
						callback:function(records, operation, success){
							//m.close();
							
							if(success){
								Ext.getCmp('countries').fromField.bindStore(storeCountries);
							}
						}
					});
				});
			});
		</script>
	</head>
	<body>
		
		<div id="itemselector"></div>
		<div id="multiselect"></div>
	</body>
</html>