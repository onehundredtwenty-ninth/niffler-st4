package guru.qa.niffler.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtils {

  public static Date dateFromString(String dateAsString) {
    return Date.from(
        LocalDate.parse(dateAsString, DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH))
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC)
    );
  }
}
