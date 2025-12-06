package mockapi.models.request.createorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

	private ProductDetails productDetails;

	private Integer userId;

	private Integer addressId;

}
