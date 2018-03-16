-- тесты по 3 лабораторной
INSERT INTO lab_test_question (id, version, lab_number, question_title, question_number) VALUES
  (20, 0, 2, 'Основные понятия и характеристики шума', 1),
  (21, 0, 2, 'Шумовое воздействие ТЭС на окружающую среду', 2),
  (22, 0, 2, 'Расчет уровня шума от энергетических объектов', 3),
  (23, 0, 2, 'Работа с шумомером Алгоритм-01', 4),
  (24, 0, 2, 'Глушители шума энергетического оборудования', 5),
  (25, 0, 2, 'Определение верхней/нижней границы для среднегеометрической частоты октавной полосы', 6),
  (26, 0, 2, 'Расчет звуковой мощности агрегата', 7),
  (27, 0, 2, 'Определение очередности шумоглушения', 8),
  (28, 0, 2, 'Допустимое время нахождения в шумном помещении', 9),
  (29, 0, 2, 'Влияние геометрических размеров помещения', 10);


INSERT INTO lab_test_question_variant (question_id, version, image, question_text, question_variant, answers, question_answer_number)
VALUES
  -- 1 вопрос
  (20, 0, NULL,
   'В каком диапазоне частот слышит человек?',
   1,
   ('20 кГц - 20 МГц',
    '1 мГц - 100 мГц',
    '15 Гц - 15 000 Гц',
    '20 Гц - 20 000 Гц',
   ), 4),
  (20, 0, NULL,
   'Как называется звук с частотой ниже 20 Гц?',
   2,
   ('Шум',
    'Инфразвук',
    'Ультразвук',
    'Гиперзвук',
   ), 2),
  (20, 0, NULL,
   'Как называется звук с частотой выше 20 000 Гц?',
   3,
   ('Шум',
    'Инфразвук',
    'Ультразвук',
    'Гиперзвук',
   ), 3),
  (20, 0, NULL,
   'Как называется случайное сочетание звуков различной интенсивности и частоты?',
   4,
   ('Шум',
    'Инфразвук',
    'Ультразвук',
    'Гиперзвук',
   ), 1),
  (20, 0, NULL,
   'Как называются упругие волны, распространяющиеся в упругой среде, вызванные каким-либо источником?',
   5,
   ('Звуковое давление',
    'Звуковая мощность',
    'Звуковое поле',
    'Звук',
   ), 4),

  -- 2 вопрос
  (21, 0, NULL,
   'Работающий в условиях длительного шумового воздействия НЕ испытывает:',
   1,
   ('Головную боль',
    'Тошноту',
    'Повышенную утомляемость',
    'Нарушение сна',
   ), 2),
  (21, 0, NULL,
   'Шумовая болезнь является следствием:',
   2,
   ('Кратковременного воздействия шума, уровень которого превышает допустимые значения',
    'Долговременного воздействия шума, уровень которого превышает допустимые значения',
    'Кратковременного воздействия шума, уровень которого не превышает допустимые значения',
    'Долговременного воздействия шума, уровень которого не превышает допустимые значения',
   ), 2),
  (21, 0, NULL,
   'Зависит ли воздействие шума на человека от частотного состава шума (спектра)?',
   3,
   ('Да',
    'Нет',
   ), 1),
  (21, 0, NULL,
   'Воздействие шума на организм человека зависит от:',
   4,
   ('Уровня звукового давления',
    'Уровня звука',
    'Частотного состава шума',
    'От всех перечисленных факторов',
   ), 4),

  -- 3 вопрос
  (22, 0, 'content/lab2/test/3.png',
   'С помощью какой формулы рассчитывается среднегеометрическая частота октавной полосы?',
   1,
   ('1',
    '2',
    '3',
    '4',
   ), 1),
  (22, 0, NULL,
   'Какое количество октавных полос существует в слышимом спектре человека?',
   2,
   ('7',
    '8',
    '9',
    '10',
   ), 2),
  (22, 0, NULL,
   'В чем измеряется уровень звуковой мощности?',
   3,
   ('Па',
    'Вт',
    'дБ',
    'Безразмерная величина',
   ), 3),
  (22, 0, NULL,
   'В чем измеряется уровень звукового давления?',
   4,
   ('Па',
    'дБ',
    'мм.рт.ст.',
    'Безразмерная величина',
   ), 2),
  (22, 0, NULL,
   'На подавление каких типов оксидов азота направлен впрыск воды (или сжигание водомазутной эмульсии) в ядро факела?',
   5,
   ('<p>f<sub>ср</sub>=<sub>⎷ </sub> <span>f<sub>н</sub>f<sub>х</sub></span></p>',
    'Топливных оксидов азота',
    'Быстрых оксидов азота',
    'Всех перечисленных типов'
   ), 1),

  -- 4 вопрос
  (23, 0, NULL,
   'Какой элемент шумомера преобразует акустическую энергию звука в электрический сигнал?',
   1,
   ('Измерительный усилитель',
    'Частотный фильтр',
    'Микрофон',
    'Все перечисленные элементы',
   ), 3),
  (23, 0, NULL,
   'Какой показатель измеряется с помощью шумомера?',
   2,
   ('Уровень звука',
    'Уровень звукового давления',
    'Уровень звуковой мощности',
    'Все перечисленные',
   ), 2),
  (23, 0, NULL,
   'Какой из перечисленных элементов отсутствует в шумомере?',
   3,
   ('Измерительный усилитель',
    'Микрофон',
    'Частотный фильтр',
    'Присутствуют все перечисленные элементы',
   ), 4),

  -- 5 вопрос
  (24, 0, NULL,
   'С помощью какой формулы вычисляется радиус полусферы?
( R- радиус полусферы [м]; l - самый большой геометрический параметр оборудования [м])',
   1,
   ('R=l/2',
    'R=l',
    'R=l/2+1',
    'R=l+1',
   ), 3),
  (24, 0, NULL,
   'На каком расстоянии от поверхности пола производятся измерения?',
   2,
   ('1 м',
    '1,5 м',
    '0,5 м',
    '2 м',
   ), 2),
  (24, 0, NULL,
   'На каком расстоянии от объекта исследования (оборудования) производятся измерения?',
   3,
   ('1 м',
    '1,5 м',
    '0,5 м',
    '2 м',
   ), 1),
  (24, 0, NULL,
   'В скольких точках производятся измерения количественных характеристик шума от паровой турбины?',
   4,
   ('2',
    '4',
    '6',
    '8',
   ), 4),
  (24, 0, NULL,
   'В скольких точках производятся измерения количественных характеристик шума от питательного насоса?',
   5,
   ('2',
    '4',
    '6',
    '8',
   ), 2),
  (24, 0, NULL,
   'В скольких точках производятся измерения количественных характеристик шума от дутьевого вентилятора?',
   6,
   ('2',
    '4',
    '6',
    '8',
   ), 2),
  -- 6 вопрос
  (25, 0, NULL,
   'Какой из перечисленных способов уменьшения воздействия шума на окружающую среду НЕ относится к основным?',
   1,
   ('Снижение шума в самом источнике',
    'Снижение шума на путях его распространения',
    'Индивидуальные средства защиты',
    'Архитектурно-планировочный способ снижении шума',
   ), 4),
  (25, 0, NULL,
   'Какой из основных способов уменьшения воздействия шума на окружающую среду является наиболее экономически затратным и малоосуществимым?',
   2,
   ('Снижение шума в самом источнике',
    'Снижение шума на путях его распространения',
    'Индивидуальные средства защиты',
    'Архитектурно-планировочный способ снижении шума',
   ), 1),
  (25, 0, NULL,
   'Какой из основных способов уменьшения воздействия шума на окружающую среду является наиболее эффективным?',
   3,
   ('Снижение шума в самом источнике',
    'Снижение шума на путях его распространения',
    'Индивидуальные средства защиты',
    'Архитектурно-планировочный способ снижении шума',
   ), 2),
  (25, 0, NULL,
   'Мероприятия по снижению шума на путях его распространения применяются на:',
   4,
   ('Вновь строящихся объектах',
    'На существующих объектах',
    'Как на вновь строящихся, так и на существующих объектах',
   ), 3),
  (25, 0, NULL,
   'Какой из перечисленных способов уменьшения воздействия шума на окружающую среду применяется только на стадии проектирования энергообъекта?',
   5,
   ('Снижение шума в самом источнике',
    'Снижение шума на путях его распространения',
    'Индивидуальные средства защиты',
    'Архитектурно-планировочный способ снижении шума',
   ), 4),
  -- 7 вопрос
  (26, 0, NULL,
   'При увеличении объема помещения в 2 раза и неизменном частотном множителе, постоянная помещения:',
   1,
   ('Увеличится на 100%',
    'Уменьшится на 100%',
    'Увеличится на 50%',
    'Уменьшится на 50%',
   ), 1),
  (26, 0, NULL,
   'Корректирующая поправка учитывает влияние:',
   2,
   ('Объема помещения',
    'Барометрического давления',
    'Типа оборудования',
    'Всех перечисленных факторов',
   ), 2),
  (26, 0, NULL,
   'Показатель измерительной поверхности зависит от:',
   3,
   ('Радиуса полусферы',
    'Объема помещения',
    'Высоты помещения',
    'От всех перечисленных факторов',
   ), 1),
  (26, 0, NULL,
   'При уменьшении поверхности полусферы уровень звуковой мощности:',
   3,
   ('Увеличится',
    'Уменьшится',
    'Останется неизменным',
    'Невозможно определить однозначно',
   ), 2);

