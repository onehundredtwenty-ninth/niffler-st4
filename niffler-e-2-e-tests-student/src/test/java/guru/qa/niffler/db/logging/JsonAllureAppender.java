package guru.qa.niffler.db.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

public class JsonAllureAppender {

  private final String templateName = "json-template.ftl";
  private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

  public void logJson(String json) {
    if (StringUtils.isNoneEmpty(json)) {
      var attachment = new JsonAttachment("Json attachment", json);
      attachmentProcessor.addAttachment(attachment, new FreemarkerAttachmentRenderer(templateName));
    }
  }

  @SneakyThrows
  public void logJson(Object o) {
    if (o != null) {
      var mapper = new ObjectMapper();
      var objctAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);

      var attachment = new JsonAttachment("Json attachment", objctAsString);
      attachmentProcessor.addAttachment(attachment, new FreemarkerAttachmentRenderer(templateName));
    }
  }
}
