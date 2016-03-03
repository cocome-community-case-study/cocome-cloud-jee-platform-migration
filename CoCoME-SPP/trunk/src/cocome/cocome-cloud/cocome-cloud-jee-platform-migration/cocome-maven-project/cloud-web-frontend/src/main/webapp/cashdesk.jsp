<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cash Desk</title>
</head>
<body >
<center>
<div class="body-wrapper">
<f:view>
	<div class="wrapper">
		<div class="logo">
			<h:graphicImage url="faces/WEB-INF/image/SPP_large.png" />
		</div>
	</div>
				<h:form id="sale">

					<div class="search">

						<!-- 		to select one action: home (back to store UI), Finish Sale or Logout    -->
						<center>
							<h:messages errorClass="errorMessage" infoClass="infoMessage"
								layout="table" globalOnly="true" showDetail="false"
								showSummary="true" style="color:red;margin:8px;" />
							<h:message for="barcodetext" style="color:red" />
							<h:message for="pay-bar" style="color:red" />
							<h:message for="pay-card" style="color:red" />
							<h:message for="pin" style="color:red" />
							<h:message for="result" style="color:red" />
							<h:panelGroup
								rendered="#{login.per == 'admin' or login.per == 'cashier'}">
								<table>
									<tr>
										<td>

											<p class="submit">
												<h:commandButton value="Back"
													action="#{newSaleProcess.goHome}" />
											</p>

										</td>

										<td align="right">
											<p class="submit">
												<h:commandButton value="Logout" action="#{login.logout}" />
											</p>
										</td>
									</tr>
<%-- 									<h:panelGroup rendered="#{newSaleProcess.cashDeskNameSet}">
									<tr>
										<td align="center">
											<b>Cash desk name :</b> <h:outputText
											id="cashDeskName" value="  #{newSaleProcess.cashDeskName}" />
										</td>
									</tr>
									</h:panelGroup> --%>
								</table>
							</h:panelGroup>
						</center>
					</div>
					<h:panelGroup
						rendered="#{newSaleProcess.cashDeskNameSet and (login.per == 'admin' or login.per == 'cashier') 
							and newSaleProcess.saleStarted}">

						<div id="LeftSide">
							<table align="left">
								<tr>
									<td align="left"><b>User Display :</b> <h:outputText
											id="result" value="  #{newSaleProcess.userDisplayOut}" /></td>
								</tr>
								<tr>
									<td align="left"><b>Express Light :</b> <h:graphicImage
											url="faces/WEB-INF/image/blackBall.png" styleClass="add-icon"
											rendered="#{not newSaleProcess.inExpressMode}" /> <h:graphicImage
											url="faces/WEB-INF/image/green.png" styleClass="add-icon"
											rendered="#{newSaleProcess.inExpressMode}" /></td>
								</tr>

								<tr>
									<td align="left"><h:panelGroup
											rendered="#{newSaleProcess.payByCash}">
											<table>
												<tr>
													<td><h:outputText value="Cash Amount: " /></td>
													<td><h:inputText binding="#{cashAmount}" />
													</td>
												</tr>
