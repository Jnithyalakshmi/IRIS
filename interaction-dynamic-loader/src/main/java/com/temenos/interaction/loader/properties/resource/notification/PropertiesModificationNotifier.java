package com.temenos.interaction.loader.properties.resource.notification;

/*
 * #%L
 * interaction-springdsl
 * %%
 * Copyright (C) 2012 - 2014 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import com.temenos.interaction.core.loader.PropertiesEvent;
import com.temenos.interaction.core.loader.PropertiesResourceModificationAction;

public class PropertiesModificationNotifier implements ApplicationContextAware {
    private ApplicationContext ctx;
    private Collection<PropertiesResourceModificationAction> rmas;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
    
    public Set<String> getPatterns() {
    	Set<String> resourcePatterns = new HashSet<String>();
    	
		if(rmas == null) {
			rmas = ctx.getBeansOfType(PropertiesResourceModificationAction.class).values();
		}
    	    	
	    for(PropertiesResourceModificationAction rma: rmas) {
	    	resourcePatterns.add(rma.getResourcePattern());
		}
		
    	return resourcePatterns;
    }
    
	public void execute(PropertiesEvent<Resource> event) {
		if(rmas == null) {
			rmas = ctx.getBeansOfType(PropertiesResourceModificationAction.class).values();
		}
		
		Iterator<PropertiesResourceModificationAction> rmaIter = rmas.iterator();
		
		while(rmaIter.hasNext()) {
			rmaIter.next().notify(event);
		}
	}
}
