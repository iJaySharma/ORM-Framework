package com.thinking.machines.orm.annotation;
import java.lang.annotation.*;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrement {
    public String name();
}