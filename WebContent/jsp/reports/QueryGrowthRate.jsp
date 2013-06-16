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
		Tasa de crecimiento promedio anual sigue el patrón de crecimiento exponencial.
		<br/>
		<br/>
		<b>Período</b>: se pueden seleccionar varios períodos pero se sugiere que cada período sea mayor de cinco años
		<br/>
		<br/>
		<b>Fuente de datos</b>: 
		Datos de comercio (exportaciones e importaciones) y producción (área cosechada, volumen de producción) proporcionados por SIMAs
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
				
				Ext.define('modelGrid', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'prodId', type: 'int'},
				        {name: 'prodDesc', type: 'string'},
				        {name: 'varId', type: 'int'},
				        {name: 'varDesc', type: 'string'},
				        {name: 'quaId', type: 'int'},
				        {name: 'quaDesc', type: 'string'},
				        {name: 'sizeId', type: 'int'},
				        {name: 'sizeDesc', type: 'string'}
				    ]
				});
				
				Ext.define('modelYearSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'yearSk', type: 'int', mapping:'yearsk'}
				    ],
				    idProperty: 'yearSk'
				});
				
				Ext.define('modelVarId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'msg', type:'string'},
				        {name: 'prodId',  type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'},
				        {name: 'varId',  type: 'int', mapping:'varid'},
				        {name: 'varDesc',  type: 'string', mapping:'vardesc'}
				    ],
				    idProperty: 'varId'
				});
				
				Ext.define('modelQuaId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'msg', type:'string'},
				        {name: 'quaId', type: 'int', mapping:'quaid'},
						{name: 'quaDesc', type: 'string', mapping: 'quadesc'}
				    ],
		            idProperty: 'quaId'
				});
				
				Ext.define('modelSizeId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'msg', type:'string'},
				        {name: 'sizeId', type: 'int', mapping:'sizeid'},
				        {name: 'sizeDesc', type: 'string', mapping: 'sizedesc'}
				    ],
		            idProperty: 'sizeId'
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
			            url: 'queryGrowthRateAction.do?accion=load&store=loadYears',
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
			        autoLoad: false
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
			            url: 'queryGrowthRateAction.do?accion=load&store=loadCountries',
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
				
				var storeComboProducts = Ext.create('Ext.data.Store', {
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
			            url: 'queryGrowthRateAction.do?accion=load&store=loadProducts',
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
			            url: 'queryGrowthRateAction.do?accion=load&store=loadProducts',
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
					Ext.getCmp('addRecord').enable();
					
					var tmpStore = storeYears;
	        		var variable = Ext.getCmp('chkVariable').getValue().variable;
					
	        		if(variable=='PromedioXUnidad'){
	        			Ext.getCmp('gridProd').setHeight(200);
						Ext.getCmp('containerProducts').setHeight(0);//100
						Ext.getCmp('labelProduct1').setHeight(0);//20
						Ext.getCmp('labelProduct2').setHeight(0);//20
						
	        		}else{
	        			Ext.getCmp('gridProd').setHeight(0);
						Ext.getCmp('containerProducts').setHeight(100);//100
						Ext.getCmp('labelProduct1').setHeight(20);//20
						Ext.getCmp('labelProduct2').setHeight(20);//20
	        		}
	        		
	        		tmpStore.proxy.extraParams={
		                variable: variable
		            };
	        		
	        		if("PromedioXUnidad"==variable){
	        			Ext.getCmp('report').setValue('08_tasa_crecimientoPrices.jrxml');
	        		}else{
	        			Ext.getCmp('report').setValue('08_tasa_crecimientoCommProd.jrxml');
	        		}
	        		
					var m = wait();
					
					Ext.getCmp('periods').setValue('');
					Ext.getCmp('gridPeriods').getStore().removeAll();
					
					/*var tmpFinalDate = Ext.getCmp('finalDate').getValue();
					var tmpInitialDate = Ext.getCmp('initialDate').getValue();*/
					
					//Ext.getCmp('finalDate').setValue('');
					//Ext.getCmp('initialDate').setValue('');
					
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
							
							loadCountries();
							/*if(initialDate){
								Ext.getCmp('initialDate').setValue(tmpInitialDate);
							}
							
							if(finalDate){
								Ext.getCmp('finalDate').setValue(tmpFinalDate);
								Ext.getCmp('finalDate').fireEvent('change');
							}*/
						}
					});
				
				}
				
				function loadProducts(){
					var variable = Ext.getCmp('chkVariable').getValue().variable;
					var storeGridProducts = Ext.getStore('storeGridProducts');
					
					if(Ext.getCmp('countries').toField.store.getCount()>0){
						
						var tmpStore = null;
						
						if('PromedioXUnidad'==variable){
							tmpStore = storeComboProducts;
						}else{
							tmpStore = storeProducts;
						}
		        		
		        		tmpStore.proxy.extraParams={
	        				periods: Ext.getCmp('periods').getValue(),
			                countries: Ext.getCmp('countries').getSubmitValue(),
			                variable: Ext.getCmp('chkVariable').getValue().variable
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								if('PromedioXUnidad'==variable){
									storeGridProducts.removeAll();
									Ext.getCmp('showReport').disable();
									
									if(tmpStore.getCount()>0){
										Ext.getCmp('addRecordProduct').enable();
									}else{
										Ext.getCmp('addRecordProduct').disable();
									}
								}else{
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
							}
						});
					}else{
						
						if('PromedioXUnidad'==variable){
							storeGridProducts.removeAll();
							Ext.getCmp('addRecordProduct').disable();
							Ext.getCmp('showReport').disable();
						}else{
							var tmpComponent = Ext.getCmp('products');
							
							tmpComponent.clearValue();
							
							if(tmpComponent.store.getCount()>0){
								//tmpComponent.store.removeAll();
								tmpComponent.fromField.store.removeAll();
							}
						}
					}
				}
				
				function getPeriods(){
					var periods = '';
					var strPeriods = Ext.getCmp('gridPeriods').getStore();
					strPeriods.sort('initialDate','ASC');
					
					for(var i =0, list = strPeriods.getRange(); i<list.length; i++){
						var record = strPeriods.getAt(i);
						
						if(i>0){
							periods= periods+',';
						}
						
						periods = periods + record.get('initialDate') + '-' + record.get('finalDate');
					}
					return periods;
				}
				
				function loadCountries(){
					var strPeriods = Ext.getCmp('gridPeriods').getStore();
					
					if(strPeriods.getCount()>0){
						Ext.getCmp('periods').setValue(getPeriods());
						
						var tmpStore = storeCountries;//Ext.getStore('storeCountries');
		        		
		        		tmpStore.proxy.extraParams={
			                periods: Ext.getCmp('periods').getValue(),
			                variable: Ext.getCmp('chkVariable').getValue().variable
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
				
				function loadVarieties(){
					
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue())){
						var varSk = Ext.getCmp('varId');
		        		
		        		varSk.enable();
		        		//var tmp = varSk.getValue();
						var tmpStore = Ext.getStore('storeVarieties');
		        		
		        		tmpStore.proxy.extraParams={
	        				periods: Ext.getCmp('periods').getValue(),
			                variable: Ext.getCmp('chkVariable').getValue().variable,
			                countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue()
			            };
		        		
						var m = wait();
						
						//Ext.getCmp('varId').setValue('');
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								//varSk.setValue(tmp);
								if(!Ext.isEmpty(globalVarId)){
									varSk.setValue(globalVarId);
									globalVarId = null;
									Ext.getCmp('varId').focus();
								}
								
								if(tmpStore.getCount() == 0){
									loadQualities();
								}
							}
						});
					}
				}
				
				function loadQualities(){
					
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue())){
						var quaId = Ext.getCmp('quaId');
		        		
		        		var tmpStore = Ext.getStore('storeQualities');
		        		
		        		tmpStore.proxy.extraParams={
	        				periods: Ext.getCmp('periods').getValue(),
			                variable: Ext.getCmp('chkVariable').getValue().variable,
			                countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue(),
			                varId: Ext.getCmp('varId').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								if(!Ext.isEmpty(globalQuaId)){
									quaId.setValue(globalQuaId);
									globalQuaId = null;
								}
								
								Ext.getCmp('quaId').focus();
							}
						});
					}
				}
				
				function loadSizes(){
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue()) && !Ext.isEmpty(Ext.getCmp('quaId').getValue())){
						var sizeId = Ext.getCmp('sizeId');
		        		
		        		var tmpStore = Ext.getStore('storeSizes');
		        		
		        		tmpStore.proxy.extraParams={
	        				periods: Ext.getCmp('periods').getValue(),
			                variable: Ext.getCmp('chkVariable').getValue().variable,
			                countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue(),
			                varId: Ext.getCmp('varId').getValue(),
			                quaId: Ext.getCmp('quaId').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								if(!Ext.isEmpty(globalSizeId)){
									sizeId.setValue(globalSizeId);
									globalSizeId = null;
								}
								
								Ext.getCmp('sizeId').focus();
							}
						});
					}
				}
				
				
				
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
			        		
			        		if(finalDate < initialDate+2){
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
			        		/**/
			        		
			        		sm.getSelection()[0].commit();
			        		Ext.getCmp('addRecord').enable();
			        		loadCountries();
			        	}
			        }
			    });
				
				
				// create the grid and specify what field you want
			    // to use for the editor at each column.
			    var gridPeriods = Ext.create('Ext.grid.Panel', {
			    	colspan:2,
			    	id:'gridPeriods',
			    	plugins: [rowEditing],
			    	height: 150,
			    	width: 250,
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
			            text: '<bean:message key="queryGrowthRate.addPeriod"/>',
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
			            }, 
			            disabled: true
			        }, {
			            itemId: 'removeRecord',
			            id: 'removeRecord',
			            text: '<bean:message key="queryGrowthRate.delPeriod"/>',
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
			            header: '<bean:message key="queryGrowthRate.initialDate"/>',
			            dataIndex: 'initialDate',
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                id:'initialDate',
	      	                //name: 'initialDate',
	      	                allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryGrowthRate.initialDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        disabled:true,
					        forceSelection:true,
					        editable: false,
					        listeners:{
					        	change:function(obj, newValue, oldValue){
					        		Ext.getCmp('finalDate').setValue(newValue+2);
					        	}
					        }
					    }
			         }, {
			            header: '<bean:message key="queryGrowthRate.finalDate"/>',
			            dataIndex: 'finalDate',
			            //width: 160,
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="queryGrowthRate.finalDate"/>',
	      	                id:'finalDate',
	      	                //name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryGrowthRate.finalDate.required"/>',
					        store: storeYears,
					        queryMode: 'local',
					        displayField: 'yearSk',
					        valueField: 'yearSk',
					        disabled:true,
					        forceSelection:true,
					        editable: false,
					        validator:function(value){
	      	              		var tmpSup = Ext.getCmp('initialDate').getValue();
	      	              		
	      	              		if(value < tmpSup+2){
	      	              			return '<bean:message key="queryGrowthRate.finalDate.minor"/>';
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
				
				
				
				
				
			    var rowEditingProducts = Ext.create('Ext.grid.plugin.RowEditing', {
			        //clicksToMoveEditor: 1,
			        autoCancel: true,
			        listeners:{
			        	/*validateedit:function(editor, e, eOpts){
			        		alert(e.record.get('prodDesc'));
			        	},//*/
			        	cancelEdit: function(){
			        		Ext.getCmp('addRecordProduct').enable();
			        	},
			        	validateedit: function(editor, e){
			        		var varId = Ext.getCmp('varId');
			        		var strVarSk = varId.getStore();
			        		
			        		if(strVarSk.getCount()>0 && (Ext.isEmpty(varId.getValue()) || varId.getValue() ==0)){
		        				e.cancel = true;
		        				
		        				Ext.Msg.show({
			           				title: '<bean:message key="global.error.title" />',
			           				msg: '<bean:message key="price.prdVarSk.required"/>',
			           				icon: Ext.Msg.ERROR,
			           				buttons: Ext.Msg.OK,
			           				modal: true,
			           				animateTarget: 'globalErrors'
			           			});
		        				
		        				return false;
			        		}else{
			        		    e.cancel = false;
			        		}
			        		
			        		return true;
			        	},
			        	beforeedit:function(editor, e){
			        		var prodId = e.record.get('prodId'); //ID seleccionado
			        		var varId = e.record.get('varId');
			        		var quaId = e.record.get('quaId');
			        		var sizeId = e.record.get('sizeId');
			        		
			        		new Ext.util.DelayedTask().delay(50,function(){
			        			globalVarId = varId;
			        			globalQuaId = quaId;
			        			globalSizeId = sizeId;
			        			
			        			Ext.getCmp('prodId').setValue(prodId);
                    		});
			        	},
			        	edit: function(editor, e){
			        		var prodId = e.record.get('prodDesc'); //ID seleccionado
			        		var varId = e.record.get('varDesc');
			        		var quaId = e.record.get('quaDesc');
			        		var sizeId = e.record.get('sizeDesc');
			        		
			        		var strProdId = Ext.getCmp('prodId').getStore();//Store donde se encuentran los demas campos
			        		var strVarId = Ext.getCmp('varId').getStore();
			        		var strQuaId = Ext.getCmp('quaId').getStore();
			        		var strSizeId = Ext.getCmp('sizeId').getStore();
			        		
			        		var sm = Ext.getCmp('gridProd').getSelectionModel();
			        		
			        		
			        		strProdId.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strProdId.filter([
			        			{id: 'prodId', property: 'prodId', value: prodId, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strProdId.getCount() > 0 ){
				        		var record = strProdId.getAt(0);
				        		sm.getSelection()[0].set('prodId', record.get('prodId'));
				        		sm.getSelection()[0].set('prodDesc', record.get('prodDesc'));
			        		}
			        		
			        		strProdId.clearFilter();
			        		
			        		//-------------------------------------
			        		
			        		strVarId.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strVarId.filter([
			        			{id: 'varId', property: 'varId', value: varId, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strVarId.getCount() > 0 ){
				        		var record = strVarId.getAt(0);
				        		sm.getSelection()[0].set('varId', record.get('varId'));
				        		sm.getSelection()[0].set('varDesc', record.get('varDesc'));
			        		}
			        		
			        		strVarId.clearFilter();
			        		
			        		//---------------------------------------------------------------------
			        		
			        		strQuaId.clearFilter();

			        		//Filtro el registro segun el ID proporcionado
			        		strQuaId.filter([
			        			{id: 'quaId', property: 'quaId', value: quaId, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strQuaId.getCount() > 0 ){
				        		var record = strQuaId.getAt(0);
				        		sm.getSelection()[0].set('quaId', record.get('quaId'));
				        		sm.getSelection()[0].set('quaDesc', record.get('quaDesc'));
			        		}
			        		
			        		strQuaId.clearFilter();
			        		
			        		//------------------------------------------------
			        		
			        		strSizeId.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strSizeId.filter([
			        			{id: 'sizeId', property: 'sizeId', value: sizeId, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strSizeId.getCount() > 0 ){
				        		var record = strSizeId.getAt(0);
				        		sm.getSelection()[0].set('sizeId', record.get('sizeId'));
				        		sm.getSelection()[0].set('sizeDesc', record.get('sizeDesc'));
			        		}
			        		
			        		strSizeId.clearFilter();//*/
			        		
			        		sm.getSelection()[0].commit();
			        		Ext.getCmp('showReport').enable();
			        		Ext.getCmp('addRecordProduct').enable();
			        	}
			        }
			    });
				
				
				// create the grid and specify what field you want
			    // to use for the editor at each column.
			    var gridProducts = Ext.create('Ext.grid.Panel', {
			    	colspan:2,
			    	id:'gridProd',
			    	plugins: [rowEditingProducts],
			    	height: 200,
			    	width: 500,
			    	title: '<bean:message key="queryGrowthRate.product.selected"/>',
			    	//renderTo: 'container',
			    	store: Ext.create('Ext.data.Store', {
			    		id:'storeGridProducts',
				        // destroy the store if the grid is destroyed
				        autoDestroy: true,
				        model: 'modelGrid',
				        proxy: {
				            type: 'memory',
				            totalProperty: 'totalCount'
				        }
				    }),
				    tbar: [{
			            text: '<bean:message key="queryPrices.addProduct"/>',
			            iconCls: 'option-item-add',
			            id: 'addRecordProduct',
			            handler : function() {
			                rowEditingProducts.cancelEdit();
			                //rowEditingProducts.completeEdit();
			                Ext.getCmp('addRecordProduct').disable();

			                // Create a record instance through the ModelManager
			                var r = Ext.ModelManager.create({
			                    prodId:null,
			                    prodDesc: '',
			                    varId: null,
			                    varDesc: '',
			                    quaId: null,
			                    quaDesc: '',
			                    sizeId: null,
			                    sizeDesc: ''
			                }, 'modelGrid');

			                this.up('grid.panel').store.insert(0, r);
			                rowEditingProducts.startEdit(0, 0);
			            }, 
			            disabled: true
			        }, {
			            itemId: 'removeRecordProduct',
			            id: 'removeRecordProduct',
			            text: '<bean:message key="queryPrices.delProduct"/>',
			            iconCls: 'option-item-delete',
			            handler: function() {
			            	Ext.getCmp('addRecordProduct').enable();
			                var sm = this.up('grid.panel').getSelectionModel();
			                rowEditingProducts.cancelEdit();
			                this.up('grid.panel').store.remove(sm.getSelection());
			                
			                if(Ext.getStore('storeGridProducts').getCount()==0){
			                	Ext.getCmp('showReport').disable();
			                }
			                //sm.select(0);
			            },
			            disabled: true
			        }],
			        columns: [{
			            header: '<bean:message key="price.prodId"/>',
			            dataIndex: 'prodDesc',
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="price.prodId"/>',
	      	                id:'prodId',
	      	                //name: 'prodId',
	      	                lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	allowBlank:false,
	      	              	width: 160,
					        store: storeComboProducts,
					        queryMode: 'local',
					        displayField: 'prodDesc',
					        valueField: 'prodId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 50,
					        	
					        	change: loadVarieties
					        }
	      	            }
			        }, {
			            header: '<bean:message key="price.varId"/>',
			            dataIndex: 'varDesc',
			            //width: 160,
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="price.varId"/>',
	      	                id:'varId',
	      	                //name: 'varId',
	      	              	allowBlank:true,
	      	              	disabled: true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeVarieties',
						        model: 'modelVarId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryGrowthRateAction.do?accion=load&store=loadVarieties',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'varId',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
					        queryMode: 'local',
					        displayField: 'varDesc',
					        valueField: 'varId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 80,
					        	specialkey: function(field, e){
				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
				                    if (e.getKey() == e.BACKSPACE) {
				                        if(Ext.isEmpty(field.getValue())){
				                    		new Ext.util.DelayedTask().delay(100,function(){
				                    			Ext.getCmp('prodId').fireEvent('change', Ext.getCmp('prodId'));
				                    		});
				                        }
				                    }
				                },
				                change: loadQualities
					        }
	      	            }
			        },{
			            header: '<bean:message key="price.quaSk"/>',
			            dataIndex: 'quaDesc',
			            width: 100,
			            //flex: 1,
			            field:{
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="price.quaSk"/>',
	      	                id:'quaId',
	      	                //name: 'quaSk',
	      	              	allowBlank:false,
					        store: Ext.create('Ext.data.Store', {
						        id: 'storeQualities',
						        model: 'modelQuaId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryGrowthRateAction.do?accion=load&store=loadQualities',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'quaId',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'quaDesc',
					        valueField: 'quaId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: loadSizes
					        }
					    }
			        },{
			            header: '<bean:message key="price.sizeSk"/>',
			            dataIndex: 'sizeDesc',
			            width: 100,
			            //flex: 1,
			            field:{
	      	              	xtype: 'combo',
	      	                //fieldLabel: '<bean:message key="price.sizeSk"/>',
	      	                id:'sizeId',
	      	                //name: 'sizeSk',
	      	              	allowBlank:false,
	      			      	store: Ext.create('Ext.data.Store', {
						        id: 'storeSizes',
						        model: 'modelSizeId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup: false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryGrowthRateAction.do?accion=load&store=loadSizes',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'sizeId',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'sizeDesc',
					        valueField: 'sizeId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>'
					    }
			        }],
			        listeners: {
			            'selectionchange': function(view, records) {
			                this.down('#removeRecordProduct').setDisabled(!records.length);
			            }
			        }
			    });
				
				
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
			            blankText: '<bean:message key="queryGrowthRate.product.selected.required"/>',
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
					title: '<bean:message key="queryGrowthRate.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'productFilters',
      	        		name:'productFilters'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'periods',
      	        		name:'periods'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'types',
      	        		name:'types'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value: ''
      	        	},{
      	        		xtype:'hidden',
      	        		id:'FormatPrint',
      	        		name:'FormatPrint',
      	        		value:'xlsx'
      	        	},{
      	            	//colspan: 2,
      	            	//rowspan:2,
      	            	xtype: 'radiogroup',
	      	            allowBlank: false,
	      	            msgTarget: 'side',
	      	            id:'chkVariable',
	      	            //autoFitErrors: false,
	      	            //anchor: '-18',
	      	            //layout: 'column',
	      	            columns: 3,
						vertical: true,
						labelWidth: 40,
	      	            fieldLabel: '<bean:message key="queryGrowthRate.variable" />',
	      	          	//height: 525,
	      	            //defaultType: 'container',
	      	            items: [/*{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.wholesalePrice" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'PromedioXUnidad',
  	                    	//checked: true,
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },*/{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.exportVolDollar" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ExportDollar',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.exportVolKilo" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ExportKilo',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.importVolDollar" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ImportDollar',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.importVolKilo" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ImportKilo',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.productionVol" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ProductionVol',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.harvestedArea" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'HarvestedArea',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    },{
  	                    	xtype: 'radiofield', 
  	                    	boxLabel: '<bean:message key="queryGrowthRate.variable.productionCost" />', 
  	                    	name: 'variable', 
  	                    	inputValue: 'ProductionCost',
  	                    	listeners:{
  	                    		change: loadYears
  	                    	}
  	                    }]
	      	        },{
	      	            id:'fldProducts',
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
	      	            items :[gridPeriods,
						{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryGrowthRate.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryGrowthRate.countries.selected" />'
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
	      	                    name: 'countries',
					            id: 'countries',
					            hideLabel: true,
					            blankText: '<bean:message key="queryGrowthRate.countries.selected.required"/>',
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
	      	        		text:'<bean:message key="queryGrowthRate.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		id:'labelProduct2',
	      	        		hideMode:'offset',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryGrowthRate.product.selected" />'
	      	        	}, 
	      	        	itemSelectorProducts,
	      	        	//gridProducts,
	      	        	{
	        	            colspan:2,
	        	            id:'showReport',
	        	            xtype: 'button',
	        	            disabled:true,
	      	            	text: '<bean:message key="queryGrowthRate.showReport" />',
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
										
										Ext.getCmp('types').setValue(Ext.getCmp('chkVariable').getValue().variable);
										

										//**************************************************
										//Set the data from the grid into a hidden
										var tmpValue='';
										tmpStore = Ext.getCmp('gridProd').getStore();
										total = tmpStore.getCount();
										
										for(var i =0; i<total; i=i+1){
											var reg = tmpStore.getAt(i);
											
											if(i>0){
												tmpValue =tmpValue+'\n union \n';
											}
											tmpValue = tmpValue + 
												'select prdvar_sk, \n'+
													reg.get('quaId')+' quaId, \n'+
													reg.get('sizeId')+ ' sizeId \n'+
												' from product_variety_dim \n'+
												' where prod_id = '+reg.get('prodId')+ '\n'+
												'   and var_id '+ ((Ext.isEmpty(reg.get('varId'))||reg.get('varId')=='0')?'is null':'= '+reg.get('varId'));
										}
										
										Ext.getCmp('productFilters').setValue(tmpValue);
										
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
				/*new Ext.util.DelayedTask().delay(500,function(){
					//Ext.getCmp('fldProducts').remove(itemSelectorProducts);
					//Ext.getCmp('products').hide();
					
					Ext.getCmp('gridProd').setHeight(0);
					Ext.getCmp('containerProducts').setHeight(0);//100
					Ext.getCmp('labelProduct1').setHeight(0);//20
					Ext.getCmp('labelProduct2').setHeight(0);//20
				});*/
				
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
               			tmpMsg = tmpMsg+'<bean:message key="queryGrowthRate.finalDate.minor"/>';
               		}
               		
               		return tmpMsg;
				}
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryGrowthRate.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryGrowthRate.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>