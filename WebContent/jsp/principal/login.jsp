<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import= "org.apache.struts.Globals"  %>
<%@ page import= "java.util.Locale"  %>

<tiles:insert name="plantillaSinMenu" definition="plantillaSinMenu">

	<tiles:put name="base">
		<html:base />
	</tiles:put>
	
	
  <tiles:put name="scripts" type="String">
    <script type="text/javascript" lang="javascript">
    	function lostPwd(){
    		location.href= location.href.substr(0,location.href.lastIndexOf('/'))+'/lostPwdAction.do?accion=find';
    	}
    
    	function genericUser(){
    		Ext.getCmp('username').setValue('reportes');
    		Ext.getCmp('password').setValue('admin');
    		button = Ext.getCmp('btnLogin');
    		button.fireEvent('click', button); 
    	}
    	
    	
    		Ext.onReady(function(){
				var pnlPrin = Ext.create('Ext.form.Panel',{
				    //renderTo: 'container',
				    id: 'formulario',
				    url: 'login.do',
				    border: 1,
       	        	//title: 'User Form',
					align: 'middle',
					standardSubmit: true,
					//height: 130,
					//width: 400,
					bodyPadding: 10,
					labelWidth: 75,
					defaults: {
			            /*anchor: '95%',
			            allowBlank: false,*/
			            selectOnFocus: true,
			            msgTarget: 'side'
			        },
					defaultType: 'textfield',
					layout: {
						type:'table',
						columns: '1',
						tableAttrs: {
							align: 'center'
						},
						tdAttrs:{
							align:'center'
						}
					},
					items: [
				        {
				        	xtype: 'hidden',
				        	name:'accion',
				        	id: 'accion',
				        	inputId: 'accion'
				        },
				        {
					    	xtype: 'image',
					    	src: '<%=request.getContextPath()%>/images/LogoSimefrutB.jpg',//simefrut3.png
					    	maxWidth: 200,
					    	maxHeight: 60,
					    	//width: 200,
					    	height: 220
					    },{
		      	            xtype:'fieldset',
		      	            checkboxToggle:false,
		      	            //title: 'User Information',
		      	            defaultType: 'textfield',
		      	            //collapsed: false,
		      	            //width: 400,
		      	            layout: 'hbox',
		      	            width:350,
		      	            border: 0,
		      	            items :[{
						    	xtype: 'image',
						    	src: '<%=request.getContextPath()%>/images/logoPROMEFRUT.jpg',
						    	maxWidth: 200,
						    	maxHeight: 200,
						    	height: 150
						    	,id: 'img1'
						    },{
						    	xtype: 'image',
						    	src: '<%=request.getContextPath()%>/images/OIMA.png',
						    	maxWidth: 200,
						    	maxHeight: 200,
						    	height: 150
						    	,id: 'img2'
						    }
					    ]},/* */
				        {
					        xtype: 'combo',
					        name: 'lenguaje',
					        id: 'extLenguaje',
					        margin: '10 0 5 0', 
					        fieldLabel: '<bean:message key="login.lenguaje" />',
					        store: 	Ext.create('Ext.data.Store',{
					        	fields: ['name','value'],
					        	data: [
					        		{'name':'<bean:message key="lenguaje.ingles" />', 'value':'1'},
					        		{'name':'<bean:message key="lenguaje.espanol" />', 'value':'2'}
					        	]
					        }),
					        queryMode: 'local',
					        displayField: 'name',
					        valueField: 'value',
					        allowBlank: false,
					        editable: false,
					        //value: (lenguaje).toString(),
					        listeners: {
					        	beforerender: function(obj, eOpts){
					        		var lenguaje = '<%=((Locale)(request.getSession(true).getAttribute(Globals.LOCALE_KEY))).getCountry()%>';
				        			
				        			if(lenguaje.toLowerCase()=='sv'){
				        				lenguaje = '2';
				        			}else if(lenguaje.toLowerCase()=='us'){
				        				lenguaje = '1';
				        			}
				        	
					        		if(lenguaje == 1){
					        			obj.setValue(lenguaje);
					        		}else if(lenguaje == 2){
					        			obj.setValue(lenguaje);
					        		}
					        	},//*/
					        	/*change: function(obj, newValue, oldValue, eOpts){
					        		var form = this.up('form').getForm();
					        		Ext.getCmp('accion').setValue('change');
					                form.submit({clientValidation:false});
					        	},*/
					        	select: function(obj, records, eOpts){
					        		var form = this.up('form').getForm();
					        		Ext.getCmp('accion').setValue('change');
					        		
					        		wait();
					        		form.submit({
					                	clientValidation:false/*,
					                	params : {lenguaje : form.findField('lenguaje').getValue()},*/
					                });
					        	}//Select
					        } //Listeners
					    },
					    {
					        fieldLabel: '<bean:message key="login.username" />',
					        name: 'username',
					        id: 'username',
					        blankText: '<bean:message key="login.message.username" />',
					        allowBlank: false,
					        listeners:{
					        	specialkey: function(field, e){
				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
				                    if (e.getKey() == e.ENTER) {
				                        if(!Ext.isEmpty(field.getValue())){
				                    			Ext.getCmp('btnLogin').fireEvent('click');
				                        }
				                    }
				                },
				                
				                afterrender: function(){
					        		this.focus(true,500);
					        	}
					        }
					    },
					    {
					    	fieldLabel: '<bean:message key="login.password" />',
					    	inputType: 'password',
					        name: 'password',
					        id: 'password',
					        blankText: '<bean:message key="login.message.password" />',
					        allowBlank: false, 
					        listeners: {
				                specialkey: function(field, e){
				                    // e.HOME, e.END, e.PAGE_UP, e.PAGE_DOWN,
				                    // e.TAB, e.ESC, arrow keys: e.LEFT, e.RIGHT, e.UP, e.DOWN
				                    if (e.getKey() == e.ENTER) {
				                        if(!Ext.isEmpty(field.getValue())){
				                    			Ext.getCmp('btnLogin').fireEvent('click');
				                        }
				                    }
				                }
					        }
					    },{
	      	        		xtype:'container',
	      	        		html:'<a href="#" onclick="lostPwd();"><bean:message key="generic.lostPwd"/></a><br/><br/>'
	      	        	},{
	      	        		xtype:'container',
	      	        		html:'<a href="#" onclick="genericUser();"><bean:message key="generic.user"/></a>'
	      	        	},
					    {
					    	xtype: 'panel',
					    	contentEl: 'spanErrors',
					    	id: 'spanErrors',
					    	layout: 'fit',
					    	border: 0
					    }
					],//Items
    	            dockedItems:[
 					    Ext.create('Ext.ux.StatusBar', {
 			                dock: 'bottom',
 			                id: 'form-statusbar',
 			                defaultText: '<bean:message key="statusbar.defaultText" />',
 			                plugins: Ext.create('Ext.ux.statusbar.ValidationStatus', {form:'formulario'})
 			            })
 					]
				});// Panel
				
       	     	var loginwindow = Ext.create('widget.window', {
    	            //height: 200,
    	            width: 400,
    	            closable: false,
    	            draggable: false,
    	            resizable: false,
    	            title: '<bean:message key="login.encabezado" />',
    	            //plain: true,
    	            layout: 'fit',
    	            //modal: true,
    	            items:[ pnlPrin ],
    	            buttons:[{
						text: '<bean:message key="login.ingresar"/>',
						id:'btnLogin',
						listeners: {
							click: function(){
								var form = Ext.getCmp('formulario').getForm();
								if (form.isValid()) {
					                Ext.getCmp('accion').setValue('login');
					                
					                wait();
					                form.submit(/*{
					                	params : {lenguaje : form.findField('lenguaje').getValue()}
					                }*/);
					            }
							}
						}
					}]/**/
    	        }); 
    	        
    	        loginwindow.show();
    	        
    	        var spanErrors = Ext.get('spanErrors');
    	        
    	        if(!Ext.isEmpty(spanErrors)){
    	        	spanErrors.show();
    	        	Ext.getCmp('spanErrors').show();
    	        }else{
    	        	Ext.getCmp('spanErrors').hide();
    	        }
    	        new Ext.util.DelayedTask().delay(1000,function(){
    	        	Ext.getCmp('img1').doComponentLayout();
    	        	Ext.getCmp('img2').doComponentLayout();
				});
    	        
			});
			
    </script>
  </tiles:put>

	<tiles:put name="title" type="String">
		PROMEFRUT - <bean:message key="login.titulo" /> 
	</tiles:put>	
 
	<tiles:put name="encabezado" type="String">
		<%-- <bean:message key="login.titulo" /> --%>    
	</tiles:put>
 	
	<tiles:put name="contenido" type="String">
		<div style="vertical-align: bottom; height: 500px; text-align:center;">
			<div>
				<font style="font-family: Comic Sans-serif">
					Developed by: <i><b>Henry Willy Melara</b></i><br/>
					El Salvador - C.A.<br/>
					<a href="http://sv.linkedin.com/in/henrywillym">sv.linkedin.com/in/henrywillym</a>
				</font>
			</div>
		</div>
        <span id="spanErrors" >
        	<%-- <html:errors />--%>
        	<html:messages id="message" footer="errores.pie"  header="errores.cabecera" property="login.username.requerido">
	          <br><span class="error" > <bean:write name="message" filter="false" /></span>
	        </html:messages>
	        <html:messages id="message" footer="errores.pie" header="errores.cabecera" property="login.password.requerido">
	          <br><span class="error" > <bean:write name="message" filter="false" /> </span>
	        </html:messages>
	        <%--
	        <html:errors property="login.fail.password"/>
	        <html:errors property="login.fail.username"/>
	        <html:errors/>
	        --%>
        </span>
        

		<br>
	</tiles:put>

</tiles:insert>