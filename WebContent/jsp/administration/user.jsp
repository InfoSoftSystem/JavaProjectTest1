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
		        
		        
				Ext.define('modelCtrId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
				
				Ext.define('modelRolId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'rolId', type: 'numeric', mapping: 'rolid'},
				        {name: 'rolDesc', type: 'string', mapping: 'roldesc'}
				    ],
				    idProperty: 'rolId'
				});
		        
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'userAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					habBtnDelete: true,
					width: 550,
					title: '<bean:message key="user.header" />',
					cancelFn: function(){
						Ext.getStore('dataStore').load(); 
						Ext.getCmp('password').enable();
						Ext.getCmp('confirmPsw').enable();
						Ext.getCmp('btnChangePassword').disable();
						
						Ext.getCmp('flgPasswordUpd').setValue(false);
						return true;
						},
					items: [{
      	        		xtype:'hidden',
      	        		id:'userId',
      	        		name:'userId'
      	        	},{
      	        		xtype:'hidden',
      	        		id:'flgPasswordUpd',
      	        		name:'flgPasswordUpd'
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
	      	            	width:250, 
	      	            	height: 25,
	      	            	labelAlign:'right',
	      	            	margin: '0 0 0 0'
	      	            },
	      	            /*defaults: {
	      	                anchor: '100%'
	      	            },*/
	      	            items :[
						{
	      	                fieldLabel: '<bean:message key="user.username"/>',
	      	                id: 'username',
	      	                name: 'username',
	      	              	maxLength: 50,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="user.username.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="user.namePerson"/>',
	      	                id: 'namePerson',
	      	                name: 'namePerson',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="user.namePerson.required" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="user.email"/>',
	      	                id: 'email',
	      	                name: 'email',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	vtype: 'email',
	      	            	blankText: '<bean:message key="user.email.required" />'
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="user.ctrId"/>',
	      	                id:'ctrId',
	      	              	name:'ctrId',
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
					        blankText: '<bean:message key="user.ctrId.required"/>'
	      	            },{
	      	                fieldLabel: '<bean:message key="user.phone"/>',
	      	                id: 'phone',
	      	                name: 'phone',
	      	              	maxLength: 50,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:true
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="user.rolId"/>',
	      	                id:'rolId',
	      	              	name:'rolId',
	      	                allowBlank:false,
	      	                disabled: false,//ctrDisabled,
					        store: Ext.create('Ext.data.Store',{
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
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'rolDesc',
					        valueField: 'rolId',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        blankText: '<bean:message key="user.rolId.required"/>'
	      	            },{
	      	            	xtype: 'button',
	      	            	id:'btnChangePassword',
	      	            	colspan:2,
	      	            	disabled:true,
	      	            	text: '<bean:message key="user.generate" />',
	      	            	margin:'15 0 0 0',
	          	            width: 115,
	          	          	listeners: {
								click: function(){
									Ext.getCmp('password').enable();
									Ext.getCmp('confirmPsw').enable();
									Ext.getCmp('flgPasswordUpd').setValue(true);
								}
							}
	          	        },{
	      	            	inputType: 'password',
	      	            	fieldLabel: '<bean:message key="user.password"/>',
	      	                id: 'password',
	      	                name: 'password',
	      	              	maxLength: 20,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="user.password.required" />',
	      	            	validator: function(value){
	      	              		Ext.getCmp('confirmPsw').validate();
	      	              		return true;
	      	              	}
	      	            },{
	      	            	inputType: 'password',
	      	            	fieldLabel: '<bean:message key="user.confirmPsw"/>',
	      	                id: 'confirmPsw',
	      	                name: 'confirmPsw',
	      	              	maxLength: 20,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="user.confirmPsw.required" />',
	      	            	validator:function(value){
	      	              		var tmpSup = Ext.getCmp('password').getValue();
	      	              		
	      	              		if(value!=tmpSup){
	      	              			return '<bean:message key="user.password.confirm"/>';
	      	              		}
	      	              		return true;
	      	              	}
	      	            }]//Items formfield
	      	        },{
                        xtype: 'container',
                        layout: 'hbox',
                        defaultType: 'textfield',
                        defaults:{
                        	labelAlign:'right'//,
                        	//margin: globalMargin
                        },
                        items: [{
      	        			xtype:'container',
      	        			width:125,
      	        			html:'<bean:message key="user.notify" />:<br/>'
	      	        	},{
	      	            	colspan: 2,
	      	            	xtype: 'checkboxgroup',
	      	            	width:150,
							//fieldLabel: '<bean:message key="user.notify" />',
							// Arrange radio buttons into two columns, distributed vertically
							//labelAlign:'top',
							columns: 1,
							vertical: true,
							items:[
								{ 
									boxLabel: '<bean:message key="user.notifyPrice" />', 
									name: 'notifyPrice'
								},
								{ 
									boxLabel: '<bean:message key="user.notifyCommerce" />', 
									name: 'notifyCommerce'
								},
								{ 
									boxLabel: '<bean:message key="user.notifyProduction" />', 
									name: 'notifyProduction'
								}
							]
						},{
	      	        		xtype:'container',
	      	        		width:175,
	      	        		html:'<br/><bean:message key="user.noCountry" />:<br/>'
	      	        	},{
	      	            	colspan: 2,
	      	            	xtype: 'radiogroup',
							//fieldLabel: '<bean:message key="user.notify" />',
							// Arrange radio buttons into two columns, distributed vertically
							//labelAlign:'top',
							id:'chkVariable',
							columns: 1,
							vertical: true,
							items:[
								{ 
									xtype: 'radiofield', 
		  	                    	boxLabel: '<bean:message key="global.yes" />', 
									name: 'noCountry',
									inputValue:'true'
								},
								{ 
									xtype: 'radiofield', 
		  	                    	boxLabel: '<bean:message key="global.no" />', 
									name: 'noCountry',
									inputValue:'false'
								}
							]
						}]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'userId', type: 'number', mapping:'userid'},
			            {name: 'rolId', type: 'number', mapping:'rolid'},
			            {name: 'rolDesc', type: 'string', mapping:'roldesc'},
			            {name: 'ctrId', type: 'number', mapping:'ctrid'},
			            {name: 'ctrDesc', type: 'string', mapping: ctrMapping},
			            {name: 'username', type: 'string', mapping:'username'},
			            {name: 'noCountry', type: 'string', mapping:'nocountry'},
			            {name: 'namePerson', type: 'string', mapping:'nameperson'},
			            {name: 'email', type: 'string', mapping:'email'},
			            {name: 'phone', type: 'string', mapping:'phone'},
			            {name: 'notifyPrice', type: 'boolean', mapping:'notifyprice'},
			            {name: 'notifyCommerce', type: 'boolean', mapping:'notifycommerce'},
			            {name: 'notifyProduction', type: 'boolean', mapping:'notifyproduction'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'flgPasswordUpd', type: 'boolean', mapping:'flgpasswordupd'}
			        ],
			        idProperty: 'userId'
			    });
				
								
				// create the Data Store
			    var store = Ext.create('Ext.data.Store', {
			        id: 'dataStore',
			        //pageSize: 50,
			        groupField: 'ctrDesc',
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
			            url: 'userAction.do?accion=load',
			            extraParams: {
			                total: 50000
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
			            property: 'userId',
			            direction: 'ASC'
			        }]
			    });//Store
			    
			    var filters = {
		            ftype: 'filters',
		            // encode and local configuration options defined previously for easier reuse
		            encode: false, // json encode the filter query
		            local: true,   // defaults to false (remote filtering)
		            filters: [{
		                type: 'string',
		                dataIndex: 'rolDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'username'
		            }, {
		                type: 'string',
		                dataIndex: 'namePerson'
		            }, {
		                type: 'string',
		                dataIndex: 'email'
		            }]
		        };
			    
			    var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			        groupHeaderTpl: '<bean:message key="optionGroup.grpId" />: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
			        hideGroupedHeader: true
			    });
			    
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 650,
			        height: 300,
			        title: '<bean:message key="user.header" />',
			        store: store,
			        idForm: 'formulario',
			        features: [filters, groupingFeature],
					afterLoadReg:function(){
						Ext.getCmp('password').setValue('');
						Ext.getCmp('confirmPsw').setValue('');
						Ext.getCmp('password').disable();
						Ext.getCmp('confirmPsw').disable();
						Ext.getCmp('btnChangePassword').enable();
			     	},
			     	// grid columns
			        columns:[
			        {
			        	xtype: 'rownumberer',
			        	width: 35, 
			        	sortable: false
			        },{
			        	dataIndex: 'ctrId',
			        	hidden: true
			        },{
			            text: '<bean:message key="user.username" />',
			            dataIndex: 'username',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="user.namePerson" />',
			            dataIndex: 'namePerson',
			            width: 120,
			            sortable: true
			        },{
			            text: '<bean:message key="user.rolId" />',
			            dataIndex: 'rolDesc',
			            width: 80,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="user.ctrId" />',
			            dataIndex: 'ctrDesc',
			            width: 80,
			            flex:1,
			            sortable: true
			        },{
			            text: '<bean:message key="user.email" />',
			            dataIndex: 'email',
			            width: 100,
			            sortable: true
			        },{
			            text: '<bean:message key="user.phone" />',
			            dataIndex: 'phone',
			            width: 80,
			            sortable: true
			        }]
			    });

			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(300,function(){Ext.getStore('dataStore').load();});
			    
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="user.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="user.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>