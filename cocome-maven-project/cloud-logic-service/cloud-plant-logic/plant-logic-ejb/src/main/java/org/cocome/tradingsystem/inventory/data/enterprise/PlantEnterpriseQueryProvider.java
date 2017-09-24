package org.cocome.tradingsystem.inventory.data.enterprise;

import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(IEnterpriseQuery.class)
public class PlantEnterpriseQueryProvider extends ProxyEnterpriseQueryProvider {
}
