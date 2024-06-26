package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import io.qameta.allure.junit5.AllureJunit5;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({ContextHolderExtension.class, AllureJunit5.class})
public @interface GrpcTest {
}
