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
		El precio reportado es el del mercado mayorista principal de cada país. En caso, seleccione una consulta diaria, el reporte le dará el precio moda de ese día para los productos seleccionados. Si selecciona una consulta semanal, mensual y anual, los precios mostrados serán promedio. 
		<br/><br/>
		<b>Precio moda</b>: representa el rango de precio al que se vendió más del 50% del volumen comercializado en ese mercado. 
		<br/><br/>
		<b>Precio promedio</b>: A nivel diario se calcula a través del promedio del rango del precio moda. A nivel semanal y mensual, es el promedio de los promedios diarios en el período seleccionado.
		<br/><br/>
		<b>Fuente de Datos</b>:
		Sistemas de información de mercados agrícolas (SIMAs) de la región centroamericana y República Dominicana
		<br/><br/>
		<b>Fuente Metodológica</b>:
		El precio moda recopilado se basa en la metodología del AMS-USDA y los SIMAs, bajo asesoría del AMS están en el proceso de ajustar su metodología para tomar este dato con precisión
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
		        var globalPtypeId = null;
		        var globalVarId = null;
		        var globalQuaId = null;
    			var globalSizeId = null;
		        
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
		      	
				Ext.define('modelMonthSk', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'monthCode', type: 'string'},
						{name: 'monthId', type: 'int'},
						{name: 'monthDesc', type: 'string'}
				    ],
				    idProperty: 'yearSk'
				});
				
				Ext.define('modelYearSk', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'yearSk', type: 'int', mapping:'yearsk'}
				    ],
				    idProperty: 'yearSk'
				});
				
				

				//************ GRID
				Ext.define('modelGrid', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'prodId', type: 'int'},
				        {name: 'prodDesc', type: 'string'},
				        {name: 'ptypeId', type: 'int'},
				        {name: 'ptypeDesc', type: 'string'},
				        {name: 'varId', type: 'int'},
				        {name: 'varDesc', type: 'string'},
				        {name: 'quaId', type: 'int'},
				        {name: 'quaDesc', type: 'string'},
				        {name: 'sizeId', type: 'int'},
				        {name: 'sizeDesc', type: 'string'}
				    ]
				});
				

				Ext.define('modelProdId', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'msg', type:'string'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ],
				    idProperty: 'prodId'
				});
				
				Ext.define('modelPtypeId', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'msg', type:'string'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
						{name: 'ptypeId', type: 'int', mapping:'ptypeid'},
				        {name: 'ptypeDesc', type: 'string', mapping:'ptypedesc'}
				    ],
				    idProperty: 'ptypeId'
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
				
				
				var storeCountries = Ext.create('Ext.data.Store', {
			        id: 'storeCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryPricesAction.do?accion=load&store=loadCountries',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },sorters: [{
			            property: 'ctrDesc',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				function getInitialDate(){
					var levelAgregation = Ext.getCmp('chkLevelAgregation').getValue().levelAgregation;
					
					if('daily' == levelAgregation){
						return Ext.Date.format(Ext.getCmp('initialDate').getValue(),'Ymd');
						
					}else if('weekly' == levelAgregation){
						return Ext.Date.format(Ext.getCmp('initialWeek').getValue(),'Ymd');
						
					}else if('monthly' == levelAgregation){
						return Ext.getCmp('initialYear').getValue()+Ext.getCmp('initialMonth').getValue()+'01';
						
					}else if('yearly' == levelAgregation){
						return Ext.getCmp('initialYear').getValue()+'0101';
					}
				}
				
				function getFinalDate(){
					var levelAgregation = Ext.getCmp('chkLevelAgregation').getValue().levelAgregation;
					
					if('daily' == levelAgregation){
						return Ext.Date.format(Ext.getCmp('initialDate').getValue(),'Ymd');
						
					}else if('weekly' == levelAgregation){
						return Ext.Date.format(Ext.getCmp('finalWeek').getValue(),'Ymd');
						
					}else if('monthly' == levelAgregation){
						return Ext.getCmp('finalYear').getValue()+Ext.getCmp('finalMonth').getValue()+'31';
						
					}else if('yearly' == levelAgregation){
						return Ext.getCmp('finalYear').getValue()+'1231';
					}
				}
				
				function getProducts(){
					//Set the data from the grid into a hidden
					var tmpValue='';
					var tmpStore = Ext.getCmp('gridProd').getStore();
					var total = tmpStore.getCount();
					
					for(var i =0; i<total; i=i+1){
						var reg = tmpStore.getAt(i);
						
						if(i>0){
							tmpValue =tmpValue+'\n union \n';
						}
						tmpValue = tmpValue + 
							'select prdvar_sk, ptype_sk, \n'+
								reg.get('quaId')+' quaId, \n'+
								reg.get('sizeId')+ ' sizeId \n'+
							' from product_variety_dim a inner join product_type_dim b \n'+
							'				on a.prod_id = b.prod_id\n'+ 
							' where coalesce(b.prod_id, a.prod_id) = '+reg.get('prodId')+ '\n'+
							'   and a.var_id '+ ((Ext.isEmpty(reg.get('varId'))||reg.get('varId')=='0')?'is null':'= '+reg.get('varId')) +
							'   and current_date between coalesce(b.effective_date,a.effective_date) and coalesce(b.expiry_date, a.expiry_date)'+
							'   and coalesce(b.ptype_id, '+reg.get('ptypeId')+ ') = '+reg.get('ptypeId')+ '\n';
					}
					
					return tmpValue;
				}
				
				function isDateSelected(){
					if( !Ext.isEmpty(getInitialDate()) && !Ext.isEmpty(getFinalDate()) ){
						return true;
					}else{
						return false;
					}
				}
				
				function loadCountries(){
					Ext.getCmp('showReport').disable();
					var productsFilter = getProducts();
					
					if(isDateSelected() && !Ext.isEmpty(productsFilter)){
						var tmpStore = storeCountries;
		        		
		        		tmpStore.proxy.extraParams={
	        				initialDate: getInitialDate(),
			                finalDate: getFinalDate(),
			                productsFilter: productsFilter
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback: function(records, operation, success){
								m.close();
								
								var tmpCountries = Ext.getCmp('countries');
								tmpCountries.clearValue();
								
								tmpCountries.fromField.store.removeAll();
								
								if(success){
									tmpCountries.store.add(storeCountries.getRange());
									tmpCountries.fromField.store.add(storeCountries.getRange());
								}
							}
						});
					}else{
						var tmpCountries = Ext.getCmp('countries');
						tmpCountries.clearValue();
						
						tmpCountries.fromField.store.removeAll();
					}
				}
				
				function loadProducts(){
					var tmpStore = Ext.getStore('storeProducts');
					var tmpGridStore = Ext.getStore('storeGridProducts');
					tmpGridStore.removeAll();
					
	        		tmpStore.proxy.extraParams={
		                initialDate: getInitialDate(),
		                finalDate: getFinalDate()//,
		                //countries: Ext.getCmp('countries').getSubmitValue()
		            };
	        		
					var m = wait();
					
					tmpStore.load({
						scope:this,
						callback:function(records, operation, success){
							m.close();
							
							var tmpCountries = Ext.getCmp('countries');
							tmpCountries.clearValue();
							
							tmpCountries.fromField.store.removeAll();
							
							if(tmpStore.getCount()>0 &&isDateSelected()){
								Ext.getCmp('addRecord').enable();
							}else{
								Ext.getCmp('addRecord').disable();
							}
						}
					});
					
				}
								
				
				function loadProductTypes(){
					
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue())){
						var ptypeSk = Ext.getCmp('ptypeId');
		        		
						ptypeSk.enable();
		        		//var tmp = varSk.getValue();
						var tmpStore = Ext.getStore('storeProductTypes');
		        		
		        		tmpStore.proxy.extraParams={
	        				initialDate: getInitialDate(),
			                finalDate: getFinalDate(),
			                //countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue()
			            };
		        		
						var m = wait();
						
						//Ext.getCmp('varId').setValue('');
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								//alert(globalPtypeId);
								if(!Ext.isEmpty(globalPtypeId)){
									var ptypeSk = Ext.getCmp('ptypeId');
					        		ptypeSk.setValue(globalPtypeId);
									globalPtypeId = null;
									Ext.getCmp('ptypeId').focus();
								}

								if(tmpStore.getCount() == 0){
									loadVarieties();
								}/*else{
									if(!Ext.isEmpty(globalPtypeId)){
										ptypeSk.setValue(globalPtypeId);
										globalPtypeId = null;
										Ext.getCmp('ptypeId').focus();
									}
								}*/
							}
						});
					}
				}
				
				function loadVarieties(){
					
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue())){
						var varSk = Ext.getCmp('varId');
		        		
		        		varSk.enable();
		        		//var tmp = varSk.getValue();
						var tmpStore = Ext.getStore('storeVarieties');
		        		
		        		tmpStore.proxy.extraParams={
	        				initialDate: getInitialDate(),
			                finalDate: getFinalDate(),
			                //countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue(),
			                ptypeId: Ext.getCmp('ptypeId').getValue()
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
	        				initialDate: getInitialDate(),
			                finalDate: getFinalDate(),
			                //countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue(),
			                ptypeId: Ext.getCmp('ptypeId').getValue(),
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
								
								if(!Ext.isEmpty(Ext.getCmp('quaId').getValue())){
									loadSizes();
								}
							}
						});
					}
				}
				
				function loadSizes(){
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue()) && !Ext.isEmpty(Ext.getCmp('quaId').getValue())){
						var sizeId = Ext.getCmp('sizeId');
		        		
		        		var tmpStore = Ext.getStore('storeSizes');
		        		
		        		tmpStore.proxy.extraParams={
	        				initialDate: getInitialDate(),
			                finalDate: getFinalDate(),
			                //countries: Ext.getCmp('countries').getSubmitValue(),
			                prodId: Ext.getCmp('prodId').getValue(),
			                ptypeId: Ext.getCmp('ptypeId').getValue(),
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
			        	/*validateedit:function(editor, e, eOpts){
			        		alert(e.record.get('prodDesc'));
			        	},//*/
			        	cancelEdit: function(){
			        		Ext.getCmp('addRecord').enable();
			        	},
			        	validateedit: function(editor, e){
			        		var varId = Ext.getCmp('varId');
			        		var strVarSk = varId.getStore();
			        		var ptypeId = Ext.getCmp('ptypeId');
			        		var strPtypeId = ptypeId.getStore();
			        		
			        		if(strPtypeId.getCount()>0 && (Ext.isEmpty(ptypeId.getValue()) || ptypeId.getValue() ==0)){
		        				e.cancel = true;
		        				
		        				Ext.Msg.show({
			           				title: '<bean:message key="global.error.title" />',
			           				msg: '<bean:message key="queryPrices.ptypeId.required"/>',
			           				icon: Ext.Msg.ERROR,
			           				buttons: Ext.Msg.OK,
			           				modal: true,
			           				animateTarget: 'globalErrors'
			           			});
		        				
		        				return false;
			        		}
			        		
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
			        		}
			        		
			        		e.cancel = false;
			        		return true;
			        	},
			        	beforeedit:function(editor, e){
			        		var prodId = e.record.get('prodId'); //ID seleccionado
			        		var varId = e.record.get('varId');
			        		var ptypeId = e.record.get('ptypeId');
			        		var quaId = e.record.get('quaId');
			        		var sizeId = e.record.get('sizeId');
			        		
			        		new Ext.util.DelayedTask().delay(80,function(){
			        			globalVarId = varId;
			        			globalPtypeId = ptypeId;
			        			globalQuaId = quaId;
			        			globalSizeId = sizeId;
			        			
			        			Ext.getCmp('prodId').setValue(prodId);
			        			/*Ext.getCmp('ptypeId').setValue(ptypeId);
			        			Ext.getCmp('varId').setValue(varId);
			        			Ext.getCmp('quaId').setValue(quaId);
				        		Ext.getCmp('sizeId').setValue(sizeId);*/
				        		
				        		/*function tmpClear(tmp){
				        			tmp.getStore().suspendEvents();
				        			tmp.getStore().clearFilter();
				        			tmp.getStore().resumeEvents();
				        		}
				        		
				        		tmpClear(Ext.getCmp('prodId'));
				        		tmpClear(Ext.getCmp('varId'));
				        		tmpClear(Ext.getCmp('quaId'));
				        		tmpClear(Ext.getCmp('sizeId'));*/
                    		});
			        	},
			        	edit: function(editor, e){
			        		var prodId = e.record.get('prodDesc'); //ID seleccionado
			        		var varId = e.record.get('varDesc');
			        		var ptypeId = e.record.get('ptypeDesc');
			        		var quaId = e.record.get('quaDesc');
			        		var sizeId = e.record.get('sizeDesc');
			        		
			        		var strProdId = Ext.getCmp('prodId').getStore();//Store donde se encuentran los demas campos
			        		var strVarId = Ext.getCmp('varId').getStore();
			        		var strPtypeId = Ext.getCmp('ptypeId').getStore();
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
			        		
							//-------------------------------------
			        		
			        		strPtypeId.clearFilter();
			        		
			        		//Filtro el registro segun el ID proporcionado
			        		strPtypeId.filter([
			        			{id: 'ptypeId', property: 'ptypeId', value: ptypeId, exactMatch: true}
			        		]);
			        		
			        		//Si lo encuentra procede a actualizar el Store del registro seleccionado.
			        		if(strPtypeId.getCount() > 0 ){
				        		var record = strPtypeId.getAt(0);
				        		sm.getSelection()[0].set('ptypeId', record.get('ptypeId'));
				        		sm.getSelection()[0].set('ptypeDesc', record.get('ptypeDesc'));
			        		}
			        		
			        		strPtypeId.clearFilter();
			        		
			        		sm.getSelection()[0].commit();
			        		loadCountries();
			        		Ext.getCmp('addRecord').enable();
			        	}
			        }
			    });
				
				
				// create the grid and specify what field you want
			    // to use for the editor at each column.
			    var grid = Ext.create('Ext.grid.Panel', {
			    	id:'gridProd',
			    	plugins: [rowEditing],
			    	height: 150,
			    	width: 500,
			    	colspan:2,
			    	title: '<bean:message key="queryPrices.product.selected"/>',
			    	//renderTo: 'container',
			    	store: Ext.create('Ext.data.Store', {
			    		id:'storeGridProducts',
				        // destroy the store if the grid is destroyed
				        autoDestroy: true,
				        model: 'modelGrid',
				        proxy: {
				            type: 'memory',
				            totalProperty: 'totalCount'
				        }/*,
				        data: data,
				        sorters: [{
				            property: 'start',
				            direction: 'ASC'
				        }]*/	
				    }),
				    tbar: [{
			            text: '<bean:message key="queryPrices.addProduct"/>',
			            iconCls: 'option-item-add',
			            id: 'addRecord',
			            handler : function() {
			                rowEditing.cancelEdit();
			                //rowEditing.completeEdit();
			                Ext.getCmp('addRecord').disable();

			                // Create a record instance through the ModelManager
			                var r = Ext.ModelManager.create({
			                    prodId:null,
			                    prodDesc: '',
			                    varId: null,
			                    varDesc: '',
			                    ptypeId: null,
			                    ptypeDesc: '',
			                    quaId: null,
			                    quaDesc: '',
			                    sizeId: null,
			                    sizeDesc: ''
			                }, 'modelGrid');

			                this.up('grid.panel').store.insert(0, r);
			                rowEditing.startEdit(0, 0);
			            }, 
			            disabled: true
			        }, {
			            itemId: 'removeRecord',
			            id: 'removeRecord',
			            text: '<bean:message key="queryPrices.delProduct"/>',
			            iconCls: 'option-item-delete',
			            handler: function() {
			            	Ext.getCmp('addRecord').enable();
			                var sm = this.up('grid.panel').getSelectionModel();
			                rowEditing.cancelEdit();
			                this.up('grid.panel').store.remove(sm.getSelection());
			                
			                loadCountries();
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
					        store: Ext.create('Ext.data.Store', {
						        id: 'storeProducts',
						        model: 'modelProdId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryPricesAction.do?accion=load&store=loadProducts',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'prodDesc',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
					        queryMode: 'local',
					        displayField: 'prodDesc',
					        valueField: 'prodId',
					        editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 50,
					        	// delete the previous query in the beforequery event or set
					            // combo.lastQuery = null (this will reload the store the next time it expands)
					            /*beforequery: function(qe){
					                delete qe.combo.lastQuery;
					            },*/
					        	/*change: function(combo, newValue, oldValue, eOpts){
					        		
					        		var varSk = Ext.getCmp('varId');
					        		var strVarSk = varSk.store;
					        		
					        		varSk.enable();
				        			var tmpValue = varSk.getValue();
				        			varSk.clearValue();
					        		
				        			
				        			strVarSk.suspendEvents();
					        		strVarSk.clearFilter();
					        		strVarSk.resumeEvents();
					        		
					        		strVarSk.filter([
					        			{id: 'prodId', property: 'prodId', value: combo.getValue(), exactMatch: true}
					        		]);
					        		
					        		
					        		varSk.setValue(tmpValue);
					        	}*/
					        	change: loadProductTypes
					        }
	      	            }
			        },{
			            header: '<bean:message key="queryPrices.ptype"/>',
			            dataIndex: 'ptypeDesc',
			            //width: 160,
			            flex: 1,
			            field: {
	      	              	xtype: 'combo',
	      	                id:'ptypeId',
	      	                allowBlank:true,
	      	              	disabled: true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeProductTypes',
						        model: 'modelPtypeId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryPricesAction.do?accion=load&store=loadProductTypes',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        sorters: [{
	      				            property: 'ptypeId',
	      				            direction: 'ASC'
	      				        }],
						        autoLoad: false
						    }),
					        queryMode: 'local',
					        displayField: 'ptypeDesc',
					        valueField: 'ptypeId',
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
				                change: loadVarieties
					        }
	      	            }
			        },{
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
						            url: 'queryPricesAction.do?accion=load&store=loadVarieties',
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
						            url: 'queryPricesAction.do?accion=load&store=loadQualities',
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
						            url: 'queryPricesAction.do?accion=load&store=loadSizes',
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
			                this.down('#removeRecord').setDisabled(!records.length);
			            }
			        }
			    });
				
				//************************************************************************************************
				
				var cstFormPanel = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					//url:'sizeAction.do',
					url:'reportAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					width: 550,
					title: '<bean:message key="queryPrices.header" />',
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
      	        		id:'report',
      	        		name:'report'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'FormatPrint',
      	        		name:'FormatPrint',
      	        		value:'xlsx'
      	        	},{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
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
	      	            	//colspan: 2,
	      	            	//rowspan:2,
	      	            	xtype: 'radiogroup',
		      	            allowBlank: false,
		      	            msgTarget: 'side',
		      	            id:'chkLevelAgregation',
		      	            //autoFitErrors: false,
		      	            //anchor: '-18',
		      	            layout: 'column',
		      	            width:100,
		      	            defaultType: 'container',
		      	            items: [{
		      	                //columnWidth: .3,
		      	                margin:'0 0 0 100',
		      	                items: [
		      	                    {xtype: 'component', html: '<bean:message key="queryPrices.levelAgregation" />', cls:'x-form-check-group-label'},
		      	                    {
		      	                    	xtype: 'radiofield', 
		      	                    	boxLabel: '<bean:message key="queryPrices.levelAgregation.daily" />', 
		      	                    	name: 'levelAgregation', 
		      	                    	inputValue: 'daily',
		      	                    	checked: true,
		      	                    	listeners:{
		      	                    		change: createDaily
		      	                    	}
		      	                    },{
		      	                    	xtype: 'radiofield', 
		      	                    	boxLabel: '<bean:message key="queryPrices.levelAgregation.weekly" />', 
		      	                    	name: 'levelAgregation', 
		      	                    	inputValue: 'weekly',
		      	                    	listeners:{
		      	                    		change: createWeekly
		      	                    	}
		      	                    },{
		      	                    	xtype: 'radiofield', 
		      	                    	boxLabel: '<bean:message key="queryPrices.levelAgregation.monthly" />', 
		      	                    	name: 'levelAgregation', 
		      	                    	inputValue: 'monthly',
		      	                    	listeners:{
		      	                    		change: createMonthly
		      	                    	}
		      	                    },{
		      	                    	xtype: 'radiofield', 
		      	                    	boxLabel: '<bean:message key="queryPrices.levelAgregation.yearly" />', 
		      	                    	name: 'levelAgregation', 
		      	                    	inputValue: 'yearly',
		      	                    	listeners:{
		      	                    		change: createYearly
		      	                    	}
		      	                    }
		      	                ]
		      	            }]
						},
		      	      	{
		      	        	//rowspan:2,
		      	        	//width:500,
		      	        	xtype:'container',
		      	        	id:'pnlAgregation',
		      	        	width:250,
		      	        	layout: {
		      	            	type:'anchor',
		      	            	columns: 2
		      	            },
		      	            defaults:{
		      	            	labelAlign:'right'
		      	            },
		      	        	items:[]
		      	        },
		      	      	grid,
		      	      	{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryPrices.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryPrices.countries.selected" />'
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
					            blankText: '<bean:message key="queryPrices.countries.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
						        	change: function(){
						        		if(Ext.getCmp('countries').toField.boundList.getStore().getCount()==0){
						                	Ext.getCmp('showReport').disable();
						                }else{
						                	Ext.getCmp('showReport').enable();
						                }
						        	}
						        }
      	                    }]
				        }]
	      	        },
					{
        	            colspan:2,
        	            id:'showReport',
        	            disabled:true,
        	            xtype: 'button',
        	            layout:'column',
        	            margin: '10 0 0 0',
      	            	text: '<bean:message key="queryPrices.showReport" />',
      	            	//width: 100,
        	          	listeners: {
							click: function(){
								Ext.getCmp('accion').setValue('generateReport');
								var form = this.up('form.panel');
								
								
								
								if (form.getForm().isValid() && validations()){
									
									//Set the Countries names to display in the report
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
									
									//**************************************************
									//Set the data from the grid into a hidden
									var tmpValue='';
									tmpValue = getProducts();
									
									Ext.getCmp('productFilters').setValue(tmpValue);
									
									form._doCustomSubmit();
									new Ext.util.DelayedTask().delay(2000,function(){if(m)m.close();});
								}
							},
							render:function(){
								this.setPosition(this.up('form.panel').getWidth()/2 - this.width/2 - 20, 0);
							}
						}
        	        }]//Items Form.Panel
				});
				
				
				cstFormPanel.reportMode();
				createDaily(null,true);
				
				function validations(){
					var tmpMsg = validateRange()+ validateProducts();
					
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
				
				function validateProducts(){
					var tmpMsg='';
					
					if(Ext.getCmp('gridProd').getStore().getCount()==0){
						tmpMsg='<bean:message key="queryPrices.product.selected.required"/>';
					}
					
					return tmpMsg;
				}
				
				function validateRange(){
           			var initialDate;
               		var tmpMsg='';
               		var finalDate; 
               		
               		var levelAgregation=null;
               		
               		if(Ext.getCmp('chkLevelAgregation').getValue()){
               			levelAgregation = Ext.getCmp('chkLevelAgregation').getValue().levelAgregation;
               		}
               		
               		switch(levelAgregation){
               			case 'daily':
               				break;
               				
               			case 'weekly':
               				initialDate = Ext.getCmp('initialWeek').getValue();
                       		finalDate = Ext.getCmp('finalWeek').getValue();
                       		
                       		if(initialDate > finalDate){
                       			tmpMsg = tmpMsg+'<bean:message key="queryPrices.finalWeek.minor"/>';
                       		}
               				break;
               				
               			case 'monthly':
               				initialDate = new Number(Ext.getCmp('initialYear').getValue()+''+Ext.getCmp('initialMonth').getValue()).valueOf();
                       		finalDate = new Number(Ext.getCmp('finalYear').getValue()+''+Ext.getCmp('finalMonth').getValue()).valueOf();
                       		
                       		if(initialDate > finalDate){
                       			tmpMsg = tmpMsg+'<bean:message key="queryPrices.finalMonth.minor"/>';
                       		}
               				break;
               				
               			case 'yearly':
               				initialDate = Ext.getCmp('initialYear').getValue();
                       		finalDate = Ext.getCmp('finalYear').getValue();
                       		
                       		if(initialDate > finalDate){
                       			tmpMsg = tmpMsg+'<bean:message key="queryPrices.finalYear.minor"/>';
                       		}
                       		break;
                       		
               			default:
               				tmpMsg= '<bean:message key="queryPrices.levelAgregation.required"/>';
               		}
               		
               		
               		return tmpMsg;
				}
				
				//#########Functions that create Agregation fields
				
				function destroyAllAgregation(){
					if(Ext.getCmp('initialDate')){
						Ext.getCmp('initialDate').destroy();
					}
					
					if(Ext.getCmp('fieldContainerInitial')){
						Ext.getCmp('fieldContainerInitial').destroy();
					}
				
					if(Ext.getCmp('fieldContainerFinal')){
						Ext.getCmp('fieldContainerFinal').destroy();
					}
					
					if(Ext.getCmp('initialWeek')){
						Ext.getCmp('initialWeek').destroy();
					}
					
					if(Ext.getCmp('finalWeek')){
						Ext.getCmp('finalWeek').destroy();
					}
					
					if(Ext.getCmp('initialMonth')){
						Ext.getCmp('initialMonth').destroy();
					}
					
					if(Ext.getCmp('finalMonth')){
						Ext.getCmp('finalMonth').destroy();
					}
					
					if(Ext.getCmp('initialYear')){
						Ext.getCmp('initialYear').destroy();
					}
					
					if(Ext.getCmp('finalYear')){
						Ext.getCmp('finalYear').destroy();
					}
				}
				
				function createDaily(obj, newValue, oldValue){
					if(!newValue){
						destroyAllAgregation();
						return;
					}
					
					new Ext.util.DelayedTask().delay(150,function(){
						Ext.getCmp('report').setValue('01_Cosulta_de precios_dia.jrxml');
						
						Ext.getCmp('pnlAgregation').add([{
							xtype: 'datefield',
							fieldLabel: '<bean:message key="queryPrices.date"/>',
							id: 'initialDate',
							name:'initialDate',
							format: 'd/m/Y',
							allowBlank: false,
							blankText: '<bean:message key="queryPrices.initialDate.required"/>',
							//maxValue: new Date(),  // limited to the current date or prior
							value: Ext.Date.add(new Date(), Ext.Date.DAY,-1)
	      	            }]);
						
						Ext.getCmp('initialDate').addListener('change', loadProducts);
						Ext.getCmp('initialDate').fireEvent('change');
					});
				}
				
				function createWeekly(obj, newValue, oldValue){
					if(!newValue){
						destroyAllAgregation();
						return;
					}
					
					new Ext.util.DelayedTask().delay(150,function(){
						Ext.getCmp('report').setValue('01_consulta_de_precios_semana.jrxml');
						
						Ext.getCmp('pnlAgregation').add([{
							xtype: 'datefield',
							fieldLabel: '<bean:message key="queryPrices.initialWeek"/>',
							id: 'initialWeek',
							name:'initialWeek',
							format: 'd/m/Y',
							allowBlank: false,
							blankText: '<bean:message key="queryPrices.initialWeek.required"/>',
							//maxValue: new Date(),  // limited to the current date or prior
							value: Ext.Date.add(new Date(), Ext.Date.DAY,-1)
	      	            },{
							xtype: 'datefield',
							fieldLabel: '<bean:message key="queryPrices.finalWeek"/>',
							id: 'finalWeek',
							name:'finalWeek',
							format: 'd/m/Y',
							allowBlank: false,
							blankText: '<bean:message key="queryPrices.finalWeek.required"/>',
							//maxValue: new Date(),  // limited to the current date or prior
							value: Ext.Date.add(new Date(), Ext.Date.DAY,-1)
	      	            }]);
						
						Ext.getCmp('initialWeek').addListener('change', loadProducts);
						Ext.getCmp('finalWeek').addListener('change', loadProducts);
						Ext.getCmp('finalWeek').fireEvent('change');
					});
				}
				
				function createMonthly(obj, newValue, oldValue){
					if(!newValue){
						destroyAllAgregation();
						return;
					}
					new Ext.util.DelayedTask().delay(150,function(){
						Ext.getCmp('report').setValue('01_Consulta_de_precio_mes.jrxml');
						
						Ext.getCmp('pnlAgregation').add([{
				                xtype: 'fieldcontainer',
				                id: 'fieldContainerInitial',
				                fieldLabel: '<bean:message key="queryPrices.initialDate"/>',
				                combineErrors: true,
				                //autoFitErrors: false,
				                msgTarget : 'side',
				                layout: 'hbox',
				                defaults: {
				                    flex: 1,
				                    hideLabel: true
				                },
				                items: [{
			      	              	xtype: 'combo',
			      	                id:'initialMonth',
			      	                name: 'initialMonth',
			      	                //maxLength: 1,
			      	              	allowBlank:false,
			      	              	forceSelection: true,
			      	              	width: 50,
			      	              	blankText:'<bean:message key="queryPrices.initialMonth.required"/>',
							        store: 	Ext.create('Ext.data.Store',{
							        	model: 'modelMonthSk',
							        	data: {registers: [
							        		{'monthCode':'01', 'monthId':1, 'monthDesc':'<bean:message key="production.grid.jan"/>'},
							        		{'monthCode':'02', 'monthId':2, 'monthDesc':'<bean:message key="production.grid.feb"/>'},
							        		{'monthCode':'03', 'monthId':3, 'monthDesc':'<bean:message key="production.grid.mar"/>'},
							        		{'monthCode':'04', 'monthId':4, 'monthDesc':'<bean:message key="production.grid.apr"/>'},
							        		{'monthCode':'05', 'monthId':5, 'monthDesc':'<bean:message key="production.grid.may"/>'},
							        		{'monthCode':'06', 'monthId':6, 'monthDesc':'<bean:message key="production.grid.jun"/>'},
							        		{'monthCode':'07', 'monthId':7, 'monthDesc':'<bean:message key="production.grid.jul"/>'},
							        		{'monthCode':'08', 'monthId':8, 'monthDesc':'<bean:message key="production.grid.aug"/>'},
							        		{'monthCode':'09', 'monthId':9, 'monthDesc':'<bean:message key="production.grid.sep"/>'},
							        		{'monthCode':'10', 'monthId':10, 'monthDesc':'<bean:message key="production.grid.oct"/>'},
							        		{'monthCode':'11', 'monthId':11, 'monthDesc':'<bean:message key="production.grid.nov"/>'},
							        		{'monthCode':'12', 'monthId':12, 'monthDesc':'<bean:message key="production.grid.dec"/>'}
							        	]},
							        	proxy:{
							        		type: 'memory',
							        		reader: {
							                    type: 'json',
							                    root: 'registers'
							                }
							        	},
			      			        	sorters: [{
			      				            property: 'monthId',
			      				            direction: 'ASC'
			      				        }]
							        }),
							        queryMode: 'local',
							        displayField: 'monthDesc',
							        valueField: 'monthCode',
							        value: Ext.Date.format(new Date(), 'm'),
							        editable: false
							    },{
			      	              	xtype: 'combo',
			      	              	id:'initialYear',
			      	                name: 'initialYear',
			      	                //maxLength: 1,
			      	                forceSelection: true,
			      	              	allowBlank:false,
			      	              	hideLabel:true,
			      	              	autoFitErrors: false,
			      	              	width: 70,
			      	              	blankText:'<bean:message key="queryPrices.initialYear.required"/>',
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
							        value: new Number(Ext.Date.format(new Date(), 'Y')).valueOf(),
							        editable: false
							    }
							]},{
				                xtype: 'fieldcontainer',
				                id: 'fieldContainerFinal',
				                fieldLabel: '<bean:message key="queryPrices.finalDate"/>',
				                combineErrors: true,
				                //autoFitErrors: false,
				                msgTarget : 'side',
				                layout: 'hbox',
				                defaults: {
				                    //flex: 1,
				                    hideLabel: true
				                },
				                items: [{
			      	              	xtype: 'combo',
			      	                id:'finalMonth',
			      	                name: 'finalMonth',
			      	                //maxLength: 1,
			      	              	allowBlank:false,
			      	              	forceSelection: true,
			      	              	width: 50,
			      	              	blankText:'<bean:message key="queryPrices.finalMonth.required"/>',
							        store: 	Ext.create('Ext.data.Store',{
							        	model: 'modelMonthSk',
							        	data: {registers: [
							        		{'monthCode':'01', 'monthId':1, 'monthDesc':'<bean:message key="production.grid.jan"/>'},
							        		{'monthCode':'02', 'monthId':2, 'monthDesc':'<bean:message key="production.grid.feb"/>'},
							        		{'monthCode':'03', 'monthId':3, 'monthDesc':'<bean:message key="production.grid.mar"/>'},
							        		{'monthCode':'04', 'monthId':4, 'monthDesc':'<bean:message key="production.grid.apr"/>'},
							        		{'monthCode':'05', 'monthId':5, 'monthDesc':'<bean:message key="production.grid.may"/>'},
							        		{'monthCode':'06', 'monthId':6, 'monthDesc':'<bean:message key="production.grid.jun"/>'},
							        		{'monthCode':'07', 'monthId':7, 'monthDesc':'<bean:message key="production.grid.jul"/>'},
							        		{'monthCode':'08', 'monthId':8, 'monthDesc':'<bean:message key="production.grid.aug"/>'},
							        		{'monthCode':'09', 'monthId':9, 'monthDesc':'<bean:message key="production.grid.sep"/>'},
							        		{'monthCode':'10', 'monthId':10, 'monthDesc':'<bean:message key="production.grid.oct"/>'},
							        		{'monthCode':'11', 'monthId':11, 'monthDesc':'<bean:message key="production.grid.nov"/>'},
							        		{'monthCode':'12', 'monthId':12, 'monthDesc':'<bean:message key="production.grid.dec"/>'}
							        	]},
							        	proxy:{
							        		type: 'memory',
							        		reader: {
							                    type: 'json',
							                    root: 'registers'
							                }
							        	},
			      			        	sorters: [{
			      				            property: 'monthId',
			      				            direction: 'ASC'
			      				        }]
							        }),
							        queryMode: 'local',
							        displayField: 'monthDesc',
							        valueField: 'monthCode',
							        value: Ext.Date.format(new Date(), 'm'),
							        editable: false
							    },{
			      	              	xtype: 'combo',
			      	                hideLabel:true,
			      	                id:'finalYear',
			      	                name: 'finalYear',
			      	                //maxLength: 1,
			      	              	forceSelection: true,
			      	                allowBlank:false,
			      	              	//autoFitErrors: false,
			      	              	width: 70,
			      	              	blankText:'<bean:message key="queryPrices.finalYear.required"/>',
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
							        value: new Number(Ext.Date.format(new Date(), 'Y')).valueOf(),
							        editable: false
							    }
							]}
						]);
						
						Ext.getCmp('initialYear').addListener('change', loadProducts);
						Ext.getCmp('initialMonth').addListener('change', loadProducts);
						Ext.getCmp('finalMonth').addListener('change', loadProducts);
						Ext.getCmp('finalYear').addListener('change', loadProducts);
						Ext.getCmp('finalYear').fireEvent('change');
						
					});
				}
				
				function createYearly(obj, newValue, oldValue){
					if(!newValue){
						destroyAllAgregation();
						return;
					}
					
					new Ext.util.DelayedTask().delay(150,function(){
						Ext.getCmp('report').setValue('01_Consulta_de_precios_anio.jrxml');
						
						Ext.getCmp('pnlAgregation').add([{
		      	              	colspan:2,
								xtype: 'combo',
		      	                fieldLabel: '<bean:message key="queryPrices.initialDate"/>',
		      	                id:'initialYear',
		      	                name: 'initialYear',
		      	                allowBlank:false,
		      	              	//autoFitErrors: false,
		      	              	forceSelection: true,
		      	              	width: 150,
		      	              	blankText:'<bean:message key="queryPrices.initialYear.required"/>',
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
						        value: new Number(Ext.Date.format(new Date(), 'Y')).valueOf(),
						        editable: false
						    },{
						    	xtype: 'combo',
		      	                fieldLabel: '<bean:message key="queryPrices.finalDate"/>',
		      	                id:'finalYear',
		      	                name: 'finalYear',
		      	                //maxLength: 1,
		      	              	allowBlank:false,
		      	              	forceSelection: true,
		      	              	//autoFitErrors: false,
		      	              	width: 150,
		      	              	blankText:'<bean:message key="queryPrices.finalYear.required"/>',
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
						        value: new Number(Ext.Date.format(new Date(), 'Y')).valueOf(),
						        editable: false
						    }
						]);
						
						Ext.getCmp('initialYear').addListener('change', loadProducts);
						Ext.getCmp('finalYear').addListener('change', loadProducts);
						Ext.getCmp('finalYear').fireEvent('change');
					});
				}
				
				
		      	
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryPrices.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryPrices.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>