package com.revature.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.revature.orm.enums.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Relationship {

	RelationshipType type();
	
	String joinTable() default "";
	
	String ownerJoinColumn() default "";
	
	String ownedJoinColumn() default "";
}
