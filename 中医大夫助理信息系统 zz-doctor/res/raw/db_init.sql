-- RX_RECIPE table
insert into RX_RECIPE 
			(PK_ID, NAME, ALIAS, KEY_CODES, DESCRIPTION, ORIGIN) 
---- （一）解表剂:
-------- 辛温解表:
      select '01101', '麻黄汤', null, 'MHT', '［功用］：发汗解表，宣肺平喘。 \n［适应症］：感冒风寒，怕冷发热，无汗，咳嗽气喘。', '《伤寒论》'
union select '01102', '三拗汤', null, 'SLT', '［功用］：宣肺平喘，止咳。 \n［适应症］：感冒风寒，咳嗽气喘。', '《和剂局方》' 
-- TODO? 01103, MGSGT,DQLT
-- TODO? 01104, MHFZXXT
union select '01105', '桂枝汤', null, 'GZT', '［功用］：解肌发表，调和营卫。 \n［适应症］：外感风邪，头痛发热，汗出恶风；时寒时热，营卫不和，脉缓有汗，舌苔白滑。', '《伤寒论》' 
-- TODO? 01106, CQT,CDT
-- TODO? 01107, CJHTT
-- TODO? 01108, CBQWY
-- TODO? 01109, CQJGT,CDJGT
union select '01110', '荆防败毒散', null, 'JFBDS', '［功用］：解表邪，祛风湿，止疼痛。 \n［适应症］：流感，感冒初起，怕冷、发热，无汗，头痛和肌肉痠痛。', '《摄生众妙方》'
-- TODO? ...
--
-------- 辛凉解表:
union select '01201', '银翘散', null, 'YCS', '［功用］：辛凉透表，清热解毒。 \n［适应症］：流感，感冒初起，及其他感染引起的发热，咳嗽，咽痛，有头痛，怕冷，苔薄白，或舌边尖微红，脉浮数等表热症。', '《温病条辨》'
-- TODO? ...
--
---- （二）清热剂:
-------- 清气分热:
union select '02101', '石膏知母汤', '白虎汤', 'SGZMT,BHT-白虎汤', '［功用］：清气热, 泻胃火, 生津止渴。 \n［适应症］：外感热病, 气分热盛, 见高热,烦躁,口渴欲饮,面红,大汗出,恶热,脉洪大或滑数; 以及胃火旺的头痛,齿痛,鼻(血丑),牙龈出血等症.', '《伤寒论》'
-- TODO? ...
-------- 清营凉血:
union select '02201', '犀角地黄汤', null, 'XJDHT', '［功用］：清热解毒, 凉血散瘀。 \n［适应症］：外感热病, 热入营血, 高热, 神志不清, 舌质红绛, 脉细数; 或发斑疹, 或血热妄行, 吐(血丑),便血; 以及疔疮走黄等症.', '《千金方》'
-------- 泻火解毒:
union select '02301', '黄连解毒汤', null, 'HLJDT', '［功用］：泻火解毒, 清化湿热。 \n［适应症］：一切实热火症, 谵语昏狂, 湿热黄疸, 疔疮走黄, 热甚吐血,(血丑)血, 舌苔黄腻等症.', '《外台秘要》'
union select '02302', '普济消毒饮', null, 'PJXDY', '［功用］：疏散风热, 清热解毒。 \n［适应症］：大头瘟, 恶寒发热, 头面红肿, 目不能开, 咽喉不利, 舌燥口渴, 脉浮数有力.', '《东垣试效方》'
-------- 清脏腑热:
union select '02401', '泻心汤', null, 'XXT', '［功用］：泻火解毒, 清化湿热。 \n［适应症］：一切湿热火症. 如外感热病的高热,面红,目赤,烦躁,神昏发狂, 舌苔黄腻; 热甚迫血妄行, 吐血,(血丑)血; 疔疮走黄, 丹毒痈肿, 败血症; 眼目红肿, 口舌生疮, 湿热黄疸, 以及下痢脓血等.', '《金匮要略》'
-------- 清虚热:
union select '02501', '清蒿鳖甲汤', null, 'QHBJT', '［功用］：养阴凉血, 亲热生津。 \n［适应症］：阴虚潮热或低热. 如热病后期, 邪热未尽, 阴液已伤, 低热不退; 肺痨或其他慢性疾病过程中出现潮热骨蒸, 或其他虚热之由于阴虚火旺者.', '《温病条辨》'
--
---- （三）泻下剂:
-------- 寒下:
union select '03101', '大承气汤', null, 'DCQT', '［功用］：下燥屎, 泻实热, 除痞满, 泻火解毒。 \n［适应症］：伤寒温病, 热甚便秘, 腹部胀满, 烦躁谵语, 舌苔焦黄起刺, 脉沉实有力的阳明腑实症. 或热结旁流, 或热盛痉厥等症.', '《伤寒论》'
-------- 温下:
union select '03201', '大黄附子汤', null, 'DHFZT', '［功用］：温经散寒, 通便止痛。 \n［适应症］：阴寒积聚, 腹部胀满作痛, 便秘,肢冷,畏寒, 或见苔白, 脉沉弦.', '《金匮要略》'
-------- 润下:
union select '03301', '麻子仁丸', '脾约麻仁丸', 'MZRW,PYMRW-脾约麻仁丸', '［功用］：润肠通便。 \n［适应症］：肠胃燥热, 大便秘结, 腹中胀满, 以及痔疮便秘.', '《伤寒论》'
--
---- （四）和剂:
-------- 和解少阳:
union select '04101', '小柴胡汤', null, 'XCHT', '［功用］：和解少阳, 扶正祛邪。 \n［适应症］：邪在少阳, 寒热往来, 胸胁苦满, 不欲饮食, 心烦呕恶, 口苦咽干, 目眩, 舌苔薄白或微黄腻, 脉弦等症.', '《伤寒论》'
-------- 和理肝脾:
union select '04201', '四逆散', null, 'SNS,SLS', '［功用］：疏肝理气, 和营解郁。 \n［适应症］：热厥, 阳气内郁, 而致手足厥冷, 或胸胁脘腹疼痛, 或兼见泄泻.', '《伤寒论》'
-------- 和理胃肠:
union select '04301', '半夏泻心汤', null, 'BXXXT', '［功用］：和胃降逆, 开结散痞。 \n［适应症］：肠胃功能失调, 寒热互结, 心下痞硬, 但无痛感, 腸鸣下利, 呕恶, 不思饮食, 舌苔腻而微黄.', '《伤寒论》'
-------- 截疟:
union select '04401', '达原饮', null, 'DYY', '［功用］：辟秽浊, 开达膜原, 截虐。 \n［适应症］：疫疟,瘴疟, 发作无定时, 高热,胸闷,烦躁,口干, 舌质红, 苔白厚如积粉.', '《温疫论》'
--
---- （五）祛湿剂:
-------- 化湿浊:
union select '05101', '平胃散', null, 'PWS', '［功用］：燥湿, 健脾。 \n［适应症］：湿阻脾胃, 胸腹胀满, 食欲不振, 四肢倦怠, 大便溏薄, 舌苔白腻或厚腻而滑润.', '《和剂局方》'
-------- 利水湿:
union select '05201', '五苓散', null, 'WLS,WNS', '［功用］：通阳化气, 健脾利水。 \n［适应症］：水湿停聚, 小便不利, 水肿, 舌苔滑润, 或渴欲饮水, 水入即吐, 小便不利等症.', '《伤寒论》'
--
---- （六）祛风湿剂:
-------- 祛风通络:
union select '06101', '羌活胜湿汤', null, 'QHSST', '［功用］：祛风湿, 止疼痛。 \n［适应症］：风湿在表, 头痛头重, 腰背重痛, 或一身尽痛, 恶寒微热, 苔白脉浮者.', '《内外伤辨惑论》'
--
--
---- （十八）治疡剂:
-------- 内服:
union select '18101', '苇茎汤', null, 'WJT', '［功用］：清肺化痰, 逐瘀排脓。 \n［适应症］：肺痈, 咳吐腥臭浓痰, 胸中隐隐作痛, 脉滑数, 口干, 舌红, 苔黄腻.', '《千金方》'
-------- 外用:
union select '18201', '金黄散', '如意金黄散', 'JHS,RYJHS', '［功用］：清热解毒, 消肿止痛。 \n［适应症］：治一切阳症痈疡疮疖.', '《医宗金鉴》'
;


