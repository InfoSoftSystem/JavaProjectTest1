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
				<%
		        	UserForm user = (UserForm) session.getAttribute("user");
		        %>
				
		        //Property used to choose the country column according to current language 
		        var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var userCtrId = <%=user.getCtrId()%>;
		        
				//************************************************************************************************
				//*** Models for Forms
				
				Ext.define('modelCtrId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
				
				Ext.define('modelProdId', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ],
				    idProperty: 'prodId'
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
				
				
				function loadProductTypes(){
					
					if(!Ext.isEmpty(Ext.getCmp('prodId').getValue())){
						var ptypeSk = Ext.getCmp('ptypeId');
		        		
						ptypeSk.enable();
		        		//var tmp = varSk.getValue();
						var tmpStore = Ext.getStore('storeProductTypes');
		        		
		        		tmpStore.proxy.extraParams={
	        				prodId: Ext.getCmp('prodId').getValue()
			            };
		        		
						var m = wait();
						
						//Ext.getCmp('varId').setValue('');
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								Ext.getCmp('ptypeId').focus();
								
								if(tmpStore.getCount() == 0){
									ptypeSk.disable();
									new Ext.util.DelayedTask().delay(300,function(){
										Ext.getCmp('varId').focus();
									});
								}
								
								loadVarieties();
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
			                prodId: Ext.getCmp('prodId').getValue()
			            };
		        		
						var m = wait();
						
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								m.close();
								
								if(tmpStore.getCount() == 0){
									varSk.disable();
									Ext.getCmp('ptypeId').focus();
								}
								
								var ptypeIdCpy = Ext.getCmp('ptypeIdCpy');
								var varIdCpy = Ext.getCmp('varIdCpy');
								
								if(!Ext.isEmpty(ptypeIdCpy.getValue())){
									Ext.getCmp('ptypeId').setValue(new Number(ptypeIdCpy.getValue()).valueOf());
								}
								
								if(!Ext.isEmpty(varIdCpy.getValue())){
									varSk.setValue(new Number(varIdCpy.getValue()).valueOf());
								}
							}
						});
					}
				}
				
				
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
	      	                id:'ctrIdCmb',
	      	                allowBlank:false,
	      	                disabled: false,//ctrDisabled,
					        store: Ext.create('Ext.data.Store',{
					        	model: 'modelCtrId',
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
					        valueField: 'ctrId',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        blankText: '<bean:message key="priceDateException.ctrId.required"/>',
					        listeners:{
								//buffer: 100,
					        	afterrender:function(){

					        		//Now we set user's country as default value in the combobox
					        		//var tmpSk = tmpStore.getAt(0).get('ctrId');
					        		//tmpSk = userCtrId;
					        		
					        		this.setValue(userCtrId);
					        		new Ext.util.DelayedTask().delay(200,function(){
					        			Ext.getCmp('ctrId').setValue(userCtrId);
					        		});
					        	},
					        	change: function(field, newValue, oldValue){
					        		if( Ext.isEmpty(oldValue) ){
										new Ext.util.DelayedTask().delay(200,function(){
											Ext.getCmp('ctrId').setValue(newValue);
										});
									}
					        	},
								select: function(){
									Ext.getCmp(idFormulario+'-btnCancel').fireEvent('click');
								}
					        }
	      	            }
                    ]
  	        	});
				
				//************************************************************************************
				Ext.create('Ext.custom.FormPanel',{
					id: idFormulario,
					url:'priceDateExceptionAction.do',
					standardSubmit: false,
					collapsible: false,
					habBtnDelete: true,
					labelWidth: 100,
					width: 320,
					title: '<bean:message key="priceDateException.header" />',
					onFailure: function(){
						Ext.getCmp('ctrId').setValue(Ext.getCmp('ctrIdCmb').getValue());
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrId: Ext.getCmp('ctrId').getValue()
			            };
					},
					cancelFn: function(){
						Ext.getCmp('ctrId').setValue(Ext.getCmp('ctrIdCmb').getValue());
						
						Ext.getStore('dataStore').proxy.extraParams={
   							ctrId: Ext.getCmp('ctrId').getValue()
			            };
						//var m = wait();
						Ext.getStore('dataStore').load();
						return true;
					},
					afterCancelFn: function(){
						Ext.getCmp('ctrId').setValue(Ext.getCmp('ctrIdCmb').getValue());
					},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            collapsed: false,
	      	            collapsible:false,
	      	            layout: 'anchor',
	      	            defaults: {
	      	                anchor: '100%',
							labelAlign:'right'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'pdeId',
	      	        		name:'pdeId'
	      	        	},{
	                		xtype:'hidden',
	                		id:'ctrId',
	                		name:'ctrId'
                		},{
	                		xtype:'hidden',
	                		id:'varIdCpy'
                		},{
	                		xtype:'hidden',
	                		id:'ptypeIdCpy'
                		},{
							xtype: 'datefield',
							fieldLabel: '<bean:message key="price.dateField"/>',
							id: 'dateField',
							name: 'dateField',
							format: 'd/m/Y',
							allowBlank:false,
							blankText: '<bean:message key="priceDateException.dateField.required"/>'
	      	            },{
							xtype: 'combo',
							fieldLabel: '<bean:message key="price.prodId"/>',
							id:'prodId',
							name: 'prodId',
							allowBlank:true,
							blankText: '<bean:message key="priceDateException.prodId.required"/>',
							store:	Ext.create('Ext.data.Store',{
								id: 'storeProducts',
								model: 'modelProdId',
								remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'priceDateExceptionAction.do?accion=load&store=loadProducts',
						            reader: {
						            	type: 'json',
						                root: 'registers'
						            }
						        },
						        autoLoad: true,
								sorters: [{
									property: 'prodId',
									direction: 'ASC'
								}]
							}),
							queryMode: 'local',
							displayField: 'prodDesc',
							valueField: 'prodId',
							//editable: true,
							forceSelection: true,
							emptyText: '<bean:message key="combo.emptyText"/>',
							listeners:{
								change: loadProductTypes
							}
	      	        	},{
	      	              	xtype: 'combo',
	      	              	fieldLabel: '<bean:message key="price.ptypeId"/>',
	      	                id:'ptypeId',
	      	                disabled: true,
	      	              	name:'ptypeId',
	      	                allowBlank:true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeProductTypes',
						        model: 'modelPtypeId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'priceDateExceptionAction.do?accion=load&store=loadProductTypes',
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
					        emptyText: '<bean:message key="combo.emptyText"/>'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="price.varId"/>',
	      	                id:'varId',
	      	                name: 'varId',
	      	                disabled:true,
	      	              	allowBlank:true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: Ext.create('Ext.data.Store', {
						        id: 'storeVarieties',
						        model: 'modelVarId',
						        remoteSort: false,
						        remoteFilter: false,
						        remoteGroup:false,
						        proxy: {
						            type: 'ajax',
						            url: 'priceDateExceptionAction.do?accion=load&store=loadVarieties',
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
					        emptyText: '<bean:message key="combo.emptyText"/>'
	      	            },{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
							xtype:'textareafield',
							fieldLabel: '<bean:message key="priceDateException.pdeComment"/>',
							//grow:true,
							id:'pdeComment',
							name: 'pdeComment',
							anchor: '100%',
							allowBlank:false,
							blankText: '<bean:message key="priceDateException.pdeComment.required"/>'
						}
	      	        ]//FieldSet
					}]//Items Form.Panel
				});
				
								
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
						{name: 'pdeId', type: 'number', mapping:'pdeid'},
						{name: 'dateField', type: 'date', mapping:'datefield', dateFormat:'Y-m-d'},
			            {name: 'ctrIdCmb', type: 'number', mapping:'ctrid'},
						{name: 'ctrId', type: 'number', mapping:'ctrid'},
			            
			            {name: 'prodId', type: 'number', mapping:'prodid'},
			            {name: 'ptypeIdCpy', type: 'number', mapping:'ptypeid'},
			            {name: 'varIdCpy', type: 'number', mapping:'varid'},
			            /*{name: 'ptypeId', type: 'number', mapping:'ptypeid'},
			            {name: 'varId', type: 'number', mapping:'varid'},*/
			            
			            {name: 'pdeComment', type: 'string', mapping:'pdecomment'},
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'},
			            {name: 'varDesc', type: 'string', mapping:'vardesc'},
			            {name: 'ptypeDesc', type: 'string', mapping:'ptypedesc'},
						{name: 'commTypeText', type: 'string', mapping:'commtypetext'},
			            
			            {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
			        ],
			        idProperty: 'pdeId'
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
			            url: 'priceDateExceptionAction.do?accion=load&store=loadGrid',
			            extraParams: {
			                ctrId: Ext.getCmp('ctrId').getValue()
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
			            property: 'dateField',
			            direction: 'DESC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'ptypeDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'varId'
		            }, {
		                type: 'date',
		                dataIndex: 'dateField'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 700,
			        height: 220,
			        title: '<bean:message key="priceDateException.title.table" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters],
			     	// grid columns
			     	afterLoadReg:function(){
			     		
			     	},
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	xtype:'datecolumn',
			        	text: '<bean:message key="price.dateField" />',
			            dataIndex: 'dateField',
			            width: 80,
			            flex:1,
			            format: 'd/m/Y',
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="price.prodId" />',
			            dataIndex: 'prodDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.ptypeId" />',
			            dataIndex: 'ptypeDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="price.varId" />',
			            dataIndex: 'varDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="priceDateException.pdeComment" />',
			            dataIndex: 'pdeComment',
			            width: 300,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(500,function(){
			    	Ext.getStore('dataStore').proxy.extraParams={
							ctrId: Ext.getCmp('ctrId').getValue()
		            };
			    	
			    	Ext.getStore('dataStore').load();
			    });
			    
	      	});//onReady
		</script>
		
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="priceDateException.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="priceDateException.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>