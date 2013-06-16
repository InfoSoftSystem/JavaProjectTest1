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
				Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
			}
			
			Ext.onReady(function(){
				//Property to disable the country field if the user is not an Administrator	
				//var ctrDisabled;
				<%
		        	UserForm user = (UserForm) session.getAttribute("user");
		            if(user.getNoCountry()){
		        %>
		        	ctrDisabled=false;
		        <%	}else{%>
		        	ctrDisabled= true;
		        <%	}%>
				
		        //Property used to choose the country column according to current language 
		        var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var userCtrId = <%=user.getCtrId()%>;
		        
				//************************************************************************************************
				//*** Models for Forms
				
				Ext.define('modelCtrSk', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrSk', type: 'int', mapping:'ctrsk'},
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrSk'
				});
				
				Ext.define('modelProdSk', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'prodSk', type: 'int', mapping:'prodsk'},
				        {name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ],
				    idProperty: 'prodSk'
				});
				
				var idFormulario = 'formulario';
				Ext.create('Ext.container.Container',{
                    //xtype: 'container',
                    renderTo:'container',
                    layout: 'hbox',
                    defaultType: 'textfield',
                    defaults:{
                    	labelAlign:'right',
                    	margin: '20 0 4 0'
                    },
                    items: [{
      	        		xtype:'container',html:'<br/>'
      	        	},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="production.ctrSk"/>',
	      	                id:'ctrSkCmb',
	      	                allowBlank:true,
	      	            	store: Ext.create('Ext.data.Store',{
					        	model: 'modelCtrSk',
					        	data: {registers: <%=request.getAttribute("countriesCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'ctrDesc',
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'ctrDesc',
					        valueField: 'ctrSk',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
								buffer: 100,
					        	afterrender:function(){
					        		var tmpStore = this.store;
					        		
					        		tmpStore.suspendEvents();
					        		tmpStore.clearFilter();
					        		tmpStore.resumeEvents();
					        		
					        		tmpStore.filter([{
    					            	id: 'ctrId',
					        			property: 'ctrId',
					        			exactMatch: true,
    					            	value: userCtrId
    					            }]);

					        		//Now we set user's country as default value in the combobox
					        		var tmpSk = tmpStore.getAt(0).get('ctrSk');
					        		tmpStore.suspendEvents();
					        		tmpStore.clearFilter();
					        		tmpStore.resumeEvents();
					        		
					        		this.setValue(tmpSk);
					        		new Ext.util.DelayedTask().delay(300,function(){
					        			Ext.getCmp('ctrSk').setValue(tmpSk);
					        		});
					        	},
					        	change: function(field, newValue, oldValue){
					        		if( Ext.isEmpty(oldValue) ){
										new Ext.util.DelayedTask().delay(300,function(){
											Ext.getCmp('ctrSk').setValue(newValue);
										});
									}
					        	},
								select: function(){
									Ext.getCmp(idFormulario+'-btnCancel').fireEvent('click');
								}
					        }
	      	            },{
	      	                xtype: 'numberfield',
                        	fieldLabel: '<bean:message key="production.yearSk"/>',
	      	                id: 'yearSkFiltro',
	      	              	minValue: 1990,
							width: 170,
	      	              	value: Number(Ext.Date.format(new Date(),'Y')),
	      	                allowBlank:false,
	      	            	blankText: '<bean:message key="production.yearSk.required" />',
	      	            	hideTrigger: false,
	      	              	keyNavEnabled: true,
	      	              	mouseWheelEnabled: false,
	      	              	listeners:{
		      	              	afterrender:function(){
									new Ext.util.DelayedTask().delay(300,function(){
										Ext.getCmp('yearSk').setValue(Ext.getCmp('yearSkFiltro').getValue());
					        		});
								},
								change: function(field, newValue, oldValue){
									if(Ext.isEmpty(oldValue) || oldValue===0){
										new Ext.util.DelayedTask().delay(500,function(){
											Ext.getCmp('yearSk').setValue(field.getValue());
										});
									}else{
										Ext.getCmp('yearSk').setValue(field.getValue());
									}
									Ext.getCmp(idFormulario+'-btnCancel').fireEvent('click');
								}
	      	              	}
	      	            }
                    ]
  	        	});
				
				//************************************************************************************
				///var globalMargin = '0 75 5 0';
				Ext.create('Ext.custom.FormPanel',{
					id: idFormulario,
					url:'productionAction.do',
					standardSubmit: false,
					habBtnDelete: true,
					labelWidth: 130,
					width: 320,
					title: '<bean:message key="production.title" />',
					onFailure: function(){
						Ext.getCmp('yearSk').setValue(Ext.getCmp('yearSkFiltro').getValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrSk: Ext.getCmp('ctrSk').getValue(),
			                yearSk: Ext.getCmp('yearSkFiltro').getValue()
			            };
					},
					cancelFn: function(){
						Ext.getCmp('yearSk').setValue(Ext.getCmp('yearSkFiltro').getValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
						
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrSk: Ext.getCmp('ctrSk').getValue(),
			                yearSk: Ext.getCmp('yearSk').getValue()
			            };
						//var m = wait();
						Ext.getStore('dataStore').load();
						return true;
					},
					afterCancelFn: function(){
						Ext.getCmp('yearSk').setValue(Ext.getCmp('yearSkFiltro').getValue());
						Ext.getCmp('ctrSk').setValue(Ext.getCmp('ctrSkCmb').getValue());
					},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            collapsed: false,
	      	            collapsible:true,
	      	            layout: 'anchor',
	      	            defaults: {
	      	                anchor: '100%',
							labelAlign:'right'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'productionId',
	      	        		name:'productionId'
	      	        	},{
	                		xtype:'hidden',
	                		id:'ctrSk',
	                		name:'ctrSk'
                		},{
	                		xtype:'hidden',
	                		id:'yearSk',
	                		name:'yearSk'
                		},{
							xtype: 'combo',
							fieldLabel: '<bean:message key="production.prodSk"/>',
							id:'prodSk',
							name: 'prodSk',
							allowBlank:false,
							store: 	Ext.create('Ext.data.Store',{
								model: 'modelProdSk',
								data: {registers: <%=request.getAttribute("productsCollection")%>},
								proxy:{
									type: 'memory',
									reader: {
										type: 'json',
										root: 'registers'
									}
								},
								sorters: [{
									property: 'prodSk',
									direction: 'ASC'
								}]
							}),
							queryMode: 'local',
							displayField: 'prodDesc',
							valueField: 'prodSk',
							editable: true,
							forceSelection: true,
							emptyText: '<bean:message key="combo.emptyText"/>'
	      	        	},{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="production.harvestedArea"/>',
							id: 'harvestedArea',
							name: 'harvestedArea',
							minValue: 0,
							allowBlank:true,
							blankText: '<bean:message key="production.harvestedArea.required" />',
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="production.volProd"/>',
							id: 'volProd',
							name: 'volProd',
							minValue: 0,
							allowBlank:true,
							blankText: '<bean:message key="production.volProd.required" />',
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="production.maintenanceCost"/>',
							id: 'maintenanceCost',
							name: 'maintenanceCost',
							minValue: 0,
							allowBlank:true,
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="production.establishmentCost"/>',
							id: 'establishmentCost',
							name: 'establishmentCost',
							minValue: 0,
							allowBlank:true,
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="production.productionSystem"/>',
	      	                id:'productionSystem',
	      	                name: 'productionSystem',
	      	                value:null,
	      	                //maxLength: 1,
	      	              	allowBlank:true,
	      	              	//width: 200,
					        store: 	Ext.create('Ext.data.Store',{
					        	fields: ['name','value'],
					        	data: [
					        		{'name':'---', 'value':null},
					        		{'name':'<bean:message key="production.productionSystem.low" />', 'value':1},
					        		{'name':'<bean:message key="production.productionSystem.intermediate" />', 'value':2},
					        		{'name':'<bean:message key="production.productionSystem.high" />', 'value':3}
					        	]
					        }),
					        queryMode: 'local',
					        displayField: 'name',
					        valueField: 'value',
					        editable: false
	      	            },/*{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="production.costProd"/>',
							id: 'costProd',
							name: 'costProd',
							minValue: 0,
							allowBlank:true,
							blankText: '<bean:message key="production.costProd.required" />',
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},*/{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
							xtype: 'checkboxgroup',
							fieldLabel: '<bean:message key="production.harvestCalendar" />',
							// Arrange radio buttons into two columns, distributed vertically
							labelAlign:'top',
							columns: 3,
							vertical: true,
							items: [
								{ boxLabel: '<bean:message key="production.jan" />', name: 'jan'},
								{ boxLabel: '<bean:message key="production.feb" />', name: 'feb'},
								{ boxLabel: '<bean:message key="production.mar" />', name: 'mar'},
								{ boxLabel: '<bean:message key="production.apr" />', name: 'apr'},
								{ boxLabel: '<bean:message key="production.may" />', name: 'may'},
								{ boxLabel: '<bean:message key="production.jun" />', name: 'jun'},
								{ boxLabel: '<bean:message key="production.jul" />', name: 'jul'},
								{ boxLabel: '<bean:message key="production.aug" />', name: 'aug'},
								{ boxLabel: '<bean:message key="production.sep" />', name: 'sep'},
								{ boxLabel: '<bean:message key="production.oct" />', name: 'oct'},
								{ boxLabel: '<bean:message key="production.nov" />', name: 'nov'},
								{ boxLabel: '<bean:message key="production.dec" />', name: 'dec'}
							]
						}
	      	        ]//FieldSet
					}]//Items Form.Panel
				});
				
								
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'productionId', type: 'number', mapping:'productionid'},
			            {name: 'yearSkFiltro', type: 'int', mapping:'yearsk'},
						{name: 'yearSk', type: 'int', mapping:'yearsk'},
			            {name: 'ctrSkCmb', type: 'number', mapping:'ctrsk'},
						{name: 'ctrSk', type: 'number', mapping:'ctrsk'},
			            
			            {name: 'harvestedArea', type: 'string', mapping:'harvestedarea'},
			            {name: 'prodSk', type: 'number', mapping:'prodsk'},
			            
			            {name: 'volProd', type: 'string', mapping:'volprod'},
			            {name: 'costProd', type: 'string', mapping:'costprod'},
			            {name: 'maintenanceCost', type: 'string', mapping:'maintenancecost'},
			            {name: 'establishmentCost', type: 'string', mapping:'establishmentcost'},
			            {name: 'productionSystem', type: 'number', mapping:'productionsystem'},
			            {name: 'productionSystemText', type: 'string', mapping:'productionsystemtext'},
			            
						{name: 'jan', type: 'boolean', mapping:'jan'},
						{name: 'feb', type: 'boolean', mapping:'feb'},
						{name: 'mar', type: 'boolean', mapping:'mar'},
						{name: 'apr', type: 'boolean', mapping:'apr'},
						{name: 'may', type: 'boolean', mapping:'may'},
						{name: 'jun', type: 'boolean', mapping:'jun'},
						{name: 'jul', type: 'boolean', mapping:'jul'},
						{name: 'aug', type: 'boolean', mapping:'aug'},
						{name: 'sep', type: 'boolean', mapping:'sep'},
						{name: 'oct', type: 'boolean', mapping:'oct'},
						{name: 'nov', type: 'boolean', mapping:'nov'},
						{name: 'dec', type: 'boolean', mapping:'dec'},
			            
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'}
			        ],
			        idProperty: 'productionId'
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
			            url: 'productionAction.do?accion=load',
			            extraParams: {
			                ctrSk: Ext.getCmp('ctrSk').getValue(),
			                yearSk: Ext.getCmp('yearSkFiltro').getValue()
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
			            property: 'productionId',
			            direction: 'DESC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'int',
		                dataIndex: 'yearSkFiltro'
		            }, {
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'oriCtrDesc'
		            }, {
		                type: 'numeric',
		                dataIndex: 'volProd'
		            },{
		                type: 'numeric',
		                dataIndex: 'harvestedArea'
		            },{
		                type: 'numeric',
		                dataIndex: 'maintenanceCost'
		            },{
		                type: 'numeric',
		                dataIndex: 'establishmentCost'
		            },{
		                type: 'list',
		                dataIndex: 'productionSystemText',
		                options: ['<bean:message key="production.productionSystem.low" />', 
		                          '<bean:message key="production.productionSystem.intermediate" />',
		                          '<bean:message key="production.productionSystem.high" />'
		                          ]
		            },{
						type: 'boolean',
						dataIndex: 'jan'
					},{
						type: 'boolean',
						dataIndex: 'feb'
					},{
						type: 'boolean',
						dataIndex: 'mar'
					},{
						type: 'boolean',
						dataIndex: 'apr'
					},{
						type: 'boolean',
						dataIndex: 'may'
					},{
						type: 'boolean',
						dataIndex: 'jun'
					},{
						type: 'boolean',
						dataIndex: 'jul'
					},{
						type: 'boolean',
						dataIndex: 'aug'
					},{
						type: 'boolean',
						dataIndex: 'sep'
					},{
						type: 'boolean',
						dataIndex: 'oct'
					},{
						type: 'boolean',
						dataIndex: 'nov'
					},{
						type: 'boolean',
						dataIndex: 'dec'
					}]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 740,
			        height: 300,
			        title: '<bean:message key="production.title.table" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
						text:'#',
			        	width: 35, 
			        	sortable: false
			        },{
			            text: '<bean:message key="production.yearSk" />',
			            dataIndex: 'yearSk',
			            width: 40,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="production.prodSk" />',
			            dataIndex: 'prodDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="production.grid.harvestedArea" />',
			            dataIndex: 'harvestedArea',
			            width: 100,
			            sortable: true,
						renderer : Ext.util.Format.numberRenderer('0,000.00 Ha')
			        },{
			            text: '<bean:message key="production.grid.volProd" />',
			            dataIndex: 'volProd',
			            width: 100,
			            sortable: true,
						renderer : Ext.util.Format.numberRenderer('0,000.00 Kg.')
			        },{
			            text: '<bean:message key="production.grid.maintenanceCost" />',
			            dataIndex: 'maintenanceCost',
			            width: 100,
			            renderer : function(value){
							if(value && value !=''){
								return Ext.util.Format.usMoney(value);
							}else{
								return '';
							}
						},
			            sortable: true
			        },{
			            text: '<bean:message key="production.grid.establishmentCost" />',
			            dataIndex: 'establishmentCost',
			            width: 100,
			            renderer : function(value){
							if(value && value !=''){
								return Ext.util.Format.usMoney(value);
							}else{
								return '';
							}
						},
			            sortable: true
			        },{
			            text: '<bean:message key="production.grid.productionSystem" />',
			            dataIndex: 'productionSystemText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
			            sortable: true
			        },/*{
			            text: '<bean:message key="production.grid.costProd" />',
			            dataIndex: 'costProd',
			            width: 100,
			            renderer : function(value){
							if(value && value !=''){
								return Ext.util.Format.usMoney(value);
							}else{
								return '';
							}
						},
			            sortable: true
			        },*/{
			            text: '<bean:message key="production.grid.jan" />',
			            dataIndex: 'jan',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.feb" />',
			            dataIndex: 'feb',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.mar" />',
			            dataIndex: 'mar',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.apr" />',
			            dataIndex: 'apr',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.may" />',
			            dataIndex: 'may',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.jun" />',
			            dataIndex: 'jun',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.jul" />',
			            dataIndex: 'jul',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.aug" />',
			            dataIndex: 'aug',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.sep" />',
			            dataIndex: 'sep',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.oct" />',
			            dataIndex: 'oct',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.nov" />',
			            dataIndex: 'nov',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        },{
			            text: '<bean:message key="production.grid.dec" />',
			            dataIndex: 'dec',
			            width: 30,
			            sortable: true,
						renderer: function(value){return value?'X':'';}
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    //new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
	      	});//onReady
		</script>
		
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="production.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="production.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>