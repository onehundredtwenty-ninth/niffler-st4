package guru.qa.niffler.jupiter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlRequestFile;
import guru.qa.niffler.jupiter.extension.GqlRequestResolver;
import guru.qa.niffler.model.gql.GqlRequest;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class GqlRequestConverter implements ArgumentConverter {

  private static final ObjectMapper om = new ObjectMapper();
  private final ClassLoader cl = GqlRequestResolver.class.getClassLoader();

  @Override
  public GqlRequest convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
    if (parameterContext.isAnnotated(GqlRequestFile.class)
        && parameterContext.findAnnotation(GqlRequestFile.class).orElseThrow().value().isBlank()
        && o instanceof String fileName) {
      try (InputStream is = cl.getResourceAsStream(fileName)) {
        return om.readValue(is.readAllBytes(), GqlRequest.class);
      } catch (IOException e) {
        throw new ArgumentConversionException(e.getMessage());
      }
    }
    throw new ArgumentConversionException("Cant`t convert to GqlRequest");
  }
}
