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
					url:'optionGroupAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 110,
					habBtnDelete: true,
					width: 350,
					title: '<bean:message key="optionGroup.header" />',
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
	      	        		id:'grpId',
	      	        		name:'grpId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="optionGroup.grpDesc"/>',
	      	                id: 'grpDesc',
	      	                name: 'grpDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="optionGroup.grpDesc.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="optionGroup.grpIconCSS"/>',
	      	                id: 'grpIconCSS',
	      	                name: 'grpIconCSS',
	      	              	maxLength: 50,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:true
	      	            },{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="optionGroup.grpOrder"/>',
							id: 'grpOrder',
							name: 'grpOrder',
							width: 170,
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="optionGroup.grpOrder.required" />',
							hideTrigger: false,
							keyNavEnabled: true,
							mouseWheelEnabled: false,
							allowDecimals: false
						},{
	      	                fieldLabel: '<bean:message key="optionGroup.grpMsgProperty"/>',
	      	                id: 'grpMsgProperty',
	      	                name: 'grpMsgProperty',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:true
	      	            }]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'grpDesc', type: 'string', mapping:'grpdesc'},
			            {name: 'grpIconCSS', type: 'string', mapping:'grpiconcss'},
			            {name: 'grpOrder', type: 'number', mapping:'grporder'},
			            {name: 'grpMsgProperty', type: 'string', mapping:'grpmsgproperty'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'grpId', type: 'number', mapping:'grpid'}
			        ],
			        idProperty: 'grpId'
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
			            url: 'optionGroupAction.do?accion=load',
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
			            property: 'grpOrder',
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
		                dataIndex: 'grpId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'grpDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'grpIconCSS'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 600,
			        height: 300,
			        title: '<bean:message key="optionGroup.header" />',
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
			        	dataIndex: 'grpId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="optionGroup.grpOrder" />',
			            dataIndex: 'grpOrder',
			            width: 40,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionGroup.grpDesc" />',
			            dataIndex: 'grpDesc',
			            width: 100,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="optionGroup.grpIconCSS" />',
			            dataIndex: 'grpIconCSS',
			            width: 200,
			            flex:1,
			            sortable: true,
			            renderer: function(value) {
			                return Ext.String.format('<div class="{0}" style="height:17px; padding-left:20px">{1}</div>', value, value);
			            }
			        },{
			            text: '<bean:message key="optionGroup.grpMsgProperty" />',
			            dataIndex: 'grpMsgProperty',
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
		<bean:message key="optionGroup.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="optionGroup.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>