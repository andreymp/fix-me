package edu.school42.fixme.market.model;

import edu.school42.fixme.common.model.Source;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "fix_me", name = "fix_messages")
public class FixMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fix_messages_id_generator")
	@SequenceGenerator(name = "fix_messages_id_generator", sequenceName = "fix_me.fix_messages_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "body", nullable = false)
	private String body;

	@Enumerated(EnumType.STRING)
	@Column(name = "source", nullable = false)
	private Source source;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;
}
