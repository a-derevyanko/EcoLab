package org.ekolab.server.common;

import com.github.aleksandy.petrovich.Case;
import com.github.aleksandy.petrovich.Gender;
import com.github.aleksandy.petrovich.Petrovich;
import org.ekolab.server.model.UserInfo;
import org.springframework.util.StringUtils;

/**
 * Created by 777Al on 29.05.2017.
 */
public abstract class UserInfoUtils {
    private static final Petrovich PETROVICH = new Petrovich();

    private UserInfoUtils() {
        throw new AssertionError();
    }

    public static String getShortInitials(UserInfo userInfo) {
        Gender gender = StringUtils.hasText(userInfo.getMiddleName()) ? Gender.detect(userInfo.getMiddleName()) : Gender.androgynous;
        return PETROVICH.inflectTo(
                new Petrovich.Names(userInfo.getLastName(), userInfo.getFirstName(), userInfo.getMiddleName(), gender),
                Case.GENITIVE).lastName
                + ' ' + userInfo.getFirstName().substring(0, 1) + ". " +
                ((StringUtils.hasText(userInfo.getMiddleName())) ? userInfo.getMiddleName().substring(0, 1) + ". " : "");
    }
}
