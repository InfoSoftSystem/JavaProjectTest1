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
				//Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
			}
			
			
			Ext.onReady(function(){
				<%
					UserForm user = (UserForm) session.getAttribute("user");
				%>
		        //Property used to choose the country column according to current language 
		        var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var userCtrId = <%=user.getCtrId()%>;
		        
		      	//************************************************************************************************
				//*** Models for Forms
				
				Ext.define('modelCtrSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'msg', type:'string'},
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
		      	
				Ext.define('modelProdSk', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'msg', type:'string'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ],
				    idProperty: 'prodId'
				});
				
				Ext.define('modelYearSk', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'yearSk', type: 'int', mapping:'yearsk'}
				    ],
				    idProperty: 'yearSk'
				});
				
				Ext.define('modelCIFOB', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'farmPrice', type: 'numeric', mapping:'farmprice'},
						{name: 'packagingCost', type: 'numeric', mapping:'packagingcost'},
						{name: 'freightCost', type: 'numeric', mapping:'freightcost'},
						{name: 'exportCost', type: 'numeric', mapping:'exportcost'},
						{name: 'percentageGain', type: 'numeric', mapping:'percentagegain'}
				    ]
				});
				
				var storeOffererCountries = Ext.create('Ext.data.Store', {
			        id: 'storeOffererCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryWindowOpportunityAction.do?accion=load&store=loadOffererCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },
		        	sorters: [{
			            property: 'ctrDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: true
			    });
				
				var storeCompetitorCountries = Ext.create('Ext.data.Store', {
			        id: 'storeCompetitorCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryWindowOpportunityAction.do?accion=load&store=loadCompetitorCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },
		        	sorters: [{
			            property: 'ctrDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				var storeDestinationCountries = Ext.create('Ext.data.Store', {
			        id: 'storeDestinationCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryWindowOpportunityAction.do?accion=load&store=loadDestinationCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },
		        	sorters: [{
			            property: 'ctrDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				var storeProducts = Ext.create('Ext.data.Store', {
			        id: 'storeProducts',
			        model: 'modelProdSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryWindowOpportunityAction.do?accion=load&store=loadProducts',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },sorters: [{
			            property: 'prodDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				var storeCIFOB = Ext.create('Ext.data.Store', {
			        id: 'storeCIFOB',
			        model: 'modelCIFOB',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryWindowOpportunityAction.do?accion=load&store=loadCIFOB',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },
			        autoLoad: false
			    });
				
				function loadProducts(){
					var tmpStore = storeProducts;
	        		
	        		tmpStore.proxy.extraParams={
		                initialDate: Ext.getCmp('initialDate').getValue(),
		                competitorCountries: Ext.getCmp('competitorCountries').getSubmitValue(),
		                destinationCountry: Ext.getCmp('destinationCountry').getValue()
		            };
	        		
					var m = wait();
					
					tmpStore.load({
						scope:this,
						callback:function(records, operation, success){
							m.close();
							
							/*var tmpComponent = Ext.getCmp('products');
							
							tmpComponent.clearValue();
							
							if(tmpComponent.store.getCount()>0){
								tmpComponent.fromField.store.removeAll();
							}
							
							if(success){
								tmpComponent.store.add(storeProducts.getRange());
								tmpComponent.fromField.store.add(storeProducts.getRange());
							}*/
						}
					});
				}
				
				function loadCompetitorCountries(){
					var tmpStore = storeCompetitorCountries;
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && Ext.getCmp('initialDate').isValid() && !Ext.isEmpty(Ext.getCmp('destinationCountry').getValue()) ){
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                destinationCountry: Ext.getCmp('destinationCountry').getValue()
			            };
		        		
						var m = wait();
						
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								var tmpCountries = Ext.getCmp('competitorCountries');
								tmpCountries.clearValue();
								
								tmpCountries.fromField.store.removeAll();
							
								tmpCountries.store.add(tmpStore.getRange());
								tmpCountries.fromField.store.add(tmpStore.getRange());
							}
						});
					}else{
						var tmpCountries = Ext.getCmp('competitorCountries');
						tmpCountries.clearValue();
						
						tmpCountries.fromField.store.removeAll();
					}
				}
				
				function loadDestinationCountries(){
					var tmpStore = storeDestinationCountries;
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue())){
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue()
			            };
		        		
						var m = wait();
						Ext.getCmp('destinationCountry').setValue('');
						Ext.getCmp('destinationCountry').enable();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
							}
						});
					}else{
						tmpStore.removeAll();
					}
				}
				
				function loadCIFOB(){
					var tmpStore = storeCIFOB;
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('offererCountries').getValue()) && !Ext.isEmpty(Ext.getCmp('destinationCountry').getValue()) ){
		        		
		        		tmpStore.proxy.extraParams={
			                destinationCountry: Ext.getCmp('destinationCountry').getValue(),
			                offererCountries: Ext.getCmp('offererCountries').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								Ext.getCmp('farmPrice').enable();
								Ext.getCmp('packagingCost').enable();
								Ext.getCmp('freightCost').enable();
								Ext.getCmp('exportCost').enable();
								Ext.getCmp('percentageGain').enable();
								
								if(tmpStore.getCount()>0){
									
									var record = tmpStore.getAt(0);
									Ext.getCmp('farmPrice').setValue(record.get('farmPrice'));
									Ext.getCmp('packagingCost').setValue(record.get('packagingCost'));
									Ext.getCmp('freightCost').setValue(record.get('freightCost'));
									Ext.getCmp('exportCost').setValue(record.get('exportCost'));
									Ext.getCmp('percentageGain').setValue(record.get('percentageGain'));
								}else{
									Ext.getCmp('farmPrice').setValue(null);
									Ext.getCmp('packagingCost').setValue(null);
									Ext.getCmp('freightCost').setValue(null);
									Ext.getCmp('exportCost').setValue(null);
									Ext.getCmp('percentageGain').setValue(null);
								}
							}
						});
					}else{
						tmpStore.removeAll();
						Ext.getCmp('farmPrice').setValue(null);
						Ext.getCmp('packagingCost').setValue(null);
						Ext.getCmp('freightCost').setValue(null);
						Ext.getCmp('exportCost').setValue(null);
						Ext.getCmp('percentageGain').setValue(null);
					}
				}
				
				//************************************************************************************************
				
				var cstFormPanel = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'queryWindowOpportunityAction.do',
					//url:'reportAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					width: 550,
					title: '<bean:message key="queryWindowOpportunity.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value:'10_Ventana_Oportunidad.jrxml'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'FormatPrint',
      	        		name:'FormatPrint',
      	        		value:'xlsx'
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
	      	            	width:200, 
	      	            	height: 25,
	      	            	labelAlign:'right',
	      	            	margin: '0 0 0 0'
	      	            },
	      	            items :[
						{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryWindowOpportunity.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryWindowOpportunity.initialDate.required"/>',
					        store: 	Ext.create('Ext.data.Store',{
					        	model: 'modelYearSk',
					        	data: {registers: <%=request.getAttribute("yearsCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'yearSk',
	      				            direction: 'DESC'
	      				        }]
					        }),
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        editable: false,
					        listeners:{
					        	change:function(obj, newValue, oldValue){
					        		loadDestinationCountries();
					        	}
					        }
					    },{
	      	              	//labelWidth:180,
	      	              	width: 250,
					    	xtype: 'combo',
					    	fieldLabel: '<bean:message key="queryWindowOpportunity.destination"/>',
	      	              	blankText:'<bean:message key="queryWindowOpportunity.destination.required"/>',
	      	                id:'destinationCountry',
	      	                name: 'destinationCountry',
	      	                allowBlank:false,
	      	              	store: storeDestinationCountries,
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrId',
					        disabled: true,
					        editable: false,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: function(){
					        		loadCIFOB();
					        		new Ext.util.DelayedTask().delay(500,function(){
					        			loadCompetitorCountries();
					        		});
					        	}
					        }
	      	            },{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryWindowOpportunity.competitorCountries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryWindowOpportunity.competitorCountries.selected" />'
	      	        	},{
	      	        		colspan:2,
	      	        		xtype: 'container',
      	                    layout: 'fit',
      	                    height: 100,
      	                    width: 500,
      	                    defaultType: 'textfield',
      	                    defaults:{
      	                    	labelAlign:'right',
      	                    	margin:'0 0 0 10'
      	                    },
      	                    items: [{
      	                    	xtype: 'itemselector',
	      	                    name: 'competitorCountries',
					            id: 'competitorCountries',
					            hideLabel: true,
					            blankText: '<bean:message key="queryWindowOpportunity.competitorCountries.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeCompetitorCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
						        	change: loadProducts
						        }
      	                    }]
				        },/*{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryWindowOpportunity.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryWindowOpportunity.product.selected" />'
	      	        	},{
	      	        		colspan:2,
	      	        		xtype: 'container',
      	                    layout: 'fit',
      	                    height: 100,
      	                    width: 500,
      	                    defaultType: 'textfield',
      	                    defaults:{
      	                    	labelAlign:'right',
      	                    	margin:'0 0 0 10'
      	                    },
      	                    items: [{
      	                    	xtype: 'itemselector',
	      	                    name: 'products',
					            id: 'products',
					            hideLabel: true,
					            blankText: '<bean:message key="queryWindowOpportunity.product.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeProducts,
					            displayField: 'prodDesc',
					            valueField: 'prodId',
					            allowBlank: false,
					            //values: [userCtrSk],
					            listeners:{
					            	render: function(){
					            		this.clearValue();
					            	},
					            	change: function(){
					            		if(this.getValue().length >0){
					            			Ext.getCmp('showReport').enable();
					            		}else{
					            			Ext.getCmp('showReport').disable();
					            		}
					            	}
					            }
      	                    }]
				        }*/
				        {
							//colspan:2,
							xtype: 'combo',
							labelWidth:80,
							width: 220,
							fieldLabel: '<bean:message key="queryWindowOpportunity.product.available"/>',
							id:'products',
							name: 'products',
							allowBlank:false,
							store: 	storeProducts,
							queryMode: 'local',
							displayField: 'prodDesc',
							valueField: 'prodId',
							editable:false,
							forceSelection: true,
							emptyText: '<bean:message key="combo.emptyText"/>',
				            listeners:{
				            	change: function(){
				            		if(!Ext.isEmpty(this.getValue())){
				            			Ext.getCmp('showReport').enable();
				            		}else{
				            			Ext.getCmp('showReport').disable();
				            		}
				            	}
				            }
	      	        	},{
	      	              	labelWidth:50,
	      	              	width: 225,
					    	xtype: 'combo',
					    	fieldLabel: '<bean:message key="queryWindowOpportunity.offerer"/>',
	      	              	blankText:'<bean:message key="queryWindowOpportunity.offerer.required"/>',
	      	                id:'offererCountries',
	      	                name: 'offererCountries',
	      	                allowBlank:false,
	      	              	store: storeOffererCountries,
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrId',
					        editable: false,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 100,
					        	afterrender:function(){
					        		new Ext.util.DelayedTask().delay(300,function(){
					        			Ext.getCmp('offererCountries').setValue(userCtrId);
					        		});
					        	},
					        	change: loadCIFOB
					        }
	      	            },{
	      	        		xtype: 'container',
	      	        		layout: 'vbox',
      	                    height: 150,
      	                  	width: 250,
      	                    //width: 500,
      	                    defaultType: 'numberfield',
      	                  	defaults:{
    	                    	labelAlign:'right',
    	                    	margin:'10 0 0 10',
    	                    	width:150,
    	                    	minValue: 0,
    	                    	disabled:true,
    	                    	disabled:true,
 		      	                allowBlank:false,
 		      	            	hideTrigger: true,
 								keyNavEnabled: false,
 		      	              	mouseWheelEnabled: false
    	                    },
      	                  	items: [{
     	                    	xtype: 'numberfield',
   	                        	fieldLabel: '<bean:message key="queryWindowOpportunity.farmPrice"/>',
   		      	                id: 'farmPrice',
   		      	                name: 'preciosFinca',
   		      	              	blankText: '<bean:message key="queryWindowOpportunity.farmPrice.required" />'
      	                    },{
      	                    	xtype: 'numberfield',
   	                        	fieldLabel: '<bean:message key="queryWindowOpportunity.packagingCost"/>',
   		      	                id: 'packagingCost',
   		      	                name: 'costoEmpaque',
   		      	              	blankText: '<bean:message key="queryWindowOpportunity.packagingCost.required" />'
      	                    },{
      	                    	xtype: 'numberfield',
   	                        	fieldLabel: '<bean:message key="queryWindowOpportunity.freightCost"/>',
   		      	                id: 'freightCost',
   		      	                name: 'costoFlete',
   		      	              	blankText: '<bean:message key="queryWindowOpportunity.freightCost.required" />'
      	                    }]
      	                },{
	      	        		xtype: 'container',
	      	        		layout: 'vbox',
      	                    height: 150,
      	                    //width: 500,
      	                    defaultType: 'numberfield',
      	                  	defaults:{
      	                    	labelAlign:'right',
      	                    	margin:'10 0 0 10',
      	                    	width:150,
      	                    	minValue: 0,
      	                    	disabled:true,
      	                    	disabled:true,
   		      	                allowBlank:false,
   		      	            	hideTrigger: true,
   								keyNavEnabled: false,
   		      	              	mouseWheelEnabled: false
      	                    },
      	                    items: [{
      	                    	xtype: 'numberfield',
   	                        	fieldLabel: '<bean:message key="queryWindowOpportunity.exportCost"/>',
   		      	                id: 'exportCost',
   		      	                name: 'costoTramite',
   		      	              	blankText: '<bean:message key="queryWindowOpportunity.exportCost.required" />'
      	                    },{
      	                    	xtype: 'numberfield',
   	                        	fieldLabel: '<bean:message key="queryWindowOpportunity.percentageGain"/>',
   		      	                id: 'percentageGain',
   		      	                name: 'porcentajeGanancia',
   		      	              	blankText: '<bean:message key="queryWindowOpportunity.percentageGain.required" />'
      	                    }]
	      	            },{
	        	            colspan:2,
	        	            id:'showReport',
	        	            disabled:true,
	        	            xtype: 'button',
	      	            	text: '<bean:message key="queryWindowOpportunity.showReport" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('insert');
									var form = this.up('form.panel');
									
									
									
									if (form.getForm().isValid()){
										form._doCustomSubmit();
										new Ext.util.DelayedTask().delay(2000,function(){if(m)m.close();});
									}
								},
								render:function(){
									this.setPosition(this.up('form.panel').getWidth()/2 - this.width/2 - 20, 0);
								}
							}
	        	        }]
	      	        }]//Items Form.Panel
				});
				
				
				cstFormPanel.reportMode();
				
				
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryWindowOpportunity.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryWindowOpportunity.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>