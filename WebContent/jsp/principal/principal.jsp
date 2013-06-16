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
	
	<tiles:put name="css">
		
	</tiles:put>

	<tiles:put name="title" type="String">
		<bean:message key="principal.titulo" />
	</tiles:put>
	
	<tiles:put name="encabezado" type="String">
		<bean:message key="principal.titulo" />		 
	</tiles:put>
	
	<tiles:put name="contenido" type="String">
		<img src="<%=request.getContextPath()%>/images/logoPROMEFRUT.jpg" height="70%" style="position: absolute; top:10%; left:0%; opacity: 0.20; filter:alpha(opacity=20); "/>
		<img src="<%=request.getContextPath()%>/images/OIMA.png" height="70%" style="position: absolute; top:10%; left:55%; opacity: 0.20; filter:alpha(opacity=20); "/>
		<!--  -->
	</tiles:put>

</tiles:insert>