-- MEDICINE table
insert into MEDICINE 
			(PK_ID, NAME, KEY_CODES, DESCRIPTION) 
-- 麻黄汤, 三拗汤:
      select '0001', '麻黄', 'MH', 'TODO?'
union select '0002', '桂枝', 'GZ', 'TODO?'
union select '0003', '杏仁', 'XR', 'TODO?'
union select '0004', '甘草', 'GC', 'TODO?'
union select '10004', '甘草(生)', 'GCS,SGC', 'TODO?'
union select '20004', '甘草(炙)', 'GCZ,ZGC', 'TODO?'
-- 桂枝汤:
union select '0005', '芍药', 'SY', 'TODO?'
union select '10005', '芍药(白)', 'SYB,BS', 'TODO?'
union select '20005', '芍药(赤)', 'SYC,CS', 'TODO?'
union select '0006', '生姜', 'SJ', 'TODO?'
union select '0007', '大枣', 'DZ', 'TODO?'
-- 荆防败毒散:
union select '0008', '荆芥', 'JJ', 'TODO?'
union select '0009', '防风', 'FF', 'TODO?'
union select '0010', '羌活', 'QH', 'TODO?'
union select '0011', '独活', 'DH', 'TODO?'
union select '0012', '川芎', 'CX', 'TODO?'
union select '0013', '柴胡', 'CH', 'TODO?'
union select '0014', '前胡', 'QH', 'TODO?'
union select '0015', '桔梗', 'JG', 'TODO?'
union select '0016', '枳壳', 'ZK', 'TODO?'
union select '0017', '茯苓', 'FL', 'TODO?'
-- 银翘散:
union select '0018', '银花', 'YH', 'TODO?'
union select '0019', '连翘', 'LQ', 'TODO?'
union select '0020', '豆豉', 'DC', 'TODO?'
union select '0021', '牛蒡子', 'NPZ,NBZ,LPZ,LBZ', 'TODO?'
union select '0022', '薄荷', 'BH', 'TODO?'
union select '0024', '竹叶', 'ZY', 'TODO?'
union select '0025', '鲜芦根', 'XLG', 'TODO?'
-- 石膏知母汤 (白虎汤):
union select '0026', '石膏', 'SG', 'TODO?'
union select '0027', '知母', 'ZM', 'TODO?'
union select '0028', '粳米', 'JM', 'TODO?'
-- 犀角地黄汤:
union select '0029', '犀角', 'SJ', 'TODO?'
union select '0030', '地黄', 'DH', 'TODO?'
union select '10030', '地黄(生)', 'DHS,SDH', 'TODO?'
union select '0031', '丹皮', 'DP', 'TODO?'
-- 黄连解毒汤:
union select '0032', '黄连', 'HL', 'TODO?'
union select '0033', '黄芩', 'HQ', 'TODO?'
union select '0034', '黄柏', 'HB', 'TODO?'
union select '0035', '栀子', 'ZZ', 'TODO?'
-- 普济消毒饮:
union select '0036', '玄参', 'XC', 'TODO?'
union select '0037', '板蓝根', 'BLG', 'TODO?'
union select '0038', '马勃', 'MB', 'TODO?'
union select '0039', '僵蚕', 'JC', 'TODO?'
union select '0040', '升麻', 'SM', 'TODO?'
union select '0041', '陈皮', 'CP', 'TODO?'
-- 泻心汤:
union select '0042', '大黄', 'DH', 'TODO?'
-- 清蒿鳖甲汤:
union select '0043', '青蒿', 'QH', 'TODO?'
union select '0044', '鳖甲', 'BJ', 'TODO?'
-- 大承气汤:
union select '0045', '芒硝', 'MX', 'TODO?'
union select '0046', '厚朴', 'HP', 'TODO?'
union select '0047', '枳实', 'ZS', 'TODO?'
-- 大黄附子汤:
union select '0048', '附子', 'FZ', 'TODO?'
union select '0049', '细辛', 'XX', 'TODO?'
-- 麻子仁丸:
union select '0050', '麻子仁', 'MZR', 'TODO?'
union select '0051', '炼蜜', 'LM', 'TODO?'
-- 小柴胡汤:
union select '0052', '半夏', 'BX', 'TODO?'
union select '0053', '人参', 'RS', 'TODO?'
-- 达原饮:
union select '0054', '槟榔', 'BL', 'TODO?'
union select '0055', '草果', 'CG', 'TODO?'
-- 平胃散:
union select '0056', '苍术', 'CS', 'TODO?'
-- 五苓散:
union select '0057', '白术', 'BS', 'TODO?'
union select '0058', '猪苓', 'ZL', 'TODO?'
union select '0059', '泽泻', 'ZL', 'TODO?'
-- 羌活胜湿汤:
union select '0061', '藁本', 'GB', 'TODO?'
union select '0062', '蔓荆子', 'MJZ', 'TODO?'
-- 苇茎汤:
union select '0063', '薏苡仁', 'YYR', 'TODO?'
union select '0064', '冬瓜仁', 'DGR', 'TODO?'
union select '0065', '桃仁', 'TR', 'TODO?'
-- 金黄散:
union select '0066', '姜黄', 'JH', 'TODO?'
union select '0067', '白芷', 'BZ', 'TODO?'
union select '0068', '蓝星', 'LX', 'TODO?'
union select '0069', '川朴', 'CP', 'TODO?'
union select '0070', '天花粉', 'THF', 'TODO?'
;


