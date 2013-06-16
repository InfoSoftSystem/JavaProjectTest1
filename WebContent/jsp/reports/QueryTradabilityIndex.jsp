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
		Este reporte evalúa la participación de las exportaciones o las importaciones (exportaciones netas: exportaciones-importaciones) 
		en el consumo aparente (producción+importaciones-exportaciones) de un país. Es importante ver la evolución del índice en un 
		período de tiempo, a fin de evaluar la ganancia o pérdida de la capacidad exportadora de un bien. 
		<br/>
		<br/>
		<b>Fuente de datos</b>:Datos proporcionados por los SIMAs
		<br/>
		<br/>
		<b>Fuente metodológica</b>:AGRONET Colombia
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
		      	
				Ext.define('modelProdId', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'msg', type:'string'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ]
				});
				
				//************ GRID
				Ext.define('modelGridPeriods', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'initialDate', type: 'int'},
				        {name: 'finalDate', type: 'int'}
				    ]
				});
				
				Ext.define('modelYearSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'yearSk', type: 'int', mapping:'yearsk'}
				    ],
				    idProperty: 'yearSk'
				});
				
				
				
				var storeYears = Ext.create('Ext.data.Store', {
			        id: 'storeYears',
			        //pageSize: 50,
			        model: 'modelYearSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'queryTradabilityIndexAction.do?accion=load&store=loadYears',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }//,
			            // sends single sort as multi parameter
			            //simpleSortMode: true
			        },
		        	sorters: [{
			            property: 'yearSk',
			            direction: 'DESC'
			        }],
			        autoLoad: true
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
			            url: 'queryTradabilityIndexAction.do?accion=load&store=loadCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }//,
			            // sends single sort as multi parameter
			            //simpleSortMode: true
			        },sorters: [{
			            property: 'ctrDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				/*var storeComboProducts = Ext.create('Ext.data.Store', {
			        id: 'storeComboProducts',
			        //pageSize: 50,
			        model: 'modelProdId',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'queryTradabilityIndexAction.do?accion=load&store=loadProducts',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },sorters: [{
			            property: 'prodDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });/**/
				
				var storeProducts = Ext.create('Ext.data.Store', {
			        id: 'storeProducts',
			        //pageSize: 50,
			        model: 'modelProdId',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        // allow the grid to interact with the paging scroller by buffering
			        //buffered: true,
			        proxy: {
			            // load using script tags for cross domain, if the data in on the same domain as
			            // this page, an HttpProxy would be better
			            type: 'ajax',
			            url: 'queryTradabilityIndexAction.do?accion=load&store=loadProducts',
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
				
				function loadYears(obj, newValue, oldValue){
					Ext.getCmp('initialDate').enable();
					Ext.getCmp('finalDate').enable();
					//Ext.getCmp('addRecord').enable();
					
					var tmpStore = storeYears;
	        		
					var m = wait();
					
					Ext.getCmp('periods').setValue('');
					//Ext.getCmp('gridPeriods').getStore().removeAll();
					
					/*var tmpFinalDate = Ext.getCmp('finalDate').getValue();
					var tmpInitialDate = Ext.getCmp('initialDate').getValue();*/
					
					Ext.getCmp('finalDate').setValue('');
					Ext.getCmp('initialDate').setValue('');
					
					tmpStore.load({
						scope:this,
						callback:function(records, operation, success){
							m.close();
							
							if(!success){
								if(records){
									Ext.get('globalErrors').update(records[0].get('msg'));
									showGlobalErrors();
								}
							}
							
							//loadCountries();
						}
					});
				
				}
				
				function loadProducts(){
					
					if(Ext.getCmp('countries').toField.store.getCount()>0){
						
						var tmpStore = null;
						
						tmpStore = storeProducts;
						
		        		tmpStore.proxy.extraParams={
	        				periods: Ext.getCmp('periods').getValue(),
			                countries: Ext.getCmp('countries').getSubmitValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								var tmpComponent = Ext.getCmp('products');
								
								tmpComponent.clearValue();
								
								if(tmpComponent.store.getCount()>0){
									tmpComponent.fromField.store.removeAll();
								}
								
								if(!success){
									if(records){
										Ext.get('globalErrors').update(records[0].get('msg'));
										showGlobalErrors();
									}
								}else{
									tmpComponent.store.add(tmpStore.getRange());
									tmpComponent.fromField.store.add(tmpStore.getRange());
								}
							
							}
						});
					}else{
					
						var tmpComponent = Ext.getCmp('products');
						
						tmpComponent.clearValue();
						
						if(tmpComponent.store.getCount()>0){
							//tmpComponent.store.removeAll();
							tmpComponent.fromField.store.removeAll();
						}
					}
				}
				
				function getPeriods(){
					var periods = '';
					/*var strPeriods = Ext.getCmp('gridPeriods').getStore();
					strPeriods.sort('initialDate','ASC');
					
					for(var i =0, list = strPeriods.getRange(); i<list.length; i++){
						var record = strPeriods.getAt(i);
						
						if(i>0){
							periods= periods+',';
						}
						
						periods = periods + record.get('initialDate') + '-' + record.get('finalDate');
					}*/
					
					periods = Ext.getCmp('initialDate').getValue()+'-'+Ext.getCmp('finalDate').getValue();
					return periods;
				}
				
				function loadCountries(){
					//var strPeriods = Ext.getCmp('gridPeriods').getStore();
					
					//if(strPeriods.getCount()>0){
					if(Ext.getCmp('initialDate').validate() && Ext.getCmp('finalDate').validate() ){
						Ext.getCmp('periods').setValue(getPeriods());
						
						var tmpStore = storeCountries;//Ext.getStore('storeCountries');
		        		
		        		tmpStore.proxy.extraParams={
			                periods: Ext.getCmp('periods').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								if(!success){
									if(records){
										Ext.get('globalErrors').update(records[0].get('msg'));
										showGlobalErrors();
									}
								}else{
									var tmpCountries = Ext.getCmp('countries');
									tmpCountries.clearValue();
									
									//if(tmpCountries.store.getCount()>0){
										//tmpCountries.store.removeAll();
										tmpCountries.fromField.store.removeAll();
									//}
									
									tmpCountries.store.add(storeCountries.getRange());
									tmpCountries.fromField.store.add(storeCountries.getRange());
								}
							}
						});
					}else{
						var tmpComponent = Ext.getCmp('countries');
						
						tmpComponent.clearValue();
						
						if(tmpComponent.store.getCount()>0){
							//tmpComponent.store.removeAll();
							tmpComponent.fromField.store.removeAll();
						}
					}
				}
				
				var delayLoadCountries = new Ext.util.DelayedTask(function(){
        			loadCountries();
        		});
				
				/*
				var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
			        //clicksToMoveEditor: 1,
			        autoCancel: true,
			        listeners:{
			        	cancelEdit: function(){
			        		Ext.getCmp('addRecord').enable();
			        	},
			        	validateedit: function(editor, e){
			        		var initialDate = Ext.getCmp('initialDate').getValue();
			        		var finalDate = Ext.getCmp('finalDate').getValue();
			        		
			        		if(initialDate+1 != finalDate){
				        		e.cancel = true;
		        				return false;
			        		}
			        		
			        		if(true==false){
		        				e.cancel = true;
		        				
		        				return false;
			        		}else{
			        		    e.cancel = false;
			        		}
			        		
			        		return true;
			        	},
			        	beforeedit:function(editor, e){
			        		var initialDate = e.record.get('initialDate'); //ID seleccionado
			        		//var finalDate= e.record.get('finalDate');
			        		
			        		new Ext.util.DelayedTask().delay(50,function(){
			        			Ext.getCmp('initialDate').setValue(initialDate);
                    		});
			        	},
			        	edit: function(editor, e){
			        		var sm = Ext.getCmp('gridPeriods').getSelectionModel();
			        		var initialDate = e.record.get('initialDate'); //ID seleccionado
			        		var finalDate = e.record.get('finalDate');
			        		
			        		var strInitialDate = Ext.getCmp('initialDate').getStore();//Store donde se encuentran los demas campos
			        		var strFinalDate = Ext.getCmp('finalDate').getStore();
			        		
			        		strInitialDate.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strInitialDate.filter([
			        			{id: 'yearSk', property: 'yearSk', value: initialDate, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strInitialDate.getCount() > 0 ){
				        		var record = strInitialDate.getAt(0);
				        		sm.getSelection()[0].set('yearSk', record.get('yearSk'));
			        		}
			        		
			        		strInitialDate.clearFilter();
			        		
			        		//-------------------------------------
			        		
			        		strFinalDate.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strFinalDate.filter([
			        			{id: 'yearSk', property: 'yearSk', value: finalDate, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strFinalDate.getCount() > 0 ){
				        		var record = strFinalDate.getAt(0);
				        		sm.getSelection()[0].set('yearSk', record.get('yearSk'));
			        		}
			        		
			        		strFinalDate.clearFilter();
			        		
			        		//---------------------------------------------------------------------
			        		
			        		sm.getSelection()[0].commit();
			        		Ext.getCmp('addRecord').enable();
			        		loadCountries();
			        	}
			        }
			    });/**/
				
				
				// create the grid and specify what field you want
			    // to use for the editor at each column.
			    /*
			    var gridPeriods = Ext.create('Ext.grid.Panel', {
			    	colspan:2,
			    	id:'gridPeriods',
			    	plugins: [rowEditing],
			    	height: 150,
			    	width: 250,
			    	//title: '<bean:message key="queryTradabilityIndex.periods.selected"/>',
			    	store: Ext.create('Ext.data.Store', {
			    		id:'storeGridPeriods',
				        // destroy the store if the grid is destroyed
				        autoDestroy: true,
				        model: 'modelGridPeriods',
				        proxy: {
				            type: 'memory',
				            totalProperty: 'totalCount'
				        }	
				    }),
				    tbar: [{
			            text: '<bean:message key="queryTradabilityIndex.addPeriod"/>',
			            iconCls: 'option-item-add',
			            id: 'addRecord',
			            handler : function() {
			                rowEditing.cancelEdit();
			                //rowEditing.completeEdit();
			                Ext.getCmp('addRecord').disable();

			                // Create a record instance through the ModelManager
			                var r = Ext.ModelManager.create({
			                    initialDate:null,
			                    finalDate: null
			                }, 'modelGridPeriods');

			                this.up('grid.panel').store.insert(0, r);
			                rowEditing.startEdit(0, 0);
			            }
			        }, {
			            itemId: 'removeRecord',
			            id: 'removeRecord',
			            text: '<bean:message key="queryTradabilityIndex.delPeriod"/>',
			            iconCls: 'option-item-delete',
			            handler: function() {
			            	Ext.getCmp('addRecord').enable();
			                
			            	var sm = this.up('grid.panel').getSelectionModel();
			                rowEditing.cancelEdit();
			                
			                this.up('grid.panel').store.remove(sm.getSelection());
			                
			                if(Ext.getStore('storeGridPeriods').getCount()==0){
			                	Ext.getCmp('showReport').disable();
			                }
			                //sm.select(0);
			                loadCountries();
			            },
			            disabled: true
			        }],
			        columns: [{
			            header: '<bean:message key="queryTradabilityIndex.initialDate"/>',
			            dataIndex: 'initialDate',
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="queryTradabilityIndex.initialDate"/>',
	      	                id:'initialDate',
	      	                //name: 'initialDate',
	      	                allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryTradabilityIndex.initialDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        forceSelection:true,
					        editable: false,
					        listeners:{
					        	change:function(obj, newValue, oldValue){
					        		Ext.getCmp('finalDate').setValue(newValue+1);
					        	}
					        }
					    }
			         }, {
			            header: '<bean:message key="queryTradabilityIndex.finalDate"/>',
			            dataIndex: 'finalDate',
			            //width: 160,
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="queryTradabilityIndex.finalDate"/>',
	      	                id:'finalDate',
	      	                //name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryTradabilityIndex.finalDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        forceSelection:true,
					        editable: false,
					        validator:function(value){
	      	              		var tmpSup = Ext.getCmp('initialDate').getValue();
	      	              		
	      	              		if(tmpSup+1!=value){
	      	              			return '<bean:message key="queryTradabilityIndex.finalDate.minor"/>';
	      	              		}
								return true;
	      	              	}
					    }
			        }],
			        listeners: {
			            'selectionchange': function(view, records) {
			                this.down('#removeRecord').setDisabled(!records.length);
			            },
						render:function(){
							this.setPosition(this.up('form.panel').getWidth()/2 - this.width/2 - 20, 0);
						}
			        }
			    });
				/**/
				
				
				
			    var itemSelectorProducts = Ext.create('Ext.container.Container',{
	  	        	id:'containerProducts',
	  	        	colspan:2,
  	        		//xtype: 'container',
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
			            hideMode:'offsets',
			            hideLabel: true,
			            blankText: '<bean:message key="queryTradabilityIndex.product.selected.required"/>',
			            buttons:["add", "remove"],
			            store: storeProducts,
			            displayField: 'prodDesc',
			            valueField: 'prodId',
			            allowBlank: true,
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
		        });
				
				//************************************************************************************************
				
				var cstFormPanel = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					//url:'sizeAction.do',
					url:'reportAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					width: 550,
					title: '<bean:message key="queryTradabilityIndex.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'periods',
      	        		name:'periods'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value: '12_Indice_Transabilidad.jrxml'
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
	      	            items :[{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryTradabilityIndex.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryTradabilityIndex.initialDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        forceSelection:true,
					        editable: false,
					        listeners:{
					        	change:function(obj, newValue, oldValue){
					        		//Ext.getCmp('finalDate').setValue(newValue+1);
					        		
					        		delayLoadCountries.delay(50);
					        	}
					        }
					    },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryTradabilityIndex.finalDate"/>',
	      	                id:'finalDate',
	      	                name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryTradabilityIndex.finalDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        forceSelection:true,
					        editable: false,
					        /*validator:function(value){
	      	              		var tmpSup = Ext.getCmp('initialDate').getValue();
	      	              		
	      	              		if(tmpSup+1!=value){
	      	              			return '<bean:message key="queryTradabilityIndex.finalDate.minor"/>';
	      	              		}
								return true;
	      	              	},*/
	      	              	listeners:{
	      	              		change: function(){
	      	              			delayLoadCountries.delay(100);
	      	              		}
	      	              	}
					    },{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryTradabilityIndex.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryTradabilityIndex.countries.selected" />'
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
	      	                    name: 'offererCountries',
					            id: 'countries',
					            hideLabel: true,
					            blankText: '<bean:message key="queryTradabilityIndex.countries.selected.required"/>',
					            buttons:["add", "remove"],
						        store: storeCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
					            	change: loadProducts
						        }
      	                    }]
				        },{
	      	        		xtype:'label',
	      	        		id:'labelProduct1',
	      	        		hideMode:'offset',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryTradabilityIndex.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		id:'labelProduct2',
	      	        		hideMode:'offset',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryTradabilityIndex.product.selected" />'
	      	        	}, 
	      	        	itemSelectorProducts,
	      	        	{
	        	            colspan:2,
	        	            id:'showReport',
	        	            xtype: 'button',
	        	            disabled:true,
	      	            	text: '<bean:message key="queryTradabilityIndex.showReport" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('generateReport');
									var form = this.up('form.panel');
									
									
									
									if (form.getForm().isValid() && validations()){
										var tmpStore = Ext.getCmp('countries').toField.boundList.getStore();
										var total = tmpStore.getCount();
										var ctrDesc = '';
										
										for(var i =0; i<total; i=i+1){
											var reg = tmpStore.getAt(i);
											if(i>0){
												ctrDesc =ctrDesc + ", ";
											}
											ctrDesc = ctrDesc + reg.get('ctrDesc');
										}
										
										Ext.getCmp('countriesDesc').setValue(ctrDesc);
										
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
				
				
				function validations(){
					var tmpMsg = validateRange();
					
					if(tmpMsg==''){
               			return true;
               		}else{
               			//tmpMsg = tmpMsg+'<br/><bean:message key="global.warning.confirmation"/>';
               			
	               		Ext.Msg.show({
	           				title: '<bean:message key="global.error.title" />',
	           				msg: tmpMsg,
	           				icon: Ext.Msg.ERROR,
	           				buttons: Ext.Msg.OK,
	           				modal: true,
	           				animateTarget: 'globalErrors'
	           			});
               		}
           			return false;
				}
				
				function validateRange(){
           			var initialDate;
               		var tmpMsg='';
               		var finalDate; 
               		
               		initialDate = Ext.getCmp('initialDate').getValue();
               		finalDate = Ext.getCmp('finalDate').getValue();
               		
               		if(initialDate > finalDate){
               			tmpMsg = tmpMsg+'<bean:message key="queryTradabilityIndex.finalDate.minor"/>';
               		}
               		
               		return tmpMsg;
				}
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryTradabilityIndex.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryTradabilityIndex.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>