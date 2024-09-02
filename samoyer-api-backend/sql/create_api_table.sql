# 数据库初始化

-- 创建库
create database if not exists samoyer_api;

-- 切换库
use samoyer_api;

-- interface_info表
-- 接口信息
create table if not exists samoyer_api.`interface_info`
(
    `id`             bigint                             not null auto_increment comment '主键' primary key,
    `name`           varchar(256)                       not null comment '名称',
    `description`    varchar(256)                       null comment '描述',
    `url`            varchar(512)                       not null comment '接口地址',
    `requestParams`  text                               not null comment '请求参数',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         int                                not null comment '接口状态（0-关闭，1-开启）',
    `method`         varchar(256)                       not null comment '请求类型',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted`      tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';

insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('郭峻熙', '宋展鹏', 'www.john-halvorson.name', '夏瑾瑜', '黎智辉', 0, '苏果', 9724426);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('侯鹏煊', '叶明哲', 'www.maurice-rath.org', '曾黎昕', '赖越彬', 0, '阎煜城', 8573512923);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('黄楷瑞', '董浩然', 'www.vashti-crooks.net', '姚晟睿', '邹修洁', 0, '汪振家', 7);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('韩乐驹', '林文', 'www.shelton-kreiger.io', '田煜城', '朱立果', 0, '顾瑞霖', 892296300);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('郭智渊', '武智宸', 'www.leora-boyer.name', '余烨霖', '金明', 0, '陶熠彤', 27029707);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('梁哲瀚', '崔伟祺', 'www.elenor-frami.net', '王鑫鹏', '龙雨泽', 0, '汪昊天', 83677797);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('雷弘文', '郭黎昕', 'www.george-lowe.name', '韩天磊', '杜弘文', 0, '宋烨华', 39601);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('程浩轩', '陶峻熙', 'www.danyel-carroll.co', '邵子骞', '于荣轩', 0, '余苑博', 2);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('袁展鹏', '蒋修洁', 'www.wilhemina-mraz.com', '金峻熙', '宋耀杰', 0, '程弘文', 7);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('熊文', '洪熠彤', 'www.ruben-williamson.name', '宋雨泽', '董子骞', 0, '叶乐驹', 494031);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('薛峻熙', '郭琪', 'www.lane-friesen.net', '熊子涵', '姚金鑫', 0, '郑文昊', 182913);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('唐立诚', '赵正豪', 'www.patricia-schaden.info', '石致远', '龚立诚', 0, '许梓晨', 60486964);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('秦思', '薛天磊', 'www.nakisha-huels.name', '曾君浩', '苏智渊', 0, '金风华', 992401695);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('许果', '丁烨伟', 'www.marcos-bogan.info', '尹弘文', '韩楷瑞', 0, '陈志泽', 81618);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('史思聪', '曹果', 'www.christian-crona.biz', '朱绍齐', '胡晋鹏', 0, '余君浩', 79);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('韦思', '范哲瀚', 'www.hershel-bosco.com', '罗瑾瑜', '石炎彬', 0, '万苑博', 86);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('熊鹏涛', '覃文昊', 'www.louisa-beier.org', '王鑫鹏', '傅泽洋', 0, '邓雨泽', 800799);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('周越泽', '马子涵', 'www.randolph-bechtelar.name', '赵楷瑞', '袁昊天', 0, '吴瑞霖', 9);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('郭越彬', '林驰', 'www.thuy-kuvalis.net', '蔡彬', '陈越彬', 0, '梁昊然', 78);
insert into samoyer_api.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`,
                                          `method`, `userId`)
values ('夏雪松', '陶煜祺', 'www.ross-wilkinson.biz', '梁峻熙', '尹君浩', 0, '赵煜祺', 84133216);

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    gender       tinyint                                null comment '性别',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    accessKey    varchar(512)                           not null comment 'accessKey',
    secretKey    varchar(512)                           not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (userAccount)
) comment '用户';

-- 用户调用接口关系表
create table if not exists samoyer_api.`user_interface_info`
(
    `id`              bigint                             not null auto_increment comment '主键' primary key,
    `userId`          bigint                             not null comment '调用用户 id',
    `interfaceInfoId` bigint                             not null comment '接口 id',
    `totalNum`        int      default 0 not null comment '总调用次数',
    `leftNum`         int      default 0 not null comment '剩余调用次数',
    `status`          int      default 0 not null comment '0-正常，1-禁用',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`        tinyint  default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';