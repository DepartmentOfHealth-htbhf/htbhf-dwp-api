package uk.gov.dhsc.htbhf.dwp.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import uk.gov.dhsc.htbhf.dwp.converter.RequestHeaderToDWPEligibilityRequestConverter;
import uk.gov.dhsc.htbhf.dwp.model.DWPEligibilityRequest;

import javax.annotation.Nonnull;

@AllArgsConstructor
public class DwpEligibilityRequestResolver implements HandlerMethodArgumentResolver {

    private RequestHeaderToDWPEligibilityRequestConverter converter;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(DWPEligibilityRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nonnull WebDataBinderFactory binderFactory) throws Exception {
        DWPEligibilityRequest request = converter.convert(webRequest);
        validateRequest(parameter, webRequest, binderFactory, request);
        return request;
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    private void validateRequest(MethodParameter parameter, NativeWebRequest webRequest, WebDataBinderFactory binderFactory,
                                 DWPEligibilityRequest request) throws Exception {
        WebDataBinder binder = binderFactory.createBinder(webRequest, request, "request");
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.getErrorCount() > 0) {
            throw new MethodArgumentNotValidException(parameter, bindingResult);
        }
    }
}
