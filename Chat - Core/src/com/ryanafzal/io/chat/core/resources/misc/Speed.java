package com.ryanafzal.io.chat.core.resources.misc;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target({ METHOD, CONSTRUCTOR })
public @interface Speed {
	String value();
}
