package individual.me.exception;

import individual.me.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class MldxExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public R authException(AuthException e){
        e.printStackTrace();
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public R bindException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return R.fail("客户端请求参数不正确",errors);
    }

    @ExceptionHandler(BadRequestException.class)
    public R badRequestException(BadRequestException e){
        e.printStackTrace();
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public R throwable(Throwable e){
        e.printStackTrace();
        return R.fail(e.getMessage());
    }
}
