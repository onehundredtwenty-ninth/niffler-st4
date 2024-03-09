package guru.qa.niffler.jupiter.annotation;

public @interface Friends {

  int count() default 1;

  boolean pending() default false;

  FriendshipRequestType friendshipRequestType() default FriendshipRequestType.INCOME;

  boolean handle() default true;

  enum FriendshipRequestType {
    INCOME, OUTCOME
  }
}
