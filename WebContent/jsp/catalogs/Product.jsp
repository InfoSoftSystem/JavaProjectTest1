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
				Ext.getCmp('frmVariety').fireEvent('position',{"this":Ext.getCmp('frmVariety'), eOpts: {}});
				Ext.getCmp('frmProductType').fireEvent('position',{"this":Ext.getCmp('frmProductType'), eOpts: {}});
				Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
				Ext.getCmp('GridPanelVariety').fireEvent('position',{"this":Ext.getCmp('GridPanelVariety'), eOpts: {}});
				Ext.getCmp('GridPanelProductType').fireEvent('position',{"this":Ext.getCmp('GridPanelProductType'), eOpts: {}});
			}
			
			function showProduct(){
				Ext.getCmp('formulario').showForm();
				Ext.getCmp('GridPanel').showForm();
				//Ext.getCmp('formulario').setHeight(280);
			}
			
			function hideProduct(){
				Ext.getCmp('formulario').hideForm();
				Ext.getCmp('GridPanel').hideForm();
			}
			
			function showVariety(){
				Ext.getCmp('frmVariety').showForm();
				Ext.getCmp('GridPanelVariety').showForm();
			}
			
			function hideVariety(){
				Ext.getCmp('frmVariety').hideForm();
				Ext.getCmp('GridPanelVariety').hideForm();
			}
			
			function showProductType(){
				Ext.getCmp('frmProductType').showForm();
				Ext.getCmp('GridPanelProductType').showForm();
			}
			
			function hideProductType(){
				Ext.getCmp('frmProductType').hideForm();
				Ext.getCmp('GridPanelProductType').hideForm();
			}
			
			
			Ext.onReady(function(){
				
				
				//************************************************************************************************
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'productAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					width: 280,
					title: '<bean:message key="product.header" />',
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 280
						    }
						});
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
					cancelFn: function(){
						Ext.getStore('dataStore').load();
						Ext.getCmp('btnProductType').disable();
						Ext.getCmp('btnVariety').disable();
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('frmVariety').cancelFn();
						Ext.getCmp('frmProductType').cancelFn();
					},
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
	      	        		id:'prodId',
	      	        		name:'prodId',
				        	listeners:{
				        		change:function(){
				        			Ext.getCmp('varprodId').setValue(Ext.getCmp('prodId').getValue());
				        			Ext.getCmp('ptypeprodId').setValue(Ext.getCmp('prodId').getValue());
				        		}
				        	}
	      	        	},{
	      	                fieldLabel: '<bean:message key="product.prodCode"/>',
	      	                id: 'prodCode',
	      	                name: 'prodCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="product.prodCode.required" />',
				            listeners:{
				            	change:function(){
				            		Ext.getCmp('varprodCode').setValue(Ext.getCmp('prodCode').getValue());
				            		Ext.getCmp('ptypeprodCode').setValue(Ext.getCmp('prodCode').getValue());
				            	}
				            }
	      	            },{
	      	                fieldLabel: '<bean:message key="product.prodDesc"/>',
	      	                id: 'prodDesc',
	      	                name: 'prodDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="product.prodDesc.required" />',
				            listeners:{
				            	change:function(){
				            		Ext.getCmp('varprodDesc').setValue(Ext.getCmp('prodDesc').getValue());
				            		Ext.getCmp('ptypeprodDesc').setValue(Ext.getCmp('prodDesc').getValue());
				            	}
				            }
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="product.prodStatus"/>',
	      	                id:'prodStatus',
	      	                name: 'prodStatus',
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
	      	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="variety.title" />',
          	            id: 'btnVariety',
          	            disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								hideProduct();
								showVariety();
								hideProductType();
							}
						}
          	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="productType.title" />',
          	            id: 'btnProductType',
          	            width: 115,
						disabled: true,
          	          	listeners: {
							click: function(){
								hideProduct();
								showProductType();
								hideVariety();
							}
						}
          	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'prodCode', type: 'string', mapping:'prodcode'},
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'},
			            {name: 'prodStatus', type: 'string', mapping:'prodstatus'},
			            {name: 'prodStatusText', type: 'string', mapping:'prodstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'prodId', type: 'number', mapping:'prodid'}
			        ],
			        idProperty: 'prodId'
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
			            url: 'productAction.do?accion=load',
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
			            property: 'prodId',
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
		                dataIndex: 'prodId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'prodCode'
		            }, {
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'prodStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 350,
			        height: 300,
			        title: '<bean:message key="product.header" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			        showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 250
						    }
						});
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
			        afterLoadReg: function(){
			        	Ext.getCmp('frmVariety').cancelFn();
			        	Ext.getCmp('frmProductType').cancelFn();
			        	
			        	Ext.getCmp('btnProductType').enable();
						Ext.getCmp('btnVariety').enable();
			        },
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'prodId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="product.grid.prodCode" />',
			            dataIndex: 'prodCode',
			            width: 50,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="product.prodDesc" />',
			            dataIndex: 'prodDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="product.prodStatus" />',
			            dataIndex: 'prodStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
			    
			    
				//######################################   PRODUCT TYPES   #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmProductType',
					url:'productTypeAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					width: 250,
					margin:'00 0 0 0',
					title: '<bean:message key="productType.header" />',
					prodId:null,
					prodCode:null,
					prodDesc:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 325
						    }
						});
						
						Ext.getCmp(this.id+'-btnCancel').fireEvent('click');
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
					cancelFn: function(){
						this.prodId = Ext.getCmp('ptypeprodId').getValue();
						this.prodCode = Ext.getCmp('ptypeprodCode').getValue();
						this.prodDesc = Ext.getCmp('ptypeprodDesc').getValue();
						
						var objStore = Ext.getStore('dataStoreProductType'); 
						if(!Ext.isEmpty(this.prodId)){
							
							objStore.proxy.extraParams={
								prodId: this.prodId 
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('ptypeprodId').setValue(this.prodId);
						Ext.getCmp('ptypeprodCode').setValue(this.prodCode);
						Ext.getCmp('ptypeprodDesc').setValue(this.prodDesc);
					},
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
	      	        		id:'ptypeprodId',
	      	        		name:'prodId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="product.prodCode"/>',
	      	                id: 'ptypeprodCode',
	      	                name: 'prodCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="product.prodDesc"/>',
	      	                id: 'ptypeprodDesc',
	      	                name: 'prodDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'ptypeId',
	      	        		name:'ptypeId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="productType.ptypeCode"/>',
	      	                id: 'ptypeCode',
	      	                name: 'ptypeCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="productType.ptypeCode.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="productType.ptypeDesc"/>',
	      	                id: 'ptypeDesc',
	      	                name: 'ptypeDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="productType.ptypeDesc.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="productType.ptypeStatus"/>',
	      	                id:'ptypeStatus',
	      	                name: 'ptypeStatus',
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
	      	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="product.back" />',
          	            //width: 115,
          	          	listeners: {
							click: function(){
								showProduct();
								hideVariety();
								hideProductType();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelProductType', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'ptypeprodId', type: 'number', mapping:'ptypeprodid'},
			            {name: 'ptypeprodCode', type: 'string', mapping:'ptypeprodcode'},
			            {name: 'ptypeprodDesc', type: 'string', mapping:'ptypeproddesc'},
			            {name: 'ptypeId', type: 'number', mapping:'ptypeid'},
			            {name: 'ptypeCode', type: 'string', mapping:'ptypecode'},
			            {name: 'ptypeDesc', type: 'string', mapping:'ptypedesc'},
			            {name: 'ptypeStatus', type: 'string', mapping:'ptypestatus'},
			            {name: 'ptypeStatusText', type: 'string', mapping:'ptypestatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'ptypeId'
			    });
				
								
				// create the Data Store
			    var storeProductType = Ext.create('Ext.data.Store', {
			        id: 'dataStoreProductType',
			        //pageSize: 50,
			        model: 'GridModelProductType',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'productTypeAction.do?accion=load',
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
			            property: 'ptypeId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersProductType = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'ptypeId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'ptypeCode'
		            }, {
		                type: 'string',
		                dataIndex: 'ptypeDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'ptypeStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelProductType',
			    	width: 350,
			        height: 300,
			        title: '<bean:message key="productType.header" />',
			        store: storeProductType,
			        idForm: 'frmProductType',
			        features: [filtersProductType],
			        showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 250
						    }
						});
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'ptypeId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="productType.grid.ptypeCode" />',
			            dataIndex: 'ptypeCode',
			            width: 50,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productType.ptypeDesc" />',
			            dataIndex: 'ptypeDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="productType.ptypeStatus" />',
			            dataIndex: 'ptypeStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });
			    
			    
			    
			    
			    //######################################   VARIETIES #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmVariety',
					url:'varietyAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					width: 250,
					margin:'0 0 0 0',
					title: '<bean:message key="variety.header" />',
					prodId:null,
					prodCode:null,
					prodDesc:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 325
						    }
						});
						
						Ext.getCmp(this.id+'-btnCancel').fireEvent('click');
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
					cancelFn: function(){
						this.prodId = Ext.getCmp('varprodId').getValue();
						this.prodCode = Ext.getCmp('varprodCode').getValue();
						this.prodDesc = Ext.getCmp('varprodDesc').getValue();
						
						var objStore = Ext.getStore('dataStoreVariety'); 
						if(!Ext.isEmpty(this.prodId)){
							
							objStore.proxy.extraParams={
								prodId: this.prodId 
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('varprodId').setValue(this.prodId);
						Ext.getCmp('varprodCode').setValue(this.prodCode);
						Ext.getCmp('varprodDesc').setValue(this.prodDesc);
					},
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
	      	        		id:'varprodId',
	      	        		name:'prodId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="product.prodCode"/>',
	      	                id: 'varprodCode',
	      	                name: 'prodCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="product.prodDesc"/>',
	      	                id: 'varprodDesc',
	      	                name: 'prodDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'varId',
	      	        		name:'varId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="variety.varCode"/>',
	      	                id: 'varCode',
	      	                name: 'varCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="variety.varCode.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="variety.varDesc"/>',
	      	                id: 'varDesc',
	      	                name: 'varDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="variety.varDesc.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="variety.varStatus"/>',
	      	                id:'varStatus',
	      	                name: 'varStatus',
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
	      	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="product.back" />',
          	            //disabled: true,
          	          	//width: 115,
          	          	listeners: {
							click: function(){
								showProduct();
								hideVariety();
								hideProductType();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelVariety', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'varprodId', type: 'number', mapping:'varprodid'},
			            {name: 'varprodCode', type: 'string', mapping:'varprodcode'},
			            {name: 'varprodDesc', type: 'string', mapping:'varproddesc'},
			            {name: 'varId', type: 'number', mapping:'varid'},
			            {name: 'varCode', type: 'string', mapping:'varcode'},
			            {name: 'varDesc', type: 'string', mapping:'vardesc'},
			            {name: 'varStatus', type: 'string', mapping:'varstatus'},
			            {name: 'varStatusText', type: 'string', mapping:'varstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'varId'
			    });
				
								
				// create the Data Store
			    var storeVariety = Ext.create('Ext.data.Store', {
			        id: 'dataStoreVariety',
			        //pageSize: 50,
			        model: 'GridModelVariety',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'varietyAction.do?accion=load',
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
			            property: 'varId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersVariety = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'varId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'varCode'
		            }, {
		                type: 'string',
		                dataIndex: 'varDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'varStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelVariety',
			    	width: 350,
			        //height: 300,
			        title: '<bean:message key="variety.header" />',
			        store: storeVariety,
			        idForm: 'frmVariety',
			        features: [filtersVariety],
			        showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 285
						    }
						});
					},
					hideForm: function(){
						this.animate({
							to:{
								height: 0
							}
						}).animate({
						    to: {
						        opacity: 0
						    }
						});
					},
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'varId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="variety.grid.varCode" />',
			            dataIndex: 'varCode',
			            width: 50,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="variety.varDesc" />',
			            dataIndex: 'varDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="variety.varStatus" />',
			            dataIndex: 'varStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });
			    
			    hideVariety();
			    hideProductType();
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="product.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="product.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>