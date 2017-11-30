/*
 *************************************************************************
 * Copyright 2013 DFG SPP 1593 (http://dfg-spp1593.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************************************************************
 */

package org.cocome.test;

import org.cocome.tradingsystem.inventory.application.plant.PlantTO;
import org.cocome.tradingsystem.inventory.application.plant.recipe.RecipeTO;
import org.cocome.tradingsystem.inventory.application.store.EnterpriseTO;
import org.cocome.tradingsystem.inventory.application.store.StoreWithEnterpriseTO;

import java.util.List;

/**
 * Data structures for bundling all enterprise-related TO objects
 *
 * @author Rudolf Biczok
 */
public class EnterpriseInfo {

    private final EnterpriseTO enterprise;
    private final List<StoreWithEnterpriseTO> stores;
    private final List<PlantTO> plants;
    private final List<RecipeTO> recipes;
    private final List<CustomProductInfo> customProducts;

    /**
     * Minimal constructor
     *
     * @param enterprise     the enterprise TO object
     * @param stores         the store TOs
     * @param plants         the plant TOs
     * @param recipes        the recipe TOs
     * @param customProducts the product data
     */
    public EnterpriseInfo(final EnterpriseTO enterprise,
                          final List<StoreWithEnterpriseTO> stores,
                          final List<PlantTO> plants,
                          final List<RecipeTO> recipes,
                          final List<CustomProductInfo> customProducts) {
        this.enterprise = enterprise;
        this.stores = stores;
        this.plants = plants;
        this.recipes = recipes;
        this.customProducts = customProducts;
    }

    public EnterpriseTO getEnterprise() {
        return enterprise;
    }

    public List<StoreWithEnterpriseTO> getStores() {
        return stores;
    }

    public List<PlantTO> getPlants() {
        return plants;
    }

    public List<RecipeTO> getRecipes() {
        return recipes;
    }

    public List<CustomProductInfo> getCustomProducts() {
        return customProducts;
    }
}
