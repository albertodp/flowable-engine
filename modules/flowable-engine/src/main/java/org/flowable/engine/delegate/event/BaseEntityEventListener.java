/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.delegate.event;

import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEntityEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;

/**
 * Base event listener that can be used when implementing an {@link FlowableEventListener} to get notified when an entity is created, updated, deleted or if another entity-related event occurs.
 * 
 * Override the <code>onXX(..)</code> methods to respond to entity changes accordingly.
 * 
 * @author Frederik Heremans
 * 
 */
public class BaseEntityEventListener implements FlowableEventListener {

    protected boolean failOnException;
    protected Class<?> entityClass;

    /**
     * Create a new BaseEntityEventListener, notified when an event that targets any type of entity is received. Returning true when {@link #isFailOnException()} is called.
     */
    public BaseEntityEventListener() {
        this(true, null);
    }

    /**
     * Create a new BaseEntityEventListener.
     * 
     * @param failOnException
     *            return value for {@link #isFailOnException()}.
     */
    public BaseEntityEventListener(boolean failOnException) {
        this(failOnException, null);
    }

    public BaseEntityEventListener(boolean failOnException, Class<?> entityClass) {
        this.failOnException = failOnException;
        this.entityClass = entityClass;
    }

    @Override
    public final void onEvent(FlowableEvent event) {
        if (isValidEvent(event)) {
            // Check if this event
            if (event.getType() == FlowableEngineEventType.ENTITY_CREATED) {
                onCreate(event);
            } else if (event.getType() == FlowableEngineEventType.ENTITY_INITIALIZED) {
                onInitialized(event);
            } else if (event.getType() == FlowableEngineEventType.ENTITY_DELETED) {
                onDelete(event);
            } else if (event.getType() == FlowableEngineEventType.ENTITY_UPDATED) {
                onUpdate(event);
            } else {
                // Entity-specific event
                onEntityEvent(event);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return failOnException;
    }

    /**
     * @return true, if the event is an {@link FlowableEntityEvent} and (if needed) the entityClass set in this instance, is assignable from the entity class in the event.
     */
    protected boolean isValidEvent(FlowableEvent event) {
        boolean valid = false;
        if (event instanceof FlowableEntityEvent) {
            if (entityClass == null) {
                valid = true;
            } else {
                valid = entityClass.isAssignableFrom(((FlowableEntityEvent) event).getEntity().getClass());
            }
        }
        return valid;
    }

    /**
     * Called when an entity create event is received.
     */
    protected void onCreate(FlowableEvent event) {
        // Default implementation is a NO-OP
    }

    /**
     * Called when an entity initialized event is received.
     */
    protected void onInitialized(FlowableEvent event) {
        // Default implementation is a NO-OP
    }

    /**
     * Called when an entity delete event is received.
     */
    protected void onDelete(FlowableEvent event) {
        // Default implementation is a NO-OP
    }

    /**
     * Called when an entity update event is received.
     */
    protected void onUpdate(FlowableEvent event) {
        // Default implementation is a NO-OP
    }

    /**
     * Called when an event is received, which is not a create, an update or delete.
     */
    protected void onEntityEvent(FlowableEvent event) {
        // Default implementation is a NO-OP
    }
}
