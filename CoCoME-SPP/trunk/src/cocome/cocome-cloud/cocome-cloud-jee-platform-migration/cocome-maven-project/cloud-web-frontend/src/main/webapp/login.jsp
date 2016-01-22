<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns:p="http://xmlns.jcp.org/jsf/passthrough">
<head>
<style type="text/css">
<%@include file = "WEB-INF/css/style.css" %>
<%@include file = "WEB-INF/css/table_style.css" %>
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body >
	<section class="container">
	<div class="login">
		<h1>Login to CoCoME Project</h1>
		<f:view>
<%-- 			<h:form id="formLogin" prependId="false" onsubmit="document.getElementById('formLogin').action = 'j_security_check';"> --%>
				<h:form id="formLogin" prependId="false">
				<h:panelGroup rendered="#{login.invalid}">
							<font color=red> Invalid User Name or Password </font>
				</h:panelGroup>
				<p>
					<h:selectOneMenu value="#{login.manager}" onchange="submit()"
						valueChangeListener="#{login.selectItem}" >
						<f:selectItem itemValue="Enterprise Manager" itemLabel="Enterprise Manager" />
						<f:selectItem itemValue="Store Manager" itemLabel="Store Manager" />
						<f:selectItem itemValue="Stock Manager" itemLabel="Stock Manager"/>
						<f:selectItem itemValue="Cashier" itemLabel="Cashier"/>
						<f:selectItem itemDisabled="true" itemValue="#{null}"  itemLabel="_________________"/>
						<f:selectItem itemDisabled="true" itemValue="#{null}"  itemLabel=""/>
						<f:selectItem itemValue="admin" itemLabel="Admin" />
						<f:selectItem itemValue="Database Manager" itemLabel="Database Manager" />
					</h:selectOneMenu>
				</p>
				<p>
				<h:outputText value="Store ID: "/>
				<h:inputText value="#{login.storeId}" rendered="#{login.ifStore}"/>
				</p>
				<p>
					<h:outputText value="User ID: "/>
<%-- 					<h:inputText id="j_username" value="#{login.userName}" /> --%>
					<h:inputText  value="#{login.userName}" />
				</p>
				<p>
					<h:outputText value="Password: "/>
<%-- 					<h:inputSecret id="j_password" value="#{login.password}" /> --%>
					<h:inputSecret  value="#{login.password}" />
				</p>
				<p class="submit">
					<h:commandButton type="submit" value="Login" action="#{login.setUserPer}" actionListener="#{createEnterprise.attrListener}">
					     <f:attribute name="enterpriseName" value="#{login.enterpriseName}"/>
					     <f:attribute name="storeId" value="#{login.storeId}"/>
					     <f:attribute name="storeName" value="#{login.storeName}"/>
					     <f:attribute name="storeLocation" value="#{login.storeLocation}"/>
					</h:commandButton>
				</p>
			</h:form>
		</f:view>
	</div>
	</section>
</body>
</html>