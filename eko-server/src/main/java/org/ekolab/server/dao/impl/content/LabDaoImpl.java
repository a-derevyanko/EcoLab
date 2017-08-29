package org.ekolab.server.dao.impl.content;

import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.db.h2.public_.tables.records.LabTestQuestionRecord;
import org.ekolab.server.model.content.*;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.ekolab.server.db.h2.public_.Tables.*;

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
    public Collection<LabTestQuestionVariants> getTestQuestions(Locale locale) {
        Map<LabTestQuestionRecord, LabTestQuestionVariant> variants =  dsl.selectFrom(LAB_TEST_QUESTION.join(LAB_TEST_QUESTION_VARIANT).
                on(LAB_TEST_QUESTION.ID.eq(LAB_TEST_QUESTION_VARIANT.QUESTION_ID))).
                where(LAB_TEST_QUESTION.LAB_NUMBER.eq(getLabNumber())).fetchMap(LAB_TEST_QUESTION, record -> {
            LabTestQuestionVariant questionVariant = new LabTestQuestionVariant();
            questionVariant.setQuestion(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_TEXT));
            questionVariant.setAnswers(Arrays.asList((String[])record.get(LAB_TEST_QUESTION_VARIANT.ANSWERS)));
            questionVariant.setRightAnswer(record.get(LAB_TEST_QUESTION_VARIANT.QUESTION_ANSWER_NUMBER));
            questionVariant.setImage(record.get(LAB_TEST_QUESTION_VARIANT.IMAGE));
            return questionVariant;
        });

        Map<Integer, LabTestQuestionVariants> questions = new HashMap<>();
        for (Map.Entry<LabTestQuestionRecord, LabTestQuestionVariant> questionVariant : variants.entrySet()) {
            LabTestQuestionVariants question = questions.putIfAbsent(questionVariant.getKey().getQuestionNumber(), new LabTestQuestionVariants());
            question.setTitle(questionVariant.getKey().getQuestionTitle());

            if (question.getVariants() == null) {
                question.setVariants(new ArrayList<>());
            }
            question.getVariants().add(questionVariant.getValue());
        }

        //todo загрузить вопросы ДЗ
        return questions.values();
    }

    protected abstract int getLabNumber();
}
