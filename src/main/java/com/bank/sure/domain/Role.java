package com.bank.sure.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bank.sure.domain.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_role")
@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING) //with this annotation I am telling database the field is enum and the type is String and write the Stginr representation of the enumrole which are ADMIN and CUSTOMEr
	@Column(length=30,nullable=false)
	private UserRole name;

	@Override
	public String toString() {
		return "Role [ name=" + name + "]";
	}


}
