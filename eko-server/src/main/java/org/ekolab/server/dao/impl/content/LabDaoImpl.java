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
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.Select;
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
import static org.ekolab.server.db.h2.public_.Tables.USERS;

/**
 * Created by 777Al on 19.04.2017.
 */
public abstract class LabDaoImpl<T extends LabData> implements LabDao<T> {
    @Autowired
    protected DSLContext dsl;

    protected Select<Record1<Long>> getFindUserIdSelect(String userName) {
        return dsl.select(USERS.ID).from(USERS).where(USERS.LOGIN.eq(userName));
    }

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
            LabTestQuestionVariantWithAnswers questionVariant = new LabTestQuestionVariantWithAnswers();
            questionVariant.setQuestion(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_TEXT));
            questionVariant.setAnswers(Arrays.stream(record.get(LAB_TEST_QUESTION_VARIANT.ANSWERS)).map(Object::toString).collect(Collectors.toList()));
            questionVariant.setRightAnswer(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_ANSWER_NUMBER));
            questionVariant.setImage(record.get(LAB_TEST_QUESTION_VARIANT.IMAGE));
            questions.get(record.get(LAB_TEST_QUESTION.QUESTION_NUMBER)).getVariants().add(questionVariant);
        }

        for (Record record : homeWorkRecords) {
            LabTestHomeWorkQuestion homeWorkQuestion = new LabTestHomeWorkQuestion();
            homeWorkQuestion.setQuestion(record.get(LAB_TEST_HOME_WORK_QUESTION.QUESTION_TEXT));
            homeWorkQuestion.setImage(record.get(LAB_TEST_HOME_WORK_QUESTION.IMAGE));
            homeWorkQuestion.setFormulae(record.get(LAB_TEST_HOME_WORK_QUESTION.IMAGE));
            homeWorkQuestion.setDimension(record.get(LAB_TEST_HOME_WORK_QUESTION.DIMENSION));
            try {
                homeWorkQuestion.setValueType(
                        ClassUtils.primitiveToWrapper(ClassUtils.getClass(ClassLoader.getSystemClassLoader(),
                        record.get(LAB_TEST_HOME_WORK_QUESTION.VALUE_TYPE), true)));
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }

            questions.get(record.get(LAB_TEST_QUESTION.QUESTION_NUMBER)).getVariants().add(homeWorkQuestion);
        }

        return questions.values();
    }

    protected abstract int getLabNumber();
}