-- MAP_RX_RECIPE_MEDICINE table
insert into MAP_RX_RECIPE_MEDICINE 
			(RX_RECIPE_ID, MEDICINE_ID, ORDER_NUM, QUANTITY, IS_OPTIONAL)
-- 麻黄汤:
      select '01101', '0001', '1', '6', null
union select '01101', '0002', '2', '6', null
union select '01101', '0003', '3', '9', null
union select '01101', '0004', '4', '3', null
-- 三拗汤:
union select '01102', '0001', '1', '3', null
union select '01102', '0003', '2', '3', null
union select '01102', '10004', '3', '3', null
-- 桂枝汤:
union select '01105', '0002', '1', '8', null
union select '01105', '10005', '2', '8', null
union select '01105', '0006', '3', '4', null
union select '01105', '0007', '4', '8', null
union select '01105', '20004', '5', '6', null
-- 荆防败毒散:
union select '01110', '0008', '1', '9', null
union select '01110', '0009', '2', '9', null
union select '01110', '0010', '3', '7', null
union select '01110', '0011', '4', '6', null
union select '01110', '0012', '5', '6', null
union select '01110', '0013', '6', '9', null
union select '01110', '0014', '7', '6', null
union select '01110', '0015', '8', '3', null
union select '01110', '0016', '9', '6', null
union select '01110', '0017', '10', '9', null
union select '01110', '0004', '11', '3', null
-- 银翘散:
union select '01201', '0018', '1', '3', null
union select '01201', '0019', '2', '3', null
union select '01201', '0020', '3', '2', null
union select '01201', '0021', '4', '2', null
union select '01201', '0008', '5', '1', null
union select '01201', '0022', '6', '2', null
union select '01201', '0015', '7', '2', null
union select '01201', '0004', '8', '1', null
union select '01201', '0024', '9', '1', null
union select '01201', '0025', '10', '30', null
-- 石膏知母汤 (白虎汤):
union select '02101', '0026', '1', '60', null
union select '02101', '0027', '2', '12', null
union select '02101', '0004', '3', '4', null
union select '02101', '0028', '4', '30', null
-- 犀角地黄汤:
union select '02201', '0029', '1', '2', null
union select '02201', '10030', '2', '20', null
union select '02201', '20005', '3', '9', null
union select '02201', '0031', '4', '9', null
-- 黄连解毒汤:
union select '02301', '0032', '1', '6', null
union select '02301', '0033', '2', '9', null
union select '02301', '0034', '3', '9', null
union select '02301', '0035', '4', '9', null
-- 普济消毒饮:
union select '02302', '0033', '1', '9', null
union select '02302', '0032', '2', '6', null
union select '02302', '0019', '3', '9', null
union select '02302', '0036', '4', '9', null
union select '02302', '0037', '5', '12', null
union select '02302', '0038', '6', '2', null
union select '02302', '0021', '7', '12', null
union select '02302', '0039', '8', '6', null
union select '02302', '0040', '9', '4', null
union select '02302', '0013', '10', '6', null
union select '02302', '0041', '11', '8', null
union select '02302', '0015', '12', '4', null
union select '02302', '0004', '13', '4', null
union select '02302', '0022', '14', '4', null
-- 泻心汤:
union select '02401', '0042', '1', '7', null
union select '02401', '0032', '2', '6', null
union select '02401', '0033', '3', '9', null
-- 清蒿鳖甲汤:
union select '02501', '0043', '1', '9', null
union select '02501', '0044', '2', '9', null
union select '02501', '10030', '3', '14', null
union select '02501', '0027', '4', '9', null
union select '02501', '0031', '5', '8', null
-- 大承气汤:
union select '03101', '0042', '1', '9', null
union select '03101', '0045', '2', '12', null
union select '03101', '0046', '3', '9', null
union select '03101', '0047', '4', '9', null
-- 大黄附子汤:
union select '03201', '0042', '1', '8', null
union select '03201', '0048', '2', '8', null
union select '03201', '0049', '3', '3', null
-- 麻子仁丸:
union select '03301', '0050', '1', '120', null
union select '03301', '0003', '2', '120', null
union select '03301', '0047', '3', '60', null
union select '03301', '0042', '4', '120', null
union select '03301', '0046', '5', '120', null
union select '03301', '10005', '6', '90', null
union select '03301', '0051', '7', '-1', null
-- 小柴胡汤:
union select '04101', '0013', '1', '9', null
union select '04101', '0033', '2', '7', null
union select '04101', '0052', '3', '8', null
union select '04101', '0053', '4', '3', null
union select '04101', '20004', '5', '5', null
union select '04101', '0006', '6', '4', null
union select '04101', '0007', '7', '8', null
-- 四逆散:
union select '04201', '0013', '1', '6', null
union select '04201', '10005', '2', '9', null
union select '04201', '0047', '3', '7', null
union select '04201', '0004', '4', '4', null
-- 半夏泻心汤:
union select '04301', '0052', '1', '5', null
union select '04301', '0033', '2', '5', null
union select '04301', '0006', '3', '3', null
union select '04301', '0053', '4', '3', null
union select '04301', '20004', '5', '4', null
union select '04301', '0032', '6', '3', null
union select '04301', '0007', '7', '8', null
-- 达原饮:
union select '04401', '0054', '1', '6', null
union select '04401', '0046', '2', '6', null
union select '04401', '0055', '3', '8', null
union select '04401', '0005', '4', '8', null
union select '04401', '0027', '5', '8', null
union select '04401', '0033', '6', '9', null
union select '04401', '0004', '7', '3', null
-- 平胃散:
union select '05101', '0056', '1', '5', null
union select '05101', '0046', '2', '6', null
union select '05101', '0041', '3', '5', null
union select '05101', '0004', '4', '3', null
union select '05101', '0006', '5', '3', null
union select '05101', '0007', '6', '6', null
-- 五苓散:
union select '05201', '0057', '1', '8', null
union select '05201', '0002', '2', '4', null
union select '05201', '0058', '3', '10', null
union select '05201', '0059', '4', '10', null
union select '05201', '0017', '5', '12', null
-- 羌活胜湿汤:
union select '06101', '0010', '1', '6', null
union select '06101', '0011', '2', '6', null
union select '06101', '0061', '3', '10', null
union select '06101', '0009', '4', '6', null
union select '06101', '0012', '5', '4', null
union select '06101', '0062', '6', '10', null
union select '06101', '0004', '7', '3', null
-- 苇茎汤:
union select '18101', '0025', '1', '100', null
union select '18101', '0063', '2', '20', null
union select '18101', '0064', '3', '20', null
union select '18101', '0065', '4', '9', null
-- 金黄散:
union select '18201', '0042', '1', '500', null
union select '18201', '0034', '2', '500', null
union select '18201', '0065', '3', '500', null
union select '18201', '0067', '4', '500', null
union select '18201', '0068', '5', '200', null
union select '18201', '0041', '6', '200', null
union select '18201', '0056', '7', '200', null
union select '18201', '0069', '8', '200', null
union select '18201', '0004', '9', '200', null
union select '18201', '0070', '10', '1000', null
;
