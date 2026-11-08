package com.ricardo.financeiro.repository.helper.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.dto.UsuarioDTO;
import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.model.Usuario;
import com.ricardo.financeiro.model.UsuarioGrupo;
import com.ricardo.financeiro.repository.filter.UsuariosFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class UsuarioRepositoryImpl implements UsuarioQueries {


	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Optional<Usuario> porEmailEAtivo(String email) {		
		return entityManager
				.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email)
				.getResultList()
				.stream()
				.findFirst();
	}
	
	@Override
	public List<String> permissoes(Usuario usuario) {		
		return entityManager
				.createQuery("select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> filtrar(UsuariosFilter filtro, Pageable pageable) {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		
		Root<Usuario> root = criteriaQuery.from(Usuario.class);		
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, criteriaQuery, root);
		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));		
		
		paginacaoUtil.adicionarOrdenacao(pageable, criteriaBuilder, criteriaQuery, root);
		
		TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
		
		paginacaoUtil.preparar(typedQuery, pageable);
		
		List<Usuario> filtrados = typedQuery.getResultList();		
		filtrados.forEach(u -> Hibernate.initialize(u.getGrupos()));
		
		return new PageImpl<>(filtrados, pageable, total(filtro));
	}
	
	private Long total(UsuariosFilter filtro) {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<Usuario> root = criteriaQuery.from(Usuario.class);		
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, criteriaQuery, root);
		
		criteriaQuery.select(criteriaBuilder.count(root));		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		return entityManager
				.createQuery(criteriaQuery)
				.getSingleResult();
	}
	
	private List<Predicate> adicionarFiltro(UsuariosFilter filtro,	CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Usuario> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(filtro != null) {			
			if(filtro.getNome() != null	&& !filtro.getNome().isBlank()) {				
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
			}			
			if(filtro.getEmail() != null && !filtro.getEmail().isBlank()) {				
				predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), filtro.getEmail().toLowerCase() + "%"));
			}
			//criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
			if(filtro.getGrupos() != null && !filtro.getGrupos().isEmpty()) {		
				for (Long codigoGrupo : filtro.getGrupos()
						.stream()
						.mapToLong(Grupo::getCodigo)
						.toArray()) {
					
					Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);					
					Root<UsuarioGrupo> usuarioGrupo = subquery.from(UsuarioGrupo.class);
					
					subquery.select(criteriaBuilder.literal(1));
					
					subquery.where(
							criteriaBuilder.equal(usuarioGrupo.get("id").get("usuario").get("codigo"), root.get("codigo")),							
							criteriaBuilder.equal(usuarioGrupo.get("id").get("grupo").get("codigo"), codigoGrupo)
					);
					
					predicates.add(criteriaBuilder.exists(subquery));
				}
			}
		}		
		return predicates;
	}
	
	@Override
	public List<UsuarioDTO> carregarPessoaUsuario() {
		String jpql = "select new com.ricardo.financeiro.dto.UsuarioDTO(codigo, nome) from Usuario";		
		List<UsuarioDTO> usuarios = entityManager.createQuery(jpql,	UsuarioDTO.class).getResultList();		
		return usuarios;
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario buscarUsuarioComGrupos(Long codigo) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		
		Root<Usuario> root = criteriaQuery.from(Usuario.class);		
		root.fetch("grupos", jakarta.persistence.criteria.JoinType.LEFT);
		
		criteriaQuery.select(root);		
		criteriaQuery.where(criteriaBuilder.equal(root.get("codigo"), codigo));		
		criteriaQuery.distinct(true);
		
		return entityManager
				.createQuery(criteriaQuery)
				.getResultStream()
				.findFirst()
				.orElse(null);
	}

}

/*
select u.email usuario
, group_concat(substring(p.nome, 6) order by p.nome separator ', ') permissao
from usuario u
, usuario_grupo ug
, grupo g
, grupo_permissao gp
, permissao p
where ug.codigo_usuario = u.codigo
and ug.codigo_grupo = g.codigo
and g.codigo = gp.codigo_grupo
and gp.codigo_permissao = p.codigo
group by usuario
*/

/*
SELECT * FROM usuario u
LEFT OUTER JOIN usuario_grupo ug ON u.codigo = ug.codigo_usuario
LEFT OUTER JOIN grupo g ON ug.codigo_grupo = g.codigo
WHERE (
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 1)
AND
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 2))

SELECT * FROM usuario u
INNER JOIN usuario_grupo ug ON u.codigo = ug.codigo_usuario
INNER JOIN grupo g ON ug.codigo_grupo = g.codigo
WHERE (
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 1)
AND
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 2)
AND
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 3)
AND
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 4)
AND
u.codigo IN (SELECT codigo_usuario FROM usuario_grupo WHERE codigo_grupo = 5));

/*

SELECT * FROM grupo g
INNER JOIN grupo_permissao gp ON g.codigo = gp.codigo_permissao
INNER JOIN permissao p ON gp.codigo_permissao = p.codigo
WHERE (
g.codigo IN (SELECT codigo_permissao FROM grupo_permissao WHERE codigo_permissao = 1));

*/

