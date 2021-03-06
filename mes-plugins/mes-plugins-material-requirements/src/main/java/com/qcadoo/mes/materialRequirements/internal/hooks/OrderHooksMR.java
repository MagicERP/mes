/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
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
package com.qcadoo.mes.materialRequirements.internal.hooks;

import static com.qcadoo.mes.materialRequirements.internal.constants.OrderFieldsMR.INPUT_PRODUCTS_REQUIRED_FOR_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.mes.basic.ParameterService;
import com.qcadoo.mes.materialRequirements.internal.MaterialRequirementService;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;

@Service
public class OrderHooksMR {

    @Autowired
    private MaterialRequirementService materialRequirementService;

    @Autowired
    private ParameterService parameterService;

    public final boolean checkIfInputProductsRequiredForTypeIsSelected(final DataDefinition orderDD, final Entity order) {
        return materialRequirementService.checkIfInputProductsRequiredForTypeIsSelected(orderDD, order,
                INPUT_PRODUCTS_REQUIRED_FOR_TYPE, "orders.order.message.inputProductsRequiredForTypeIsNotSelected");
    }

    public void setInputProductsRequiredForTypeDefaultValue(final DataDefinition orderDD, final Entity order) {
        materialRequirementService.setInputProductsRequiredForTypeDefaultValue(order, INPUT_PRODUCTS_REQUIRED_FOR_TYPE,
                getInputProductsRequiredForType());
    }

    public void setInputProductsRequiredForTypeDefaultValueOnCopy(final DataDefinition orderDD, final Entity order) {
        materialRequirementService.setInputProductsRequiredForTypeDefaultValue(order, INPUT_PRODUCTS_REQUIRED_FOR_TYPE,
                getInputProductsRequiredForType());
    }

    private String getInputProductsRequiredForType() {
        return parameterService.getParameter().getStringField(INPUT_PRODUCTS_REQUIRED_FOR_TYPE);
    }

}
