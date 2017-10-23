package org.cocome.tradingsystem.inventory.data.persistence;

public final class ServiceAdapterHeaders {

    static final String SEPARATOR = ";";

    public static final String SET_SEPARATOR = ",";

    public static final String NULL_VALUE = "null";

    static final String PRODUCTORDER_HEADER = "ProductOrderId;StoreId;ProductBarcode;"
            + "OrderDeliveryDate;OrderOrderingDate;OrderAmount";
    static final String STOCKITEM_HEADER = "StoreId;ProductBarcode;StockItemMinStock;"
            + "StockItemMaxStock;StockItemIncomingAmount;StockItemAmount;StockItemSalesPrice";

    static final String ENTERPRISE_CREATE_HEADER = "EnterpriseName";
    static final String ENTERPRISE_UPDATE_HEADER = "EnterpriseId;EnterpriseName";

    static final String PLANT_CREATE_HEADER = "TradingEnterpriseId;PlantName;PlantLocation";
    static final String PLANT_UPDATE_HEADER = "TradingEnterpriseId;PlantId;PlantName;PlantLocation";

    static final String STORE_CREATE_HEADER = "EnterpriseName;StoreName;StoreLocation";
    static final String STORE_UPDATE_HEADER = "EnterpriseName;StoreId;StoreName;StoreLocation";

    static final String PRODUCT_HEADER = "ProductBarcode;ProductName;ProductPurchasePrice";

    static final String PRODUCTSUPPLIER_CREATE_HEADER = "ProductSupplierName";
    static final String PRODUCTSUPPLIER_UPDATE_HEADER = "ProductSupplierId;ProductSupplierName";

    static final String USER_CREATE_HEADER = "UserName;CredentialType;CredentialString;Role";
    static final String USER_UPDATE_HEADER = "UserId;UserName;CredentialType;CredentialString;Role";

    static final String CUSTOMER_CREATE_HEADER = "FirstName;LastName;MailAddress;"
            + "UserName";
    static final String CUSTOMER_UPDATE_HEADER = "CustomerId;FirstName;LastName;MailAddress;"
            + "UserName";

    static final String CUSTOMER_CREATE_HEADER_WITH_STORE = "FirstName;LastName;MailAddress;"
            + "PreferredStoreEnterpriseName;PreferredStoreId;PreferredStoreName;PreferredStoreLocation;"
            + "UserName";
    static final String CUSTOMER_UPDATE_HEADER_WITH_STORE = "CustomerId;FirstName;LastName;MailAddress;"
            + "PreferredStoreEnterpriseName;PreferredStoreId;PreferredStoreName;PreferredStoreLocation;"
            + "UserName";

    static final String PRODUCTIONUNITCLASS_UPDATE_HEADER = "PlantId;ProductionUnitClassId;"
            + "ProductionUnitClassName";
    static final String PRODUCTIONUNITCLASS_CREATE_HEADER = "PlantId;ProductionUnitClassName";

    static final String CUSTOMPRODUCT_CREATE_HEADER = "CustomProductBarcode;CustomProductLocation;"
            + "CustomProductPurchasePrice";
    static final String CUSTOMPRODUCT_UPDATE_HEADER = "CustomProductId;CustomProductBarcode;CustomProductLocation;"
            + "CustomProductPurchasePrice";

    static final String PRODUCTIONUNITOPERATION_CREATE_HEADER
            = "ProductionUnitOperationName;ProductionUnitOperationOID;ProductionUnitOperationExpectedTime;"
            + "ProductionUnitClassId";
    static final String PRODUCTIONUNITOPERATION_UPDATE_HEADER
            = "ProductionUnitOperationId;ProductionUnitOperationName;ProductionUnitOperationOID;"
            + "ProductionUnitOperationExpectedTime;ProductionUnitClassId";

    static final String ENTRYPOINT_CREATE_HEADER = "EntryPointName";
    static final String ENTRYPOINT_UPDATE_HEADER = "EntryPointId;EntryPointName";

    static final String CONDITIONALEXPRESSION_CREATE_HEADER
            = "PlantOperationParameterId;ConditionalExpressionParameterValue;"
            + "ConditionalExpressionOnTrueExpressions;ConditionalExpressionOnFalseExpressions";
    static final String CONDITIONALEXPRESSION_UPDATE_HEADER
            = "PlantOperationParameterId;ConditionalExpressionId;ConditionalExpressionParameterValue;"
            + "ConditionalExpressionOnTrueExpressions;ConditionalExpressionOnFalseExpressions";

