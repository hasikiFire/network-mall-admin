package com.hasikiFire.networkmall.core.common.constant;

import lombok.Getter;

/**
 * 数据库 常量
 *
 * @author hasikiFire
 * @date 2024/6/4
 */
public class DatabaseConsts {

    /**
     * 用户信息表
     */
    public static class UserInfoTable {

        private UserInfoTable() {
            throw new IllegalStateException(SystemConfigConsts.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_USERID = "user_id";

    }

    public static class RolesTable {

        private RolesTable() {
            throw new IllegalStateException(SystemConfigConsts.CONST_INSTANCE_EXCEPTION_MSG);
        }

        public static final String COLUMN_EMAIL = "email";

        public enum RoleEnum {

            /**
             * 管理员
             */
            ADMIN("admin"),

            /**
             * 普通用户
             */
            USER("user");

            RoleEnum(final String roleName) {
                this.roleName = roleName;
            }

            private final String roleName;

            public String getRoleName() {
                return roleName;
            }

        }
    }

    /**
     * 通用列枚举类
     */
    @Getter
    public enum CommonColumnEnum {

        ID("id"),
        SORT("sort"),
        CREATE_TIME("create_time"),
        UPDATE_TIME("update_time");

        private final String name;

        CommonColumnEnum(final String name) {
            this.name = name;
        }

    }

    /**
     * SQL语句枚举类
     */
    @Getter
    public enum SqlEnum {

        LIMIT_1("limit 1"),
        LIMIT_2("limit 2"),
        LIMIT_5("limit 5"),
        LIMIT_30("limit 30"),
        LIMIT_500("limit 500");

        private final String sql;

        SqlEnum(final String sql) {
            this.sql = sql;
        }

    }

}
