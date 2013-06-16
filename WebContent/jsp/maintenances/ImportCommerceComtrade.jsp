<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import= "org.apache.commons.lang.StringUtils"  %>

<tiles:insert name="plantillaBase" definition="plantillaBase">
	<tiles:put name="base">
		<html:base />
	</tiles:put>
	
	<tiles:put name="help" type="String">
		<b>Archivo Ejemplo/Example File</b>: Es un archivo ejemplo del formato que debe tener los datos en el archivo, para que pueda ser cargada la información correctamente.
		<br/>
	</tiles:put>
	
	<tiles:put name="scripts" type="String">
		<script>
			<%
				String lastYearLoad = (String)request.getAttribute("lastYearLoad");			
			%>
			/**
				This funtion is used to fire the events for positioning the components. centerNow function is used on the properties
				onMenuCollapse and onMenuExpand of BaseViewer.BaseView. This function must be in every page if the positioning is necesary
			*/
			function centerNow(){
				Ext.getCmp('formulario').fireEvent('position',{"this":Ext.getCmp('formulario'), eOpts: {}});
				//Ext.getCmp('frm2').fireEvent('position',{"this":Ext.getCmp('frm2'), eOpts: {}});
			}
			
			Ext.onReady(function(){
				
				//************************************************************************************************
				Ext.create('Ext.custom.FormPanel',{
					id:'formulario',
					url:'importCommerceComtradeAction.do',
					standardSubmit: true, //This is a must! even if the definitions has true. 
					labelWidth: 75,
					width: 450,
					title: '<bean:message key="importCommerceComtrade.header" />',
					//cancelFn: function(){Ext.getStore('dataStore').load(); return true;},
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
	      	            items :[{
	      	        		xtype:'hidden',
	      	        		id:'yearList',
	      	        		name:'yearList'
	      	        	},{
	      	        		xtype:'hidden',
	      	        		id:'oldFilename',
	      	        		name:'oldFilename'
	      	        	},{
							xtype: 'filefield',
							id: 'repFile',
							name: 'repFile',
							labelAlign:'top',
							fieldLabel: '<bean:message key="importCommerceComtrade.file"/>',
							//labelWidth: 100,
							msgTarget: 'side',
							allowBlank: false,
							//anchor: '100%',
							buttonText: '<bean:message key="importCommerceComtrade.file.label"/>'
	      	        	},{
	      	        		xtype:'container',
	      	        		html:'<a href="getAction.do?accion=exampleComtrade"><bean:message key="importCommerceComtrade.file.example"/></a>'
	      	        	},{
	      	        		xtype:'container',
	      	        		html:'<br/><hr/><bean:message key="importCommerceComtrade.lastYearLoad"/> <%=lastYearLoad%>'
	      	        	}]
	      	        }]//Items Form.Panel
				});
				/**/
				<%
					String yearList = (String)request.getAttribute("yearList");
					String oldFilename = (String)request.getAttribute("oldFilename");
					
					if(StringUtils.isBlank(yearList)){
						yearList = new String();
					}
					
					if(StringUtils.isBlank(oldFilename)){
						oldFilename = new String();
					}
				%>
				
				var yearList = '<%=yearList%>';
				var oldFilename = '<%=oldFilename%>';
				
				if(!Ext.isEmpty(yearList)){
					
					Ext.Msg.show({
						title:'<bean:message key="global.warning.title"/>',
						msg: '<bean:message key="importCommerceComtrade.data.exists.part1"/> '+yearList+'.<br/><bean:message key="importCommerceComtrade.data.exists.part2"/>',
						buttons: Ext.Msg.YESNO,
						icon: Ext.Msg.QUESTION,
						modal:true,
						closable:false,
						scope: this,
						fn:function(buttonId){
							if(buttonId=='yes'){
								Ext.getCmp('yearList').setValue(yearList);
								
								if(!Ext.isEmpty(oldFilename)){
									Ext.getCmp('oldFilename').setValue(oldFilename);
								}
								
								Ext.get('globalMessages').update('<bean:message key="importCommerceComtrade.data.overwrite.confirmed"/>');
								showGlobalMessages();
							}
						}
					});
				}
	      	});//onReady
		</script>
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="importCommerceComtrade.title" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="importCommerceComtrade.header" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>