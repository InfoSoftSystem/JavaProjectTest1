<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<tiles:insert name="plantillaBase" definition="plantillaBase">

	<tiles:put name="base">
		<html:base />
	</tiles:put>

	<tiles:put name="help" type="String">
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="acceso.denegado.titulo" />
	</tiles:put>
	
	<tiles:put name="encabezado" type="String">
		<bean:message key="acceso.denegado.titulo" />
	</tiles:put>
	
	<tiles:put name="contenido" type="String">
		<form>							
			<center>				
				<img src="<%=request.getContextPath()%>/images/LockedResource.jpg" align="middle" />
				<br><span class="error" style="font-size:20pt;" > <bean:message key="acceso.denegado" /> </span>														       
				<hr width="80%"  >
			</center>
		</form>		
		<br>
	</tiles:put>

</tiles:insert>

