package guru.qa.niffler.db;

import com.p6spy.engine.spy.P6DataSource;
import guru.qa.niffler.config.Config;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public enum DataSourceProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<JdbcUrl, DataSource> store = new ConcurrentHashMap<>();

  public DataSource dataSource(JdbcUrl jdbcUrl) {
    return store.computeIfAbsent(jdbcUrl, k -> {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      ds.setURL(k.getUrl());
      ds.setUser(cfg.jdbcUser());
      ds.setPassword(cfg.jdbcPassword());
      return new P6DataSource(ds);
    });
  }
}
