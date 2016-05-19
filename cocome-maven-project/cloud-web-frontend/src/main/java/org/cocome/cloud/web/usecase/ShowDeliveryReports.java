package org.cocome.cloud.web.usecase;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.cocome.cloud.web.entitywrapper.StoreTOWrapper;


@ManagedBean
@SessionScoped
public class ShowDeliveryReports implements ActionListener, IUseCase {
	
	private List<StoreTOWrapper> listOfStore;
	private String enterpriseId;
	private boolean showFeld = false;
	private boolean showReport = false;

	public List<StoreTOWrapper> getListOfStore() {
		return listOfStore;
	}

	public void setListOfStore(List<StoreTOWrapper> listOfStore) {
		this.listOfStore = listOfStore;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public boolean isShowFeld() {
		return showFeld;
	}

	public void setShowFeld(boolean showFeld) {
		this.showFeld = showFeld;
	}

	public boolean isShowReport() {
		return showReport;
	}

	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	@Override
	public String invoke() throws Exception {
		showFeld = true;
		listOfStore = new ArrayList<>();
		return null;
	}
	
	public String searchEnterprise() {
//		
//		GetXMLFromBackend get = new GetXMLFromBackend();
//		CSVParser parser = new CSVParser();
//		
//		if (enterpriseId != null) {
//			//enterpriseId.matches("[-][0-9]*")
//			
//					
//				String dataEnterprise;	
//				try {
//					
//					dataEnterprise = get.getEnterprises("id==" + enterpriseId);
//					
//					parser.parse(dataEnterprise);
//					
//					if (parser.getModel().getRows().size() > 0) {
//						
//						CSVParser storeParser = new CSVParser();
//						String enterpriseName = parser.getModel().getRows().get(0).getColumns().get(1).getValue();
//						String dataStore = get.getStores("enterprise.name=like'" + enterpriseName + "'");
//						
//						storeParser.parse(dataStore);
//						
//						for (int i = 0; i < storeParser.getModel().getRows().size(); i ++) {
//							Store e = new Store();
//							e.setEnterpriseName(storeParser.getModel().getRows().get(i).getColumns().get(0).getValue());
//							e.setStoreId(storeParser.getModel().getRows().get(i).getColumns().get(1).getValue());
//							e.setStoreName(storeParser.getModel().getRows().get(i).getColumns().get(2).getValue());
//							e.setStoreLocation(storeParser.getModel().getRows().get(i).getColumns().get(3).getValue());
//
//							listOfStore.add(e);
//						}
//						
//						CSVParser orderParser = new CSVParser();
//						
//						for (Store e : listOfStore) {
//							String dataOrder = get.getProductOrder("store.id=" + e.getStoreId());
//							orderParser.parse(dataOrder);
//							
//							for (int i = 0; i < orderParser.getModel().getRows().size(); i++) {
//								
//								ProductOrder o = new ProductOrder();
//								
//								o.setEnterpriseName(enterpriseName);
//								o.setStoreID(e.getStoreId());
//								o.setStoreName(e.getStoreName());
//								o.setStoreLocation(e.getStoreLocation());
//								//o.setProductName(productName);
//								o.setProductBarcode(parser.getModel().getRows().get(i).getColumns().get(4).getValue());
//								o.setProductOrderID(parser.getModel().getRows().get(i).getColumns().get(0).getValue());
//								o.setOrderOrderingDate(parser.getModel().getRows().get(i).getColumns().get(6).getValue());
//								o.setOrderDeliveryDate(parser.getModel().getRows().get(i).getColumns().get(5).getValue());
//								o.setOrderAmount(parser.getModel().getRows().get(i).getColumns().get(7).getValue());
//								
//								CSVParser productParser = new CSVParser();
//								productParser.parse(get.getProducts("barcode==" + o.getProductBarcode()));
//								o.setProductName(productParser.getModel().getRows().get(0).getColumns().get(1).getValue());
//								
//								DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//								
//								Date deliveryDate = dateFormat.parse(o.getOrderDeliveryDate());
//								Date orderingDate = dateFormat.parse(o.getOrderOrderingDate());
//								
//								if (deliveryDate.after(orderingDate)) {
//									listOfOrder.add(o);
//									System.out.println("Hallo");
//								}
//							}
//						}
//						
//						showReport = true;
//					} else {
//						
//					}
//					
//				} catch (Exception ex) {
//					
//				}
//				
//				
//			} else {
//				
//			}
//		
		
		return null;
	}

	@Override
	public void attrListener(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String finish() {
		showFeld = false;
		showReport = false;
		listOfStore = new ArrayList<>();
		enterpriseId = null;
		return null;
	}

	@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		finish();
		
	}
	
	
	
	
	

}