    static final String BOOLEAN_CUSTOM_PRODUCT_PARAM_CREATE_HEADER
            = "CustomProductId;BooleanCustomProductParameterName;BooleanCustomProductParameterCategory\n";
    static final String BOOLEAN_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER
            = "CustomProductId;BooleanCustomProductParameterId;BooleanCustomProductParameterName;"
            + "BooleanCustomProductParameterCategory";

    static final String NORMINAL_CUSTOM_PRODUCT_PARAM_CREATE_HEADER
            = "CustomProductId;NorminalCustomProductParameterName;NorminalCustomProductParameterCategory;"
            + "NorminalCustomProductParameterOptions";
    static final String NORMINAL_CUSTOM_PRODUCT_PARAM_UPDATE_HEADER
            = " CustomProductId;NorminalCustomProductParameterId;NorminalCustomProductParameterName;"
            + "NorminalCustomProductParameterCategory;NorminalCustomProductParameterOptions";

    static final String PLANTOPERATION_CREATE_HEADER = "PlantId;ExpressionId;PlantOperationName;"
            + "EntryPointInputId;EntryPointOutputId";
    static final String PLANTOPERATION_UPDATE_HEADER = "PlantOperationId;PlantId;ExpressionId;PlantOperationName;"
            + "EntryPointInputId;EntryPointOutputId";

    static final String BOOLEAN_PLANT_OPERATION_PARAM_CREATE_HEADER
            = "PlantOperationId;BooleanPlantOperationParameterName;BooleanPlantOperationParameterCategory";
    static final String BOOLEAN_PLANT_OPERATION_PARAM_UPDATE_HEADER
            = "PlantOperationId;BooleanPlantOperationParameterId;BooleanPlantOperationParameterName;"
            + "BooleanPlantOperationParameterCategory";

    static final String NORMINAL_PLANT_OPERATION_PARAM_CREATE_HEADER
            = "PlantOperationId;NorminalPlantOperationParameterId;NorminalPlantOperationParameterName;"
            + "NorminalPlantOperationParameterCategory;NorminalPlantOperationParameterOptions";
    static final String NORMINAL_PLANT_OPERATION_PARAM_UPDATE_HEADER
            = "PlantOperationId;NorminalPlantOperationParameterName;NorminalPlantOperationParameterCategory;"
            + "NorminalPlantOperationParameterOptions";

    static final String ENTRYPOINTINTERACTION_CREATE_HEADER
            = "EntryPointInteractionToId;EntryPointInteractionFromId";
    static final String ENTRYPOINTINTERACTION_UPDATE_HEADER
            = "EntryPointInteractionId;EntryPointInteractionToId;EntryPointInteractionFromId";

    static final String PARAMETERINTERACTION_CREATE_HEADER
            = "ParameterInteractionToId;ParameterInteractionFromId";
    static final String PARAMETERINTERACTION_UPDATE_HEADER
            = "ParameterInteractionId;ParameterInteractionToId;ParameterInteractionFromId";

    static final String RECIPE_CREATE_HEADER
            = "CustomProductId;PlantOperationId;EntryPointInteractionId;ParameterInteractionId";
    static final String RECIPE_UPDATE_HEADER
            = "RecipeId;CustomProductId;PlantOperationId;EntryPointInteractionId;ParameterInteractionId";

    static final String PLANTOPERATIONORDER_CREATE_HEADER
            = "PlantOperationOrderDeliveryDate;PlantOperationOrderOrderingDate;TradingEnterpriseId";
    static final String PLANTOPERATIONORDER_UPDATE_HEADER
            = "PlantOperationOrderId;PlantOperationOrderDeliveryDate;PlantOperationOrderOrderingDate;"
            + "TradingEnterpriseId";

    static final String PLANTOPERATIONENTRY_CREATE_HEADER
            = "PlantOperationOrderEntryAmount;PlantOperationId;PlantOperationOrderId";
    static final String PLANTOPERATIONENTRY_UPDATE_HEADER
            = "PlantOperationOrderEntryId;PlantOperationOrderEntryAmount;PlantOperationId;PlantOperationOrderId";

    static final String PARAMETERVALUECONTENT_CREATE_HEADER
            = "PlantOperationParameterValueValue;PlantOperationParameterId;PlantOperationOrderEntryId";
    static final String PARAMETERVALUECONTENT_UPDATE_HEADER
            = "PlantOperationParameterValueId;PlantOperationParameterValueValue;PlantOperationParameterId;"
            + "PlantOperationOrderEntryId";
}
