package org.cocome.cloud.web.frontend.navigation;

public enum NavigationElements {
	LOGOUT("logout", null),
	BACK("dashboard", null),
	START_SALE("start_sale", "cashier"),
	ORDER_PRODUCTS("order_products", "store manager"),
	SHOW_REPORTS("show_reports", "store manager"),
	CHANGE_PRICE("change_price", "store manager"),
	RECEIVE_PRODUCTS("receive_products", "stock manager"),
	SHOW_ENTERPRISES("show_enterprises", "enterprise manager"),
	CREATE_ENTERPRISE("create_enterprise", "enterprise manager"),
	CREATE_PRODUCT("create_product", "enterprise manager"),
	SHOW_PRODUCTS("show_products", "enterprise manager");
	
	private String navOutcome;
	private String permission;
	
	private NavigationElements(String navOutcome, String permission) {
		this.navOutcome = navOutcome;
		this.permission = permission;
	}
	
	public String getNavigationOutcome() {
		return this.navOutcome;
	}
	
	public String getNeededPermission() {
		return this.permission;
	}
}
