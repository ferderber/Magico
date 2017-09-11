package net.cobaltium.magico.db.tables;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DataColumn {
    boolean primaryKey() default false;

    boolean autoIncrement() default false;

    boolean notNull() default false;

    boolean id() default false;
}
