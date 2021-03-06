/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo Framework
 * Version: 1.2.0
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.basicProductionCounting.aop;

import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingOperationRunFields.RUNS;
import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingOperationRunFields.TECHNOLOGY_OPERATION_COMPONENT;
import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields.IS_NON_COMPONENT;
import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields.OPERATION_PRODUCT_IN_COMPONENT;
import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields.OPERATION_PRODUCT_OUT_COMPONENT;
import static com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields.PLANNED_QUANTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.qcadoo.mes.basicProductionCounting.constants.BasicProductionCountingConstants;
import com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields;
import com.qcadoo.mes.orders.constants.OrderFields;
import com.qcadoo.mes.orders.states.constants.OrderStateStringValues;
import com.qcadoo.mes.technologies.ProductQuantitiesServiceImpl;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.search.SearchRestrictions;

@Service
public class ProductQuantitiesServiceImplBPCOverrideUtil {

    @Autowired
    private ProductQuantitiesServiceImpl productQuantitiesServiceImpl;

    @Autowired
    private DataDefinitionService dataDefinitionService;

    public Map<Entity, BigDecimal> getProductComponentWithQuantitiesForOrders(final List<Entity> orders,
            final Map<Entity, BigDecimal> operationRuns, final Set<Entity> nonComponents) {
        Map<Long, Map<Entity, BigDecimal>> productComponentWithQuantitiesForOrders = Maps.newHashMap();

        for (Entity order : orders) {
            BigDecimal plannedQuantity = (BigDecimal) order.getField("plannedQuantity");

            Entity technology = order.getBelongsToField("technology");

            if (technology == null) {
                throw new IllegalStateException("Order doesn't contain technology.");
            }

            if (OrderStateStringValues.ACCEPTED.equals(order.getStringField(OrderFields.STATE))
                    || OrderStateStringValues.IN_PROGRESS.equals(order.getStringField(OrderFields.STATE))
                    || OrderStateStringValues.INTERRUPTED.equals(order.getStringField(OrderFields.STATE))) {
                productComponentWithQuantitiesForOrders.put(order.getId(), getProductComponentWithQuantities(order));

                fillOperationRuns(operationRuns, order);
                fillNonComponents(nonComponents, order);
            } else {
                productComponentWithQuantitiesForOrders.put(order.getId(),
                        productQuantitiesServiceImpl.getProductComponentWithQuantitiesForTechnology(technology, plannedQuantity,
                                operationRuns, nonComponents));
            }
        }

        return productQuantitiesServiceImpl.groupProductComponentWithQuantities(productComponentWithQuantitiesForOrders);
    }

    private Map<Entity, BigDecimal> getProductComponentWithQuantities(final Entity order) {
        Map<Entity, BigDecimal> productComponentWithQuantities = Maps.newHashMap();

        List<Entity> productionCountingQuantities = dataDefinitionService
                .get(BasicProductionCountingConstants.PLUGIN_IDENTIFIER,
                        BasicProductionCountingConstants.MODEL_PRODUCTION_COUNTING_QUANTITY).find()
                .add(SearchRestrictions.belongsTo(ProductionCountingQuantityFields.ORDER, order)).list().getEntities();

        for (Entity productionCountingQuantity : productionCountingQuantities) {
            Entity operationProductInComponent = productionCountingQuantity.getBelongsToField(OPERATION_PRODUCT_IN_COMPONENT);
            Entity operationProductOutComponent = productionCountingQuantity.getBelongsToField(OPERATION_PRODUCT_OUT_COMPONENT);
            BigDecimal plannedQuantity = productionCountingQuantity.getDecimalField(PLANNED_QUANTITY);

            if ((operationProductInComponent != null) || (operationProductOutComponent != null)) {
                if (operationProductInComponent != null) {
                    productComponentWithQuantities.put(operationProductInComponent, plannedQuantity);
                } else if (operationProductOutComponent != null) {
                    productComponentWithQuantities.put(operationProductOutComponent, plannedQuantity);
                }
            }
        }

        return productComponentWithQuantities;
    }

    private void fillOperationRuns(final Map<Entity, BigDecimal> operationRuns, final Entity order) {
        List<Entity> productionCountingOperationRuns = dataDefinitionService
                .get(BasicProductionCountingConstants.PLUGIN_IDENTIFIER,
                        BasicProductionCountingConstants.MODEL_PRODUCTION_COUNTING_OPERATON_RUN).find()
                .add(SearchRestrictions.belongsTo(ProductionCountingQuantityFields.ORDER, order)).list().getEntities();

        for (Entity productionCountingOperationRun : productionCountingOperationRuns) {
            Entity technologyOperationComponent = productionCountingOperationRun
                    .getBelongsToField(TECHNOLOGY_OPERATION_COMPONENT);
            BigDecimal runs = productionCountingOperationRun.getDecimalField(RUNS);

            operationRuns.put(technologyOperationComponent, runs);
        }
    }

    private void fillNonComponents(final Set<Entity> nonComponents, final Entity order) {
        List<Entity> productionCountingQuantities = dataDefinitionService
                .get(BasicProductionCountingConstants.PLUGIN_IDENTIFIER,
                        BasicProductionCountingConstants.MODEL_PRODUCTION_COUNTING_QUANTITY).find()
                .add(SearchRestrictions.belongsTo(ProductionCountingQuantityFields.ORDER, order))
                .add(SearchRestrictions.eq(IS_NON_COMPONENT, true)).list().getEntities();

        for (Entity productionCountingQuantity : productionCountingQuantities) {
            Entity operationProductInComponent = productionCountingQuantity.getBelongsToField(OPERATION_PRODUCT_IN_COMPONENT);
            Entity operationProductOutComponent = productionCountingQuantity.getBelongsToField(OPERATION_PRODUCT_OUT_COMPONENT);

            if ((operationProductInComponent != null) || (operationProductOutComponent != null)) {
                if (operationProductInComponent != null) {
                    nonComponents.add(operationProductInComponent);
                } else if (operationProductOutComponent != null) {
                    nonComponents.add(operationProductOutComponent);
                }
            }
        }
    }

}
