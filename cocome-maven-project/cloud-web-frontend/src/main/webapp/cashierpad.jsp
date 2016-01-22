<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function changeText(a) {
		var x = document.getElementById('barcodetext');
		x.value = x.value.toString() + a;
	}
</script>
<style type="text/css">
 
#desc2 {
	-webkit-transform: rotate(270deg);
	-moz-transform: rotate(270deg);
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to CoCome: Cashier</title>
</head>
<body>
	<f:view>
		<table align="center">
	
		<tr>
			<td> <iframe src="header.jsp" name="header"  width="1300" height="140"  frameborder="0"></iframe> </td> 
		</tr>
		
		<tr>
			<td> <iframe src="navigation.jsp" name="navigation"  width="200" height="500" align="right" > </iframe></td>
			
			<td> <iframe src="cashdesck.jsp" name="cash desck" width="1290" height="500" align="right" ></iframe>
		</tr>
		</table>
<%-- 		<h:form id="sale"> --%>
<%-- 		 <h:messages style="color:red;margin:8px;" /> --%>
<!-- 			<br> -->
<!-- 			<br> -->
<%-- 			<h2> --%>
<%-- 				<font color="white"> Welcome to </font> <font color="red"> --%>
<%-- 					CoCome: </font> <h:outputText value="#{createEnterprise.enterpriseName}"/> <br> --%>
<%-- 									<h:outputText value="#{createEnterprise.storeName}"/> <br> --%>
<%-- 									<h:outputText value="#{createEnterprise.storeLocation}"/> --%>
<%-- 			</h2>  --%>
<!-- 			<div align="center"> -->
<!-- 				<table> -->
<!-- 					<tr> -->
<!-- 						<td> -->

<!-- 							<p class="submit"> -->
<%-- 								<h:commandButton value="Home" action="#{newSaleProcess.goHome}" /> --%>
<!-- 							</p> -->

<!-- 						</td> -->

<!-- 						<td> -->

<!-- 							<p class="submit"> -->
<%-- 								<h:commandButton value="Finish Sale" --%>
<%-- 									action="#{newSaleProcess.finish}" /> --%>
<!-- 							</p> -->

<!-- 						</td> -->

<!-- 						<td> -->
<!-- 							<p class="submit"> -->
<%-- 								<h:commandButton value="Logout" action="#{login.logout}" /> --%>
<!-- 							</p> -->
<!-- 						</td> -->
<!-- 					</tr> -->
<!-- 				</table> -->
<!-- 			</div> -->

<!-- 			<br> -->
<!-- 			<br> -->

<!-- 			<table> -->
<!-- 				<tr> -->

<!-- 					<td class="fiveboxes"> -->
<!-- 						<p> Image </p> -->
<!-- 					</td> -->
<!-- 					<td > -->
<!-- 						<table> -->
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 									<p class="submit"> -->
<%-- 										<h:commandButton value="Bar Payment" --%>
<%-- 											action="#{newSaleProcess.barpay}" /> --%>
<!-- 									</p> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 							<tr> -->
<%-- 								<td><h:panelGroup rendered="#{not newSaleProcess.express}"> --%>
<!-- 										<p class="submit"> -->
<%-- 											<h:commandButton value="Card Payment" --%>
<%-- 												action="#{newSaleProcess.cardpay}" /> --%>
<!-- 										</p> -->
<%-- 									</h:panelGroup></td> --%>
<!-- 							</tr> -->
<!-- 							<tr> -->
<!-- 								<td> -->
<!-- 									<p class="submit"> -->
<%-- 										<h:commandButton value="#{newSaleProcess.expressmode}" --%>
<%-- 											action="#{newSaleProcess.disableMode}" rendered="#{not newSaleProcess.expressmode == 'Express Mode'}"/> --%>
<!-- 									</p> -->
<!-- 								</td> -->
<!-- 							</tr> -->
<!-- 						</table> -->

<!-- 					</td> -->

<!-- 					<td class="contentarea"> -->
<!-- 						<table> -->

<!-- 							<tr> -->

<!-- 								<td> -->

<!-- 									<table> -->

<!-- 										<tr> -->
<!-- 											<td class="user"> -->
<%-- 												<h2>User Display:</h2> --%>
<!-- 											</td> -->
<%-- 											<td class="user"><h:outputText --%>
<%-- 													value="#{newSaleProcess.sum}" /></td> --%>
<!-- 										</tr> -->

<!-- 										<tr> -->
<!-- 											<td class="user"> -->
<%-- 												<h2>Express Light:</h2> --%>
<!-- 											</td> -->
<!-- 											<td class="user"> -->
<%-- 											<h:graphicImage url="faces/WEB-INF/image/blackBall.png" styleClass="add-icon" rendered="#{newSaleProcess.color == 'black'}"/> --%>
<%-- 											<h:graphicImage url="faces/WEB-INF/image/green.png" styleClass="add-icon" rendered="#{newSaleProcess.color == 'green'}"/> --%>
<!-- 											</td> -->
<!-- 										</tr> -->

