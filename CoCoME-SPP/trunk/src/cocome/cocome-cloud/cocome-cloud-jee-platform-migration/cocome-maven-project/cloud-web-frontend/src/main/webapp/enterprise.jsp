<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:p="http://xmlns.jcp.org/jsf/passthrough">
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

	function testEnter(enter,id) {
		
		if (enter.equals("")) {
			document.getElementById(id).setAttribute("disabled", true);
		}
		    document.getElementById(id).setAttribute("disabled", false);
	}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<f:view>
<center>
<div class="body-wrapper">
	<h:form id="db">
	
		<div class="wrapper">
			<div class="logo">
				<h:graphicImage library="image" name="SPP_large.png" />
			</div>
		</div>
		
		<div class="navigation" >
			<center>
				<h:messages errorClass="errorMessage" infoClass="infoMessage" layout="table" globalOnly="true" showDetail="false" showSummary="true" style="color:red;margin:8px;" />
				<h:message for="createstockItem" style="color:red"/>
				<h:message for="createenterprise" style="color:red"/>
				<h:message for="createproduct" style="color:red"/>
				<h:message for="createstore" style="color:red"/>
				<table>
					
					<tr>
						
						<td> <p class="submit"><h:commandButton value="Show All Enterprises" action="#{createEnterprise.getAllFromDB}" id="showenterprise" /> </p> </td>
						<td> <p class="submit"><h:commandButton value="Create Enterprise" action="#{createEnterprise.setCreateEnterprise}" id="createenterprise" /> </p> </td>
						<td> <p class="submit"><h:commandButton value="Create Product" action="#{createEnterprise.setCreateProduct}" id="createproduct" /> </p> </td>
						<td> <p class="submit"><h:commandButton value="Show All Products" action="#{createEnterprise.getAllProduct}" id="showproduct" />	</p></td>
						<td> <p class="submit"> <h:commandButton value="Logout" action="#{login.logout}" /> </p></td>
						
					</tr>
				</table>
			</center>
		</div>
		
		<div class="id" align="left">
		</div>
		
		<div class="conetent">
			<center>
				<table >
			    								<tr>
			    									<td>
			    										<h:panelGroup rendered="#{createEnterprise.showListofProduct}">
			    										
			    											<table>
			    											<tr>
			    											<td>
																<h:dataTable value="#{createEnterprise.listOfProduct}"
																	var="good" styleClass="jsfDataTable"
																	headerClass="jsfDataTableHeader"
																	rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
																	<h:column>
																		<f:facet name="header">
																			<h:outputText value="Product Barcode" />
																		</f:facet>
																		<h:outputText value="#{good.barcode}" />
																	</h:column>
																	<h:column>
																		<f:facet name="header">
																			<h:outputText value="Product Name" />
																		</f:facet>
																		<h:outputText value="#{good.name}" />
																	</h:column>
																	<h:column>
																		<f:facet name="header">
																			<h:outputText value="Product Purchase Price" />
																		</f:facet>
																		<h:outputText value="#{good.purchasePrice}" />
																	</h:column>
																</h:dataTable>
															</td>
															</tr>
															</table>	
															
														</h:panelGroup>
														<h:panelGroup rendered="#{createEnterprise.showCreateStore}">
																<table>
																	<tr>
																		<td colspan="2"><h:outputText value="Store Name:" /></td>
																		<td><h:inputText value="#{createEnterprise.newStoreName}"  />
																		</td>
																	</tr>
																	<tr>
																		<td colspan="2"><h:outputText value="Store Location:"  /></td>
																		<td><h:inputText
																				value="#{createEnterprise.newStoreLocation}" /></td>
																	</tr>
																	<tr>
																		<td colspan="2"><h:commandButton
																				action="#{createEnterprise.createStore}" value="Create New Store" id="createstore"
																				/></td>
																	</tr>
																</table>
														</h:panelGroup>
														<h:panelGroup rendered="#{createEnterprise.viewData}">
															<table>
																<tr>
																<td>
																<h:dataTable
																	value="#{createEnterprise.listOfEnterprise}" var="enterprises"
																	binding="#{createEnterprise.data}" styleClass="jsfDataTable"
																	headerClass="jsfDataTableHeader"
																	rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Enterprise ID" />
																			</f:facet>
																			<h:outputText value="#{enterprises.id}" />
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Enterprise Name" />
																			</f:facet>
																			<h:outputText value="#{enterprises.name}" />
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Action" />
																			</f:facet>
																			<h:commandButton action="#{createEnterprise.getStores}" styleClass="add-icon"
																				title="Show All Store in this Enterprise" id="getstore" 
																				image="#{resource['icon/icon_Shop.png']}" />
																			<h:commandButton action="#{createEnterprise.setCreateStore}" styleClass="add-icon"
																				title="New Store" id="newstore" image="#{resource['image/add-icon.png']}" />
																		</h:column>
																	</h:dataTable>
																	</td>
																	</tr>
																</table>
															</h:panelGroup>
															<h:panelGroup rendered="#{createEnterprise.showCreateProduct}">
																	<table>
																		<tr>
																			<td colspan="2"><h:outputText value="Product Name:" /></td>
																			<td><h:inputText value="#{createEnterprise.productName}" size="10" />
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><h:outputText value="Product Barcode:" />
																			</td>
																			<td><h:inputText
																					value="#{createEnterprise.productBarcode}" size="10"/></td>
																		</tr>
																		<tr>
																			<td colspan="2"><h:outputText
																					value="Product Purchase Price:" /></td>
																			<td><h:inputText
																					value="#{createEnterprise.purchasePrice}" size="10" /></td>
																		</tr>
																		<tr>
																			<td colspan="2"><h:commandButton
																					action="#{createEnterprise.createProduct}" value="Create New Product"  id="makeproduct"/>
																			</td>
																		</tr>
																	</table>
															</h:panelGroup>
															<h:panelGroup rendered="#{createEnterprise.showCreateEnterprise}">
																	<table>
																		<tr>
																			<td > <h:outputLabel value="Enterprise Name:"/> </td>
																			<td>
																				<p>
																					<h:inputText value="#{createEnterprise.enterpriseName}" size="10"/>
																				</p>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="1" >
																					<h:commandButton action="#{createEnterprise.invoke}"value="Create New Enterprise"  id="makeenterprise"/>
																			</td>
																		</tr>
																		</table>
															</h:panelGroup>
															<h:panelGroup rendered="#{createEnterprise.viewStores}">
																<table>
																<tr>
																<td>
																<h:dataTable
																		value="#{createEnterprise.listOfStores}" var="s"
																		binding="#{createEnterprise.data_stores}"
																		styleClass="jsfDataTable" headerClass="jsfDataTableHeader"
																		rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Enterprise Name" />
																			</f:facet>
																			<h:outputText value="#{s.enterpriseTO.name}" />
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Store ID" />
																			</f:facet>
																			<h:outputText value="#{s.storeTO.id}" />
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Store Name" />
																			</f:facet>
																			<h:outputText value="#{s.storeTO.name}" rendered="#{not s.editingEnabled}"/>
																			<h:inputText value="#{createEnterprise.newStoreName}" rendered="#{s.editingEnabled}" size="8"/>
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Store Location" />
																			</f:facet>
																			<h:outputText value="#{s.storeTO.location}"  rendered="#{not s.editingEnabled}"/>
																			<h:inputText value="#{createEnterprise.newStoreLocation}"  rendered="#{s.editingEnabled}" size="8"/>
																			
																		</h:column>
																		<h:column>
																			<f:facet name="header">
																				<h:outputText value="Action" />
																			</f:facet>
																			<h:commandButton rendered="#{not s.editingEnabled}" action="#{createEnterprise.editStoreAction}" 
																				styleClass="add-icon" title="Edit Store" id="editstore"
																				image="#{resource['icon/icon_shoppingcart_edit.png']}">
																				<f:attribute name="id" value="#{s.storeTO.id}" />
																			</h:commandButton>
																			<h:commandButton action="#{createEnterprise.goToStore(s)}"
																				styleClass="add-icon" rendered="#{not s.editingEnabled}" 
																				actionListener="#{createEnterprise.attrListener}" id="gostore" title="Go To Store"
																				image="#{resource['icon/icon_Shop.png']}">
																				<f:attribute name="enterpriseName" value="#{s.enterpriseTO.name}" />
																				<f:attribute name="storeId" value="#{s.storeTO.id}" />
																				<f:attribute name="storeName" value="#{s.storeTO.name}" />
																				<f:attribute name="storeLocation" value="#{s.storeTO.location}" />
																			</h:commandButton>	
																			<h:commandButton action="#{createEnterprise.saveStoreAction}" styleClass="add-icon" 
																				rendered="#{s.editingEnabled}" title="Save" id="savestore"
																				image="#{resource['icon/icon_save.png']}">
																			</h:commandButton>
																			<h:commandButton action="#{createEnterprise.getAllStockItem}" styleClass="add-icon" 
																				image="#{resource['icon/icon_show.png']}" title="Show All StockItem" 
																				rendered="#{not s.editingEnabled}" id="getallStockItem"/>
																			<h:commandButton action="#{createEnterprise.setCreateStockItem}" styleClass="add-icon" 
																				image="#{resource['icon/icon_shoppingcart_add.png']}" 
																				title="Create Stock Item" rendered="#{not s.editingEnabled}" id="createStockItem"/>
																		</h:column>
																	</h:dataTable>
																	<h:commandButton action="#{createEnterprise.setCreateStore}" value="Add New Store" />
																	</td>
																	</tr>
																	</table>
															</h:panelGroup>
															<h:panelGroup rendered="#{createEnterprise.showListOfStockItem}">
																		<table>
																		<tr>
																		<td>
																		<h:dataTable
																				value="#{createEnterprise.listOfStockItem}" var="stock_Item"
																				binding="#{createEnterprise.data_stockItem}"
																				styleClass="jsfDataTable" headerClass="jsfDataTableHeader"
																				rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Product Name" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.productTO.name}" />
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Product Barcode" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.productTO.barcode}" />
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Stock Item Min Stock" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.stockItemTO.minStock}"  rendered="#{not stock_Item.editingEnabled}"/>
																					<h:inputText value="#{stock_Item.stockItemTO.minStock}" rendered="#{stock_Item.editingEnabled}" size="8"/>
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Stock Item Max Stock" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.stockItemTO.maxStock}"  rendered="#{not stock_Item.editingEnabled}"/>
																					<h:inputText value="#{stock_Item.stockItemTO.maxStock}" rendered="#{stock_Item.editingEnabled}" size="8"/>
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Stock Item Incoming Amount" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.stockItemTO.incomingAmount}"  rendered="#{not stock_Item.editingEnabled}"/>
																					<h:inputText value="#{stock_Item.stockItemTO.incomingAmount}" rendered="#{stock_Item.editingEnabled}" size="8"/>
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Stock Item Amount" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.stockItemTO.amount}"  rendered="#{not stock_Item.editingEnabled}"/>
																					<h:inputText value="#{stock_Item.stockItemTO.amount}" rendered="#{stock_Item.editingEnabled}" size="8"/>
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Stock Item Sale Price" />
																					</f:facet>
																					<h:outputText value="#{stock_Item.stockItemTO.salesPrice}"  rendered="#{not stock_Item.editingEnabled}"/>
																					<h:inputText value="#{stock_Item.stockItemTO.salesPrice}" rendered="#{stock_Item.editingEnabled}" size="8"/>
																				</h:column>
																				<h:column>
																					<f:facet name="header">
																						<h:outputText value="Action" />
																					</f:facet>
																					<h:commandButton action="#{createEnterprise.setEditStockItemAction}" 
																						styleClass="add-icon" title="Edit Stock Item" 
																						rendered="#{not stock_Item.editingEnabled}"  id="changestockItem"
																						image="#{resource['icon/icon_shoppingcart_edit.png']}" />
																					<h:commandButton action="#{createEnterprise.saveStockItemAction}" 
																						styleClass="add-icon" title="Save" rendered="#{stock_Item.editingEnabled}" id="savechangestockItem"
																						image="#{resource['icon/icon_save.png']}" />
																				</h:column>
																				</h:dataTable>
																			</td> 
																			</tr>
																			</table>	
																</h:panelGroup>	
																<h:panelGroup rendered="#{createEnterprise.showCreateStockItem}">
																				<table>
																				<tr>
																				<td>
																				<h:dataTable
																						value="#{createEnterprise.listOfNewStockItems}" var="newStockItem"
																						styleClass="jsfDataTable" headerClass="jsfDataTableHeader"
																						rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow"
																						>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Product Barcode"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.productTO.barcode}" size="8"/>
																						</h:column>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Stock Item Min Stock"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.stockItemTO.minStock}" size="8"/>
																						</h:column>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Stock Item Max Stock"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.stockItemTO.maxStock}" size="8"/>
																						</h:column>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Stock Item Incomming Amount"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.stockItemTO.incomingAmount}" size="8"/>
																						</h:column>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Stock Item Amount"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.stockItemTO.amount}" size="8"/>
																						</h:column>
																						<h:column>
																						<f:facet name="header">
																						 <h:outputText value="Stock Item Sales Price"/>
																						</f:facet>
																						<h:inputText value="#{newStockItem.stockItemTO.salesPrice}" size="8"/>
																						</h:column>
																				</h:dataTable>
																				</td> 
																					</tr>
																					 
																					<tr>
																					<td>
																					<table>
																						<tr>
																						<td> 
																						<h:commandButton action="#{createEnterprise.addNewStockItem}" value="Add New Stock Items"/>
																						</td>
																						<td>
																						<h:commandButton action="#{createEnterprise.createStockItem}" value="Create New Stock Item" id="makestockItem"/>
																						</td>
																						</tr>
																					</table>
																				
																				
																					</td> 
																					</tr>
																					</table>
																</h:panelGroup>	
			    									</td>
			    								</tr>
			    	</table>
			</center>
		</div>
		
		<div class="message" align="left">
			Messages :
				    <br>
				    <br>
						<h:panelGroup rendered="#{createEnterprise.showMessage}">
						<table>
				    	<tr>
						<td> 
							<h:dataTable var="message" value="#{createEnterprise.messages}">
								<h:column>
									    <h:outputText value="#{message}"/>
								</h:column>
							</h:dataTable>
						</td> 
						</tr>
						</table>
						</h:panelGroup>

						
		</div>
	
	</h:form>
	
</div>

</center>
</f:view>
</body>
</html>