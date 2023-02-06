package mscs.hms.controller;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;
import jakarta.annotation.Nullable;
import mscs.hms.model.Role;
import mscs.hms.service.IUserService;
import mscs.hms.service.impl.UserServiceImpl;

public class RolesEditor extends PropertyEditorSupport{
    IUserService userService = new UserServiceImpl();

    boolean allowEmpty;
    public RolesEditor(IUserService userService, boolean allowEmpty) {
        this.userService = userService;
        this.allowEmpty = allowEmpty;
	}

    @Override
	public void setAsText(@Nullable String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else {
			try {
                List<Role> list = new ArrayList<>();
                for(String roleIdString : StringUtils.tokenizeToStringArray(text, ",")){
                    list.add(userService.getRoleById(Integer.parseInt(roleIdString)));
                }
				setValue(list);
			}
			catch (Exception ex) {
				throw new IllegalArgumentException("Could not get Role %s: " + ex.getMessage(), ex);
			}
		}
	}	
}