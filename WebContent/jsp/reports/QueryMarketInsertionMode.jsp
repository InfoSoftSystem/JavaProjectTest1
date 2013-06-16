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
		Mide el grado de la dinámica exportadora de un país en un mercado meta al evaluar la tasa de crecimiento exponencial de la 
		exportación de un producto ("posicionamiento") y la tasa de crecimiento exponencial de la participación de las exportaciones 
		("eficiencia") del producto en ese mercado.
		<br/>
		<br/>
		<b>País Análisis</b>: País exportador<br/>
		<b>País Referencia</b>: Mercado meta (país importador)
		<br/>
		<br/>
		<b>Fuente metodológica</b>:Adaptado de Ministerio de Agricultura y Desarrollo Rural de Colombia
		<br/>
		<br/>
		<b>Fuente de datos</b>: FAOSTAT
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
				
				var storeOffererCountries = Ext.create('Ext.data.Store', {
			        id: 'storeOffererCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryMarketInsertionModeAction.do?accion=load&store=loadOffererCountries',
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
			            url: 'queryMarketInsertionModeAction.do?accion=load&store=loadDestinationCountries',
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
			            url: 'queryMarketInsertionModeAction.do?accion=load&store=loadProducts',
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
				
				function loadProducts(){
					var tmpStore = storeProducts;
	        		
	        		tmpStore.proxy.extraParams={
		                initialDate: Ext.getCmp('initialDate').getValue(),
		                finalDate: Ext.getCmp('finalDate').getValue(),
		                offererCountry: Ext.getCmp('offererCountry').getSubmitValue(),
		                destinationCountry: Ext.getCmp('destinationCountry').getValue()
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
							
							if(success){
								tmpComponent.store.add(storeProducts.getRange());
								tmpComponent.fromField.store.add(storeProducts.getRange());
							}
						}
					});
				}
				
				function loadOffererCountries(){
					var tmpStore = storeOffererCountries;
					
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('finalDate').getValue()) && Ext.getCmp('finalDate').isValid() && Ext.getCmp('initialDate').isValid()){
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                finalDate: Ext.getCmp('finalDate').getValue()
			            };
		        		
						var m = wait();
						/*Ext.getCmp('offererCountry').enable();
						Ext.getCmp('offererCountry').setValue('');*/
						
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
									var tmpCountries = Ext.getCmp('offererCountry');
									tmpCountries.clearValue();
									
									//if(tmpCountries.store.getCount()>0){
										//tmpCountries.store.removeAll();
										tmpCountries.fromField.store.removeAll();
									//}
									
									tmpCountries.store.add(tmpStore.getRange());
									tmpCountries.fromField.store.add(tmpStore.getRange());
								}
							}
						});
					}else{
						tmpStore.removeAll();
						/*Ext.getCmp('offererCountry').disable();
						Ext.getCmp('offererCountry').setValue('');*/
						
						var tmpCountries = Ext.getCmp('offererCountry');
						tmpCountries.clearValue();
						
						tmpCountries.fromField.store.removeAll();
					
					}
				}
				
				function loadDestinationCountries(){
					var tmpStore = storeDestinationCountries;
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('finalDate').getValue()) && !Ext.isEmpty(Ext.getCmp('offererCountry').getSubmitValue())){
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                finalDate: Ext.getCmp('finalDate').getValue(),
			                offererCountry: Ext.getCmp('offererCountry').getSubmitValue()
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
				
				//************************************************************************************************
				
				var cstFormPanel = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					//url:'sizeAction.do',
					url:'reportAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					width: 550,
					title: '<bean:message key="queryMarketInsertionMode.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value:'16_Modo_Insercion.jrxml'
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
	      	                fieldLabel: '<bean:message key="queryMarketInsertionMode.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryMarketInsertionMode.initialDate.required"/>',
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
					        		Ext.getCmp('finalDate').setValue(newValue+2);
					        		new Ext.util.DelayedTask().delay(500,function(){loadOffererCountries();});
					        	}
					        }
					    },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryMarketInsertionMode.finalDate"/>',
	      	                id:'finalDate',
	      	                name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryMarketInsertionMode.finalDate.required"/>',
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
					        	render: function(){
				            		this.clearValue();
				            	},
				            	change: loadOffererCountries
					        },
					        validator:function(value){
	      	              		var tmpSup = Ext.getCmp('initialDate').getValue();
	      	              		
	      	              		if(value < tmpSup+2){
	      	              			return '<bean:message key="queryMarketInsertionMode.finalDate.minor"/>';
	      	              		}
								return true;
	      	              	}
					    },/*<%--{
	      	              	//labelWidth:180,
	      	              	width: 250,
					    	xtype: 'combo',
					    	fieldLabel: '<bean:message key="queryMarketInsertionMode.offerer"/>',
	      	              	blankText:'<bean:message key="queryMarketInsertionMode.offerer.required"/>',
	      	                id:'offererCountry',
	      	                name: 'analisisCountries',
	      	                allowBlank:false,
	      	              	store: storeOffererCountries,
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrId',
					        editable: false,
					        disabled: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: loadDestinationCountries
					        }
	      	            },--%>*/{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryMarketInsertionMode.offerer.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryMarketInsertionMode.offerer.selected" />'
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
					            id: 'offererCountry',
	      	                    name: 'analisisCountries',
					            hideLabel: true,
					            blankText: '<bean:message key="queryMarketInsertionMode.offerer.selected.required"/>',
					            buttons:["add", "remove"],
						        store: storeOffererCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
						        	change: loadDestinationCountries
						        }
      	                    }]
				        },{
	      	              	//labelWidth:180,
	      	              	width: 250,
	      	              	colspan:2,
					    	xtype: 'combo',
					    	margin: '15 0 0 0',
					    	fieldLabel: '<bean:message key="queryMarketInsertionMode.destination"/>',
	      	              	blankText:'<bean:message key="queryMarketInsertionMode.destination.required"/>',
	      	                id:'destinationCountry',
	      	                name: 'referenceCountry',
	      	                allowBlank:false,
	      	              	store: storeDestinationCountries,
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrId',
					        disabled: true,
					        editable: false,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: loadProducts
					        }
	      	            },{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryMarketInsertionMode.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryMarketInsertionMode.product.selected" />'
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
					            blankText: '<bean:message key="queryMarketInsertionMode.product.selected.required"/>',
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
				        },{
	        	            colspan:2,
	        	            id:'showReport',
	        	            disabled:true,
	        	            xtype: 'button',
	      	            	text: '<bean:message key="queryMarketInsertionMode.showReport" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('generateReport');
									var form = this.up('form.panel');
									
									
									
									if (form.getForm().isValid() && validations()){
										
										
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
               			tmpMsg = tmpMsg+'<bean:message key="queryMarketInsertionMode.finalDate.minor"/>';
               		}
               		
               		return tmpMsg;
				}
				
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryMarketInsertionMode.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryMarketInsertionMode.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>