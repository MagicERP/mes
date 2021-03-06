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

import static com.qcadoo.mes.materialRequirements.internal.constants.InputProductsRequiredForType.START_ORDER;
import static com.qcadoo.mes.materialRequirements.internal.constants.ParameterFieldsMR.INPUT_PRODUCTS_REQUIRED_FOR_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.mes.materialRequirements.internal.MaterialRequirementService;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;

@Service
public class ParameterHooksMR {

    @Autowired
    private MaterialRequirementService materialRequirementService;

    public final boolean checkIfInputProductsRequiredForTypeIsSelected(final DataDefinition parameterDD, final Entity parameter) {
        return materialRequirementService.checkIfInputProductsRequiredForTypeIsSelected(parameterDD, parameter,
                INPUT_PRODUCTS_REQUIRED_FOR_TYPE, "basic.parameter.message.inputProductsRequiredForTypeIsNotSelected");
    }

    public void setInputProductsRequiredForTypeDefaultValue(final DataDefinition parameterDD, final Entity parameter) {
        materialRequirementService.setInputProductsRequiredForTypeDefaultValue(parameter, INPUT_PRODUCTS_REQUIRED_FOR_TYPE,
                START_ORDER.getStringValue());
    }

}
