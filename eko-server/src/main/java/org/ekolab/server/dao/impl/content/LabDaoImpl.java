package org.ekolab.server.dao.impl.content;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.ClassUtils;
import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTestHomeWorkQuestion;
import org.ekolab.server.model.content.LabTestQuestion;
import org.ekolab.server.model.content.LabTestQuestionVariantWithAnswers;
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

import static org.ekolab.server.db.h2.public_.Tables.LAB_TEST_HOME_WORK_QUESTION;
import static org.ekolab.server.db.h2.public_.Tables.LAB_TEST_QUESTION;
import static org.ekolab.server.db.h2.public_.Tables.LAB_TEST_QUESTION_VARIANT;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabDaoImpl<T extends LabData> implements LabDao<T> {
    @Autowired
    protected DSLContext dsl;

    @Override
    public Collection<LabTestQuestion> getTestQuestions(Locale locale) {
        Map<Integer, LabTestQuestion> questions = new TreeMap<>();
        Result<Record> variantRecords =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_QUESTION_VARIANT).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_QUESTION_VARIANT.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetch();

        Result<Record> homeWorkRecords =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_HOME_WORK_QUESTION).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_HOME_WORK_QUESTION.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetch();

        for (Record record : Iterables.concat(variantRecords, homeWorkRecords)) {
            LabTestQuestion question = new LabTestQuestion();
            question.setQuestionNumber(record.get(LAB_TEST_QUESTION.QUESTION_NUMBER));
            question.setTitle(record.get(LAB_TEST_QUESTION.QUESTION_TITLE));
            question.setVariants(new ArrayList<>());
            questions.put(question.getQuestionNumber(), question);
        }

        for (Record record : variantRecords) {
            int number = record.get(LAB_TEST_QUESTION.QUESTION_NUMBER);
            LabTestQuestionVariantWithAnswers questionVariant = new LabTestQuestionVariantWithAnswers();
            questionVariant.setNumber(number);
            questionVariant.setQuestion(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_TEXT));
            questionVariant.setAnswers(Arrays.stream(record.get(LAB_TEST_QUESTION_VARIANT.ANSWERS)).map(Object::toString).collect(Collectors.toList()));
            questionVariant.setRightAnswer(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_ANSWER_NUMBER));
            questionVariant.setImage(record.get(LAB_TEST_QUESTION_VARIANT.IMAGE));
            questions.get(number).getVariants().add(questionVariant);
        }

        for (Record record : homeWorkRecords) {
            int number = record.get(LAB_TEST_QUESTION.QUESTION_NUMBER);
            LabTestHomeWorkQuestion homeWorkQuestion = new LabTestHomeWorkQuestion();
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
}
