package com.visualpathit.account.validator;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

        if (user.getUsername() != null) {
            if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
                errors.rejectValue("username", "Size.userForm.username");
            }

            if (userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "Duplicate.userForm.username");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        if (user.getPassword() != null) {
            if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
                errors.rejectValue("password", "Size.userForm.password");
            }
        }

        if (user.getPassword() != null
                && user.getPasswordConfirm() != null
                && !user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}