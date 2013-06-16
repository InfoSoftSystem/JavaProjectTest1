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
				var ctrDisabled;
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
	      	                fieldLabel: '<bean:message key="comm.ctrSk"/>',
	      	                id:'ctrSkCmb',
	      	                allowBlank:true,
	      	            	disabled: false,//ctrDisabled,
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
                        	fieldLabel: '<bean:message key="comm.yearSk"/>',
	      	                id: 'yearSkFiltro',
	      	              	minValue: 1990,
							width: 170,
	      	              	value: Number(Ext.Date.format(new Date(),'Y')),
	      	                allowBlank:false,
	      	            	blankText: '<bean:message key="comm.yearSk.required" />',
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
				var globalMargin = '0 75 5 0';
				Ext.create('Ext.custom.FormPanel',{
					id: idFormulario,
					url:'commerceAction.do',
					standardSubmit: false,
					habBtnDelete: true,
					labelWidth: 130,
					width: 320,
					title: '<bean:message key="comm.title" />',
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
	      	        		id:'commId',
	      	        		name:'commId'
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
							fieldLabel: '<bean:message key="comm.prodSk"/>',
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
							xtype: 'combo',
							fieldLabel: '<bean:message key="comm.commType"/>',
							id:'commType',
							name: 'commType',
							allowBlank:false,
							store: 	Ext.create('Ext.data.Store',{
								fields: ['name','value'],
								data: [
									{'name':'<bean:message key="comm.commType.import" />', 'value':'I'},
									{'name':'<bean:message key="comm.commType.export" />', 'value':'E'}
								]
							}),
							queryMode: 'local',
							displayField: 'name',
							valueField: 'value',
							editable: false,
							forceSelection: true,
							emptyText: '<bean:message key="combo.emptyText"/>'
						},{
							xtype: 'combo',
							fieldLabel: '<bean:message key="comm.oriCtrSk"/>',
							id:'oriCtrSk',
							name: 'oriCtrSk',
							store: Ext.create('Ext.data.Store',{
								model: 'modelCtrSk',
								data: {registers: <%=request.getAttribute("oriCountriesCollection")%>},
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
							forceSelection: true,
							emptyText: '<bean:message key="combo.emptyText"/>',
							listeners:{
								buffer: 50,
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
								}
							}
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="comm.volKilo"/>',
							id: 'volKilo',
							name: 'volKilo',
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="comm.volKilo.required" />',
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="comm.volDollar"/>',
							id: 'volDollar',
							name: 'volDollar',
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="comm.volDollar.required" />',
							hideTrigger: true,
							keyNavEnabled: false,
							mouseWheelEnabled: false
						}
	      	        ]//FieldSet
					}]//Items Form.Panel
				});
				
								
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'commId', type: 'number', mapping:'commid'},
			            {name: 'yearSkFiltro', type: 'int', mapping:'yearsk'},
						{name: 'yearSk', type: 'int', mapping:'yearsk'},
			            {name: 'ctrSkCmb', type: 'number', mapping:'ctrsk'},
						{name: 'ctrSk', type: 'number', mapping:'ctrsk'},
			            
			            {name: 'oriCtrSk', type: 'number', mapping:'orictrsk'},
			            {name: 'prodSk', type: 'number', mapping:'prodsk'},
			            
			            {name: 'volKilo', type: 'number', mapping:'volkilo'},
			            {name: 'volDollar', type: 'number', mapping:'voldollar'},
			            {name: 'commType', type: 'string', mapping:'commtype'},
						{name: 'commTypeText', type: 'string', mapping:'commtypetext'},
			            
			            {name: 'oriCtrDesc', type: 'string', mapping: ctrMapping},
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'}
			        ],
			        idProperty: 'commId'
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
			            url: 'commerceAction.do?accion=load',
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
			            property: 'commId',
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
		                dataIndex: 'volKilo'
		            }, {
		                type: 'numeric',
		                dataIndex: 'volDollar'
		            }, {
		                type: 'list',
		                dataIndex: 'commTypeText',
		                options: ['<bean:message key="comm.commType.import" />', '<bean:message key="comm.commType.export" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 600,
			        height: 300,
			        title: '<bean:message key="comm.title.table" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			            text: '<bean:message key="comm.yearSk" />',
			            dataIndex: 'yearSk',
			            width: 80,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="comm.prodSk" />',
			            dataIndex: 'prodDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="comm.commType" />',
			            dataIndex: 'commTypeText',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="comm.grid.oriCtrSk" />',
			            dataIndex: 'oriCtrDesc',
			            width: 100,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="comm.grid.volKilo" />',
			            dataIndex: 'volKilo',
			            width: 100,
			            sortable: true,
						renderer : Ext.util.Format.numberRenderer('0,000.00 Kg.')
			        },{
			            text: '<bean:message key="comm.grid.volDollar" />',
			            dataIndex: 'volDollar',
			            width: 100,
			            renderer : 'usMoney',
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    //new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
	      	});//onReady
		</script>
		
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="comm.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="comm.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>