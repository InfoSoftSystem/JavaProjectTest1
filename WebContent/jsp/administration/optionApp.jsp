<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.promefrut.simefrut.struts.administration.forms.UserForm"%>

<tiles:insert name="plantillaBase" definition="plantillaBase">
	<tiles:put name="base">
		<html:base />
	</tiles:put>
	
	<tiles:put name="help" type="String">
	</tiles:put>
	
	<tiles:put name="scripts" type="String">
		<script>
			
			/**
				This funtion is used to fire the events for positioning the components. centerNow function is used on the properties
				onMenuCollapse and onMenuExpand of BaseViewer.BaseView. This function must be in every page if the positioning is necesary
			*/
			function centerNow(){
				Ext.getCmp('formulario').fireEvent('position',{"this":Ext.getCmp('formulario'), eOpts: {}});
				Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
			}
			
			
			
			Ext.onReady(function(){
				//Property to disable the country field if the user is not an Administrator	
				<%
		        	UserForm user = (UserForm) session.getAttribute("user");
		        %>
				
		        //Property used to choose the country column according to current language 
		        //var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        
				
				Ext.define('modelGrpId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'grpId', type: 'numeric', mapping: 'grpid'},
				        {name: 'grpDesc', type: 'string', mapping: 'grpdesc'},
				        {name: 'grpDescMsg', type: 'string', mapping: 'grpdescmsg'}
				    ],
				    idProperty: 'grpId'
				});
		        
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'optionAppAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 100,
					habBtnDelete: true,
					width: 550,
					title: '<bean:message key="optionApp.header" />',
					cancelFn: function(){
						Ext.getStore('dataStore').load(); 
						return true;
						},
					items: [{
      	        		xtype:'hidden',
      	        		id:'optId',
      	        		name:'optId'
      	        	},{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            //collapsed: false,
	      	            layout: {
	      	            	type:'table',
	      	            	columns: 2
	      	            },
	      	          	defaults: {
	      	                //anchor: '100%'
	      	            	width:250, 
	      	            	height: 25,
	      	            	labelAlign:'right',
	      	            	margin: '0 0 0 0'
	      	            },
	      	            /*defaults: {
	      	                anchor: '100%'
	      	            },*/
	      	            items :[
						{
	      	                fieldLabel: '<bean:message key="optionApp.optDesc"/>',
	      	                id: 'optDesc',
	      	                name: 'optDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="optionApp.optDesc.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="optionApp.optVisible"/>',
	      	                id:'optVisible',
	      	                name: 'optVisible',
	      	                allowBlank:false,
	      	              	value: 'Y',
	      	              	width: 160,
					        store: 	Ext.create('Ext.data.Store',{
					        	fields: ['name','value'],
					        	data: [
					        		{'name':'<bean:message key="global.yes" />', 'value':'Y'},
					        		{'name':'<bean:message key="global.no" />', 'value':'N'}
					        	]
					        }),
					        queryMode: 'local',
					        displayField: 'name',
					        valueField: 'value',
					        editable: false
	      	            },{
	      	                fieldLabel: '<bean:message key="optionApp.optUrl"/>',
	      	                id: 'optUrl',
	      	                name: 'optUrl',
	      	                colspan:2,	
	      	              	width: 500,
	      	              	allowBlank:false,
	      	            	blankText: '<bean:message key="optionApp.optUrl.required" />'
	      	            },{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="optionApp.optOrder"/>',
							id: 'optOrder',
							name: 'optOrder',
							width: 170,
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="optionApp.optOrder.required" />',
							hideTrigger: false,
							keyNavEnabled: true,
							mouseWheelEnabled: false,
							allowDecimals: false
						},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="optionGroup.grpId"/>',
	      	                id:'grpId',
	      	              	name:'grpId',
	      	                allowBlank:false,
	      	                store: Ext.create('Ext.data.Store',{
					        	model: 'modelGrpId',
					        	data: {registers: <%=request.getAttribute("optionGroupsCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'grpDescMsg',
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'grpDescMsg',
					        valueField: 'grpId',
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        blankText: '<bean:message key="optionApp.grpId.required"/>'
	      	            },{
	      	                fieldLabel: '<bean:message key="optionApp.optIconCSS"/>',
	      	                id: 'optIconCSS',
	      	                name: 'optIconCSS',
	      	              	maxLength: 50,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:true
	      	            },{
	      	                fieldLabel: '<bean:message key="optionApp.optMsgProperty"/>',
	      	                id: 'optMsgProperty',
	      	                name: 'optMsgProperty',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:true
	      	            }]//Items formfield
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'optId', type: 'number', mapping:'optid'},
			            {name: 'optDesc', type: 'string', mapping:'optdesc'},
			            {name: 'grpDesc', type: 'string', mapping:'grpdesc'},
			            {name: 'grpDescMsg', type: 'string', mapping:'grpdescmsg'},
			            {name: 'optUrl', type: 'string', mapping:'opturl'},
			            {name: 'optVisible', type: 'string', mapping:'optvisible'},
			            {name: 'optIconCSS', type: 'string', mapping:'opticoncss'},
			            {name: 'optOrder', type: 'number', mapping:'optorder'},
			            {name: 'optMsgProperty', type: 'string', mapping:'optmsgproperty'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'grpId', type: 'number', mapping:'grpid'}
			        ],
			        idProperty: 'optId'
			    });
				
								
				// create the Data Store
			    var store = Ext.create('Ext.data.Store', {
			        id: 'dataStore',
			        //pageSize: 50,
			        model: 'GridModel',
			        groupField: 'grpDescMsg',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'optionAppAction.do?accion=load',
			            extraParams: {
			                total: 50000
			            },
			            reader: {
			            	type: 'json',
			                root: 'registers',//'topics',
			                totalProperty: 'totalCount'
			            },
			            // sends single sort as multi parameter
			            simpleSortMode: true
			        },
			        sorters: [{
			            property: 'grpId',
			            direction: 'ASC'
			        },{
			            property: 'optOrder',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    	
			    var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			        groupHeaderTpl: '<bean:message key="optionGroup.grpId" />: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
			        hideGroupedHeader: true
			    });
			    
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'optId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'optDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'optIconCSS'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 600,
			        height: 300,
			        title: '<bean:message key="optionApp.header" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters, groupingFeature],
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'optId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="optionGroup.grpDesc" />',
			            dataIndex: 'grpDescMsg',
			            width: 100,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionApp.optOrder" />',
			            dataIndex: 'optOrder',
			            width: 40,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionApp.optDesc" />',
			            dataIndex: 'optDesc',
			            width: 100,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionApp.optUrl" />',
			            dataIndex: 'optUrl',
			            width: 200,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionApp.optVisible" />',
			            dataIndex: 'optVisible',
			            width: 40,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionApp.optIconCSS" />',
			            dataIndex: 'optIconCSS',
			            width: 200,
			            flex:1,
			            sortable: true,
			            renderer: function(value) {
			                return Ext.String.format('<div class="{0}" style="height:17px; padding-left:20px">{1}</div>', value, value);
			            }
			        },{
			            text: '<bean:message key="optionApp.optMsgProperty" />',
			            dataIndex: 'optMsgProperty',
			            width: 200,
			            flex:1,
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="optionApp.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="optionApp.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>