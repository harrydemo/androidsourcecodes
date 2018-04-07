insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('1', '麻黄汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'MHT', '【功用】发汗解表, 宣肺平喘.
【主治】感冒风寒, 怕冷发热, 无汗, 咳嗽气喘.
【用法及注意】日服一剂, 水煎取汁, 分二次服.
【歌诀】:
麻黄汤中用桂枝， \n杏仁甘草四般施， \n发热恶寒头项痛， \n喘而无汗服之宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('2', '桂枝汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'GZT', '【功用】解肌发表，调和营卫。
【主治】外感风寒表虚证。头痛发热，汗出恶风，鼻鸣干呕，苔白不渴，脉浮缓或脉弱者。
【用法及注意】日服一剂, 水煎取汁, 分二次温服.服后片刻，饮热稀粥（或热开水）一小碗, 使其微微汗出.
【歌诀】:
桂枝汤治太阳风， \n芍药甘草姜枣同， \n解肌发表调营卫， \n表虚有汗此为功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('3', '九味羌活汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'JWQHT', '【功用】发汗祛湿，兼清里热。
【主治】外感风寒湿邪，兼有里热证。恶寒发热，肌表无汗，头痛项强，肢体痠楚疼痛，口苦微渴，舌苔白或微黄，脉浮。
【用法及注意】
【歌诀】:
九味羌活用防风， \n细辛苍芷与川芎， \n黄芩生地同甘草， \n分经论治宜变通。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('4', '香薷散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'XRS', '【功用】祛暑解表，化湿和中。
【主治】阴暑。恶寒发热，腹痛吐泻，头痛身重，无汗，胸闷，舌苔白腻，脉浮。
【用法及注意】
【歌诀】:
三物香薷豆朴先， \n散寒化湿功效兼， \n若著银翘豆易花， \n新加香薷祛暑煎。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('5', '小青龙汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'XQLT', '【功用】解表散寒，温肺化饮。
【主治】外感内饮证。恶寒发热，无汗，胸痞喘咳，痰多而稀，或痰饮喘咳，不得平卧，或身体疼重，头面四肢浮肿，舌苔白滑，脉浮者。
【用法及注意】
【歌诀】:
小青龙汤最有功， \n风寒束表饮停胸， \n辛夏甘草和五味， \n姜桂麻黄芍药同。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('6', '止嗽散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛温解表'), '', 'ZKS', '【功用】宣利肺气，疏风止咳。
【主治】风邪犯肺证。咳嗽咽痒，咳痰不爽，或微有恶风发热，舌苔薄白，脉浮缓。
【用法及注意】
【歌诀】:
止嗽散内用桔梗， \n紫菀荆芥百部陈， \n白前甘草共为末， \n姜汤调服止嗽频。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('7', '银翘散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛凉解表'), '', 'YQS', '【功用】辛凉透表，清热解毒。
【主治】温病初起。发热无汗，或有汗不畅，微恶风寒，头痛口渴，咳嗽咽痛，舌尖红，苔薄白或微黄，脉浮数。
【用法及注意】
【歌诀】:
银翘散主上焦疴， \n竹叶荆牛豉薄荷， \n甘桔芦根凉解法， \n清疏风热煮无过。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('8', '桑菊饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛凉解表'), '', 'SJY', '【功用】疏风清热，宣肺止咳。
【主治】风温初起。但咳，身热不甚，口微渴，脉浮数。
【用法及注意】
【歌诀】:
桑菊饮中桔杏翘， \n芦根甘草薄荷饶， \n清疏肺卫轻宣剂， \n风温咳嗽服之消。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('9', '麻杏甘石汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛凉解表'), '', 'MXGST', '【功用】辛凉宣肺，清热平喘。
【主治】表邪未解，肺热咳喘证。身热不解，咳逆气急鼻煽，口渴，有汗或无汗，舌苔薄白或黄，脉浮而数者。
【用法及注意】
【歌诀】:
伤寒麻杏石甘汤， \n汗出而喘法度良， \n辛凉宣泄能清肺， \n定喘除热效力彰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('10', '柴葛解肌汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛凉解表'), '', 'CGJJT', '【功用】解肌清热。
【主治】感冒风寒，郁而化热证。恶寒渐轻，身热增盛 ，无汗头痛，目疼鼻干，心烦不眠，嗌干耳聋，眼眶痛，舌苔薄黄，脉浮微洪者。
【用法及注意】
【歌诀】:
陶氏柴葛解肌汤， \n邪在三阳热势张， \n芩芍桔甘羌活芷， \n石膏大枣与生姜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('11', '升麻葛根汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__辛凉解表'), '', 'SMGGT', '【功用】解肌透疹。
【主治】麻疹初起。疹出不透，身热头痛，咳嗽，目赤流泪，口渴，舌红，脉数。
【用法及注意】
【歌诀】:
阎氏升麻葛根汤， \n芍药甘草合成方， \n麻疹初期发不透， \n解肌透疹此为良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('12', '败毒散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__扶正解表'), '', 'BDS', '【功用】散寒祛湿，益气解表。
【主治】气虚外感证。憎寒壮热，头项强痛，肢体痠痛，无汗，鼻塞声重，咳嗽有痰，胸膈痞满，舌淡苔白，脉浮而按之无力。
【用法及注意】
【歌诀】:
人参败毒茯苓草， \n枳桔柴前羌独芎， \n薄荷少许姜三片， \n时行感冒有奇功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('13', '参苏饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__扶正解表'), '', 'CSY', '【功用】益气解表，理气化痰。
【主治】虚人外感风寒，内有痰饮证。恶寒发热，无汗，头痛，鼻塞，咳嗽痰白，胸膈满闷，倦怠无力，气短懒言，舌苔白，脉弱。
【用法及注意】
【歌诀】:
参苏饮内用陈皮， \n枳壳前胡半夏齐， \n干葛木香甘桔茯， \n气虚外感最相宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('14', '再造散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__扶正解表'), '', 'ZZS', '【功用】助阳益气，解表散寒。
【主治】阳气虚弱，外感风寒。恶寒发热，热轻寒重，无汗肢冷，倦怠嗜卧，面色苍白，语言低微，舌淡苔白，脉沉无力，或浮大无力。
【用法及注意】
【歌诀】:
再造散用参芪甘， \n桂附羌防芎芍参， \n细辛煨姜大枣入， \n阳虚外感服之安。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('15', '加减葳蕤汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__扶正解表'), '', 'JJWRT', '【功用】滋阴解表。
【主治】阴虚外感风热证。头痛身热，微恶风寒，无汗或有汗不多，咳嗽，心烦，口渴，咽干，舌红脉数。
【用法及注意】
【歌诀】:
加减葳蕤用白薇， \n豆豉葱白桔梗随， \n草枣薄荷八味共， \n滋阴发汗功可慰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('16', '大承气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__寒下'), '', 'DCQT', '【功用】峻下热结。
【主治】（1）阳明腑实证（痞、满、燥、实）。大便不通，频转矢气，脘腹痞满，腹痛拒按，按之则硬，日晡潮热，神昏谵语，手足出汗，舌苔黄燥起刺或焦黑燥裂，脉沉实。 \n（2）热结旁流证。下利清水，色纯青，其气臭秽，其腹疼痛，按之坚硬有块，口舌干燥，脉滑数。 \n（3）热厥、痉病、发狂属里热实证者。
【用法及注意】
【歌诀】:
大承气汤用硝黄， \n配伍枳朴泻力强， \n痞满燥实四证见， \n峻下热结宜此方； \n去硝名曰小承气， \n便秘痞满泻热良， \n调胃承气硝黄草， \n便秘口渴急煎尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('17', '大黄牡丹汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__寒下'), '', 'DHMDT', '【功用】泻热破瘀，散结消肿。
【主治】肠痈初起。右下腹疼痛拒按，或右足曲而不伸，伸则痛甚，甚则局部肿痞，或时时发热，自汗恶寒，舌苔薄腻而黄，脉滑数。
【用法及注意】
【歌诀】:
金匮大黄牡丹汤， \n桃仁瓜子芒硝襄， \n肠痈初起腹按痛， \n苔黄脉数服之康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('18', '大黄附子汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温下'), '', 'DHFZT', '【功用】温里散寒，通便止痛。
【主治】寒积腹痛。便秘腹痛，胁下偏痛，发热，手足不温，舌苔白腻，脉弦紧。
【用法及注意】
【歌诀】:
金匮大黄附子汤， \n细辛散寒止痛良， \n冷积内结成实证， \n功专温下妙非常。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('19', '温脾汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温下'), '', 'WPT', '【功用】攻下寒积，温补脾阳。
【主治】寒积腹痛。便秘腹痛，脐下绞结，绕脐不止，手足欠温，苔白不渴，脉沉弦而迟。
【用法及注意】
【歌诀】:
温脾参附与干姜， \n甘草当归硝大黄， \n寒热并行治寒积， \n脐腹绞结痛非常。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('20', '三物备急丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温下'), '', 'SWBJW', '【功用】攻逐寒积。
【主治】寒实腹痛。卒然心腹胀痛，痛如锥刺，气急口噤，大便不通。
【用法及注意】
【歌诀】:
三物备急巴豆研， \n干姜大黄不需煎， \n卒然腹痛因寒积， \n速投此方急救先。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('21', '五仁丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__润下'), '', 'WRW', '【功用】润肠通便。
【主治】津枯便秘。大便干燥，艰涩难出，以及年老或产后血虚便秘。
【用法及注意】
【歌诀】:
五仁柏仁杏仁桃， \n松仁陈皮郁李饶， \n炼蜜为丸米饮下， \n润肠通便此方效。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('22', '济川煎', (select PK_ID from SYNDROME_SUBJECT where NAME = '__润下'), '', 'JCJ', '【功用】温肾益精，润肠通便。
【主治】肾虚便秘。大便秘结，小便清长，腰膝酸软，舌淡苔白，脉沉迟。
【用法及注意】
【歌诀】:
济川归膝肉苁蓉， \n泽泻升麻枳壳从， \n肾虚津亏肠中燥， \n寓通于补法堪宗。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('23', '麻子仁丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__润下'), '脾约丸', 'MZRW,PYW(脾约丸)', '【功用】润肠泻热，行气通便。
【主治】脾约证。肠胃燥热，脾津不足，大便秘结，小便频数。
【用法及注意】
【歌诀】:
麻子仁丸治脾约， \n大黄枳朴杏仁芍， \n胃热津枯便难解， \n润肠通便功效高。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('24', '黄龙汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__攻补兼施'), '', 'HLT', '【功用】攻下热结，益气养血。
【主治】阳明腑实，气血不足证。自利清水，色纯青，或大便秘结，脘腹胀满，腹痛拒按，身热口渴，神倦少气，谵语甚或循衣撮空，神昏肢厥，舌苔焦黄或焦黑，脉虚。
【用法及注意】
【歌诀】:
黄龙汤枳朴硝黄， \n参归甘桔枣生姜， \n阳明腑实气血弱， \n攻补兼施效力强。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('25', '增液承气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__攻补兼施'), '', 'ZYCQT', '【功用】滋阴增液，泄热通便。
【主治】热结阴亏证。燥屎不行，下之不通，脘腹胀满，口干舌燥，舌红苔黄，脉细数。
【用法及注意】
【歌诀】:
增液承气参地冬， \n硝黄加入五药供， \n热结阴亏大便秘， \n增水行舟肠腑通。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('26', '十枣汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__逐水'), '', 'SZT', '【功用】攻逐水饮。
【主治】（1）悬饮。咳唾胸胁引痛，心下痞硬，干呕短气，头痛目眩，或胸背掣痛不得息，舌苔滑，脉沉弦。 \n（2）水肿。一身悉肿，尤以身半以下为重，腹胀喘满，二便不利。
【用法及注意】
【歌诀】:
十枣逐水效堪夸， \n大戟甘遂与芫花， \n悬饮内停胸胁痛， \n大腹肿满用无差。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('27', '禹功散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__逐水'), '', 'YGS', '【功用】逐水通便，行气消肿。
【主治】阳水。遍身水肿，腹胀喘满，大便秘结，小便不利，脉沉有力。
【用法及注意】
【歌诀】:
儒门事亲禹功散， \n牵牛茴香一起研， \n行气逐水又通便， \n姜汁调下阳水痊。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('28', '小柴胡汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__和解少阳'), '', 'XCHT', '【功用】和解少阳。
【主治】（1）伤寒少阳证。往来寒热，胸胁苦满，默默不欲饮食，心烦喜呕，口苦，咽干，目眩，舌苔薄白，脉弦者。 \n（2）妇人热入血室。经水适断，寒热发作有时；以及疟疾，黄疸等病而见少阳证者。
【用法及注意】
【歌诀】:
小柴胡汤和解供, \n半夏人参甘草从, \n更用黄芩加姜枣, \n少阳百病此为宗。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('29', '蒿芩清胆汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__和解少阳'), '', 'HCQDT', '【功用】清胆利湿，和胃化痰。
【主治】少阳湿热症。寒热如疟，寒轻热重，口苦膈闷，吐酸苦水，或呕黄涎而粘，甚则干呕呃逆，胸胁胀痛，小便黄少，舌红苔白腻，间现杂色，脉数而右滑左弦者。
【用法及注意】
【歌诀】:
蒿芩清胆碧玉需, \n陈夏茯苓枳竹茹, \n热重寒轻痰挟湿, \n胸痞呕恶总能除。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('30', '四逆散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__调和肝脾'), '', 'SNS,SLS', '【功用】透邪解郁，疏肝理气。
【主治】（1）阳郁厥逆证。手足不温，或身微热，或咳，或悸，或小便不利，或腹痛，或泄利，脉弦。 \n（2）肝脾不和证。胁肋胀闷，脘腹疼痛，脉弦等。
【用法及注意】
【歌诀】:
四逆散里用柴胡, \n芍药枳实甘草须, \n此是阳郁成厥逆, \n疏肝理脾奏效奇。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('31', '逍遥散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__调和肝脾'), '', 'XYS', '【功用】疏肝解郁，养血健脾。
【主治】肝郁血虚脾弱证。两胁作痛，头痛目眩，口燥咽干，神疲食少，或往来寒热，或月经不调，乳房胀痛，脉弦而虚者。
【用法及注意】
【歌诀】:
逍遥散用归芍柴, \n苓术甘草姜薄偕, \n疏肝养血兼理脾, \n丹栀加入热能排。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('32', '半夏泻心汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__调和寒热'), '', 'BXXXT', '【功用】寒热平调，散结除痞。
【主治】寒热互结之痞证。心下痞，但满而不痛，或呕吐，肠鸣下利，舌苔腻而微黄。
【用法及注意】
【歌诀】:
半夏泻心黄连芩, \n干姜甘草与人参, \n大枣合之治虚痞, \n法在降阳而和阴。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('33', '大柴胡汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__表里双解'), '', 'DCHT', '【功用】和解少阳 ，内泻热结。
【主治】少阳阳明合病。往来寒热，胸胁苦满，呕不止，郁郁微烦，心下痞硬，或心下满痛，大便不解或下利，舌苔黄，脉弦数有力者。
【用法及注意】
【歌诀】:
大柴胡汤用大黄, \n枳实芩夏白芍将, \n煎加姜枣表兼里, \n妙法内攻并外攘。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('34', '防风通圣散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__表里双解'), '', 'FFTSS', '【功用】疏风解表，清热通便。
【主治】风热壅盛，表里俱实证。憎寒壮热无汗，头目昏眩，目赤睛痛，口苦舌干，咽喉不利，涕唾稠粘，大便秘结，小便赤涩，舌苔黄腻，脉数有力。并治疮疡肿毒，肠风痔漏，鼻赤瘾疹等证。
【用法及注意】
【歌诀】:
防风通圣大黄硝, \n荆芥麻黄栀芍翘, \n甘桔芎归膏滑石, \n薄荷苓术力偏饶, \n表里交攻阳热盛, \n外科疡毒总能消。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('35', '葛根黄芩黄连汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__表里双解'), '', 'GGHQHLT,GGHCHLT', '【功用】解表清里。
【主治】协热下利。身热下利，胸脘烦热，口中作渴，喘而无汗，舌红苔黄，脉数或促。
【用法及注意】
【歌诀】:
葛根黄芩黄连汤, \n再加甘草共煎尝, \n邪陷阳明成热利, \n清里解表保安康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('36', '疏凿饮子', (select PK_ID from SYNDROME_SUBJECT where NAME = '__表里双解'), '', 'SZYZ', '【功用】泻下逐水，疏风发表。
【主治】阳水实证。遍身水肿，气喘，口渴，二便不利。
【用法及注意】
【歌诀】:
疏凿槟榔及商陆, \n苓皮大腹同椒目, \n赤豆艽羌泻木通, \n煎加生姜阳水服。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('37', '白虎汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清气分热'), '石膏知母汤', 'BHT,SGZMT(石膏知母汤)', '【功用】清气热, 泻胃火, 生津止渴
【主治】外感热病, 气分热盛, 见高热, 烦躁, 口渴欲饮, 面红, 大汗出, 恶热, 脉洪大或滑数;  以及胃火旺的头痛, 齿痛, 鼻衄, 牙龈出血等症.
【用法及注意】日服一剂, 水煎取汁, 分二次服.
【歌诀】:
白虎膏知甘草梗， \n气分大热此方清， \n热渴汗出脉洪大， \n加入人参气津生。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('38', '竹叶石膏汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清气分热'), '', 'ZYSGT', '【功用】清热生津，益气和胃。
【主治】伤寒、温病、暑病余热未清，气津两伤证。身热多汗，心胸烦闷，气逆欲呕，口干喜饮，或虚烦不寐，舌红苔少，脉虚数。
【用法及注意】
【歌诀】:
竹叶石膏汤人参， \n麦冬半夏甘草临， \n再加粳米同煎服， \n清热益气养阴津。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('39', '清营汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清营凉血'), '', 'QYT', '【功用】清营解毒，透热养阴。
【主治】热入营分证。身热夜甚，神烦少寐，时有谵语，目常喜开或喜闭，口渴或不渴，斑疹隐隐，脉数，舌绛而干。
【用法及注意】
【歌诀】:
清营汤是鞠通方， \n热入心包营血伤， \n角地银翘玄连竹， \n丹麦清热佐之良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('40', '犀角地黄汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清营凉血'), '', 'XJDHT', '【功用】清热解毒，凉血散瘀。
【主治】（1）热入血分证。身热谵语，斑色紫黑，舌绛起刺，脉细数，或喜忘如狂，漱水不欲咽，大便色黑易解等。 \n（2）热伤血络证。吐血，衄血，便血，尿血等，舌红绛，脉数。
【用法及注意】
【歌诀】:
犀角地黄芍药丹， \n血热妄行吐衄斑， \n蓄血发狂舌质绛， \n凉血散瘀病可痊。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('41', '黄连解毒汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热解毒'), '', 'HLJDT', '【功用】泻火解毒。
【主治】三焦火毒热盛证。大热烦躁，口燥咽干，错语不眠；或热病吐血，衄血；或热甚发斑，身热下利，湿热黄疸；外科痈疡疔毒，小便黄赤，舌红苔黄，脉数有力。
【用法及注意】
【歌诀】:
黄连解毒汤四味， \n黄芩黄柏栀子备， \n躁狂大热呕不眠， \n吐衄斑黄均可为。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('42', '普济消毒饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热解毒'), '', 'PJXDY', '【功用】清热解毒，疏风散邪。
【主治】大头瘟。恶寒发热，头面红肿焮痛，目不能开，咽喉不利，舌燥口渴，舌红苔白兼黄，脉浮数有力。
【用法及注意】
【歌诀】:
普济消毒芩连鼠， \n玄参甘桔蓝根侣， \n升柴马勃连翘陈， \n薄荷僵蚕为末咀。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('43', '凉膈散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热解毒'), '', 'LGS', '【功用】泻火通便，清上泄下。
【主治】上中二焦火热证。烦躁口渴，面赤唇焦，胸膈烦热，口舌生疮，或咽痛吐衄，便秘溲赤，或大便不畅，舌红苔黄，脉滑数。
【用法及注意】
【歌诀】:
凉膈硝黄栀子翘， \n黄芩甘草薄荷饶， \n竹叶蜜煎疗膈上， \n中焦燥实服之消。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('44', '仙方活命饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热解毒'), '', 'XFHMY', '【功用】清热解毒，消肿溃坚，活血止痛。
【主治】痈疡肿毒初起。红肿焮痛，或身热凛寒，苔薄白或黄，脉数有力。
【用法及注意】
【歌诀】:
仙方活命金银花， \n防芷归陈草芍加， \n贝母花粉兼乳没， \n穿山角刺酒煎佳。 \n一切痈毒能溃散， \n溃后忌服用勿差。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('45', '导赤散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'DCS', '【功用】清心利水养阴。
【主治】心经火热证。心胸烦热，口渴面赤，意欲冷饮，以及口舌生疮；或心热移于小肠，症见小溲赤涩刺痛，舌红，脉数。
【用法及注意】
【歌诀】:
导赤生地与木通， \n草梢竹叶四般攻， \n口糜淋痛小肠火， \n引热同归小便中。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('46', '龙胆泻肝汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'LGXDT', '【功用】清肝胆实火，泻下焦湿热。
【主治】（1）肝胆实火上炎证。症见头痛目赤，胁痛，口苦，耳聋，耳肿等，舌红苔黄，脉弦数有力。 \n（2）肝胆湿热下注证。症见阴肿，阴痒，阴汗，小便淋浊，或妇女带下黄臭等，舌红苔黄，脉弦数有力。
【用法及注意】
【歌诀】:
龙胆泻肝栀芩柴， \n生地车前泽泻偕， \n木通甘草当归合， \n肝经湿热力能排。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('47', '左金丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'ZJW', '【功用】清泻肝火，降逆止呕。
【主治】肝火犯胃证。症见胁肋疼痛，嘈杂吞酸，呕吐口苦，舌红苔黄，脉弦数。
【用法及注意】
【歌诀】:
左金连萸六一丸， \n肝火犯胃吐吞酸， \n再加芍药名戊己， \n热泄热痢服之安。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('48', '泻白散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '泻肺散', 'XBS,XFS(泻肺散)', '【功用】清泻肺热，平喘止咳。
【主治】肺热喘咳证。气喘咳嗽，皮肤蒸热，日晡尤甚，舌红苔黄，脉细数。
【用法及注意】
【歌诀】:
泻白桑皮地骨皮， \n甘草梗米四般宜， \n参茯知苓皆可入， \n肺热喘嗽此方施。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('49', '苇茎汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'WJT', '【功用】清肺化痰，逐瘀排脓。
【主治】肺痈。身有微热，咳嗽痰多，甚则咳吐腥臭脓血，胸中隐隐作痛，舌红苔黄腻，脉滑数。
【用法及注意】
【歌诀】:
苇茎汤方出千金， \n桃仁薏苡冬瓜仁， \n肺痈痰热兼脓血， \n化浊排脓病自宁。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('50', '清胃散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'QWS', '【功用】清胃凉血。
【主治】胃火牙痛。牙痛牵引头疼，面颊发热，其齿喜冷恶热；或牙宣出血；或牙龈红肿溃烂；或唇舌颊腮肿痛；口气热臭，口干舌燥，舌红苔黄，脉滑数。
【用法及注意】
【歌诀】:
清胃散有升麻连， \n当归生地牡丹全， \n或加石膏清胃热， \n口疮吐衄与牙宣。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('51', '玉女煎', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'YNJ,YLJ', '【功用】清胃热，滋肾阴。
【主治】胃热阴虚证。头痛，牙痛，齿松牙衄，烦热干渴，舌红苔黄而干。亦治消渴，消谷善饥等。
【用法及注意】
【歌诀】:
玉女煎用熟地黄， \n膏知牛膝麦冬襄， \n胃火阴虚相因病， \n牙痛齿枯宜煎尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('52', '芍药汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'SYT', '【功用】清热燥湿，调气和血。
【主治】湿热痢疾。腹痛，便脓血，赤白相兼，里急后重，肛门灼热，小便短赤，舌苔黄腻，脉弦数。
【用法及注意】
【歌诀】:
芍药汤中用大黄， \n苓连归桂槟草香， \n清热燥湿调气血， \n里急腹痛自安康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('53', '白头翁汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清脏腑热'), '', 'BTWT', '【功用】清热解毒，凉血止痢。
【主治】热毒痢疾。腹痛，里急后重，肛门灼热，下痢脓血，赤多白少，渴欲饮水，舌红苔黄，脉弦数。
【用法及注意】
【歌诀】:
白头翁汤治热痢， \n黄连黄柏与秦皮， \n味苦性寒能凉血， \n解毒坚阴功效奇。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('54', '六一散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛暑'), '', 'LYS', '【功用】清暑利湿。
【主治】暑湿证。身热烦渴，小便不利，或泄泻。
【用法及注意】
【歌诀】:
六一散用滑石草， \n清暑利湿有功效， \n益元碧玉与鸡苏， \n砂黛薄荷加之好。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('55', '桂苓甘露饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛暑'), '', 'GLGLY', '【功用】清暑解热，化气利湿。
【主治】暑湿证。发热头痛，烦渴引饮，小便不利，及霍乱吐下。
【用法及注意】
【歌诀】:
桂苓甘露猪苓膏， \n术泽寒水滑石草， \n祛暑清热以利湿， \n发热烦渴吐泻消。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('56', '清暑益气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛暑'), '', 'QSYQT', '【功用】清暑益气，养阴生津。
【主治】暑热气津两伤证。身热汗多，口渴心烦，小便短赤，体倦少气，精神不振，脉虚数。
【用法及注意】
【歌诀】:
王氏清暑益气汤， \n善治中暑气阴伤， \n洋参冬斛荷瓜翠， \n连竹知母甘梗襄。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('57', '青蒿鳖甲汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清虚热'), '', 'QHBJT', '【功用】养阴透热。
【主治】温病后期，邪伏阴分证。夜热早凉，热退无汗，舌红苔少，脉细数。
【用法及注意】
【歌诀】:
青蒿鳖甲地知丹， \n热自阴来仔细辨， \n夜热早凉无汗出， \n养阴透热服之安。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('58', '清骨散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清虚热'), '', 'QGS', '【功用】清虚热，退骨蒸。
【主治】虚劳发热。骨蒸潮热，或低热日久不退，形体消瘦，唇红颧赤，困倦盗汗，或口渴心烦，舌红少苔，脉细数等。
【用法及注意】
【歌诀】:
清骨散用银柴胡， \n胡连秦艽鳖甲扶， \n地骨青蒿知母草， \n骨蒸劳热保无虞。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('59', '当归六黄汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清虚热'), '', 'DGLHT', '【功用】滋阴泻火，固表止汗。
【主治】阴虚火旺盗汗。发热盗汗，面赤心烦，口干唇燥，大便干结，小便黄赤，舌红苔黄，脉数。
【用法及注意】
【歌诀】:
当归六黄二地黄， \n芩连芪柏共煎尝， \n滋阴泻火兼固表， \n阴虚火旺盗汗良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('60', '理中丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温中祛寒'), '', 'WZW', '【功用】温中散寒，补气健脾。
【主治】脾胃虚寒证。脘腹疼痛，喜温欲按，自利不渴，畏寒肢冷，呕吐，不欲饮食，舌淡苔白，脉沉细；或阳虚失血；或小儿慢惊；或病后喜唾涎沫；或霍乱吐泻；或胸痹等中焦虚寒。
【用法及注意】
【歌诀】:
理中丸主理中乡， \n甘草人参术干姜， \n呕利腹痛阴寒盛， \n或加附子总扶阳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('61', '小建中汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温中祛寒'), '', 'XJZT', '【功用】温中补虚，和里缓急。
【主治】虚劳里急证。腹中时痛，喜温欲按，舌淡苔白，脉细弦；或虚劳而心中悸动，虚烦不宁，面色无华，或手足烦热，咽干口燥等。
【用法及注意】
【歌诀】:
小建中汤芍药多， \n桂姜甘草大枣和， \n更加饴糖补中脏， \n虚劳腹冷服之瘥。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('62', '吴茱萸汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温中祛寒'), '', 'WZYT', '【功用】温中补虚，降逆止呕。
【主治】虚寒呕吐。食谷欲呕，畏寒喜热，或胃脘痛，吞酸嘈杂；或厥阴头痛，干呕吐涎沫；或少阴吐利，手足逆冷，烦躁欲死。
【用法及注意】
【歌诀】:
吴茱萸汤人参枣， \n重用生姜温胃好， \n阳明寒呕少阴利， \n厥阴头痛皆能保。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('63', '四逆汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__回阳救逆'), '', 'SNT,SLT', '【功用】回阳救逆。
【主治】少阴病。四肢厥冷，恶寒踡卧，呕吐不渴，腹痛下利，神衰欲寐，舌苔白滑，脉微；或太阳病误汗亡阳。
【用法及注意】
【歌诀】:
四逆汤中附草姜， \n四肢厥冷急煎尝， \n腹痛吐泻脉微细， \n急投此方可回阳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('64', '回阳救急汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__回阳救逆'), '', 'HYJJT', '【功用】回阳救急，益气生脉。
【主治】寒邪直中三阴，真阳衰微证。恶寒踡卧，四肢厥冷，吐泻腹痛，口不渴，神衰欲寐，或身寒战慄，或指甲口唇青紫，或吐涎沫，舌淡苔白，脉沉微，甚或无脉等。
【用法及注意】
【歌诀】:
回阳救急用六君， \n桂附干姜五味寻， \n加麝三厘或胆汁， \n三阴寒厥建奇勋。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('65', '当归四逆汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温经散寒'), '', 'DGSNT,DGSLT', '【功用】温经散寒，养血通脉。
【主治】血虚寒厥证。手足厥寒，口不渴，或腰、股、腿、足疼痛，舌淡苔白，脉沉细或细而欲绝。
【用法及注意】
【歌诀】:
当归四逆桂芍枣， \n细辛甘草与通草， \n血虚肝寒手足冷， \n煎服此方乐陶陶。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('66', '黄芪桂枝五物汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温经散寒'), '', 'HQGZWWT', '【功用】益气温经，和血通痹。
【主治】血痹。肌肤麻木不仁，脉微涩而紧。
【用法及注意】
【歌诀】:
黄芪桂枝五物汤， \n芍药大枣与生姜， \n益气温经和营卫， \n血痹风痹功效良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('67', '阳和汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温经散寒'), '', 'YHT', '【功用】温阳补血，散寒通滞。
【主治】阴疽。漫肿无头，皮色不变，痠痛无热，口中不渴，舌淡苔白，脉沉细或迟细。或贴骨疽、脱疽、流注、痰核、鹤膝风等属于阴寒证者。
【用法及注意】
【歌诀】:
阳和汤法解寒凝， \n贴骨流注鹤膝风， \n熟地鹿角姜炭桂， \n麻黄白芥甘草从。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('68', '四君子汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'SJZT', '【功用】益气健脾。
【主治】脾胃气虚证。面色恍白，语音低微，气短乏力，食少便溏，舌淡苔白，脉虚弱。
【用法及注意】
【歌诀】:
四君子汤中和义， \n参术茯苓甘草比， \n益以夏陈名六君， \n祛痰补益气虚饵， \n除却半夏名异功， \n或加香砂气滞使。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('69', '参苓白术散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'CLBSS', '【功用】益气健脾，渗湿止泻。
【主治】脾虚夹湿证。饮食不化，胸脘痞闷，肠鸣泄泻，四肢乏力，形体消瘦，面色萎黄，舌淡苔白腻，脉虚缓。
【用法及注意】
【歌诀】:
参苓白术扁豆陈， \n山药甘莲砂薏仁， \n桔梗上浮兼保肺， \n枣汤调服益脾神。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('70', '补中益气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'BZYQT', '【功用】补中益气，升阳举陷。
【主治】（1）脾胃气虚证。饮食减少，体倦肢软，少气懒言，面色白光白，大便稀溏，脉大而虚软。 \n（2）气虚下陷证。脱肛，子宫脱垂，久泻，久痢，崩漏等，气短乏力，舌淡，脉虚者。 \n（3）气虚发热证。身热，自汗，渴喜热饮，气短乏力，舌淡，脉虚大无力。
【用法及注意】
【歌诀】:
补中益气芪术陈， \n升柴参草当归身， \n虚劳内伤功独擅， \n亦治阳虚外感因。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('71', '玉屏风散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'YPFS', '【功用】益气固表止汗。
【主治】表虚自汗。汗出恶风，面色白光白，舌淡苔薄白，脉浮虚。亦治虚人腠理不固，易于感冒。
【用法及注意】
【歌诀】:
玉屏风散最有灵， \n芪术防风鼎足形， \n表虚汗多易感冒， \n药虽相畏效相成。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('72', '生脉散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'SMS', '【功用】益气生津，敛阴止汗。
【主治】（1）温热、暑热，耗气伤阴证。汗多神疲，体倦乏力，气短懒言，咽干口渴，舌干红少苔，脉虚数。 \n（2）久咳肺虚，气阴两虚证。干咳少痰，短气自汗，口干舌燥，脉虚细。
【用法及注意】
【歌诀】:
生脉麦味与人参， \n保肺清心治暑淫， \n气少汗多兼口渴， \n病危脉绝急煎斟。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('73', '人参蛤蚧散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补气'), '', 'RSHGS', '【功用】补肺益肾，止咳定喘。
【主治】肺肾气虚喘息、咳嗽。痰稠色黄，或咳吐脓血，胸中烦热，身体羸瘦，或遍身浮肿，脉浮虚。
【用法及注意】
【歌诀】:
补益人参蛤蚧散， \n专治痰血与咳喘， \n桑皮二母杏苓草， \n若非虚热慎毋餐。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('74', '四物汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补血'), '', 'SWT', '【功用】补血和血。
【主治】营血虚滞证。心悸失眠，头晕目眩，面色无华，妇人月经不调，量少或经闭不行，脐腹作痛，舌淡，脉细弦或细涩。
【用法及注意】
【歌诀】:
四物地芍与归芎， \n血家百病此方通， \n经带胎产俱可治， \n加减运用在胸中。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('75', '当归补血汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补血'), '', 'DGBXT', '【功用】补气生血。
【主治】血虚发热证。肌热面红，烦渴欲饮，脉洪大而虚，重按无力。亦治妇人经期、产后血虚发热头痛，或疮痒溃后，久不愈合者。
【用法及注意】
【歌诀】:
当归补血东垣笺， \n黄芪一两归二钱， \n血虚发热口烦渴， \n脉大而虚宜此煎。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('76', '归脾汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补血'), '', 'GPT', '【功用】益气补血，健脾养心。
【主治】（1）心脾气血两虚证。心悸怔忡，健忘失眠，盗汗虚热，体倦食少，面色萎黄，舌淡，苔薄白，脉虚弱。 \n（2）脾不统血证。便血，皮下紫癜，妇女崩漏，月经超前，量多色淡，或淋漓不止，舌淡，脉细者。
【用法及注意】
【歌诀】:
归脾汤用术参芪， \n归草茯神远志随， \n酸枣木香龙眼肉， \n煎加姜枣益心脾， \n怔忡健忘俱可却， \n肠风崩漏总能医。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('77', '八珍汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__气血双补'), '', 'BZT', '【功用】益气补血。
【主治】气血两虚证。面色苍白或萎黄，头晕目眩，四肢倦怠，气短懒言，心悸怔忡，饮食减少，舌淡苔薄白，脉细弱或虚大无力。
【用法及注意】
【歌诀】:
双补气血八珍汤， \n四君四物合成方， \n煎加姜枣调营卫， \n气血亏虚服之康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('78', '泰山磐石散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__气血双补'), '', 'TSPSS', '【功用】益气健脾，养血安胎。
【主治】堕胎、滑胎。胎动不安，或屡有堕胎宿疾，面色淡白，倦怠乏力，不思饮食，舌淡苔薄白，脉滑无力。
【用法及注意】
【歌诀】:
泰山磐石八珍全， \n去苓加芪芩断联， \n再益砂仁及糯米， \n妇人胎动可安全。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('79', '六味地黄丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '', 'LWDHW', '【功用】滋阴补肾。
【主治】肾阴虚证。腰膝痠软，头晕目眩，耳聋耳鸣，盗汗，遗精，消渴，骨蒸潮热，手足心热，舌燥咽痛，牙齿动摇，足跟作痛，小便淋漓，以及小儿囟门不合，舌红少苔，脉沉细数。
【用法及注意】
【歌诀】:
六味地黄益肾肝， \n茱薯丹泽地苓专， \n更加知柏成八味， \n阴虚火旺自可煎。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('80', '左归丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '', 'ZGW', '【功用】滋阴补肾，填精益髓。
【主治】真阴不足证。头目眩晕，腰痠腿软，遗精滑泄，自汗盗汗，口燥舌干，舌红少苔，脉细。
【用法及注意】
【歌诀】:
左归丸内山药地， \n萸肉枸杞与牛膝， \n菟丝龟鹿二胶合， \n壮水之主方第一。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('81', '大补阴丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '大补丸', 'DBYW,DBW(大补丸)', '【功用】滋阴降火。
【主治】阴虚火旺证。骨蒸潮热，盗汗遗精，咳嗽咯血，心烦易怒，足膝疼热，舌红少苔，尺脉数而有力。
【用法及注意】
【歌诀】:
大补阴丸知柏黄， \n龟版脊髓蜜成方， \n咳嗽咯血骨蒸热， \n阴虚火旺制亢阳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('82', '炙甘草汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '复脉汤', 'ZGCT,FMT(复脉汤)', '【功用】滋阴养血，益气温阳，复脉止悸。
【主治】（1）阴血不足，阳气虚弱证。脉结代，心动悸，虚羸少气，舌光少苔，或质干而瘦小者。 \n（2）虚劳肺痿。咳嗽，涎唾多，形瘦短气，虚烦不眠，自汗盗汗，咽干舌燥，大便干结，脉虚数。
【用法及注意】
【歌诀】:
炙甘草汤参姜桂， \n麦冬生地大麻仁， \n大枣阿胶加酒服， \n虚劳肺痿效如神。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('83', '一贯煎', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '', 'YGJ', '【功用】滋阴疏肝。
【主治】肝肾阴虚，肝气不舒证。胸脘胁痛，吞酸吐苦，咽干口燥，舌红少津，脉细弱或虚弦。亦治疝气瘕聚。
【用法及注意】
【歌诀】:
一贯煎中用地黄， \n沙参杞子麦冬襄， \n当归川楝水煎服， \n阴虚肝郁是妙方。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('84', '百合固金汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '', 'BHGJT', '【功用】滋肾保肺，止咳化痰。
【主治】肺肾阴亏，虚火上炎证。咳嗽气喘，痰中带血，咽喉燥痛，头晕目眩，午后潮热，舌红少苔，脉细数。
【用法及注意】
【歌诀】:
百合固金二地黄， \n玄参贝母桔甘藏， \n麦冬芍药当归配， \n喘咳痰血肺家伤。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('85', '补肺阿胶汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '阿胶散,补肺散', 'BJAJT,AJS(阿胶散),BFS(补肺散)', '【功用】养阴补肺，清热止血。
【主治】小儿肺虚有热证。咳嗽气喘，咽喉干燥，咯痰不多，或痰中带血，舌红少苔，脉细数。
【用法及注意】
【歌诀】:
补肺阿胶马兜铃， \n鼠粘甘草杏糯停， \n肺虚火盛人当服， \n顺气生津嗽哽宁。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('86', '益胃汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阴'), '', 'YWT', '【功用】养阴益胃。
【主治】阳明温病，胃阴损伤证。不能食，口干咽燥，舌红少苔，脉细数者。
【用法及注意】
【歌诀】:
益胃汤能养胃阴， \n冰糖玉竹与沙参， \n麦冬生地同煎服， \n温病须虑热伤津.
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('87', '肾气丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阳'), '', 'SQW', '【功用】补肾助阳。
【主治】肾阳不足证。腰痛脚软，身半以下常有冷感，少腹拘急，小便不利，或小便反多，入夜尤甚，阳痿早泄，舌淡而胖，脉虚弱，尺部沉细，以及痰饮，水肿，消渴，脚气，转胞等。
【用法及注意】
【歌诀】:
金匮肾气治肾虚， \n熟地淮药及山萸， \n丹皮苓泽加桂附， \n引火归原热下趋。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('88', '右归丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补阳'), '', 'YGW', '【功用】温补肾阳，填精益髓。
【主治】肾阳不足，命门火衰证。年老或久病气衰神疲，畏寒肢冷，腰膝软弱，阳痿遗精，或阳衰无子，或饮食减少，大便不实，或小便自遗，舌淡苔白，脉沉而迟。
【用法及注意】
【歌诀】:
右归丸中地附桂， \n山药茱萸菟丝归， \n杜仲鹿胶枸杞子， \n益火之源此方魁。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('89', '地黄饮子', (select PK_ID from SYNDROME_SUBJECT where NAME = '__阴阳并补'), '', 'DHYZ', '【功用】滋肾阴，补肾阳，开窍化痰。
【主治】喑痱。舌强不能言，足废不能用，口干不欲饮，足冷面赤，脉沉细弱。
【用法及注意】
【歌诀】:
地黄饮子山茱斛， \n麦味菖蒲远志茯， \n苁蓉桂附巴戟天， \n少入薄荷姜枣服。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('90', '龟鹿二仙胶', (select PK_ID from SYNDROME_SUBJECT where NAME = '__阴阳并补'), '', 'GLEXJ', '【功用】滋阴填精，益气壮阳。
【主治】真元虚损，精血不足证。全身瘦削，阳痿遗精，两目昏花，腰膝痠软，久不孕育。
【用法及注意】
【歌诀】:
龟鹿二仙最守真， \n补人三宝精气神， \n人参枸杞和龟鹿， \n益寿延年实可珍。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('91', '七宝美髯丹', (select PK_ID from SYNDROME_SUBJECT where NAME = '__阴阳并补'), '', 'QBMRD', '【功用】补益肝肾，乌发壮骨。
【主治】肝肾不足证。须发早白，脱发，齿牙动摇，腰膝痠软，梦遗滑精，肾虚不育等。
【用法及注意】
【歌诀】:
七宝美髯何首乌， \n菟丝牛膝茯苓俱， \n骨脂枸杞当归合， \n专益肝肾精血虚。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('92', '牡蛎散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__固表止汗'), '', 'MLS', '【功用】益气固表，敛阴止汗。
【主治】自汗，盗汗。常自汗出，夜卧更甚，心悸惊惕，短气烦倦，舌淡红，脉细弱。
【用法及注意】
【歌诀】:
牡蜘散内用黄芪, \n小麦麻根合用宜, \n卫虚自汗或盗汗, \n固表收敛见效奇。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('93', '九仙散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__敛肺止咳'), '', 'JXS', '【功用】敛肺止咳，益气养阴。
【主治】久咳肺虚证。久咳不已，咳甚则气喘自汗，痰少而粘，脉虚数。
【用法及注意】
【歌诀】:
九仙散中罂粟君, \n五味乌梅共为臣, \n参胶款桑贝桔梗, \n敛肺止咳益气阴。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('94', '真人养脏汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩肠固脱'), '', 'ZRYZT', '【功用】涩肠止泻，温中补虚。
【主治】久泻久痢。泻痢无度，滑脱不禁，甚至脱肛坠下，脐腹疼痛，不思饮食，舌淡苔白，脉迟细。
【用法及注意】
【歌诀】:
真人养脏诃粟壳, \n肉寇当归桂木香, \n术芍参甘为涩剂, \n脱肛久痢早煎尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('95', '四神丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩肠固脱'), '', 'SSW', '【功用】温肾暖脾，固肠止泻。
【主治】肾泄。五更泄泻，不思饮食，食不消化，或腹痛肢冷，神疲乏力，舌淡，苔薄白，脉沉迟无力。
【用法及注意】
【歌诀】:
四神故纸与吴萸, \n肉寇五味四般须, \n大枣生姜为丸服, \n五更肾泄最相宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('96', '桃花汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩肠固脱'), '', 'THT', '【功用】温中涩肠止痢。
【主治】虚寒痢。下痢不止，便脓血，色黯不鲜，日久不愈，腹痛喜温喜按，舌淡苔白，脉迟弱或微细。
【用法及注意】
【歌诀】:
桃花汤中赤石脂, \n干姜粳米共用之, \n虚寒下痢便脓血, \n温涩止痢最宜施。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('97', '金锁固精丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩精止遗'), '', 'JSGJW', '【功用】补肾涩精。
【主治】遗精。遗精滑泄，神疲乏力，腰痛耳鸣，舌淡苔白，脉细弱。
【用法及注意】
【歌诀】:
金锁固精芡莲须， \n蒺藜龙骨与牡蛎， \n莲粉糊丸盐汤下， \n补肾涩精止滑遗。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('98', '桑螵蛸散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩精止遗'), '', 'SPQS', '【功用】调补心肾，涩精止遗。
【主治】心肾两虚证。小便频数，或尿如米泔色，或遗尿遗精，心神恍惚，健忘，舌淡苔白，脉细弱。
【用法及注意】
【歌诀】:
桑螺蛸散治便数, \n参苓龙骨同龟壳, \n菖蒲远志当归入, \n补肾宁心健忘却。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('99', '缩泉丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__涩精止遗'), '', 'SQW', '【功用】温肾祛寒，缩尿止遗。
【主治】膀胱虚寒证。小便频数，或遗尿不止，舌淡，脉沉弱。
【用法及注意】
【歌诀】:
缩泉丸治小便频, \n膀胱虚寒遗尿斟, \n乌药益智各等分, \n山药糊丸效更珍。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('100', '固冲汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__固崩止带'), '', 'GCT', '【功用】益气健脾，固冲摄血。
【主治】脾气虚弱，冲脉不固证。血崩或月经过多，色淡质稀，心悸气短，腰膝痠软，舌淡，脉微弱者。
【用法及注意】
【歌诀】:
固冲汤中用术芪, \n龙牡芍萸茜草施, \n倍子海蛸棕榈炭, \n崩中漏下总能医。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('101', '固经丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__固崩止带'), '', 'GJW', '【功用】滋阴清热，固经止血。
【主治】崩漏。经水过期不止，或下血量过多，血色深红或紫黑稠粘，手足心热，腰膝痠软，舌红，脉弦数。
【用法及注意】
【歌诀】:
固经丸用龟版君, \n黄柏椿皮香附群, \n黄芩芍药酒丸服, \n漏下崩中色黑殷。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('102', '易黄汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__固崩止带'), '', 'YHT', '【功用】补肾清热，祛湿止带。
【主治】湿热带下。带下色黄，其气腥秽，舌红，苔黄腻者。
【用法及注意】
【歌诀】:
易黄白果与芡实， \n车前黄柏加薯蓣， \n能消带下粘稠秽， \n补肾清热又祛湿。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('103', '朱砂安神丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__重镇安神'), '', 'ZSASW', '【功用】重镇安神，清心泻火。
【主治】心火亢盛，阴血不足证。失眠多梦，惊悸怔忡，心烦神乱，舌红，脉细数。
【用法及注意】
【歌诀】:
朱砂安神东垣方, \n归连甘草合地黄, \n怔忡不寐心烦乱, \n清热养阴可复康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('104', '磁朱丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__重镇安神'), '', 'CZW', '【功用】益阴明目，重镇安神。
【主治】心肾不交证。视物昏花，耳鸣耳聋，心悸失眠，亦治癫痫。
【用法及注意】
【歌诀】:
磁朱丸中有神曲, \n安神潜阳治目疾, \n心悸失眠皆可用, \n癫狂痫证宜服之。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('105', '天王补心丹', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补养安神'), '', 'TWBXD', '【功用】滋阴养血，补心安神。
【主治】阴虚血少，神志不安证。心悸失眠，虚烦神疲，梦遗健忘，手足心热，口舌生疮，舌红少苔，脉细而数。
【用法及注意】
【歌诀】:
补心丹用柏枣仁, \n二冬生地当归身, \n三参桔梗朱砂味, \n远志茯苓共养神。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('106', '酸枣仁汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补养安神'), '', 'SZRT', '【功用】养血安神，清热除烦。
【主治】虚烦不眠证。失眠心悸，虚烦不安，头目眩晕，咽干口燥，舌红，脉弦细。
【用法及注意】
【歌诀】:
酸枣二升先煮汤, \n茯知二两用之良, \n芎二甘一相调剂， \n服后安然入梦乡。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('107', '甘麦大枣汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__补养安神'), '', 'GMDZT', '【功用】养心安神，和中缓急。
【主治】脏躁。精神恍惚，常悲伤欲哭，不能自主，心中烦乱，睡眠不安，甚则言行失常，呵欠频作，舌淡红苔少，脉细微数。
【用法及注意】
【歌诀】:
金匮甘麦大枣汤, \n妇人脏躁喜悲伤, \n精神恍惚常欲哭, \n养心安神效力彰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('108', '安宫牛黄丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__凉开'), '', 'AGNHW,AGLHW', '【功用】清热开窍，豁痰解毒。
【主治】邪热内陷心包证。高热烦躁，神昏谵语，口干舌燥，痰涎壅盛，舌红或绛，脉数。亦治中风昏迷，小儿惊厥，属邪热内闭者。
【用法及注意】
【歌诀】:
安宫牛黄开窍方， \n芩连栀郁朱雄黄， \n牛角珍珠冰麝箔， \n热闭心包功效良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('109', '行军散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__凉开'), '', 'XJS', '【功用】清热开窍，辟秽解毒。
【主治】暑秽。吐泻腹痛，烦闷欲绝，头目昏晕，不省人事。并治口疮咽痛。点目去风热障翳；搐鼻可避时疫之气。
【用法及注意】
【歌诀】:
诸葛行军痧胀方， \n珍珠牛麝冰雄黄， \n硼硝金箔共研末， \n窍闭神昏服之康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('110', '苏合香丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温开'), '', 'SHXW', '【功用】芳香开窍，行气温中。
【主治】寒闭证。突然昏倒，牙关紧闭，不省人事，苔白，脉迟；心腹卒痛，甚则昏厥。亦治中风、中气及感受时行瘴疠之气，属于寒闭证者。
【用法及注意】
【歌诀】:
苏合香丸麝息香， \n木丁朱乳荜檀襄， \n牛冰术沉诃香附， \n中恶救急莫彷徨。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('111', '紫金锭', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温开'), '玉枢丹', 'ZJD,YSD(玉枢丹)', '【功用】化痰开窍，辟秽解毒，消肿止痛。
【主治】（1）中暑时疫。脘腹胀闷疼痛，恶心呕吐，泄泻，及小儿痰厥。 \n（2）外敷疔疮疖肿，虫咬损伤，无名肿毒，以及痄腮、丹毒、喉风等。
【用法及注意】
【歌诀】:
紫金锭用麝朱雄， \n慈戟千金五倍同， \n太乙玉枢名又别， \n祛痰逐秽及惊风。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('112', '越鞠丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '芎术丸', 'YJW,XSW(芎术丸)', '【功用】行气解郁。
【主治】郁证。胸膈痞闷，脘腹胀痛，嗳腐吞酸，恶心呕吐，饮食不消等。
【用法及注意】
【歌诀】:
越橘丸治六般郁， \n气血痰火食湿因， \n芎苍香附兼栀曲， \n气畅郁舒痛闷伸。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('113', '柴胡疏肝散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'CHSGS', '【功用】疏肝解郁，行气止痛。
【主治】肝气郁滞证。胁肋疼痛，或寒热往来，嗳气太息，脘腹胀满，脉弦。
【用法及注意】
【歌诀】:
柴胡疏肝芍川芎， \n枳壳陈皮草香附， \n疏肝行气兼活血， \n胁肋疼痛皆能除。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('114', '四磨汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'SMT', '【功用】行气降逆，宽胸散结。
【主治】肝气郁结证。胸膈胀闷，上气喘急，心下痞满，不思饮食。
【用法及注意】
【歌诀】:
四磨饮子七情侵， \n人参乌药及槟沉， \n浓磨煎服调滞气， \n实者枳壳易人参。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('115', '瓜蒌薤白白酒汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'GWJBBJT', '【功用】通阳散结，行气祛痰。
【主治】胸痹。胸中闷痛，甚至胸痛彻背，喘息咳唾，短气，舌苔白腻，脉弦紧。
【用法及注意】
【歌诀】:
瓜蒌薤白白酒汤， \n胸痹胸闷痛难当， \n喘息短气时咳唾， \n难卧仍加半夏良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('116', '半夏厚朴汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'BXHPT', '【功用】行气散结，降逆化痰。
【主治】梅核气。咽中如有物阻，咯吐不出，吞咽不下，胸膈满闷，或咳或呕，舌苔白润或白腻，脉弦缓或弦滑。
【用法及注意】
【歌诀】:
半夏厚朴痰气疏， \n茯苓生姜共紫苏， \n加枣同煎名四七， \n痰凝气滞皆能除。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('117', '枳实消痞丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '失笑丸', 'ZSXPW,SXW(失笑丸)', '【功用】行气消痞，健脾和胃。
【主治】脾虚气滞，寒热互结证。心下痞满，不欲饮食，倦怠乏力，大便失调。
【用法及注意】
【歌诀】:
枳实消痞四君全， \n麦芽夏曲朴姜连， \n蒸饼糊丸消积满， \n消中有补两相兼。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('118', '厚朴温中汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'HPWZT', '【功用】行气温中，燥湿除满。
【主治】寒湿气滞证。脘腹胀满或疼痛，不思饮食，舌苔白腻，脉沉弦。
【用法及注意】
【歌诀】:
厚朴温中陈草苓， \n干姜草寇木香停， \n煎服加姜治腹痛， \n虚寒胀满用皆灵。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('119', '良附丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'LFW', '【功用】行气疏肝，祛寒止痛。
【主治】气滞寒凝证。胃脘疼痛，胸闷胁痛，畏寒喜热，以及妇女痛经等。
【用法及注意】
【歌诀】:
良附丸用醋香附， \n良姜酒洗加盐服， \n米饮姜汁同调下， \n心脘胁痛一齐除。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('120', '橘核丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'JHW', '【功用】行气止痛，软坚散结。
【主治】癞疝，睾丸肿胀偏坠，或坚硬如石，或痛引脐腹。
【用法及注意】
【歌诀】:
橘核丸中川楝桂， \n朴实延胡藻带昆， \n桃仁二木酒糊丸， \n癞疝痛丸盐酒吞。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('121', '暖肝煎', (select PK_ID from SYNDROME_SUBJECT where NAME = '__行气'), '', 'RGJ', '【功用】温补肝肾，行气止痛。
【主治】肝肾虚寒证。睾丸冷痛，或小腹疼痛，畏寒喜暖，舌淡苔白，脉沉迟。
【用法及注意】
【歌诀】:
暖肝煎中杞茯归， \n茴沉乌药合肉桂， \n下焦虚寒疝气痛， \n温补肝肾此方推。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('122', '苏子降气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__降气'), '', 'SZJQT,SZXQT', '【功用】降气平喘，祛痰止咳。
【主治】实喘。痰涎壅盛，喘咳短气，胸膈满闷，或腰疼脚软，或肢体浮肿，舌苔白滑或白腻，脉弦滑。
【用法及注意】
【歌诀】:
苏子降气半夏归， \n前胡桂朴草姜随， \n上实下虚痰嗽喘， \n或加沉香去肉桂。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('123', '定喘汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__降气'), '', 'DCT', '【功用】宣肺降气，清热化痰。
【主治】哮喘。咳嗽痰多气急，痰稠色黄，微恶风寒，舌苔黄腻，脉滑数。
【用法及注意】
【歌诀】:
定喘白果与麻黄， \n款冬半夏白皮桑， \n苏杏黄芩兼甘草， \n外寒痰热喘哮尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('124', '旋覆代赭汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__降气'), '', 'XFDZT', '【功用】降逆化痰，益气和胃。
【主治】胃气虚弱，痰浊内阻证。心下痞鞕，噫气不除，或反胃呕逆，吐涎沫，舌淡，苔白滑，脉弦而虚。
【用法及注意】
【歌诀】:
旋覆代赭用人参， \n半夏姜甘大枣临， \n重以镇逆咸软痞， \n痞鞕噫气力能禁。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('125', '橘皮竹茹汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__降气'), '', 'JPZRT', '【功用】降逆止呃，益气清热。
【主治】胃虚有热之呃逆。呃逆或干呕，舌红嫩，脉虚数。
【用法及注意】
【歌诀】:
橘皮竹茹治呕逆， \n人参甘草枣姜益， \n胃虚有热失和降， \n久病之后更相宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('126', '丁香柿蒂汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__降气'), '', 'DXSDT', '【功用】温中益气、降逆止呃。
【主治】虚寒呃逆。呃逆不已，胸脘痞闷，舌淡苔白，脉沉迟。
【用法及注意】
【歌诀】:
丁香柿蒂人参姜， \n呃逆因寒中气伤， \n温中降逆又益气， \n胃气虚寒最相当。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('127', '桃核承气汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'THCQT', '【功用】破血下瘀。
【主治】下焦蓄血证。少腹急结，小便自利，甚则谵语烦躁，其人如狂，至夜发热。以及血瘀经闭，痛经，脉沉实而涩等。
【用法及注意】
【歌诀】:
桃核承气五般施， \n甘草硝黄并桂枝， \n瘀热互结小腹胀， \n如狂蓄血功最奇。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('128', '血府逐瘀汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'XFZYT', '【功用】活血祛瘀，行气止痛。
【主治】胸中血瘀证。胸痛，头痛日久，痛如针刺而有定处，或呃逆日久不止，或内热烦闷，或心悸失眠，急燥易怒，入暮潮热，唇暗或两目暗黑，舌黯红或有瘀斑，脉涩或弦紧。
【用法及注意】
【歌诀】:
血府当归生地桃， \n红花甘草壳赤芍， \n柴胡芎桔牛膝等， \n血化下行不作劳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('129', '补阳还五汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'BYHWT', '【功用】补气活血通络。
【主治】中风。半身不遂，口眼喎斜，语言蹇涩，口角流涎，小便频数或遗尿不禁，舌黯淡，苔白，脉缓。
【用法及注意】
【歌诀】:
补阳还五赤芍芎， \n归尾通经佐地龙， \n四两黄芪为主药， \n血中瘀滞用桃红。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('130', '复元活血汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'FYHXT', '【功用】活血祛瘀，疏肝通络。
【主治】跌打损伤。瘀血留于胁下，痛不可忍。
【用法及注意】
【歌诀】:
复元活血汤柴胡， \n花粉当归山甲俱， \n桃仁红花大黄草， \n损伤瘀血酒煎去。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('131', '温经汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'WJT', '【功用】温经散寒，祛瘀养血。
【主治】冲任虚寒，瘀血阻滞证。漏下不止，月经不调，或前或后，或一月再行，或经停不至，而见入暮发热，手心烦热，唇口干燥。亦治妇人久不受孕。
【用法及注意】
【歌诀】:
温经汤用吴萸芎， \n归芍丹桂姜夏冬， \n参草益脾胶养血， \n调经重在暖胞宫。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('132', '生化汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'SHT', '【功用】化瘀生新，温经止血。
【主治】产后瘀血腹痛。恶露不行，小腹冷痛。
【用法及注意】
【歌诀】:
生化汤是产后方， \n归芎桃草酒炮姜， \n消瘀活血功偏擅， \n止痛温经效亦彰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('133', '桂枝茯苓丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'GZFLW', '【功用】活血化瘀，缓消癥块。
【主治】瘀阻胞宫证。腹痛拒按，或漏下不止，血色紫黑晦暗，或妊娠胎动不安等。
【用法及注意】
【歌诀】:
金匮桂枝茯苓丸， \n桃仁芍药和牡丹， \n等分为末蜜丸服， \n缓消癥块胎可安。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('134', '失笑散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'SXS', '【功用】活血祛瘀，散结止痛。
【主治】瘀血停滞。心胸刺痛，脘腹疼痛，或产后恶露不行，或月经不调，少腹急痛等。
【用法及注意】
【歌诀】:
失笑灵脂蒲黄同， \n等量为散酽醋冲， \n瘀滞心腹时作痛， \n祛瘀止痛有奇功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('135', '丹参饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__活血祛瘀'), '', 'DSY', '【功用】活血祛瘀，行气止痛。
【主治】血瘀气滞，心胃诸痛。
【用法及注意】
【歌诀】:
丹参饮中用檀香， \n砂仁合用成妙方， \n血瘀气滞两相结， \n心胃诸痛用之良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('136', '四生丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__止血'), '', 'SSW', '【功用】凉血止血。
【主治】血热妄行。吐血、衄血，血色鲜红，口干咽燥，舌红或绛，脉弦数。
【用法及注意】
【歌诀】:
四生丸中三般叶， \n侧柏艾叶荷叶兼， \n生地合用为丸服， \n血热吐衄效可验。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('137', '咳血方', (select PK_ID from SYNDROME_SUBJECT where NAME = '__止血'), '', 'KXF', '【功用】清肝宁肺，凉血止血。
【主治】肝火犯肺之咳血证。咳嗽痰稠带血，咯吐不爽，心烦易怒，胸胁作痛，咽干口苦，颊赤便秘，舌红苔黄，脉弦数。
【用法及注意】
【歌诀】:
咳血方中诃子收， \n瓜蒌海粉山栀投， \n青黛蜜丸口噙化， \n咳嗽痰血服之瘳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('138', '小蓟饮子', (select PK_ID from SYNDROME_SUBJECT where NAME = '__止血'), '', 'XJYZ', '【功用】凉血止血，利水通淋。
【主治】血淋、尿血。尿中带血，小便频数，赤涩热痛，舌红，脉数等。
【用法及注意】
【歌诀】:
小蓟饮子藕蒲黄， \n木通滑石生地黄， \n归草黑栀淡竹叶， \n血淋热结服之良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('139', '槐花散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__止血'), '', 'HHS', '【功用】清肠凉血，疏风行气。
【主治】肠风脏毒下血。便前出血，或便后出血，或粪中带血，以及痔疮出血，血色鲜红或晦暗。
【用法及注意】
【歌诀】:
槐花散用治肠风， \n侧柏荆芥枳壳充， \n为末等分米饮下， \n宽肠凉血逐风功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('140', '黄土汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__止血'), '', 'HTT', '【功用】温阳健脾，养血止血。
【主治】阳虚便血。大便下血，先便后血，或吐血、衄血，及妇人崩漏，血色暗淡，四肢不温，面色萎黄，舌淡苔白，脉沉细无力者。
【用法及注意】
【歌诀】:
黄土汤用芩地黄， \n术附阿胶甘草尝， \n温阳健脾能摄血， \n便血崩漏服之康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('141', '川芎茶调散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'CXTTS', '【功用】疏风止痛。
【主治】风邪头痛。偏正头痛或巅顶作痛，恶寒发热，目眩鼻塞，舌苔薄白，脉浮者。
【用法及注意】
【歌诀】:
川芎茶调散荆防， \n辛芷薄荷甘草羌， \n目昏鼻塞风攻上， \n正偏头痛悉能康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('142', '独活寄生汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'DHJST', '【功用】祛风湿，止痹痛，益肝肾，补气血。
【主治】痹证日久，肝肾两虚，气血不足证。腰膝疼痛，肢节屈伸不利，或麻木不仁，畏寒喜温，心悸气短，舌淡苔白，脉细弱。
【用法及注意】
【歌诀】:
独活寄生艽防辛， \n芎归地芍桂苓均， \n杜仲牛膝人参草， \n冷风顽痹屈能伸。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('143', '大秦艽汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'DQJT', '【功用】祛风清热，养血活血。
【主治】风邪初中经络证。口眼喎斜，舌强不能言语，手足不能运动，风邪散见，不拘一经者。
【用法及注意】
【歌诀】:
大秦艽汤羌独防， \n芎芷辛芩二地黄， \n石膏归芍苓甘术， \n风邪散见可通尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('144', '小活络丹', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'XHLD', '【功用】祛风除湿，化痰通络，活血止痛。
【主治】风寒湿痹。肢体筋脉疼痛，麻木拘挛，关节屈伸不利，疼痛游走不定。亦治中风，手足不仁，日久不愈，经络中湿痰瘀血，而见腰腿沉重，或腿臂间作痛。
【用法及注意】
【歌诀】:
小活络丹天南星， \n二乌乳没与地龙， \n寒湿瘀血成痹痛， \n搜风活血经络通。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('145', '牵正散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'QZS', '【功用】祛风化痰止痉。
【主治】风中经络，口眼喎斜。
【用法及注意】
【歌诀】:
牵正散是杨家方， \n全蝎僵蚕白附襄， \n服用少量热酒下， \n口眼歪斜疗效彰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('146', '玉真散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__疏散外风'), '', 'YZS', '【功用】祛风定搐。
【主治】破伤风。牙关紧急，口撮唇紧，身体强直，角弓反张，甚则咬牙缩舌。
【用法及注意】
【歌诀】:
玉真散治破伤风， \n牙关紧急反张弓， 星麻白附羌防芷， \n外敷内服一方通。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('147', '羚角钩藤汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__平熄内风'), '', 'LYGTT,NYGTT', '【功用】凉肝熄风，增液舒筋。
【主治】肝热生风证。高热不退，烦闷躁扰，手足抽搐，发为惊厥，甚则神昏，舌绛而干，或舌焦起刺，脉弦而数。
【用法及注意】
【歌诀】:
俞氏羚角钩藤汤， \n桑菊茯神鲜地黄， \n贝草竹茹同芍药， \n肝风内动急煎尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('148', '镇肝熄风汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__平熄内风'), '', 'ZGXFT', '【功用】镇肝熄风，滋阴潜阳。
【主治】类中风。头目眩晕，目胀耳鸣，脑部热痛，心中烦热，面色如醉，或时常噫气，或肢体渐觉不利，口角渐形歪斜；甚或眩晕颠仆，昏不知人，移时始醒；或醒后不能复原，脉弦长有力者。
【用法及注意】
【歌诀】:
镇肝熄风芍天冬， \n玄参牡蛎赭茵供， \n麦龟膝草龙川楝， \n肝风内动有奇功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('149', '天麻钩藤饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__平熄内风'), '', 'TMGTY', '【功用】平肝熄风，清热活血，补益肝肾。
【主治】肝阳偏亢，肝风上扰证。头痛、眩晕，失眠，舌红苔黄，脉弦。
【用法及注意】
【歌诀】:
天麻钩藤石决明， \n杜仲牛膝桑寄生， \n栀子黄芩益母草， \n茯神夜交安神宁。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('150', '杏苏散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__轻宣外燥'), '', 'XSS', '【功用】轻宣凉燥，理肺化痰。
【主治】外感凉燥证。头微痛，恶寒无汗，咳嗽痰稀，鼻塞咽干，苔白，脉弦。
【用法及注意】
【歌诀】:
杏苏散内夏陈前， \n枳桔苓草姜枣研， \n轻宣温润治凉燥， \n咳止痰化病自痊。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('151', '桑杏汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__轻宣外燥'), '', 'SXT', '【功用】轻宣温燥。
【主治】外感温燥证。头痛，身热不甚，口渴咽干鼻燥，干咳无痰，或痰少而粘，舌红，苔薄白而干，脉浮数而右脉大者。
【用法及注意】
【歌诀】:
桑杏汤中象贝宣， \n沙参栀豉与梨皮， \n干咳鼻燥右脉大， \n辛凉甘润燥能医。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('152', '清燥救肺汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__轻宣外燥'), '', 'QZJFT', '【功用】清燥润肺。
【主治】温燥伤肺证。头痛身热，干咳无痰，气逆而喘，咽喉干燥，口渴鼻燥，胸膈满闷，舌干少苔，脉虚大而数。
【用法及注意】
【歌诀】:
清燥救肺参草杷， \n石膏胶杏麦胡麻， \n经霜收下冬桑叶， \n清燥救肺效可夸。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('153', '麦门冬汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__滋阴润燥'), '', 'MMDT', '【功用】润肺益胃，降逆下气。
【主治】肺痿。咳唾涎沫，短气喘促，咽喉干燥，舌干红少苔，脉虚数。
【用法及注意】
【歌诀】:
麦门冬汤用人参， \n枣草粳米半夏存， \n肺痿咳逆因虚火， \n益胃生津此方珍。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('154', '养阴清肺汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__滋阴润燥'), '', 'YYQFT', '【功用】养阴清肺，解毒利咽。
【主治】白喉。喉间起白如腐，不易拭去，咽喉肿痛，初起或发热或不发热，鼻干唇燥，或咳或不咳，呼吸有声，似喘非喘，脉数无力或细数。
【用法及注意】
【歌诀】:
养阴清肺是妙方， \n玄参草芍麦地黄， \n薄荷贝母丹皮入， \n时疫白喉急煎尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('155', '玉液汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__滋阴润燥'), '', 'YYT', '【功用】益气滋阴，固肾止渴。
【主治】消渴。口常干渴，饮水不解，小便数多，困倦气短，脉虚细无力。
【用法及注意】
【歌诀】:
玉液山药芪葛根， \n花粉知味鸡内金， \n消渴口干溲多数， \n补脾固肾益气阴。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('156', '增液汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__滋阴润燥'), '', 'ZYT', '【功用】增液润燥。
【主治】阳明温病，津亏便秘证。大便秘结，口渴，舌干红，脉细数或沉而无力者。
【用法及注意】
【歌诀】:
增液玄参与麦冬， \n热病津枯便不通， \n补药之体作泻剂， \n但非重用不为功。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('157', '平胃散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__化湿和胃'), '', 'PWS', '【功用】燥湿运脾，行气和胃。
【主治】湿滞脾胃证。脘腹胀满，不思饮食，呕吐恶心，嗳气吞酸，肢体沉重，怠惰嗜卧，常多自利，舌苔白腻而厚，脉缓。
【用法及注意】
【歌诀】:
平胃散用朴陈皮， \n苍术甘草姜枣齐， \n燥湿运脾除胀满， \n调胃和中此方宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('158', '藿香正气散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__化湿和胃'), '', 'HXZQS', '【功用】解表化湿，理气和中。
【主治】外感风寒，内伤湿滞证。霍乱吐泻，恶寒发热，头痛，脘腹疼痛，舌苔白腻，以及山岚瘴疟等。
【用法及注意】
【歌诀】:
藿香正气大腹苏， \n甘桔陈苓术朴俱， \n夏曲白芷加姜枣， \n感伤岚瘴并能驱。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('159', '茵陈蒿汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '', 'YCHT', '【功用】清热利湿退黄。
【主治】湿热黄疸。一身面目俱黄，黄色鲜明，腹微满，口中渴，小便短赤，舌苔黄腻，脉沉数等。
【用法及注意】
【歌诀】:
茵陈蒿汤治阳黄， \n栀子大黄组成方， \n栀子柏皮加甘草， \n茵陈四逆治阴黄。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('160', '八正散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '', 'BZS', '【功用】清热泻火，利水通淋。
【主治】湿热淋证。尿频尿急，溺时涩痛，淋沥不畅，尿色浑赤，甚则癃闭不通，小腹急满，口燥咽干，舌苔黄腻，脉滑数。
【用法及注意】
【歌诀】:
八正木通与车前， \n萹蓄大黄滑石研， \n草梢瞿麦兼栀子， \n煎加灯草痛淋蠲。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('161', '三仁汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '', 'SRT', '【功用】宣畅气机，清利湿热。
【主治】湿温初起及暑温夹湿。头痛恶寒，身重疼痛，面色淡黄，胸闷不饥，午后身热，苔白不渴，脉弦细而濡。
【用法及注意】
【歌诀】:
三仁汤蔻薏苡仁， \n朴夏白通滑竹伦， \n水用甘澜扬百遍， \n湿温初起法堪尊。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('162', '甘露消毒丹', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '普济解毒丹', 'GLXDD,PJJDD(普济解毒丹)', '【功用】利湿化浊，清热解毒。
【主治】湿温时疫。发热倦怠，胸闷腹胀，肢痠咽肿，身目发黄，颐肿口渴，小便短赤，泄泻淋浊等，舌苔淡白或厚腻或干黄。并主水土不服。
【用法及注意】
【歌诀】:
甘露消毒蔻藿香， \n茵陈滑石木通菖， \n芩翘贝母射干薄， \n湿温时疫是主方。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('163', '连朴饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '', 'LPY', '【功用】清热化湿，理气和中。
【主治】湿热霍乱。上吐下泻，胸脘痞闷，心烦躁扰，小便短赤，舌苔黄腻，脉滑等。
【用法及注意】
【歌诀】:
连朴饮用香豆豉， \n菖蒲半夏焦山栀， \n芦根厚朴黄连入， \n湿热霍乱此方施。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('164', '当归拈痛汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '拈痛汤', 'DGNTT,DGLTT,NTT(拈痛汤),LTT(拈痛汤)', '【功用】利湿清热，疏风止痛。
【主治】湿热相搏，外受风邪证。遍身肢节烦痛，或肩背沉重，或脚气肿痛，脚膝生疮，舌苔白腻微黄，脉弦数等。
【用法及注意】
【歌诀】:
当归拈痛羌防升， \n猪泽茵陈芩葛朋， \n二术苦参知母草， \n疮疡湿热服皆应。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('165', '二妙散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热祛湿'), '', 'EMS', '【功用】清热燥湿。
【主治】湿热下注证。筋骨疼痛，或两足痿软，或足膝红肿疼痛，或湿热带下，下部湿疮等，小便短赤，舌苔黄腻者。
【用法及注意】
【歌诀】:
二妙散中苍柏兼， \n若云三妙牛膝添， \n四妙再加薏苡仁， \n湿热下注痿痹痊。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('166', '五苓散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__利水渗湿'), '', 'WLS', '【功用】利水渗湿，温阳化气。
【主治】（1）蓄水证。小便不利，头痛微热，烦渴欲饮，甚则水入即吐，舌苔白，脉浮。 \n（2）水湿内停。水肿，泄泻，小便不利，以及霍乱等。 \n（3）痰饮。脐下动悸，吐涎沫而头眩，或短气而咳者。
【用法及注意】
【歌诀】:
五苓散治太阳府， \n泽泻白术与二苓， \n温阳化气添桂枝， \n利便解表治水停。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('167', '猪苓汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__利水渗湿'), '', 'ZLT', '【功用】利水清热养阴。
【主治】水热互结证。小便不利，发热，口渴欲饮，或心烦不寐，或兼有咳嗽，呕恶，下利等，舌红苔白或微黄，脉细数者。
【用法及注意】
【歌诀】:
猪苓汤用猪茯苓， \n泽泻滑石阿胶并， \n小便不利兼烦渴， \n利水养阴热亦平。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('168', '防己黄芪汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__利水渗湿'), '', 'FJHZT', '【功用】益气祛风，健脾利水。
【主治】风水或风湿。汗出恶风，身重，小便不利，舌淡苔白，脉浮。
【用法及注意】
【歌诀】:
防己黄芪金匮方， \n白术甘草枣生姜， \n汗出恶风兼身重， \n表虚湿盛服之康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('169', '五皮散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__利水渗湿'), '', 'WPS', '【功用】利水消肿，理气健脾。
【主治】皮水。一身悉肿，肢体沉重，心腹胀满，上气喘急，小便不利，以及妊娠水肿等，苔白腻，脉缓。
【用法及注意】
【歌诀】:
五皮散用五般皮， \n陈茯姜桑大腹奇， \n或以五加易桑白， \n脾虚腹胀此方施。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('170', '苓桂术甘汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温化水湿'), '', 'LGSGT', '【功用】温阳化饮，健脾利湿。
【主治】痰饮。胸胁支满，目眩心悸，或短气而咳，舌苔白滑，脉弦滑。
【用法及注意】
【歌诀】:
苓桂术甘化饮剂， \n湿阳化饮又健脾， \n饮邪上逆胸胁满， \n水饮下行悸眩去。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('171', '甘草干姜茯苓白术汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温化水湿'), '甘姜苓术汤,肾著汤', 'GCGJFLBST,GJLST(甘姜苓术汤),SZT(肾著汤)', '【功用】祛寒除湿。
【主治】肾著病。身重腰下冷痛，腰重如带五千钱，饮食如故，口不渴，小便自利，舌淡苔白，脉沉迟或沉缓。
【用法及注意】
【歌诀】:
肾著汤内用干姜， \n茯苓甘草白术襄， \n伤湿身重与腰冷， \n亦名甘姜苓术汤。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('172', '真武汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温化水湿'), '', 'ZWT', '【功用】温阳利水。
【主治】（1）脾肾阳虚，水气内停证。小便不利，四肢沉重疼痛，腹痛下利，或肢体浮肿，苔白不渴，脉沉。 \n（2）太阳病发汗太过，阳虚水泛。汗出不解，其人仍发热，心下悸，头眩，身目闰动，振振欲擗地。
【用法及注意】
【歌诀】:
真武汤壮肾中阳， \n茯苓术芍附生姜， \n少阴腹痛有水气， \n悸眩瞤惕保安康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('173', '实脾散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温化水湿'), '', 'SPS', '【功用】温阳健脾，行气利水。
【主治】阳虚水肿。身半以下肿甚，手足不温，胸腹胀满，大便溏薄，舌苔白腻，脉沉弦而迟者。
【用法及注意】
【歌诀】:
实脾苓术与木瓜， \n甘草木香大腹加， \n草果附姜兼厚朴， \n虚寒阴水效堪夸。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('174', '萆薢分清饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '__祛湿化浊'), '', 'BXFQY,BJFQY', '【功用】温暖下元，利湿化浊。
【主治】虚寒白浊。小便频数，白如米泔，凝如膏糊，舌淡苔白，脉沉。
【用法及注意】
【歌诀】:
萆薢分清石菖蒲， \n萆薢乌药益智俱， \n或益茯苓盐煎服， \n通心固肾浊精驱。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('175', '完带汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__祛湿化浊'), '', 'WDT', '【功用】补脾疏肝，化湿止带。
【主治】脾虚肝郁，湿浊带下。带下色白，清稀如涕，肢体倦怠，舌淡苔白，脉缓或濡弱。
【用法及注意】
【歌诀】:
完带汤中用白术， \n山药人参白芍辅， \n苍术车前黑芥穗， \n陈皮甘草与柴胡。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('176', '二陈汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__燥湿化痰'), '', 'ECT', '【功用】燥湿化痰，理气和中。
【主治】湿痰咳嗽。痰多色白易咯，胸膈痞闷，恶心呕吐，肢体倦怠，或头眩心悸，舌苔白润，脉滑。
【用法及注意】
【歌诀】:
二陈汤用半夏陈， \n益以茯苓甘草臣， \n利气和中燥湿痰， \n煎加生姜与乌梅。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('177', '茯苓丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__燥湿化痰'), '', 'FLW', '【功用】燥湿行气，软坚化痰。
【主治】痰停中脘证。两臂疼痛，手不得上举，或左右时复转移，或两手疲软，或四肢浮肿，舌苔白腻，脉沉细或弦滑等。
【用法及注意】
【歌诀】:
指迷茯苓丸半夏， \n风硝枳壳姜汤下， \n中脘停痰肩臂痛， \n气行痰消痛自罢。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('178', '温胆汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__燥湿化痰'), '', 'WDT', '【功用】理气化痰，清胆和胃。
【主治】胆胃不和，痰热内扰证。胆怯易惊，虚烦不宁，失眠多梦，呕吐呃逆，癫痫等证。
【用法及注意】
【歌诀】:
温胆汤中苓半草， \n枳竹陈皮加姜枣， \n虚烦不眠证多端， \n此系胆虚痰热扰。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('179', '清气化痰丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热化痰'), '', 'QQHTW', '【功用】清热化痰，理气止咳。
【主治】痰热咳嗽。痰稠色黄，咯之不爽，胸膈痞闷，甚则气急呕恶，舌质红，苔黄腻，脉滑数。
【用法及注意】
【歌诀】:
清气化痰星夏橘， \n杏仁枳实瓜蒌实， \n芩苓姜汁糊为丸， \n气顺火消痰自失。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('180', '小陷胸汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热化痰'), '', 'XXXT', '【功用】清热化痰，宽胸散结。
【主治】痰热互结证。胸脘痞闷，按之则痛，或咳痰黄稠，舌苔黄腻，脉滑数。
【用法及注意】
【歌诀】:
小陷胸汤连夏蒌， \n宽胸开结涤痰优， \n膈上热痰痞满痛， \n舌苔黄腻服之休。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('181', '滚痰丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__清热化痰'), '', 'GTW', '【功用】泻火逐痰。 \n
【主治】实热老痰证。癫狂惊悸，或怔忡昏迷，或咳喘痰稠，或胸脘痞闷，或眩晕耳鸣，或绕项结核，或口眼蠕动，或不寐，或梦寐奇怪之状，或骨节卒痛难以名状，或噎息烦闷。大便秘结，舌苔黄腻，脉滑数有力。
【用法及注意】
【歌诀】:
滚痰丸用青礞石， \n大黄黄芩与沉香， \n百病皆因痰作祟， \n顽痰怪证力能匡。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('182', '贝母瓜蒌散', (select PK_ID from SYNDROME_SUBJECT where NAME = '__润燥化痰'), '', 'BMGLS', '【功用】润肺清热，理气化痰。
【主治】燥痰咳嗽。咯痰不爽，涩而难出，咽喉干燥，苔白而干等。
【用法及注意】
【歌诀】:
贝母瓜蒌花粉研， \n橘红桔梗茯苓添， \n呛咳咽干痰难出， \n润燥化痰病自安。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('183', '苓甘五味姜辛汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__温化寒痰'), '', 'LGWWJXT', '【功用】温肺化饮。
【主治】寒饮咳嗽。咳痰量多，清稀色白，胸膈不快，舌苔白滑，脉弦滑等。
【用法及注意】
【歌诀】:
苓甘五味姜辛汤， \n温阳化饮常用方， \n半夏杏仁均可入， \n寒痰冷饮保安康。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('184', '半夏白术天麻汤', (select PK_ID from SYNDROME_SUBJECT where NAME = '__化痰熄风'), '', 'BXBSTMT', '【功用】燥湿化痰，平肝熄风。
【主治】风痰上扰证。眩晕头痛，胸闷呕恶，舌苔白腻，脉弦滑等。
【用法及注意】
【歌诀】:
半夏白术天麻汤， \n苓草橘红大枣姜， \n眩晕头痛风痰证， \n热盛阴亏切莫尝。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('185', '定痫丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__化痰熄风'), '', 'DXW', '【功用】涤痰熄风。
【主治】痰热痫证。忽然发作，眩仆倒地，不省高下，甚则抽搐，目斜口歪，痰涎直流，叫喊作声。亦可用于癫狂。
【用法及注意】
【歌诀】:
定痫二茯贝天麻， \n丹麦陈远菖蒲夏， \n胆星蝎蚕草竹沥， \n姜汁琥珀与朱砂。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('186', '保和丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__消食化滞'), '', 'BHW', '【功用】消食和胃。
【主治】食积。脘腹痞满胀痛，嗳腐吞酸，恶食呕吐，或大便泄泻，舌苔厚腻，脉滑等。
【用法及注意】
【歌诀】:
保和神曲与山植, \n苓夏陈翘菔子加, \n炊饼为丸白汤下, \n消食和胃效堪夸。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('187', '枳实导滞丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__消食化滞'), '', 'ZSDZW', '【功用】消食导滞，清热祛湿。
【主治】湿热食积。脘腹胀痛，下痢泄泻，或大便秘结，小便短赤，舌苔黄腻，脉沉有力。
【用法及注意】
【歌诀】:
枳实导滞首大黄, \n芩连白术茯苓襄, \n泽泻蒸饼糊丸服, \n湿热积滞力能攘。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('188', '木香槟榔丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__消食化滞'), '', 'MXBLW', '【功用】行气导滞，攻积泄热。
【主治】痢疾，食积。赤白痢疾，里急后重；或食积内停，脘腹胀满，大便秘结，舌苔黄腻，脉沉实。
【用法及注意】
【歌诀】:
木香槟榔青陈皮, \n枳柏黄连莪术齐, \n大黄黑丑兼香附, \n痢疾后重热滞宜。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('189', '健脾丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__健脾消食'), '', 'JPW', '【功用】健脾和胃，消食止泻。
【主治】脾虚停食证。食少难消，脘腹痞闷，大便溏薄，苔腻微黄，脉象虚弱。
【用法及注意】
【歌诀】:
健脾参术苓草陈, \n肉楚香连合砂仁, \n植肉山药曲麦炒, \n消补兼施比方寻。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('190', '肥儿丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '__健脾消食'), '', 'FEW', '【功用】健脾消食，清热驱虫。
【主治】小儿疳积。消化不良，面黄体瘦，肚腹胀满，发热口臭，大便溏薄，以及虫积腹痛。
【用法及注意】
【歌诀】:
肥儿丸内用使君, \n豆蔻香连曲麦槟, \n猪胆为丸热水下, \n虫疳食积一扫清。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('191', '乌梅丸', (select PK_ID from SYNDROME_SUBJECT where NAME = '驱虫'), '', 'WMW', '【功用】温脏安蛔。
【主治】蛔厥证。腹痛时作，心烦呕吐，时发时止，常自吐蛔，手足厥冷。亦治久痢久泻。
【用法及注意】
【歌诀】:
乌梅丸用细辛桂, \n黄连黄柏及当归, \n人参椒姜加附子, \n清上温下又安蛔。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('192', '瓜蒂散', (select PK_ID from SYNDROME_SUBJECT where NAME = '涌吐'), '', 'GDS', '【功用】涌吐痰涎宿食。
【主治】痰涎宿食，壅滞胸脘证。胸中痞鞕，懊憹不安，欲吐不出，气上冲咽喉不得息，寸脉微浮者。
【用法及注意】
【歌诀】:
瓜蒂散中赤小豆, \n豆豉汁调酸苦凑, \n逐邪涌吐功最捷, \n胸脘痰食服之瘳。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('193', '救急稀涎散', (select PK_ID from SYNDROME_SUBJECT where NAME = '涌吐'), '', 'JJXXS', '【功用】开关涌吐。
【主治】中风闭证。痰涎壅盛，喉中痰声漉漉，气闭不通，心神瞀闷，四肢不收，或倒仆不省，或口角似歪，脉滑实有力者。亦治喉痹。
【用法及注意】
【歌诀】:
稀涎皂角与白矾, \n痰浊壅阻宜开关, \n中风痰闭口不语, \n涌吐通关气自还。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('194', '盐汤探吐方', (select PK_ID from SYNDROME_SUBJECT where NAME = '涌吐'), '', 'YTTTF', '【功用】涌吐宿食。
【主治】宿食。饮食停留胃中，脘腹胀疼不舒。或干霍乱，欲吐不得吐，欲泻不得泻。
【用法及注意】
【歌诀】:
盐汤探吐千金方, \n干霍乱证宜急尝, \n宿食填脘气机阻, \n运用及时效最良。
');
insert into RX_RECIPE (PK_ID, NAME, SYNDROME_ID, ALIAS, KEY_CODES, DESCRIPTION) values ('195', '参芦饮', (select PK_ID from SYNDROME_SUBJECT where NAME = '涌吐'), '', 'SLY', '【功用】涌吐痰涎。
【主治】虚弱之人，痰涎壅盛。胸膈满闷，温温欲吐，脉象虚弱者。
【用法及注意】
【歌诀】:
参芦饮是丹溪方, \n竹沥新加效更良, \n气虚体弱痰壅盛, \n服此得吐自然康。
');
