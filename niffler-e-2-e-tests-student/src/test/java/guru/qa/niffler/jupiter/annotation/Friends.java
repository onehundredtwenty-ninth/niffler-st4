package guru.qa.niffler.jupiter.annotation;

public @interface Friends {

  int count() default 1;

  boolean handle() default true;
}
