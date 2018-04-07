/*�û���user�Ĵ���*/
CREATE TABLE user(
	u_no			INT		NOT NULL,		
	u_pwd			VARCHAR(16)	NOT NULL,
	u_name		VARCHAR(8),
	u_email		VARCHAR(18),
	u_state		TINYTEXT,
	h_id		INT,
	PRIMARY		KEY(u_no),
	FOREIGN KEY(h_id) REFERENCES head(h_id);
);
/*�����б�friend�Ĵ���*/
CREATE TABLE friend
(
	f_id			INT NOT NULL,
	u_noz			INT NOT NULL,
	u_noy 		INT NOT NULL,
	f_date 		TIMESTAMP,
	PRIMARY KEY(f_id),
	FOREIGN KEY(u_noz) REFERENCES user(u_no),
	FOREIGN KEY(u_noy) REFERENCES user(u_no)
);

CREATE TABLE diary(/*�ռ�diary��Ĵ���*/
	r_id 				INT NOT NULL,
	r_title			VARCHAR(18) NOT NULL,
	r_content 	TEXT NOT NULL,
	r_date 			TIMESTAMP,
	u_no 				INT NOT NULL,
	PRIMARY 		KEY(r_id),
	FOREIGN 		KEY(u_no) REFERENCES user(u_no)
);

CREATE TABLE album(/*���album��Ĵ���*/
	x_id 		INT NOT NULL,
	x_name 	VARCHAR(18) NOT NULL,
	u_no 		INT NOT NULL,
	x_access INT DEFAULT 0,						-- 0:������1�����ѣ�2�����Լ��ɼ�
	x_date 	TIMESTAMP,
	PRIMARY KEY(x_id),
	FOREIGN KEY(u_no) REFERENCES user(u_no)
);

CREATE TABLE photo(/*��Ƭphoto��Ĵ���*/
	p_id 		INT NOT NULL,
	p_name 		VARCHAR(18) NOT NULL,
	p_des		VARCHAR(50) NOT NULL,
	p_data 		MEDIUMBLOB,
	x_id 		INT NOT NULL,
	PRIMARY KEY(p_id),
	FOREIGN KEY(x_id) REFERENCES album(x_id)
);

CREATE TABLE comment(/*����comment��Ĵ���*/
	c_id 			INT NOT NULL,
	c_content		TEXT NOT NULL,
	u_no 			INT NOT NULL,
	r_id 			INT NOT NULL,
	c_date 		TIMESTAMP,
	PRIMARY 	KEY(c_id),
	FOREIGN 	KEY(u_no) REFERENCES user(u_no),
	FOREIGN 	KEY(r_id) REFERENCES diary(r_id)
);
CREATE TABLE p_comment(/*��Ƭ����p_comment��*/
	c_id		INT NOT NULL,
	c_content	TEXT NOT NULL,
	u_no		INT NOT NULL,
	p_id		INT NOT NULL,
	c_date		TIMESTAMP,
	PRIMARY KEY(c_id),
	FOREIGN	KEY(u_no) REFERENCES user(u_no),
	FOREIGN KEY(p_id) REFERENCES photo(p_id)
)
/*�ÿ��㼣visit��Ĵ���*/
CREATE TABLE visit
(
	v_id 			INT NOT NULL,
	u_no 		INT NOT NULL,
	v_no 		INT NOT NULL,
	v_date 			TIMESTAMP,
	PRIMARY KEY(v_id),
	FOREIGN KEY(u_no) REFERENCES user(u_no),
	FOREIGN KEY(v_no) REFERENCES user(u_no)
);


CREATE TABLE max(/*����max��*/
	friend_max INT NOT NULL DEFAULT 0,
	diary_max INT NOT NULL DEFAULT 0,
	album_max INT NOT NULL DEFAULT 0,
	photo_max INT NOT NULL DEFAULT 0,
	comment_max INT NOT NULL DEFAULT 0,
	visit_max INT NOT NULL DEFAULT 0,
	user_max INT NOT NULL DEFAULT 0,
	head_max INT NOT NULL DEFAULT 0,
	p_comment_max INT NOT NULL DEFAULT 0
);


CREATE TABLE head(/*ͷ��head��*/
	h_id INT NOT NULL,
	h_des VARCHAR(40) NOT NULL,
	h_data MEDIUMBLOB NOT NULL,
	u_no INT,
	PRIMARY KEY(h_id),
	FOREIGN KEY(u_no) REFERENCES user(u_no)
);
-- ������Ȱ�Ĭ��ͷ����ȥ







