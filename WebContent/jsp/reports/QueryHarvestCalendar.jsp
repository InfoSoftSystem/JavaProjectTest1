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
		<b>Fuente de Datos</b>: Sistemas de informaci�n de mercados agr�colas (SIMAs) de la regi�n centroamericana y Rep�blica Dominicana. No necesariamente recolectan los datos pero ellos la recopilan y la ingresan en el SIMEFRUT.
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
			            url: 'queryHarvestCalendarAction.do?accion=loadCountries',
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
			            url: 'queryHarvestCalendarAction.do?accion=loadProducts',
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
							
							if(success){
								tmpComponent.store.add(storeProducts.getRange());
								tmpComponent.fromField.store.add(storeProducts.getRange());
							}
						}
					});
				}
				
				function loadCountries(){
					
					if(!Ext.isEmpty(Ext.getCmp('initialDate').getValue())){
						var tmpStore = storeCountries;
		        		
		        		tmpStore.proxy.extraParams={
			                initialDate: Ext.getCmp('initialDate').getValue()
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
					title: '<bean:message key="queryHarvestCalendar.header" />',
					items: [
				    {
      	        		xtype:'hidden',
      	        		id:'countriesDesc',
      	        		name:'countriesDesc'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'report',
      	        		name:'report',
      	        		value:'05_calendario_cosecha.jrxml'
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
	      	              	colspan:2,
							xtype: 'combo',
	      	                fieldLabel: '<bean:message key="queryHarvestCalendar.initialDate"/>',
	      	                id:'initialDate',
	      	                name: 'initialDate',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	//width: 200,
	      	              	blankText:'<bean:message key="queryHarvestCalendar.initialDate.required"/>',
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
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryHarvestCalendar.countries.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryHarvestCalendar.countries.selected" />'
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
					            blankText: '<bean:message key="queryHarvestCalendar.countries.selected.required"/>',
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
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryHarvestCalendar.product.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="queryHarvestCalendar.product.selected" />'
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
					            blankText: '<bean:message key="queryHarvestCalendar.product.selected.required"/>',
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
	      	            	text: '<bean:message key="queryHarvestCalendar.showReport" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('generateReport');
									var form = this.up('form.panel');
									
									
									
									if (form.getForm().isValid()){
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
				
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="queryHarvestCalendar.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="queryHarvestCalendar.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>