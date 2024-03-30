package guru.qa.niffler.config;

public class LocalConfig implements Config {

  static final LocalConfig instance = new LocalConfig();

  private LocalConfig() {
  }

  @Override
  public String authUrl() {
    return "http://auth.niffler.dc:9000";
  }

  @Override
  public String categoryUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String currencyUrl() {
    return "http://127.0.0.1:8091";
  }

  @Override
  public String friendsUrl() {
    return "http://127.0.0.1:8089";
  }

  @Override
  public String registerUrl() {
    return "http://127.0.0.1:9000";
  }

  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String userUrl() {
    return "http://127.0.0.1:8089";
  }

  @Override
  public String gatewayUrl() {
    return "http://gateway.niffler.dc:8090";
  }

  @Override
  public String frontUrl() {
    return "http://frontend.niffler.dc";
  }

  @Override
  public String jdbcHost() {
    return "localhost";
  }

  @Override
  public String currencyGrpcHost() {
    return "localhost";
  }

  @Override
  public String spendGrpcHost() {
    return "localhost";
  }
}
