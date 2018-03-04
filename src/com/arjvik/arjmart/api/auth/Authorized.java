package com.arjvik.arjmart.api.auth;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@Documented
@NameBinding
@Retention(RUNTIME)
@Target({TYPE,METHOD})
public @interface Authorized {
	public static String NO_ROLE = "NO_ROLE";
	
	String value() default NO_ROLE;
	
}