INSERT INTO lab_test_home_work_question (question_id, version, image, question_text, question_variant, value_type, dimension, formulae)
VALUES
  -- 8 вопрос
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 96 дБ. Площадь измерительной поверхности ' ||
   'равна 830 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 </sup></i>',
   1,
   'int', NULL,
   '96-Math.log(830)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 96,2 дБ. Площадь измерительной поверхности ' ||
   'равна 830 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   2,
   'int', NULL,
   '96,2-Math.log(830)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 96,2 дБ. Площадь измерительной поверхности ' ||
   'равна 830 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   3,
   'int', NULL,
   '96,2-Math.log(830)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 96,4 дБ. Площадь измерительной поверхности ' ||
   'равна 830 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   4,
   'int', NULL,
   '96,4-Math.log(830)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 96,6 дБ. Площадь измерительной поверхности ' ||
   'равна 860 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности. <br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   5,
   'int', NULL,
   '96,6-Math.log(860)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 100 дБ. Площадь измерительной поверхности ' ||
   'равна 840 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности. <br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   6,
   'int', NULL,
   '100-Math.log(840)/Math.log(10)'),
  (27, 0, NULL,
   'Уровень звуковой мощности от корпуса энергетического оборудования  ' ||
   'на расстоянии 1 м составляет 102 дБ. Площадь измерительной поверхности ' ||
   'равна 850 м<sup>2</sup>. S<sub>0</sub>=1м<sup>2</sup>.<br>' ||
   'Определить Уровень звукового давления на измерительной поверхности.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 209 Дб</sup></i>',
   6,
   'int', NULL,
   '102-Math.log(850)/Math.log(10)'),

  -- 9 вопрос
  (28, 0, NULL,
   'Для расчетной среднегеометрической частоты определить уровень звукового давления в помещении' ||
   'в зоне отраженного звука при увеличении объема расчетного помещения на 50%.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 117 Дб</sup></i>',
   6,
   'int', NULL,
   'soundPowerLevel + 10.0*Math.log(quantityOfSingleTypeEquipment)/Math.log(10) - 10.0*Math.log(1.5*frequencyCoefficient*roomSize/20.0)/Math.log(10) + 6'),

  -- 10 вопрос
  (29, 0, NULL,
   'Для расчетной среднегеометрической частоты определить уровень звукового' ||
   'давления в помещении в зоне отраженного звука при уменьшении объема расчетного помещения на 15%.<br>' ||
   'Ответ округлить до целых.<br>' ||
   '<i>Пример: 117 Дб</sup></i>',
   6,
   'int', NULL,
   'soundPowerLevel + 10.0*Math.log(quantityOfSingleTypeEquipment)/Math.log(10) - 10.0*Math.log(0.85*frequencyCoefficient*roomSize/20.0)/Math.log(10) + 6');