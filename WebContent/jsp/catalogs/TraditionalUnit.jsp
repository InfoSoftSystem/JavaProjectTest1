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
		        
				Ext.define('modelCtrId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
				
				Ext.define('modelVarId', {
				    extend: 'Ext.data.Model',
				    fields: [
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'varId',  type: 'int', mapping:'varid'},
				        {name: 'varDesc',  type: 'string', mapping:'vardesc'}
				    ],
				    idProperty: 'varId'
				});
				
				Ext.define('modelProdId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'prodDesc', type: 'string', mapping:'proddesc'}
				    ],
				    idProperty: 'prodId'
				});
				
				Ext.define('modelQuaId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'quaId', type: 'int', mapping:'quaid'},
						{name: 'quaDesc', type: 'string', mapping: 'quadesc'}
				    ],
		            idProperty: 'quaId'
				});
				
				Ext.define('modelSizeId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'sizeId', type: 'int', mapping:'sizeid'},
				        {name: 'sizeDesc', type: 'string', mapping: 'sizedesc'}
				    ],
		            idProperty: 'sizeId'
				});
				

				Ext.define('modelPtypeId', {
					extend: 'Ext.data.Model',
					fields: [
				        {name: 'ptypeId', type: 'int', mapping:'ptypeid'},
						{name: 'prodId', type: 'int', mapping:'prodid'},
				        {name: 'ptypeDesc', type: 'string', mapping: 'ptypedesc'}
				    ],
		            idProperty: 'ptypeId'
				});
				
				function validateRange(){
					var tmpMsg = '';
					
					if(!Ext.getCmp('varId').isDisabled() && Ext.isEmpty(Ext.getCmp('varId').getValue())){
						tmpMsg = tmpMsg+'<bean:message key="tunit.varId.required"/>';
					}
					
					if(!Ext.getCmp('ptypeId').isDisabled() && Ext.isEmpty(Ext.getCmp('ptypeId').getValue())){
						tmpMsg = tmpMsg+'<bean:message key="tunit.ptypeId.required"/>';
					}
					
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
				
				//************************************************************************************************
				
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'traditionalUnitAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					width: 250,
					title: '<bean:message key="tunit.header" />',
					insertFn: validateRange,
					updateFn: validateRange,
					cancelFn: function(){Ext.getStore('dataStore').load(); return true;},
					afterCancelFn: function(){
						//Set default values
						Ext.getCmp('ctrId').fireEvent('afterrender');
					},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            //collapsed: false,
	      	            layout: 'anchor',
	      	            defaults: {
	      	                anchor: '100%'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'tunitId',
	      	        		name:'tunitId'
	      	        	},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.ctrId"/>',
	      	                id:'ctrId',
	      	                name: 'ctrId',
	      	                /*allowBlank:true,
	      	            	disabled: false,//ctrDisabled,*/
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
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	afterrender:function(){
					        		this.setValue(userCtrId);
					        		new Ext.util.DelayedTask().delay(300,function(){
					        			Ext.getCmp('ctrId').setValue(userCtrId);
					        		});
					        	}
					        }
	      	            },/*{
	      	                fieldLabel: '<bean:message key="tunit.tunitCode"/>',
	      	                id: 'tunitCode',
	      	                name: 'tunitCode',
	      	              	maxLength: 10,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="tunit.tunitCode.required" />'
	      	            },*/{
	      	                fieldLabel: '<bean:message key="tunit.tunitDesc"/>',
	      	                id: 'tunitDesc',
	      	                name: 'tunitDesc',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="tunit.tunitDesc.required" />'
	      	            },{
	      	                xtype: 'numberfield',
                        	fieldLabel: '<bean:message key="tunit.tunitKilo"/>',
	      	                id: 'tunitKilo',
	      	              	minValue: 0,
	      	                name: 'tunitKilo',
	      	                allowBlank:false,
	      	            	blankText: '<bean:message key="tunit.tunitKilo.required" />',
	      	            	hideTrigger: true,
	      	              	keyNavEnabled: false,
	      	              	mouseWheelEnabled: false
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.prodId"/>',
	      	                id:'prodId',
	      	                name: 'prodId',
	      	              	allowBlank:false,
					        store: 	Ext.create('Ext.data.Store',{
					        	model: 'modelProdId',
					        	data: {registers: <%=request.getAttribute("productsCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'prodId',
	      				            direction: 'ASC'
	      				        }]
					        }),
					        queryMode: 'local',
					        displayField: 'prodDesc',
					        valueField: 'prodId',
					        editable: true,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 50,
					        	// delete the previous query in the beforequery event or set
					            // combo.lastQuery = null (this will reload the store the next time it expands)
					            beforequery: function(qe){
					                delete qe.combo.lastQuery;
					            },
					        	change: function(combo, records, eOpts){
					        		
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
					        		
					        		if(strVarSk.getCount()==0){
					        			varSk.disable();
					        		}else{
					        			varSk.enable();
					        		}
					        		
					        		varSk.setValue(tmpValue);
					        		
					        		
					        		/*********************************/
					        		var ptypeId = Ext.getCmp('ptypeId');
					        		var strPtypeId = ptypeId.store;
					        		
					        		ptypeId.enable();
				        			var tmpValue = ptypeId.getValue();
				        			ptypeId.clearValue();
					        		
				        			strPtypeId.suspendEvents();
					        		strPtypeId.clearFilter();
					        		strPtypeId.resumeEvents();
					        		
					        		strPtypeId.filter([
					        			{id: 'prodId', property: 'prodId', value: combo.getValue(), exactMatch: true}
					        		]);
					        		
					        		if(strPtypeId.getCount()==0){
					        			ptypeId.disable();
					        		}else{
					        			ptypeId.enable();
					        		}
					        		
					        		ptypeId.setValue(tmpValue);
					        	}
					        }
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.ptypeId"/>',
	      	                id:'ptypeId',
							name:'ptypeId',
	      	                allowBlank:true,
	      	              	disabled: true,
	      	            	store: Ext.create('Ext.data.Store',{
					        	model: 'modelPtypeId',
					        	data: {registers: <%=request.getAttribute("productTypesCollection")%>},
					        	proxy:{
					        		type: 'memory',
					        		reader: {
					                    type: 'json',
					                    root: 'registers'
					                }
					        	},
	      			        	sorters: [{
	      				            property: 'ptypeDesc',
	      				            direction: 'ASC'
	      				        }]
					        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'ptypeDesc',
					        valueField: 'ptypeId',
					        //editable: false,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 50,
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
				                }
					        }
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.varId"/>',
	      	                id:'varId',
	      	                name: 'varId',
	      	              	allowBlank:true,
	      	              	disabled: true,
	      	              	lastQuery:'', //! IMPORTANT <-- if not set to empty, the first filter doesn't work!
	      	              	store: 	Ext.create('Ext.data.Store',{
	      			        	model: 'modelVarId',
	      			        	data: {registers: <%=request.getAttribute("varietiesCollection")%>},
	      			        	proxy:{
	      			        		type: 'memory',
	      			        		reader: {
	      			                    type: 'json',
	      			                    root: 'registers'
	      			                }
	      			        	},
	      			        	sorters: [{
	      				            property: 'prdVarSk',
	      				            direction: 'ASC'
	      				        }]
	      			        }),
					        queryMode: 'local',
					        displayField: 'varDesc',
					        valueField: 'varId',
					        editable: true,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>',
					        listeners:{
					        	buffer: 50,
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
				                }
					        }
	      	            },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.sizeId"/>',
	      	                id:'sizeId',
	      	                name: 'sizeId',
	      	              	allowBlank:false,
					        store: Ext.create('Ext.data.Store',{
	      			        	model: 'modelSizeId',
	      			        	data: {registers: <%=request.getAttribute("sizesCollection")%>},
	      			        	proxy:{
	      			        		type: 'memory',
	      			        		reader: {
	      			                    type: 'json',
	      			                    root: 'registers'
	      			                }
	      			        	},
	      			        	sorters: [{
	      				            property: 'sizeId',
	      				            direction: 'ASC'
	      				        }]
	      			        }),
	      	              	lastQuery:'',
					        queryMode: 'local',
					        displayField: 'sizeDesc',
					        valueField: 'sizeId',
					        editable: true,
					        forceSelection: true,
					        emptyText: '<bean:message key="combo.emptyText"/>'
					    },{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="tunit.tunitStatus"/>',
	      	                id:'tunitStatus',
	      	                name: 'tunitStatus',
	      	                //maxLength: 1,
	      	              	allowBlank:false,
	      	              	value: 'A',
	      	              	//width: 200,
					        store: 	Ext.create('Ext.data.Store',{
					        	fields: ['name','value'],
					        	data: [
					        		{'name':'<bean:message key="registro.active" />', 'value':'A'},
					        		{'name':'<bean:message key="registro.inactive" />', 'value':'I'}
					        	]
					        }),
					        queryMode: 'local',
					        displayField: 'name',
					        valueField: 'value',
					        editable: false
	      	            }]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            //{name: 'tunitCode', type: 'string', mapping:'tunitcode'},
			            {name: 'tunitDesc', type: 'string', mapping:'tunitdesc'},
			            {name: 'tunitStatus', type: 'string', mapping:'tunitstatus'},
			            {name: 'ctrId', type: 'int', mapping:'ctrid'},
			            {name: 'ctrDesc', type: 'string', mapping: ctrMapping},
			            {name: 'prodId', type: 'int', mapping:'prodid'},
			            {name: 'prodDesc', type: 'string', mapping:'proddesc'},
			            {name: 'ptypeId', type: 'number', mapping:'ptypeid'},
			            {name: 'ptypeDesc', type: 'string', mapping:'ptypedesc'},
			            {name: 'varId', type: 'int', mapping:'varid'},
			            {name: 'varDesc', type: 'string', mapping:'vardesc'},
			            {name: 'sizeId', type: 'int', mapping:'sizeid'},
			            {name: 'sizeDesc', type: 'string', mapping:'sizedesc'},
			            {name: 'tunitKilo', type: 'number', mapping:'tunitkilo'},
			            {name: 'tunitStatus', type: 'string', mapping:'tunitstatus'},
			            {name: 'tunitStatusText', type: 'string', mapping:'tunitstatustext'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'tunitId', type: 'number', mapping:'tunitid'}
			        ],
			        idProperty: 'tunitId'
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
			            url: 'traditionalUnitAction.do?accion=load',
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
			            property: 'tunitId',
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
		                dataIndex: 'tunitDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'prodDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'varDesc'
		            }, {
		                type: 'string',
		                dataIndex: 'sizeDesc'
		            }, {
		                type: 'numeric',
		                dataIndex: 'tunitKilo'
		            }, {
		                type: 'string',
		                dataIndex: 'tunitCode'
		            }, {
		                type: 'list',
		                dataIndex: 'tunitStatusText',
		                options: ['<bean:message key="registro.active" />', '<bean:message key="registro.inactive" />']
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 750,
			        height: 250,
			        beforeSelectionchange: function(){
			    		//Clear the filters in child combo Boxes in order to set the values
			    		var tmpStore = Ext.getCmp('varId').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
		        		Ext.getCmp('varId').enable();
		        		
		        		//Clear the filters in child combo Boxes in order to set the values
			    		tmpStore = Ext.getCmp('ptypeId').store;
		        		
		        		tmpStore.suspendEvents();
		        		tmpStore.clearFilter();
		        		tmpStore.resumeEvents();
		        		Ext.getCmp('ptypeId').enable();
		        		
		        		Ext.getCmp('prodId').setValue('');
			    	},
			        title: '<bean:message key="tunit.header" />',
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
			            text: '<bean:message key="tunit.tunitId" />',
			            dataIndex: 'tunitId',
			            width: 50,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.tunitDesc" />',
			            dataIndex: 'tunitDesc',
			            width: 150,
			            flex:1,
			            //hidden: true,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.ctrId" />',
			            dataIndex: 'ctrDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.prodId" />',
			            dataIndex: 'prodDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.varId" />',
			            dataIndex: 'varDesc',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.sizeId" />',
			            dataIndex: 'sizeDesc',
			            width: 90,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.tunitKilo" />',
			            dataIndex: 'tunitKilo',
			            width: 80,
			            sortable: true
			        },{
			            text: '<bean:message key="tunit.tunitStatus" />',
			            dataIndex: 'tunitStatusText',
			            width: 80,
			            //renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
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
		<bean:message key="tunit.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="tunit.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>