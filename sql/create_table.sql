# 数据库初始化

-- 创建库
create database if not exists data_wise_master;

-- 切换库
use data_wise_master;

-- 用户表
drop table if exists `user`;
create table if not exists `user`
(
    `id`            bigint unsigned primary key comment 'id',
    `user_account`  varchar(256)                           not null comment '用户账户',
    `user_password` varchar(512)                           not null comment '用户密码',
    `user_nickname` varchar(256) comment '用户昵称',
    `user_avatar`   varchar(1024) comment '用户头像',
    `user_profile`  varchar(512) comment '用户简介',
    `user_role`     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    `create_time`   datetime     default current_timestamp not null comment '创建时间',
    `update_time`   datetime     default current_timestamp not null on update current_timestamp comment '更新时间',
    `is_delete`     tinyint      default 0                 not null comment '是否删除',
    unique key uk_user_account (`user_account`)
) comment '用户表' collate = utf8mb4_unicode_ci;
INSERT INTO data_wise_master.user (id, user_account, user_password, user_nickname, user_avatar, user_profile, user_role,
                                   create_time, update_time, is_delete)
VALUES (1, 'administrator', '37c6f5938f00d2780a6d75f3eac103a3', '皮卡丘管理员',
        'http://source.cute-pikachu.cn/user_avatar/1/NtgrXuRW-test.png', '皮卡丘管理员', 'admin', '2024-05-06 17:10:39',
        '2024-05-06 17:21:55', 0);


-- 帖子表
drop table if exists `post`;
create table if not exists `post`
(
    `id`          bigint unsigned comment 'id' primary key,
    `title`       varchar(512)                       not null comment '标题',
    `content`     text                               not null comment '内容',
    `tags`        varchar(1024)                      not null comment '标签列表（json 数组）',
    `thumb_num`   int      default 0                 not null comment '点赞数',
    `favour_num`  int      default 0                 not null comment '收藏数',
    `user_id`     bigint                             not null comment '创建用户 id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除',
    index idx_user_id (`user_id`)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
drop table if exists `post_thumb`;
create table if not exists `post_thumb`
(
    `id`          bigint unsigned comment 'id' primary key,
    `post_id`     bigint                             not null comment '帖子 id',
    `user_id`     bigint                             not null comment '创建用户 id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (`post_id`),
    index idx_user_id (`user_id`)
) comment '帖子点赞' collate = utf8mb4_unicode_ci;

-- 帖子收藏表（硬删除）
drop table if exists `post_favour`;
create table if not exists `post_favour`
(
    `id`          bigint unsigned comment 'id' primary key,
    `post_id`     bigint                             not null comment '帖子 id',
    `user_id`     bigint                             not null comment '创建用户 id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (`post_id`),
    index idx_user_id (`user_id`)
) comment '帖子收藏' collate = utf8mb4_unicode_ci;

-- 图表信息表
drop table if exists `chart`;
create table if not exists `chart`
(
    `id`          bigint unsigned primary key comment 'id',
    `user_id`     bigint unsigned                    not null comment '用户id',
    `goal`        text                               not null comment '分析目标',
    `name`        varchar(128)                       not null comment '图表名称',
    `data`        text                               not null comment '原始数据',
    `chart_type`  varchar(128)                       not null comment '图表类型',
    `gen_chart`    text comment 'AI分析生成的图表数据',
    `gen_result`   text comment 'AI分析的结论',
    `create_time` datetime default current_timestamp not null comment '创建时间',
    `update_time` datetime default current_timestamp not null on update current_timestamp comment '更新时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除',
    index idx_user_id (`user_id`)
) comment '图表信息表' collate = utf8mb4_unicode_ci;
