package com.algaworks.brewer.repository.helper.cerveja;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.filter.CervejaFilter;

public class CervejasImpl implements CervejasQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cerveja> filtrar(CervejaFilter filtro) {
		
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> query = cb.createQuery(Cerveja.class);
		Root<Cerveja> root = query.from(Cerveja.class);
		
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getSku())) {
				query.select(root).where(cb.equal(root.get("sku"), filtro.getSku()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				query.select(root).where(cb.like(root.get("nome"), "%" + filtro.getNome() + "%"));
			}
			
			if (isEstiloPresente(filtro)) {
				query.select(root).where(cb.equal(root.get("estilo"), filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {
				query.select(root).where(cb.equal(root.get("sabor"), filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {
				query.select(root).where(cb.equal(root.get("origem"), filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {
				query.select(root).where(cb.ge(root.get("valor"), filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				query.select(root).where(cb.le(root.get("valor"), filtro.getValorAte()));
			}
		}
		
		return manager.createQuery(query).getResultList();
	}
	
	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}

}