<!-- 										<tr> -->
<!-- 											<td class="user"> -->
<%-- 												<h2>Bar Code of the Product :</h2> --%>
<!-- 											</td> -->
<%-- 											<td class="user"><h:inputText id="barcodetext" --%>
<%-- 													value="#{newSaleProcess.barcode}" /> --%>
<%-- 													<h:message for="barcodetext" style="color:red"/></td> --%>
<!-- 											<td class="user"> -->
<!-- 												<p class="submit"> -->
<%-- 													<h:commandButton value="Scan" --%>
<%-- 														action="#{newSaleProcess.scan}" /> --%>
<!-- 												</p> -->
<!-- 											</td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 										<td> -->
<!-- 										</td> -->
<!-- 										<td colspan="1"> -->
<!-- 										<table> -->
<!-- 										<tr> -->
<!-- 											<td> -->
<!-- 												<table> -->
<!-- 													<tr> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="7" --%>
<%-- 																	action="#{newSaleProcess.seven}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="8" --%>
<%-- 																	action="#{newSaleProcess.hight}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="9" --%>
<%-- 																	action="#{newSaleProcess.nine}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 													<tr> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="4" --%>
<%-- 																	action="#{newSaleProcess.four}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="5" --%>
<%-- 																	action="#{newSaleProcess.five}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="6" --%>
<%-- 																	action="#{newSaleProcess.six}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 													<tr> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="1" --%>
<%-- 																	action="#{newSaleProcess.one}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="2" --%>
<%-- 																	action="#{newSaleProcess.two}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="3" --%>
<%-- 																	action="#{newSaleProcess.three}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 													<tr> -->
<!-- 														<td> -->
<!-- 															<p class="submit"> -->
<%-- 																<h:commandButton value="0" --%>
<%-- 																	action="#{newSaleProcess.nil}" /> --%>
<!-- 															</p> -->
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 												</table> -->
<!-- 											</td> -->
<!-- 										</tr> -->

<!-- 									</table> -->
<!-- 										</td> -->
<!-- 										</tr> -->
<!-- 									</table> -->
									

<!-- 								</td> -->

<!-- 							</tr> -->


<!-- 						</table> -->


<!-- 						<table> -->

<!-- 							<tr> -->
<!-- 								<td></td> -->
<!-- 							</tr> -->

<!-- 						</table> -->

<!-- 					</td>		 -->
<!-- 					<td class="printer"> -->
<%-- 					<h:dataTable --%>
<%-- 							value="#{newSaleProcess.buyOfProduct}" var="stockItem" --%>
<%-- 							styleClass="jsfDataTable" headerClass="jsfDataTableHeader" --%>
<%-- 							rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow"> --%>
<%-- 							<h:column> --%>
<%-- 								<f:facet name="header"><h:outputText value="Product Name"/> </f:facet> --%>
<%-- 								<h:outputText value="#{stockItem.productName}"/> --%>
<%-- 							</h:column>  --%>
<%-- 							<h:column> --%>
<%-- 								<f:facet name="header"><h:outputText value="Product ID"/> </f:facet> --%>
<%-- 								<h:outputText value="#{stockItem.productBarcode}" /> --%>
<%-- 							</h:column>  --%>
<%-- 					                <h:column> --%>
<%-- 								<f:facet name="header"><h:outputText value="Bar Code"/></f:facet>								 --%>
<%-- 								<h:outputText value="#{stockItem.stockItemSalesPrice}" /> --%>
<%-- 							</h:column>  --%>
<%-- 					 </h:dataTable>  --%>
<!-- 					        <br>  -->
<%-- 					        <h:panelGroup rendered="#{newSaleProcess.payFeld}"> --%>
<!-- 					        <table> -->
<!-- 					        	<tr> -->
<%-- 								<td> <h:outputText value="Pay: " /> </td> --%>
<%-- 								<td> <h:inputText id ="paytext" value="#{newSaleProcess.payString}" /> </td> --%>
<!-- 								</tr> -->
<!-- 								<tr> -->
<%-- 								<td> <h:outputText value="Back:  " /> </td> --%>
<%-- 								<td> <h:outputText value="#{newSaleProcess.back}" /> </td> --%>
<!-- 							    </tr> -->
<%-- 								<tr> <td> <h:commandButton action="#{newSaleProcess.checkBarPay}" value="Pay" /> </td> --%>
<%-- 									 <td> <h:message for="paytext" style="color:red"/> </td>  --%>
<!-- 								</tr> -->
<!-- 							</table> -->
<%-- 							</h:panelGroup> --%>
							
<%-- 							<h:panelGroup rendered="#{newSaleProcess.payed}"> --%>
<!-- 					        <table> -->
<!-- 					        	<tr> -->
<%-- 								<td> <h:outputText value="Total: " /> </td> --%>
<%-- 								<td> <h:outputText value="#{newSaleProcess.sum}" /> </td> --%>
<!-- 								</tr> -->
								
<!-- 								<tr> -->
<%-- 								<td> <h:inputText id ="pintext" value="#{newSaleProcess.pin}" /> </td> --%>
<!-- 								</tr> -->

<!-- 								<tr> -->
<%-- 									<td> <h:commandButton action="#{newSaleProcess.checkCardpay}" value="Pay" /> </td> --%>
<%-- 									<td> <h:message for="pintext" style="color:red"/> </td> --%>
<!-- 								</tr> -->
<!-- 							</table> -->
<%-- 							</h:panelGroup>  --%>


<!-- 				</tr> -->

<!-- 			</table> -->
<%-- 		</h:form> --%>
	</f:view>
</body>
</html>