<%-- 												<tr>
													<td><h:outputText value="Back:  " /></td>
													<td><h:outputText value="#{newSaleProcess.back}" /></td>
												</tr> --%>
												<tr>
													<td><h:commandButton id="pay-sum"
															action="#{newSaleProcess.enterCashAmount(cashAmount.value)}" value="Pay" /></td>
												</tr>
											</table>
										</h:panelGroup> <h:panelGroup rendered="#{newSaleProcess.payByCard}">
											<table>
												<tr>
													<td><h:outputText value="Card Info:  " /> <h:inputText
															id="cardInfo" binding="#{cardInfo}" /></td>
												</tr>
												
												<tr>
													<td><h:outputText value="PIN:  " /> <h:inputText
															id="pin" binding="#{pin}" /></td>
												</tr>

												<tr>
													<td><h:commandButton
															action="#{newSaleProcess.enterCardInfo(cardInfo.value, pin.value)}" value="verify" />
													</td>
												</tr>
											</table>
										</h:panelGroup></td>
								</tr>



							</table>

						</div>

						<div id="RightSide">
							<table align="right">
								<tr>
									<td align="right"><h:commandButton id="pay-bar"
											style="width: 150px" value=" Bar  Payment "
											action="#{newSaleProcess.barpay}" /></td>
								</tr>
								<tr>
									<td align="right"><h:commandButton id="pay-card"
											style="width: 150px" value=" Card Payment "
											action="#{newSaleProcess.cardpay}" 
											rendered="#{not newSaleProcess.inExpressMode}"/></td>
								</tr>
								<tr>
									<td align="right"><h:commandButton style="width: 150px"
											value=" Reset " action="#{newSaleProcess.startNewSale}" /></td>
								</tr>
							</table>
						</div>

						<div id="Center">
							<table align="center">

								<tr>
									<td align="center"><b> Bar Code of the Product : </b> <h:inputText
											id="barcodetext" style="width:145px"
											value="#{newSaleProcess.barcode}" /></td>
								</tr>
								<tr>
									<td><h:commandButton style="width: 350px" value="Scan"
											action="#{newSaleProcess.scan}" /></td>
								</tr>
								<tr>
									<td align="center">
										<table border="1">
											<tr>
												<td bgcolor="#E0E0E0">
													<table border="0" cellpadding="0" cellspacing="2">
														<tr>
															<td><h:commandButton value="  7  "
																	action="#{newSaleProcess.seven}" styleClass="button" /></td>
															<td><h:commandButton value="  8  "
																	action="#{newSaleProcess.hight}" styleClass="button" /></td>
															<td><h:commandButton value="  9  "
																	action="#{newSaleProcess.nine}" styleClass="button" /></td>
														</tr>
														<tr>
															<td><h:commandButton value="  4  "
																	action="#{newSaleProcess.four}" styleClass="button" /></td>
															<td><h:commandButton value="  5  "
																	action="#{newSaleProcess.five}" styleClass="button" /></td>
															<td><h:commandButton value="  6  "
																	action="#{newSaleProcess.six}" styleClass="button" /></td>
														</tr>
														<tr>
															<td><h:commandButton value="  1  "
																	action="#{newSaleProcess.one}" styleClass="button" /></td>
															<td><h:commandButton value="  2  "
																	action="#{newSaleProcess.two}" styleClass="button" /></td>
															<td><h:commandButton value="  3  "
																	action="#{newSaleProcess.three}" styleClass="button" /></td>
														</tr>
														<tr>
															<td><h:commandButton value="  C  "
																	action="#{newSaleProcess.removeAll}"
																	styleClass="button"
																	disabled="#{newSaleProcess.barcode == ''}" /></td>
															<td><h:commandButton value="  0  "
																	action="#{newSaleProcess.nil}" styleClass="button" /></td>
															<td><h:commandButton value="  <  "
																	action="#{newSaleProcess.removelastDigit}"
																	styleClass="button"
																	disabled="#{newSaleProcess.barcode == ''}" /></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<h:panelGroup rendered="#{newSaleProcess.showPrint}">
									<tr>
										<td align="center">
											<table>
												<tr>
													<td><h:dataTable
															value="#{newSaleProcess.printerOut}" var="printerLine"
															styleClass="jsfDataTablePrint"
															headerClass="jsfDataTableHeader"
															rowClasses="jsfDataTableOddRow,jsfDataTableEvenRow">
															<h:column>
																<h:outputText value="#{printerLine}" />
															</h:column>
														</h:dataTable>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</h:panelGroup>
							</table>
						</div>


					</h:panelGroup>

					<h:panelGroup
						rendered="#{not newSaleProcess.cashDeskNameSet and (login.per == 'admin' or login.per == 'cashier')}">
						<div id="Center">
							<table align="center">

								<tr>
									<td align="center"><b> Enter the cash desk name: </b> <h:inputText
											id="cashDeskNameText" style="width:145px"
											binding="#{input}" value="#{newSaleProcess.cashDeskName}" /></td>
								</tr>
								<tr>
									<td><h:commandButton style="width: 350px" value="Confirm"
											action="#{newSaleProcess.updateCashDeskName(input.value)}" /></td>
								</tr>
							</table>
						</div>
					</h:panelGroup>

				</h:form>

			</f:view>
</div>
</center>

</body>
</html>