package com.ricardo.financeiro.model;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario_grupo")
public class UsuarioGrupo {
	
	@EmbeddedId
	private UsuarioGrupoId id;

	
	public UsuarioGrupoId getId() {
		return id;
	}
	public void setId(UsuarioGrupoId id) {
		this.id = id;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioGrupo other = (UsuarioGrupo) obj;
		return Objects.equals(id, other.id);
	}	

}
