package guru.qa.niffler.config;

public class DockerConfig implements Config {

  static final DockerConfig instance = new DockerConfig();

  private DockerConfig() {
  }

  @Override
  public String authUrl() {
    return "http://auth.niffler.dc:9000";
  }

  @Override
  public String categoryUrl() {
    return "http://spend.niffler.dc:8093";
  }

  @Override
  public String currencyUrl() {
    return "http://currency.niffler.dc:8091";
  }

  @Override
  public String friendsUrl() {
    return "http://userdata.niffler.dc:8089";
  }

  @Override
  public String registerUrl() {
    return "http://auth.niffler.dc:9000";
  }

  @Override
  public String spendUrl() {
    return "http://spend.niffler.dc:8093";
  }

  @Override
  public String userUrl() {
    return "http://userdata.niffler.dc:8089";
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
    return "niffler-all-db";
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
