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
				var habBtnDelete;
				<%
		        	UserForm user = (UserForm) session.getAttribute("user");
		            if(user.getNoCountry()){
		        %>
		        		habBtnDelete= true;
		        <%	}else{%>
		        		habBtnDelete=false;
		        <%	}%>
				
		        //Property used to choose the country column according to current language 
		        var ctrMapping = javaLocation=='es'?'ctrdescspa':'ctrdesceng';
		        var spiValue = javaLocation=='es'?'spivaluespa':'spivalueeng';
		        var userCtrId = <%=user.getCtrId()%>;
		        
				Ext.define('modelCtrId', {
				    extend: 'Ext.data.Model',
				    fields: [
				        {name: 'ctrId', type: 'numeric', mapping: 'ctrid'},
				        {name: 'ctrDesc', type: 'string', mapping: ctrMapping}
				    ],
				    idProperty: 'ctrId'
				});
				
				function validateRange(){
					var tmpMsg = '';
					
					if(Ext.getCmp('monday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Lunes';
					}
					
					if(Ext.getCmp('tuesday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Martes';
					}
					
					if(Ext.getCmp('wednesday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Miercoles';
					}
					
					if(Ext.getCmp('thursday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Jueves';
					}
					
					if(Ext.getCmp('friday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Viernes';
					}
					
					if(Ext.getCmp('saturday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Sabado';
					}
					
					if(Ext.getCmp('sunday').getValue()){
						if(tmpMsg.length>0){
							tmpMsg = tmpMsg+',';
						}
						tmpMsg = tmpMsg+'Domingo';
					}
					
					if(tmpMsg!=''){
						Ext.getCmp('spiValue').setValue(tmpMsg);
						Ext.getCmp('ctrDesc').setValue(Ext.getCmp('ctrId').getRawValue());
						
               			return true;
               		}else{
               			tmpMsg = tmpMsg+'<bean:message key="schedulePriceInsertion.spiValue.required"/>';
               			
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
					url:'schedulePriceInsertionAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 85,
					habBtnDelete: habBtnDelete,
					width: 400,
					title: '<bean:message key="schedulePriceInsertion.header" />',
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
	      	                anchor: '100%',
	      	                labelAlign:'right'
	      	            },
	      	            items :[
						{
	      	        		xtype:'hidden',
	      	        		id:'spiId',
	      	        		name:'spiId'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'spiValue',
	      	        		name:'spiValue'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'ctrDesc',
	      	        		name:'ctrDesc'
	      	        	},{
	      	              	xtype: 'combo',
	      	                fieldLabel: '<bean:message key="schedulePriceInsertion.ctrId"/>',
	      	                id:'ctrId',
	      	                name: 'ctrId',
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
	      	            },{
	      	        		xtype:'container',html:'<br/>'
	      	        	},{
							xtype: 'checkboxgroup',
							fieldLabel: '<bean:message key="schedulePriceInsertion.spiValue" />',
							// Arrange radio buttons into two columns, distributed vertically
							labelAlign:'top',
							columns: 4,
							vertical: true,
							defaults:{
								width:90
							},
							items: [
								{ boxLabel: '<bean:message key="schedulePriceInsertion.monday" />', name: 'monday', id: 'monday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.tuesday" />', name: 'tuesday', id: 'tuesday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.wednesday" />', name: 'wednesday', id: 'wednesday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.thursday" />', name: 'thursday', id: 'thursday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.friday" />', name: 'friday', id: 'friday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.saturday" />', name: 'saturday', id: 'saturday'},
								{ boxLabel: '<bean:message key="schedulePriceInsertion.sunday" />', name: 'sunday', id: 'sunday'}
							]
						}]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'spiValue', type: 'string', mapping: spiValue},
			            {name: 'ctrId', type: 'int', mapping:'ctrid'},
			            {name: 'ctrDesc', type: 'string', mapping: ctrMapping},
			            {name: 'monday', type: 'boolean', mapping:'monday'},
			            {name: 'tuesday', type: 'boolean', mapping:'tuesday'},
			            {name: 'wednesday', type: 'boolean', mapping:'wednesday'},
			            {name: 'thursday', type: 'boolean', mapping:'thursday'},
			            {name: 'friday', type: 'boolean', mapping:'friday'},
			            {name: 'saturday', type: 'boolean', mapping:'saturday'},
			            {name: 'sunday', type: 'boolean', mapping:'sunday'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'spiId', type: 'number', mapping:'spiid'}
			        ],
			        idProperty: 'spiId'
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
			            url: 'schedulePriceInsertionAction.do?accion=load',
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
			            property: 'spiId',
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
		                dataIndex: 'spiValue'
		            }, {
		                type: 'string',
		                dataIndex: 'ctrDesc'
		            }]
		        };
			    
			    Ext.create('Ext.custom.GridPanel',{
			    	id:'GridPanel',
			    	width: 400,
			        height: 250,
			        title: '<bean:message key="schedulePriceInsertion.header" />',
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
			            text: '<bean:message key="schedulePriceInsertion.ctrId" />',
			            dataIndex: 'ctrDesc',
			            width: 100,
			            sortable: true
			        },{
			            text: '<bean:message key="schedulePriceInsertion.spiValue" />',
			            dataIndex: 'spiValue',
			            width: 150,
			            flex:1,
			            //hidden: true,
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
		<bean:message key="schedulePriceInsertion.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="schedulePriceInsertion.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>