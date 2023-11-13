package edu.school42.fixme.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "fix_me", name = "fix_messages")
public class FixMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fix_messages_id_generator")
	@SequenceGenerator(name = "fix_messages_id_generator", sequenceName = "fix_messages_id_seq", allocationSize = 1)
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
