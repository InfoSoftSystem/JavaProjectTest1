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
				//Ext.getCmp('GridPanel').fireEvent('position',{"this":Ext.getCmp('GridPanel'), eOpts: {}});
			}
			
			
			Ext.onReady(function(){
				<%
					UserForm user = (UserForm) session.getAttribute("user");
				%>
		        //Property used to choose the country column according to current language 
		        //var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var globalRolId = null;
		      	//************************************************************************************************
				//*** Models for Forms
				
				Ext.define('modelRolId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'rolId', type: 'numeric', mapping: 'rolid'},
				        {name: 'rolDesc', type: 'string', mapping: 'roldesc'}
				    ],
				    idProperty: 'rolId'
				});
		      	
				Ext.define('modelOptId', {
				    extend: 'Ext.data.Model',
			        fields: [
			            {name: 'optId', type: 'number', mapping:'optid'},
			            {name: 'optDesc', type: 'string', mapping:'optdesc'},
			            {name: 'optDescMsg', type: 'string', mapping:'optdescmsg'},
			            {name: 'grpId', type: 'number', mapping:'grpid'},
			            {name: 'grpDesc', type: 'string', mapping:'grpdesc'},
			            {name: 'grpDescMsg', type: 'string', mapping:'grpdescmsg'},
			            {name: 'optUrl', type: 'string', mapping:'opturl'},
			            {name: 'optVisible', type: 'string', mapping:'optvisible'},
			            {name: 'optIconCSS', type: 'string', mapping:'opticoncss'},
			            {name: 'optOrder', type: 'number', mapping:'optorder'},
			            {name: 'optMsgProperty', type: 'string', mapping:'optmsgproperty'},
			            {name: 'selected', type: 'boolean', mapping:'selected'}
			        ],
			        idProperty: 'optId'
			    });

				
				var storeOptions = Ext.create('Ext.data.Store', {
			        id: 'storeOptions',
			        model: 'modelOptId',
			        remoteSort: false,
			        remoteFilter: false,
			        remoteGroup:false,
			        proxy: {
			            type: 'ajax',
			            url: 'optionRolAction.do?accion=load&store=storeOptions',
			            reader: {
			            	type: 'json',
			                root: 'registers'
			            }
			        },
		        	sorters: [{
			            property: 'optDescMsg',
			            direction: 'ASC'
			        }],
			        autoLoad: false
			    });
				
				
				function loadOptions(){
					var tmpStore = storeOptions;
	        		
					if(!Ext.isEmpty(Ext.getCmp('rolId').getValue())){
		        		tmpStore.proxy.extraParams={
			                rolId: Ext.getCmp('rolId').getValue()
			            };
		        		
		        		if(Ext.isEmpty(globalRolId)){
							var m = wait();
		        		}
		        		
						tmpStore.load({
							scope:this,
							callback:function(records, operation, success){
								
								if(Ext.isEmpty(globalRolId)){
									m.close();
								}
								
								var tmpComponent = Ext.getCmp('optId');
								
								tmpComponent.clearValue();
								
								//if(tmpComponent.store.getCount()>0){
								tmpComponent.fromField.store.removeAll();
								tmpComponent.toField.store.removeAll();
								//}
								
								if(success){
									//All posible options are loaded into the main Store
									tmpComponent.store.add(storeOptions.getRange());
									
									//let's filter by the selected, and the by the still available
									storeOptions.suspendEvents();
									storeOptions.clearFilter();
									storeOptions.resumeEvents();
						        		
									storeOptions.filter([{
	   					            	id: 'selected',
					        			property: 'selected',
					        			exactMatch: true,
	   					            	value: true
	   					            }]);
									
									tmpComponent.toField.store.add(storeOptions.getRange());
									
									//Now let's filter by the still available
									storeOptions.suspendEvents();
									storeOptions.clearFilter();
									storeOptions.resumeEvents();
						        		
									storeOptions.filter([{
	   					            	id: 'selected',
					        			property: 'selected',
					        			exactMatch: true,
	   					            	value: false
	   					            }]);
									
									tmpComponent.fromField.store.add(storeOptions.getRange());
									
									storeOptions.suspendEvents();
									storeOptions.clearFilter();
									storeOptions.resumeEvents();
									
									tmpComponent.syncValue();
									
									tmpComponent.fireEvent('change');
								}
							}
						});
					}
				}
				
				
				//************************************************************************************************
				
				
				var cstFormPanel = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'optionRolAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					width: 550,
					title: '<bean:message key="optionRol.header" />',
					afterCancelFn: function(){
						this.reportMode();
						Ext.getCmp('rolId').setValue(globalRolId);
						
						new Ext.util.DelayedTask().delay(1000,function(){
							globalRolId=null;
						});						
						
					},
					items: [{
						xtype:'hidden',
						name:'rolDesc',
						id:'rolDesc'
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
	      	                fieldLabel: '<bean:message key="optionRol.rolDesc"/>',
	      	                id:'rolId',
	      	                name: 'rolId',
	      	                colspan:2,
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	width: 300,
	      	              	blankText:'<bean:message key="user.rolId.required"/>',
					        store: 	Ext.create('Ext.data.Store',{
					        	model: 'modelRolId',
					        	data: {registers: <%=request.getAttribute("rolesCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'rolDesc',
	      				            direction: 'DESC'
	      				        }]
					        }),
					        queryMode: 'local',
					        displayField: 'rolDesc',
					        valueField: 'rolId',
					        editable: false,
					        listeners:{
					        	change:function(obj, newValue, oldValue){
					        		loadOptions();
					        	}
					        }
					    },{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="optionRol.option.available" />'
	      	        	},{
	      	        		xtype:'label',
	      	        		baseCls:'x-form-item-label',
	      	        		margin: '10 0 0 0',
	      	        		align:'middle',
	      	        		text:'<bean:message key="optionRol.option.selected" />'
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
	      	                    name: 'optId',
					            id: 'optId',
					            hideLabel: true,
					            blankText: '<bean:message key="optionRol.options.selected.required"/>',
					            buttons:["add", "remove"],
					            store: storeOptions,
					            displayField: 'optDescMsg',
					            valueField: 'optId',
					            allowBlank: false,
					            listeners:{
					            	render: function(){
					            		this.clearValue();
					            	},
					            	change: function(){
					            		//var form = this.up('form.panel');
					            		
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
	      	            	text: '<bean:message key="opc.update" />',
	      	            	width: 100,
	        	          	listeners: {
								click: function(){
									Ext.getCmp('accion').setValue('ajaxUpdate');
									
									var form = this.up('form.panel');
									globalRolId = Ext.getCmp('rolId').getValue();
									Ext.getCmp('rolDesc').setValue(Ext.getCmp('rolId').getRawValue());
									
									if (form.getForm().isValid()){
										form._doCustomSubmit();
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
		<bean:message key="optionRol.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="optionRol.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>