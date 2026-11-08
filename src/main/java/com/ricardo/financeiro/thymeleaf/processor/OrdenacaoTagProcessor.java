package com.ricardo.financeiro.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class OrdenacaoTagProcessor extends AbstractElementTagProcessor {
	
	private static final String NOME_DA_TAG = "ordenacao";
	private static final int PRECEDENCIA = 1000;

	public OrdenacaoTagProcessor(String dialectPrefix) {		
		super(TemplateMode.HTML, dialectPrefix, NOME_DA_TAG, true, null, false, PRECEDENCIA);		
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
		
		IModelFactory modelFactory = context.getModelFactory();
		
		IAttribute page = tag.getAttribute("page");
		IAttribute field = tag.getAttribute("field");
		IAttribute text = tag.getAttribute("text");
		
		IModel model = modelFactory.createModel();
		model.add(modelFactory.createStandaloneElementTag(
				"th:block",
				"th:replace",
				String.format("~{fragments/Ordenacao :: ordenacao(%s, %s, '%s')}", page.getValue(), field.getValue(), text.getValue())
				));
		
		structureHandler.replaceWith(model, true);		
	}

}
