<%@ page language="java" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="org.promefrut.simefrut.struts.administration.forms.UserForm"%>
<%@ page import= "org.apache.struts.Globals"  %>
<%@ page import= "java.util.Locale"  %>

<%
	String contextPath = request.getContextPath();

	//Language setting for Ext JS 
	String lenguaje = ((Locale)(request.getSession(true).getAttribute(Globals.LOCALE_KEY))).getCountry();
	String location="";
	
	if("sv".equals(lenguaje.toLowerCase())){
		location = "es";
	}else if("us".equals(lenguaje.toLowerCase())){
		location = "en";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<link rel="SHORTCUT ICON" href="<%=contextPath%>/images/favicon.png" type="image/x-icon" />
		<%--
		<!--[if IE]>
		
		<![endif]-->
		
		<!--[if !IE]>
		<link  rel="icon" href="<%=request.getContextPath()%>/images/favicon.ico" type="image/x-icon" />
		<link type="image/x-icon" href="<%=request.getContextPath()%>/images/favicon2.ico" rel="shortcut icon" />
		<![endif]-->
		--%>
		
		<title><tiles:getAsString name="title" /></title>
		
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ext-all-gray-4.1.0-b3.css"	rel="stylesheet" type="text/css"/>
		<!-- <link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/xtheme-slate.css"	rel="stylesheet" type="text/css"> -->
		
		<link href="<%=contextPath%>/Ext JS 4.0.7/resources/css/ux/statusbar.css"	rel="stylesheet" type="text/css"/>
		
		<tiles:getAsString name="css" />
		
		
		
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ext-all-debug-w-comments 4.1.0-b2.js"></script>
		
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/StatusBar.js"></script>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/ux/ValidationStatus.js"></script>
		
		
		<%-- Internacionalizacion de Ext JS  --%>
		<script type="text/javascript" language="Javascript" src="<%=contextPath%>/Ext JS 4.0.7/locale/ext-lang-<%=location%>.js"></script>
		
		<script type="text/javascript" language="javascript">
		
			Ext.onReady(function(){
				/**
				Ext.Loader.setConfig({
				    enabled: true
				});
				
				Ext.Loader.setPath('Ext.ux', '<%=contextPath%>/Ext JS 4.0.7/ux/');
				*/
				
				//Setting background color
				//Ext.getBody().setStyle({background: 'RGB(199,230,163)'});
				//Ext.getBody().setStyle({background: 'RGB(255,212,125)'});
				//Ext.getBody().setStyle({background: 'RGB(255,222,165)'});
				Ext.getBody().setStyle({background: 'RGB(255,255,215)'});
				
				Ext.tip.QuickTipManager.init();
				
				var globalErrors = document.getElementById('globalErrors').innerHTML;
				globalErrors = Ext.String.trim(globalErrors);
				
				if(globalErrors.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.error.title" />',
						msg: globalErrors,
						icon: Ext.Msg.ERROR,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalErrors'
					});
				}
				
				var globalWarnings = document.getElementById('globalWarnings').innerHTML;
				globalWarnings = Ext.String.trim(globalWarnings);
				
				if(globalWarnings.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.warning.title" />',
						msg: globalWarnings,
						icon: Ext.Msg.WARNING,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalWarnings'
					});
				}
				
				var globalMessages = document.getElementById('globalMessages').innerHTML;
				globalMessages = Ext.String.trim(globalMessages);
				
				if(globalMessages.length > 0){
					Ext.Msg.show({
						title: '<bean:message key="global.attention.title" />',
						msg: globalMessages,
						icon: Ext.Msg.INFO,
						buttons: Ext.Msg.OK,
						animateTarget: 'globalMessages'
					});
				}
				/*
				Ext.Ajax.request({
					url: 'Ext JS 4.0.7/locale/ext-lang-'+location+'.js',
					async:false,
					success	: function(response){
						eval(response.responseText);
					},
					failure	: function(){
						Ext.Msg.alert('Failure', 'Failed to load locale file.');
					},
					scope	: this
				});*/
			});
			
			/**
			* Generic wait window
			* @return Ext.window.MessageBox
			*/
			function wait(){
				//return Ext.MessageBox.wait('<bean:message key="login.wait.message" />','<bean:message key="login.wait.title" />');
				return Ext.MessageBox.wait('<bean:message key="login.wait.message" />');
			}
		</script>
		
		<tiles:getAsString name="scripts" />
		
	</head>
	<body>
		<div style="display:none">
			<span id="globalErrors">
				<html:messages id="message" property="globalErrors">
		          <bean:write name="message" filter="false" />
		        </html:messages>
		        
				<%--<html:errors property="globalErrors"/>--%>
			</span>
			
			<span id="globalWarnings">
				<html:messages id="message" property="globalWarnings">
		          <bean:write name="message" filter="false" />
		        </html:messages>
			</span>
			
			<span id="globalMessages">
				<html:messages id="message" property="globalMessages">
		          <bean:write name="message" filter="false" />
		        </html:messages>
			</span>
			
		</div>
			
			<div id="container">
				<div class="titulo">
					<tiles:insert name="encabezado" />
					<!-- <hr width="97%" style="margin-left: -2px; margin-top: -1px;"> -->
				</div>
				<tiles:insert name="contenido" />
			</div>

	</body>
</html>

