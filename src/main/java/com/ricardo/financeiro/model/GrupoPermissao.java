package com.ricardo.financeiro.model;

import java.util.Objects;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupo_permissao")
public class GrupoPermissao {
	
	@EmbeddedId
	private GrupoPermissaoId id;
	

	public GrupoPermissaoId getId() {
		return id;
	}
	public void setId(GrupoPermissaoId id) {
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
		GrupoPermissao other = (GrupoPermissao) obj;
		return Objects.equals(id, other.id);
	}
	
}
