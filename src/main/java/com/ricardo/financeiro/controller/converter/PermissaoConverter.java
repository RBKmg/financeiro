//package com.ricardo.financeiro.controller.converter;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.util.StringUtils;
//
//import com.ricardo.salvavidas.model.Permissao;
//
//public class PermissaoConverter implements Converter<String, Permissao> {
//
//	@Override
//	public Permissao convert(String codigo) {
//		if(!StringUtils.isEmpty(codigo)) {
//			Permissao permissao = new Permissao();
//			permissao.setCodigo(Long.valueOf(codigo));
//			return permissao;
//		}
//		return null;
//	}
//	
//}
