package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);
  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    var query = "SELECT * FROM \"user\" WHERE id = ?";

    try (var con = authDs.getConnection();
        var ps = con.prepareStatement(query)) {
      ps.setObject(1, id);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          var userAuthEntity = new UserAuthEntity();
          userAuthEntity.setId(id);
          userAuthEntity.setUsername(rs.getString("username"));
          userAuthEntity.setPassword(rs.getString("password"));
          userAuthEntity.setEnabled(rs.getBoolean("enabled"));
          userAuthEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          userAuthEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          userAuthEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

          try (var psForAuthority = con.prepareStatement("SELECT * FROM \"authority\" WHERE user_id = ?")) {
            psForAuthority.setObject(1, id);
            try (var rsForAuthority = psForAuthority.executeQuery()) {
              while (rsForAuthority.next()) {
                var authority = new AuthorityEntity();
                authority.setId((UUID) rsForAuthority.getObject("id"));
                authority.setAuthority(Authority.valueOf(rsForAuthority.getString("authority")));
                userAuthEntity.getAuthorities().add(authority);
              }
            }
          }
          return Optional.of(userAuthEntity);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    var query = "SELECT * FROM \"user\" WHERE id = ?";

    try (var con = udDs.getConnection();
        var ps = con.prepareStatement(query)) {
      ps.setObject(1, id);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          var user = new UserEntity();
          user.setId(id);
          user.setUsername(rs.getString("username"));
          user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          user.setFirstname(rs.getString("firstname"));
          user.setSurname(rs.getString("surname"));
          user.setPhoto(rs.getBytes("photo"));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.empty();
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
          PreparedStatement authorityPs = conn.prepareStatement(
              "INSERT INTO \"authority\" " +
                  "(user_id, authority) " +
                  "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getCurrency().name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserAuthEntity updateInAuth(UserAuthEntity user) {
    var query = "UPDATE \"user\" SET username = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, "
        + "credentials_non_expired = ? WHERE id = ?";
    var queryForDeleteAuthority = "DELETE FROM \"authority\" WHERE user_id = ?";
    var queryForInsertAuthority = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";

    try (Connection con = authDs.getConnection()) {
      con.setAutoCommit(false);

      try (var ps = con.prepareStatement(query);
          var psForDelete = con.prepareStatement(queryForDeleteAuthority);
          var psForInsert = con.prepareStatement(queryForInsertAuthority)) {
        ps.setString(1, user.getUsername());
        ps.setBoolean(2, user.getEnabled());
        ps.setBoolean(3, user.getAccountNonExpired());
        ps.setBoolean(4, user.getAccountNonLocked());
        ps.setBoolean(5, user.getCredentialsNonExpired());
        ps.setObject(6, user.getId());
        ps.executeUpdate();

        psForDelete.setObject(1, user.getId());
        psForDelete.executeUpdate();

        for (var authority : user.getAuthorities()) {
          psForInsert.setObject(1, user.getId());
          psForInsert.setString(2, authority.getAuthority().name());
          psForInsert.addBatch();
          psForInsert.clearParameters();
        }
        psForInsert.executeBatch();

        return user;
      } catch (SQLException e) {
        con.rollback();
        throw new RuntimeException(e);
      } finally {
        con.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity updateInUserdata(UserEntity user) {
    var query = "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ? WHERE id = ?";
    try (Connection con = udDs.getConnection();
        var ps = con.prepareStatement(query)) {
      ps.setString(1, user.getUsername());
      ps.setObject(2, user.getCurrency());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setObject(6, user.getId());
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInAuthById(UUID id) {
    var queryForDeleteUser = "DELETE FROM \"user\" WHERE id = ?";
    var queryForDeleteAuthority = "DELETE FROM \"authority\" WHERE user_id = ?";

    try (var con = authDs.getConnection()) {
      con.setAutoCommit(false);

      try (var psForDeleteUser = con.prepareStatement(queryForDeleteUser);
          var psForDeleteAuthority = con.prepareStatement(queryForDeleteAuthority)) {

        psForDeleteAuthority.setObject(1, id);
        psForDeleteAuthority.executeUpdate();

        psForDeleteUser.setObject(1, id);
        psForDeleteUser.executeUpdate();

        con.commit();
      } catch (SQLException e) {
        con.rollback();
        throw new RuntimeException(e);
      } finally {
        con.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    var query = "DELETE FROM \"user\" WHERE id = ?";

    try (var con = udDs.getConnection();
        var ps = con.prepareStatement(query)) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
