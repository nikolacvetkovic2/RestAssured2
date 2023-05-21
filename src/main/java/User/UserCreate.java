package User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreate {


  private String male;
    private String email;
    @JsonProperty("firstName")
    private String first_Name;
    private  String lastName;
    @JsonProperty("location")
     UserLocation userLocation;


}


