package guru.qa.niffler.api;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendsApi {

  @GET("/friends")
  Call<List<UserJson>> friends(@Query("username") String username, @Query("includePending") Boolean includePending);

  @GET("/invitations")
  Call<List<UserJson>> invitations(@Query("username") String username);

  @POST("/acceptInvitation")
  Call<List<UserJson>> acceptInvitation(@Query("username") String username, @Query("invitation") UserJson invitation);

  @POST("/declineInvitation")
  Call<List<UserJson>> declineInvitation(@Query("username") String username, @Query("invitation") UserJson invitation);

  @POST("/addFriend")
  Call<UserJson> addFriend(@Query("username") String username, @Query("friend") FriendJson friend);

  @DELETE("/removeFriend")
  Call<Void> removeFriend(@Query("username") String username, @Query("friendUsername") String friendUsername);
}
