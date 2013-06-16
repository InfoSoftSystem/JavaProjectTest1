<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

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
				
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'qualityAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 120,
					title: '<bean:message key="quality.header" />',
					cancelFn: function(){Ext.getStore('dataStore').load(); return true;},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            //collapsed: false,
	      	            layout: 'anchor',
	      	            defaults: {
	      	                anchor: '100%'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'qualityId',
	      	        		name:'qualityId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="quality.qualityCode"/>',
	      	                id: 'qualityCode',
	      	                name: 'qualityCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="quality.qualityCode.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="quality.qualityDesc"/>',
	      	                id: 'qualityDesc',
	      	                name: 'qualityDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="quality.qualityDesc.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="quality.qualityStatus"/>',
	      	                id:'qualityStatus',
	      	                name: 'qualityStatus',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	value: 'A',
	      	              	//width: 200,
					        store: 	Ext.create('Ext.data.Store',{
					        	fields: ['name','value'],
					        	data: [
					        		{'name':'<bean:message key="registro.active" />', 'value':'A'},
					        		{'name':'<bean:message key="registro.inactive" />', 'value':'I'}
					        	]
					        }),
					        queryMode: 'local',
					        displayField: 'name',
					        valueField: 'value',
					        editable: false
	      	            }]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'qualityCode', type: 'string', mapping:'qualitycode'},
			            {name: 'qualityDesc', type: 'string', mapping:'qualitydesc'},
			            {name: 'qualityStatus', type: 'string', mapping:'qualitystatus'},
			            {name: 'qualityStatusText', type: 'string', mapping:'qualitystatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'qualityId', type: 'number', mapping:'qualityid'}
			        ],
			        idProperty: 'qualityId'
			    });
				
								
				// create the Data Store
			    var store = Ext.create('Ext.data.Store', {
			        id: 'dataStore',
			        //pageSize: 50,
			        model: 'GridModel',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'qualityAction.do?accion=load',
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
			            property: 'qualityId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'qualityId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'qualityCode'
		            }, {
		                type: 'string',
		                dataIndex: 'qualityDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'qualityStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 370,
			        height: 300,
			        title: '<bean:message key="quality.header" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'qualityId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="quality.qualityCode" />',
			            dataIndex: 'qualityCode',
			            width: 50,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="quality.qualityDesc" />',
			            dataIndex: 'qualityDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="quality.qualityStatus" />',
			            dataIndex: 'qualityStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
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
		<bean:message key="quality.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="quality.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>