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
<%-- <%@include file = "WEB-INF/css/styleNew.css" %> --%>
<%-- <%@include file = "WEB-INF/css/style-ie.css" %> --%>
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<f:view>
		<h:form>
			<br>
			<br>

			<h2>
				<font color="white"> Welcome to </font> <font color="red">
					CoCoME: </font> <br>
				<h:outputText value="Enterprise Name: " />
				<h:outputText value="#{createEnterprise.enterpriseName}" />
				<br>
				<h:outputText value="Store ID: " />
				<h:outputText value="#{createEnterprise.storeId}" />
				<br>
				<h:outputText value="Store Name: " />
				<h:outputText value="#{createEnterprise.storeName}" />
				<br>
				<h:outputText value="Store Location: " />
				<h:outputText value="#{createEnterprise.storeLocation}" />
			</h2>
			<br>

			<div align="center">
				<table>
					<tr>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'storemanager') or (login.per == 'stockmanager') or (login.per == 'cashier')}">
								<p class="submit">
									<h:commandButton action="#{login.logout}" value="logout">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{orderProduct}" />
									</h:commandButton>
								</p>
							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'cashier')}">
								<p class="submit">
									<h:commandButton value="Start new Sale"
										action="#{newSaleProcess.invoke}"
										actionListener="#{newSaleProcess.attrListener}">

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton>
								</p>
							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'storemanager')}">
								<p class="submit">
									<h:commandButton value="Order Product"
										action="#{orderProduct.invoke}"
										actionListener="#{orderProduct.getSelected}">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{showReportOfProduct}" />

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />

									</h:commandButton>
								</p>

							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'storemanager')}">
								<p class="submit">
									<h:commandButton action="#{showReportOfProduct.invoke}"
										actionListener="#{showReportOfProduct.getSelected}"
										value="Show Stock Reports">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{orderProduct}" />

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton>
								</p>
							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'storemanager')}">
								<p class="submit">
									<h:commandButton value="Change Price"
										action="#{changePrice.invoke}"
										actionListener="#{changePrice.attrListener}">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{orderProduct}" />

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton>
								</p>
							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'stockmanager')}">
								<p class="submit">
									<h:commandButton value="Receive Ordered Products" action="#{receiveOrderedProducts.invoke}"
										actionListener="#{receiveOrderedProducts.attrListener}">
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{orderProduct}" />
										<f:actionListener binding="#{showReportOfProduct}" />

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton>
								</p>
							</h:panelGroup></td>
					</tr>
				</table>
			</div>
			<!-- Stroe Manager : Order Group -->
			<h:panelGroup rendered="#{orderProduct.ordert}">
				<section class="container">
				<div class="tableD">
					<h:dataTable value="#{orderProduct.stockItemList}" var="s"
						binding="#{orderProduct.data}" styleClass="jsfDataTable"
						headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
								<f:facet name="header"><h:outputText value="Product Name"/> </f:facet>
								<h:outputText value="#{s.productName}"/>
							</h:column> 
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Barcode" />
							</f:facet>
							<h:outputText value="#{s.productBarcode}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item min stock" />
							</f:facet>
							<h:outputText value="#{s.stockItemMinStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item max stock" />
							</f:facet>
							<h:outputText value="#{s.stockItemMaxStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item actuelly amount" />
							</f:facet>
							<h:outputText value="#{s.stockItemAmount}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item order amount" />
							</f:facet>
							<h:inputText value="#{orderProduct.orderAmount}" />
							<h:commandButton action="#{orderProduct.order}" value="Order" />
						</h:column>

					</h:dataTable>
					<br>
					<br>
					<h:commandButton value="Save" action="#{orderProduct.save}"/>
				</div>
				</section>
			</h:panelGroup>


			<!-- Stroe Manager : Change Price Group -->
			<h:panelGroup rendered="#{changePrice.showChangePrise}">
				<section class="container">
				<div class="tableD">
					<h:dataTable value="#{changePrice.stockItems}" var="stockItem"
						binding="#{changePrice.data}" styleClass="jsfDataTable"
						headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{stockItem.name}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Barcode" />
							</f:facet>
							<h:outputText value="#{stockItem.barcode}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sale Price" />
							</f:facet>
							<h:outputText value="#{stockItem.stockItemTO.salesPrice}" rendered="#{not stockItem.editable}"/>
							<h:inputText  value="#{changePrice.newSalePrice}" rendered="#{stockItem.editable}"/>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Action" />
							</f:facet>
							<h:commandButton action="#{changePrice.editStockItemPrice}"
								value="Edit Sale Price" rendered="#{not stockItem.editable}" />
							<h:commandButton action="#{changePrice.changeSalesPrice}"
								value="Save" rendered="#{stockItem.editable}" />	
						</h:column>
					</h:dataTable>
				</div>
				</section>
			</h:panelGroup>


			<!-- Stroe Manager : Show Stock Report of one Product or all -->
			<h:panelGroup rendered="#{showReportOfProduct.showSearchContinent}">
				<section  class="container">
					<table>
						<tr>	<td>	<h:outputText value="Store ID:      "/> <h:inputText value="#{showReportOfProduct.searchID}"/>	</td> </tr>
						<tr>	<td>	<h:commandButton action="#{showReportOfProduct.searchStockItemsOfStore}" value="Get Stock Items Report"/> </td> </tr>
					</table>
				</section>
			</h:panelGroup>
			<h:panelGroup rendered="#{showReportOfProduct.showStockreport}">
				<section class="container">
				<h:outputText value="#{showReportOfProduct.message}"/>
				<div class="login">
					<h:dataTable value="#{showReportOfProduct.stockItemList}"
						var="item"
						styleClass="jsfDataTable" headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{item.productName}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Barcode" />
							</f:facet>
							<h:outputText value="#{item.productBarcode}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item min stock" />
							</f:facet>
							<h:outputText value="#{item.stockItemMinStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item max stock" />
							</f:facet>
							<h:outputText value="#{item.stockItemMaxStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item actuelly amount" />
							</f:facet>
							<h:outputText value="#{item.stockItemAmount}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item price" />
							</f:facet>
							<h:outputText value="#{item.stockItemSalesPrice}" />
						</h:column>

					</h:dataTable>

					<table>
						<tr>
							<td><h:panelGroup rendered="#{createEnterprise.showMessage}">
									<h:outputText value="#{createEnterprise.message}" />
								</h:panelGroup></td>
						</tr>
					</table>
				</div>
				</section>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{receiveOrderedProducts.showReceive}">
				<section class="container">
				<div>
					
					<table>
					<tr>
					<td colspan="3"> <h:outputText value="Order ID" rendered="#{not receiveOrderedProducts.showamounthere}"/> </td> <td> <h:inputText value="#{receiveOrderedProducts.orderId}" rendered="#{not receiveOrderedProducts.showamounthere}"/> </td>
					</tr>
					<tr>
					<td> <h:commandButton action="#{receiveOrderedProducts.searchProductOrder}" value="Search" rendered="#{not receiveOrderedProducts.showamounthere}"/> </td> 
					</tr>
					<tr>
					<td colspan="3"> <h:outputText value="Amount Here" rendered="#{receiveOrderedProducts.showamounthere}"/></td> <td> <h:inputText value="#{receiveOrderedProducts.amountHere}" rendered="#{receiveOrderedProducts.showamounthere}"/> </td> 
					</tr>
					<tr>
					<td> <h:commandButton action="#{receiveOrderedProducts.checkTheamount}"  rendered="#{receiveOrderedProducts.showamounthere}" value="Check"/> </td>
					</tr>
					<tr>
						<td> <h:commandButton value="Get All" action="#{receiveOrderedProducts.getAllOrderProduct}"/> </td>
					</tr>
					</table>
						
					<table>
						<tr>
							<td>
									<h:outputText value="#{receiveOrderedProducts.orderMessage}" />
							
						</tr>
					</table>
					<br>
					<br>
					<br>
					<h:dataTable value="#{receiveOrderedProducts.listOfReceProductOrder}" var="r"
						binding="#{receiveOrderedProducts.data}" styleClass="jsfDataTable"
						headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{r.productName}" />
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Order ID" />
						</f:facet>
							<h:outputText value="#{r.productOrderID}" />
						</h:column>
						
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Barcode" />
						</f:facet>
							<h:outputText value="#{r.productBarcode}"/>
						</h:column>
						
						<h:column>
						<f:facet name="header">
								<h:outputText value="Order DeliveryDate" />
						</f:facet>
							<h:outputText value="#{r.orderDeliveryDate}" rendered="#{not r.editable}"/>
							<h:inputText  value="#{receiveOrderedProducts.orderDeliveryDate}" rendered="#{r.editable}"/>
						</h:column>
						
						<h:column>
						<f:facet name="header">
								<h:outputText value="Order Ordering Date"/>
						</f:facet>
							<h:outputText value="#{r.orderOrderingDate}"/>
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Order Amount" />
						</f:facet>
							<h:outputText value="#{r.orderAmount}"/>
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Action" />
						</f:facet>
							<h:inputText value="#{r.incommingAmount}" />
							<h:commandButton action="#{receiveOrderedProducts.checkAmount}" value="Check"/>
							<h:graphicImage url="faces/WEB-INF/image/check.png" styleClass="add-icon" rendered="#{r.incommingAmount == r.orderAmount}"/>
							<h:graphicImage url="faces/WEB-INF/image/no_check.png" styleClass="add-icon" rendered="#{not (r.incommingAmount == r.orderAmount)}"/>
						</h:column>
			</h:dataTable>
				</div>
				</section>
			</h:panelGroup>
			
			

			
		

		</h:form>
	</f:view>
</body>
</html>