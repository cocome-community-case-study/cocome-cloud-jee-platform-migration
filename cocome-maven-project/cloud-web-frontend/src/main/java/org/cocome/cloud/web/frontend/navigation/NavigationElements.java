package org.cocome.cloud.web.frontend.navigation;

public enum NavigationElements {
    LOGIN("/login", null),
    ENTERPRISE_MAIN("/enterprise/main", null),
    SHOW_STORES("/enterprise/show_stores", null),
    SHOW_PLANTS("/enterprise/show_plants", null),
    STORE_MAIN("/store/main", null),
    PLANT_MAIN("/plant/main", null),
    PLANT_PU("/plant/show_pu", "plant manager"),
    PLANT_PUC("/plant/show_puc", "plant manager"),
    PLANT_OPERATION("/plant/show_operation", "plant manager"),
    START_SALE("/store/start_sale", "cashier"),
    ORDER_PRODUCTS("/store/order_products", "stock manager"),
    SHOW_STOCK("/store/show_stock", "stock manager"),
    STOCK_REPORT("/store/show_reports", "store manager"),
    RECEIVE_PRODUCTS("/store/receive_products", "stock manager"),
    SHOW_ENTERPRISES("/enterprise/show_enterprises", "enterprise manager"),
    SHOW_PRODUCTS("/enterprise/show_products", "enterprise manager");

    private String navOutcome;
    private String permission;

    NavigationElements(String navOutcome, String permission) {
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
