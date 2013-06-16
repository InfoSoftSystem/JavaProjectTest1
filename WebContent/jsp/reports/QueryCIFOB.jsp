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
		<b>País Origen</b>: se refiere al país de donde proviene el producto importado por el país de análisis
		<br/>
		<b>País Destino</b>: se refiere al país al que el país de análisis exporta un producto 
		<br/>
		<br/>
		<b>Fuente de Datos</b>: SIMAs, a través de sus fuentes oficiales que en la mayoría de los casos son los bancos centrales de cada país.
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
				
				var storeOriginCountries = Ext.create('Ext.data.Store', {
			        id: 'storeOriginCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryCIFOBAction.do?accion=load&store=loadOriginCountries',
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
				
				var storeProducts = Ext.create('Ext.data.Store', {
			        id: 'storeProducts',
			        model: 'modelProdSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryCIFOBAction.do?accion=load&store=loadProducts',
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
		                originCountries: Ext.getCmp('originCountries').getSubmitValue(),
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
				
				function loadCountries(){
					
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('finalDate').getValue()) ){
						var tmpStore = storeOriginCountries;
						
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                finalDate: Ext.getCmp('finalDate').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								var tmpCountries = Ext.getCmp('originCountries');
								tmpCountries.clearValue();
								
								//if(tmpCountries.store.getCount()>0){
									tmpCountries.fromField.store.removeAll();
								//}
								
								if(success){
									tmpCountries.store.add(storeOriginCountries.getRange());
									tmpCountries.fromField.store.add(storeOriginCountries.getRange());
								}
							}
						});
					}else{
						var tmpCountries = Ext.getCmp('originCountries');
						tmpCountries.clearValue();
						
						tmpCountries.fromField.store.removeAll();
					}
				}
				
				function loadDestinationCountries(){
					var tmpStore = Ext.getStore('storeDestinationCountries');
	        		
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('finalDate').getValue()) && !Ext.isEmpty(Ext.getCmp('originCountries').getSubmitValue())){
						
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                finalDate: Ext.getCmp('finalDate').getValue(),
			                originCountries: Ext.getCmp('originCountries').getSubmitValue()
			            };
		        		
						var m = wait();
						
						Ext.getCmp('destinationCountry').setValue('');
						//Ext.getCmp('destinationCountry').fireEvent('change');
						
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
					title: '<bean:message key="queryCIFOB.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'FormatPrint',
      	        		name:'FormatPrint',
      	        		value:'xlsx'
      	        	},{
      	        		xtype:'hidden',	
      	        		id:'report',
      	        		name:'report'
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
	      	                fieldLabel: '<bean:message key="queryCIFOB.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryCIFOB.initialDate.required"/>',
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
					        	change: loadCountries
					        }
					    },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryCIFOB.finalDate"/>',
	      	                id:'finalDate',
	      	                name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryCIFOB.finalDate.required"/>',
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
					        	change: loadCountries
					        }
					    },{
	      	            	colspan: 2,
	      	            	xtype: 'checkboxgroup',
							fieldLabel: '<bean:message key="queryCIFOB.variable" />',
							// Arrange radio buttons into two columns, distributed vertically
							//labelAlign:'top',
							columns: 2,
							vertical: true,
							items:[
								{ 
									boxLabel: '<bean:message key="queryCIFOB.variable.cif" />', 
									name: 'cif', 
									id:'cif',
									listeners:{
										change:function(field, newValue, oldValue){
											setReport();
										}
									}
								},
								{ 
									boxLabel: '<bean:message key="queryCIFOB.variable.fob" />', 
									name: 'fob', 
									id:'fob',
									listeners:{
										change:function(field, newValue, oldValue){
											setReport();
										}
									}
								}
							]
						},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryCIFOB.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryCIFOB.countries.selected" />'
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
	      	                    name: 'originCountries',
					            id: 'originCountries',
					            hideLabel: true,
					            blankText: '<bean:message key="queryCIFOB.countries.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeOriginCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
						        	change: loadDestinationCountries
						        }
      	                    }]
				        },{
	      	              	colspan: 2,
	      	              	labelWidth:180,
	      	              	width: 350,
					    	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryCIFOB.country.destination"/>',
	      	              	blankText:'<bean:message key="queryCIFOB.country.destination.required"/>',
	      	                id:'destinationCountry',
	      	                name: 'destinationCountry',
	      	                allowBlank:false,
	      	              	store: Ext.create('Ext.data.Store',{
					        	id: 'storeDestinationCountries',
						        model: 'modelCtrSk',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'queryCIFOBAction.do?accion=load&store=loadDestinationCountries',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },sorters: [{
						            property: 'ctrDesc',
						            direction: 'ASC'
						        }],
						        autoLoad: false
					        }),
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrId',
					        editable: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	change: loadProducts
					        }
	      	            },{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryCIFOB.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryCIFOB.product.selected" />'
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
					            blankText: '<bean:message key="queryCIFOB.product.selected.required"/>',
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
	      	            	text: '<bean:message key="queryCIFOB.showReport" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('generateReport');
									var form = this.up('form.panel');
									
									if (form.getForm().isValid() && validations()){
										var tmpStore = Ext.getCmp('originCountries').toField.boundList.getStore();
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
					var tmpMsg = validateRange()+validateCIFOB();
					
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
               			tmpMsg = tmpMsg+'<bean:message key="queryCIFOB.finalDate.minor"/>';
               		}
               		
               		return tmpMsg;
				}
				
				function validateCIFOB(){
					var cif = Ext.getCmp('cif').getValue();
					var fob = Ext.getCmp('fob').getValue();
					
					if(cif == false && fob == false){
						return '<bean:message key="queryCIFOB.cifob.required"/>';
					}else{
						return '';
					}
				}
				
				function setReport(){
					var cif = Ext.getCmp('cif').getValue();
					var fob = Ext.getCmp('fob').getValue();
					
					var report = Ext.getCmp('report');
					if(cif && fob){
						report.setValue('07_precios_CIFOB.jrxml');
					}else if (cif){
						report.setValue('07_precios_CIF.jrxml');
					}else if (fob){
						report.setValue('07_precios_FOB.jrxml');
					}else{
						report.setValue('');
					}
				}
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryCIFOB.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryCIFOB.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>