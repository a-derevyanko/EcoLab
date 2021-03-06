package org.ecolab.server.dao.impl.content;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ClassUtils;
import org.ecolab.server.dao.api.content.LabDao;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabTestHomeWorkQuestion;
import org.ecolab.server.model.content.LabTestQuestion;
import org.ecolab.server.model.content.LabTestQuestionVariantWithAnswers;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.ecolab.server.db.h2.public_.Tables.LAB_TEST_HOME_WORK_QUESTION;
import static org.ecolab.server.db.h2.public_.Tables.LAB_TEST_QUESTION;
import static org.ecolab.server.db.h2.public_.Tables.LAB_TEST_QUESTION_VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabDaoImpl<T extends LabData> implements LabDao<T> {
    protected final DSLContext dsl;

    @Autowired
    public LabDaoImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Collection<LabTestQuestion> getTestQuestions(Locale locale) {
        Map<Integer, LabTestQuestion> questions = new TreeMap<>();
        var variantRecords =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_QUESTION_VARIANT).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_QUESTION_VARIANT.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetch();

        var homeWorkRecords =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_HOME_WORK_QUESTION).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_HOME_WORK_QUESTION.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetch();

        for (var record : Iterables.concat(variantRecords, homeWorkRecords)) {
            var question = new LabTestQuestion();
            question.setQuestionNumber(record.get(LAB_TEST_QUESTION.QUESTION_NUMBER));
            question.setPointCount(record.get(LAB_TEST_QUESTION.POINT_COUNT));
            question.setTitle(record.get(LAB_TEST_QUESTION.QUESTION_TITLE));
            question.setVariants(new ArrayList<>());
            questions.put(question.getQuestionNumber(), question);
        }

        for (var record : variantRecords) {
            int number = record.get(LAB_TEST_QUESTION.QUESTION_NUMBER);
            var questionVariant = new LabTestQuestionVariantWithAnswers();
            questionVariant.setNumber(number);
            questionVariant.setQuestion(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_TEXT));
            questionVariant.setAnswers(Arrays.stream(record.get(LAB_TEST_QUESTION_VARIANT.ANSWERS)).map(Object::toString).collect(Collectors.toList()));
            questionVariant.setRightAnswer(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_ANSWER_NUMBER));
            questionVariant.setImage(record.get(LAB_TEST_QUESTION_VARIANT.IMAGE));
            questions.get(number).getVariants().add(questionVariant);
        }

        for (var record : homeWorkRecords) {
            int number = record.get(LAB_TEST_QUESTION.QUESTION_NUMBER);
            var homeWorkQuestion = new LabTestHomeWorkQuestion();
            homeWorkQuestion.setNumber(number);
            homeWorkQuestion.setQuestion(record.get(LAB_TEST_HOME_WORK_QUESTION.QUESTION_TEXT));
            homeWorkQuestion.setImage(record.get(LAB_TEST_HOME_WORK_QUESTION.IMAGE));
            homeWorkQuestion.setFormulae(record.get(LAB_TEST_HOME_WORK_QUESTION.FORMULAE));
            homeWorkQuestion.setDimension(record.get(LAB_TEST_HOME_WORK_QUESTION.DIMENSION));
            try {
                homeWorkQuestion.setValueType(
                        ClassUtils.primitiveToWrapper(ClassUtils.getClass(ClassLoader.getSystemClassLoader(),
                        record.get(LAB_TEST_HOME_WORK_QUESTION.VALUE_TYPE), true)));
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }

            questions.get(number).getVariants().add(homeWorkQuestion);
        }

        return questions.values();
    }

    /**
     * Возвращает номер лабораторной
     * @return номер лабораторной
     */
    protected abstract int getLabNumber();

    protected abstract void fillLabUsers(T data);

    protected abstract void saveLabUsers(T data);
}
