package org.cocome.tradingsystem.util.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.NormalScope;

import org.cocome.tradingsystem.util.scope.context.INamedSessionContext;

/**
 * Session scope for a cash desk session. This scope has to be activated
 * from outside and is active until it gets deactivated or deleted again.
 * 
 * Due to the nature of a real cash desk, the beans in this scope are not 
 * automatically deleted and remain in memory after being created for the 
 * first time. Therefore it is necessary to be careful when using this scope.
 * 
 * @see CashDeskSessionScoped
 * @see INamedSessionContext
 *  
 * @author Tobias Pöppke
 *
 */
@NormalScope
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
public @interface CashDeskSessionScoped {
}
