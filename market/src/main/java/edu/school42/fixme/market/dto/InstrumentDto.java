package edu.school42.fixme.market.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InstrumentDto {
	private String instrument;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private int quantity;
}
