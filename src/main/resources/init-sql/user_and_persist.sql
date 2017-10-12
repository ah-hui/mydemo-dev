CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) NOT NULL,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL,
    PRIMARY KEY (series)
);

CREATE TABLE `user` (
  `id` int(19) NOT NULL AUTO_INCREMENT COMMENT '用户ID,PK',
	`version` int(19) NOT NULL DEFAULT '1' COMMENT 'version',
  `enabled` boolean NOT NULL DEFAULT true COMMENT '账户状态',
  `account_expired` boolean NOT NULL DEFAULT false COMMENT '账户失效',
  `account_locked` boolean NOT NULL DEFAULT false COMMENT '账户锁定',
  `login_name` varchar(20) NOT NULL COMMENT '登录名',
  `password_expired` boolean NOT NULL DEFAULT false COMMENT '密码失效',
  `hashed_password` varchar(240) NOT NULL COMMENT '密码(加密)',
  `full_name` varchar(60) NOT NULL COMMENT '用户全名',
  `phone` varchar(20) NOT NULL COMMENT '手机',
  `email` varchar(120) NOT NULL COMMENT '邮箱',
	`sex` varchar(6) NOT NULL COMMENT '性别',
	`birth_day` datetime DEFAULT NULL COMMENT '生日',
  `created_by` int(19) NOT NULL DEFAULT '-1' COMMENT '创建人',
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updated` timestamp NOT NULL COMMENT '最近更新时间',
  `updated_by` int(19) NOT NULL DEFAULT '-1' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_name_unique_key` (`login_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--password:0cf7cfd9580e9c15d32342eddd288e57