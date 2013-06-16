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
		La participación se mide en base a una categoría, es decir, representa el % de importaciones o exportaciones de una fruta sobre el total de fruta fresca tropical importado o exportado.
		<br/>
		<br/>
		Si selecciona por producto, puede elegir las frutas tropicales que desea analizar en relación a la categoría "Fruta Fresca Tropical". 
		Si selecciona por categoría, le aparecerán las frutas que los SIMAs hayan ingresado en el sistema correspondientes a la 
		categoría  "Fruta Fresca Tropical". En el futuro, el sistema se puede expandir a más categorías 
		(ej. "Fruta Congelada Tropical").
		<br/>
		<br/>
		<b>Fuente de datos</b>:
		Los datos de importación y exportación de las frutas fueron proporcionados por los SIMAs. 
		Los datos de importación y exportación de la categoría de fruta fresca tropical fueron tomados de la FAOSTAT, sumando los valores 
		individuales de las frutas de esa categoría. Para mayor detalle, ver <a href="temp/Categorias Agricolas.xlsx">link</a>
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
				
				var storeCountries = Ext.create('Ext.data.Store', {
			        id: 'storeCountries',
			        model: 'modelCtrSk',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'queryParticipationGroupAction.do?accion=load&store=loadCountries',
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
			            url: 'queryParticipationGroupAction.do?accion=load&store=loadProducts',
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
					
					var tmpCountries = Ext.getCmp('countries');
					
					if(tmpCountries.toField.store.getCount()>0){
						Ext.getCmp('chkCategory').enable();
					}else{
						Ext.getCmp('chkCategory').disable();
					}
					
					
	        		tmpStore.proxy.extraParams={
		                initialDate: Ext.getCmp('initialDate').getValue(),
		                finalDate: Ext.getCmp('finalDate').getValue(),
		                countries: Ext.getCmp('countries').getSubmitValue(),
		                variable: getType(),
		                selection: Ext.getCmp('chkCategory').getValue().selection
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
					
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue()) && !Ext.isEmpty(Ext.getCmp('finalDate').getValue()) && !Ext.isEmpty(Ext.getCmp('chkVariable').getValue().variable)){
						var tmpStore = storeCountries;
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue(),
			                finalDate: Ext.getCmp('finalDate').getValue(),
			                variable: getType()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								var tmpCountries = Ext.getCmp('countries');
								tmpCountries.clearValue();
								
								//if(tmpCountries.store.getCount()>0){
									tmpCountries.fromField.store.removeAll();
								//}
								
								if(success){
									tmpCountries.store.add(storeCountries.getRange());
									tmpCountries.fromField.store.add(storeCountries.getRange());
								}
							}
						});
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
					title: '<bean:message key="queryParticipationGroup.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'queryCategory',
      	        		name:'queryCategory'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value:'11_Participacion_Grupo_Categoria.jrxml'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'type',
      	        		name:'type'
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
	      	                fieldLabel: '<bean:message key="queryParticipationGroup.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryParticipationGroup.initialDate.required"/>',
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
	      	                fieldLabel: '<bean:message key="queryParticipationGroup.finalDate"/>',
	      	                id:'finalDate',
	      	                name: 'finalDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryParticipationGroup.finalDate.required"/>',
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
				            	change: loadCountries
					        }
					    },{
	      	            	colspan: 2,
	      	            	id:'chkVariable',
	      	            	xtype: 'radiogroup',
							fieldLabel: '<bean:message key="queryParticipationGroup.variable" />',
							// Arrange radio buttons into two columns, distributed vertically
							//labelAlign:'top',
							columns: 2,
							width: 350,
							vertical: true,
							defaults:{
								margin:'0 0 0 5'
							},
							items:[
								{ 
									boxLabel: '<bean:message key="queryParticipationGroup.variable.importDollar" />', 
									name: 'variable', 
									inputValue:'importDollar',
									listeners:{
						            	change: loadCountries
							        }
								},
								{ 
									boxLabel: '<bean:message key="queryParticipationGroup.variable.exportDollar" />', 
									name: 'variable', 
									inputValue:'exportDollar',
									listeners:{
						            	change: loadCountries
							        }
								}
							]
						},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryParticipationGroup.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryParticipationGroup.countries.selected" />'
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
					            blankText: '<bean:message key="queryParticipationGroup.countries.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeCountries,
					            displayField: 'ctrDesc',
					            valueField: 'ctrId',
					            allowBlank: false,
					            listeners:{
					            	render: function(){
					            		this.clearValue();
					            	},
					            	change: loadProducts
						        }
      	                    }]
				        },{
	      	            	colspan: 2,
	      	            	id:'chkCategory',
	      	            	xtype: 'radiogroup',
							fieldLabel: '<bean:message key="queryParticipationGroup.selection" />',
							//labelAlign:'top',
							columns: 2,
							disable: true,
							width: 350,
							vertical: true,
							defaults:{
								margin:'0 0 0 5'
							},
							items:[
								{ 
									boxLabel: '<bean:message key="queryParticipationGroup.selection.product" />', 
									name: 'selection', 
									inputValue:'product',
									checked: true,
									listeners:{
						            	change: loadProducts
							        }
								},
								{ 
									boxLabel: '<bean:message key="queryParticipationGroup.selection.category" />', 
									name: 'selection', 
									inputValue:'category',
									listeners:{
						            	change: loadProducts
							        }
								}
							]
						},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryParticipationGroup.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryParticipationGroup.product.selected" />'
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
					            blankText: '<bean:message key="queryParticipationGroup.product.selected.required"/>',
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
	      	            	text: '<bean:message key="queryParticipationGroup.showReport" />',
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
										
										Ext.getCmp('type').setValue(getType());
										
										if(Ext.getCmp('chkCategory').getValue().selection=='product'){
											Ext.getCmp('queryCategory').setValue('product1.prod_id');
										}else{
											Ext.getCmp('queryCategory').setValue('faogrp.grp_id');
										}
										
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
					var tmpMsg = validateRange()+validateCheckBoxes();
					
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
               			tmpMsg = tmpMsg+'<bean:message key="queryParticipationGroup.finalDate.minor"/>';
               		}
               		
               		return tmpMsg;
				}
				
				function validateCheckBoxes(){
					var variable = Ext.getCmp('chkVariable').getValue().variable;
					
					if(!variable){
						return '<bean:message key="queryParticipationGroup.variable.required"/>';
					}else{
						return '';
					}
				}
				
				function getType(){
					var variable = Ext.getCmp('chkVariable').getValue().variable;
					
					var type= '';
					
					if(variable == 'importDollar'){
						type ='I';
					}else if (variable == 'exportDollar'){
						type ='E';
					}
					
					return type;
				}
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryParticipationGroup.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryParticipationGroup.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>