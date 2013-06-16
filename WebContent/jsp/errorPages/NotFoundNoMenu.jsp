<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<%@ page import="java.util.List,java.util.ArrayList,java.util.Map"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<tiles:insert name="plantillaSinMenu" definition="plantillaSinMenu">

	<tiles:put name="base">
		<html:base />
	</tiles:put>
	
	<tiles:put name="title" type="String">
		<bean:message key="error.pageNotFound.title" />
	</tiles:put>
	
	<tiles:put name="encabezado" type="String">
		
	</tiles:put>
	
	<tiles:put name="contenido" type="String">
		<form>							
			<center>
				<img src='<%=request.getContextPath()%>/images/simefrut.png' align="middle" />
				<br/>
				<br/>
				<br/>
				<span class="error" style="font-size:20pt;" > 
					<bean:message key="error.pageNotFound.title" />
				</span><br/>
				<img src="<%=request.getContextPath()%>/images/404-error-icon.jpg" align="middle" />
				<br><span class="error" style="font-size:20pt;" > <bean:message key="error.pageNotFound" /> </span>														       
				<hr width="80%"  >
			</center>
		</form>		
		<br>
  
	</tiles:put>
</tiles:insert>