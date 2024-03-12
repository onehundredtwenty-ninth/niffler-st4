package guru.qa.niffler.model.gql;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlUsers extends GqlResponse<GqlUsers> {

  private List<GqlUser> users;
}
