package com.ricardo.financeiro.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class GrupoPermissaoId implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "codigo_grupo")
	private Grupo grupo;
	
	@ManyToOne
	@JoinColumn(name = "codigo_permissao")
	private Permissao permissao;

	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public Permissao getPermissao() {
		return permissao;
	}
	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(grupo, permissao);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoPermissaoId other = (GrupoPermissaoId) obj;
		return Objects.equals(grupo, other.grupo) && Objects.equals(permissao, other.permissao);
	}

}
