alter table merchant
    add status int default 0 not null after patrol_code;

alter table merchant
    add store_area int null after merchant_health_code;

alter table merchant
    add away_from_school tinyint(1) default 0 not null after merchant_health_code;

alter table merchant
    add referrer varchar(16) null after merchant_health_code;

alter table merchant
    add referrer_phone varchar(16) null after merchant_health_code;

alter table merchant
    add wechat_company_flag tinyint(1) default 0 not null after merchant_health_code;

alter table merchant
    add bd_id bigint null after merchant_health_code;

-- auto-generated definition
create table bank_appointment
(
    id               bigint auto_increment
        primary key,
    bank_id          int           not null comment '预约银行ID',
    merchant_id      bigint        not null comment '关联商户id',
    status           int default 1 not null comment '状态',
    appointment_time date          not null comment '预约时间'
)
    comment '预约银行表';

-- auto-generated definition
create table BD
(
    id            bigint auto_increment
        primary key,
    name          varchar(16)                        null comment 'BD姓名',
    department_id bigint                             null comment '所属部门ID',
    role_id       bigint                             null comment '角色ID',
    sys_user_id   bigint                             null comment '关联账号ID',
    entry_date    date                               null comment '入职时间',
    creator       varchar(32)                        null,
    create_time   datetime default CURRENT_TIMESTAMP not null,
    deleted       int      default 0                 not null comment '是否删除',
    constraint BD_sys_user_id_uindex
        unique (sys_user_id)
)
    comment 'BD信息表';

alter table business_license
    modify valid_period timestamp null comment '营业执照有效期限';

alter table business_license
    modify update_time timestamp null;

alter table business_license
    modify create_time timestamp null default CURRENT_TIMESTAMP ;