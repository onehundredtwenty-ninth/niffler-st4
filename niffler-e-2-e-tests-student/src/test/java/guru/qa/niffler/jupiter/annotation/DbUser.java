package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.DbUserExtension;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DbUserExtension.class)
public @interface DbUser {

  String username() default "";

  String password() default "";

  boolean handle() default true;
}
