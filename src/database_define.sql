DROP DATABASE IF EXISTS oj;
CREATE DATABASE IF NOT EXISTS oj charset = "utf8";
USE oj;

DROP TABLE IF EXISTS t_problem;
CREATE TABLE t_problem (
  /*题目信息*/
  `problem_id`    INT   NOT NULL auto_increment,      /*题目id,主键*/
  `title`         VARCHAR(100)   NOT NULL DEFAULT "", /*题目标题*/
  `desc`          text NOT NULL,  /*题目描述*/
  `input_desc`    text NOT NULL,  /*输入描述*/
  `output_desc`   text NOT NULL,  /*输出描述*/
  `input_sample`  text NOT NULL,  /*输入样例*/
  `output_sample` text NOT NULL,  /*输出样例*/
  `hint`          text NOT NULL,  /*提示*/
  `source`        text NOT NULL,  /*来源*/
  `create_time`   bigint NOT NULL DEFAULT 0,     /*创建时间*/
  `static_lang_time_limit` mediumint NOT NULL default 1000,  /*静态语言时间限制*/
  `static_lang_mem_limit` mediumint NOT NULL default 65535,  /*静态语言内存限制*/
  `dynamic_lang_time_limit` mediumint NOT NULL default 2000, /*动态语言时间限制*/
  `dynamic_lang_mem_limit` mediumint NOT NULL default 131070, /*动态语言内存限制*/
  `accepted`      INT   NOT NULL DEFAULT 0,  /*通过人数*/
  `submitted`      INT   NOT NULL DEFAULT 0,  /*提交次数*/
  primary key(`problem_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE=InnoDB;


DROP TABLE IF EXISTS t_test_point;
CREATE TABLE t_test_point (
  /*测试点数据*/
  `problem_id`         INT          NOT NULL, /*题目id,主键*/
  `test_point_id`      INT          NOT NULL, /*测试点编号*/
  `input_text_path`    VARCHAR(256) NOT NULL, /*输入文件路径*/
  `input_text_length`  INT          NOT NULL, /*输入类容长度*/
  `output_text_path`   VARCHAR(256) NOT NULL, /*输出文件路径*/
  `output_text_length` INT          NOT NULL, /*输出文件长度*/
  PRIMARY KEY (`problem_id`, `test_point_id`)
)
  DEFAULT CHARSET = "utf8"
  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  /*用户信息*/
  `user_id`       INT  NOT NULL auto_increment, /*用户id*/
  `email`         VARCHAR(64)    NOT NULL,     /*邮箱*/
  `user_name`     VARCHAR(64)     NOT NULL,     /*用户名*/
  `password`      VARCHAR(40)     NOT NULL,     /*密码*/
  `register_time` bigint  NOT NULL,  /*注册时间*/
  `last_login_time` bigint NOT NULL, /*最后登录时间*/
  `user_type`    TINYINT NOT NULL DEFAULT 0,    /*用户类型*/
  `prefer_language` VARCHAR(16) NOT NULL DEFAULT -1,/*偏好语言*/
  `accepted`      INT  NOT NULL DEFAULT 0,      /*通过次数*/
  `submitted`     INT  NOT NULL DEFAULT 0,       /*提交次数*/
  `bio`         VARCHAR(64),                 /*签名*/
  `send_code`    TINYINT,                       /*是否发送通过的代码到邮箱*/
    PRIMARY KEY(`user_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE=InnoDB;


DROP TABLE IF EXISTS t_submit_record;
CREATE TABLE t_submit_record (
  /*用户提交记录*/
  `submit_id`    INT AUTO_INCREMENT AUTO_INCREMENT                                                                                                                                                                                         NOT NULL, /*提交id*/
  `user_id`      INT                                                                                                                                                                                                                       NOT NULL, /*用户*/
  `problem_id`   INT                                                                                                                                                                                                                       NOT NULL, /*题目*/
  `contest_id`   INT                                                                                                                                                                                                                       NOT NULL DEFAULT 0, /*比赛ID,正常做题为0, 在比赛中的话该值设为比赛ID*/
  `result`       VARCHAR(32)  NOT NULL, /*运行结果*/
  `language`     VARCHAR(16)  NOT NULL, /*代码语言*/
  `source_code`  text,                         /*源代码*/
  `code_length`  MEDIUMINT DEFAULT 0,          /*代码长度*/
  `time_consume` SMALLINT                                                                                                                                                                                                                  NOT NULL DEFAULT 0, /*耗时*/
  `mem_consume`  MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗内存*/
  `submit_time`  BIGINT                                                                                                                                                                                                                    NOT NULL DEFAULT 0, /*提交时间*/
  `judge_time`   BIGINT                                                                                                                                                                                                                    NOT NULL DEFAULT 0, /*评测时间*/
  primary key(`submit_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE = InnoDB;



DROP TABLE IF EXISTS t_compile_info;
CREATE TABLE t_compile_info (
  /*提交代码编译结果*/
  `submit_id` INT NOT NULL,           /*提交id*/
  `compile_result` text,   /*编译结果,成功为空,失败保存错误信息*/
  PRIMARY KEY(`submit_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;



DROP TABLE IF EXISTS t_judge_detail;
CREATE TABLE t_judge_detail (
  /*保存每个测试点的详细信息*/
  `submit_id`     INT                                                                                                                                                                                                                       NOT NULL,             /*提交id*/
  `test_point_id` TINYINT                                                                                                                                                                                                                   NOT NULL, /*测试点id*/
  `time_consume`  MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗时*/
  `mem_consume`   MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗内存*/
  `return_val`    INT                                                                                                                                                                                                                       NOT NULL DEFAULT 0, /*进程返回值*/
  `result`       VARCHAR(32)  NOT NULL, /*运行结果*/
  PRIMARY KEY (`submit_id`, `test_point_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE = InnoDB;




DROP TABLE IF EXISTS t_contest;
CREATE TABLE t_contest (
  /*比赛信息*/
  `contest_id` INT auto_increment NOT NULL,        /*比赛id*/
  `title` VARCHAR(100) NOT NULL,    /*比赛名称*/
  `desc` VARCHAR(400),               /*比赛介绍*/
  `start_time` bigint NOT NULL,        /*开始时间*/
  `end_time` bigint NOT NULL,          /*结束时间*/
  `register_start_time` bigint NOT NULL, /*报名开始时间*/
  `register_end_time` bigint NOT NULL,   /*报名结算时间*/
  `open` TINYINT DEFAULT 0,      /*是否公开*/
  `sponsor` VARCHAR(64) NOT NULL,     /*发起人*/
  `contest_type` ENUM("OI", "ACM") DEFAULT "ACM", /*赛制*/
  `create_time`  BIGINT DEFAULT 0 NOT NULL, /*比赛创建时间*/
  PRIMARY KEY(`contest_id`)
) DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;



DROP TABLE IF EXISTS t_contest_problem;
CREATE TABLE t_contest_problem (
  /*比赛题目*/
  `contest_id` INT NOT NULL,  /*比赛id*/
  `problem_id` INT NOT NULL,  /*题目全局id*/
  `inner_id`   VARCHAR(2),    /*题目在本次比赛中的id*/
  `accepted`   INT NOT NULL DEFAULT 0, /*通过次数*/
  `submitted`  INT NOT NULL DEFAULT 0, /*提交次数*/
  PRIMARY KEY(`contest_id`, `problem_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;



DROP TABLE IF EXISTS t_contest_user;
CREATE TABLE t_contest_user (
  /*参与比赛的用户*/
  `contest_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY(`contest_id`, `user_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_discuss;
CREATE TABLE t_discuss (
  /*
  针对一个题目的讨论
  发起的新话题
  回复某人
  */
  `post_id` INT auto_increment NOT NULL,  /*消息id*/
  `type` TINYINT NOT NULL, /*类型*/
  `post_title` VARCHAR(100) NOT NULL, /*标题*/
  `post_content` text NOT NULL,  /*内容*/
  `post_time` bigint NOT NULL,  /*提交时间*/
  `post_user` INT NOT NULL,  /*提交用户*/
  PRIMARY KEY(`post_id`)
) DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;

DROP TABLE IF EXISTS t_image_path;
CREATE TABLE t_image_path (
  /*保存照片路径*/
  `image_id` INT auto_increment NOT NULL,  /*照片id*/
  `abs_path` VARCHAR(4096), /*绝对路径*/
  `rlt_path` VARCHAR(4096), /*相对路径*/
  PRIMARY KEY(`image_id`)
) DEFAULT charset = "utf8" auto_increment=1 ENGINE = InnoDB;


DROP TABLE t_language;
CREATE TABLE t_language (
  `language_id` TINYINT auto_increment NOT NULL,
  `language`   VARCHAR(16) NOT NULL,
  PRIMARY KEY(`language_id`, `language`)
)  DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;

INSERT INTO t_language(`language`) VALUES ("C");
INSERT INTO t_language(`language`) VALUES ("C++");
INSERT INTO t_language(`language`) VALUES ("Java");
INSERT INTO t_language(`language`) VALUES ("Python2");
INSERT INTO t_language(`language`) VALUES ("Python3");




/*视图*/
CREATE VIEW v_submit_record AS
  SELECT
    submit_id,
    problem_id,
    title,
    user_id,
    user_name,
    contest_id,
    result,
    language,
    source_code,
    code_length,
    time_consume,
    mem_consume,
    submit_time,
    judge_time,
    compile_result
  FROM ((t_submit_record submit LEFT JOIN t_compile_info compile USING(`submit_id`)) JOIN t_user USING (`user_id`)) JOIN t_problem USING (`problem_id`)





SELECT * FROM (t_contest_problem LEFT JOIN t_contest USING(`contest_id`)) LEFT JOIN t_problem USING (`problem_id`)


/*没使用的表*/
DROP TABLE IF EXISTS t_source_code;
CREATE TABLE t_source_code (
  /*提交的源代码*/
  `submit_id`          INT NOT NULL,
  `source_code`        text,
  `source_code_length` INT,
  PRIMARY KEY(`submit_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;