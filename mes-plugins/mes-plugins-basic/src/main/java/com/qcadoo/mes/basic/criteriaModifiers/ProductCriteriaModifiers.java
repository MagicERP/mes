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
package com.qcadoo.mes.basic.criteriaModifiers;

import static com.qcadoo.mes.basic.constants.ProductFields.ENTITY_TYPE;

import org.springframework.stereotype.Service;

import com.qcadoo.mes.basic.constants.ProductFamilyElementType;
import com.qcadoo.model.api.search.SearchCriteriaBuilder;
import com.qcadoo.model.api.search.SearchRestrictions;

@Service
public class ProductCriteriaModifiers {

    public void showProductFamilyOnly(final SearchCriteriaBuilder scb) {
        scb.add(SearchRestrictions.eq(ENTITY_TYPE, ProductFamilyElementType.PRODUCTS_FAMILY.getStringValue()));
    }

    public void showParticularProductOnly(final SearchCriteriaBuilder scb) {
        scb.add(SearchRestrictions.eq(ENTITY_TYPE, ProductFamilyElementType.PARTICULAR_PRODUCT.getStringValue()));
    }

}
