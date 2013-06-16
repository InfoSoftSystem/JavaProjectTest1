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
			
			Ext.define('modelProdId', {
			    extend: 'Ext.data.Model',
			    fields: [
					{name: 'prodSk', type: 'number', mapping:'prodsk'},
			        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
			    ],
			    idProperty: 'prodId'
			});
			
			Ext.define('modelGrpId', {
			    extend: 'Ext.data.Model',
			    fields: [
					{name: 'grpId', type: 'number', mapping:'grpid'},
			        {name: 'grpDesc', type: 'string', mapping:'grpdesc'}
			    ],
			    idProperty: 'grpId'
			});
			
			Ext.onReady(function(){
				
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'productFAOSTATAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					habBtnDelete: true,
					labelWidth: 125,
					width: 300,
					title: '<bean:message key="productFAOSTAT.header" />',
					afterCancelFn: function(){
						Ext.getStore('dataStore').load(); 
						Ext.getStore('storeGroupProducts').load();
						Ext.getStore('storeProducts').load();
						return true;
					},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            //collapsed: false,
	      	            layout: 'anchor',
	      	          	defaults: {
	      	          		labelAlign:'right',anchor: '100%'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'faoprodSk',
	      	        		name:'faoprodSk'
	      	        	},{
	      	                fieldLabel: '<bean:message key="productFAOSTAT.faoprodCode"/>',
	      	                id: 'faoprodCode',
	      	                name: 'faoprodCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="productFAOSTAT.faoprodCode.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="productFAOSTAT.faoprodDescSpa"/>',
	      	                id: 'faoprodDescSpa',
	      	                name: 'faoprodDescSpa',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="productFAOSTAT.faoprodDescSpa.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="productFAOSTAT.faoprodDescEng"/>',
	      	                id: 'faoprodDescEng',
	      	                name: 'faoprodDescEng',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="productFAOSTAT.faoprodDescEng.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	              	fieldLabel: '<bean:message key="productFAOSTAT.grpId"/>',
	      	                id:'grpId',
	      	              	name:'grpId',
	      	                allowBlank:false,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeGroupProducts',
						        model: 'modelGrpId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'productFAOSTATAction.do?accion=load&store=loadGroupProducts',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'grpId',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
					        queryMode: 'local',
					        displayField: 'grpDesc',
					        valueField: 'grpId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>'
	      	            },{
	      	              	xtype: 'combo',
	      	              	fieldLabel: '<bean:message key="productFAOSTAT.prodSk"/>',
	      	                id:'prodSk',
	      	                name:'prodSk',
	      	                allowBlank:true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeProducts',
						        model: 'modelProdId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'productFAOSTATAction.do?accion=load&store=loadProducts',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'prodSk',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
					        queryMode: 'local',
					        displayField: 'prodDesc',
					        valueField: 'prodSk',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>'
	      	            }]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'faoprodSk', type: 'number', mapping:'faoprodsk'},
						{name: 'prodSk', type: 'number', mapping:'prodsk'},
						{name: 'prodDesc', type: 'string', mapping:'proddesc'},
						{name: 'faoprodCode', type: 'string', mapping:'faoprodcode'},
						{name: 'faoprodDescSpa', type: 'string', mapping:'faoproddescspa'},
						{name: 'faoprodDescEng', type: 'string', mapping:'faoproddesceng'},
						{name: 'grpId', type: 'number', mapping:'grpid'},
			            {name: 'grpDesc', type: 'string', mapping:'grpdesc'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'faoprodSk'
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
			            url: 'productFAOSTATAction.do?accion=load&store=loadGrid',
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
			            property: 'faoprodSk',
			            direction: 'DESC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'faoprodSk',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'faoprodCode'
		            }, {
		                type: 'string',
		                dataIndex: 'faoprodDescSpa'
		            }, {
		                type: 'string',
		                dataIndex: 'faoprodDescEng'
		            }, {
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'grpDesc'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 650,
			        height: 300,
			        title: '<bean:message key="productFAOSTAT.header" />',
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
			        	dataIndex: 'faoprodSk',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="productFAOSTAT.faoprodCode" />',
			            dataIndex: 'faoprodCode',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productFAOSTAT.faoprodDescSpa" />',
			            dataIndex: 'faoprodDescSpa',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productFAOSTAT.faoprodDescEng" />',
			            dataIndex: 'faoprodDescEng',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productFAOSTAT.prodSk" />',
			            dataIndex: 'prodDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productFAOSTAT.grpId" />',
			            dataIndex: 'grpDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(300,function(){
			    	Ext.getStore('dataStore').load(); 
					Ext.getStore('storeGroupProducts').load();
					Ext.getStore('storeProducts').load();
			    });
			    
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="productFAOSTAT.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="productFAOSTAT.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>