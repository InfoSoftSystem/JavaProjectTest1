<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import= "org.promefrut.simefrut.struts.administration.forms.ParametersForm"  %>

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
			}
			
			
			Ext.onReady(function(){
				
				//************************************************************************************************
				
				var formulario = Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'parametersAction.do',
					standardSubmit: false, //This is a must! even if the definitions has true. 
					labelWidth: 170,
					habBtnDelete: false,
					width: 400,
					title: '<bean:message key="parameters.header" />',
					cancelFn: function(){Ext.getStore('dataStore').load(); return true;},
					afterCancelFn:function(){this.updateMode();},
					items: [{
	      	            xtype:'fieldset',
	      	            checkboxToggle:false,
	      	            //title: 'User Information',
	      	            defaultType: 'textfield',
	      	            //collapsed: false,
	      	            layout: 'anchor',
	      	            defaults:{
	      	            	labelAlign:'right'
	      	            },
	      	            items :[
	      	            {
	      	                fieldLabel: '<bean:message key="parameters.emailAccount"/>',
	      	                id: '<%=ParametersForm.emailAccountID%>',
	      	                name: 'emailAccount',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="parameters.emailAccount.required" />'
	      	            }, {
	      	                fieldLabel: '<bean:message key="parameters.emailPassword"/>',
	      	              id: '<%=ParametersForm.emailPasswordID%>',
	      	                name: 'emailPassword',
	      	              	maxLength: 100,
	      	              	enforceMaxLength: true,
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="parameters.emailPassword.required" />'
	      	            },{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="parameters.priceTolerance"/>',
							id: '<%=ParametersForm.priceToleranceID%>',
							name: 'priceTolerance',
							width: 235,
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="parameters.priceTolerance.required" />',
							hideTrigger: false,
							keyNavEnabled: false,
							mouseWheelEnabled: false,
							allowDecimals: true
						},{
							xtype: 'numberfield',
							fieldLabel: '<bean:message key="parameters.productionTolerance"/>',
							id: '<%=ParametersForm.productionToleranceID%>',
							name: 'productionTolerance',
							width: 235,
							minValue: 0,
							allowBlank:false,
							blankText: '<bean:message key="parameters.productionTolerance.required" />',
							hideTrigger: false,
							keyNavEnabled: false,
							mouseWheelEnabled: false,
							allowDecimals: true
						}]
	      	        }]//Items Form.Panel
				});
				
				//*************************** CUSTOM GRID PANEL ********************************
				
				Ext.define('GridModel', {
			        extend: 'Ext.data.Model',
			        fields: [
			            {name: 'parId', type: 'number', mapping:'parid'},
			            {name: 'parName', type: 'string', mapping:'parname'},
			            {name: 'dpmValue', type: 'string', mapping:'dpmvalue'},
			            {name: 'auditStatus', type: 'string', mapping:'auditstatus'},
			            {name: 'audit_user', type: 'string', mapping:'audit_user'},
			            {name: 'audit_date', type: 'string', mapping:'audit_date'},
			            {name: 'rolId', type: 'number', mapping:'rolid'}
			        ],
			        idProperty: 'parId'
			    });
				
								
				// create the Data Store
			    Ext.create('Ext.data.Store', {
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
			            url: 'parametersAction.do?accion=load',
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
			            property: 'parId',
			            direction: 'ASC'
			        }],
			        listeners: {
			        	load: function(store, records, successful, eOpts){
			        		if(successful){
			        			for(var i = 0; i<store.count();i=i+1){
			        				var record = store.getAt(i);
			        				
			        				if(!Ext.isEmpty(record.get('parName'))){
			        					Ext.getCmp(record.get('parName')).setValue(record.get('dpmValue'));
			        				};
			        			}
			        		}
			        	}
			        }
			    });//Store
			    
			    // trigger the data store load. We use a delay because the MaskLoad appears in a wrong position. 
			    // So we set a delay to wait for the final component's position inside the container 
			    new Ext.util.DelayedTask().delay(200,function(){Ext.getStore('dataStore').load();});
			    
			    formulario.updateMode();
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="parameters.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="parameters.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>