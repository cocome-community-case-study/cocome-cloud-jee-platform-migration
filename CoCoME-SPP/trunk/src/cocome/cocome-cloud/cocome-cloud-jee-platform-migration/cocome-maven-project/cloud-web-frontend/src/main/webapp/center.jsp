<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
<%@include file = "WEB-INF/css/table_style.css" %>
<%@include file = "WEB-INF/css/styleNew.css" %>
<%@include file = "WEB-INF/css/style-ie.css" %> 

.add-icon {
    margin: 2px;
    padding: 1;
    border: 1;
    width: 25px;
    height: 25px;
    cursor: pointer;
}
.button {  width:110px; text-align:center;
           font-family:System,sans-serif;
           font-size:100%; }
           
.display { width:100%; text-align:right;
           font-family:System,sans-serif;
           font-size:100%; }
</style>
<script type="text/javascript">
	
function resizeInputText() {
	
	var x = document.getElementById("order");
	
	var initialSize = 25-x.value.length;
	
	x.style.fontSize = initialSize + "px";
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CoCoME</title>
</head>
<body>
<f:view>
<center>
<div class="body-wrapper">
	<h:form id="usecase">
	
		<div class="wrapper">
			<div class="logo">
				<h:graphicImage url="faces/WEB-INF/image/SPP_large.png" />
			</div>
		</div>
		
		<div class="navigation">
			<center>
				<h:messages errorClass="errorMessage" infoClass="infoMessage" layout="table" globalOnly="true" showDetail="false" showSummary="true" style="color:red;margin:8px;" />
				<h:message for="newprice" style="color:red"/>
				<h:message for="searchID" style="color:red"/>
				<h:message for="order" style="color:red"/>
				<h:message for="orderSearch" style="color:red" />
				<table>
					<tr>
						
						<td ><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Cashier')}">
								<p class="submit">	<h:commandButton value="Start new Sale"
										action="#{newSaleProcess.invoke}"
										actionListener="#{newSaleProcess.attrListener}">
										
										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton> </p>
							</h:panelGroup></td>
						<td ><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Store Manager')}">
								<p class="submit">
									<h:commandButton value="Order Product"
										action="#{orderProduct.invoke}"
										actionListener="#{orderProduct.getSelected}">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{showDeliveryReports}" /> 

										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />

									</h:commandButton>
								</p>

							</h:panelGroup></td>
						<td ><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Store Manager')}">
								<p class="submit">	<h:commandButton action="#{showReportOfProduct.invoke}"
										actionListener="#{showReportOfProduct.getSelected}"
										value="Show Stock Reports">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{orderProduct}" /> 
										<f:actionListener binding="#{showDeliveryReports}" /> 
										
										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton> </p>
							</h:panelGroup>
							</td>
						<td ><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Store Manager')}">
								<p class="submit">	<h:commandButton value="Change Price"
										action="#{changePrice.invoke}"
										actionListener="#{changePrice.attrListener}">
										<f:actionListener binding="#{receiveOrderedProducts}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{orderProduct}" />
										<f:actionListener binding="#{showDeliveryReports}" /> 
										
										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton> </p>
							</h:panelGroup></td>
						<td><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Stock Manager')}">
									<p class="submit"> <h:commandButton value="Receive Ordered Products" action="#{receiveOrderedProducts.invoke}"
										actionListener="#{receiveOrderedProducts.attrListener}">
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{orderProduct}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{showDeliveryReports}" /> 
										
										<f:attribute name="enterpriseName"
											value="#{createEnterprise.enterpriseName}" />
										<f:attribute name="storeId" value="#{createEnterprise.storeId}" />
										<f:attribute name="storeName" value="#{createEnterprise.storeName}" />
										<f:attribute name="storeLocation"
											value="#{createEnterprise.storeLocation}" />
									</h:commandButton> </p>
							</h:panelGroup></td>
<%-- 							<td ><h:panelGroup --%>
<%--    								rendered="#{(login.per == 'admin') or (login.per == 'Enterprise Manager')}">  --%>
<%--    								<p class="submit">	<h:commandButton action="#{showDeliveryReports.invoke}"  value="Show Delivery Reports">    --%>
<%--  										<f:actionListener binding="#{showReportOfProduct}" />   --%>
<%--    										<f:actionListener binding="#{receiveOrderedProducts}" />    --%>
<%--   										<f:actionListener binding="#{changePrice}" />    --%>
<%--    										<f:actionListener binding="#{orderProduct}" />   --%>
<%--  									</h:commandButton> </p>  --%>
<%--   							</h:panelGroup>    --%>
<!-- 							</td> -->
						<td id="logout"><h:panelGroup
								rendered="#{(login.per == 'admin') or (login.per == 'Enterprise Manager') or (login.per == 'Store Manager') or (login.per == 'Stock Manager') or (login.per == 'Cashier')}">

								<p class="submit">	<h:commandButton action="#{login.logout}" value="logout" >
										<f:actionListener binding="#{receiveOrderedProducts}"/>
										<f:actionListener binding="#{changePrice}" />
										<f:actionListener binding="#{showReportOfProduct}" />
										<f:actionListener binding="#{orderProduct}" />
										<f:actionListener binding="#{createEnterprise}" />
										<f:actionListener binding="#{showDeliveryReports}" /> 
									</h:commandButton> </p>

							</h:panelGroup></td>
					</tr>
				</table>
				</center>
		</div>
	 <center>
		<div class="id">
			<table align="left">
		<tr> <td>
		<table align="left">
					<tr> 
						<td> <h:outputText value="Store ID :" /> </td>  <td> <b> <h:outputText value="#{login.storeId}"/> </b> </td>
					</tr>
					
					<tr> 
						<td> <h:outputText value="Store Name :" /> </td>  <td> <b> <h:outputText value="#{login.storeName}"/> </b> </td>
					</tr>
					
					<tr> 
						<td> <h:outputText value="Store Location :" /> </td>  <td> <b> <h:outputText value="#{login.storeLocation}"/> </b> </td>
					</tr>
					
					<tr> 
						<td> <h:outputText value="Enterprise Name :" /> </td>  <td> <b> <h:outputText value="#{login.enterpriseName}"/> </b> </td>
					</tr>
				</table> 
				</td> </tr>
				
				<tr>
				<td>
			<table align="left">
					<tr> <td> </td> <td> </td> </tr>
					<tr> <td colspan="2">
					
						<h:panelGroup rendered="#{orderProduct.showOrderProductView}">
						
							<h3 align="left"> Order Product: </h3>
						</h:panelGroup>
						
						<h:panelGroup rendered="#{changePrice.showChangePrice}">
							<h3 align="left"> Change Sale Price Of Stock Item: </h3>
						</h:panelGroup>
						
						<h:panelGroup rendered="#{showReportOfProduct.showSearchContinent}">
							<h3 align="left"> Report of Stock Items in Stores: </h3>
						</h:panelGroup>
						
						<h:panelGroup rendered="#{receiveOrderedProducts.showReceive}">
							<h3 align="left"> Receive Order of Stock Items: </h3>
						</h:panelGroup>
						
						<h:panelGroup rendered="#{showDeliveryReports.showFeld}">
							<h3 align="left"> Show Delivery Reports of Enterprise: </h3>
						</h:panelGroup>
						
					</td> 
					
					</tr>
				
				</table>
				</td> </tr>
				</table>
		</div>
		<div class="width:300px;" > </div>
		<div class="conetent">
		<div align="left">
		<h:panelGroup  rendered="#{orderProduct.showOrderList}">
		<table>
			
			
			<tr>
			<td>
						<h:dataTable value="#{orderProduct.orderList}" var="elemorder"
							styleClass="jsfDataTableforOrder"
							headerClass="jsfDataTableHeader"
							rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
							
							<h:column>
								<f:facet name="header">
									<h:outputText value="Product Name"/> 
								</f:facet>
								<h:outputText value="#{elemorder.productTO.name}"/>
							</h:column>
							
							<h:column>
								<f:facet name="header">
									<h:outputText value="Product Barcode"/> 
								</f:facet>
								<h:outputText value="#{elemorder.productTO.barcode}"/>
							</h:column>
							
							<h:column>
								<f:facet name="header">
									<h:outputText value="Order Amount"/> 
								</f:facet>
								<h:outputText value="#{elemorder.amount}"/>
							</h:column>
							
						</h:dataTable>
						
			</td>	
			</tr>
			<tr>
			<td colspan="2">
				<h:commandButton  value="Submit The Order" action="#{orderProduct.save}"/>
			</tr>
						
				
		</table>
		</h:panelGroup>
		</div>
			<br>
			<!-- Stroe Manager : Order Group -->
				<h:panelGroup id="order" rendered="#{orderProduct.showOrderProductView}">
					<div class="tableD" align="left">
						<h:dataTable value="#{orderProduct.stockItemList}" var="elem"
							binding="#{orderProduct.data}" styleClass="jsfDataTable"
							headerClass="jsfDataTableHeader"
							rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
							<h:column>
									<f:facet name="header"><h:outputText value="Product Name"/> </f:facet>
									<h:outputText value="#{elem.productTO.name}"/>
								</h:column> 
							<h:column>
								<f:facet name="header">
									<h:outputText value="Product Barcode" />
								</f:facet>
								<h:outputText value="#{elem.productTO.barcode}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText value="Stock item min stock" />
								</f:facet>
								<h:outputText value="#{elem.stockItemTO.minStock}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText value="Stock item max stock" />
								</f:facet>
								<h:outputText value="#{elem.stockItemTO.maxStock}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText value="Stock item actuelly amount" />
								</f:facet>
								<h:outputText value="#{elem.stockItemTO.amount}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText value="Stock item order amount" />
								</f:facet>
								<h:inputText value="#{elem.orderAmount}" id="order" size="4" />
								<h:commandButton  action="#{orderProduct.order}" title="Order" styleClass="add-icon" image="faces/WEB-INF/icon/icon_shoppingcart_accept.png"/>
							</h:column>
	
						</h:dataTable>
						
					</div>
				</h:panelGroup>


			<!-- Stroe Manager : Change Price Group -->
			<h:panelGroup rendered="#{changePrice.showChangePrice}">
				<div class="tableD" align="left">
					<h:dataTable value="#{changePrice.stockItems}" var="stockItem"
						binding="#{changePrice.data}" styleClass="jsfDataTable"
						headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{stockItem.productTO.name}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Barcode" />
							</f:facet>
							<h:outputText value="#{stockItem.productTO.barcode}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sale Price" />
							</f:facet>
							<h:outputText value="#{stockItem.stockItemTO.salesPrice}" rendered="#{not stockItem.editingEnabled}"/>
							<h:inputText value="#{stockItem.newPrice}" binding="#{newPrice}" rendered="#{stockItem.editingEnabled}" size="4" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Action" />
							</f:facet>
							<h:commandButton action="#{changePrice.editStockItemPrice}"
								value="Edit Sale Price" rendered="#{not stockItem.editingEnabled}"  />
							<h:commandButton id="newprice" action="#{changePrice.changeSalesPrice(newPrice.value)}"
								title="Save" rendered="#{stockItem.editingEnabled}"  styleClass="add-icon" image="faces/WEB-INF/icon/icon_save.png" />	
						</h:column>
					</h:dataTable>
				</div>
			</h:panelGroup>


			<!-- Stroe Manager : Show Stock Report of one Product or all -->
			
			<h:panelGroup rendered="#{showReportOfProduct.showSearchContinent}">
			<div align="left">
					<table style="width: 255px; ">
						<tr>	<td align="left">	<h:outputText id="searchID" value="Store ID:      "/> <h:inputText value="#{showReportOfProduct.searchID}"/>	</td> </tr>
						<tr>	<td align="left">	<h:commandButton  action="#{showReportOfProduct.searchStockItemsOfStore}" value="Get Stock Items Report" /> </td> </tr>
					</table>
			</div>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{showReportOfProduct.showStockreport}">
					<h:dataTable value="#{showReportOfProduct.stockItemList}"
						var="item"
						styleClass="jsfDataTable" headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{item.productTO.name}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Barcode" />
							</f:facet>
							<h:outputText value="#{item.productTO.barcode}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item min stock" />
							</f:facet>
							<h:outputText value="#{item.stockItemTO.minStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item max stock" />
							</f:facet>
							<h:outputText value="#{item.stockItemTO.maxStock}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item actuelly amount" />
							</f:facet>
							<h:outputText value="#{item.stockItemTO.amount}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Stock item price" />
							</f:facet>
							<h:outputText value="#{item.stockItemTO.salesPrice}" />
						</h:column>

					</h:dataTable>

				
			</h:panelGroup>
			
			<h:panelGroup rendered="#{receiveOrderedProducts.showReceive}">
				<div align="left">
					
					<table>
					<tr>
					<td align="left"> <h:outputText id="orderSearch" value="Order ID: " rendered="#{not receiveOrderedProducts.showamounthere}"/>  <h:inputText value="#{receiveOrderedProducts.orderId}" rendered="#{not receiveOrderedProducts.showamounthere}"/> </td>
					</tr>
					<tr>
					<td align="left"> <h:commandButton style="width:218px;" action="#{receiveOrderedProducts.searchProductOrder}" value="Search" rendered="#{not receiveOrderedProducts.showamounthere}"/> </td> 
					</tr>
					<tr>
						<td align="left"> <h:commandButton style="width:218px;" value="Get All" action="#{receiveOrderedProducts.getAllOrderProduct}"/> </td>
					</tr>
					</table>
					
					
					<br>
					<br>
					<h:dataTable value="#{receiveOrderedProducts.listOfReceProductOrder}" var="r"
						binding="#{receiveOrderedProducts.data}" styleClass="jsfDataTable" style="width:680px;"
						headerClass="jsfDataTableHeader"
						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Product Name" />
							</f:facet>
							<h:outputText value="#{r.productTO.name}" />
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Order ID" />
						</f:facet>
							<h:outputText value="#{r.containingOrder.id}" />
						</h:column>
						
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Barcode" />
						</f:facet>
							<h:outputText value="#{r.productTO.barcode}"/>
						</h:column>
						
						<h:column>
						<f:facet name="header">
								<h:outputText value="Order Ordering Date"/>
						</f:facet>
							<h:outputText value="#{r.orderingDate}"/>
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Product Order Amount" />
						</f:facet>
							<h:outputText value="#{r.orderTO.amount}"/>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Current Status" />
							</f:facet>
							
							<h:outputText value="Arrived" rendered="#{r.arrived}" />
							<h:outputText value="Pending" rendered="#{not r.arrived}" />
						</h:column>
						<h:column>
						<f:facet name="header">
								<h:outputText value="Action" />
						</f:facet>
							
							<h:commandButton action="#{receiveOrderedProducts.rollInOrder}" title="Roll in order" styleClass="add-icon" image="faces/WEB-INF/icon/icon_gear_check.png" rendered="#{not r.arrived}"/>
						</h:column>
						
			</h:dataTable>
				</div>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{showDeliveryReports.showFeld}">
			<div align="left">
				<table>
					<tr>
						<td> <h:outputText id="enterpriseSearch" value="Enterprise ID: "/>  </td> <h:inputText value="#{showDeliveryReports.enterpriseId}" ></h:inputText> 
					</tr>
					<tr>
						<td> <h:commandButton style="width:213px;"  action="#{showDeliveryReports.searchEnterprise}" value="Show Report"/> </td>
					</tr>
				</table>				
			</div>
			</h:panelGroup>
			<h:panelGroup rendered="#{showDeliveryReports.showReport}">
				
																
			</h:panelGroup>
		</div>
	</center>	
	</h:form>
</div>

</center>
</f:view>




</body>
</html>