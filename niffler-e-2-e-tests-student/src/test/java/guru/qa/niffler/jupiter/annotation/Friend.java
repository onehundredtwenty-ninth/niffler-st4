package guru.qa.niffler.jupiter.annotation;

public @interface Friend {

  boolean pending() default false;

  FriendshipRequestType friendshipRequestType() default FriendshipRequestType.INCOME;

  enum FriendshipRequestType {
    INCOME, OUTCOME
  }
}
