<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


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
	      		Ext.create('Ext.form.Panel', {
	      	        id:'formulario',
	      			renderTo:'container',
	      			url:'changePwdAction.do',
	      	        frame:true,
	      	        title: '<bean:message key="changePassword.encabezado" />',
	      	        bodyStyle:'padding:5px 5px 0 5px',
	      	        width: 350,
	      	      	standardSubmit: true,
	      	        fieldDefaults: {
	      	            msgTarget: 'side',
	      	            labelWidth: 175
	      	        },
	      	        defaults: {
	      	            anchor: '100%'
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
	      	        		id:'accion',
	      	        		name:'accion'
	      	        	},{
	      	                fieldLabel: '<bean:message key="changePassword.passwordOld"/>',
	      	                id: 'passwordOld',
	      	                name: 'passwordOld',
	      	                inputType: 'password',
	      	                allowBlank:false,
	      	            	blankText: '<bean:message key="changePassword.passwordOld.requerido" />'
	      	            },{
	      	                fieldLabel: '<bean:message key="changePassword.passwordNew"/>',
	      	                id: 'passwordNew',
	      	                name: 'passwordNew',
	      	              	inputType: 'password',
	      	            	allowBlank:false,
	      	            	blankText: '<bean:message key="changePassword.passwordNew.requerido" />',
	      	            	validator: function(obj){
	      	            		Ext.getCmp('confirmPsw').isValid();
	      	            		var passwordNew = Ext.getCmp('passwordNew').getValue();
	      	            		
	      	            		if(passwordNew ==''){
	      	            			return '<bean:message key="changePassword.passwordNew.requerido" />';
	      	            		}else{
	      	            			return true;
	      	            		}
	      	            	}
	      	            },{
	      	                fieldLabel: '<bean:message key="changePassword.confirm"/>',
	      	                id:'confirmPsw',
	      	                name: 'confirmPsw',
	      	                inputType: 'password',
	      	              	allowBlank:false,
	      	            	blankText: '<bean:message key="changePassword.passwordNew.requerido" />',
	      	            	validator: function(obj){
	      	            		var passwordNew = Ext.getCmp('passwordNew').getValue();
	      	            		var confirmPsw = Ext.getCmp('confirmPsw').getValue();
	      	            		
	      	            		if(confirmPsw != passwordNew) {
		      	  					return '<bean:message key="changePassword.passwordNew.confirm" />';
		      	  				}else{
		      	  					return true;
		      	  				}
	      	            	}
	      	            }]
	      	        }],
	  	            dockedItems:[
						    Ext.create('Ext.ux.StatusBar', {
				                dock: 'bottom',
				                id: 'form-statusbar',
				                defaultText: '<bean:message key="statusbar.defaultText" />',
				                plugins: Ext.create('Ext.ux.statusbar.ValidationStatus', {id:'validationStatus',form:'formulario'})/*,
				                items: [{
				      	            text: 'Save',
				      	          	handler: function(){
										var form = Ext.getCmp('formulario').getForm();
										if (form.isValid()) {
							                Ext.getCmp('accion').setValue('update');
							                
							                wait();
							                form.submit();
							            }
									}
				      	        	},{
				      	            text: 'Cancel',
				      	            handler: function(){
				      	            	Ext.getCmp('formulario').getForm().reset();
				      	            	Ext.getCmp('validationStatus').hideErrors();
				      	            	Ext.getCmp('form-statusbar').clearStatus();
				      	            }
				      	        }]*/
				            })
						],
					buttons: [{
						text: '<bean:message key="opc.guardar" />',
	      	          	handler: function(){
							var form = Ext.getCmp('formulario').getForm();
							if (form.isValid()) {
				                Ext.getCmp('accion').setValue('update');
				                
				                wait();
				                form.submit();
				            }
						}
	      	        },{
	      	        	text: '<bean:message key="opc.cancel" />',
	      	            handler: function(){
	      	            	Ext.getCmp('formulario').getForm().reset();
	      	            	Ext.getCmp('validationStatus').hideErrors();
	      	            	Ext.getCmp('form-statusbar').clearStatus();
	      	            }
	      	        }],
	      	        listeners:{
	      	        	position: function(){
	      	        		this.setPosition(Ext.get('container').getWidth()/2 - this.width/2, 10);
	      	        	}
	      	        }
	      	    });
	      		
	      		
	      	});
		</script>
	</tiles:put>
			
	<tiles:put name="title" type="String">
		<bean:message key="changePassword.titulo" />
	</tiles:put>

	<tiles:put name="encabezado" type="String">
		<bean:message key="changePassword.encabezado" />
	</tiles:put>

	<tiles:put name="contenido" type="String">
		
	</tiles:put>

</tiles:insert>