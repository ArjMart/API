package com.arjvik.arjmart.api.auth;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.glassfish.hk2.api.AnnotationLiteral;

@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR })
@Qualifier
public @interface UserID {
	
	@SuppressWarnings("serial")
	public static class Instance extends AnnotationLiteral<UserID> implements UserID {
		
	}
	
}
