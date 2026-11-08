package com.ricardo.financeiro.validation.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.ricardo.financeiro.validation.AtributoConfirmacao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtributoConfirmacaoValidator implements ConstraintValidator<AtributoConfirmacao, Object> {

    private String atributo;
    private String atributoConfirmacao;

    @Override
    public void initialize(AtributoConfirmacao constraintAnnotation) {
        this.atributo = constraintAnnotation.atributo();
        this.atributoConfirmacao = constraintAnnotation.atributoConfirmacao();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {

        // Se o objeto for nulo, deixa a responsabilidade
        // de validar @NotNull para outra anotação.
        if (object == null) {
            return true;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(object);

        Object valorAtributo = beanWrapper.getPropertyValue(this.atributo);
        Object valorAtributoConfirmacao = beanWrapper.getPropertyValue(this.atributoConfirmacao);

        boolean valido = ambosSaoNulos(valorAtributo, valorAtributoConfirmacao) || ambosSaoIguais(valorAtributo, valorAtributoConfirmacao);

        if (!valido) {
            context.disableDefaultConstraintViolation();
            String mensagem = context.getDefaultConstraintMessageTemplate();

            context.buildConstraintViolationWithTemplate(mensagem)
                    .addPropertyNode(this.atributoConfirmacao)
                    .addConstraintViolation();
        }
        return valido;
    }

    private boolean ambosSaoNulos(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo == null && valorAtributoConfirmacao == null;
    }

    private boolean ambosSaoIguais(Object valorAtributo, Object valorAtributoConfirmacao) {
        return valorAtributo != null && valorAtributo.equals(valorAtributoConfirmacao);
    }
}
