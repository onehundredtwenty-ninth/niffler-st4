package guru.qa.niffler.api;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import java.util.List;
import lombok.SneakyThrows;

public class FriendsApiClient extends RestClient {

  private final FriendsApi friendsApi;

  public FriendsApiClient() {
    super("http://127.0.0.1:8089");
    friendsApi = retrofit.create(FriendsApi.class);
  }

  @SneakyThrows
  public List<UserJson> friends(String username, Boolean includePending) {
    return friendsApi.friends(username, includePending).execute().body();
  }

  @SneakyThrows
  public List<UserJson> invitations(String username) {
    return friendsApi.invitations(username).execute().body();
  }

  @SneakyThrows
  public List<UserJson> acceptInvitation(String username, UserJson invitation) {
    return friendsApi.acceptInvitation(username, invitation).execute().body();
  }

  @SneakyThrows
  public List<UserJson> declineInvitation(String username, UserJson invitation) {
    return friendsApi.declineInvitation(username, invitation).execute().body();
  }

  @SneakyThrows
  public UserJson addFriend(String username, FriendJson friend) {
    return friendsApi.addFriend(username, friend).execute().body();
  }

  @SneakyThrows
  public void removeFriend(String username, String friendUsername) {
    friendsApi.removeFriend(username, friendUsername).execute();
  }
}
