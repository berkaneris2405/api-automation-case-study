package mockapi.models.response.getorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderResponse {
	private ItemResponse item;
	private ShippingResponse shipping;
	private OrderResponse order;
}
