package com.quincy.auth.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@DynamicInsert
@DynamicUpdate
@EntityListeners({AuditingEntityListener.class})
@Entity(name = "b_country")
public class Country implements Serializable {
	private static final long serialVersionUID = -7160794018694023343L;
	@Id
	@Column(name="id")
	private Long id;
	@Column(name="en_name")
	private String enName;
	@Column(name="cn_name")
	private String cnName;
	@Column(name="tel_prefix")
	private String telPrefix;
	@Column(name="code")
	private String code;
	@Column(name="code2")
	private String code2;
	@Column(name="currency")
	private String currency;
	@Column(name="locale")
	private String locale;
}
