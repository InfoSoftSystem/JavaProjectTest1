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
				Ext.getCmp('frmRegion').fireEvent('position',{"this":Ext.getCmp('frmRegion'), eOpts: {}});
				Ext.getCmp('frmProvince').fireEvent('position',{"this":Ext.getCmp('frmProvince'), eOpts: {}});
				Ext.getCmp('frmCountryCOMTRADE').fireEvent('position',{"this":Ext.getCmp('frmCountryCOMTRADE'), eOpts: {}});
				Ext.getCmp('frmCountryFAOSTAT').fireEvent('position',{"this":Ext.getCmp('frmCountryFAOSTAT'), eOpts: {}});
				
				Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
				Ext.getCmp('GridPanelRegion').fireEvent('position',{"this":Ext.getCmp('GridPanelRegion'), eOpts: {}});
				Ext.getCmp('GridPanelProvince').fireEvent('position',{"this":Ext.getCmp('GridPanelProvince'), eOpts: {}});
				Ext.getCmp('GridPanelCountryCOMTRADE').fireEvent('position',{"this":Ext.getCmp('GridPanelCountryCOMTRADE'), eOpts: {}});
				Ext.getCmp('GridPanelCountryFAOSTAT').fireEvent('position',{"this":Ext.getCmp('GridPanelCountryFAOSTAT'), eOpts: {}});
			}
			
			function showCountry(){
				Ext.getCmp('formulario').showForm();
				Ext.getCmp('GridPanel').showForm();
			}
			
			function hideCountry(){
				Ext.getCmp('formulario').hideForm();
				Ext.getCmp('GridPanel').hideForm();
			}
			
			function showRegion(){
				Ext.getCmp('frmRegion').showForm();
				Ext.getCmp('GridPanelRegion').showForm();
			}
			
			function hideRegion(){
				Ext.getCmp('frmRegion').hideForm();
				Ext.getCmp('GridPanelRegion').hideForm();
			}
			
			function showProvince(){
				Ext.getCmp('frmProvince').showForm();
				Ext.getCmp('GridPanelProvince').showForm();
			}
			
			function hideProvince(){
				Ext.getCmp('frmProvince').hideForm();
				Ext.getCmp('GridPanelProvince').hideForm();
			}
			
			function showCountryCOMTRADE(){
				Ext.getCmp('frmCountryCOMTRADE').showForm();
				Ext.getCmp('GridPanelCountryCOMTRADE').showForm();
			}
			
			function hideCountryCOMTRADE(){
				Ext.getCmp('frmCountryCOMTRADE').hideForm();
				Ext.getCmp('GridPanelCountryCOMTRADE').hideForm();
			}
			
			function showCountryFAOSTAT(){
				Ext.getCmp('frmCountryFAOSTAT').showForm();
				Ext.getCmp('GridPanelCountryFAOSTAT').showForm();
			}
			
			function hideCountryFAOSTAT(){
				Ext.getCmp('frmCountryFAOSTAT').hideForm();
				Ext.getCmp('GridPanelCountryFAOSTAT').hideForm();
			}
			
			Ext.onReady(function(){
				
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'countryAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 120,
					title: '<bean:message key="country.header" />',
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 315
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
						Ext.getCmp('btnRegion').disable();
						Ext.getCmp('btnCountryCOMTRADE').disable();
						Ext.getCmp('btnCountryFAOSTAT').disable();
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('frmRegion').cancelFn();
						Ext.getCmp('frmProvince').cancelFn();
						Ext.getCmp('frmCountryCOMTRADE').cancelFn();
						Ext.getCmp('frmCountryFAOSTAT').cancelFn();
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
	      	        		id:'ctrId',
	      	        		name:'ctrId',
				        	listeners:{
				        		change:function(){
				        			Ext.getCmp('regctrId').setValue(Ext.getCmp('ctrId').getValue());
				        			Ext.getCmp('provctrId').setValue(Ext.getCmp('ctrId').getValue());
				        			Ext.getCmp('ctradectrId').setValue(Ext.getCmp('ctrId').getValue());
				        			Ext.getCmp('faoctrctrId').setValue(Ext.getCmp('ctrId').getValue());
				        		}
				        	}
	      	        	},{
	      	                fieldLabel: '<bean:message key="country.ctrISO3"/>',
	      	                id: 'ctrISO3',
	      	                name: 'ctrISO3',
	      	                maxLength: 3,
	      	                width: 170,
	      	              	enforceMaxLength: true,
	      	                allowBlank:false,
	      	                fieldStyle: 'text-transform: uppercase',
	      	            	blankText: '<bean:message key="country.ctrISO3.required" />',
				            listeners:{
				            	change:function(){
				            		Ext.getCmp('regctrISO3').setValue(Ext.getCmp('ctrISO3').getValue());
				            		Ext.getCmp('provctrISO3').setValue(Ext.getCmp('ctrISO3').getValue());
				            		Ext.getCmp('ctradectrISO3').setValue(Ext.getCmp('ctrISO3').getValue());
				            		Ext.getCmp('faoctrctrISO3').setValue(Ext.getCmp('ctrISO3').getValue());
				            	}
				            }
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescSpanish"/>',
	      	                id: 'ctrDescSpanish',
	      	                name: 'ctrDescSpanish',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="country.ctrDescSpanish.required" />',
				            listeners:{
				            	change:function(){
				            		Ext.getCmp('regctrDesc').setValue(Ext.getCmp('ctrDescSpanish').getValue());
				            		Ext.getCmp('provctrDesc').setValue(Ext.getCmp('ctrDescSpanish').getValue());
				            		Ext.getCmp('ctradectrDesc').setValue(Ext.getCmp('ctrDescSpanish').getValue());
				            		Ext.getCmp('faoctrctrDesc').setValue(Ext.getCmp('ctrDescSpanish').getValue());
				            	}
				            }
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescEnglish"/>',
	      	                id:'ctrDescEnglish',
	      	                name: 'ctrDescEnglish',
	      	                maxLength: 100,
	      	              	enforceMaxLength: true,
	      	              	allowBlank:false,
	      	            	blankText: '<bean:message key="country.ctrDescEnglish.required" />',
				            listeners:{
				            	change:function(){
				            		Ext.getCmp('regctrDescEng').setValue(Ext.getCmp('ctrDescEnglish').getValue());
				            		Ext.getCmp('provctrDescEng').setValue(Ext.getCmp('ctrDescEnglish').getValue());
				            		Ext.getCmp('ctradectrDescEng').setValue(Ext.getCmp('ctrDescEnglish').getValue());
				            		Ext.getCmp('faoctrctrDescEng').setValue(Ext.getCmp('ctrDescEnglish').getValue());
				            	}
				            }
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="country.ctrStatus"/>',
	      	                id:'ctrStatus',
	      	                name: 'ctrStatus',
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
      	            	text: '<bean:message key="region.title" />',
          	            id: 'btnRegion',
          	            disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								hideCountry();
								showRegion();
								hideProvince();
							}
						}
          	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="countryCOMTRADE.title" />',
          	            id: 'btnCountryCOMTRADE',
          	            disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								hideCountry();
								hideRegion();
								hideProvince();
								showCountryCOMTRADE();
							}
						}
          	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="countryFAOSTAT.title" />',
          	            id: 'btnCountryFAOSTAT',
          	            disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								hideCountry();
								hideRegion();
								hideProvince();
								hideCountryCOMTRADE();
								showCountryFAOSTAT();
							}
						}
          	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'ctrISO3', type: 'string', mapping:'ctriso3'},
			            {name: 'ctrDescSpanish', type: 'string', mapping:'ctrdescspanish'},
			            {name: 'ctrDescEnglish', type: 'string', mapping:'ctrdescenglish'},
			            {name: 'ctrStatus', type: 'string', mapping:'ctrstatus'},
			            {name: 'ctrStatusText', type: 'string', mapping:'ctrstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'ctrId', type: 'number', mapping:'ctrid'}
			        ],
			        idProperty: 'ctrId'
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
			            url: 'countryAction.do?accion=load',
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
			            property: 'ctrId',
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
		                dataIndex: 'ctrId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'ctrISO3'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDescSpanish'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDescEnglish'
		            }, {
		                type: 'list',
		                dataIndex: 'ctrStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 500,
			        height: 300,
			        title: '<bean:message key="country.header" />',
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
			        	Ext.getCmp('frmRegion').cancelFn();
			        	Ext.getCmp('btnRegion').enable();
			        	Ext.getCmp('frmCountryCOMTRADE').cancelFn();
			        	Ext.getCmp('btnCountryCOMTRADE').enable();
			        	Ext.getCmp('frmCountryFAOSTAT').cancelFn();
			        	Ext.getCmp('btnCountryFAOSTAT').enable();
			        	
			        },
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'crtId',
			        	hidden: true,
			        	disabled: true
			        },{
			            // id assigned so we can apply custom css (e.g. .x-grid-cell-topic b { color:#333 })
			            // BUG This poses an issue in subclasses of Grid now because Headers are now Components
			            // therefore the id will be registered in the ComponentManager and conflict. Need a way to
			            // add additional CSS classes to the rendered cells.
			            //id: 'topic',
			            text: '<bean:message key="country.grid.ctrISO3" />',
			            dataIndex: 'ctrISO3',
			            flex: 1,
			            width: 35,
			            //renderer: renderTopic,
			            sortable: true
			        	
			        },{
			            text: '<bean:message key="country.ctrDescSpanish" />',
			            dataIndex: 'ctrDescSpanish',
			            width: 150,
			            //hidden: true,
			            sortable: true
			        },{
			        	text: '<bean:message key="country.ctrDescEnglish" />',
			            dataIndex: 'ctrDescEnglish',
			            //align: 'center',
			            width: 150,
			            sortable: true
			        },{
			            text: '<bean:message key="country.ctrStatus" />',
			            dataIndex: 'ctrStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
			    
			    
				//######################################   REGIONS   #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmRegion',
					url:'regionAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 80,
					width: 300,
					margin:'0 0 0 0',
					title: '<bean:message key="region.header" />',
					ctrId:null,
					ctrISO3:null,
					ctrDesc:null,
					ctrDescEng:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 355
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
						Ext.getCmp('btnProvince').disable();
						
						this.ctrId = Ext.getCmp('regctrId').getValue();
						this.ctrISO3 = Ext.getCmp('regctrISO3').getValue();
						this.ctrDesc = Ext.getCmp('regctrDesc').getValue();
						this.ctrDescEng = Ext.getCmp('regctrDescEng').getValue();
						
						var objStore = Ext.getStore('dataStoreRegion'); 
						if(!Ext.isEmpty(this.ctrId)){
							
							objStore.proxy.extraParams={
								ctrId: this.ctrId 
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('regctrId').setValue(this.ctrId);
						Ext.getCmp('regctrISO3').setValue(this.ctrISO3);
						Ext.getCmp('regctrDesc').setValue(this.ctrDesc);
						Ext.getCmp('regctrDescEng').setValue(this.ctrDescEng);
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
	      	        		id:'regctrId',
	      	        		name:'ctrId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="country.ctrISO3"/>',
	      	                id: 'regctrISO3',
	      	                name: 'ctrISO3',
	      	              	maxLength: 4,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescSpanish"/>',
	      	                id: 'regctrDesc',
	      	                name: 'ctrDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'regctrDescEng',
	      	        		name:'ctrDescEng'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'regId',
	      	        		name:'regId',
				        	listeners:{
				        		change:function(){
				        			Ext.getCmp('provregId').setValue(Ext.getCmp('regId').getValue());
				        		}
				        	}
	      	        	},{
	      	                fieldLabel: '<bean:message key="region.regDesc"/>',
	      	                id: 'regDesc',
	      	                name: 'regDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="region.regDesc.required" />',
				        	listeners:{
				        		change:function(){
				        			Ext.getCmp('provregDesc').setValue(Ext.getCmp('regDesc').getValue());
				        		}
				        	}
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="region.regStatus"/>',
	      	                id:'regStatus',
	      	                name: 'regStatus',
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
      	            	text: '<bean:message key="country.back" />',
          	            //disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								showCountry();
								hideRegion();
								hideProvince();
							}
						}
          	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="province.title" />',
          	            id: 'btnProvince',
          	            width: 115,
						disabled: true,
          	          	listeners: {
							click: function(){
								hideCountry();
								showProvince();
								hideRegion();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelRegion', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'regctrId', type: 'number', mapping:'regctrid'},
			            {name: 'regctrISO3', type: 'string', mapping:'regctriso3'},
			            {name: 'regctrDesc', type: 'string', mapping:'regctrdesc'},
			            {name: 'regctrDescEng', type: 'string', mapping:'regctrdesceng'},
			            {name: 'regId', type: 'number', mapping:'regid'},
			            {name: 'regDesc', type: 'string', mapping:'regdesc'},
			            {name: 'regStatus', type: 'string', mapping:'regstatus'},
			            {name: 'regStatusText', type: 'string', mapping:'regstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'regId'
			    });
				
								
				// create the Data Store
			    var storeRegion = Ext.create('Ext.data.Store', {
			        id: 'dataStoreRegion',
			        //pageSize: 50,
			        model: 'GridModelRegion',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'regionAction.do?accion=load',
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
			            property: 'regId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersRegion = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'regId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'regDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'regStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelRegion',
			    	width: 350,
			        //height: 300,
			        title: '<bean:message key="region.header" />',
			        store: storeRegion,
			        idForm: 'frmRegion',
			        features: [filtersRegion],
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
			        afterLoadReg: function(){
			        	Ext.getCmp('frmProvince').cancelFn();
			        	
			        	Ext.getCmp('btnProvince').enable();
			        },
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'regId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="region.regDesc" />',
			            dataIndex: 'regDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="region.regStatus" />',
			            dataIndex: 'regStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });
			    
			    
			    
			//######################################   PROVINCE   #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmProvince',
					url:'provinceAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 90,
					width: 300,
					margin:'0 0 0 0',
					title: '<bean:message key="province.header" />',
					ctrId:null,
					ctrISO3:null,
					ctrDesc:null,
					ctrDescEng:null,
					regId:null,
					regDesc:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 365
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
						this.ctrId = Ext.getCmp('provctrId').getValue();
						this.ctrISO3= Ext.getCmp('provctrISO3').getValue();
						this.ctrDesc = Ext.getCmp('provctrDesc').getValue();
						this.ctrDescEng = Ext.getCmp('provctrDescEng').getValue();
						
						this.regId = Ext.getCmp('provregId').getValue();
						this.regDesc = Ext.getCmp('provregDesc').getValue();
						
						var objStore = Ext.getStore('dataStoreProvince'); 
						if(!Ext.isEmpty(this.ctrId) && !Ext.isEmpty(this.regId)){
							
							objStore.proxy.extraParams={
								ctrId: this.ctrId,
								regId: this.regId
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('provctrId').setValue(this.ctrId);
						Ext.getCmp('provctrISO3').setValue(this.ctrISO3);
						Ext.getCmp('provctrDesc').setValue(this.ctrDesc);
						Ext.getCmp('provctrDescEng').setValue(this.ctrDescEng);
						
						Ext.getCmp('provregId').setValue(this.regId);
						Ext.getCmp('provregDesc').setValue(this.regDesc);
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
	      	        		id:'provctrId',
	      	        		name:'ctrId'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'provregId',
	      	        		name:'regId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="country.ctrISO3"/>',
	      	                id: 'provctrISO3',
	      	                name: 'ctrISO3',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescSpanish"/>',
	      	                id: 'provctrDesc',
	      	                name: 'ctrDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'provctrDescEng',
	      	        		name:'ctrDescEng'
	      	        	},{
	      	                fieldLabel: '<bean:message key="region.regDesc"/>',
	      	                id: 'provregDesc',
	      	                name: 'regDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'provId',
	      	        		name:'provId'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	                fieldLabel: '<bean:message key="province.provDesc"/>',
	      	                id: 'provDesc',
	      	                name: 'provDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="province.provDesc.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="province.provStatus"/>',
	      	                id:'provStatus',
	      	                name: 'provStatus',
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
      	            	text: '<bean:message key="country.back" />',
          	            width: 115,
          	          	listeners: {
							click: function(){
								showCountry();
								hideRegion();
								hideProvince();
							}
						}
          	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="region.back" />',
          	            //disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								hideCountry();
								showRegion();
								hideProvince();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelProvince', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'provctrId', type: 'number', mapping:'provctrid'},
			            {name: 'provctrISO3', type: 'string', mapping:'provctriso3'},
			            {name: 'provctrDesc', type: 'string', mapping:'provctrdesc'},
			            {name: 'provctrDescEng', type: 'string', mapping:'provctrdesceng'},
			            {name: 'provregId', type: 'number', mapping:'provregid'},
			            {name: 'provregDesc', type: 'string', mapping:'provregdesc'},
			            {name: 'provId', type: 'number', mapping:'provid'},
			            {name: 'provDesc', type: 'string', mapping:'provdesc'},
			            {name: 'provStatus', type: 'string', mapping:'provstatus'},
			            {name: 'provStatusText', type: 'string', mapping:'provstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'provId'
			    });
				
								
				// create the Data Store
			    var storeProvince = Ext.create('Ext.data.Store', {
			        id: 'dataStoreProvince',
			        //pageSize: 50,
			        model: 'GridModelProvince',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'provinceAction.do?accion=load',
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
			            property: 'provId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersProvince = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'provId',
		                disabled: true
		            }, {
		                type: 'string',
		                dataIndex: 'provDesc'
		            }, {
		                type: 'list',
		                dataIndex: 'provStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelProvince',
			    	width: 350,
			        height: 300,
			        title: '<bean:message key="province.header" />',
			        store: storeProvince,
			        idForm: 'frmProvince',
			        features: [filtersProvince],
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
			        	dataIndex: 'provId',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="province.provDesc" />',
			            dataIndex: 'provDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="province.provStatus" />',
			            dataIndex: 'provStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        }]
			    });
			    
			    
			    
				//######################################   COUNTRY COMTRADE.ORG   #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmCountryCOMTRADE',
					url:'countryCOMTRADEAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 80,
					width: 300,
					margin:'0 0 0 0',
					habBtnDelete: true,
					title: '<bean:message key="countryCOMTRADE.header" />',
					ctrId:null,
					ctrISO3:null,
					ctrDesc:null,
					ctrDescEng:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 355
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
						this.ctrId = Ext.getCmp('ctradectrId').getValue();
						this.ctrISO3 = Ext.getCmp('ctradectrISO3').getValue();
						this.ctrDesc = Ext.getCmp('ctradectrDesc').getValue();
						this.ctrDescEng = Ext.getCmp('ctradectrDescEng').getValue();
						
						var objStore = Ext.getStore('dataStoreCountryCOMTRADE'); 
						if(!Ext.isEmpty(this.ctrId)){
							
							objStore.proxy.extraParams={
								ctrId: this.ctrId 
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('ctradectrId').setValue(this.ctrId);
						Ext.getCmp('ctradectrISO3').setValue(this.ctrISO3);
						Ext.getCmp('ctradectrDesc').setValue(this.ctrDesc);
						Ext.getCmp('ctradectrDescEng').setValue(this.ctrDescEng);
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
	      	        		id:'ctradectrId',
	      	        		name:'ctrId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="country.ctrISO3"/>',
	      	                id: 'ctradectrISO3',
	      	                name: 'ctrISO3',
	      	              	maxLength: 4,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescSpanish"/>',
	      	                id: 'ctradectrDesc',
	      	                name: 'ctrDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'ctradectrDescEng',
	      	        		name:'ctrDescEng'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'ctradeSk',
	      	        		name:'ctradeSk'
	      	        	},{
	      	                xtype:'numberfield',
	      	        		fieldLabel: '<bean:message key="countryCOMTRADE.ctradeId"/>',
	      	                id: 'ctradeId',
	      	                name: 'ctradeId',
	      	              	width: 170,
							minValue: 0,
							allowBlank:false,
							hideTrigger: false,
							keyNavEnabled: true,
							mouseWheelEnabled: false,
							allowDecimals: false,
							blankText: '<bean:message key="countryCOMTRADE.ctradeDesc.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="countryCOMTRADE.ctradeDesc"/>',
	      	                id: 'ctradeDesc',
	      	                name: 'ctradeDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="countryCOMTRADE.ctradeDesc.required" />'
	      	            }]
	      	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="country.back" />',
          	            //disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								showCountry();
								hideRegion();
								hideProvince();
								hideCountryCOMTRADE();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelCountryCOMTRADE', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'ctradectrId', type: 'number', mapping:'ctradectrid'},
			            {name: 'ctradectrISO3', type: 'string', mapping:'ctradectriso3'},
			            {name: 'ctradectrDesc', type: 'string', mapping:'ctradectrdesc'},
			            {name: 'ctradectrDescEng', type: 'string', mapping:'ctradectrdesceng'},
			            {name: 'ctradeSk', type: 'string', mapping:'ctradesk'},
			            {name: 'ctradeId', type: 'number', mapping:'ctradeid'},
			            {name: 'ctradeDesc', type: 'string', mapping:'ctradedesc'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'ctradeSk'
			    });
				
								
				// create the Data Store
			    var storeCountryCOMTRADE = Ext.create('Ext.data.Store', {
			        id: 'dataStoreCountryCOMTRADE',
			        //pageSize: 50,
			        model: 'GridModelCountryCOMTRADE',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'countryCOMTRADEAction.do?accion=load',
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
			            property: 'ctradeSk',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersCountryCOMTRADE = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'ctradeId'
		            }, {
		                type: 'string',
		                dataIndex: 'ctradeDesc'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelCountryCOMTRADE',
			    	width: 350,
			        //height: 300,
			        title: '<bean:message key="countryCOMTRADE.header" />',
			        store: storeCountryCOMTRADE,
			        idForm: 'frmCountryCOMTRADE',
			        features: [filtersCountryCOMTRADE],
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
			        afterLoadReg: function(){
			        	
			        },
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'ctradeSk',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="countryCOMTRADE.ctradeId" />',
			            dataIndex: 'ctradeId',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="countryCOMTRADE.ctradeDesc" />',
			            dataIndex: 'ctradeDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        }]
			    });
			    
			    
				//######################################   COUNTRY FAOSTAT.ORG   #################################
			    
			    Ext.create('Ext.custom.FormPanel',{
					id:'frmCountryFAOSTAT',
					url:'countryFAOSTATAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 80,
					width: 300,
					margin:'0 0 0 0',
					habBtnDelete: true,
					title: '<bean:message key="countryFAOSTAT.header" />',
					ctrId:null,
					ctrISO3:null,
					ctrDesc:null,
					ctrDescEng:null,
					showForm: function(){
						this.animate({
							to:{
						        opacity: 100
							}
						}).animate({
						    to: {
								height: 355
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
						this.ctrId = Ext.getCmp('faoctrctrId').getValue();
						this.ctrISO3 = Ext.getCmp('faoctrctrISO3').getValue();
						this.ctrDesc = Ext.getCmp('faoctrctrDesc').getValue();
						this.ctrDescEng = Ext.getCmp('faoctrctrDescEng').getValue();
						
						var objStore = Ext.getStore('dataStoreCountryFAOSTAT'); 
						if(!Ext.isEmpty(this.ctrId)){
							
							objStore.proxy.extraParams={
								ctrId: this.ctrId 
							};
							
							objStore.load();
						}else{
							objStore.removeAll();
						}
						return true;
					},
					afterCancelFn:function(){
						Ext.getCmp('faoctrctrId').setValue(this.ctrId);
						Ext.getCmp('faoctrctrISO3').setValue(this.ctrISO3);
						Ext.getCmp('faoctrctrDesc').setValue(this.ctrDesc);
						Ext.getCmp('faoctrctrDescEng').setValue(this.ctrDescEng);
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
	      	        		id:'faoctrctrId',
	      	        		name:'ctrId'
	      	        	},{
	      	                fieldLabel: '<bean:message key="country.ctrISO3"/>',
	      	                id: 'faoctrctrISO3',
	      	                name: 'ctrISO3',
	      	              	maxLength: 4,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	                fieldLabel: '<bean:message key="country.ctrDescSpanish"/>',
	      	                id: 'faoctrctrDesc',
	      	                name: 'ctrDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	readOnly: true
	      	            },{
	      	        		xtype:'hidden',
	      	        		id:'faoctrctrDescEng',
	      	        		name:'ctrDescEng'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'faoctrSk',
	      	        		name:'faoctrSk'
	      	        	},{
	      	                xtype:'numberfield',
	      	        		fieldLabel: '<bean:message key="countryFAOSTAT.faoctrId"/>',
	      	                id: 'faoctrId',
	      	                name: 'faoctrId',
	      	              	width: 170,
							minValue: 0,
							allowBlank:false,
							hideTrigger: false,
							keyNavEnabled: true,
							mouseWheelEnabled: false,
							allowDecimals: false,
							blankText: '<bean:message key="countryFAOSTAT.faoctrDesc.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="countryFAOSTAT.faoctrDesc"/>',
	      	                id: 'faoctrDesc',
	      	                name: 'faoctrDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="countryFAOSTAT.faoctrDesc.required" />'
	      	            }]
	      	        },{
      	            	xtype: 'button',
      	            	text: '<bean:message key="country.back" />',
          	            //disabled: true,
          	          	width: 115,
          	          	listeners: {
							click: function(){
								showCountry();
								hideRegion();
								hideProvince();
								hideCountryFAOSTAT();
							}
						}
          	        }]//Items Form.Panel
				});
			    
			    
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModelCountryFAOSTAT', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'faoctrctrId', type: 'number', mapping:'faoctrctrid'},
			            {name: 'faoctrctrISO3', type: 'string', mapping:'faoctrctriso3'},
			            {name: 'faoctrctrDesc', type: 'string', mapping:'faoctrctrdesc'},
			            {name: 'faoctrctrDescEng', type: 'string', mapping:'faoctrctrdesceng'},
			            {name: 'faoctrSk', type: 'string', mapping:'faoctrsk'},
			            {name: 'faoctrId', type: 'number', mapping:'faoctrid'},
			            {name: 'faoctrDesc', type: 'string', mapping:'faoctrdesc'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'}
			        ],
			        idProperty: 'faoctrSk'
			    });
				
								
				// create the Data Store
			    var storeCountryFAOSTAT = Ext.create('Ext.data.Store', {
			        id: 'dataStoreCountryFAOSTAT',
			        //pageSize: 50,
			        model: 'GridModelCountryFAOSTAT',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'countryFAOSTATAction.do?accion=load',
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
			            property: 'faoctrSk',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filtersCountryFAOSTAT = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'numeric',
		                dataIndex: 'faoctrId'
		            }, {
		                type: 'string',
		                dataIndex: 'faoctrDesc'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanelCountryFAOSTAT',
			    	width: 350,
			        //height: 300,
			        title: '<bean:message key="countryFAOSTAT.header" />',
			        store: storeCountryFAOSTAT,
			        idForm: 'frmCountryFAOSTAT',
			        features: [filtersCountryFAOSTAT],
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
			        afterLoadReg: function(){
			        	
			        },
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'faoctrSk',
			        	hidden: true,
			        	disabled: true
			        },{
			            text: '<bean:message key="countryFAOSTAT.faoctrId" />',
			            dataIndex: 'faoctrId',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="countryFAOSTAT.faoctrDesc" />',
			            dataIndex: 'faoctrDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        }]
			    });
			    
			    hideRegion();
			    hideProvince();
			    hideCountryCOMTRADE();
			    hideCountryFAOSTAT();
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="country.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="country.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>