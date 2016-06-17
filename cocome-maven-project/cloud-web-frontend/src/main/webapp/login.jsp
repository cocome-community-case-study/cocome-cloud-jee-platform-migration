<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html">
<h:head>
<h:outputStylesheet name="css/style.css" />
<h:outputStylesheet name="css/table_style.css" />
<h:meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
</h:head>
<h:body>
	<f:view>
	<ui:component>
	<div class="container">
	<div class="login">
		<h1>Login to CoCoME Project</h1>
				<h:form id="formLogin" prependId="false">
				<h:messages/>
				<p>
					<h:selectOneMenu value="#{login.requestedView}">
						<f:selectItem itemValue="#{'ENTERPRISE_VIEW'}" itemLabel="#{strings['login.enterprise_manager.text']}" />
						<f:selectItem itemValue="#{'STORE_VIEW'}" itemLabel="#{strings['login.store_manager.text']}" />
						<f:selectItem itemValue="#{'STORE_VIEW'}" itemLabel="#{strings['login.stock_manager.text']}"/>
						<f:selectItem itemValue="#{'STORE_VIEW'}" itemLabel="#{strings['login.cashier.text']}" />
						<f:selectItem itemDisabled="true" itemValue="#{null}"  itemLabel="_________________"/>
						<f:selectItem itemDisabled="true" itemValue="#{null}"  itemLabel=""/>
						<f:selectItem itemValue="#{'STORE_VIEW'}" itemLabel="#{strings['login.admin.text']}" />
						<f:selectItem itemValue="#{'ENTERPRISE_VIEW'}" itemLabel="#{strings['login.database_manager.text']}" />
						<f:ajax render="@form" execute="@form"/>
					</h:selectOneMenu>
				</p>
				<p>
					<h:outputText value="Store ID: "/>
					<h:inputText value="#{login.requestedStoreId}" rendered="#{login.storeRequired}"/>
				</p>
				<p>
					<h:outputText value="User ID: "/>
					<h:inputText  value="#{login.userName}" />
				</p>
				<p>
					<h:outputText value="Password: "/>
					<h:inputSecret  value="#{login.password}" />
				</p>
				<p class="submit">
					<h:commandButton type="submit" value="Login" action="#{login.login}" />
				</p>
			</h:form>
	</div>
	</div>
	</f:view>
	</ui:component>
</h:body>
</html>