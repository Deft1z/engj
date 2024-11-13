drop table if exists b_survey;
create table b_survey
(
    id             int auto_increment primary key comment '主键id',
    survey_code    varchar(50)  not null comment '表单编码',
    survey_name    varchar(100) not null comment '调查表单名称',
    remark         varchar(200) null comment '备注',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单表';
alter table b_survey add unique (survey_code, tenant_id);

insert into b_survey (id, survey_code, survey_name, remark)
values (1, 'customer-satisfaction', '客户满意度调查', '用电业务客户满意度调查表单');

drop table if exists b_survey_item;
create table b_survey_item
(
    id             int auto_increment primary key comment '主键id',
    survey_id      int          not null comment '调查表单id',
    item_code      varchar(30)  null comment '表单项code',
    item_name      varchar(200) not null comment '表单项名称',
    item_type      varchar(20)  not null default 'text' comment '表单项类型：title-标题，text-文本，longtext-长文本，radio-单选，checkbox-多选，select-下拉框，date-日期，time-时间，datetime-日期时间，number-数字，stars-星级，file-附件',
    required       tinyint      not null default 0 comment '是否必填：0-否，1-是',
    parent_id      int          null comment '父级id',
    priority       int          not null default 1 comment '优先级',
    fill_by        varchar(20)  not null default 'all' comment '可填写人：all-所有人, promoter-发起人, invitee-受邀请人',
    general_biz_key varchar(30) null comment '通用业务字段key：survey_obj_code-业务对象编码，survey_obj_name-业务对象名称',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单项表';
alter table b_survey_item add unique (survey_id, general_biz_key, tenant_id);

insert into b_survey_item (id, survey_id, item_code, item_name, item_type, required, parent_id, priority, fill_by, general_biz_key)
values (1, 1, '', '项目信息', 'title', false, null, 1, 'promoter', null)
     , (2, 1, 'projectName', '项目名称', 'text', true, null, 2, 'promoter', 'survey_obj_name')
     , (3, 1, 'projectNum', '项目编号', 'text', true, null, 3, 'promoter', 'survey_obj_code')
     , (4, 1, 'projectType', '项目类型', 'select', true, null, 4, 'promoter', null)
     , (5, 1, 'serviceUnit', '服务单位', 'text', true, null, 5, 'promoter', null)
     , (6, 1, 'serviceAddr', '服务地址', 'text', true, null, 6, 'promoter', null)
     , (7, 1, 'returnVisitor', '回访人员', 'text', false, null, 7, 'promoter', null)
     , (8, 1, 'returnPhone', '回访人电话', 'text', false, null, 8, 'promoter', null)
     , (9, 1, 'remark', '备注', 'longtext', false, null, 9, 'promoter', null)
     , (10, 1, '', '受访人信息', 'title', false, null, 10, 'invitee', null)
     , (11, 1, 'clientName', '客户名称', 'text', false, null, 11, 'all', 'client_name')
     , (12, 1, 'clientPhone', '客户手机', 'text', false, null, 12, 'all', null)
     , (13, 1, '', '评价', 'title', false, null, 13, 'invitee', null)
     , (14, 1, '', '总体评价', 'stars', false, null, 14, 'invitee', null)
     , (15, 1, '', '维护过程及结果', 'stars', true, 14, 1, 'invitee', null)
     , (16, 1, '', '整体服务工作', 'stars', true, 14, 2, 'invitee', null)
     , (17, 1, '', '业务跟进服务评价', 'stars', false, null, 17, 'invitee', null)
     , (18, 1, '', '业务人员工作态度', 'stars', true, 17, 1, 'invitee', null)
     , (19, 1, '', '合同到期提醒沟通', 'stars', true, 17, 2, 'invitee', null)
     , (20, 1, '', '改进意见和建议', 'longtext', false, null, 20, 'invitee', null)
;

drop table if exists b_survey_item_option;
create table b_survey_item_option
(
    id             int auto_increment primary key comment '主键id',
    item_id        int          not null comment '调查表单项id',
    item_val       varchar(200) not null comment '表单项值',
    priority       int          not null default 1 comment '优先级',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单项选值表';

insert into b_survey_item_option(item_id, item_val, priority)
values (4, '运维项目', 1)
;

drop table if exists b_survey_record;
create table b_survey_record
(
    id             int auto_increment primary key comment '主键id',
    survey_id      int          not null comment '调查表单id',
    survey_name    varchar(100) not null comment '调查表名称',
    fill_json      json         default null comment '发起人填写的表单内容',
    share_url      varchar(200) null comment '分享评价链接',
    share_expire_at datetime    null comment '分享链接过期时间',
    status         int          not null default 0 comment '0 未提交 1 待评价 2 已完成',
    create_user_id int          null comment '创建人ID',
    create_time    datetime     not null default current_timestamp comment '创建时间',
    modify_user_id int          null comment '更新人ID',
    modify_time    datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag           tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id      int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单记录表';


drop table if exists b_survey_record_answer;
create table b_survey_record_answer
(
    id               int auto_increment primary key comment '主键id',
    promoter_id      int          not null comment '发起人id',
    invitee_id       int          not null comment '受邀请人id',
    survey_record_id int          not null comment '调查表单记录id',
    survey_name      varchar(100) null comment '调查表单名称',
    fill_json      json           default null comment '受邀请人填写的表单内容',
    status         int            not null default 1 comment '0 未提交 1 待评价 2 已评价',
    create_user_id   int          null comment '创建人ID',
    create_time      datetime     not null default current_timestamp comment '创建时间',
    modify_user_id   int          null comment '更新人ID',
    modify_time      datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    flag             tinyint      not null default 1 comment '数据状态：-1-删除，1-正常',
    tenant_id        int          not null default 1 comment '租户id'
) engine = innodb
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment ='调查表单记录填写表';


