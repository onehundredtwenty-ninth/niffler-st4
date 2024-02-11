package guru.qa.niffler.jupiter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DbUserExtension.class)
public @interface DbUser {

  String username() default "";
  String password() default "";
}
