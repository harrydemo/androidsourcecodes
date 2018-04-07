------------------------------------------------------------------------------
---- Part A: basic tables for V1                                          ----
---- assist memory for accurate details                                   ---- 
------------------------------------------------------------------------------

-- 辩证(类别）
create table SYNDROME_SUBJECT
(
    PK_ID           integer     primary key     autoincrement,
    NAME            text        unique          not null,
    KEY_CODES       text                        not null,                       -- 关键代码(拼音), comma seperated to including alias, etc.
    DESCRIPTION     text,
    PARENT_ID       integer
                    references SYNDROME_SUBJECT(PK_ID) on delete set null
);

-- 方剂/药方/(经)验方 (RX stands for Prescription)
create table RX_RECIPE
(
    PK_ID           integer     primary key     autoincrement,
    NAME            text        unique          not null,
    ALIAS           text,
    KEY_CODES       text                        not null,
    SYNDROME_ID     integer
                    references SYNDROME_SUBJECT(PK_ID) on delete restrict,
    EFFICACY        text,
    INDICATION      text,
    DESCRIPTION     text,
    ORIGIN          text,                                                       -- 出处
    BASE_ID         integer     
                    references RX_RECIPE(PK_ID) on delete restrict
);

-- 药物/草药
create table MEDICINE
(
    PK_ID           integer     primary key     autoincrement,
    NAME            text        unique          not null,
    ALIAS           text,
    KEY_CODES       text                        not null,
    UNIT            text                        not null
                    check(UNIT in ('克', '毫升', '枚'))         default '克',    -- 药量单位: "克" 或 "毫升" 或 "枚"
    POISON          int                         not null        default 3,      -- 毒性: 是药三分毒
    SYNDROME_ID     integer
                    references SYNDROME_SUBJECT(PK_ID) on delete restrict,
    PROPERTY        text,
    EFFICACY        text,
    INDICATION      text,
    SYNERGIST       text,
    DESCRIPTION     text
);

create table MAP_RX_RECIPE_MEDICINE
(    
    RX_RECIPE_ID    integer                     not null
                    references RX_RECIPE(PK_ID) on delete restrict,
    MEDICINE_ID     integer                     not null
                    references MEDICINE(PK_ID) on delete restrict,
    ORDER_NUM       real                        not null,
    QUANTITY        real                        not null,
    IS_OPTIONAL     char                                                        -- 常用可加减药物
                    check(IS_OPTIONAL in ('Y', null))           default null,
    constraint      UK_MAP_RX_RECIPE_MEDICINE   unique(RX_RECIPE_ID, MEDICINE_ID)
);

------------------------------------------------------------------------------
