package org.cocome.cloud.web.usecase;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class ProductExchange {

	private String id;
	private String amountOfSale;
	private String barcode;
	private boolean toDoIt;
	
	public ProductExchange(String id, String amountOfSale, String barcode) {
		this.id = id;
		this.amountOfSale = amountOfSale;
		this.barcode = barcode;
		this.toDoIt = false;
	}
	
	public String idStoreOfMaxAmount() throws Exception{
//		
//		GetXMLFromBackend get = new GetXMLFromBackend();
//		String stocks = get.getStockItems("store.id=>0;product.barcode==" + this.barcode);
//		
//		CSVParser parser = new CSVParser();
//		parser.parse(stocks);
//		int max = Integer.parseInt(parser.getModel().getRows().get(0).getColumns().get(7).getValue());
//		String id = this.id;
//		for (int i = 0; i < parser.getModel().size(); i++) {
//			int amount = Integer.parseInt(parser.getModel().getRows().get(i).getColumns().get(7).getValue());
//			int minAmount = Integer.parseInt(parser.getModel().getRows().get(i).getColumns().get(4).getValue());
//			if (max < amount && Integer.parseInt(amountOfSale) < amount && amount > minAmount) {
//				max = Integer.parseInt(parser.getModel().getRows().get(i).getColumns().get(7).getValue());
//				String entName = parser.getModel().getRows().get(i).getColumns().get(0).getValue();
//				String storeName = parser.getModel().getRows().get(i).getColumns().get(1).getValue();
//				String store = get.getStores("enterprise.name=like'" + entName + "';name=like'" + storeName + "'");
//				
//				CSVParser storeParser = new CSVParser();
//				storeParser.parse(store);
//				
//				if (!this.id.equalsIgnoreCase(storeParser.getModel().getRows().get(0).getColumns().get(1).getValue()))
//					id = storeParser.getModel().getRows().get(0).getColumns().get(1).getValue();
//			}
//		}
		
		return id;
	}
	

	public String doIt() throws Exception {
//		GetXMLFromBackend get = new GetXMLFromBackend();
//		if (!id.equalsIgnoreCase(idStoreOfMaxAmount())) {
//			String stockItem = get.getStockItems("store.id==" + idStoreOfMaxAmount() + ";product.barcode" + this.barcode);
//			CSVParser parser = new CSVParser();
//			parser.parse(stockItem);
//			
//			StockItem e = new StockItem();
//			e.setProductBarcode(parser.getModel().getRows().get(0).getColumns().get(3).getValue());
//			e.setStockItemMinStock(parser.getModel().getRows().get(0).getColumns().get(4).getValue());
//			e.setStockItemMaxStock(parser.getModel().getRows().get(0).getColumns().get(5).getValue());
//			e.setStockItemIncomingAmount(parser.getModel().getRows().get(0).getColumns().get(6).getValue());
//			int newAmount = Integer.parseInt(parser.getModel().getRows().get(0).getColumns().get(7).getValue()) - Integer.parseInt(this.amountOfSale);
//			e.setStockItemAmount(parser.getModel().getRows().get(0).getColumns().get(7).getValue());
//			e.setStockItemSalesPrice(parser.getModel().getRows().get(0).getColumns().get(8).getValue());
//			String url = "http://anas-pc:8080/de.kit.ipd.cocome.cloud.serviceadapter/Services/Database/SetData";
//			String entity = "StockItem";
//			String header = "StoreId;ProductBarcode;StockItemMinStock;StockItemMaxStock;"
//					+ "StockItemIncomingAmount;StockItemAmount;StockItemSalesPrice \n";
//			String content = idStoreOfMaxAmount() + ";" + e.getProductBarcode() + ";" + e.getStockItemMinStock() + ";" + e.getStockItemMaxStock() + 
//						 ";" +	e.getStockItemIncomingAmount() + ";" + newAmount + ";" + e.getStockItemSalesPrice(); 
//			String type = "update";
//			PostDataToBackEnd p = new PostDataToBackEnd(url, entity, header,
//					content, type);
//			p.post();
//		}
//		
		
		return null;
	}
	
	public boolean isOK() throws Exception{
//		GetXMLFromBackend get = new GetXMLFromBackend();
//		String stockItem = get.getStockItems("store.id==" + idStoreOfMaxAmount() + ";product.barcode" + this.barcode);
//		
//		
		return false;
	}

	

}
