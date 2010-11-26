package com.qcadoo.mes.newview;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public abstract class AbstractStateTest {

    protected ComponentState createMockComponent(final String name) {
        ComponentState component1 = mock(ComponentState.class,
                withSettings().extraInterfaces(ScopeEntityIdChangeListener.class, FieldEntityIdChangeListener.class));
        given(component1.getName()).willReturn(name);
        return component1;
    }

}
