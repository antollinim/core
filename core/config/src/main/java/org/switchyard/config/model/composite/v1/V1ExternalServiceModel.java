/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.switchyard.config.model.composite.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseNamedModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.composite.ExternalServiceModel;
import org.switchyard.config.util.QNames;

/**
 * V1ExternalServiceModel.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class V1ExternalServiceModel extends BaseNamedModel implements ExternalServiceModel {

    private List<BindingModel> _bindings = new ArrayList<BindingModel>();

    public V1ExternalServiceModel() {
        super(new QName(CompositeModel.DEFAULT_NAMESPACE, ExternalServiceModel.SERVICE));
    }

    public V1ExternalServiceModel(Configuration config, Descriptor desc) {
        super(config, desc);
        for (Configuration binding_config : config.getChildrenStartsWith(BindingModel.BINDING)) {
            BindingModel binding = (BindingModel)readModel(binding_config);
            if (binding != null) {
                _bindings.add(binding);
            }
        }
    }

    @Override
    public CompositeModel getComposite() {
        return (CompositeModel)getParentModel();
    }

    @Override
    public ComponentModel getComponent() {
        CompositeModel composite = getComposite();
        if (composite != null) {
            QName promote = getPromote();
            if (promote != null) {
                for (ComponentModel component : composite.getComponents()) {
                    if (promote.equals(component.getQName())) {
                        return component;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public QName getPromote() {
        return QNames.create(getModelAttribute(ExternalServiceModel.PROMOTE));
    }

    @Override
    public ExternalServiceModel setPromote(QName promote) {
        setModelAttribute(ExternalServiceModel.PROMOTE, promote != null ? promote.toString() : null);
        return this;
    }

    @Override
    public synchronized List<BindingModel> getBindings() {
        return Collections.unmodifiableList(_bindings);
    }

    @Override
    public synchronized ExternalServiceModel addBinding(BindingModel binding) {
        addChildModel(binding);
        _bindings.add(binding);
        return this;
    }

}